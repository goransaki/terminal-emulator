package monri.com.terminalemulator;

import io.reactivex.Single;

/**
 * Created by jasminsuljic on 01/07/2018.
 * TerminalEmulator
 */

public class ApiProxy implements Api {

    private final Api httpApi;
    private final ApiDemo demoApi;

    public ApiProxy(Api httpApi, ApiDemo demoApi) {
        this.httpApi = httpApi;
        this.demoApi = demoApi;
    }

    @Override
    public Single<EnrollmentStatus> getEnrollmentStatus(String type, String id) {
        return demoApi.getEnrollmentStatus(type, id);
    }

    @Override
    public Single<User> getEnrolledUser(String id) {
        return demoApi.getEnrolledUser(id);
    }

    @Override
    public Single<PaymentResponse> payWithCreditCard(String orderNumber, NewCreditCardPayment newCreditCardPayment) {
        return demoApi.payWithCreditCard(orderNumber, newCreditCardPayment);
    }

    @Override
    public Single<PaymentResponse> payWithSavedCreditCard(String orderNumber, SavedCreditCardPayment savedCreditCardPayment) {
        return demoApi.payWithSavedCreditCard(orderNumber, savedCreditCardPayment);
    }

    @Override
    public Single<PaymentResponse> payWithEwallet(String orderNumber, EWalletPayment EWalletPayment) {
        return demoApi.payWithEwallet(orderNumber, EWalletPayment);
    }

    @Override
    public Single<PendingOrder> getPendingOrder(String orderNumber, String expand) {
        return demoApi.getPendingOrder(orderNumber, expand);
    }

    @Override
    public Single<PendingOrder> getCompletedOrder(String orderNumber) {
        return demoApi.getCompletedOrder(orderNumber);
    }

    @Override
    public Single<PaymentMethodsResponse> getPaymentMethods(String userId) {
        return demoApi.getPaymentMethods(userId);
    }
}
