package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class databaseConnection {
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "seruput_teh";
	private final String HOST ="localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql//%s/%s", HOST, DATABASE);
	
	public ResultSet rs;
	public ResultSetMetaData rsm;
	
	private Connection con;
	private Statement st;
	private static databaseConnection DatabaseConnection;
	
	public static databaseConnection getInstance() {
		if(DatabaseConnection == null) {
			return new databaseConnection();
		}else {
			return DatabaseConnection;
		}
	}
	
	private databaseConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(CONNECTION, USERNAME,PASSWORD);
			st = con.createStatement();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public ResultSet execQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
}
