package mv.com.bml.mib.activities;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.widget.Toast;
import mv.com.bml.mib.MyBMLApplication;
import mv.com.bml.mib.R;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.auth.a;
import mv.com.bml.mibapi.models.misc.SignatureHelper;

public class SettingsActivity extends BasePinCodeActivity {

    public static class prefFrag extends PreferenceFragment implements OnPreferenceChangeListener {
        int eliteCounter = 0;

        private void promptAndClear() {
            Builder builder = new Builder(getActivity());
            builder.setTitle("Clear saved account?");
            builder.setMessage("This will clear your saved credentials and passcode for this device. Are you sure you wish to proceed?");
            builder.setNegativeButton("Cancel", null);
            builder.setPositiveButton("Yes", new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == -1) {
                        BMLApi.getInstance().setCredentials(prefFrag.this.getActivity(), null);
                        BMLApi.getInstance().clearSession();
                        BMLApi.getInstance().getDataCache().clearCacheAndDisk(prefFrag.this.getActivity());
                        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(prefFrag.this.getActivity());
                        defaultSharedPreferences.edit().remove("pinRetries").apply();
                        defaultSharedPreferences.edit().remove("fingerprint_auth").apply();
                        prefFrag.this.setLayout();
                        Toast.makeText(prefFrag.this.getActivity(), "You have been logged out", 0).show();
                    }
                }
            });
            builder.show();
        }

        /* access modifiers changed from: private */
        public void setLayout() {
            int i;
            boolean a = BMLApi.getInstance().getCredentials(getActivity()).a();
            boolean z = true;
            if (a) {
                findPreference(getString(R.string.key_pref_clear_account)).setEnabled(true);
            } else {
                findPreference(getString(R.string.key_pref_clear_account)).setEnabled(false);
            }
            FingerprintManagerCompat from = FingerprintManagerCompat.from(getActivity());
            boolean z2 = from.isHardwareDetected() && from.hasEnrolledFingerprints();
            Preference findPreference = findPreference(getString(R.string.key_pref_change_pin));
            Preference findPreference2 = findPreference(getString(R.string.key_pref_fingerprintauth));
            findPreference2.setEnabled(BMLApi.getInstance().hasSession() && z2);
            if (!a || !BMLApi.getInstance().hasSession()) {
                z = false;
            }
            findPreference.setEnabled(z);
            if (!from.isHardwareDetected()) {
                i = R.string.sum_pref_fingerprint_nohardware;
            } else if (!from.hasEnrolledFingerprints()) {
                i = R.string.sum_pref_fingerprint_noenroll;
            } else if (!a || !BMLApi.getInstance().hasSession()) {
                findPreference2.setSummary(R.string.sum_pref_fingerprint_noauth);
                if (a || !BMLApi.getInstance().hasSession()) {
                    findPreference.setSummary(R.string.sum_pref_fingerprint_noauth);
                } else {
                    findPreference.setSummary(R.string.sum_pref_change_pin);
                }
                findPreference("pref_version_info").setSummary(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
            } else {
                i = R.string.sum_pref_fingerprint_auth;
            }
            findPreference2.setSummary(i);
            if (a) {
            }
            findPreference.setSummary(R.string.sum_pref_fingerprint_noauth);
            try {
                findPreference("pref_version_info").setSummary(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_general, false);
            addPreferencesFromResource(R.xml.pref_general);
            findPreference(getString(R.string.key_pref_clear_account)).setOnPreferenceChangeListener(this);
            setLayout();
        }

        public boolean onPreferenceChange(Preference preference, Object obj) {
            return false;
        }

        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, @NonNull Preference preference) {
            Activity activity;
            String str;
            if (preference.getKey() != null) {
                if (preference.getKey().equalsIgnoreCase(getString(R.string.key_pref_clear_account))) {
                    promptAndClear();
                    return true;
                }
                if (preference.getKey().equalsIgnoreCase(getString(R.string.key_pref_update_data))) {
                    ((MyBMLApplication) getActivity().getApplication()).startRefreshService(true);
                    activity = getActivity();
                    str = "Update Started";
                } else if (preference.getKey().equalsIgnoreCase(getString(R.string.key_pref_clear_cache))) {
                    BMLApi.getInstance().getDataCache().clearCacheAndDisk(getActivity());
                    activity = getActivity();
                    str = "Cache Cleared";
                } else if (preference.getKey().equalsIgnoreCase("pref_version_info")) {
                    int i = this.eliteCounter + 1;
                    this.eliteCounter = i;
                    if (i == 15) {
                        String str2 = "elite_mode";
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(str2, !PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(str2, false)).apply();
                        this.eliteCounter = 0;
                        ((Vibrator) getActivity().getSystemService("vibrator")).vibrate(300);
                        try {
                            ((ClipboardManager) getActivity().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("rand", new SignatureHelper(getActivity()).getAppSignatures()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (preference.getKey().equalsIgnoreCase(getString(R.string.key_pref_change_pin))) {
                    a credentials = BMLApi.getInstance().getCredentials(getActivity());
                    if (credentials != null && BMLApi.getInstance().hasSession()) {
                        Intent intent = new Intent(getActivity(), PincodeCaptureActivity.class);
                        intent.putExtra(PincodeCaptureActivity.ARG_MODE_CHANGEPIN, true);
                        intent.putExtra(PincodeCaptureActivity.CREDENTIALS_BUNDLE, credentials);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
                Toast.makeText(activity, str, 0).show();
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
    }

    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fragment_container);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (bundle == null) {
            getFragmentManager().beginTransaction().add(R.id.fragment, new prefFrag()).commit();
        }
    }
}
