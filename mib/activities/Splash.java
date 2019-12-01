package mv.com.bml.mib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import mv.com.bml.mib.R;

public class Splash extends AppCompatActivity {
    /* access modifiers changed from: private */
    public void postForward() {
        Intent intent;
        try {
            ProviderInstaller.installIfNeeded(this);
            intent = new Intent(this, DialDashboardActivity.class);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            intent = new Intent(this, DialDashboardActivity.class);
        } catch (GooglePlayServicesNotAvailableException e2) {
            e2.printStackTrace();
            intent = new Intent(this, DialDashboardActivity.class);
        } catch (Throwable th) {
            startActivity(new Intent(this, DialDashboardActivity.class));
            throw th;
        }
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    Splash.this.postForward();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable th) {
                    Splash.this.finish();
                    throw th;
                }
                Splash.this.finish();
            }
        }, 800);
    }
}
