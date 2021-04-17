package element;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zaxxer.hikari.HikariDataSource;

class SendElementsToDatabase {

	static SendElementsToDatabase singleton = new SendElementsToDatabase();
	private HikariDataSource hikari;
	private Connection con;
	private Statement stm;

	private final String DRIVER_NAME="com.mysql.jdbc.Driver";
	private final String URL = "jdbc:mysql://localhost:3306/umlds";
	private final String USER = "root";
	private final String PASWORD = "root";

	private int classNum = 1; // クラスに番号をつけるのに使う
	private int fieldNum = 1;
	private int methodNum = 1;
	private int paraNum = 1;

	private SendElementsToDatabase()
	{
		// データベースmotooへの接続
		System.out.println("SendElementsToDatabase.コンストラクタ");

//		HikariConfig config = new HikariConfig();
//		config.setDriverClassName("com.mysql.jdbc.Driver");
//		config.setJdbcUrl("jdbc:mysql://localhost:3306/umlds");
//
//		config.setUsername("root");
//		config.setPassword("root");
//
//		hikari= new HikariDataSource(config);
//
//		System.out.println("SQL接続完了");

	}

	static SendElementsToDatabase getInstance()
	{
		return singleton;
	}

	public void sendDatabase(Map<String, ActiveSourceCodeInfo> sendDatabaseinfo)
	{
		reset(); // テーブルの削除をして前の情報が残ることによるバグを対処
		deleteBeforeData();
		// umldsに送信
		Iterator<String> key_itr = sendDatabaseinfo.keySet().iterator();


		while(key_itr.hasNext())
		{
			String keyClassName = key_itr.next();

			sendClassInfo(classNum, keyClassName);
			sendFieldInfo(classNum, sendDatabaseinfo.get(keyClassName).getFieldMap());
			sendMethodInfo(classNum,keyClassName,sendDatabaseinfo);
			classNum++;
		}

	}

	private void sendClassInfo(final int classNum,final String classname)
	{
		System.out.println("SendElementsToDatabase.sendClassInfo");
		final String INSERT = "insert into class(class_num,class_name,project_num) values (?,?,?)";
		String value = "(" + String.valueOf(classNum) + "," + "\"" + classname + "\"" + "," + "0);";

		// クラスに関するsqlを送信
		String sql = INSERT + value;
		sendClass(INSERT,classNum,classname,0);
		System.out.println("SendElementsToDatabase.sendClassInfo.終わり");
	}

	private void sendFieldInfo(final int classNum,final Map<String, String> fieldMap)
	{
		System.out.println("SendElementsToDatabase.sendFieldInfo");
		final String INSERT = "insert into field(field_num,class_num,field_name,field_type) values (?,?,?,?)";
		Iterator<String> key_itr = fieldMap.keySet().iterator();


		// フィールドが複数ある時のループ
		while(key_itr.hasNext())
		{
			String fieldname = key_itr.next();
			sen_FieldandMethod(INSERT,fieldNum,classNum,fieldname,fieldMap.get(fieldname));
			fieldNum++;
		}
	}


	// ゴミだから直したい
	private void sendMethodInfo(final int classNum,final String classname, final Map<String, ActiveSourceCodeInfo> sendDatabaseinfo)
	{
		System.out.println("SendElementsToDatabase.sendMethodInfo");
		final Map<String, String> methodreturnvalueMap = sendDatabaseinfo.get(classname).getMethodeRturnValueMap();
		final String INSERT = "insert into method(method_num,class_num,method_name,returnvalue) values (?,?,?,?)";
		Iterator<String> key_itr = methodreturnvalueMap.keySet().iterator();


		// フィールドが複数ある時のループ
		while(key_itr.hasNext())
		{
			String methodName = key_itr.next();

			Map<String, List<String>> methodparatypeMap = sendDatabaseinfo.get(classname).getMethodParaTypeMap();
			Map<String, List<String>> methodparanameMap = sendDatabaseinfo.get(classname).getMethodParaNameMap();

//			// 戻り値に関するsqlを送信
			sen_FieldandMethod(INSERT,methodNum,classNum,methodName,methodreturnvalueMap.get(methodName));

			// パラメータに関するsqlを送信
			sendMethodParaInfo(methodNum,methodName,methodparatypeMap,methodparanameMap);
			methodNum++;
		}
	}

