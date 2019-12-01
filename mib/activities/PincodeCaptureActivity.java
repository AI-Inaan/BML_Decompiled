package mv.com.bml.mib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import mv.com.bml.mib.R;
import mv.com.bml.mib.fragments.PincodeCaptureFragment;
import mv.com.bml.mib.fragments.PincodeCapturePrompt;
import mv.com.bml.mib.fragments.PincodeCapturePrompt.OnFragmentInteractionListener;
import mv.com.bml.mibapi.models.auth.a;

public class PincodeCaptureActivity extends BasePinCodeActivity implements OnFragmentInteractionListener {
    public static final String ARG_MODE_CHANGEPIN = "arg.change";
    public static final String CREDENTIALS_BUNDLE = "cred.bundle";
    private static final String TAG = "PincodeCaptureActivity";

    private void startDashboard() {
        startActivity(new Intent(this, DialDashboardActivity.class));
    }

    public void onCaptureActionClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, PincodeCaptureFragment.newInstance((a) getIntent().getExtras().getSerializable(CREDENTIALS_BUNDLE))).commit();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fragment_container);
        getSupportFragmentManager().beginTransaction().add((int) R.id.fragment, getIntent().getExtras().getBoolean(ARG_MODE_CHANGEPIN, false) ? PincodeCaptureFragment.newInstance((a) getIntent().getExtras().getSerializable(CREDENTIALS_BUNDLE), true) : new PincodeCapturePrompt()).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pincode_capture, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.action_skip) {
            return super.onOptionsItemSelected(menuItem);
        }
        startDashboard();
        return true;
    }

    public void onResumeFromSleep() {
    }
}
