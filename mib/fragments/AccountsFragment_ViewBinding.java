package mv.com.bml.mib.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class AccountsFragment_ViewBinding implements Unbinder {
    private AccountsFragment target;

    @UiThread
    public AccountsFragment_ViewBinding(AccountsFragment accountsFragment, View view) {
        this.target = accountsFragment;
        accountsFragment.mListView = (ListView) Utils.findRequiredViewAsType(view, R.id.accounts_listview, "field 'mListView'", ListView.class);
        accountsFragment.mSwipeLayout = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.swipe_layout, "field 'mSwipeLayout'", SwipeRefreshLayout.class);
    }

    @CallSuper
    public void unbind() {
        AccountsFragment accountsFragment = this.target;
        if (accountsFragment != null) {
            this.target = null;
            accountsFragment.mListView = null;
            accountsFragment.mSwipeLayout = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
