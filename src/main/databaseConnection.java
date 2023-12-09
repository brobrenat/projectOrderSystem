package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

	public String getLastRegisteredProdID() {
		 String lastprodID = null;
	     String query = "SELECT productID FROM product ORDER BY productID DESC LIMIT 1";

	        try {
	            ResultSet rs = st.executeQuery(query);
	            if (rs.next()) {
	                lastprodID = rs.getString("productID");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return lastprodID;
	}
	
	public boolean deleteProduct(String productName) {
	    String query = "DELETE FROM product WHERE product_name = ?";
	    
	    try {
	       
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, productName);
	            
	            
	            int rowsAffected = preparedStatement.executeUpdate();
	            
	         
	            if (rowsAffected > 0) {
	                return true;  
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return false;  
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
	               
	                    description = resultSet.getString("product_des");
	                }
	            }
	        } else {
	     
	            System.err.println("Database connection is not initialized or closed.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        
	    }

	    return description;
	}

	public void executeProdAdd(String productID, String product_name, String product_price, String product_des) {
	    String query = "INSERT INTO product (productID, product_name, product_price, product_des) VALUES (?, ?, ?, ?)";
	    try {
	        System.out.println(query);

	        // Use a prepared statement
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, productID);
	        	preparedStatement.setString(2, product_name);
	            preparedStatement.setString(3, product_price);
	            preparedStatement.setString(4, product_des);

	            preparedStatement.executeUpdate();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
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
	    ResultSet rs = executeQuery(username);

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
	    

	    String query = "SELECT productID, product_name, product_price, product_des FROM product";
	    try (ResultSet rs = st.executeQuery(query)) {
	        while (rs.next()) {
	            String productID = rs.getString("productID");
	            String productName = rs.getString("product_name");
	            double productPrice = rs.getDouble("product_price");
	            String productDescription = rs.getString("product_des");

	            item newItem = new item(productName, productPrice);
	            items.add(newItem);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return items;
	}
	
	public String getUserIDByUsername(String username) {
	    String userID = null;
	    String query = "SELECT userID FROM user WHERE username = ?";

	    try {
	
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, username);

	
	            ResultSet resultSet = preparedStatement.executeQuery();

	            
	            if (resultSet.next()) {
	                userID = resultSet.getString("userID");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return userID;
	}

	public boolean isProductIDExists(String nextprodID) {

		String query = "SELECT COUNT(*) AS count FROM product WHERE productID = ?";

	    try {
	       
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, nextprodID);

	        
	            ResultSet resultSet = preparedStatement.executeQuery();

	           
	            if (resultSet.next()) {
	                int count = resultSet.getInt("count");
	                return count > 0;  // If count > 0, the productID exists
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;

	}
	
	public void updateProductPrice(String productName, double newPrice) {
	    String query = "UPDATE product SET product_price = ? WHERE product_name = ?";

	    try {
	      
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setDouble(1, newPrice);
	            preparedStatement.setString(2, productName);

	      
	            int rowsAffected = preparedStatement.executeUpdate();

	           
	            if (rowsAffected > 0) {
	                System.out.println("Price updated successfully.");
	            } else {
	                System.out.println("Failed to update price. Product not found.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public String getProductIDByName(String selectedItem) {
		String productID = null;
	    String query = "SELECT productID FROM product WHERE product_name = ?";

	    try {
	        
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, selectedItem);

	            
	            ResultSet resultSet = preparedStatement.executeQuery();

	           
	            if (resultSet.next()) {
	                productID = resultSet.getString("productID");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return productID;
	}

	public void addToTableCart(String productID, String userID, int quantity) {
	    String query = "INSERT INTO cart (productID, userID, quantity) VALUES (?, ?, ?)";
	    
	    try {
	       
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, productID);
	            preparedStatement.setString(2, userID);
	            preparedStatement.setInt(3, quantity);

	            
	            int rowsAffected = preparedStatement.executeUpdate();

	            
	            if (rowsAffected > 0) {
	                System.out.println("Item added to cart successfully.");
	            } else {
	                System.out.println("Failed to add item to cart.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public void updateCartQuantityInDatabase(String productID, String userID, int newQuantity) {
	    String query = "UPDATE cart SET quantity = ? WHERE productID = ? AND userID = ?";

	    try {
	     
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setInt(1, newQuantity);
	            preparedStatement.setString(2, productID);
	            preparedStatement.setString(3, userID);

	          
	            int rowsAffected = preparedStatement.executeUpdate();

	           
	            if (rowsAffected > 0) {
	                System.out.println("Cart quantity updated successfully.");
	            } else {
	                System.out.println("Failed to update cart quantity. Product not found in the cart.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean deleteCartItem(String productID, String userID) {
	    String query = "DELETE FROM cart WHERE productID = ? AND userID = ?";
	    
	    try {
	      
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, productID);
	            preparedStatement.setString(2, userID);
	            
	         
	            int rowsAffected = preparedStatement.executeUpdate();
	            
	          
	            return rowsAffected > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return false;
	}

	
	
}
