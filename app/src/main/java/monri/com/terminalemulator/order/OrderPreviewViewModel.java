package monri.com.terminalemulator.order;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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

    OrderPreviewViewModel(Api api, String orderNumber) {
        this.api = api;

        this.orderNumber = orderNumber;
    }

    void load() {
        // TODO: add all expand parts
        final Disposable subscribe = api.getPendingOrder(orderNumber, "products").map(new Function<PendingOrder, List<Product>>() {
            @Override
            public List<Product> apply(PendingOrder pendingOrder) throws Exception {
                return pendingOrder.getProducts();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Product>>() {
                    @Override
                    public void accept(List<Product> products) throws Exception {
                        behaviorSubject.onNext(products);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

        disposable.add(subscribe);
    }

    void dispose() {
        disposable.dispose();
    }

    public void cardScanned(String tag) {

    }
}