	// ゴミだから直したい
	private void sendMethodParaInfo(final int methodNum,String methodName,final Map<String, List<String>> methodparatypeMap,Map<String, List<String>> methodparanameMap)
	{
		System.out.println("SendElementsToDatabase.sendMethodParaInfo");
		final String INSERT = "insert into m_parameta(para_num,method_num,para_type,para_name) values (?,?,?,?)";
		List<String> typelist = methodparatypeMap.get(methodName);
		List<String> namelist = methodparanameMap.get(methodName);

		//
		// フィールドが複数ある時のループ
		for(int listNum = 0; listNum < typelist.size(); listNum++)
		{
			String type = typelist.get(listNum);
			String name = namelist.get(listNum);

			sen_FieldandMethod(INSERT,paraNum,methodNum,type,name);
		}
		paraNum++;
	}

	private void sendClass(String insert, int classNum, String classname,int projectNum)
	{
		System.out.println("SendElementsToDatabase.sendClass");
		try {
			//con = hikari.getConnection();
			Class.forName(DRIVER_NAME);
			this.con = DriverManager.getConnection(URL,USER,PASWORD);
			//this.con.setAutoCommit(false);
			try(PreparedStatement ps = con.prepareStatement(insert)){
				ps.setInt(1, classNum); // パラメータ番号に仮で１を入れる パラメータ番号自体なくてもいいかも
				ps.setString(2,classname);
	            ps.setInt(3,projectNum);

	            ps.executeUpdate();
	            //con.commit();
	        } catch (Exception e) {
	            con.rollback();
	            System.out.println("rollback");
	            throw e;
	        }
		} catch (SQLException | ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.out.println("SendElementsToDatabase.sendClass終わり");
	}

	private void sen_FieldandMethod(final String insert, final int primaryNum, final int foreignNum, final String parameterIndex3, final String parameterIndex4)
	{
		System.out.println("SendElementsToDatabase.sendPara");
		System.out.println("privmaryNum:" + primaryNum + " foreignNum:" + foreignNum + " parameterIndex3;" + parameterIndex3 + " paraIndex4:" + parameterIndex4);
		try {
			//con = hikari.getConnection();
			Class.forName(DRIVER_NAME);
			this.con = DriverManager.getConnection(URL,USER,PASWORD);
			//this.con.setAutoCommit(false);

			try(PreparedStatement ps = con.prepareStatement(insert)){
				ps.setInt(1, primaryNum); // パラメータ番号に仮で１を入れる パラメータ番号自体なくてもいいかも
				ps.setInt(2,foreignNum);
	            ps.setString(3,parameterIndex3);
	            ps.setString(4, parameterIndex4);

	            ps.executeUpdate();
	        } catch (Exception e) {
	            con.rollback();
	            System.out.println("rollback");
	            throw e;
	        }

		} catch (SQLException | ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void reset()
	{
		classNum = 1;
		fieldNum = 1;
		methodNum = 1;
		paraNum = 1;
		deleteBeforeData();
	}

	private void deleteBeforeData()
	{
		final String deleteclSQL_class = "delete from class;";
		final String deleteclSQL_field = "delete from field;";
		final String deleteclSQL_method = "delete from method;";
		final String deleteclSQL_parameta = "delete from m_parameta;";

		try {
			Class.forName(DRIVER_NAME);
			this.con = DriverManager.getConnection(URL,USER,PASWORD);
			this.stm = con.createStatement();
			stm.executeUpdate(deleteclSQL_parameta);
			stm.executeUpdate(deleteclSQL_field);
			stm.executeUpdate(deleteclSQL_method);
			stm.executeUpdate(deleteclSQL_class);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}




