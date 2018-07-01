package monri.com.terminalemulator.payment_methods;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import monri.com.terminalemulator.Api;
import monri.com.terminalemulator.PaymentMethod;
import monri.com.terminalemulator.PaymentMethodsResponse;
import monri.com.terminalemulator.order.OrderPreviewViewModel;

/**
 * Created by jasminsuljic on 01/07/2018.
 * TerminalEmulator
 */

public class PaymentMethodsViewModel {
    private final OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest;
    private final Api api;

    private CompositeDisposable disposable = new CompositeDisposable();

    final PublishSubject<PaymentMethodsResponse> paymentMethodsResponseSubject;

    public PaymentMethodsViewModel(OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest, Api api) {
        this.paymentMethodsRequest = paymentMethodsRequest;
        this.api = api;
        paymentMethodsResponseSubject = PublishSubject.create();
    }

    public void load() {
        final Disposable subscribe = api
                .getPaymentMethods(paymentMethodsRequest.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PaymentMethodsResponse>() {
                    @Override
                    public void accept(PaymentMethodsResponse paymentMethodsResponse) throws Exception {
                        paymentMethodsResponseSubject.onNext(paymentMethodsResponse);
                    }
                });

        disposable.add(subscribe);

    }

    void dispose() {
        disposable.dispose();
    }

    public void payWith(PaymentMethod paymentMethod) {

    }
}
