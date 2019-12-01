package mv.com.bml.mib.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class TxHistoryFragment_ViewBinding implements Unbinder {
    private TxHistoryFragment target;

    @UiThread
    public TxHistoryFragment_ViewBinding(TxHistoryFragment txHistoryFragment, View view) {
        this.target = txHistoryFragment;
        txHistoryFragment.list = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.details_list, "field 'list'", RecyclerView.class);
        txHistoryFragment.mSwipeView = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.swipe_layout, "field 'mSwipeView'", SwipeRefreshLayout.class);
    }

    @CallSuper
    public void unbind() {
        TxHistoryFragment txHistoryFragment = this.target;
        if (txHistoryFragment != null) {
            this.target = null;
            txHistoryFragment.list = null;
            txHistoryFragment.mSwipeView = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
