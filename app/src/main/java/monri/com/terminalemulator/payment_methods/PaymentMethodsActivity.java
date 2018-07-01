package monri.com.terminalemulator.payment_methods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import monri.com.terminalemulator.R;
import monri.com.terminalemulator.app.App;
import monri.com.terminalemulator.order.OrderPreviewViewModel;

public class PaymentMethodsActivity extends AppCompatActivity {

    @Inject
    PaymentMethodsViewModel viewModel;

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
    }

    private OrderPreviewViewModel.PaymentMethodsRequest fromIntent() {
        final Intent intent = getIntent();
        final String orderNumber = intent.getStringExtra("order_number");
        final String userId = intent.getStringExtra("user_id");
        final String id = intent.getStringExtra("id");
        return new OrderPreviewViewModel.PaymentMethodsRequest(userId, id, orderNumber);
    }
}
