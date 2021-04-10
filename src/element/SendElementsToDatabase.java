package element;

import java.sql.Connection;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

class SendElementsToDatabase implements IElementOperation {

	static SendElementsToDatabase singleton = new SendElementsToDatabase();
	private HikariDataSource hikari;
	private Connection con;
	private Statement stm;

	private SendElementsToDatabase()
	{
		// データベースmotooへの接続
		System.out.println("SendElementsToDatabase.コンストラクタ");

		HikariConfig config = new HikariConfig();
		config.setDriverClassName("com.mysql.jdbc.Driver");
		config.setJdbcUrl("jdbc:mysql://localhost:3306/motoo");

		config.setUsername("root");
		config.setPassword("root");

		hikari= new HikariDataSource(config);

	}

	static SendElementsToDatabase getInstance()
	{
		return singleton;
	}

	@Override
	public void excute()
	{
		// TODO 自動生成されたメソッド・スタブ
		sendDatabase();
	}

	private void sendDatabase()
	{
		//　motooに送信
	}
}
