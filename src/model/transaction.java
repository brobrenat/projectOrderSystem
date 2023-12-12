package model;

import java.util.ArrayList;

public class transaction {

	private String transactionId;
    private ArrayList<cart> cartItems;

    public transaction(String transactionId, ArrayList<cart> cartItems) {
        this.transactionId = transactionId;
        this.cartItems = cartItems;
    }

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public ArrayList<cart> getCartItems() {
		return cartItems;
	}

	public void setCartItems(ArrayList<cart> cartItems) {
		this.cartItems = cartItems;
	}





}
