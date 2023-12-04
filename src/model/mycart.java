package model;

import java.util.ArrayList;
import java.util.Collection;

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
	Label labeltitle = new Label("User's Cart");
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
	Spinner<Integer> quantity = new Spinner<>(1, 100, 1);
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
	VBox itemDescriptionBox = new VBox(itemDescriptionTitle, itemDescriptionLabel, eachprice);
	HBox quantspin = new HBox(quantlabel, quantity, totalprice);
	HBox updateremove = new HBox();
	Label subtotal, orderinfo, username, phonenum, address;

	public mycart(Stage primaryStage, ArrayList<cart> cartItems, ListView<String> cartListView) {
		this.primaryStage = primaryStage;
		this.cartItems = cartItems;
		System.out.println("Number of items in cartItems: " + cartItems.size());
		initialize();
		setbuttonevent();
		primaryStage.setScene(mycart);
		primaryStage.setTitle("Home");
		primaryStage.show();

	}

	private void initialize() {
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
		updatecart.setMaxWidth(150);
		removecart.setMaxWidth(150);
		subtotal = new Label("Total : ");
		orderinfo = new Label("Order Information");
		orderinfo.setFont(Font.font("Arial", FontWeight.BOLD, 13));
		username = new Label("Username : ");
		phonenum = new Label("Phone Number : ");
		address = new Label("Address : ");
		purchase.setMaxWidth(150);
		VBox cartinfo = new VBox(subtotal, orderinfo, username, phonenum, address, purchase);
		VBox tampilanjudul = new VBox(labeltitle, hbox, cartinfo);
		cartinfo.setPadding(new Insets(10));
		cartinfo.setSpacing(5);
		itemDescriptionBox.setSpacing(2);
		VBox mainVB = new VBox(menuBar, tampilanjudul, itemDescriptionBox);
		mainVB.setSpacing(4);
		hbox.setSpacing(15);
		hbox.setPadding(new Insets(15));
		mycart = new Scene(mainVB, 1000, 800);
		checkEmptyCart();

	}

	private void checkEmptyCart() {
		if (cartItems.isEmpty()) {
			intro.setVisible(true);
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

	private double getItemPrice(ArrayList<item> items, String itemName) {
		for (item i : items) {
			if (i.getObjectname().equals(itemName)) {
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
			if (newValue != null) {
	            String productName = extractProductName(newValue);
				if (!hbox.getChildren().contains(itemDescriptionBox)) {
					hbox.getChildren().remove(intro);
					hbox.getChildren().addAll(itemDescriptionBox);
					HBox updateremove = new HBox(updatecart, removecart);
					updateremove.setSpacing(5);
					itemDescriptionBox.getChildren().addAll(quantspin, updateremove);
					System.out.println(newValue);
					System.out.println(observable);
					System.out.println(oldValue);
				}
				eachprice.setText(
						String.valueOf(getItemPrice(itemsList, cartListView.getSelectionModel().getSelectedItem())));
				itemDescriptionTitle.setText(productName);
				itemDescriptionLabel.setText("Description for " + productName);
				itemDescriptionBox.setVisible(true);
			} else {
				hbox.getChildren().remove(itemDescriptionBox);
				hbox.getChildren().add(intro);
			}
		});

		homeMenu.setOnAction(event -> new HomePageCus(primaryStage));
		logoutMenuItem.setOnAction(event -> new login(primaryStage, mainInstance));
	}

	public void removeItemFromCart(cart itemToRemove) {
		cartItems.remove(itemToRemove);
		loadListView();
	}

}