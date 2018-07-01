package monri.com.terminalemulator.payment_methods;

import dagger.Subcomponent;
import monri.com.terminalemulator.di.ActivityScope;

/**
 * Created by jasminsuljic on 01/07/2018.
 * TerminalEmulator
 */

@ActivityScope
@Subcomponent(modules = PaymentMethodsModule.class)
public interface PaymentMethodsComponent {
    void inject(PaymentMethodsActivity paymentMethodsActivity);
}
