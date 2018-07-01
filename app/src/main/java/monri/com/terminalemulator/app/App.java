package monri.com.terminalemulator.app;

import android.app.Application;
import android.content.Context;

import monri.com.terminalemulator.di.app.AppComponent;
import monri.com.terminalemulator.di.app.AppModule;
import monri.com.terminalemulator.di.app.DaggerAppComponent;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

public class App extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this, "http://ec79344c.ngrok.io/v1/")).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static App getApp(Context context) {
        return (App) context.getApplicationContext();
    }
}
