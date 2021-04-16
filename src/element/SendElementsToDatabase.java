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
		// umldsに送信
		Iterator<String> key_itr = sendDatabaseinfo.keySet().iterator();
		int classNum = 1; // クラスに番号をつけるのに使う

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
		String insert = "insert into class(class_num,class_name,project_num) values ";
		String value = "(" + String.valueOf(classNum) + "," + "\"" + classname + "\"" + "," + "0);";

		// クラスに関するsqlを送信
		String sql = insert + value;
		send(sql);

	}

	private void sendFieldInfo(final int classNum,final Map<String, String> fieldMap)
	{
		System.out.println("SendElementsToDatabase.sendFieldInfo");
		String insert = "insert into field(field_num,class_name,field_name,field_type) values (?,?,?,?)";
		Iterator<String> key_itr = fieldMap.keySet().iterator();
		int fieldNum = 1;

		// フィールドが複数ある時のループ
		while(key_itr.hasNext())
		{
			String fieldname = key_itr.next();
//			String value = "(" + String.valueOf(fieldNum) + "," +
//								 String.valueOf(classNum) + ", "
//								 + "," + "\"" +  fieldname + "\"" + ","
//								 + "," + "\"" + fieldMap.get(fieldname) + "\"" + ");";
//
//			// フィールドに関するsqlを送信
//			String sql = insert + value;
//			send(sql);
//			fieldNum++;

			///
			try {
				//con = hikari.getConnection();
				Class.forName(DRIVER_NAME);
				this.con = DriverManager.getConnection(URL,USER,PASWORD);
				try(PreparedStatement ps = con.prepareStatement(insert)){
					ps.setString(1, String.valueOf(fieldNum)); // パラメータ番号に仮で１を入れる パラメータ番号自体なくてもいいかも
					ps.setString(2,String.valueOf(classNum));
		            ps.setString(3,fieldname);
		            ps.setString(4, fieldMap.get(fieldname));

		            ps.executeUpdate();
		            con.commit();
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
	}


	// ゴミだから直したい
	private void sendMethodInfo(final int classNum,final String classname, final Map<String, ActiveSourceCodeInfo> sendDatabaseinfo)
	{
		System.out.println("SendElementsToDatabase.sendMethodInfo");
		final Map<String, String> methodreturnvalueMap = sendDatabaseinfo.get(classname).getMethodeRturnValueMap();
		String insert = "insert into method(method_num,class_name,method_name,returnvalue) values (?,?,?,?)";
		Iterator<String> key_itr = methodreturnvalueMap.keySet().iterator();

		int methodNum = 1;
		// フィールドが複数ある時のループ
		while(key_itr.hasNext())
		{
			String methodName = key_itr.next();
//			String value = "(" + String.valueOf(methodNum) + "," +
//								 String.valueOf(classNum) + ", "
//								 + "'" + methodName + "'" + "," +
//								 "'" + methodreturnvalueMap.get(methodName) + "');";
//
			Map<String, List<String>> methodparatypeMap = sendDatabaseinfo.get(classname).getMethodParaTypeMap();
			Map<String, List<String>> methodparanameMap = sendDatabaseinfo.get(classname).getMethodParaNameMap();
//
//			// 戻り値に関するsqlを送信
//			String sql = insert + value;
//			send(sql);
			try {
				//con = hikari.getConnection();
				Class.forName(DRIVER_NAME);
				this.con = DriverManager.getConnection(URL,USER,PASWORD);
				try(PreparedStatement ps = con.prepareStatement(insert)){
					ps.setString(1, String.valueOf(methodNum)); // パラメータ番号に仮で１を入れる パラメータ番号自体なくてもいいかも
					ps.setString(2,String.valueOf(classNum));
		            ps.setString(3,methodName);
		            ps.setString(4,methodreturnvalueMap.get(methodName));

		            ps.executeUpdate();
		            con.commit();
		        } catch (Exception e) {
		            con.rollback();
		            System.out.println("rollback");
		            throw e;
		        }

			} catch (SQLException | ClassNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			// パラメータに関するsqlを送信
			sendMethodParaInfo(methodNum,methodName,methodparatypeMap,methodparanameMap);
			methodNum++;
		}
	}

	// ゴミだから直したい
	private void sendMethodParaInfo(final int methodNum,String methodName,final Map<String, List<String>> methodparatypeMap,Map<String, List<String>> methodparanameMap)
	{
		System.out.println("SendElementsToDatabase.sendMethodParaInfo");
		List<String> typelist = methodparatypeMap.get(methodName);
		List<String> namelist = methodparatypeMap.get(methodName);

		// int paraNum = 1;
		// フィールドが複数ある時のループ
		for(int listNum = 0; listNum < typelist.size(); listNum++)
		{
			String type = typelist.get(listNum);
			String name = namelist.get(listNum);

			sendPara(String.valueOf(methodNum),type,name);
		}


//		while(key_itr.hasNext())
//		{
//			String methodname = key_itr.next();
//			String value = "(" + String.valueOf(paraNum) + "," +
//								 String.valueOf(methodNum) + ", "
//								 + "'" + methodparatypeMap.get(methodname) + "'" + "," +
//								 "'" + methodparanameMap.get(methodname) + "');";
//
//			String sql = insert + value;
//			send(sql);
//			paraNum++;
//		}



//		Iterator<String> key_itr = methodparatypeMap.keySet().iterator();
//
//		int paraNum = 1;
//		// フィールドが複数ある時のループ
//		while(key_itr.hasNext())
//		{
//			String methodname = key_itr.next();
//			String value = "(" + String.valueOf(paraNum) + "," +
//								 String.valueOf(methodNum) + ", "
//								 + "'" + methodparatypeMap.get(methodname) + "'" + "," +
//								 "'" + methodparanameMap.get(methodname) + "');";
//
//			String sql = insert + value;
//			send(sql);
//			paraNum++;
//		}
	}

	private void send(String sql)
	{
		try {
			//con = hikari.getConnection();
			Class.forName(DRIVER_NAME);
			this.con = DriverManager.getConnection(URL,USER,PASWORD);
			this.stm = con.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void sendPara(final String methodNum, final String type, final String name)
	{
		System.out.println("SendElementsToDatabase.sendPara");
		String insert = "insert into m_parameta(para_num,method_num,para_type,para_name) values (?,?,?)";
		try {
			//con = hikari.getConnection();
			Class.forName(DRIVER_NAME);
			this.con = DriverManager.getConnection(URL,USER,PASWORD);
			try(PreparedStatement ps = con.prepareStatement(insert)){
				ps.setString(1, String.valueOf(paraNum++)); // パラメータ番号に仮で１を入れる パラメータ番号自体なくてもいいかも
				ps.setString(2,methodNum);
	            ps.setString(3,type);
	            ps.setString(4, name);

	            ps.executeUpdate();
	            con.commit();
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
}




