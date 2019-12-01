package mv.com.bml.mib.dialogs;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class ListSelectorDialog_ViewBinding implements Unbinder {
    private ListSelectorDialog target;

    @UiThread
    public ListSelectorDialog_ViewBinding(ListSelectorDialog listSelectorDialog, View view) {
        this.target = listSelectorDialog;
        listSelectorDialog.mTitleView = (TextView) Utils.findRequiredViewAsType(view, R.id.dialog_title, "field 'mTitleView'", TextView.class);
        listSelectorDialog.mListView = (ListView) Utils.findRequiredViewAsType(view, R.id.dialog_list, "field 'mListView'", ListView.class);
        listSelectorDialog.mProgressView = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.progress_view, "field 'mProgressView'", LinearLayout.class);
    }

    @CallSuper
    public void unbind() {
        ListSelectorDialog listSelectorDialog = this.target;
        if (listSelectorDialog != null) {
            this.target = null;
            listSelectorDialog.mTitleView = null;
            listSelectorDialog.mListView = null;
            listSelectorDialog.mProgressView = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
