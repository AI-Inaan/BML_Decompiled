package mv.com.bml.mibapi.models.auth;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import java.io.Serializable;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.R;

public class a implements Serializable {
    String a = "2.0ab2";
    private String b;
    private String c;
    private String d;

    public a(Context context, String str, String str2) {
        a(context);
        this.b = a(str, this.a);
        this.c = a(str2, this.a);
        this.d = null;
    }

    public a(Context context, String str, String str2, String str3, int i) {
        a(context);
        this.b = str;
        this.c = str2;
        this.d = str3;
        a(context, i);
    }

    public static String a(String str, String str2) {
        try {
            return Base64.encodeToString(b(str2, str.getBytes("UTF8")), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] a(String str, byte[] bArr) throws Exception {
        byte[] bytes = str.getBytes("UTF-8");
        byte[] bArr2 = new byte[16];
        System.arraycopy(bytes, 0, bArr2, 0, Math.min(bytes.length, 16));
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(2, secretKeySpec, new IvParameterSpec(new byte[16]));
        return instance.doFinal(bArr);
    }

    public static String b(String str, String str2) {
        try {
            return new String(a(str2, Base64.decode(str, 0)), "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] b(String str, byte[] bArr) throws Exception {
        byte[] bytes = str.getBytes("UTF-8");
        byte[] bArr2 = new byte[16];
        System.arraycopy(bytes, 0, bArr2, 0, Math.min(bytes.length, 16));
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(1, secretKeySpec, new IvParameterSpec(new byte[16]));
        return instance.doFinal(bArr);
    }

    public void a(Context context) {
        String string = Secure.getString(context.getContentResolver(), "android_id");
        StringBuilder sb = new StringBuilder();
        if (string == null) {
            string = "ksdfjlKLJ8KJL789423423LKJ";
        }
        sb.append(string);
        sb.append("PEP4BaFpAfn0u2xTuggxaTe3Wl9xT6RQQQcA336GOcwqdxfYacqtj6JISM9THgEj");
        sb.append(context.getString(R.string.cred_signature));
        this.a = sb.toString();
    }

    public void a(Context context, int i) {
        if (i < 2 && i == 1) {
            Log.d("CREDS", "UPDATING FROM V1");
            this.b = a(b(this.b, "PEP4BaFpAfn0u2xTuggxaTe3Wl9xT6RQQQcA336GOcwqdxfYacqtj6JISM9THgEj"), this.a);
            this.c = a(b(this.c, "PEP4BaFpAfn0u2xTuggxaTe3Wl9xT6RQQQcA336GOcwqdxfYacqtj6JISM9THgEj"), this.a);
            this.d = a(b(this.d, "PEP4BaFpAfn0u2xTuggxaTe3Wl9xT6RQQQcA336GOcwqdxfYacqtj6JISM9THgEj"), this.a);
            BMLApi.getInstance().setCredentials(context, this);
        }
    }

    public void a(String str) {
        this.d = a(str, this.a);
    }

    public boolean a() {
        return (this.b == null || this.c == null || this.d == null) ? false : true;
    }

    public String b() {
        String str = this.b;
        if (str != null) {
            return b(str, this.a);
        }
        return null;
    }

    public String c() {
        String str = this.c;
        if (str != null) {
            return b(str, this.a);
        }
        return null;
    }

    public int d() {
        String b2 = b(this.d, this.a);
        if (this.d == null) {
            return -1;
        }
        if (b2 == null) {
            b2 = "0";
        }
        return Integer.parseInt(b2);
    }

    public String e() {
        return this.b;
    }

    public String f() {
        return this.c;
    }

    public String g() {
        return this.d;
    }
}
