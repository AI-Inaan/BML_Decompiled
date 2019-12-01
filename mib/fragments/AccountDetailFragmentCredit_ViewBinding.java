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

public class AccountDetailFragmentCredit_ViewBinding implements Unbinder {
    private AccountDetailFragmentCredit target;

    @UiThread
    public AccountDetailFragmentCredit_ViewBinding(AccountDetailFragmentCredit accountDetailFragmentCredit, View view) {
        this.target = accountDetailFragmentCredit;
        accountDetailFragmentCredit.tv_name = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_name, "field 'tv_name'", TextView.class);
        accountDetailFragmentCredit.tv_name2 = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_name_secondary, "field 'tv_name2'", TextView.class);
        accountDetailFragmentCredit.am_t = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_amount, "field 'am_t'", TextView.class);
        accountDetailFragmentCredit.acc_num = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_num, "field 'acc_num'", TextView.class);
        accountDetailFragmentCredit.acc_curr = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_currency, "field 'acc_curr'", TextView.class);
        accountDetailFragmentCredit.list = (ListView) Utils.findRequiredViewAsType(view, R.id.details_list, "field 'list'", ListView.class);
        accountDetailFragmentCredit.title_list2 = (TextView) Utils.findRequiredViewAsType(view, R.id.titleList2, "field 'title_list2'", TextView.class);
        accountDetailFragmentCredit.list2 = (ListView) Utils.findRequiredViewAsType(view, R.id.details_list2, "field 'list2'", ListView.class);
        accountDetailFragmentCredit.btn_pay_to_this = (Button) Utils.findRequiredViewAsType(view, R.id.btn_paytocard, "field 'btn_pay_to_this'", Button.class);
        accountDetailFragmentCredit.make_transfer = (Button) Utils.findRequiredViewAsType(view, R.id.btn_transfer, "field 'make_transfer'", Button.class);
    }

    @CallSuper
    public void unbind() {
        AccountDetailFragmentCredit accountDetailFragmentCredit = this.target;
        if (accountDetailFragmentCredit != null) {
            this.target = null;
            accountDetailFragmentCredit.tv_name = null;
            accountDetailFragmentCredit.tv_name2 = null;
            accountDetailFragmentCredit.am_t = null;
            accountDetailFragmentCredit.acc_num = null;
            accountDetailFragmentCredit.acc_curr = null;
            accountDetailFragmentCredit.list = null;
            accountDetailFragmentCredit.title_list2 = null;
            accountDetailFragmentCredit.list2 = null;
            accountDetailFragmentCredit.btn_pay_to_this = null;
            accountDetailFragmentCredit.make_transfer = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
