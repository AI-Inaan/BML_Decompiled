package mv.com.bml.mib.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class TransactionReceiptFragment_ViewBinding implements Unbinder {
    private TransactionReceiptFragment target;

    @UiThread
    public TransactionReceiptFragment_ViewBinding(TransactionReceiptFragment transactionReceiptFragment, View view) {
        this.target = transactionReceiptFragment;
        transactionReceiptFragment.receipt_container = Utils.findRequiredView(view, R.id.receipt_container, "field 'receipt_container'");
        transactionReceiptFragment.receipt_item_list = (ListView) Utils.findRequiredViewAsType(view, R.id.receipt_item_list, "field 'receipt_item_list'", ListView.class);
        transactionReceiptFragment.receipt_title = (TextView) Utils.findRequiredViewAsType(view, R.id.receipt_title, "field 'receipt_title'", TextView.class);
        transactionReceiptFragment.btn_share = (Button) Utils.findRequiredViewAsType(view, R.id.btn_share, "field 'btn_share'", Button.class);
        transactionReceiptFragment.btn_save = (Button) Utils.findRequiredViewAsType(view, R.id.btn_save, "field 'btn_save'", Button.class);
        transactionReceiptFragment.btn_copy_recharge = (Button) Utils.findRequiredViewAsType(view, R.id.btn_copy_recharge, "field 'btn_copy_recharge'", Button.class);
    }

    @CallSuper
    public void unbind() {
        TransactionReceiptFragment transactionReceiptFragment = this.target;
        if (transactionReceiptFragment != null) {
            this.target = null;
            transactionReceiptFragment.receipt_container = null;
            transactionReceiptFragment.receipt_item_list = null;
            transactionReceiptFragment.receipt_title = null;
            transactionReceiptFragment.btn_share = null;
            transactionReceiptFragment.btn_save = null;
            transactionReceiptFragment.btn_copy_recharge = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
