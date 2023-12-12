package model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.Main;
import main.databaseConnection;

public class purchasehistory {

	private Stage primaryStage;
	private Main mainInstance;
	Label labeltitle = new Label();
	Label labelname = new Label("There's no History");
	Label labeldesc = new Label("Consider Purchasing Our Products");
	MenuBar menuBar = new MenuBar();
	Menu homeMenu = new Menu("Home");
	MenuItem homePageMenuItem = new MenuItem("Home Page");
	Menu cartMenu = new Menu("Cart");
	MenuItem myCartMenuItem = new MenuItem("My Cart");
	Menu accountMenu = new Menu("Account");
	MenuItem logoutMenuItem = new MenuItem("Log out");
	MenuItem purchaseHistoryMenuItem = new MenuItem("Purchase History");
	VBox layout = new VBox();
	Scene purchasehistory;
	ArrayList<item> itemsList = new ArrayList<>();
	Spinner<Integer> quantity = new Spinner<>(-100, 100, 1);
	Label transactionid = new Label("Transaction ID : ");
	Label name = new Label();
	Label phonenumb = new Label("Phone Number : ");
	Label addr = new Label("Address : ");
	Label totalprice = new Label();

	ArrayList<cart> cartItems = new ArrayList<>();
	VBox intro = new VBox(labelname, labeldesc);
	ListView<String> cartListView = new ListView<>();
	ListView<String> transactlist = new ListView<>();
	HBox hbox = new HBox(transactlist, intro);
	VBox itemDescriptionBox = new VBox(transactionid, name, phonenumb, addr, totalprice,cartListView);
	Label subtotal, orderinfo, phonenum, address;
	private String username;
	ArrayList<transaction> transactions = new ArrayList<>();
	
	databaseConnection dbcon;

	public purchasehistory(Stage primaryStage, ArrayList<cart> cartItems, ListView<String> cartListView,
		String username, ArrayList<transaction> transactions) {
		this.primaryStage = primaryStage;
		this.cartItems = cartItems;
		this.username = username;
		this.dbcon = databaseConnection.getConnection();
		this.transactions = transactions;
		labeltitle.setText(username + "'s" + " Purchase History");
		name.setText("Username : " + username);
		System.out.println("Number of items in cartItems: " + cartItems.size());
		loadtranstlist(username);
		initialize();
		retrieveUserInfo(username);
		setbuttonevent();
		primaryStage.setScene(purchasehistory);
		primaryStage.setTitle("Home");
		primaryStage.show();

	}
	
