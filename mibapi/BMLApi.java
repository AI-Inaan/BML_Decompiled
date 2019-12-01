package mv.com.bml.mibapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.ConnectionSpec.Builder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Interceptor.Chain;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.TlsVersion;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import mv.com.bml.mibapi.apievents.SessionTimeoutEvent;
import mv.com.bml.mibapi.models.auth.a;
import retrofit.RequestInterceptor;
import retrofit.RequestInterceptor.RequestFacade;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class BMLApi implements RequestInterceptor {
    public static final int API_NOT_AUTHD_RESPONSE_CODE = 401;
    public static final String BML_API_ENDPOINT = "https://www.bankofmaldives.com.mv/internetbanking/api/";
    public static final String BML_AUX_API_ENDPOINT = "https://ebanking.bankofmaldives.com.mv/";
    public static final String BML_PHOTOCARD_ENDPOINT = "https://ebanking.bankofmaldives.com.mv/bmlphotocard/";
    private static final String TAG = "BMLAPI";
    private static Bus _eventbus;
    private static BMLApi _instance;
    private Picasso _authedpicasso;
    private BMLAuxRestClient _auxClient;
    private BMLDataCache _cache;
    private BMLRestClient _restclient;
    private long last_network_request_timestamp;
    /* access modifiers changed from: private */
    public HashMap<String, String> mSessionCookies;
    private long max_session_duration = 500000;
    private String user_agent;

    private class Cookie {
        String name;
        String value;

        public Cookie(String str, String str2) {
            this.name = str;
            this.value = str2;
        }
    }

    private class InterceptingOkClient extends OkClient {
        InterceptingOkClient(OkHttpClient okHttpClient) {
            super(okHttpClient);
            if (VERSION.SDK_INT < 22) {
                try {
                    TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    instance.init(null);
                    TrustManager[] trustManagers = instance.getTrustManagers();
                    if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unexpected default trust managers:");
                        sb.append(Arrays.toString(trustManagers));
                        throw new IllegalStateException(sb.toString());
                    }
                    X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];
                    SSLContext instance2 = SSLContext.getInstance("TLSv1.2");
                    instance2.init(null, null, null);
                    okHttpClient.setSslSocketFactory(new Tls12SocketFactory(instance2.getSocketFactory()));
                    ConnectionSpec build = new Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).build();
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(build);
                    arrayList.add(ConnectionSpec.COMPATIBLE_TLS);
                    arrayList.add(ConnectionSpec.CLEARTEXT);
                    okHttpClient.setConnectionSpecs(arrayList);
                } catch (Exception e) {
                    Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", e);
                }
            }
        }

        public Response execute(Request request) throws IOException {
            Response execute = super.execute(request);
            if (execute != null && execute.getStatus() == 401) {
                BMLApi.getEventBus().post(new SessionTimeoutEvent());
            }
            return execute;
        }
    }

    public BMLApi() {
        StringBuilder sb = new StringBuilder();
        sb.append("bml-mobile-banking/53 (");
        sb.append(Build.PRODUCT);
        sb.append("; Android ");
        sb.append(VERSION.RELEASE);
        sb.append("; ");
        sb.append(Build.MODEL);
        sb.append(")");
        this.user_agent = sb.toString();
    }

    private BMLAuxRestClient getAUXRestClient() {
        BMLAuxRestClient bMLAuxRestClient = this._auxClient;
        if (bMLAuxRestClient != null) {
            return bMLAuxRestClient;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setProtocols(Arrays.asList(new Protocol[]{Protocol.HTTP_1_1}));
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        this._auxClient = (BMLAuxRestClient) new RestAdapter.Builder().setEndpoint(BML_AUX_API_ENDPOINT).setConverter(new GsonConverter(new GsonBuilder().serializeNulls().create())).setLogLevel(LogLevel.FULL).setRequestInterceptor(this).setClient((Client) new InterceptingOkClient(okHttpClient)).build().create(BMLAuxRestClient.class);
        return this._auxClient;
    }

    public static BMLAuxRestClient getAuxClient() {
        return getInstance().getAUXRestClient();
    }

    public static BMLRestClient getClient() {
        return getInstance().getRestClient();
    }

    public static Bus getEventBus() {
        if (_eventbus == null) {
            _eventbus = new Bus(ThreadEnforcer.ANY);
        }
        return _eventbus;
    }

    public static BMLApi getInstance() {
        if (_instance == null) {
            _instance = new BMLApi();
        }
        return _instance;
    }

    private BMLRestClient getRestClient() {
        BMLRestClient bMLRestClient = this._restclient;
        if (bMLRestClient != null) {
            return bMLRestClient;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setProtocols(Arrays.asList(new Protocol[]{Protocol.HTTP_1_1}));
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        this._restclient = (BMLRestClient) new RestAdapter.Builder().setEndpoint(BML_API_ENDPOINT).setConverter(new GsonConverter(new GsonBuilder().serializeNulls().create())).setLogLevel(LogLevel.FULL).setRequestInterceptor(this).setClient((Client) new InterceptingOkClient(okHttpClient)).build().create(BMLRestClient.class);
        return this._restclient;
    }

    private Cookie parseCookie(String str) {
        return new Cookie(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1, str.indexOf(";")));
    }

    public void clearSession() {
        clearSessionCookie();
        getDataCache().clearCache();
    }

    public void clearSessionCookie() {
        if (hasSession()) {
            this.mSessionCookies.clear();
        }
    }

    public Picasso getAuthedPicasso(Context context) {
        Picasso picasso = this._authedpicasso;
        if (picasso != null) {
            return picasso;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new Interceptor() {
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                String str;
                com.squareup.okhttp.Request.Builder addHeader = chain.request().newBuilder().addHeader("X-Requested-With", "XMLHttpRequest");
                if (BMLApi.this.hasSession()) {
                    Iterator it = BMLApi.this.mSessionCookies.keySet().iterator();
                    String str2 = "";
                    while (true) {
                        str = str2;
                        if (!it.hasNext()) {
                            break;
                        }
                        String str3 = (String) it.next();
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(str3);
                        sb.append("=");
                        sb.append((String) BMLApi.this.mSessionCookies.get(str3));
                        sb.append(";");
                        str2 = sb.toString();
                    }
                    addHeader.addHeader("Cookie", str);
                }
                return chain.proceed(addHeader.build());
            }
        });
        this._authedpicasso = new Picasso.Builder(context).downloader(new OkHttpDownloader(okHttpClient)).build();
        return this._authedpicasso;
    }

    public a getCredentials(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = defaultSharedPreferences.getString(context.getString(R.string.cred_username), null);
        String string2 = defaultSharedPreferences.getString(context.getString(R.string.cred_password), null);
        String string3 = defaultSharedPreferences.getString(context.getString(R.string.cred_pincode), null);
        int i = defaultSharedPreferences.getInt(context.getString(R.string.cred_version), 1);
        if (i == 1 && (string == null || string2 == null || string3 == null)) {
            i = 2;
        }
        return new a(context, string, string2, string3, i);
    }

    public BMLDataCache getDataCache() {
        if (this._cache == null) {
            this._cache = BMLDataCache.getInstance();
        }
        return this._cache;
    }

    public boolean hasSession() {
        if (this.mSessionCookies == null) {
            this.mSessionCookies = new HashMap<>();
        }
        return !this.mSessionCookies.isEmpty();
    }

    public void intercept(RequestFacade requestFacade) {
        requestFacade.addHeader("X-Requested-With", "XMLHttpRequest");
        requestFacade.addHeader("User-Agent", this.user_agent);
        this.last_network_request_timestamp = new Date().getTime();
        if (hasSession()) {
            Iterator it = this.mSessionCookies.keySet().iterator();
            String str = "";
            while (true) {
                String str2 = str;
                if (it.hasNext()) {
                    String str3 = (String) it.next();
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(str3);
                    sb.append("=");
                    sb.append((String) this.mSessionCookies.get(str3));
                    sb.append(";");
                    str = sb.toString();
                } else {
                    requestFacade.addHeader("Cookie", str2);
                    return;
                }
            }
        }
    }

    public boolean isSessionTimeoutExceeded() {
        return isSessionTimeoutExceeded(true);
    }

    public boolean isSessionTimeoutExceeded(boolean z) {
        long time = new Date().getTime() - this.last_network_request_timestamp;
        StringBuilder sb = new StringBuilder();
        sb.append("session lapse duration is ");
        sb.append(time);
        Log.d(TAG, sb.toString());
        if (!hasSession() || time < this.max_session_duration) {
            return false;
        }
        if (z) {
            getEventBus().post(new SessionTimeoutEvent());
        }
        return true;
    }

    public void setCredentials(Context context, a aVar) {
        if (aVar == null || aVar.a()) {
            Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
            if (aVar != null) {
                edit.putString(context.getString(R.string.cred_username), aVar.e());
                edit.putString(context.getString(R.string.cred_password), aVar.f());
                edit.putString(context.getString(R.string.cred_pincode), aVar.g());
                edit.putInt(context.getString(R.string.cred_version), 2);
            } else {
                edit.remove(context.getString(R.string.cred_username));
                edit.remove(context.getString(R.string.cred_password));
                edit.remove(context.getString(R.string.cred_pincode));
                edit.remove(context.getString(R.string.cred_version));
                getDataCache().clearCache();
            }
            edit.apply();
            Log.d(TAG, "credentials stored");
        }
    }

    public void setMaxSessionDuration(long j) {
        StringBuilder sb = new StringBuilder();
        sb.append("session timeout duration set to ");
        sb.append(j);
        sb.append("ms");
        Log.d(TAG, sb.toString());
        if (j != 0) {
            this.max_session_duration = j;
        }
    }

    public void setSessionCookie(String str, String str2) {
        this.mSessionCookies.put(str, str2);
    }

    public void setSessionCookie(List<Header> list) {
        for (Header header : list) {
            if (header.getName() != null && header.getName().trim().equalsIgnoreCase("Set-Cookie")) {
                Cookie parseCookie = parseCookie(header.getValue());
                setSessionCookie(parseCookie.name, parseCookie.value);
            }
        }
    }
}
