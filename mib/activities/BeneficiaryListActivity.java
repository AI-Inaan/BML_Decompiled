package mv.com.bml.mib.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import mv.com.bml.mib.R;
import mv.com.bml.mib.fragments.BeneficiaryListFragment;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mib.interfaces.Titleable;

public class BeneficiaryListActivity extends BasePinCodeActivity implements OnFragmentInteractionListener {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fragment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, BeneficiaryListFragment.newInstance()).commit();
    }

    public void onFragmentInteraction(Fragment fragment, boolean z) {
        String charSequence = getTitle().toString();
        if (fragment instanceof Titleable) {
            charSequence = ((Titleable) fragment).getTitle();
        }
        setTitle(charSequence);
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
