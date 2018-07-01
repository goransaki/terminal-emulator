package monri.com.terminalemulator;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

public interface Api {
    /**
     * Retrieve enrollment status for type = card | device and id [card, device]
     *
     * @param type
     * @param id
     * @return
     */
    @GET("enrollments/status")
    Single<EnrollmentStatus> getEnrollmentStatus(
            @Query("type") String type,
            @Query("id") String id
    );

    /**
     * This endpoint is used to receive user details so we can improve one to one relationship with
     * customer during checkout.
     *
     * @param id Enrollment id
     * @return
     */
    @GET("users/view")
    Single<User> getEnrolledUser(@Query("id") String id);

    /**
     * New payment with tokenized credit card details.
     *
     * @param newCreditCardPayment new credit card payment details
     * @return
     */
    @POST("orders/pay-credit-card")
    Single<PaymentResponse> payWithCreditCard(@Query("orderNumber") String orderNumber,
                                              @Body NewCreditCardPayment newCreditCardPayment);

    /**
     * New payment with enrolled user and saved card
     *
     * @param orderNumber            orderNumber
     * @param savedCreditCardPayment savedCreditCardPayment
     * @return
     */
    @POST("orders/pay-with-saved-credit-card")
    Single<PaymentResponse> payWithSavedCreditCard(
            @Query("orderNumber") String orderNumber,
            @Body SavedCreditCardPayment savedCreditCardPayment);

    /**
     * New payment with
     *
     * @param orderNumber    orderNumber
     * @param EWalletPayment EWalletPayment
     * @return
     */
    @POST("orders/pay-ewallet")
    Single<PaymentResponse> payWithEwallet(
            @Query("orderNumber") String orderNumber,
            @Body EWalletPayment EWalletPayment);

    @GET("orders/pending")
    Single<PendingOrder> getPendingOrder(
            @Query("orderNumber") String orderNumber,
            @Query("expand") String expand
    );

    @GET("orders/completed")
    Single<PendingOrder> getCompletedOrder(@Query("orderNumber") String orderNumber);

    @GET("user/pm")
    Single<PaymentMethodsResponse> getPaymentMethods(@Query("user_id") String userId);
}
