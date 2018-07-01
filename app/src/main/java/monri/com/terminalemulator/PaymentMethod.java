package monri.com.terminalemulator;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jasminsuljic on 01/07/2018.
 * TerminalEmulator
 */

public class PaymentMethod {
    String type;
    @SerializedName("default_pm")
    boolean defaultPaymentMethod;

    // CC related
    @SerializedName("masked_pan")
    String maskedPan;
    @SerializedName("card_type")
    String cardType;
    @SerializedName("exp_date")
    String expDate;
    @SerializedName("card_holder")
    String cardHolder;

    // BANK ACCOUNT RELATED
    @SerializedName("name")
    String name;
    @SerializedName("bank_details")
    String bankDetails;

    // Crypto currency related
    @SerializedName("currency_type")
    String currencyType;
    @SerializedName("address")
    String address;

    public boolean isCard() {
        return type.equals("card");
    }

    public boolean isBankAccount() {
        return type.equals("bank_account");
    }

    public boolean isCryptoCurrency() {
        return type.equals("crypto_currency");
    }

    public String getType() {
        return type;
    }

    public boolean isDefaultPaymentMethod() {
        return defaultPaymentMethod;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public String getCardType() {
        return cardType;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getName() {
        return name;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public String getAddress() {
        return address;
    }
}
