package monri.com.terminalemulator;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

public class NewCreditCardPayment {
    @SerializedName("pan")
    String pan;
    @SerializedName("exp_date")
    String expDate;
    @SerializedName("card_holder")
    String cardHolder;
    @SerializedName("tag")
    String tag;

    public NewCreditCardPayment(String pan, String expDate, String cardHolder, String tag) {
        this.pan = pan;
        this.expDate = expDate;
        this.cardHolder = cardHolder;
        this.tag = tag;
    }

    public String getPan() {
        return pan;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getTag() {
        return tag;
    }
}
