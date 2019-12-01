package mv.com.bml.mib.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import mv.com.bml.mib.R;
import mv.com.bml.mib.fragments.AccountsFragment;
import mv.com.bml.mib.fragments.BeneficiaryListFragment;
import mv.com.bml.mib.fragments.PerformPaymentTransferFragment;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mib.interfaces.Titleable;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.payments.MerchantInfo;
import mv.com.bml.mibapi.models.payments.Payable;

public class PayOrTransferActivity extends BasePinCodeActivity implements OnFragmentInteractionListener {
    public static final String ARG_PAY_FROM = "param1";
    public static final String ARG_PAY_TO = "param2";
    private AccountCommon pay_from;
    private Payable pay_to;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        FragmentTransaction beginTransaction;
        Fragment newInstance;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fragment_container);
        this.pay_to = (Payable) getIntent().getSerializableExtra(ARG_PAY_TO);
        this.pay_from = (AccountCommon) getIntent().getSerializableExtra(ARG_PAY_FROM);
        if (this.pay_from == null || this.pay_to == null) {
            Payable payable = this.pay_to;
            if (payable != null) {
                if ((payable instanceof MerchantInfo) && ((MerchantInfo) payable).merchantEnrolType.equals("PREPAID_CARD")) {
                    setTitle(R.string.topup_card);
                }
                beginTransaction = getSupportFragmentManager().beginTransaction();
                newInstance = AccountsFragment.newInstance(this.pay_to);
            } else if (this.pay_from != null) {
                beginTransaction = getSupportFragmentManager().beginTransaction();
                newInstance = BeneficiaryListFragment.newInstance(this.pay_from);
            } else {
                beginTransaction = getSupportFragmentManager().beginTransaction();
                newInstance = BeneficiaryListFragment.newInstance(true);
            }
        } else {
            beginTransaction = getSupportFragmentManager().beginTransaction();
            newInstance = PerformPaymentTransferFragment.newInstance(this.pay_to, this.pay_from);
        }
        beginTransaction.replace(R.id.fragment, newInstance).commit();
    }

    public void onFragmentInteraction(Fragment fragment, boolean z) {
        String charSequence = getTitle().toString();
        if (fragment instanceof Titleable) {
            charSequence = ((Titleable) fragment).getTitle();
        }
        if (!TextUtils.isEmpty(charSequence)) {
            setTitle(charSequence);
        }
        invalidateOptionsMenu();
        if (fragment != null) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            String str = "backmeup";
            if (z) {
                beginTransaction.addToBackStack(str);
            } else {
                getSupportFragmentManager().popBackStack(str, 1);
            }
            beginTransaction.replace(R.id.fragment, fragment);
            beginTransaction.commit();
        }
    }
}
