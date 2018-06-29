package termina_emulator.monri.com.terminalemulator;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask;
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils;

public class MainActivity extends AppCompatActivity implements CardNfcAsyncTask.CardNfcInterface {

    private NfcAdapter mNfcAdapter;
    private CardNfcUtils mCardNfcUtils;
    private boolean mIntentFromCreate;
    private CardNfcAsyncTask mCardNfcAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            //do something if there are no nfc module on device
        } else {
            //do something if there are nfc module on device

            mCardNfcUtils = new CardNfcUtils(this);
            //next few lines here needed in case you will scan credit card when app is closed
            mIntentFromCreate = true;
            onNewIntent(getIntent());
        }

        findViewById(R.id.scan_nfc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNFC();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntentFromCreate = false;
        if (mNfcAdapter != null && !mNfcAdapter.isEnabled()) {
            //show some turn on nfc dialog here. take a look in the samle ;-)
        } else if (mNfcAdapter != null) {
            mCardNfcUtils.enableDispatch();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mCardNfcUtils.disableDispatch();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            //this - interface for callbacks
            //intent = intent :)
            //mIntentFromCreate - boolean flag, for understanding if onNewIntent() was called from onCreate or not
            mCardNfcAsyncTask = new CardNfcAsyncTask.Builder(this, intent, mIntentFromCreate)
                    .build();
        }
    }

    private void scanNFC() {

    }

    @Override
    public void startNfcReadCard() {
        //notify user that scannig start
    }

    @Override
    public void cardIsReadyToRead() {
        String card = mCardNfcAsyncTask.getCardNumber();
        String expiredDate = mCardNfcAsyncTask.getCardExpireDate();
        String cardType = mCardNfcAsyncTask.getCardType();

        Toast.makeText(this, String.format("card: %s, expiredDate: %s, cardType: %s", card, expiredDate, cardType), Toast.LENGTH_LONG).show();

    }

    @Override
    public void doNotMoveCardSoFast() {
        //notify user do not move the card
    }

    @Override
    public void unknownEmvCard() {
        //notify user that current card has unnown nfc tag
    }

    @Override
    public void cardWithLockedNfc() {
        //notify user that current card has locked nfc tag
    }

    @Override
    public void finishNfcReadCard() {
        //notify user that scannig finished
    }
}
