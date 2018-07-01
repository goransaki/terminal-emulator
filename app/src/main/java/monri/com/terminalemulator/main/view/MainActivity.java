package monri.com.terminalemulator.main.view;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask;
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils;

import javax.inject.Inject;

import monri.com.terminalemulator.R;
import monri.com.terminalemulator.app.App;
import monri.com.terminalemulator.main.MainViewModel;
import monri.com.terminalemulator.main.di.MainComponent;
import monri.com.terminalemulator.main.di.MainModule;
import monri.com.terminalemulator.order.OrderPreviewActivity;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    private CardNfcUtils mCardNfcUtils;
    private boolean mIntentFromCreate;
    private CardNfcAsyncTask mCardNfcAsyncTask;

    @Inject
    MainViewModel mainViewModel;

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
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
