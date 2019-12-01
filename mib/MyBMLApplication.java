package mv.com.bml.mib;

import android.app.Application;
import android.content.Intent;
import com.squareup.otto.Subscribe;
import mv.com.bml.mib.activities.DialDashboardActivity;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.BMLDataRefreshService;
import mv.com.bml.mibapi.apievents.SessionTimeoutEvent;

public class MyBMLApplication extends Application {
    int activity_count = 0;

    public void onCreate() {
        super.onCreate();
        BMLApi.getEventBus().register(this);
        startRefreshService(false);
    }

    public int registerActivityCount(int i) {
        this.activity_count += i;
        return this.activity_count;
    }

    @Subscribe
    public void sessionTimeout(SessionTimeoutEvent sessionTimeoutEvent) {
        BMLApi.getInstance().clearSession();
        BMLApi.getInstance().getDataCache().clearCache();
        Intent intent = new Intent(this, DialDashboardActivity.class);
        intent.setFlags(335577088);
        startActivity(intent);
    }

    public void startRefreshService(boolean z) {
        Intent intent = new Intent();
        intent.putExtra(BMLDataRefreshService.ARG_FORCE, z);
        BMLDataRefreshService.enqueueWork(this, intent);
    }
}
