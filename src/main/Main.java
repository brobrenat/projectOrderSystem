package main;

import model.login;
import model.registration;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	private login loginInstance;
	private Stage primaryStage;
	Stage s;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.loginInstance = new login(primaryStage, this);

	}

	public void showErrorAlert(String header, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setHeaderText(header);
		dialogPane.setContentText(content);

		alert.showAndWait();
	}

	public void showSuccess(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setContentText(message);

		alert.showAndWait();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