	public void retrieveUserInfo(String username) {
	    try {
	        String phoneNumber = dbcon.getUserDetails(username, "phoneNum"); 
	        String userAddress = dbcon.getUserDetails(username, "address");

	        if (phoneNumber != null && userAddress != null) {
	            phonenumb.setText("Phone Number: " + phoneNumber);
	            addr.setText("Address: " + userAddress);
	        } else {
	  
	            phonenumb.setText("Phone Number: N/A");
	            addr.setText("Address: N/A");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void initialize() {
		
		checkEmptyCart();
		itemDescriptionBox.setVisible(false);
		labeltitle.setPadding(new Insets(10));
		labeltitle.setFont(Font.font("Arial", FontWeight.BOLD, 42));
		labelname.setFont(Font.font("Arial", FontWeight.BOLD, 21));
		cartListView.setMaxHeight(300);
		cartListView.setMaxWidth(400);
		transactlist.setMaxHeight(500);
		transactlist.setMaxWidth(300);
		labelname.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		HBox.setHgrow(cartListView, Priority.ALWAYS);
		homeMenu.getItems().add(homePageMenuItem);
		cartMenu.getItems().add(myCartMenuItem);
		accountMenu.getItems().addAll(logoutMenuItem, purchaseHistoryMenuItem);
		menuBar.getMenus().addAll(homeMenu, cartMenu, accountMenu);
		VBox.setMargin(hbox, new Insets(0, 0, 0, 0));
		labeltitle.setPadding(new Insets(10));
		subtotal = new Label("Total : Rp. 0.00");
		orderinfo = new Label("Order Information");
		orderinfo.setFont(Font.font("Arial", FontWeight.BOLD, 13));
		Label usernamesh = new Label("Username : ");
		phonenum = new Label("Phone Number : ");
		address = new Label("Address : ");
		VBox tampilanjudul = new VBox(labeltitle, hbox);
		itemDescriptionBox.setSpacing(2);
		VBox mainVB = new VBox(menuBar, tampilanjudul, itemDescriptionBox);
		mainVB.setSpacing(4);
		hbox.setSpacing(15);
		hbox.setPadding(new Insets(15));
		purchasehistory = new Scene(mainVB, 1000, 800);
		updateSubtotalLabel();

	}
	
	

	public void updateTransactions(transaction newTransaction, ListView<String> transactlist) {
		transactions.add(newTransaction);
		updateTransactionListView(transactlist);
	}

	private void updateTransactionListView(ListView<String> transactlist) {
		ObservableList<String> transactionIds = FXCollections.observableArrayList();
		for (transaction t : transactions) {
			transactionIds.add(t.getTransactionId());
		}
		transactlist.setItems(transactionIds);
	}

	private void checkEmptyCart() {
		
	    if (transactlist.getItems().isEmpty()) {
	        labelname.setText("There's No History");
	        labeldesc.setText("Consider Purchasing Our Products");
	        itemDescriptionBox.setVisible(false);  
	    } else {
	        labelname.setText("Select a Transaction to view Details");
	        labeldesc.setVisible(false);
	    }
	}

	private void loadtranstlist(String username) {
	    String userID = dbcon.getUserIDByUsername(username);
	    ArrayList<String> transactionIDs = dbcon.getAllTransactionIDsForUser(userID);
	    transactlist.getItems().addAll(transactionIDs);
	}

	private void updateSubtotalLabel() {
		if (subtotal != null) {
			double total = calculateTotalPrice();
			subtotal.setText("Total : Rp. " + String.format("%.2f", total));
		}
	}

	


	private double getItemPrice(ArrayList<item> items, String itemname) {

		for (cart i : cartItems) {
			if (i.getObjectname().equalsIgnoreCase(itemname)) {
				return i.getObjectprice();
			}
		}
		return 0.0;
	}

	private String extractProductName(String newValue) {
		int startIndex = newValue.indexOf('x') + 1;
		int endIndex = newValue.indexOf('(');
		if (startIndex >= 0 && endIndex >= 0) {
			return newValue.substring(startIndex, endIndex).trim();
		}
		return "";
	}
	



	private void setbuttonevent() {
		String userID = dbcon.getUserIDByUsername(username);

		transactlist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { 
			String transactionID = newValue;
			itemDescriptionBox.setVisible(true);
			if (newValue != null) {
				if (!hbox.getChildren().contains(itemDescriptionBox)) {
	                hbox.getChildren().remove(intro);
	                cartListView.getItems().clear();
	                hbox.getChildren().addAll(itemDescriptionBox);
	            }
				transactionid.setText("TransactionID : " + newValue);
				dbcon.viewTransactionDetails(transactionID, cartListView);
				String selectedItem = extractProductName(newValue);
				double price = getItemPrice(itemsList, selectedItem);
				double totalPrice = dbcon.viewTransactionDetails(transactionID, cartListView);
				System.out.println(totalPrice);
				
				totalprice.setText("Total : Rp. " + String.format("%.2f", totalPrice));
			} else {
				hbox.getChildren().remove(itemDescriptionBox);
				hbox.getChildren().add(intro);
			}
		});

		homeMenu.setOnAction(event -> new HomePageCus(primaryStage, username));
		logoutMenuItem.setOnAction(event -> new login(primaryStage));
		myCartMenuItem.setOnAction(event -> new mycart(primaryStage, cartItems, transactlist, username));

	}

	private double calculateTotalPrice() {
		double totalPrice = 0.0;

		for (cart cartItem : cartItems) {
			totalPrice += cartItem.getObjectprice() * cartItem.getObjectquantity();
		}
		return totalPrice;

	}

	public Scene getScene() {
		return purchasehistory;
	}

}
