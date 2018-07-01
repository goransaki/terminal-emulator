package monri.com.terminalemulator.main.view;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import javax.inject.Inject;

import monri.com.terminalemulator.R;
import monri.com.terminalemulator.app.App;
import monri.com.terminalemulator.main.MainViewModel;
import monri.com.terminalemulator.main.di.MainComponent;
import monri.com.terminalemulator.main.di.MainModule;
import monri.com.terminalemulator.order.OrderPreviewActivity;

public class MainActivity extends AppCompatActivity {

    @Inject
    MainViewModel mainViewModel;

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    public static void setupToolbar(AppCompatActivity appCompatActivity, Toolbar toolbar) {
        appCompatActivity.setSupportActionBar(toolbar);
        final ActionBar actionBar = appCompatActivity.getActionBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainComponent mainComponent = App.getApp(this).getAppComponent().plus(new MainModule());
        mainComponent.inject(this);

        findViewById(R.id.example_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exampleOne();
            }
        });

        findViewById(R.id.example_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exampleTwo();
            }
        });
    }

    private void exampleOne() {
        final Intent intent = OrderPreviewActivity.createIntent(this, "1");
        startActivity(intent);
    }

    private void exampleTwo() {
        final Intent intent = OrderPreviewActivity.createIntent(this, "2");
        startActivity(intent);
    }

}
