package mv.com.bml.mibapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import mv.com.bml.mibapi.apievents.OTPSMSReceived;

public class OTPSMSReceiver extends BroadcastReceiver {
    public static void subscribe(Context context) {
        Log.d("SMS", "RETRIEVER SUBSCRIBE CALLED");
        Task startSmsRetriever = SmsRetriever.getClient(context).startSmsRetriever();
        startSmsRetriever.addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void voidR) {
                Log.d("SMS", "SUCCESSFULLY STARTED LISTENING");
            }
        });
        startSmsRetriever.addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception exc) {
                Log.d("SMS", "FAILED STARTED LISTENING");
                exc.printStackTrace();
            }
        });
    }

    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            int statusCode = status.getStatusCode();
            if (statusCode == 0) {
                String str = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                StringBuilder sb = new StringBuilder();
                sb.append("RECEIVED - ");
                sb.append(str);
                Log.d("SMS", sb.toString());
                BMLApi.getEventBus().post(new OTPSMSReceived(str, status.getStatusMessage()));
            } else if (statusCode == 15) {
                Log.d("SMS", "LISTENER TIMED OUT");
            }
        }
    }
}
