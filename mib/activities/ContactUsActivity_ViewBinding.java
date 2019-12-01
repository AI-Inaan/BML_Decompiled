package mv.com.bml.mib.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class ContactUsActivity_ViewBinding implements Unbinder {
    private ContactUsActivity target;
    private View view2131361844;
    private View view2131361858;
    private View view2131361859;

    @UiThread
    public ContactUsActivity_ViewBinding(ContactUsActivity contactUsActivity) {
        this(contactUsActivity, contactUsActivity.getWindow().getDecorView());
    }

    @UiThread
    public ContactUsActivity_ViewBinding(final ContactUsActivity contactUsActivity, View view) {
        this.target = contactUsActivity;
        contactUsActivity.editname = (EditText) Utils.findRequiredViewAsType(view, R.id.edit_name, "field 'editname'", EditText.class);
        contactUsActivity.editId = (EditText) Utils.findRequiredViewAsType(view, R.id.edit_idnum, "field 'editId'", EditText.class);
        contactUsActivity.editIssue = (EditText) Utils.findRequiredViewAsType(view, R.id.edit_issue, "field 'editIssue'", EditText.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.btn_sendsms, "field 'btn_sms' and method 'onClick'");
        contactUsActivity.btn_sms = (Button) Utils.castView(findRequiredView, R.id.btn_sendsms, "field 'btn_sms'", Button.class);
        this.view2131361859 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                contactUsActivity.onClick(view);
            }
        });
        View findRequiredView2 = Utils.findRequiredView(view, R.id.btn_sendemail, "field 'btn_email' and method 'onClick'");
        contactUsActivity.btn_email = (Button) Utils.castView(findRequiredView2, R.id.btn_sendemail, "field 'btn_email'", Button.class);
        this.view2131361858 = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                contactUsActivity.onClick(view);
            }
        });
        View findRequiredView3 = Utils.findRequiredView(view, R.id.btn_callcard, "method 'onClick'");
        this.view2131361844 = findRequiredView3;
        findRequiredView3.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                contactUsActivity.onClick(view);
            }
        });
    }

    @CallSuper
    public void unbind() {
        ContactUsActivity contactUsActivity = this.target;
        if (contactUsActivity != null) {
            this.target = null;
            contactUsActivity.editname = null;
            contactUsActivity.editId = null;
            contactUsActivity.editIssue = null;
            contactUsActivity.btn_sms = null;
            contactUsActivity.btn_email = null;
            this.view2131361859.setOnClickListener(null);
            this.view2131361859 = null;
            this.view2131361858.setOnClickListener(null);
            this.view2131361858 = null;
            this.view2131361844.setOnClickListener(null);
            this.view2131361844 = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
