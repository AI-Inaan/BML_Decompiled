package mv.com.bml.mibapi.apievents;

import android.text.TextUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPSMSReceived {
    private String message;
    private String status;

    public OTPSMSReceived(String str, String str2) {
        this.message = str;
        this.status = str2;
    }

    public String getMessage() {
        return this.message;
    }

    public String getOtpcode() {
        if (!TextUtils.isEmpty(this.message)) {
            Matcher matcher = Pattern.compile("One Time Password is (\\d+)\\.").matcher(this.message);
            if (matcher.find()) {
                String group = matcher.group(1);
                if (TextUtils.isDigitsOnly(group)) {
                    return group;
                }
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<OTPSMSReceived from: ");
        sb.append(this.status);
        sb.append("> ");
        sb.append(this.message);
        return sb.toString();
    }
}
