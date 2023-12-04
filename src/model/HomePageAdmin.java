package model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.Main;

public class HomePageAdmin {

	private Stage primaryStage;
	private Main mainInstance;
	MenuBar menuBar = new MenuBar();
	Menu homeMenu = new Menu("Home");
	MenuItem homePageMenuItem = new MenuItem("Home Page");
	Menu cartMenu = new Menu("Manage Products");
	MenuItem myCartMenuItem = new MenuItem("Manage Products");
	Menu accountMenu = new Menu("Account");
	MenuItem logoutMenuItem = new MenuItem("Log out");
	VBox layout = new VBox();
	Scene homeScene;

	public HomePageAdmin(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initialize();
		primaryStage.setScene(homeScene);
		primaryStage.setTitle("Home");
		primaryStage.show();

	}

	public void initialize() {
		homeMenu.getItems().add(homePageMenuItem);
		cartMenu.getItems().add(myCartMenuItem);
		accountMenu.getItems().addAll(logoutMenuItem);
		menuBar.getMenus().addAll(homeMenu, cartMenu, accountMenu);
		layout.getChildren().addAll(menuBar);
		homeScene = new Scene(layout, 800, 800);
		primaryStage.setScene(homeScene);
	}

}
