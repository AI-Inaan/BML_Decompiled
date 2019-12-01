package mv.com.bml.mib.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class PaymentConfirmFragment_ViewBinding implements Unbinder {
    private PaymentConfirmFragment target;

    @UiThread
    public PaymentConfirmFragment_ViewBinding(PaymentConfirmFragment paymentConfirmFragment, View view) {
        this.target = paymentConfirmFragment;
        paymentConfirmFragment.from_account_view = Utils.findRequiredView(view, R.id.from_account, "field 'from_account_view'");
        paymentConfirmFragment.to_account_view = Utils.findRequiredView(view, R.id.to_account, "field 'to_account_view'");
        paymentConfirmFragment.otp_view = (EditText) Utils.findRequiredViewAsType(view, R.id.otp_method_container, "field 'otp_view'", EditText.class);
        paymentConfirmFragment.text_date = (TextView) Utils.findRequiredViewAsType(view, R.id.text_date, "field 'text_date'", TextView.class);
        paymentConfirmFragment.text_status = (TextView) Utils.findRequiredViewAsType(view, R.id.status_text_sub, "field 'text_status'", TextView.class);
        paymentConfirmFragment.btn_confirm = (Button) Utils.findRequiredViewAsType(view, R.id.button_confirm, "field 'btn_confirm'", Button.class);
    }

    @CallSuper
    public void unbind() {
        PaymentConfirmFragment paymentConfirmFragment = this.target;
        if (paymentConfirmFragment != null) {
            this.target = null;
            paymentConfirmFragment.from_account_view = null;
            paymentConfirmFragment.to_account_view = null;
            paymentConfirmFragment.otp_view = null;
            paymentConfirmFragment.text_date = null;
            paymentConfirmFragment.text_status = null;
            paymentConfirmFragment.btn_confirm = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
