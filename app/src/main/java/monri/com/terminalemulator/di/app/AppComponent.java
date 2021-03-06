package monri.com.terminalemulator.di.app;

import javax.inject.Singleton;

import dagger.Component;
import monri.com.terminalemulator.main.di.MainComponent;
import monri.com.terminalemulator.main.di.MainModule;
import monri.com.terminalemulator.order.OrderPreviewComponent;
import monri.com.terminalemulator.order.OrderPreviewModule;
import monri.com.terminalemulator.payment_methods.PaymentMethodsComponent;
import monri.com.terminalemulator.payment_methods.PaymentMethodsModule;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    MainComponent plus(MainModule module);

    OrderPreviewComponent plus(OrderPreviewModule module);

    PaymentMethodsComponent plus(PaymentMethodsModule module);
}
