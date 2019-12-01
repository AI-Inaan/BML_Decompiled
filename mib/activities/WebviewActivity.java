package mv.com.bml.mib.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import butterknife.BindView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebviewActivity extends BasePinCodeActivity {
    public static final String ARG_TITLE = "arg.title";
    public static final String ARG_URL = "arg.url";
    private static final int FCR = 1;
    /* access modifiers changed from: private */
    public String mCM;
    /* access modifiers changed from: private */
    public ValueCallback<Uri> mUM;
    /* access modifiers changed from: private */
    public ValueCallback<Uri[]> mUMA;
    @BindView(2131362099)
    WebView webView;

    public class Callback extends WebViewClient {
        public Callback() {
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            Toast.makeText(WebviewActivity.this.getApplicationContext(), "Failed loading app!", 0).show();
        }
    }

    /* access modifiers changed from: private */
    public File createImageFile() throws IOException {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("img_");
        sb.append(format);
        sb.append("_");
        return File.createTempFile(sb.toString(), ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
    }

    public static void loadWebViewActivity(Context context, String str, String str2) {
        Intent intent = new Intent(context, WebviewActivity.class);
        intent.putExtra(ARG_TITLE, str);
        intent.putExtra(ARG_URL, str2);
        context.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        Uri[] uriArr;
        super.onActivityResult(i, i2, intent);
        if (VERSION.SDK_INT >= 21) {
            if (i2 == -1 && i == 1) {
                if (this.mUMA != null) {
                    if (intent == null || intent.getData() == null) {
                        String str = this.mCM;
                        if (str != null) {
                            uriArr = new Uri[]{Uri.parse(str)};
                            this.mUMA.onReceiveValue(uriArr);
                            this.mUMA = null;
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            uriArr = new Uri[]{Uri.parse(dataString)};
                            this.mUMA.onReceiveValue(uriArr);
                            this.mUMA = null;
                        }
                    }
                } else {
                    return;
                }
            }
            uriArr = null;
            this.mUMA.onReceiveValue(uriArr);
            this.mUMA = null;
        } else if (i == 1 && this.mUM != null) {
            this.mUM.onReceiveValue((intent == null || i2 != -1) ? null : intent.getData());
            this.mUM = null;
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0097  */
    @android.annotation.SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(android.os.Bundle r8) {
        /*
            r7 = this;
            super.onCreate(r8)
            r8 = 2131492900(0x7f0c0024, float:1.8609265E38)
            r7.setContentView(r8)
            butterknife.ButterKnife.bind(r7)
            int r8 = android.os.Build.VERSION.SDK_INT
            r0 = 0
            r1 = 2
            r2 = 1
            r3 = 23
            if (r8 < r3) goto L_0x002e
            java.lang.String r8 = "android.permission.WRITE_EXTERNAL_STORAGE"
            int r3 = android.support.v4.content.ContextCompat.checkSelfPermission(r7, r8)
            java.lang.String r4 = "android.permission.CAMERA"
            if (r3 != 0) goto L_0x0025
            int r3 = android.support.v4.content.ContextCompat.checkSelfPermission(r7, r4)
            if (r3 == 0) goto L_0x002e
        L_0x0025:
            java.lang.String[] r3 = new java.lang.String[r1]
            r3[r0] = r8
            r3[r2] = r4
            android.support.v4.app.ActivityCompat.requestPermissions(r7, r3, r2)
        L_0x002e:
            android.content.Intent r8 = r7.getIntent()
            java.lang.String r3 = "arg.title"
            boolean r8 = r8.hasExtra(r3)
            if (r8 == 0) goto L_0x0045
            android.content.Intent r8 = r7.getIntent()
            java.lang.String r8 = r8.getStringExtra(r3)
            r7.setTitle(r8)
        L_0x0045:
            android.webkit.WebView r8 = r7.webView
            android.webkit.WebSettings r8 = r8.getSettings()
            r8.setJavaScriptEnabled(r2)
            r8.setAllowFileAccess(r2)
            int r3 = android.os.Build.VERSION.SDK_INT
            r4 = 21
            r5 = 0
            r6 = 19
            if (r3 < r4) goto L_0x0063
            r8.setMixedContentMode(r0)
        L_0x005d:
            android.webkit.WebView r8 = r7.webView
            r8.setLayerType(r1, r5)
            goto L_0x0071
        L_0x0063:
            int r8 = android.os.Build.VERSION.SDK_INT
            if (r8 < r6) goto L_0x0068
            goto L_0x005d
        L_0x0068:
            int r8 = android.os.Build.VERSION.SDK_INT
            if (r8 >= r6) goto L_0x0071
            android.webkit.WebView r8 = r7.webView
            r8.setLayerType(r2, r5)
        L_0x0071:
            android.webkit.WebView r8 = r7.webView
            mv.com.bml.mib.activities.WebviewActivity$Callback r0 = new mv.com.bml.mib.activities.WebviewActivity$Callback
            r0.<init>()
            r8.setWebViewClient(r0)
            android.webkit.WebView r8 = r7.webView
            java.lang.String r0 = "https://infeeds.com/"
            r8.loadUrl(r0)
            android.webkit.WebView r8 = r7.webView
            mv.com.bml.mib.activities.WebviewActivity$1 r0 = new mv.com.bml.mib.activities.WebviewActivity$1
            r0.<init>()
            r8.setWebChromeClient(r0)
            int r8 = android.os.Build.VERSION.SDK_INT
            if (r8 < r6) goto L_0x0093
            android.webkit.WebView.setWebContentsDebuggingEnabled(r2)
        L_0x0093:
            int r8 = android.os.Build.VERSION.SDK_INT
            if (r8 < r4) goto L_0x00a0
            android.webkit.WebView r8 = r7.webView
            android.webkit.WebSettings r8 = r8.getSettings()
            r8.setMixedContentMode(r1)
        L_0x00a0:
            android.content.Intent r8 = r7.getIntent()
            java.lang.String r0 = "arg.url"
            java.lang.String r8 = r8.getStringExtra(r0)
            android.webkit.WebView r0 = r7.webView
            r0.loadUrl(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: mv.com.bml.mib.activities.WebviewActivity.onCreate(android.os.Bundle):void");
    }

    public boolean onKeyDown(int i, @NonNull KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0 || i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            finish();
        }
        return true;
    }
}
