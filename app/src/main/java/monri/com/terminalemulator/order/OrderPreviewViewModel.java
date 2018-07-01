package monri.com.terminalemulator.order;

import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import monri.com.terminalemulator.Api;
import monri.com.terminalemulator.EnrollmentStatus;
import monri.com.terminalemulator.NewCreditCardPayment;
import monri.com.terminalemulator.PaymentResponse;
import monri.com.terminalemulator.PendingOrder;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

public class OrderPreviewViewModel {

    private final Api api;
    private final String orderNumber;
    private final CompositeDisposable disposable = new CompositeDisposable();

    final PublishSubject<PendingOrder> pendingOrder = PublishSubject.create();

    final PublishSubject<Boolean> isLoading = PublishSubject.create();
    final PublishSubject<Throwable> error = PublishSubject.create();
    final PublishSubject<Boolean> paymentInProgress = PublishSubject.create();
    final PublishSubject<Boolean> showRecyclerView = PublishSubject.create();
    final PublishSubject<PaymentMethodsRequest> continueToPaymentMethodsPicker = PublishSubject.create();
    final PublishSubject<PaymentResponse> paymentResponse = PublishSubject.create();

    OrderPreviewViewModel(Api api, String orderNumber) {
        this.api = api;

        this.orderNumber = orderNumber;
    }

    void load() {

        paymentInProgress.onNext(false);
        showRecyclerView.onNext(false);
        isLoading.onNext(true);

        final Disposable products = api.getPendingOrder(orderNumber, "products")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PendingOrder>() {
                    @Override
                    public void accept(PendingOrder pendingOrder) throws Exception {
                        OrderPreviewViewModel.this.pendingOrder.onNext(pendingOrder);
                        isLoading.onNext(false);
                        showRecyclerView.onNext(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        error.onNext(throwable);
                    }
                });

        disposable.add(products);
    }

    void dispose() {
        disposable.dispose();
    }

    void cardScanned(final String tag, final String pan, final String card, final String expDate) {
        // execute payment on this screen?

        paymentInProgress.onNext(true);

        final Disposable subscribe = api
                .getEnrollmentStatus("card", tag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EnrollmentStatus>() {
                    @Override
                    public void accept(EnrollmentStatus enrollmentStatus) throws Exception {
                        if (enrollmentStatus.getStatus().equals("enrolled")) {
                            paymentInProgress.onNext(false);
                            continueToPaymentMethodsPicker.onNext(new PaymentMethodsRequest(enrollmentStatus.getUserId(), tag, orderNumber));
                        } else {
                            executePayment(pan, card, expDate, tag);
                        }
                    }
                });

        disposable.add(subscribe);
    }

    private void executePayment(String pan, String card, String expDate, String tag) {
        // TODO: execute payment with credit card
        Log.d("OrderPreview", String.format("execute payment %s", tag));

        final Disposable subscribe = api
                .payWithCreditCard(orderNumber, new NewCreditCardPayment(pan, expDate, card, tag))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PaymentResponse>() {
                    @Override
                    public void accept(PaymentResponse paymentResponse) throws Exception {
                        paymentInProgress.onNext(false);
                        OrderPreviewViewModel.this.paymentResponse.onNext(paymentResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // TODO: handle payment failure
                    }
                });

        disposable.add(subscribe);
    }


    public static class PaymentMethodsRequest {
        final String userId;
        final String id;
        final String orderNumber;


        public PaymentMethodsRequest(String userId, String id, String orderNumber) {
            this.userId = userId;
            this.id = id;
            this.orderNumber = orderNumber;
        }

        public String getUserId() {
            return userId;
        }

        public String getId() {
            return id;
        }

        public String getOrderNumber() {
            return orderNumber;
        }
    }
}
