package monri.com.terminalemulator.main.di;

import dagger.Subcomponent;
import monri.com.terminalemulator.main.view.MainActivity;
import monri.com.terminalemulator.di.ActivityScope;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */


@ActivityScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
