package model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.Main;

public class ManageProducts {
	private Stage primaryStage;
	private Main mainInstance;
	private String username;
	GridPane kananGP;
	Label labeltitle = new Label("Manage Products");
	Label labeldesc = new Label("Select a prodcut to update");
	MenuBar menuBar = new MenuBar();
	Menu homeMenu = new Menu("Home");
	MenuItem homePageMenuItem = new MenuItem("Home Page");
	Menu manageProducts = new Menu("Manage Products");
	MenuItem manageProductsItem = new MenuItem("Manage Products");
	Menu accountMenu = new Menu("Account");
	MenuItem logoutMenuItem = new MenuItem("Log out");
	VBox layout = new VBox();

	Scene manageProductsScene;

	private ArrayList<item> itemsList = new ArrayList<>();
	ListView<String> listView = new ListView<>();

	Label labelname = new Label();
	Button addBtn = new Button("Add Product");
	Button upBtn = new Button("Update Product");
	Button reBtn = new Button("Remove Product");

	Label inputProductName = new Label("Input Product Name");
	TextField productName = new TextField();
	

	Label inputProductPrice = new Label("Input Product Price");
	TextField productPrice = new TextField();

	Label inputProductDesc = new Label("Input Product Description");
	TextArea productDesc = new TextArea();
	
	Button addBut = new Button("Add Product");
	Button backBut = new Button("Back");
	
	HBox addbackBut = new HBox(addBut, backBut);
	
	Button upPro = new Button("Update Product");
	Button backUpdate = new Button("Back");
	
	Label upProduct = new Label("Update Product");
	TextField newPrice = new TextField();
	
	HBox updateBut = new HBox(upPro, backUpdate);
	
	Label delLabel = new Label("Are you sure, you want to remove this product?");
	Button rePro = new Button("Remove Product");
	Button backRemove = new Button("Back");
	HBox deleteBut = new HBox(rePro, backRemove);



	GridPane addGP = new GridPane();

	private HBox tombolUpDel = new HBox(upBtn, reBtn);
	// HBox tombolUpDel = new HBox(upBtn,reBtn);
	Label eachprice = new Label();
	VBox intro = new VBox(labelname, labeldesc, eachprice, addBtn, tombolUpDel, addGP);
	HBox hbox = new HBox(listView, intro);
	VBox tampilanjudul = new VBox(labeltitle, hbox);
	Label itemDescriptionTitle = new Label();
	Label itemDescriptionLabel = new Label();
	// VBox itemDescriptionBox = new VBox(itemDescriptionTitle,
	// itemDescriptionLabel, eachprice, addBtn);
	

	public ManageProducts(Stage primaryStage, String username) {
		this.primaryStage = primaryStage;
		this.username = username;
		labelname.setText("Welcome, " + username);
		labelname.setFont(Font.font("Arial", FontWeight.BOLD, 21));
		initialize();
		setButtonEvent();
		loadListData();
		primaryStage.setScene(manageProductsScene);
		primaryStage.setTitle("Products");
		primaryStage.show();
	}

