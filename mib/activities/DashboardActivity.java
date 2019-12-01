package mv.com.bml.mib.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import mv.com.bml.mib.R;
import mv.com.bml.mib.fragments.AccountsFragment;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mib.interfaces.Titleable;

public class DashboardActivity extends BasePinCodeActivity implements OnFragmentInteractionListener {
    private static final String TAG = "DashboardActivity";
    private CharSequence mTitle = "Accounts";

    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fragment_container);
        setTitle(this.mTitle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, AccountsFragment.newInstance()).commit();
    }

    public void onFragmentInteraction(Fragment fragment, boolean z) {
        String charSequence = this.mTitle.toString();
        if (fragment instanceof Titleable) {
            charSequence = ((Titleable) fragment).getTitle();
        }
        onNavigationDrawerItemSelected(charSequence, fragment, z);
    }

    public void onNavigationDrawerItemSelected(String str, Fragment fragment, boolean z) {
        this.mTitle = str;
        invalidateOptionsMenu();
        if (fragment != null) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            String str2 = TAG;
            if (z) {
                beginTransaction.addToBackStack(str2);
            } else {
                getSupportFragmentManager().popBackStack(str2, 1);
            }
            beginTransaction.replace(R.id.container, fragment);
            beginTransaction.commit();
        }
    }
}
