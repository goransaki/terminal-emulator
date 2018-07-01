package monri.com.terminalemulator.payment_methods;

import dagger.Module;
import dagger.Provides;
import monri.com.terminalemulator.Api;
import monri.com.terminalemulator.order.OrderPreviewViewModel;

/**
 * Created by jasminsuljic on 01/07/2018.
 * TerminalEmulator
 */

@Module
public class PaymentMethodsModule {
    private final OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest;

    PaymentMethodsModule(OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest) {
        this.paymentMethodsRequest = paymentMethodsRequest;
    }

    @Provides
    PaymentMethodsViewModel paymentMethodsViewModel(Api api) {
        return new PaymentMethodsViewModel(paymentMethodsRequest, api);
    }
}
