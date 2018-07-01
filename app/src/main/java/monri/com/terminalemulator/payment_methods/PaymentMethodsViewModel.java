package monri.com.terminalemulator.payment_methods;

import monri.com.terminalemulator.order.OrderPreviewViewModel;

/**
 * Created by jasminsuljic on 01/07/2018.
 * TerminalEmulator
 */

public class PaymentMethodsViewModel {
    private final OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest;

    public PaymentMethodsViewModel(OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest) {
        this.paymentMethodsRequest = paymentMethodsRequest;
    }
}
