package termina_emulator.monri.com.terminalemulator;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    @GET("enrollment/status/{type}/{id}")
    Single<EnrollmentStatus> getEnrollmentStatus(@Path("type") String type, @Path("id") String id);

    /**
     * This endpoint is used to receive user details so we can improve one to one relationship with
     * customer during checkout.
     *
     * @param id Enrollment id
     * @return
     */
    @GET("user/enrollment/{id}")
    Single<User> getEnrolledUser(@Path("id") String id);

    /**
     * New payment with tokenized credit card details.
     *
     * @param newCreditCardPayment new credit card payment details
     * @return
     */
    @POST("order/{order_number}/pay/credit_card")
    Single<PaymentResponse> payWithCreditCard(@Path("order_number") String orderNumber,
                                              @Body NewCreditCardPayment newCreditCardPayment);

    /**
     * New payment with enrolled user and saved card
     *
     * @param orderNumber            orderNumber
     * @param savedCreditCardPayment savedCreditCardPayment
     * @return
     */
    @POST("order/{order_number}/pay/cc")
    Single<PaymentResponse> payWithSavedCreditCard(
            @Path("order_number") String orderNumber,
            @Body SavedCreditCardPayment savedCreditCardPayment);

    /**
     * New payment with
     *
     * @param orderNumber    orderNumber
     * @param EWalletPayment EWalletPayment
     * @return
     */
    @POST("order/{order_number}/pay/ewallet")
    Single<PaymentResponse> payWithEwallet(
            @Path("order_number") String orderNumber,
            @Body EWalletPayment EWalletPayment);

    @GET("order/pending/{order_number}")
    Single<PendingOrder> getPendingOrder(@Path("order_number") String orderNumber);

    @GET("order/completed/{order_number}")
    Single<PendingOrder> getCompletedOrder(@Path("order_number") String orderNumber);

}
