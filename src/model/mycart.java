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

public class mycart {

	private Stage primaryStage;
	private Main mainInstance;
	Label labeltitle = new Label();
	Label labelname = new Label("No Item in Cart");
	Label labeldesc = new Label("Considering adding one!");
	MenuBar menuBar = new MenuBar();
	Menu homeMenu = new Menu("Home");
	MenuItem homePageMenuItem = new MenuItem("Home Page");
	Menu cartMenu = new Menu("Cart");
	MenuItem myCartMenuItem = new MenuItem("My Cart");
	Menu accountMenu = new Menu("Account");
	MenuItem logoutMenuItem = new MenuItem("Log out");
	MenuItem purchaseHistoryMenuItem = new MenuItem("Purchase History");
	VBox layout = new VBox();
	Scene mycart;
	ArrayList<item> itemsList = new ArrayList<>();
	Spinner<Integer> quantity = new Spinner<>(-100, 100, 1);
	Label itemDescriptionTitle = new Label();
	Label itemDescriptionLabel = new Label();
	Label eachprice = new Label();
	Label quantlabel = new Label("Quantity : ");
	Label totalprice = new Label();
	Button updatecart = new Button("Update Cart");
	Button removecart = new Button("Remove From Cart");
	Button purchase = new Button("Make Purchase");
	ArrayList<cart> cartItems = new ArrayList<>();
	VBox intro = new VBox(labelname, labeldesc);
	ListView<String> cartListView = new ListView<>();
	HBox hbox = new HBox(cartListView, intro);
	VBox cartinfo = new VBox();
	HBox quantspin = new HBox();
	HBox updateremove = new HBox();
	VBox itemDescriptionBox = new VBox(itemDescriptionTitle, itemDescriptionLabel, eachprice, quantspin, updateremove);
	Label subtotal, orderinfo, phonenum, address;
	private String username;
	ArrayList<transaction> transactions = new ArrayList<>();
	ArrayList<user> userList = new ArrayList<>();

	public mycart(Stage primaryStage, ArrayList<cart> cartItems, ListView<String> cartListView, String username) {
	    this.primaryStage = primaryStage;
	    this.cartItems = cartItems;
	    this.username = username;
		labeltitle.setText(username+"'s"+" Cart");
		retrieveUserInfo(username);
		initialize();
		setbuttonevent();
		primaryStage.setScene(mycart);
		primaryStage.setTitle("Home");
		primaryStage.show();

	}

	private void initialize() {

		checkEmptyCart();
		System.out.println(userList.size());
		labeltitle.setPadding(new Insets(10));
		labeltitle.setFont(Font.font("Arial", FontWeight.BOLD, 42));
		labelname.setFont(Font.font("Arial", FontWeight.BOLD, 21));
		cartListView.setMaxHeight(250);
		cartListView.setMaxWidth(400);
		HBox.setHgrow(cartListView, Priority.ALWAYS);
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
		updateremove.setSpacing(5);
		updatecart.setMaxWidth(150);
		removecart.setMaxWidth(150);
		subtotal = new Label("Total : Rp. 0.00");
		orderinfo = new Label("Order Information");
		orderinfo.setFont(Font.font("Arial", FontWeight.BOLD, 13));
		Label usernamesh = new Label("Username : "+username);
		phonenum = new Label("Phone Number : ");
		address = new Label("Address : ");
		purchase.setMaxWidth(150);
		VBox cartinfo = new VBox(subtotal, orderinfo, usernamesh, phonenum, address, purchase);
		VBox tampilanjudul = new VBox(labeltitle, hbox, cartinfo);
		cartinfo.setPadding(new Insets(10));
		cartinfo.setSpacing(5);
		itemDescriptionBox.setSpacing(2);
		VBox mainVB = new VBox(menuBar, tampilanjudul, itemDescriptionBox);
		mainVB.setSpacing(4);
		hbox.setSpacing(15);
		hbox.setPadding(new Insets(15));
		mycart = new Scene(mainVB, 1000, 800);
		updateSubtotalLabel();

	}

