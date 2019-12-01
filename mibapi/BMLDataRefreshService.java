package mv.com.bml.mibapi;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import io.paperdb.Paper;
import java.text.SimpleDateFormat;
import java.util.Date;
import mv.com.bml.mibapi.databases.AuxDataDB;
import mv.com.bml.mibapi.models.misc.ExchangeRateResponse;
import mv.com.bml.mibapi.models.misc.ScamData;

public class BMLDataRefreshService extends JobIntentService {
    public static final String ARG_FORCE = "force.data";
    public static final int NOTIFICATION_ID = 1667;
    private static final String TAG = "BMLDataRefreshService";
    private static final int aux_update_threshold = 24;
    private AuxDataDB auxdb;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, BMLDataRefreshService.class, (int) NOTIFICATION_ID, intent);
    }

    private void updateExchange() {
        try {
            ExchangeRateResponse exchangeData = BMLApi.getAuxClient().getExchangeData();
            if (exchangeData.rates != null) {
                this.auxdb.updateExchangeData(exchangeData.rates);
                this.auxdb.setLastUpdated(AuxDataDB.ENDPOINT_EXCHANGE);
            }
        } catch (Exception e) {
            Log.e(TAG, "exchange data update failed");
            e.printStackTrace();
        }
    }

    private void updateLocations() {
        try {
            this.auxdb.updateLocationData(BMLApi.getAuxClient().getLocationData());
            this.auxdb.setLastUpdated(AuxDataDB.ENDPOINT_LOCATIONS);
        } catch (Exception e) {
            Log.e(TAG, "location data update failed");
            e.printStackTrace();
        }
    }

    private void updateScamGuard() {
        Log.d(TAG, "UPDATING SCAMGUARD DATA");
        try {
            ScamData scamGuardBlacklist = BMLApi.getAuxClient().getScamGuardBlacklist();
            if (scamGuardBlacklist != null && scamGuardBlacklist.numbers != null && scamGuardBlacklist.numbers.size() > 0) {
                Paper.init(getApplicationContext());
                BMLApi.getInstance().getDataCache().setCacheItem("scamguard.blacklist", scamGuardBlacklist.numbers, true);
            }
        } catch (Exception e) {
            Log.e(TAG, "scamguard data update failed");
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "Background data service started");
        this.auxdb = new AuxDataDB(getApplicationContext());
        boolean booleanExtra = intent.getBooleanExtra(ARG_FORCE, false);
        int parseInt = Integer.parseInt(new SimpleDateFormat("yyyyMMddHH").format(new Date()));
        int lastUpdated = parseInt - this.auxdb.getLastUpdated(AuxDataDB.ENDPOINT_LOCATIONS);
        if (lastUpdated >= 24 || booleanExtra) {
            StringBuilder sb = new StringBuilder();
            sb.append("Location data outdated, updating ");
            sb.append(lastUpdated);
            Log.i(TAG, sb.toString());
            updateLocations();
        }
        int lastUpdated2 = parseInt - this.auxdb.getLastUpdated(AuxDataDB.ENDPOINT_EXCHANGE);
        if (lastUpdated2 >= 24 || booleanExtra) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Exchange rate data outdated, updating now:");
            sb2.append(parseInt);
            sb2.append("delta: ");
            sb2.append(lastUpdated2);
            Log.i(TAG, sb2.toString());
            updateExchange();
        }
        this.auxdb.close();
        stopSelf();
    }
}
