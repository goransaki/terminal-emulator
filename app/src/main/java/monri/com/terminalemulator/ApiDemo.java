package monri.com.terminalemulator;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by jasminsuljic on 01/07/2018.
 * TerminalEmulator
 */

public class ApiDemo implements Api {

    private final Context context;

    public ApiDemo(Context context) {
        this.context = context;
    }

    @Override
    public Single<EnrollmentStatus> getEnrollmentStatus(String type, String id) {
        // For now only one card is enrolled
        if (type.equals("card") && id.equals("[5, -122, -65, -81, 92, 80, 0]")) {
            return load("enrollment_status_enrolled.json", EnrollmentStatus.class);
        } else {
            return Single.just(new EnrollmentStatus("not_enrolled", null));
        }
    }

    @Override
    public Single<User> getEnrolledUser(String id) {
        return load("user_view_1.json", User.class);
    }

    @Override
    public Single<PaymentResponse> payWithCreditCard(String orderNumber, NewCreditCardPayment newCreditCardPayment) {
        return load("pay_with_credit_card.json", PaymentResponse.class);
    }

    @Override
    public Single<PaymentResponse> payWithSavedCreditCard(String orderNumber, SavedCreditCardPayment savedCreditCardPayment) {
        return load("pay_with_saved_credit_card.json", PaymentResponse.class);
    }

    @Override
    public Single<PaymentResponse> payWithEwallet(String orderNumber, EWalletPayment EWalletPayment) {
        return load("pay_with_ewallet.json", PaymentResponse.class);
    }

    @Override
    public Single<PendingOrder> getPendingOrder(String orderNumber, String expand) {
        return load(String.format("completed_order_%s.json", orderNumber), PendingOrder.class);
    }

    @Override
    public Single<PendingOrder> getCompletedOrder(String orderNumber) {
        return load("pending_order_2.json", PendingOrder.class);
    }

    @Override
    public Single<PaymentMethodsResponse> getPaymentMethods(String userId) {
        return load("payment_methods_1.json", PaymentMethodsResponse.class);
    }

    private <T> Single<T> load(final String filename, final Class<T> clazz) {

        return Single.create(new SingleOnSubscribe<T>() {

            @Override
            public void subscribe(SingleEmitter<T> emitter) throws Exception {
                String json;
                try {
                    InputStream is = context.getAssets().open(filename);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                    emitter.onSuccess(new Gson().fromJson(json, clazz));
                } catch (IOException ex) {
                    emitter.onError(ex);
                }
            }
        }).delay(200, TimeUnit.MILLISECONDS);
    }
}
