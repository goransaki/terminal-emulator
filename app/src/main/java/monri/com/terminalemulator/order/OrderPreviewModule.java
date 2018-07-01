package monri.com.terminalemulator.order;

import dagger.Module;
import dagger.Provides;
import monri.com.terminalemulator.Api;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

@Module
public class OrderPreviewModule {

    private final String orderNumber;

    OrderPreviewModule(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Provides
    OrderPreviewViewModel orderPreviewViewModel(Api api) {
        return new OrderPreviewViewModel(api, orderNumber);
    }
}
