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
	databaseConnection dbcon;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.loginInstance = new login(primaryStage);
        this.dbcon = databaseConnection.getConnection();

		primaryStage.setOnCloseRequest(event -> {
			dbcon.clearCartTable();
		});
	}
	public static void main(String[] args) {
		launch(args);
	}
}
