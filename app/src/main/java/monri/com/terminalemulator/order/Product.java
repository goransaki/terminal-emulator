package monri.com.terminalemulator.order;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

public class Product {

    String name;
    @SerializedName("quantity")
    int quantity;
    @SerializedName("price")
    int price;

    public Product(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalPrice() {
        return price * quantity;
    }
}
