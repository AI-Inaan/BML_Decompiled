package mv.com.bml.mib.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationResult;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.ButterKnife;
import mv.com.bml.mib.R;
import mv.com.bml.mib.widgets.PincodeEntry;
import mv.com.bml.mib.widgets.PincodeEntry.OnPincodeEntryListener;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.auth.AuthResponse;
import mv.com.bml.mibapi.models.auth.FirstTimeCheck;
import mv.com.bml.mibapi.models.auth.UsernamePojo;
import mv.com.bml.mibapi.models.auth.a;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BasePinCodeActivity implements OnClickListener, Callback<AuthResponse> {
    public static final String ARG_AUTO_PROCEED = "arg.auto";
    public static final String ARG_ERROR_TEXT = "mv.com.bml.errortextargument";
    private static final String TAG = "ACTIVITY_LOGIN";
    boolean auto_proceed = false;
    a cc;
    /* access modifiers changed from: private */
    public boolean flag_pincode = false;
    Button mCancelButton;
    TextView mErrorTextIndicator;
    Button mForgotButton;
    Button mLoginButton;
    /* access modifiers changed from: private */
    public String mPassword;
    EditText mPasswordEdit;
    PincodeEntry mPincodeEntry;
    /* access modifiers changed from: private */
    public String mUsername;
    EditText mUsernameEdit;

    /* access modifiers changed from: private */
    public void doLogin() {
        int i;
        String obj = this.mUsernameEdit.getText().toString();
        String obj2 = this.mPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(obj)) {
            i = R.string.loginerror_unvalid_user;
        } else if (TextUtils.isEmpty(obj2)) {
            i = R.string.loginerror_unvalid_pass;
        } else {
            doLogin(obj, obj2);
            return;
        }
        setErrorText(getString(i));
    }

    private void doLogin(String str, String str2) {
        toggleWidgetState(false);
        setStatusText("Please Wait");
        this.mUsername = str;
        this.mPassword = str2;
        BMLApi.getClient().doLogin(this.mUsername, this.mPassword, this);
    }

    /* access modifiers changed from: private */
    public void registerIncorretPinAttempt() {
        this.mPincodeEntry.clearText(true);
        setErrorText(getString(R.string.loginerror_invalidpin));
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String str = "pinRetries";
        int i = defaultSharedPreferences.getInt(str, 0) + 1;
        if (i > getResources().getInteger(R.integer.pincode_retry_max_count) - 1) {
            defaultSharedPreferences.edit().putInt(str, 0).apply();
            BMLApi.getInstance().setCredentials(this, null);
            BMLApi.getInstance().getDataCache().clearCacheAndDisk(this);
            BMLApi.getInstance().clearSession();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(ARG_ERROR_TEXT, getString(R.string.loginerror_pinretrylimitreached_norestart));
            intent.setFlags(67108864);
            startActivity(intent);
            finish();
            return;
        }
        if (i == getResources().getInteger(R.integer.pincode_retry_warn_threshold)) {
            setErrorText(getString(R.string.retry_treshold_warning));
        }
        defaultSharedPreferences.edit().putInt(str, i).apply();
    }

    /* access modifiers changed from: private */
    public void setErrorText(String str) {
        TextView textView;
        int i;
        if (this.flag_pincode) {
            this.mPincodeEntry.setSubStatus(str, ContextCompat.getColor(this, R.color.primary_red));
            return;
        }
        this.mErrorTextIndicator.setText(str);
        this.mErrorTextIndicator.setTextColor(ContextCompat.getColor(this, R.color.primary_red));
        if (str != null) {
            textView = this.mErrorTextIndicator;
            i = 0;
        } else {
            textView = this.mErrorTextIndicator;
            i = 4;
        }
        textView.setVisibility(i);
    }

    private void setStatusText(String str) {
        TextView textView;
        int i;
        if (this.flag_pincode) {
            this.mPincodeEntry.setStatus(str);
            return;
        }
        this.mErrorTextIndicator.setText(str);
        this.mErrorTextIndicator.setTextColor(ContextCompat.getColor(this, R.color.primary_grey));
        if (str != null) {
            textView = this.mErrorTextIndicator;
            i = 0;
        } else {
            textView = this.mErrorTextIndicator;
            i = 4;
        }
        textView.setVisibility(i);
    }

    private void setUserPassView() {
        this.mPasswordEdit.setImeActionLabel("Login", 66);
        this.mPasswordEdit.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6 && i != 66) {
                    return false;
                }
                LoginActivity.this.doLogin();
                return true;
            }
        });
        AnonymousClass7 r0 = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                LoginActivity.this.setErrorText(null);
            }
        };
        this.mUsernameEdit.addTextChangedListener(r0);
        this.mPasswordEdit.addTextChangedListener(r0);
        this.mLoginButton.setOnClickListener(this);
        this.mForgotButton.setOnClickListener(this);
        this.mCancelButton.setOnClickListener(this);
        Intent intent = getIntent();
        String str = ARG_ERROR_TEXT;
        if (intent.hasExtra(str)) {
            setErrorText(getIntent().getExtras().getString(str));
        }
    }

    /* access modifiers changed from: private */
    public void startDashboard() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("pinRetries", 0).apply();
        if (!BMLApi.getInstance().hasSession() || BMLApi.getInstance().isSessionTimeoutExceeded(false)) {
            a credentials = BMLApi.getInstance().getCredentials(this);
            doLogin(credentials.b(), credentials.c());
            return;
        }
        if (getCallingActivity() != null) {
            finish();
        } else {
            startActivity(new Intent(this, DashboardActivity.class));
        }
    }

    /* access modifiers changed from: private */
    public void toggleWidgetState(boolean z) {
        if (this.flag_pincode) {
            this.mPincodeEntry.setEnabled(z);
            return;
        }
        this.mUsernameEdit.setEnabled(z);
        this.mPasswordEdit.setEnabled(z);
        this.mLoginButton.setEnabled(z);
    }

    public void failure(RetrofitError retrofitError) {
        toggleWidgetState(true);
        setErrorText("There was an error. Please try again later.");
        if (this.flag_pincode) {
            this.mPincodeEntry.clearText();
        }
    }

    public void onBackPressed() {
        if (getCallingActivity() == null || !BMLApi.getInstance().hasSession()) {
            super.onBackPressed();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.edit_login_cancel /*2131361911*/:
                intent = new Intent(this, DialDashboardActivity.class);
                intent.setFlags(335577088);
                break;
            case R.id.edit_login_forgot /*2131361912*/:
                intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(BMLApi.BML_API_ENDPOINT));
                break;
            case R.id.edit_login_submit /*2131361914*/:
                doLogin();
                return;
            default:
                return;
        }
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.cc = BMLApi.getInstance().getCredentials(this);
        getSupportActionBar().setDisplayOptions(16);
        getSupportActionBar().setCustomView((int) R.layout.actionbar_login);
        this.auto_proceed = getIntent().getBooleanExtra(ARG_AUTO_PROCEED, false);
        if (this.cc.a()) {
            this.flag_pincode = true;
            getSupportActionBar().hide();
            setContentView((int) R.layout.fragment_pincode_capture);
            this.mPincodeEntry = (PincodeEntry) ButterKnife.findById((Activity) this, (int) R.id.pin_entry_simple);
            findViewById(R.id.loginheader_image).setVisibility(0);
            this.mPincodeEntry.setPincodeEntryListener(new OnPincodeEntryListener() {
                public void onPincodeEntryChanged() {
                    LoginActivity.this.setErrorText(null);
                }

                public void onPincodeEntryComplete(int i) {
                    int d = LoginActivity.this.cc.d();
                    if (d < 0) {
                        new Builder(LoginActivity.this, R.style.AppTheme_DialogStyle).setTitle((int) R.string.error_generic).setMessage((int) R.string.pin_error_message).setPositiveButton((int) R.string.logout, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BMLApi.getInstance().setCredentials(LoginActivity.this, null);
                                BMLApi.getInstance().clearSession();
                                BMLApi.getInstance().getDataCache().clearCacheAndDisk(LoginActivity.this);
                                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                defaultSharedPreferences.edit().remove("pinRetries").apply();
                                defaultSharedPreferences.edit().remove("fingerprint_auth").apply();
                                dialogInterface.dismiss();
                                Intent intent = new Intent(LoginActivity.this, DialDashboardActivity.class);
                                intent.addFlags(335544320);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        }).show();
                    } else if (d == i) {
                        LoginActivity.this.startDashboard();
                    } else {
                        LoginActivity.this.registerIncorretPinAttempt();
                    }
                }
            });
            FingerprintManagerCompat from = FingerprintManagerCompat.from(this);
            if (from.isHardwareDetected() && from.hasEnrolledFingerprints()) {
                boolean z = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fingerprint_auth", false);
                View inflate = LayoutInflater.from(this).inflate(R.layout.fragment_fingerprint_dialog, null);
                final TextView textView = (TextView) inflate.findViewById(R.id.textStatus);
                if (z) {
                    final CancellationSignal cancellationSignal = new CancellationSignal();
                    final AlertDialog create = new Builder(this, R.style.AppTheme_DialogStyle).setTitle((int) R.string.unlock).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cancellationSignal.cancel();
                            dialogInterface.dismiss();
                        }
                    }).setOnCancelListener(new OnCancelListener() {
                        public void onCancel(DialogInterface dialogInterface) {
                            cancellationSignal.cancel();
                            dialogInterface.dismiss();
                        }
                    }).setView(inflate).create();
                    cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                        public void onCancel() {
                            AlertDialog alertDialog = create;
                            if (alertDialog != null && alertDialog.isShowing()) {
                                create.dismiss();
                            }
                        }
                    });
                    AnonymousClass5 r6 = new AuthenticationCallback() {
                        public void onAuthenticationError(int i, CharSequence charSequence) {
                            super.onAuthenticationError(i, charSequence);
                            textView.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.bml_red_muted));
                            textView.setText(charSequence.toString());
                        }

                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            LoginActivity.this.registerIncorretPinAttempt();
                            textView.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.bml_red_muted));
                            textView.setText(R.string.not_recognized);
                        }

                        public void onAuthenticationHelp(int i, CharSequence charSequence) {
                            super.onAuthenticationHelp(i, charSequence);
                            textView.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.secondary_blue));
                            textView.setText(charSequence);
                        }

                        public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
                            super.onAuthenticationSucceeded(authenticationResult);
                            AlertDialog alertDialog = create;
                            if (alertDialog != null && alertDialog.isShowing()) {
                                create.dismiss();
                            }
                            LoginActivity.this.startDashboard();
                        }
                    };
                    if (!isEliteModeEnabled() || !this.auto_proceed) {
                        create.show();
                        from.authenticate(null, 0, cancellationSignal, r6, null);
                        return;
                    }
                    startDashboard();
                    return;
                }
                return;
            }
            return;
        }
        setContentView((int) R.layout.activity_login);
        this.mUsernameEdit = (EditText) ButterKnife.findById((Activity) this, (int) R.id.edit_login_username);
        this.mPasswordEdit = (EditText) ButterKnife.findById((Activity) this, (int) R.id.edit_login_password);
        this.mLoginButton = (Button) ButterKnife.findById((Activity) this, (int) R.id.edit_login_submit);
        this.mErrorTextIndicator = (TextView) ButterKnife.findById((Activity) this, (int) R.id.status_text);
        this.mForgotButton = (Button) ButterKnife.findById((Activity) this, (int) R.id.edit_login_forgot);
        this.mCancelButton = (Button) ButterKnife.findById((Activity) this, (int) R.id.edit_login_cancel);
        setUserPassView();
        BMLApi.getInstance().getDataCache().clearCacheAndDisk(this);
    }

    public void onResumeFromSleep() {
        BMLApi.getInstance().isSessionTimeoutExceeded();
    }

    public void success(AuthResponse authResponse, Response response) {
        toggleWidgetState(true);
        BMLApi.getInstance().setSessionCookie(response.getHeaders());
        if (authResponse.authenticated) {
            BMLApi.getInstance().setMaxSessionDuration(600000);
            BMLApi.getClient().getFirstTimeLogin(new UsernamePojo(this.mUsername), new Callback<FirstTimeCheck>() {
                public void failure(RetrofitError retrofitError) {
                    LoginActivity.this.setErrorText("There was an error. Please try again later.");
                    BMLApi.getInstance().clearSession();
                    if (LoginActivity.this.flag_pincode) {
                        LoginActivity.this.mPincodeEntry.clearText();
                    }
                    LoginActivity.this.toggleWidgetState(true);
                }

                public void success(FirstTimeCheck firstTimeCheck, Response response) {
                    Builder builder;
                    Builder cancelable;
                    DialogInterface.OnClickListener r1;
                    String str = "Ok";
                    if (firstTimeCheck.firstTimeLogin) {
                        BMLApi.getInstance().setCredentials(LoginActivity.this, null);
                        BMLApi.getInstance().clearSession();
                        builder = new Builder(LoginActivity.this, R.style.AppTheme_DialogStyle);
                        cancelable = builder.setMessage((CharSequence) LoginActivity.this.getString(R.string.firstimelogin)).setCancelable(false);
                        r1 = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BMLApi.getInstance().clearSession();
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.setData(Uri.parse(BMLApi.BML_API_ENDPOINT));
                                LoginActivity.this.startActivity(intent);
                            }
                        };
                    } else if (firstTimeCheck.loginUserIdChangeMandatory) {
                        BMLApi.getInstance().setCredentials(LoginActivity.this, null);
                        BMLApi.getInstance().clearSession();
                        builder = new Builder(LoginActivity.this, R.style.AppTheme_DialogStyle);
                        cancelable = builder.setMessage((CharSequence) LoginActivity.this.getString(R.string.loginchangemandatory)).setCancelable(false);
                        r1 = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BMLApi.getInstance().clearSession();
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.setData(Uri.parse(BMLApi.BML_API_ENDPOINT));
                                LoginActivity.this.startActivity(intent);
                            }
                        };
                    } else if (LoginActivity.this.flag_pincode) {
                        LoginActivity.this.startDashboard();
                        return;
                    } else {
                        LoginActivity loginActivity = LoginActivity.this;
                        a aVar = new a(loginActivity, loginActivity.mUsername, LoginActivity.this.mPassword);
                        Intent intent = new Intent(LoginActivity.this, PincodeCaptureActivity.class);
                        intent.putExtra(PincodeCaptureActivity.CREDENTIALS_BUNDLE, aVar);
                        LoginActivity.this.startActivity(intent);
                        return;
                    }
                    cancelable.setPositiveButton((CharSequence) str, r1);
                    builder.create().show();
                }
            });
            return;
        }
        setErrorText(authResponse.message);
        BMLApi.getInstance().clearSession();
        if (this.flag_pincode) {
            this.mPincodeEntry.clearText();
        }
        toggleWidgetState(true);
        if (authResponse.message.toLowerCase().contains("invalid login") && this.flag_pincode) {
            BMLApi.getInstance().setCredentials(this, null);
            BMLApi.getInstance().clearSession();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(ARG_ERROR_TEXT, "Your password may have changed. Please log in again");
            startActivity(intent);
            finish();
        }
    }
}
