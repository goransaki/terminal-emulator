package monri.com.terminalemulator.main.di;

import dagger.Module;
import dagger.Provides;
import monri.com.terminalemulator.main.MainViewModel;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

@Module
public class MainModule {
    @Provides
    MainViewModel provideViewModel() {
        return new MainViewModel();
    }
}
