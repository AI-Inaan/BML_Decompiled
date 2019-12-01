package mv.com.bml.mib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import mv.com.bml.mib.MyBMLApplication;
import mv.com.bml.mib.R;
import mv.com.bml.mibapi.BMLApi;

public abstract class BasePinCodeActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public int getThemeEliteTheme() {
        return R.style.AppTheme_NotHorrible;
    }

    /* access modifiers changed from: protected */
    public boolean isEliteModeEnabled() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("elite_mode", false);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        BMLApi.getInstance().getDataCache().initDiskCache(this);
        setTheme(getThemeEliteTheme());
        super.onCreate(bundle);
    }

    public void onResumeFromSleep() {
        Log.d("PINCODE", "resuming from sleep");
        boolean isSessionTimeoutExceeded = BMLApi.getInstance().isSessionTimeoutExceeded();
        if (!BMLApi.getInstance().getCredentials(this).a() || !BMLApi.getInstance().hasSession() || isSessionTimeoutExceeded) {
            Log.d("SESSION", "timed out");
        } else {
            startActivityForResult(new Intent(this, LoginActivity.class), 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (((MyBMLApplication) getApplication()).registerActivityCount(1) <= 1) {
            onResumeFromSleep();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        ((MyBMLApplication) getApplication()).registerActivityCount(-1);
    }
}
