package monri.com.terminalemulator.di.app;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import monri.com.terminalemulator.Api;
import monri.com.terminalemulator.ApiDemo;
import monri.com.terminalemulator.ApiProxy;
import monri.com.terminalemulator.app.App;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jasminsuljic on 30/06/2018.
 * TerminalEmulator
 */

@Module
public class AppModule {

    private final App app;
    private final String baseUrl;

    public AppModule(App app, String baseUrl) {
        this.app = app;
        this.baseUrl = baseUrl;
    }

    @Singleton
    @Provides
    public App getApp() {
        return app;
    }

    @Singleton
    @Provides
    public Context getContext() {
        return app.getApplicationContext();
    }

    @Singleton
    @Provides
    Resources getResources() {
        return app.getResources();
    }

    @Provides
    Gson gson() {
        return new Gson();
    }

    @Provides
    Retrofit getUserDefinedRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    @Provides
    OkHttpClient provideOkHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(logging);

        return builder.build();
    }

    @Singleton
    @Provides
    Api provideApi(Retrofit retrofit, App app) {
        return new ApiProxy(retrofit.create(Api.class), new ApiDemo(app.getApplicationContext()));
    }
}
