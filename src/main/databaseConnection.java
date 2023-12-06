package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.item;



public class databaseConnection {
	
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "seruput_teh";
	private final String HOST ="localhost:3306";
	
	private final String CONNECT = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	private Main mainInstance;

	private Connection con;
	private Statement st;
	private static databaseConnection DatabaseConnection;
	ArrayList<item> items = new ArrayList<>();
    Connection connection;

	public databaseConnection() {
		try {
			con = DriverManager.getConnection(CONNECT, USERNAME,PASSWORD);
			st = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static databaseConnection getConnection() {
		return DatabaseConnection == null ? new databaseConnection() : DatabaseConnection;	
	}
	
	public ResultSet executeQuery(String username) {
		ResultSet rs = null;
		String query = String.format("SELECT * FROM user WHERE username = \"%s\"", username);
		
		try {
			rs = st.executeQuery(query);
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		return rs;
	}
	public String getLastRegisteredUserID() {
        String lastUserID = null;
        String query = "SELECT userID FROM user ORDER BY userID DESC LIMIT 1";

        try {
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                lastUserID = rs.getString("userID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastUserID;
    }
	
	public void executeUpdate(String userID,String username, String role, String password, String address, String phoneNum, String gender, String action) {
		String query = "";
		
		if(action == "registration") {
			query = String.format("INSERT INTO user (userID, username, password, role, address ,phone_num, gender) "
					+ "VALUES ( \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\") ", userID,username, password, role, address, phoneNum, gender);
		}
		
		try {
			System.out.println(query);
			st.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getProductDescription(String product_name) {
	    String description = "";

	    try {
	        if (con != null && !con.isClosed()) {
	            try (Statement statement = con.createStatement();
	                ResultSet resultSet = statement.executeQuery("SELECT product_des FROM product WHERE product_name = '" + product_name + "'");
	            ) {
	                if (resultSet.next()) {
	                    // Assuming 'product_description' is the column name in your 'product' table
	                    description = resultSet.getString("product_des");
	                }
	            }
	        } else {
	            // Handle the case where the connection is not initialized or closed
	            System.err.println("Database connection is not initialized or closed.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the exception according to your needs
	    }

	    return description;
	}

	
	public ResultSet authenticateUser(String username, String password) {
        String query = String.format("SELECT * FROM user WHERE username = \"%s\" AND password = \"%s\"", username, password);
        ResultSet rs = null;
        
        try {
        	rs = st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return rs;
    }

	public String getUserRole(String username) {
	    String query = String.format("SELECT role FROM user WHERE username = '%s'", username);
	    ResultSet rs = executeQuery(query);

	    try {
	        if (rs != null && rs.next()) {
	            return rs.getString("role");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
    
    public ArrayList<item> getAllItems() {


        String query = "SELECT product_name, product_price, product_des FROM product";
        try (ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                String itemName = rs.getString("product_name");
                double itemPrice = rs.getDouble("product_price");
                String itemDesc = rs.getString("product_des");

                item newItem = new item(itemName, itemPrice);
                items.add(newItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
	

	
	
}
