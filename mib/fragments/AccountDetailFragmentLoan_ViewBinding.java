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

public class AccountDetailFragmentLoan_ViewBinding implements Unbinder {
    private AccountDetailFragmentLoan target;

    @UiThread
    public AccountDetailFragmentLoan_ViewBinding(AccountDetailFragmentLoan accountDetailFragmentLoan, View view) {
        this.target = accountDetailFragmentLoan;
        accountDetailFragmentLoan.tv_name = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_name, "field 'tv_name'", TextView.class);
        accountDetailFragmentLoan.tv_name2 = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_name_secondary, "field 'tv_name2'", TextView.class);
        accountDetailFragmentLoan.am_t = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_amount, "field 'am_t'", TextView.class);
        accountDetailFragmentLoan.acc_num = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_num, "field 'acc_num'", TextView.class);
        accountDetailFragmentLoan.acc_curr = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_currency, "field 'acc_curr'", TextView.class);
        accountDetailFragmentLoan.list = (ListView) Utils.findRequiredViewAsType(view, R.id.details_list, "field 'list'", ListView.class);
        accountDetailFragmentLoan.list2 = (ListView) Utils.findRequiredViewAsType(view, R.id.details_list2, "field 'list2'", ListView.class);
        accountDetailFragmentLoan.btn_pay_to_this = (Button) Utils.findRequiredViewAsType(view, R.id.btn_paytoloan, "field 'btn_pay_to_this'", Button.class);
    }

    @CallSuper
    public void unbind() {
        AccountDetailFragmentLoan accountDetailFragmentLoan = this.target;
        if (accountDetailFragmentLoan != null) {
            this.target = null;
            accountDetailFragmentLoan.tv_name = null;
            accountDetailFragmentLoan.tv_name2 = null;
            accountDetailFragmentLoan.am_t = null;
            accountDetailFragmentLoan.acc_num = null;
            accountDetailFragmentLoan.acc_curr = null;
            accountDetailFragmentLoan.list = null;
            accountDetailFragmentLoan.list2 = null;
            accountDetailFragmentLoan.btn_pay_to_this = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