	 public void retrieveUserInfo(String username) {
	        for (user user : userList) {
	            if (user.getUsername().equals(username)) {
	                String phoneNumber = user.getPhoneNumber();
	                String userAddress = user.getAddress();

	        
	                phonenum.setText("Phone Number: " + phoneNumber);
	                address.setText("Address: " + userAddress);

	                break; 
	            }
	        }
	    }
	 public void addtotransaction(transaction newTransaction) {
	        newTransaction.setTransactionId(generateTransactionId(transactions.size() + 1));
	        newTransaction.setCartItems(cartItems);
	    }

	private void checkEmptyCart() {
		if (calculateTotalPrice() == 0) {
			labelname.setText("No Item in Cart");
			labeldesc.setText("Considering adding one!");

		} else {
			labelname.setText("Welcome User's");
			labeldesc.setText("Choose item to add or delete");
			loadListView();

		}

	}

	private void loadListView() {
		ObservableList<String> cartItemNames = FXCollections.observableArrayList();
		for (cart cartItem : cartItems) {
			cartItemNames.add(cartItem.getObjectquantity() + "x " + cartItem.getObjectname() + " ( Rp."
					+ (cartItem.getObjectprice() * cartItem.getObjectquantity()) + ")");
		}
		cartListView.getItems().addAll(cartItemNames);
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
		cartListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			itemDescriptionBox.setVisible(true);
			if (newValue != null) {
				String productName = extractProductName(newValue);
				if (!hbox.getChildren().contains(itemDescriptionBox)) {
					hbox.getChildren().remove(intro);
					hbox.getChildren().addAll(itemDescriptionBox);
					updateremove.getChildren().clear();
					updateremove.getChildren().addAll(updatecart, removecart);
					quantspin.getChildren().clear();
					quantspin.getChildren().addAll(quantlabel, quantity, totalprice);

				}
				eachprice.setText(String.valueOf(getItemPrice(itemsList, productName)));
				itemDescriptionTitle.setText(productName);
				itemDescriptionLabel.setText("Description for " + productName);
				itemDescriptionBox.setVisible(true);
				String selectedItem = extractProductName(newValue);
				double price = getItemPrice(itemsList, selectedItem);
				double totalPrice = price * quantity.getValue();

				totalprice.setText("Total : Rp. " + String.format("%.2f", totalPrice));
			} else {
				hbox.getChildren().remove(itemDescriptionBox);
				hbox.getChildren().add(intro);
			}
		});

		quantity.valueProperty().addListener((observable, oldValue, newValue) -> {
			String selectedItem = extractProductName(cartListView.getSelectionModel().getSelectedItem());
			double price = getItemPrice(itemsList, selectedItem);
			double totalPrice = price * newValue.intValue();

			totalprice.setText("Total : Rp. " + String.format("%.2f", totalPrice));
		});

		updatecart.setOnAction(event -> {
			String selectedItem = extractProductName(cartListView.getSelectionModel().getSelectedItem());
			int cartQuantity = getCartQuantity(selectedItem);
			int spinnerValue = quantity.getValue();

			if (spinnerValue < 0) {
				int newQuantity = cartQuantity + spinnerValue;
				if (newQuantity >= 0) {
					updateCartQuantity(selectedItem, newQuantity);
					updateListView(selectedItem, newQuantity);
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
			        alert.setTitle("Error");
			        alert.setHeaderText(null);
			        alert.setContentText("Quantity is not valid!");
			        alert.showAndWait();
				}
			} else if (spinnerValue == 0) {
				removeItemFromCart(selectedItem);
			} else if (spinnerValue > 0) {
				updateCartQuantity(selectedItem, spinnerValue);
				updateListView(selectedItem, spinnerValue);
			}
			updateSubtotalLabel();
			itemDescriptionBox.setVisible(false);
			labelname.setVisible(false);
			labeldesc.setVisible(false);
		});

		removecart.setOnAction(event -> {
			String selectedItem = cartListView.getSelectionModel().getSelectedItem();
			String productName = extractProductName(selectedItem);
			removeItemFromCart(productName);
			updateSubtotalLabel();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Message");
	        alert.setHeaderText(null);
	        alert.setContentText("Deleted from Cart");
	        alert.showAndWait();
	        labelname.setVisible(true);
			labeldesc.setVisible(true);
		});

		purchase.setOnAction(event -> {
		    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmation.setTitle("Confirmation");
		    confirmation.setHeaderText("Confirm Purchase");
		    confirmation.setContentText("Are you sure you want to make this purchase?");
		    ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
		    ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
		    confirmation.getButtonTypes().setAll(buttonYes, buttonNo);
			confirmation.showAndWait().ifPresent(response -> {
				if (response.equals(buttonYes)) {
					String transactionId = generateTransactionId(transactions.size() + 1);
					transaction newTransaction = new transaction(transactionId, cartItems);
					addtotransaction(newTransaction);
					transactions.add(newTransaction);
					
					System.out.println(cartItems);
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
			        alert.setTitle("Message");
			        alert.setHeaderText(null);
			        alert.setContentText("Successfully Purchased");
			        alert.showAndWait();
			        cartItems.clear();
					cartListView.getItems().clear();
					updateSubtotalLabel();
					checkEmptyCart();
				}else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
			        alert.setTitle("Error");
			        alert.setHeaderText(null);
			        alert.setContentText("Failed to Make Transaction");
			        alert.showAndWait();
				}
			});
			
			
		});
		homeMenu.setOnAction(event -> new HomePageCus(primaryStage, username));
		logoutMenuItem.setOnAction(event -> new login(primaryStage, mainInstance));
		purchaseHistoryMenuItem.setOnAction(event -> new purchasehistory(primaryStage, cartItems, cartListView, username, transactions));


	}

	private int getCartQuantity(String productName) {
		int cartQuantity = 0;
		for (cart item : cartItems) {
			if (item.getObjectname().equalsIgnoreCase(productName)) {
				cartQuantity = item.getObjectquantity();
				break;
			}
		}
		return cartQuantity;
	}

	private void updateCartQuantity(String productName, int newQuantity) {
		for (cart item : cartItems) {
			if (item.getObjectname().equalsIgnoreCase(productName)) {
				item.setObjectquantity(newQuantity);
				break;
			}
		}
	}

	private double calculateTotalPrice() {
		double totalPrice = 0.0;

		for (cart cartItem : cartItems) {
			totalPrice += cartItem.getObjectprice() * cartItem.getObjectquantity();
		}
		return totalPrice;

	}

	private void updateSubtotalLabel() {
		if (subtotal != null) {
			double total = calculateTotalPrice();
			subtotal.setText("Total : Rp. " + String.format("%.2f", total));
		}
	}

	private void updateListView(String productName, int newQuantity) {
		ObservableList<String> items = cartListView.getItems();
		for (int i = 0; i < items.size(); i++) {
			String currentItem = items.get(i);
			if (currentItem.contains(productName)) {
				items.remove(i);
				items.add(i, newQuantity + "x " + productName + " ( Rp."
						+ (getItemPrice(itemsList, productName) * newQuantity) + ")");
				break;
			}
		}
	}

	private void removeItemFromCart(String productName) {
		for (cart item : cartItems) {
			if (item.getObjectname().equalsIgnoreCase(productName)) {
				cartItems.remove(item);
				break;
			}
		}

		ObservableList<String> items = cartListView.getItems();
		for (String item : new ArrayList<>(items)) {
			if (item.contains(productName)) {
				items.remove(item);
				break;
			}
		}
		updateSubtotalLabel();

		if (calculateTotalPrice() == 0) {
			labelname.setText("No Item in Cart");
			labeldesc.setText("Considering adding one!");
		}
	}

	private String generateTransactionId(int index) {
		return "TR" + String.format("%03d", index);
	}

}
