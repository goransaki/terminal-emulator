package monri.com.terminalemulator.order;

import dagger.Subcomponent;
import monri.com.terminalemulator.di.ActivityScope;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

@ActivityScope
@Subcomponent(modules = OrderPreviewModule.class)
public interface OrderPreviewComponent {
    void inject(OrderPreviewActivity orderPreviewActivity);
}
