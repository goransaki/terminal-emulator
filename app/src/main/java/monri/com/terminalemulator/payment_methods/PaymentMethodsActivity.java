package monri.com.terminalemulator.payment_methods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import java.util.List;

import javax.inject.Inject;

import monri.com.terminalemulator.PaymentMethod;
import monri.com.terminalemulator.R;
import monri.com.terminalemulator.app.App;
import monri.com.terminalemulator.order.OrderPreviewActivity;
import monri.com.terminalemulator.order.OrderPreviewViewModel;

public class PaymentMethodsActivity extends AppCompatActivity {

    @Inject
    PaymentMethodsViewModel viewModel;

    Toolbar toolbar;

    RecyclerView paymentMethodsRecyclerView;

    Button payWithSelectedButton;

    LinearLayoutManager linearLayoutManager;

    public static Intent createIntent(Context context, OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest) {
        Intent intent = new Intent(context, PaymentMethodsActivity.class);
        intent.putExtra("order_number", paymentMethodsRequest.getOrderNumber());
        intent.putExtra("user_id", paymentMethodsRequest.getUserId());
        intent.putExtra("id", paymentMethodsRequest.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        OrderPreviewViewModel.PaymentMethodsRequest paymentMethodsRequest = fromIntent();
        final PaymentMethodsComponent component = App.getApp(this).getAppComponent().plus(new PaymentMethodsModule(paymentMethodsRequest));
        component.inject(this);

        loadViews();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
    }

    private void loadViews() {
        toolbar = findViewById(R.id.toolbar);
        paymentMethodsRecyclerView = findViewById(R.id.payment_methods);
        payWithSelectedButton = findViewById(R.id.pay_with_selected_card);
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

        Adapter(List<PaymentMethod> paymentMethods) {
            this.paymentMethods = paymentMethods;
        }

        @NonNull
        @Override
        public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.payment_method_item, parent, false);

            return new OrderPreviewActivity.ProductViewHolder(itemView);
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull PaymentMethodViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    static class PaymentMethodViewHolder extends RecyclerView.ViewHolder{

        public PaymentMethodViewHolder(View itemView) {
            super(itemView);
        }
    }
}
