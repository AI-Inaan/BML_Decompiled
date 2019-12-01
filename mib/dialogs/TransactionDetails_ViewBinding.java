package mv.com.bml.mib.dialogs;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class TransactionDetails_ViewBinding implements Unbinder {
    private TransactionDetails target;

    @UiThread
    public TransactionDetails_ViewBinding(TransactionDetails transactionDetails, View view) {
        this.target = transactionDetails;
        transactionDetails.list = (ListView) Utils.findRequiredViewAsType(view, R.id.list1, "field 'list'", ListView.class);
    }

    @CallSuper
    public void unbind() {
        TransactionDetails transactionDetails = this.target;
        if (transactionDetails != null) {
            this.target = null;
            transactionDetails.list = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
