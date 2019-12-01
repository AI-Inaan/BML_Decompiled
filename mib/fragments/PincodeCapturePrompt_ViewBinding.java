package mv.com.bml.mib.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class PincodeCapturePrompt_ViewBinding implements Unbinder {
    private PincodeCapturePrompt target;
    private View view2131361845;

    @UiThread
    public PincodeCapturePrompt_ViewBinding(final PincodeCapturePrompt pincodeCapturePrompt, View view) {
        this.target = pincodeCapturePrompt;
        pincodeCapturePrompt.t = (TextView) Utils.findRequiredViewAsType(view, R.id.txtDescription, "field 't'", TextView.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.btn_capture, "method 'onButtonPressed'");
        this.view2131361845 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                pincodeCapturePrompt.onButtonPressed();
            }
        });
    }

    @CallSuper
    public void unbind() {
        PincodeCapturePrompt pincodeCapturePrompt = this.target;
        if (pincodeCapturePrompt != null) {
            this.target = null;
            pincodeCapturePrompt.t = null;
            this.view2131361845.setOnClickListener(null);
            this.view2131361845 = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
