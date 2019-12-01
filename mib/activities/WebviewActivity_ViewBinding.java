package mv.com.bml.mib.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class WebviewActivity_ViewBinding implements Unbinder {
    private WebviewActivity target;

    @UiThread
    public WebviewActivity_ViewBinding(WebviewActivity webviewActivity) {
        this(webviewActivity, webviewActivity.getWindow().getDecorView());
    }

    @UiThread
    public WebviewActivity_ViewBinding(WebviewActivity webviewActivity, View view) {
        this.target = webviewActivity;
        webviewActivity.webView = (WebView) Utils.findRequiredViewAsType(view, R.id.webView, "field 'webView'", WebView.class);
    }

    @CallSuper
    public void unbind() {
        WebviewActivity webviewActivity = this.target;
        if (webviewActivity != null) {
            this.target = null;
            webviewActivity.webView = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
