package mv.com.bml.mib.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;
import mv.com.bml.mib.widgets.ToolTip;

public class PerformPaymentTransferFragment_ViewBinding implements Unbinder {
    private PerformPaymentTransferFragment target;
    private View view2131361864;

    @UiThread
    public PerformPaymentTransferFragment_ViewBinding(final PerformPaymentTransferFragment performPaymentTransferFragment, View view) {
        this.target = performPaymentTransferFragment;
        performPaymentTransferFragment.from_account_view = Utils.findRequiredView(view, R.id.from_account, "field 'from_account_view'");
        performPaymentTransferFragment.to_account_view = Utils.findRequiredView(view, R.id.to_account, "field 'to_account_view'");
        performPaymentTransferFragment.to_account_container = Utils.findRequiredView(view, R.id.to_account_container, "field 'to_account_container'");
        performPaymentTransferFragment.otp_view = Utils.findRequiredView(view, R.id.otp_method_container, "field 'otp_view'");
        performPaymentTransferFragment.beneficiary_inst_view = Utils.findRequiredView(view, R.id.beneficiary_detail_container, "field 'beneficiary_inst_view'");
        performPaymentTransferFragment.invoice_view = Utils.findRequiredView(view, R.id.invoice_no_container, "field 'invoice_view'");
        performPaymentTransferFragment.radio_view = Utils.findRequiredView(view, R.id.radio_payment_container, "field 'radio_view'");
        performPaymentTransferFragment.mobile_recharge_view = Utils.findRequiredView(view, R.id.mobile_recharge_container, "field 'mobile_recharge_view'");
        performPaymentTransferFragment.beneficiary_view = Utils.findRequiredView(view, R.id.beneficiary_payment_container, "field 'beneficiary_view'");
        performPaymentTransferFragment.remarks_view = Utils.findRequiredView(view, R.id.remarks_container, "field 'remarks_view'");
        performPaymentTransferFragment.currency_spinner = (Spinner) Utils.findRequiredViewAsType(view, R.id.currency_spinner, "field 'currency_spinner'", Spinner.class);
        performPaymentTransferFragment.otp_spinner = (Spinner) Utils.findRequiredViewAsType(view, R.id.otp_method, "field 'otp_spinner'", Spinner.class);
        performPaymentTransferFragment.beneficiary_inst_spinner = (Spinner) Utils.findRequiredViewAsType(view, R.id.benef_method_spinner, "field 'beneficiary_inst_spinner'", Spinner.class);
        performPaymentTransferFragment.tip = (ToolTip) Utils.findRequiredViewAsType(view, R.id.tooltip_2, "field 'tip'", ToolTip.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.button_confirm, "method 'performConfirm'");
        this.view2131361864 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                performPaymentTransferFragment.performConfirm();
            }
        });
    }

    @CallSuper
    public void unbind() {
        PerformPaymentTransferFragment performPaymentTransferFragment = this.target;
        if (performPaymentTransferFragment != null) {
            this.target = null;
            performPaymentTransferFragment.from_account_view = null;
            performPaymentTransferFragment.to_account_view = null;
            performPaymentTransferFragment.to_account_container = null;
            performPaymentTransferFragment.otp_view = null;
            performPaymentTransferFragment.beneficiary_inst_view = null;
            performPaymentTransferFragment.invoice_view = null;
            performPaymentTransferFragment.radio_view = null;
            performPaymentTransferFragment.mobile_recharge_view = null;
            performPaymentTransferFragment.beneficiary_view = null;
            performPaymentTransferFragment.remarks_view = null;
            performPaymentTransferFragment.currency_spinner = null;
            performPaymentTransferFragment.otp_spinner = null;
            performPaymentTransferFragment.beneficiary_inst_spinner = null;
            performPaymentTransferFragment.tip = null;
            this.view2131361864.setOnClickListener(null);
            this.view2131361864 = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