	private void setButtonEvent() {

		listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				labelname.setText(newValue);
				labeldesc.setText("Description for : " + newValue);
				eachprice.setText("Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
				itemDescriptionTitle.setText(newValue);
				itemDescriptionLabel.setText(newValue);
				eachprice.setVisible(true);
				addBtn.setVisible(true);
				tombolUpDel.setVisible(true);
				addGP.setVisible(true);
				
				inputProductName.setVisible(false);
				productName.setVisible(false);
				inputProductPrice.setVisible(false);
				productPrice.setVisible(false);
				inputProductDesc.setVisible(false);
				productDesc.setVisible(false);
				addbackBut.setVisible(false);
				
				delLabel.setVisible(false);
				deleteBut.setVisible(false);
				
				upProduct.setVisible(false);
				newPrice.setVisible(false);
				updateBut.setVisible(false);
				

			} else {

				eachprice.setVisible(false);
				tombolUpDel.setVisible(false);
				addGP.setVisible(false);

			}
			
			//klik tombol update product (upBtn)
			upBtn.setOnMouseClicked(e -> {
				labelname.setText(newValue);
				labeldesc.setText("Description for : " + newValue);
				eachprice.setText("Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
				itemDescriptionTitle.setText(newValue);
				itemDescriptionLabel.setText(newValue);
				eachprice.setVisible(true);
				addGP.setVisible(true);
				upProduct.setVisible(true);
				newPrice.setVisible(true);
				updateBut.setVisible(true);
				
				inputProductName.setVisible(false);
				productName.setVisible(false);
				inputProductPrice.setVisible(false);
				productPrice.setVisible(false);
				inputProductDesc.setVisible(false);
				productDesc.setVisible(false);
				addbackBut.setVisible(false);
				
				delLabel.setVisible(false);
				deleteBut.setVisible(false);
				
				newPrice.clear();
				
				
			});
			
			reBtn.setOnMouseClicked(e -> {
				labelname.setText(newValue);
				labeldesc.setText("Description for : " + newValue);
				eachprice.setText("Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
				itemDescriptionTitle.setText(newValue);
				itemDescriptionLabel.setText(newValue);
				eachprice.setVisible(true);
				addGP.setVisible(true);
				
				delLabel.setVisible(true);
				deleteBut.setVisible(true);
				
				upProduct.setVisible(false);
				newPrice.setVisible(false);
				updateBut.setVisible(false);
				
				inputProductName.setVisible(false);
				productName.setVisible(false);
				inputProductPrice.setVisible(false);
				productPrice.setVisible(false);
				inputProductDesc.setVisible(false);
				productDesc.setVisible(false);
				addbackBut.setVisible(false);

			
			});
			
			//saat update product
			upPro.setOnMouseClicked(e -> {
			    String selectedProduct = listView.getSelectionModel().getSelectedItem();
			    String newPriceText = newPrice.getText();

			    // Validasi apakah TextField terisi dan newPrice lebih dari 0
			    if (newPriceText.isEmpty() || Double.parseDouble(newPriceText) <= 0) {
			        // Tampilkan Information Alert jika kondisi tidak memenuhi
			        showAlert(Alert.AlertType.INFORMATION, "Input tidak valid", "Pastikan harga baru diisi dan lebih dari 0.");
			        return;  // Hentikan eksekusi jika validasi gagal
			    }

			    double newProductPrice = Double.parseDouble(newPriceText);

			    // Update harga pada daftar item
			    boolean updateSuccess = updateItemPrice(itemsList, selectedProduct, newProductPrice);

			    if (updateSuccess) {
			        // Perbarui tampilan label harga jika pembaruan berhasil
			        eachprice.setText("Price: Rp. " + getItemPrice(itemsList, selectedProduct));

			        // Tampilkan Alert sukses
			        showAlert(Alert.AlertType.INFORMATION, "Pembaruan berhasil", "Harga produk berhasil diperbarui.");

			        labelname.setText("Welcome, " + username);
				    labeldesc.setText("Select a product to update");
				    eachprice.setText("Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
				    
				    eachprice.setVisible(false);
			        tombolUpDel.setVisible(false);
			        addBtn.setVisible(true);
			        addGP.setVisible(false);;
			    } else {
			        // Tampilkan Error Alert jika pembaruan tidak berhasil
			        showAlert(Alert.AlertType.ERROR, "Gagal memperbarui harga", "Terjadi kesalahan dalam memperbarui harga produk.");
			    }
			});
			
			rePro.setOnMouseClicked(e -> {
				 // Mendapatkan produk yang dipilih
			    String selectedProduct = listView.getSelectionModel().getSelectedItem();

			    // Validasi: Pastikan ada produk yang dipilih
			    if (selectedProduct == null || selectedProduct.isEmpty()) {
			        showAlert(Alert.AlertType.ERROR, "Pilih Produk", "Pilih produk yang akan dihapus.");
			        return;
			    }

			    // Hapus produk dari list dan update tampilan
			    boolean deleteSuccess = deleteProduct(selectedProduct);
			    if (deleteSuccess) {
			        showAlert(Alert.AlertType.INFORMATION, "Hapus Produk Berhasil", "Produk berhasil dihapus.");
			        labelname.setText("Welcome, " + username);
			        labeldesc.setText("Select a product to update");
			        eachprice.setText("Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));

			        eachprice.setVisible(false);
			        tombolUpDel.setVisible(false);
			        addBtn.setVisible(true);
			        addGP.setVisible(false);
			    } else {
			        showAlert(Alert.AlertType.ERROR, "Gagal Menghapus Produk", "Terjadi kesalahan dalam menghapus produk.");
			    }
			});
		
			});
		
		//saat klik button Add Product pertama kali
		addBtn.setOnMouseClicked(e -> {

			labelname.setText("Welcome, " + username);
			labeldesc.setText("Select a product to update");
			eachprice.setText(
					"Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
			
			eachprice.setVisible(false);
			addGP.setVisible(true);
			
			inputProductName.setVisible(true);
			productName.setVisible(true);
			inputProductPrice.setVisible(true);
			productPrice.setVisible(true);
			inputProductDesc.setVisible(true);
			productDesc.setVisible(true);
			addbackBut.setVisible(true);
			
			delLabel.setVisible(false);
			deleteBut.setVisible(false);
			
			tombolUpDel.setVisible(false);
			addBtn.setVisible(false);
			upProduct.setVisible(false);
			newPrice.setVisible(false);
			updateBut.setVisible(false);
			
			 productName.clear();
			    productPrice.clear();
			    productDesc.clear();
			
		});
		
		//Klik tomnol back yang add product
		backBut.setOnMouseClicked(e -> {
		    labelname.setText("Welcome, " + username);
		    labeldesc.setText("Select a product to update");
		    eachprice.setText("Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
		    
		    eachprice.setVisible(false);
	        tombolUpDel.setVisible(false);
	        addBtn.setVisible(true);
	        addGP.setVisible(false);;
		    
		});
		// klik tombol add Product untuk add
		addBut.setOnMouseClicked(e -> {
			
			   // Mendapatkan nilai dari input
		    String newProductName = productName.getText();
		    String newProductPriceText = productPrice.getText();
		    String newProductDesc = productDesc.getText();

		    // Validasi: Semua bidang harus diisi
		    if (newProductName.isEmpty() || newProductPriceText.isEmpty() || newProductDesc.isEmpty()) {
		        // Menampilkan Error Alert jika salah satu input tidak terisi
		        showAlert(Alert.AlertType.ERROR, "Input tidak valid", "Semua bidang harus diisi.");
		        return;
		    }

		    // Validasi: Nama produk harus unik
		    if (isProductNameExists(newProductName)) {
		        // Menampilkan Error Alert jika nama produk sudah ada
		        showAlert(Alert.AlertType.ERROR, "Input tidak valid", "Nama produk harus unik.");
		        return;
		    }

		    // Validasi: Harga produk harus lebih dari 0
		    double newProductPrice = Double.parseDouble(newProductPriceText);
		    if (newProductPrice <= 0) {
		        // Menampilkan Error Alert jika harga produk tidak valid
		        showAlert(Alert.AlertType.ERROR, "Input tidak valid", "Harga produk harus lebih dari 0.");
		        return;
		    }

		    // Jika semua validasi terpenuhi, tambahkan produk ke listview dan database
		    itemsList.add(new item(newProductName, newProductPrice));

		    // Update tampilan listview
		    updateListView();

		    // Tampilkan Information Alert untuk notifikasi berhasil
		    showAlert(Alert.AlertType.INFORMATION, "Tambah Produk Berhasil", "Produk berhasil ditambahkan.");
			labelname.setText("Welcome, " + username);
			labeldesc.setText("Select a product to update");
			eachprice.setText(
					"Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
			

		    eachprice.setVisible(false);
	        tombolUpDel.setVisible(false);
	        addBtn.setVisible(true);
	        addGP.setVisible(false);;
			
		
		});
		
		backUpdate.setOnMouseClicked(e -> {
		    labelname.setText("Welcome, " + username);
		    labeldesc.setText("Select a product to update");
		    eachprice.setText("Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
		    
		    eachprice.setVisible(false);
	        tombolUpDel.setVisible(false);
	        addBtn.setVisible(true);
	        addGP.setVisible(false);;
		    
		});
		
		backRemove.setOnMouseClicked(e -> {
			 labelname.setText("Welcome, " + username);
			    labeldesc.setText("Select a product to update");
			    eachprice.setText("Price: Rp. " + getItemPrice(itemsList, listView.getSelectionModel().getSelectedItem()));
			    
			    eachprice.setVisible(false);
		        tombolUpDel.setVisible(false);
		        addBtn.setVisible(true);
		        addGP.setVisible(false);;
		});

		homeMenu.setOnAction(event -> new HomePageAdmin(primaryStage, username));
		logoutMenuItem.setOnAction(event -> new login(primaryStage, mainInstance));
		manageProductsItem.setOnAction(event -> new ManageProducts(primaryStage, username));

	}

	private boolean deleteProduct(String productName) {
	    for (item i : itemsList) {
	        if (i.getObjectname().equals(productName)) {
	            itemsList.remove(i);
	            updateListView();
	            return true;  // Hapus berhasil
	        }
	    }
	    return false;  // Produk tidak ditemukan
	}
	
	private boolean isProductNameExists(String productName) {
	    for (item i : itemsList) {
	        if (i.getObjectname().equals(productName)) {
	            return true;  // Nama produk sudah ada
	        }
	    }
	    return false;  // Nama produk belum ada
	}

	private void updateListView() {
	    ObservableList<String> items = FXCollections.observableArrayList();
	    for (item i : itemsList) {
	        items.add(i.getObjectname());
	    }
	    listView.getItems().clear();
	    listView.setItems(items);
	}

	
	private boolean updateItemPrice(ArrayList<item> items, String itemName, double newPrice) {
	    for (item i : items) {
	        if (i.getObjectname().equals(itemName)) {
	            i.setObjectprice(newPrice);
	            return true;  // Pembaruan berhasil
	        }
	    }
	    return false;  // Pembaruan gagal karena item tidak ditemukan
	}
	
	private double getItemPrice(ArrayList<item> items, String itemName) {
		for (item i : items) {
			if (i.getObjectname().equals(itemName)) {
				return i.getObjectprice();
			}
		}
		return 0.0;
	}
	
	private void showAlert(Alert.AlertType alertType, String title, String content) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(content);
	    alert.showAndWait();
	}

	public void initialize() {
		labeltitle.setFont(Font.font("Arial", FontWeight.BOLD, 42));

		listView.setMaxWidth(400);

		homeMenu.getItems().add(homePageMenuItem);
		manageProducts.getItems().add(manageProductsItem);
		accountMenu.getItems().addAll(logoutMenuItem);
		menuBar.getMenus().addAll(homeMenu, manageProducts, accountMenu);
		VBox.setMargin(hbox, new Insets(0, 0, 0, 0));

		labeltitle.setPadding(new Insets(10));
		itemDescriptionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		itemDescriptionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

		tombolUpDel.setSpacing(10);

		intro.setSpacing(10);

		productName.setPromptText("Input Product Name...");
		productPrice.setPromptText("Input Product Price...");
		productDesc.setPromptText("Input Product Description...");
		
		newPrice.setPromptText("Input New Price...");
		
		
	addbackBut.setSpacing(10);
	
	updateBut.setSpacing(10);
	
	deleteBut.setSpacing(10);
	
		addGP.add(inputProductName, 0, 0);
		addGP.add(productName, 0, 1);
		
		addGP.add(inputProductPrice, 0, 2);
		addGP.add(productPrice, 0, 3);

		addGP.add(inputProductDesc, 0, 4);
		addGP.add(productDesc, 0, 5);
		
		addGP.add(addbackBut, 0, 6);
		
		addGP.add(upProduct, 0, 0);
		addGP.add(newPrice, 0, 1);
		addGP.add(updateBut, 0, 2);
		
		addGP.add(delLabel, 0, 0);
		addGP.add(deleteBut, 0, 1);
		
		addGP.setVgap(10);
		
	
		
		

		eachprice.setVisible(false);
		tombolUpDel.setVisible(false);
		addGP.setVisible(false);
		
		

		VBox mainVB = new VBox(menuBar, tampilanjudul, hbox);
		mainVB.setSpacing(4);
		VBox.setVgrow(mainVB, Priority.ALWAYS);
		HBox.setHgrow(listView, Priority.ALWAYS);
		hbox.setSpacing(15);
		hbox.setPadding(new Insets(10));
		manageProductsScene = new Scene(mainVB, 1000, 800);
	}

	public void loadListData() {
		itemsList.add(new item("Product A", 10000.0));
		itemsList.add(new item("Product B", 15000.0));
		itemsList.add(new item("Product C", 20000.0));

		ObservableList<String> items = FXCollections.observableArrayList();
		for (item i : itemsList) {
			items.add(i.getObjectname());
		}

		listView.getItems().clear();
		listView.setItems(items);

	}

}