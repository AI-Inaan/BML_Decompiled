package mv.com.bml.mib.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import mv.com.bml.mib.R;
import mv.com.bml.mib.activities.DialDashboardActivity;
import mv.com.bml.mib.activities.PincodeCaptureActivity;
import mv.com.bml.mib.widgets.PincodeEntry;
import mv.com.bml.mib.widgets.PincodeEntry.OnPincodeEntryListener;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.auth.a;

public class PincodeCaptureFragment extends DialogFragment implements OnPincodeEntryListener {
    public static final String ACTION_ARG = "mv.com.bml.loginpincodeaction";
    public static final String ARG_SKIP_FP = "skip_fp";
    private a mCredentialsContainer;
    private String mPin;
    private PincodeEntry mPincodeEntry;
    private String mTitle;
    private boolean skip_fp = false;

    public static PincodeCaptureFragment newInstance() {
        return new PincodeCaptureFragment();
    }

    public static PincodeCaptureFragment newInstance(a aVar) {
        return newInstance(aVar, false);
    }

    public static PincodeCaptureFragment newInstance(a aVar, boolean z) {
        PincodeCaptureFragment newInstance = newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PincodeCaptureActivity.CREDENTIALS_BUNDLE, aVar);
        bundle.putBoolean(ARG_SKIP_FP, z);
        newInstance.setArguments(bundle);
        return newInstance;
    }

    private void setTitle() {
        String str = this.mTitle;
        if (str == null) {
            str = getString(R.string.loginheader_enter_pincode_short);
        }
        setTitle(str);
    }

    private void setTitle(String str) {
        this.mPincodeEntry.setStatus(str);
    }

    /* access modifiers changed from: private */
    public void startDashboard() {
        Intent intent = new Intent(getActivity(), DialDashboardActivity.class);
        intent.setFlags(67108864);
        startActivity(intent);
    }

    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_pincode_capture, viewGroup, false);
        this.mCredentialsContainer = (a) getArguments().getSerializable(PincodeCaptureActivity.CREDENTIALS_BUNDLE);
        this.skip_fp = getArguments().getBoolean(ARG_SKIP_FP);
        if (this.mCredentialsContainer == null) {
            BMLApi.getInstance().getCredentials(getActivity());
        }
        this.mPincodeEntry = (PincodeEntry) inflate.findViewById(R.id.pin_entry_simple);
        this.mPincodeEntry.setPincodeEntryListener(this);
        this.mPincodeEntry.setStatus(getString(R.string.loginheader_set_pincode));
        setTitle();
        return inflate;
    }

    public void onPincodeEntryChanged() {
        setTitle();
        this.mPincodeEntry.setSubStatus(null);
    }

    public void onPincodeEntryComplete(int i) {
        String pincode = this.mPincodeEntry.getPincode();
        String str = this.mPin;
        if (str == null) {
            this.mPin = pincode;
            this.mPincodeEntry.clearText();
            this.mTitle = getString(R.string.loginheader_set_pincode_confirm_short);
            setTitle();
        } else if (str.equals(pincode)) {
            this.mCredentialsContainer.a(pincode);
            BMLApi.getInstance().setCredentials(getActivity(), this.mCredentialsContainer);
            FingerprintManagerCompat from = FingerprintManagerCompat.from(getContext());
            if (!from.isHardwareDetected() || this.skip_fp) {
                startDashboard();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.fp_prompt));
            if (!from.hasEnrolledFingerprints()) {
                sb.append("\n\n");
                sb.append(getString(R.string.fp_prompt_enroll));
            }
            new Builder(getContext(), R.style.AppTheme_DialogStyle).setCancelable(false).setTitle((int) R.string.fp_prompt_title).setMessage((CharSequence) sb.toString()).setPositiveButton((int) R.string.fp_use, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    PreferenceManager.getDefaultSharedPreferences(PincodeCaptureFragment.this.getContext()).edit().putBoolean("fingerprint_auth", true).apply();
                    dialogInterface.dismiss();
                    PincodeCaptureFragment.this.startDashboard();
                }
            }).setNegativeButton((int) R.string.fp_dont_use, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    PreferenceManager.getDefaultSharedPreferences(PincodeCaptureFragment.this.getContext()).edit().putBoolean("fingerprint_auth", false).apply();
                    dialogInterface.dismiss();
                    PincodeCaptureFragment.this.startDashboard();
                }
            }).show();
        } else {
            this.mPin = null;
            this.mPincodeEntry.clearText(true);
            this.mTitle = null;
            setTitle(getString(R.string.loginheader_set_pincode_mismatch));
        }
    }

    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setDimAmount(0.9f);
        }
    }
}
