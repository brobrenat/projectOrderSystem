package model;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;
import main.databaseConnection;

public class HomePageCus {

	private Stage primaryStage;
	private Main mainInstance;
	private String username;
	Label labeltitle = new Label("SeRuput Teh");
	Label labeldesc = new Label("Select a prodcut to view");
	MenuBar menuBar = new MenuBar();
	Menu homeMenu = new Menu("Home");
	MenuItem homePageMenuItem = new MenuItem("Home Page");
	Menu cartMenu = new Menu("Cart");
	MenuItem myCartMenuItem = new MenuItem("My Cart");
	Menu accountMenu = new Menu("Account");
	MenuItem logoutMenuItem = new MenuItem("Log out");
	MenuItem purchaseHistoryMenuItem = new MenuItem("Purchase History");
	VBox layout = new VBox();
	Scene homeScenecus;
	private ArrayList<item> itemsList = new ArrayList<>();
	ListView<String> listView = new ListView<>();
	Spinner<Integer> quantity = new Spinner<>(1, 100, 1);
	Label labelname = new Label();
	VBox intro = new VBox(labelname, labeldesc);
	HBox hbox = new HBox(listView, intro);
	VBox tampilanjudul = new VBox(labeltitle, hbox);
	Label itemDescriptionTitle = new Label();
	Text itemDescriptionLabel = new Text();
	Label eachprice = new Label();
	Label quantlabel = new Label("Quantity : ");
	Label totalprice = new Label();
	Button addtocart = new Button("Add To Cart");
	VBox itemDescriptionBox = new VBox(itemDescriptionTitle, itemDescriptionLabel, eachprice);
	HBox quantspin = new HBox(quantlabel, quantity, totalprice);
    private ArrayList<cart> cartItems = new ArrayList<>();
	databaseConnection dbcon;
	

	public HomePageCus(Stage primaryStage, String username) {
		this.primaryStage = primaryStage;
		this.dbcon = databaseConnection.getConnection();
        this.username = username;
        
       
        
		labelname.setText("Welcome, "+ username);
		labelname.setFont(Font.font("Arial", FontWeight.BOLD, 21));
		initialize();
		setbuttonevent();
		loadListData();
		primaryStage.setScene(homeScenecus);
		primaryStage.setTitle("Home");
		primaryStage.show();

	}

	private void initialize() {
		labeltitle.setFont(Font.font("Arial", FontWeight.BOLD, 42));
		listView.setMaxWidth(450);
		homeMenu.getItems().add(homePageMenuItem);
		cartMenu.getItems().add(myCartMenuItem);
		accountMenu.getItems().addAll(logoutMenuItem, purchaseHistoryMenuItem);
		menuBar.getMenus().addAll(homeMenu, cartMenu, accountMenu);
		VBox.setMargin(hbox, new Insets(0, 0, 0, 0));
		labeltitle.setPadding(new Insets(10));
		itemDescriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		itemDescriptionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		totalprice.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
		quantspin.setSpacing(5);
		addtocart.setMaxWidth(150);
		itemDescriptionBox.setSpacing(15);
		VBox mainVB = new VBox(menuBar, tampilanjudul, itemDescriptionBox);
		mainVB.setSpacing(4);
		VBox.setVgrow(mainVB, Priority.ALWAYS);
		HBox.setHgrow(listView, Priority.ALWAYS);
		hbox.setSpacing(15);
		hbox.setPadding(new Insets(10));
		homeScenecus = new Scene(mainVB, 1000, 800);
	}

	private void loadListData() {
	    if (dbcon != null) {
	    
	        ArrayList<item> itemsFromDatabase = dbcon.getAllItems();

	        itemsList.clear(); 
	        itemsList.addAll(itemsFromDatabase);

	        ObservableList<String> items = FXCollections.observableArrayList();
	        for (item i : itemsList) {
	            items.add(i.getObjectname());
	        }

	        listView.getItems().clear();
	        listView.setItems(items);
	    } else {
	        System.err.println("Error: The 'dbcon' object is not initialized.");
	    }
	}

	private double getItemPrice(ArrayList<item> items, String itemName) {
		for (item i : items) {
			if (i.getObjectname().equals(itemName)) {
				return i.getObjectprice();
			}
		}
		return 0.0;
	}

	private void setbuttonevent() {
		itemDescriptionLabel.setWrappingWidth(300);
		
		listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (!hbox.getChildren().contains(itemDescriptionBox)) {
					hbox.getChildren().remove(intro);
					itemDescriptionBox.getChildren().addAll(quantspin, addtocart);
					hbox.getChildren().addAll(itemDescriptionBox);
				}

				eachprice.setText(String.valueOf(getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem())));
				itemDescriptionTitle.setText(newValue);
				String productDescription = dbcon.getProductDescription(newValue);
		        itemDescriptionLabel.setText(productDescription);				itemDescriptionBox.setVisible(true);
				double price = getItemPrice(itemsList, newValue);
				double totalPrice = price * quantity.getValue();

				totalprice.setText("Total : Rp. " + String.format("%.2f", totalPrice));

			} else {
				hbox.getChildren().remove(itemDescriptionBox);
				hbox.getChildren().add(intro);
			}
		});
		
		

		quantity.valueProperty().addListener((observable, oldValue, newValue) -> {
			String selectedItem = listView.getSelectionModel().getSelectedItem();
			double price = getItemPrice(itemsList, selectedItem);
			double totalPrice = price * newValue.intValue();
			
			

			totalprice.setText("Total : Rp. " + String.format("%.2f", totalPrice)); 
		});
		
		myCartMenuItem.setOnAction(event -> {
	        mycart myCartPage = new mycart(primaryStage, cartItems, listView, username);
	        primaryStage.setScene(myCartPage.getScene());
	    });

	    purchaseHistoryMenuItem.setOnAction(event -> {
	        purchasehistory purchaseHistoryPage = new purchasehistory(primaryStage, cartItems, listView, username, null);
	        primaryStage.setScene(purchaseHistoryPage.getScene());
	    });
		
		homeMenu.setOnAction(event -> {
            new HomePageCus(primaryStage, username);
        });
		logoutMenuItem.setOnAction(event -> new login(primaryStage));
		addtocart.setOnAction(event ->  addToCart());

	}
	
	private void addToCart() {
	    String selectedItem = listView.getSelectionModel().getSelectedItem();
	    int selectedQuantity = quantity.getValue();
	    String currentuser = username; 
	    String userID = dbcon.getUserIDByUsername(currentuser);

	    if (selectedItem != null && selectedQuantity > 0) {
	        boolean found = false;
	        for (cart item : cartItems) {
	            if (item.getObjectname().equals(selectedItem)) {
	                item.setObjectquantity(item.getObjectquantity() + selectedQuantity);
	                item.setObjectprice(getItemPrice(itemsList, selectedItem));
	                found = true;
	                break;
	            }
	        }

	        if (!found) {
	            double price = getItemPrice(itemsList, selectedItem);
	            cart cartItem = new cart(selectedItem, price, selectedQuantity, "Description");
	            cartItems.add(cartItem);
	            
	            String productID = dbcon.getProductIDByName(selectedItem);
	            
	            dbcon.addToTableCart(productID, userID, selectedQuantity);
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Message");
				alert.setHeaderText(null);
				alert.setContentText("Item Added to Cart");
				alert.showAndWait();
	        }
	    }
	    mycart.updateGlobalCart(cartItems);
	}



	


}
