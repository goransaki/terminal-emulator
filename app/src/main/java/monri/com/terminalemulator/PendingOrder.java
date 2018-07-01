package monri.com.terminalemulator;

import java.util.List;

import monri.com.terminalemulator.order.Product;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

public class PendingOrder {
    List<Product> products;
    Store store;
    int id;


    public List<Product> getProducts() {
        return products;
    }

    public Store getStore() {
        return store;
    }

    public int getId() {
        return id;
    }
}
