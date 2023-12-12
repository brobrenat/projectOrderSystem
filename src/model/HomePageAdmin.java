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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;
import main.databaseConnection;

public class HomePageAdmin {
	
	private Stage primaryStage;
	private Main mainInstance;
	private String username;
	Label labeltitle = new Label("SeRuput Teh");
	Label labeldesc = new Label("Select a prodcut to view");
	MenuBar menuBar = new MenuBar();
	Menu homeMenu = new Menu("Home");
	MenuItem homePageMenuItem = new MenuItem("Home Page");
	Menu manageProducts = new Menu("Manage Products");
	MenuItem manageProductsItem = new MenuItem("Manage Products");
	Menu accountMenu = new Menu("Account");
	MenuItem logoutMenuItem = new MenuItem("Log out");
	VBox layout = new VBox();
	Scene homeSceneAdmin;
	private ArrayList<item> itemsList = new ArrayList<>();
	ListView<String> listView = new ListView<>();
	Label labelname = new Label();
	VBox intro = new VBox(labelname, labeldesc);
	HBox hbox = new HBox(listView, intro);
	VBox tampilanjudul = new VBox(labeltitle, hbox);
	Label itemDescriptionTitle = new Label();
	Text itemDescriptionLabel = new Text();
	Label eachprice = new Label();
	VBox itemDescriptionBox = new VBox(itemDescriptionTitle, itemDescriptionLabel, eachprice);
	databaseConnection dbcon;
 
   
	public HomePageAdmin(Stage primaryStage, String username) {
		this.primaryStage = primaryStage;
		this.dbcon = databaseConnection.getConnection();
		this.username = username;
		labelname.setText("Welcome, "+ username);
		labelname.setFont(Font.font("Arial", FontWeight.BOLD, 21));
		initialize();
		setButtonEvent();
		loadListData();
		primaryStage.setScene(homeSceneAdmin);
		primaryStage.setTitle("Home");
		primaryStage.show();
	}

	private void setButtonEvent() {
		itemDescriptionLabel.setWrappingWidth(300);

	    listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue != null) {
	            if (!hbox.getChildren().contains(itemDescriptionBox)) {
	                hbox.getChildren().remove(intro);
	                hbox.getChildren().addAll(itemDescriptionBox);
	            }

	            eachprice.setText("Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
	            itemDescriptionTitle.setText(newValue);
	            String productDescription = dbcon.getProductDescription(newValue);
	            itemDescriptionLabel.setText(productDescription);
	            itemDescriptionBox.setVisible(true);
	        } else {
	            hbox.getChildren().remove(itemDescriptionBox);
	            hbox.getChildren().add(intro);
	        }
	    });
	    
	    



	    homeMenu.setOnAction(event -> {
            
            new HomePageAdmin(primaryStage, username);
        });
	    
	    logoutMenuItem.setOnAction(event -> new login(primaryStage));
	    manageProductsItem.setOnAction(event -> new ManageProducts(primaryStage, username));

	}

	private double getItemPrice(ArrayList<item> items, String itemName) {
		for (item i : items) {
			if (i.getObjectname().equals(itemName)) {
				return i.getObjectprice();
			}
		}
		return 0.0;
	}

	public void initialize() {
labeltitle.setFont(Font.font("Arial", FontWeight.BOLD, 42));
		
		listView.setMaxWidth(450);
		
		homeMenu.getItems().add(homePageMenuItem);
		manageProducts.getItems().add(manageProductsItem);
		accountMenu.getItems().addAll(logoutMenuItem);
		menuBar.getMenus().addAll(homeMenu, manageProducts, accountMenu);
		VBox.setMargin(hbox, new Insets(0, 0, 0, 0));
		
		labeltitle.setPadding(new Insets(10));
		itemDescriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		itemDescriptionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
	
		itemDescriptionBox.setSpacing(15);
		VBox mainVB = new VBox(menuBar, tampilanjudul, itemDescriptionBox);
		mainVB.setSpacing(4);
		VBox.setVgrow(mainVB, Priority.ALWAYS);
		HBox.setHgrow(listView, Priority.ALWAYS);
		hbox.setSpacing(15);
		hbox.setPadding(new Insets(10));
		homeSceneAdmin = new Scene(mainVB, 1000, 800);
	    }

	    public void loadListData() {
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

}
