package mv.com.bml.mib.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationResult;
import android.view.View;
import android.view.animation.AnimationUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mv.com.bml.mib.R;
import mv.com.bml.mib.widgets.CircleImageView;
import mv.com.bml.mib.widgets.CircleLayout;
import mv.com.bml.mib.widgets.CircleLayout.OnItemClickListener;
import mv.com.bml.mibapi.BMLApi;

public class DialDashboardActivity extends BasePinCodeActivity implements OnItemClickListener {
    private static final String TAG = "SexyDashboard";
    @BindView(2131361852)
    CircleImageView btn_history;
    @BindView(2131361846)
    CircleImageView btn_lock;
    @BindView(2131361860)
    CircleImageView btn_settings;
    @BindView(2131361862)
    CircleImageView btn_transfer;
    @BindView(2131361899)
    CircleLayout mCircleLayout;

    private boolean isUnlocked(boolean z) {
        boolean z2 = BMLApi.getInstance().hasSession() && !BMLApi.getInstance().isSessionTimeoutExceeded();
        if (z && !z2) {
            this.btn_lock.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            ((Vibrator) getSystemService("vibrator")).vibrate(80);
        }
        return z2;
    }

    private void listen_fingerprint_elite() {
        FingerprintManagerCompat from = FingerprintManagerCompat.from(this);
        if (from.isHardwareDetected() && from.hasEnrolledFingerprints() && isEliteModeEnabled()) {
            boolean z = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fingerprint_auth", false);
            if (!isUnlocked(false) && z) {
                from.authenticate(null, 0, null, new AuthenticationCallback() {
                    public void onAuthenticationError(int i, CharSequence charSequence) {
                        super.onAuthenticationError(i, charSequence);
                        DialDashboardActivity.this.btn_lock.startAnimation(AnimationUtils.loadAnimation(DialDashboardActivity.this, R.anim.shake));
                        ((Vibrator) DialDashboardActivity.this.getSystemService("vibrator")).vibrate(80);
                    }

                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        DialDashboardActivity.this.btn_lock.startAnimation(AnimationUtils.loadAnimation(DialDashboardActivity.this, R.anim.shake));
                        ((Vibrator) DialDashboardActivity.this.getSystemService("vibrator")).vibrate(80);
                    }

                    public void onAuthenticationHelp(int i, CharSequence charSequence) {
                        super.onAuthenticationHelp(i, charSequence);
                    }

                    public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
                        super.onAuthenticationSucceeded(authenticationResult);
                        Intent intent = new Intent(DialDashboardActivity.this, LoginActivity.class);
                        intent.putExtra(LoginActivity.ARG_AUTO_PROCEED, true);
                        DialDashboardActivity.this.startActivityForResult(intent, 0);
                    }
                }, null);
            }
        }
    }

    private void setLayoutItems() {
        int i;
        this.btn_lock.setMinimumWidth(this.btn_settings.getWidth());
        this.btn_lock.setMinimumHeight(this.btn_settings.getHeight());
        if (isUnlocked(false)) {
            this.btn_history.setImageResource(R.drawable.ic_accounts);
            this.btn_transfer.setImageResource(R.drawable.ic_paytransfer);
            boolean a = BMLApi.getInstance().getCredentials(this).a();
            i = R.drawable.login;
        } else {
            this.btn_history.setImageResource(R.drawable.ic_accounts_disabled);
            this.btn_transfer.setImageResource(R.drawable.ic_paytransfer_disabled);
            boolean a2 = BMLApi.getInstance().getCredentials(this).a();
            i = R.drawable.logout;
        }
        this.btn_lock.setImageResource(i);
    }

    @OnClick({2131361846})
    public void onClick(View view) {
        if (isUnlocked(false)) {
            BMLApi.getInstance().clearSession();
            BMLApi.getInstance().getDataCache().clearCache();
            setLayoutItems();
            listen_fingerprint_elite();
            return;
        }
        startActivityForResult(new Intent(this, LoginActivity.class), 0);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_dialdash);
        getSupportActionBar().setDisplayOptions(16);
        getSupportActionBar().setCustomView((int) R.layout.actionbar);
        ButterKnife.bind((Activity) this);
        this.mCircleLayout.setOnItemClickListener(this);
        setLayoutItems();
    }

    public void onItemClick(View view, String str) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_contact /*2131361847*/:
                intent = new Intent(this, ContactUsActivity.class);
                break;
            case R.id.btn_exchange /*2131361851*/:
                intent = new Intent(this, ExchangeRateActivity.class);
                break;
            case R.id.btn_history /*2131361852*/:
                if (isUnlocked(true)) {
                    intent = new Intent(this, DashboardActivity.class);
                    break;
                } else {
                    return;
                }
            case R.id.btn_photocard /*2131361856*/:
                WebviewActivity.loadWebViewActivity(this, getString(R.string.photocard_title), BMLApi.BML_PHOTOCARD_ENDPOINT);
                return;
            case R.id.btn_settings /*2131361860*/:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.btn_transfer /*2131361862*/:
                if (isUnlocked(true)) {
                    intent = new Intent(this, PayOrTransferActivity.class);
                    break;
                } else {
                    return;
                }
            default:
                return;
        }
        startActivity(intent);
    }

    public void onResumeFromSleep() {
        if (!isUnlocked(false) || BMLApi.getInstance().isSessionTimeoutExceeded()) {
            setLayoutItems();
        } else {
            super.onResumeFromSleep();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        setLayoutItems();
        listen_fingerprint_elite();
    }
}
