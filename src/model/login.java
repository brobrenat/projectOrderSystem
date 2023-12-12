package model;

import main.Main;
import main.databaseConnection;

import java.sql.Connection;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class login {
	Label loginLabel = new Label("Login");
	Label usernameLabel = new Label("Username:");
	TextField usernameField = new TextField();
	Label passwordLabel = new Label("Password:");
	PasswordField passwordField = new PasswordField();
	Label accountLabel = new Label("Don't have an account yet?");

	Hyperlink registerLink = new Hyperlink("Register here");
	Button loginButton = new Button("Login");
	HBox dontHaveAccountBox = new HBox(5);
	GridPane gridPane = new GridPane();
	Scene login;
	private Stage primaryStage;
	databaseConnection dbcon;
	
	public login(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.dbcon = databaseConnection.getConnection();
		initialize();
		setbuttonevent();
		primaryStage.setTitle("Login");
		primaryStage.show();
	}

	private void setbuttonevent() {

		registerLink.setOnAction(e -> new registration(primaryStage));

		loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showErrorAlert("Failed to Login", "All fields must be filled");
            } else {
               
                if (dbcon.authenticateUser(username, password) != null) {
                	System.out.println(dbcon.getUserRole(username));
                    showSuccess("Login successful");
                    
                    if (dbcon.getUserRole(username).equals("Admin")) {
                    	
                        new HomePageAdmin(primaryStage, username);
                    } else {
                        new HomePageCus(primaryStage, username);
                    }
                } else {
                    showErrorAlert("Failed to Login", "Invalid credentials");
                }
            }
        });

	}
	
	public void showSuccess(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setContentText(message);

		alert.showAndWait();

	}

	
	public void showErrorAlert(String header, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setHeaderText(header);
		dialogPane.setContentText(content);

		alert.showAndWait();
	}
	
	void initialize() {
		// TODO Auto-generated method stub
		loginLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

		usernameField.setPrefWidth(300);
		usernameField.setPromptText("Input username...");

		passwordField.setPromptText("Input password...");

		loginButton.setMaxWidth(90);
		dontHaveAccountBox.getChildren().addAll(accountLabel, registerLink);
		dontHaveAccountBox.setAlignment(Pos.CENTER_LEFT);

		gridPane.setHgap(10);
		gridPane.setVgap(10);

		gridPane.add(loginLabel, 1, 0);
		gridPane.add(usernameLabel, 0, 1);
		gridPane.add(usernameField, 1, 1);
		gridPane.add(passwordLabel, 0, 2);
		gridPane.add(passwordField, 1, 2);
		gridPane.add(dontHaveAccountBox, 1, 4);
		gridPane.add(loginButton, 1, 5);

		gridPane.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(gridPane);

		Scene login = new Scene(vbox, 800, 800);

		primaryStage.setScene(login);

	}

}
