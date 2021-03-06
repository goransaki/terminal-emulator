package monri.com.terminalemulator.order;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask;
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import monri.com.terminalemulator.PaymentResponse;
import monri.com.terminalemulator.PendingOrder;
import monri.com.terminalemulator.R;
import monri.com.terminalemulator.app.App;
import monri.com.terminalemulator.payment_methods.PaymentMethodsActivity;

public class OrderPreviewActivity extends AppCompatActivity implements CardNfcAsyncTask.CardNfcInterface {

    private NfcAdapter mNfcAdapter;
    private CardNfcUtils mCardNfcUtils;
    private boolean mIntentFromCreate;
    private CardNfcAsyncTask mCardNfcAsyncTask;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    OrderPreviewViewModel viewModel;

    TextView orderSummary;

    RecyclerView recyclerView;

    TextView totalPrice;

    FrameLayout loadingView;

    LinearLayoutManager linearLayoutManager;

    View paymentInProgress;

    View trxSuccess;

    OrderPreviewProductAdapter orderPreviewProductAdapter;

    public static Intent createIntent(Context context, String orderNumber) {
        Intent intent = new Intent(context, OrderPreviewActivity.class);
        intent.putExtra("order_number", orderNumber);
        return intent;
    }

    void back() {
        final ActionBar   supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        supportActionBar.setHomeAsUpIndicator(R.drawable.back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        back();

        final String orderNumber = getIntent().getStringExtra("order_number");

        getSupportActionBar().setTitle(String.format("Order %s", orderNumber));

        App.getApp(this).getAppComponent().plus(new OrderPreviewModule(orderNumber)).inject(this);

        loadViews();

        setupRecyclerAdapter();

        setupNfc();

        bindEvents();

        viewModel.load();
    }

    private void bindEvents() {

        final Disposable loading = viewModel.isLoading.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                showLoadingScreen(aBoolean);
            }
        });

        final Disposable error = viewModel.error.subscribe(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                handleError(throwable);
            }
        });

        final Disposable subscribe = viewModel.pendingOrder.subscribe(new Consumer<PendingOrder>() {
            @Override
            public void accept(PendingOrder pendingOrder) throws Exception {
                final List<Product> products = pendingOrder.getProducts();
                orderPreviewProductAdapter.setProducts(products);
                totalPrice.setText(String.format("RSD %d", pendingOrder.totalPrice()));
            }
        });

        final Disposable subscribe1 = viewModel.continueToPaymentMethodsPicker.subscribe(new Consumer<OrderPreviewViewModel.PaymentMethodsRequest>() {
            @Override
            public void accept(OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest) throws Exception {
                navigateToPaymentMethodsRequest(paymentMethodsRequest);
            }
        });

        final Disposable subscribe2 = viewModel.paymentInProgress.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                paymentInProgress.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
            }
        });

        final Disposable subscribe3 = viewModel.showRecyclerView.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                recyclerView.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
            }
        });

        final Disposable subscribe4 = viewModel.paymentResponse.subscribe(new Consumer<PaymentResponse>() {
            @Override
            public void accept(PaymentResponse paymentResponse) throws Exception {
                trxSuccess.setVisibility(View.VISIBLE);
            }
        });

        compositeDisposable.add(subscribe);
        compositeDisposable.add(loading);
        compositeDisposable.add(error);
        compositeDisposable.add(subscribe1);
        compositeDisposable.add(subscribe2);
        compositeDisposable.add(subscribe3);
        compositeDisposable.add(subscribe4);
    }

    private void navigateToPaymentMethodsRequest(OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest) {
        // TODO: navigate to payment methods request
        final Intent intent = PaymentMethodsActivity.createIntent(this, paymentMethodsRequest);
        startActivity(intent);
    }

    private void handleError(Throwable throwable) {
        // TODO
    }

    private void showLoadingScreen(Boolean aBoolean) {
        loadingView.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
    }

    private void setupRecyclerAdapter() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        orderPreviewProductAdapter = new OrderPreviewProductAdapter();
        recyclerView.setAdapter(orderPreviewProductAdapter);
    }

    private void loadViews() {
        orderSummary = findViewById(R.id.order_summary);
        recyclerView = findViewById(R.id.my_recycler_view);
        totalPrice = findViewById(R.id.total_amount);
        loadingView = findViewById(R.id.loading_view);
        paymentInProgress = findViewById(R.id.payment_in_progress);
        trxSuccess = findViewById(R.id.trx_success);

    }

    void setupNfc() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
            //do something if there are nfc module on device

            mCardNfcUtils = new CardNfcUtils(this);
            //next few lines here needed in case you will scan credit card when app is closed
            mIntentFromCreate = true;
            onNewIntent(getIntent());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntentFromCreate = false;
        mCardNfcUtils.enableDispatch();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.dispose();
        compositeDisposable.dispose();
    }

    @Override
    public void startNfcReadCard() {
        Toast.makeText(this, "Started NFC read card", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cardIsReadyToRead() {
        String card = mCardNfcAsyncTask.getCardNumber();
        String expiredDate = mCardNfcAsyncTask.getCardExpireDate();
        String cardType = mCardNfcAsyncTask.getCardType();
        final String tag = Arrays.toString(mCardNfcAsyncTask.getTag().getId());

        viewModel.cardScanned(tag, card, expiredDate, cardType);
    }

    @Override
    public void doNotMoveCardSoFast() {
        Toast.makeText(this, "Do not move card so fast", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unknownEmvCard() {

    }

    @Override
    public void cardWithLockedNfc() {

    }

    @Override
    public void finishNfcReadCard() {

    }


    static class OrderPreviewProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

        List<Product> products = new ArrayList<>();


        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_preview_product, parent, false);

            return new ProductViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            final Product product = products.get(position);

            holder.name.setText(product.getName());
            holder.quantity.setText(String.valueOf(product.getQuantity()));
            holder.price.setText(String.format("RSD %d", product.getTotalPrice()));
        }

        public void setProducts(List<Product> products) {
            this.products.clear();
            this.products.addAll(products);

            this.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return products.size();
        }
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        public final TextView name;
        final TextView quantity;
        final TextView price;

        ProductViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.product_name);
            this.quantity = itemView.findViewById(R.id.product_quantity);
            this.price = itemView.findViewById(R.id.product_price);
        }
    }
}
