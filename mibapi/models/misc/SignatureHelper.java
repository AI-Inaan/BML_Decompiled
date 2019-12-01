package mv.com.bml.mibapi.models.misc;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class SignatureHelper extends ContextWrapper {
    private static final String HASH_TYPE = "SHA-256";
    public static final int NUM_BASE64_CHAR = 11;
    public static final int NUM_HASHED_BYTES = 9;
    public static final String TAG = "SignatureHelper";

    public SignatureHelper(Context context) {
        super(context);
    }

    private static String hash(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" ");
        sb.append(str2);
        String sb2 = sb.toString();
        try {
            MessageDigest instance = MessageDigest.getInstance(HASH_TYPE);
            instance.update(sb2.getBytes(Charset.forName("UTF-8")));
            String substring = Base64.encodeToString(Arrays.copyOfRange(instance.digest(), 0, 9), 3).substring(0, 11);
            Log.d(TAG, String.format("pkg: %s -- hash: %s", new Object[]{str, substring}));
            return substring;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "hash:NoSuchAlgorithm", e);
            return null;
        }
    }

    public String getAppSignatures() {
        ArrayList arrayList = new ArrayList();
        try {
            String packageName = getPackageName();
            for (Signature charsString : getPackageManager().getPackageInfo(packageName, 64).signatures) {
                String hash = hash(packageName, charsString.toCharsString());
                if (hash != null) {
                    arrayList.add(String.format("%s", new Object[]{hash}));
                }
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Unable to find package to obtain hash.", e);
        }
        return Arrays.toString(arrayList.toArray());
    }
}
