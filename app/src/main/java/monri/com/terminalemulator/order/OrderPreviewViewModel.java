package monri.com.terminalemulator.order;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import monri.com.terminalemulator.Api;
import monri.com.terminalemulator.PendingOrder;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

public class OrderPreviewViewModel {

    private final Api api;
    private final String orderNumber;
    private final CompositeDisposable disposable = new CompositeDisposable();

    final PublishSubject<List<Product>> behaviorSubject = PublishSubject.create();
    final PublishSubject<PendingOrder> pendingOrder = PublishSubject.create();

    final PublishSubject<Boolean> isLoading = PublishSubject.create();
    final PublishSubject<Throwable> error = PublishSubject.create();

    OrderPreviewViewModel(Api api, String orderNumber) {
        this.api = api;

        this.orderNumber = orderNumber;
    }

    void load() {

        isLoading.onNext(true);

        final Disposable products = api.getPendingOrder(orderNumber, "products")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PendingOrder>() {
                    @Override
                    public void accept(PendingOrder pendingOrder) throws Exception {
                        OrderPreviewViewModel.this.pendingOrder.onNext(pendingOrder);
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

    public void cardScanned(String tag) {

    }
}
