package template.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/dbschema?useUnicode=true&characterEncoding=UTF-8";
	private String user = "userid"; // DB user ID
	private String pwd = "password"; // DB user Password
	private Connection conn = null;

	public Connection getConnection() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("BusInfoSystem : 드라이버 로딩 실패(getConnection)");
			System.out.println(e.getMessage());
		}
		try {
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (SQLException e) {
			System.out.println("BusInfoSystem : DB 연결 실패(getConnection)");
			System.out.println(e.getMessage());
		}
		return conn;
	}
}
