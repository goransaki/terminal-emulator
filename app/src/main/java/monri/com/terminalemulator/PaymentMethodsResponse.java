package monri.com.terminalemulator;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jasminsuljic on 01/07/2018.
 * TerminalEmulator
 */

public class PaymentMethodsResponse {
    @SerializedName("methods")
    List<PaymentMethod> list;

    public List<PaymentMethod> getList() {
        return list;
    }
}
