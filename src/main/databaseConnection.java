package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import model.item;

public class databaseConnection {
	
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "seruput_teh";
	private final String HOST ="localhost:3306";
	
	private final String CONNECT = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
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
	public boolean clearCartTable() {
    String query = "DELETE FROM cart";

    try {
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            int rowsAffected = preparedStatement.executeUpdate();

            // Check if any rows were affected
            if (rowsAffected > 0) {
                System.out.println("Cart table cleared successfully.");
                return true;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    System.out.println("Failed to clear cart table.");
    return false;
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
	                return count > 0;  
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
	
	public void updateCartQuantityInDatabase(String productID, String userID, int quantity) {
	    try {
	        System.out.println("Updating quantity in database. ProductID: " + productID + ", UserID: " + userID + ", Quantity: " + quantity);

	        String updateQuery = "UPDATE cart SET quantity = ? WHERE productID = ? AND userID = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(updateQuery);
	        preparedStatement.setInt(1, quantity);
	        preparedStatement.setString(2, productID);
	        preparedStatement.setString(3, userID);

	        int rowsAffected = preparedStatement.executeUpdate();
	        System.out.println("Rows affected: " + rowsAffected);
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

	public void insertTransactionHeader(String transactionID, String userID) {
	    try {
	        String query = "INSERT INTO transaction_header (transactionID, userID) VALUES (?, ?)";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, transactionID);
	        preparedStatement.setString(2, userID);
	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle the exception according to your needs
	    }
	}
	public boolean isTransactionIdExists(String transactionId) {
	    String query = "SELECT COUNT(*) AS count FROM transaction_header WHERE transactionID = ?";

	    try {
	        System.out.println("Checking existence for TransactionID: " + transactionId);

	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, transactionId);

	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                int count = resultSet.getInt("count");
	                System.out.println("Count for TransactionID " + transactionId + ": " + count);
	                return count > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}

	
	public boolean clearCart(String userID) {
	    String query = "DELETE FROM cart WHERE userID = ?";

	    try {
	        
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, userID);

	            
	            int rowsAffected = preparedStatement.executeUpdate();

	           
	            return rowsAffected > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}

	public boolean insertTransactionDetail(String transactionId, String productId, int quantity) {
	    try {
	       
	        if (isTransactionIdExists(transactionId) && isProductIDExists(productId)) {
	           
	            String query = "INSERT INTO transaction_detail (transactionId, productId, quantity) VALUES (?, ?, ?)";
	            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	                preparedStatement.setString(1, transactionId);
	                preparedStatement.setString(2, productId);
	                preparedStatement.setInt(3, quantity);

	                int rowsAffected = preparedStatement.executeUpdate();
	                return rowsAffected > 0;
	            }
	        } else {
	            
	            System.out.println("TransactionID or ProductID does not exist.");
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	
	public void insertAllCartItemsIntoTransactionDetail(String transactionID, String userID, int quantity) {
	   
	    ArrayList<String> productIDsInCart = getProductIDsInCart(userID);

	    
	    if (isTransactionIdExists(transactionID)) {
	       
	        for (String productID : productIDsInCart) {
	            insertTransactionDetail(transactionID, productID, quantity); 
	        }
	        clearCart(userID);
	    } else {
	        System.out.println("TransactionID does not exist in transaction_header.");
	    }
	}

	public ArrayList<String> getProductIDsInCart(String userID) {
	    ArrayList<String> productIDs = new ArrayList<>();
	    
	    
	    String query = "SELECT productID FROM cart WHERE userID = ?";
	    
	    try {
	   
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, userID);

	           
	            ResultSet resultSet = preparedStatement.executeQuery();

	            
	            while (resultSet.next()) {
	                String productID = resultSet.getString("productID");
	                productIDs.add(productID);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return productIDs;
	}
	
	public String getUserDetails(String username, String action) {
	    String userDetails = " ";
	    String query = "";

	    try {
	        if (action.equals("phoneNum")) {
	            query = String.format("SELECT phone_num FROM user WHERE username = \"%s\"", username);
	        } else if (action.equals("address")) {
	            query = String.format("SELECT address FROM user WHERE username = \"%s\"", username);
	        } 

	        ResultSet rs = st.executeQuery(query);

	        if (rs.next()) {
	            userDetails = rs.getString(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return userDetails;
	}
	
	public int getProductQuantitiesInCart(String productID , String userID, String action) {
	    int productQuantity = 0;
	    String query = "";

	    try {
	        if (action.equals("quantity")) {
	            query = String.format("SELECT quantity FROM cart WHERE userID = \"%s\" AND productID = \"%s\" ", userID, productID );
	        } 

	        ResultSet rs = st.executeQuery(query);

	        if (rs.next()) {
	            productQuantity = rs.getInt("quantity");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return productQuantity;
	}

	public ArrayList<String> getAllTransactionIDsForUser(String userID) {
        ArrayList<String> transactionIDs = new ArrayList<>();
        String query = String.format("SELECT transactionID FROM transaction_header WHERE userID = \"%s\"", userID);

        try {
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String transactionID = rs.getString("transactionID");
                System.out.println(transactionID);
                transactionIDs.add(transactionID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionIDs;
    }
	
	public double viewTransactionDetails(String transactionID, ListView<String> cartListView) {
        double totalPrice = 0.0;

	    try {
	    	
	    	
	    		String query = "SELECT product.product_name, transaction_detail.quantity, product.product_price " +
	                       "FROM transaction_detail " +
	                       "INNER JOIN product ON transaction_detail.productId = product.productID " +
	                       "WHERE transaction_detail.transactionId = ?";
	    	
	    		
	    		
	        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
	            preparedStatement.setString(1, transactionID);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                ObservableList<String> transactionDetails = FXCollections.observableArrayList();
	               

	                while (resultSet.next()) {
	                    String productName = resultSet.getString("product_name");
	                    int quantity = resultSet.getInt("quantity");
	                    double productPrice = resultSet.getDouble("product_price");

	                    double totalProductPrice = quantity * productPrice;
	                    totalPrice += totalProductPrice;

	                    transactionDetails.add(String.format("%-20s %-10d %-10.2f", productName, quantity, totalProductPrice));

	                }

	                
	                cartListView.setItems(transactionDetails);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return totalPrice;
	}

	   


	
}
