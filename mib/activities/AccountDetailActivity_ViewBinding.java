package mv.com.bml.mib.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class AccountDetailActivity_ViewBinding implements Unbinder {
    private AccountDetailActivity target;

    @UiThread
    public AccountDetailActivity_ViewBinding(AccountDetailActivity accountDetailActivity) {
        this(accountDetailActivity, accountDetailActivity.getWindow().getDecorView());
    }

    @UiThread
    public AccountDetailActivity_ViewBinding(AccountDetailActivity accountDetailActivity, View view) {
        this.target = accountDetailActivity;
        accountDetailActivity.tabs = (TabLayout) Utils.findRequiredViewAsType(view, R.id.tab_layout, "field 'tabs'", TabLayout.class);
        accountDetailActivity.mViewPager = (ViewPager) Utils.findRequiredViewAsType(view, R.id.container, "field 'mViewPager'", ViewPager.class);
    }

    @CallSuper
    public void unbind() {
        AccountDetailActivity accountDetailActivity = this.target;
        if (accountDetailActivity != null) {
            this.target = null;
            accountDetailActivity.tabs = null;
            accountDetailActivity.mViewPager = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
