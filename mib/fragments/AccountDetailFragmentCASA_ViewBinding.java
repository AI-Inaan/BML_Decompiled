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

public class AccountDetailFragmentCASA_ViewBinding implements Unbinder {
    private AccountDetailFragmentCASA target;

    @UiThread
    public AccountDetailFragmentCASA_ViewBinding(AccountDetailFragmentCASA accountDetailFragmentCASA, View view) {
        this.target = accountDetailFragmentCASA;
        accountDetailFragmentCASA.tv_name = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_name, "field 'tv_name'", TextView.class);
        accountDetailFragmentCASA.am_t = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_amount, "field 'am_t'", TextView.class);
        accountDetailFragmentCASA.acc_num = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_num, "field 'acc_num'", TextView.class);
        accountDetailFragmentCASA.acc_curr = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_currency, "field 'acc_curr'", TextView.class);
        accountDetailFragmentCASA.list = (ListView) Utils.findRequiredViewAsType(view, R.id.details_list, "field 'list'", ListView.class);
        accountDetailFragmentCASA.make_transfer = (Button) Utils.findRequiredViewAsType(view, R.id.btn_transfer, "field 'make_transfer'", Button.class);
    }

    @CallSuper
    public void unbind() {
        AccountDetailFragmentCASA accountDetailFragmentCASA = this.target;
        if (accountDetailFragmentCASA != null) {
            this.target = null;
            accountDetailFragmentCASA.tv_name = null;
            accountDetailFragmentCASA.am_t = null;
            accountDetailFragmentCASA.acc_num = null;
            accountDetailFragmentCASA.acc_curr = null;
            accountDetailFragmentCASA.list = null;
            accountDetailFragmentCASA.make_transfer = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
