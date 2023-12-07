
package model;

import main.Main;
import main.databaseConnection;
import java.util.concurrent.atomic.AtomicReference;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class registration implements EventHandler<ActionEvent> {
	private Stage primaryStage;
	private Main mainInstance;
	Scene registration;
	GridPane gridPane = new GridPane();
	Label registrationLabel = new Label("Registration");
	Label usernameLabel = new Label("Username:");
	Label emailLabel = new Label("Email:");
	Label passwordLabel = new Label("Password:");
	Label confirmPasswordLabel = new Label("Confirm Password:");
	Label phoneNumberLabel = new Label("Phone Number:");
	Label addressLabel = new Label("Address:");
	Label genderLabel = new Label("Gender:");
	Label descriptionLabel = new Label("Have an account?");
	Hyperlink loginLink = new Hyperlink("Login here");
	TextField usernameField = new TextField();
	TextField emailField = new TextField();
	PasswordField passwordField = new PasswordField();
	PasswordField confirmPasswordField = new PasswordField();
	TextField phoneNumberField = new TextField();
	TextArea addressArea = new TextArea();
	RadioButton maleRadioButton = new RadioButton("Male");
	RadioButton femaleRadioButton = new RadioButton("Female");
	ToggleGroup genderToggle = new ToggleGroup();
	CheckBox termsCheckBox = new CheckBox("Agree to Terms & Conditions");
	Button registerButton = new Button("Register");
	HBox gendertoggle = new HBox(5);
	HBox HaveAccountBox = new HBox(5);
	databaseConnection dbcon= new databaseConnection();
	Button check = new Button("Check");
	private AtomicReference<String> gender = new AtomicReference<>("");


	public registration(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initialize();
		setbuttonkey();
		primaryStage.setTitle("Registration");
		primaryStage.show();


	}

	private void initialize() {
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		registrationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		loginLink.setOnAction(e -> new login(primaryStage));

		usernameField.setPromptText("Input username...");
		emailField.setPromptText("Input email...");

		passwordField.setPromptText("Input password...");

		confirmPasswordField.setPromptText("Input confirm password...");

		phoneNumberField.setPromptText("Input phone number...");

		addressArea.setPromptText("Input address...");

		maleRadioButton.setToggleGroup(genderToggle);
		femaleRadioButton.setToggleGroup(genderToggle);

		registerButton.setMaxWidth(175);
		registerButton.setOnAction(this);
		
		check.setOnAction(this);

		gendertoggle.getChildren().addAll(maleRadioButton, femaleRadioButton);
		gendertoggle.setAlignment(Pos.CENTER_LEFT);

		HaveAccountBox.getChildren().addAll(descriptionLabel, loginLink);
		HaveAccountBox.setAlignment(Pos.CENTER_LEFT);

		gridPane.add(registrationLabel, 1, 0);

		gridPane.add(usernameLabel, 0, 1);
		gridPane.add(usernameField, 1, 1);

		gridPane.add(emailLabel, 0, 2);
		gridPane.add(emailField, 1, 2);

		gridPane.add(passwordLabel, 0, 3);
		gridPane.add(passwordField, 1, 3);

		gridPane.add(confirmPasswordLabel, 0, 4);
		gridPane.add(confirmPasswordField, 1, 4);

		gridPane.add(phoneNumberLabel, 0, 5);
		gridPane.add(phoneNumberField, 1, 5);

		gridPane.add(addressLabel, 0, 6);
		gridPane.add(addressArea, 1, 6);

		gridPane.add(genderLabel, 0, 7);

		gridPane.add(gendertoggle, 1, 7);

		gridPane.add(termsCheckBox, 1, 8);
		gridPane.add(HaveAccountBox, 1, 9);
		gridPane.add(registerButton, 1, 10);
		gridPane.add(check, 2, 10);

		gridPane.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(gridPane);
		Scene registration = new Scene(vbox, 800, 800);
        primaryStage.setScene(registration);


	}

	public void showSuccess(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setContentText(message);

		alert.showAndWait();

	}

	private void setbuttonkey() {
		
		
		
		maleRadioButton.setOnAction(e -> {
            gender.set("Male");
        });
		
		femaleRadioButton.setOnAction(e -> {
			gender.set("Female");
		});
		
//		if(event.getSource() == maleRadioButton) {
//			gender = "Male";
//		}else if(event.getSource() == femaleRadioButton){
//			gender = "Female";
//		}
		
		registerButton.setOnAction(e -> {
		    String username = usernameField.getText();
		    String password = passwordField.getText();
		    String address = addressArea.getText();
		    String phoneNumber = phoneNumberField.getText();
		    String userid = generateNextUserID();
		    String selectedGender = gender.get();
		    String role = "User";

		    boolean isValid = validateInputs(username, emailField.getText(), password,
		            confirmPasswordField.getText(), phoneNumber, address, genderToggle.getSelectedToggle(), termsCheckBox.isSelected());

		    if (isValid) {
		        showSuccess("Registered Successfully");

		        // Ensure none of the variables is null before making the database call
		        if (userid!= null && username != null && password != null && address != null && phoneNumber != null && selectedGender != null) {
		           dbcon.executeUpdate(userid, username, role, password, address, phoneNumber, selectedGender, "registration");
		        } else {
		            showErrorAlert("Registration Failed", "Invalid inputs. Please check the fields.");
		        }
		    } else {
		        showErrorAlert("Registration Failed", "Invalid inputs. Please check the fields.");
		    }
		});

		
		check.setOnAction(this);

	}
	public void showErrorAlert(String header, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setHeaderText(header);
		dialogPane.setContentText(content);

		alert.showAndWait();
	}

	
	private String generateNextUserID() {
        String lastUserID = dbcon.getLastRegisteredUserID();

        String numericPart = lastUserID.substring(2); // Extract the numeric part
        int nextNumber = Integer.parseInt(numericPart) + 1;
        
        String nextUserID = String.format("CU%03d", nextNumber);

        return nextUserID;
    }
	
	private boolean validateInputs(String username, String email, String password, String confirmPassword,
			String phoneNumber, String address, Toggle gender, boolean agreedTerms) {

		if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
				|| phoneNumber.isEmpty() || address.isEmpty() || gender == null || !agreedTerms) {
			return false;
		}

		if (!email.endsWith("@gmail.com")) {
			return false;
		}

		if (username.length() < 5 || username.length() > 20) {
			return false; // Adjust this condition to check for uniqueness (not handled in this method)
		}

		if (!password.matches("[a-zA-Z0-9]+") || password.length() < 5) {
			return false;
		}

		if (!password.equals(confirmPassword)) {
			return false;
		}

		if (!phoneNumber.matches("\\+62\\d{9,}")) {
			return false;
		}

		return true;
	}

	
	@Override
	public void handle(ActionEvent event) {
		String userID = "AD007";
		String username = usernameField.getText();
		String password = passwordField.getText();
		String phoneNum = phoneNumberField.getText();
		String address = addressArea.getText();
		String role = "User";
		String gender = "";
		if(event.getSource() == maleRadioButton) {
			gender = "Male";
		}else if(event.getSource() == femaleRadioButton){
			gender = "Female";
		}
		
		if(event.getSource() == registerButton) {
			//input data ke database
			dbcon.executeUpdate(username, password, address, phoneNum, gender, "registration", userID, role);
		}
			//else if(event.getSource() == check) {
//			buat ambil data dari database
			ResultSet rs = dbcon.executeQuery(username);
			
			try {
				while(rs != null && rs.next()) {
					System.out.println(rs.getString("username"));
				}
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			
		
		
	}
		


}
