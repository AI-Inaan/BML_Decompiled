package mv.com.bml.mib.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;
import mv.com.bml.mib.widgets.CircleImageView;
import mv.com.bml.mib.widgets.CircleLayout;

public class DialDashboardActivity_ViewBinding implements Unbinder {
    private DialDashboardActivity target;
    private View view2131361846;

    @UiThread
    public DialDashboardActivity_ViewBinding(DialDashboardActivity dialDashboardActivity) {
        this(dialDashboardActivity, dialDashboardActivity.getWindow().getDecorView());
    }

    @UiThread
    public DialDashboardActivity_ViewBinding(final DialDashboardActivity dialDashboardActivity, View view) {
        this.target = dialDashboardActivity;
        dialDashboardActivity.mCircleLayout = (CircleLayout) Utils.findRequiredViewAsType(view, R.id.dial, "field 'mCircleLayout'", CircleLayout.class);
        dialDashboardActivity.btn_settings = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.btn_settings, "field 'btn_settings'", CircleImageView.class);
        dialDashboardActivity.btn_history = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.btn_history, "field 'btn_history'", CircleImageView.class);
        dialDashboardActivity.btn_transfer = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.btn_transfer, "field 'btn_transfer'", CircleImageView.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.btn_center, "field 'btn_lock' and method 'onClick'");
        dialDashboardActivity.btn_lock = (CircleImageView) Utils.castView(findRequiredView, R.id.btn_center, "field 'btn_lock'", CircleImageView.class);
        this.view2131361846 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                dialDashboardActivity.onClick(view);
            }
        });
    }

    @CallSuper
    public void unbind() {
        DialDashboardActivity dialDashboardActivity = this.target;
        if (dialDashboardActivity != null) {
            this.target = null;
            dialDashboardActivity.mCircleLayout = null;
            dialDashboardActivity.btn_settings = null;
            dialDashboardActivity.btn_history = null;
            dialDashboardActivity.btn_transfer = null;
            dialDashboardActivity.btn_lock = null;
            this.view2131361846.setOnClickListener(null);
            this.view2131361846 = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
