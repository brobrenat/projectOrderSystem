package model;

import java.util.ArrayList;

public class cartmanager {
    private static final cartmanager instance = new cartmanager();
    private ArrayList<cart> cartItems = new ArrayList<>();

    private cartmanager() {
    }

    public static cartmanager getInstance() {
        return instance;
    }

    public ArrayList<cart> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<cart> cartItems) {
        this.cartItems = cartItems;
    }

}
