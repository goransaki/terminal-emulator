package monri.com.terminalemulator;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jasminsuljic on 01/07/2018.
 * TerminalEmulator
 */

public class PaymentMethod {

    @SerializedName("title")
    String title;

    @SerializedName("subtitle")
    String subtitle;

    @SerializedName("image")
    String image;

    @SerializedName("balance")
    String balance;

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getImage() {
        return image;
    }

    public String getBalance() {
        return balance;
    }
}
