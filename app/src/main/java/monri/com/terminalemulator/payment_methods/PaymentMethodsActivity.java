package monri.com.terminalemulator.payment_methods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import monri.com.terminalemulator.PaymentMethod;
import monri.com.terminalemulator.PaymentMethodsResponse;
import monri.com.terminalemulator.R;
import monri.com.terminalemulator.app.App;
import monri.com.terminalemulator.order.OrderPreviewViewModel;

public class PaymentMethodsActivity extends AppCompatActivity {

    @Inject
    PaymentMethodsViewModel viewModel;

    android.support.v7.widget.Toolbar toolbar;

    RecyclerView paymentMethodsRecyclerView;

    LinearLayoutManager linearLayoutManager;

    Adapter adapter;

    View success;

    View content;

    View loading;

    CompositeDisposable disposable = new CompositeDisposable();

    public static Intent createIntent(Context context, OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest) {
        Intent intent = new Intent(context, PaymentMethodsActivity.class);
        intent.putExtra("order_number", paymentMethodsRequest.getOrderNumber());
        intent.putExtra("user_id", paymentMethodsRequest.getUserId());
        intent.putExtra("id", paymentMethodsRequest.getId());
        return intent;
    }

    void back() {
        final ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        supportActionBar.setHomeAsUpIndicator(R.drawable.back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back();

        OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest = fromIntent();
        final PaymentMethodsComponent component = App.getApp(this).getAppComponent().plus(new PaymentMethodsModule(paymentMethodsRequest));
        component.inject(this);

        getSupportActionBar().setTitle(String.format("Order %s", paymentMethodsRequest.getOrderNumber()));

        loadViews();
        setupRecyclerView();

        bindEvents();

        viewModel.load();
    }

    private void bindEvents() {
        final Disposable subscribe = viewModel.paymentMethodsResponseSubject.subscribe(new Consumer<PaymentMethodsResponse>() {
            @Override
            public void accept(PaymentMethodsResponse paymentMethodsResponse) throws Exception {
                adapter.setPaymentMethods(paymentMethodsResponse.getList());
            }
        });

        disposable.add(subscribe);

        viewModel.loading.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                loading.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
            }
        });

        viewModel.content.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                content.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
            }
        });

        viewModel.trxSuccess.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                success.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new Adapter(new ArrayList<PaymentMethod>(), new Consumer<PaymentMethod>() {
            @Override
            public void accept(PaymentMethod paymentMethod) throws Exception {
                viewModel
                        .payWith(paymentMethod);
            }
        });
        paymentMethodsRecyclerView.setLayoutManager(linearLayoutManager);
        paymentMethodsRecyclerView.setAdapter(adapter);
    }

    private void loadViews() {
        toolbar = findViewById(R.id.toolbar);
        paymentMethodsRecyclerView = findViewById(R.id.payment_methods);
        success = findViewById(R.id.trx_success);
        content = findViewById(R.id.payment_methods_content);
        loading = findViewById(R.id.payment_in_progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.dispose();
        disposable.dispose();
    }

    private OrderPreviewViewModel.PaymentMethodsRequest fromIntent() {
        final Intent intent = getIntent();
        final String orderNumber = intent.getStringExtra("order_number");
        final String userId = intent.getStringExtra("user_id");
        final String id = intent.getStringExtra("id");
        return new OrderPreviewViewModel.PaymentMethodsRequest(userId, id, orderNumber);
    }

    static class Adapter extends RecyclerView.Adapter<PaymentMethodViewHolder> {

        final List<PaymentMethod> paymentMethods;
        final Consumer<PaymentMethod> clickHandler;

        Adapter(List<PaymentMethod> paymentMethods, Consumer<PaymentMethod> clickHandler) {
            this.paymentMethods = paymentMethods;
            this.clickHandler = clickHandler;
        }

        @NonNull
        @Override
        public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.payment_method_item, parent, false);

            return new PaymentMethodViewHolder(itemView);
        }

        void setPaymentMethods(List<PaymentMethod> list) {
            paymentMethods.clear();
            paymentMethods.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull PaymentMethodViewHolder holder, int position) {
            holder.bind(paymentMethods.get(position), clickHandler);
        }

        @Override
        public int getItemCount() {
            return paymentMethods.size();
        }
    }

    static class PaymentMethodViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView subtitle;
        final ImageView imageView;
        final CardView cardView;
        final TextView balance;


        PaymentMethodViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView;
            title = itemView.findViewById(R.id.payment_method_title);
            subtitle = itemView.findViewById(R.id.payment_method_subtitle);
            imageView = itemView.findViewById(R.id.payment_method_image);
            balance = itemView.findViewById(R.id.payment_method_balance);
        }

        int imageRes(String image) {
            int imageResource = -1;
            switch (image) {
                case "mastercard":
                    imageResource = R.drawable.mastercard;
                    break;
                case "bank_account":
                    // TODO: fix image
                    imageResource = R.drawable.bank_account;
                    break;
                case "btc":
                    // TODO: fix image
                    imageResource = R.drawable.bitcoin;
                    break;
                case "maxi":
                    // TODO: fix image
                    imageResource = R.drawable.maxi;
                    break;
            }

            return imageResource;
        }

//        @ColorRes
//        int backgroundColor(String image) {
//            return R.color.
//        }

        void bind(final PaymentMethod paymentMethod, final Consumer<PaymentMethod> clickHandler) {
            final String image = paymentMethod.getImage();
            int imageResource = imageRes(image);

            title.setText(paymentMethod.getTitle());
            subtitle.setText(paymentMethod.getSubtitle());
            balance.setText(paymentMethod.getBalance());
            if (imageResource != -1) {
                imageView.setImageResource(imageResource);
            }

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        clickHandler.accept(paymentMethod);
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }
            });
        }
    }
}
