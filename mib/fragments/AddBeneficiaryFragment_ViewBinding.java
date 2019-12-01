package mv.com.bml.mib.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class AddBeneficiaryFragment_ViewBinding implements Unbinder {
    private AddBeneficiaryFragment target;

    @UiThread
    public AddBeneficiaryFragment_ViewBinding(AddBeneficiaryFragment addBeneficiaryFragment, View view) {
        this.target = addBeneficiaryFragment;
        addBeneficiaryFragment.view_domestic_bankcode_container = Utils.findRequiredView(view, R.id.layout_domestic_code_selectorview, "field 'view_domestic_bankcode_container'");
        addBeneficiaryFragment.btn_select_domestic_bankcode = (TextView) Utils.findRequiredViewAsType(view, R.id.btn_domestic_bankcode, "field 'btn_select_domestic_bankcode'", TextView.class);
        addBeneficiaryFragment.view_domestic_currency_container = Utils.findRequiredView(view, R.id.layout_domestic_currency_selectorview, "field 'view_domestic_currency_container'");
        addBeneficiaryFragment.btn_select_domestic_currency = (TextView) Utils.findRequiredViewAsType(view, R.id.btn_domestic_currency, "field 'btn_select_domestic_currency'", TextView.class);
        addBeneficiaryFragment.view_acctype_container = Utils.findRequiredView(view, R.id.container_header_acc_type, "field 'view_acctype_container'");
        addBeneficiaryFragment.edit_accountnum = (EditText) Utils.findRequiredViewAsType(view, R.id.edit_account_num, "field 'edit_accountnum'", EditText.class);
        addBeneficiaryFragment.edit_accountName = (EditText) Utils.findRequiredViewAsType(view, R.id.edit_account_name, "field 'edit_accountName'", EditText.class);
        addBeneficiaryFragment.btn_confirm = (Button) Utils.findRequiredViewAsType(view, R.id.button_confirm, "field 'btn_confirm'", Button.class);
        addBeneficiaryFragment.error_text = (TextView) Utils.findRequiredViewAsType(view, R.id.error_text, "field 'error_text'", TextView.class);
        addBeneficiaryFragment.btn_acctype = (TextView) Utils.findRequiredViewAsType(view, R.id.btn_acctype_select, "field 'btn_acctype'", TextView.class);
    }

    @CallSuper
    public void unbind() {
        AddBeneficiaryFragment addBeneficiaryFragment = this.target;
        if (addBeneficiaryFragment != null) {
            this.target = null;
            addBeneficiaryFragment.view_domestic_bankcode_container = null;
            addBeneficiaryFragment.btn_select_domestic_bankcode = null;
            addBeneficiaryFragment.view_domestic_currency_container = null;
            addBeneficiaryFragment.btn_select_domestic_currency = null;
            addBeneficiaryFragment.view_acctype_container = null;
            addBeneficiaryFragment.edit_accountnum = null;
            addBeneficiaryFragment.edit_accountName = null;
            addBeneficiaryFragment.btn_confirm = null;
            addBeneficiaryFragment.error_text = null;
            addBeneficiaryFragment.btn_acctype = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
