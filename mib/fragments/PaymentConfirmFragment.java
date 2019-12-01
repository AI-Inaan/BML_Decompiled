package mv.com.bml.mib.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.otto.Subscribe;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import mv.com.bml.mib.R;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mib.interfaces.Titleable;
import mv.com.bml.mib.utils.MiscUtils;
import mv.com.bml.mib.utils.PaymentFactory;
import mv.com.bml.mib.utils.PaymentFactory.Builder;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.OTPSMSReceiver;
import mv.com.bml.mibapi.apievents.GenericError;
import mv.com.bml.mibapi.apievents.OTPRequestSent;
import mv.com.bml.mibapi.apievents.OTPSMSReceived;
import mv.com.bml.mibapi.apievents.PaymentTransferComplete;
import mv.com.bml.mibapi.models.accounts.AccountCASA;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.Currency;
import mv.com.bml.mibapi.models.payments.MerchantInfo;
import mv.com.bml.mibapi.models.payments.Payable;
import mv.com.bml.mibapi.models.payments.Payment;
import mv.com.bml.mibapi.models.payments.PaymentActivity;
import mv.com.bml.mibapi.models.payments.Transfer;
import mv.com.bml.mibapi.models.payments.TransferActivity;
import mv.com.bml.mibapi.models.payments.mobile.RechargeDenominations;

public class PaymentConfirmFragment extends Fragment implements Titleable {
    protected static final String ARG_BENEF_INST = "param10";
    protected static final String ARG_BENEF_METHOD = "param9";
    protected static final String ARG_CURRENCY = "param7";
    protected static final String ARG_INVOICENO = "param8";
    protected static final String ARG_OTP_METHOD = "param3";
    protected static final String ARG_PAY_AMNT = "param4";
    protected static final String ARG_PAY_FROM = "param2";
    protected static final String ARG_PAY_RCHG = "param5";
    protected static final String ARG_PAY_TO = "param1";
    protected static final String ARG_REMARKS = "param6";
    protected static final String TAG = "PerformPay";
    protected float amount;
    protected int auth_fail_count = 0;
    protected String benef_inst;
    protected String benef_method;
    @BindView(2131361864)
    Button btn_confirm;
    protected Currency currency;
    @BindView(2131361936)
    View from_account_view;
    protected String invoice_no;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog mdialog;
    protected String otp_method;
    @BindView(2131361982)
    EditText otp_view;
    protected AccountCommon pay_from;
    protected Payable pay_to;
    protected RechargeDenominations recharge;
    protected String remarks;
    @BindView(2131362066)
    TextView text_date;
    @BindView(2131362045)
    TextView text_status;
    @BindView(2131362075)
    View to_account_view;

    /* access modifiers changed from: private */
    public void commitTransaction() {
        if (PaymentFactory.isOwnAccount(this.pay_to)) {
            Log.i(TAG, "Own account transfer");
        } else if (TextUtils.isEmpty(this.otp_view.getText().toString())) {
            Toast.makeText(getActivity(), "Enter OTP", 0).show();
            return;
        }
        this.mdialog.show();
        Builder with_currency = new Builder().from(this.pay_from).to(this.pay_to).with_remarks(this.remarks).with_benef_method(this.benef_method).with_benef_instructions(this.benef_inst).with_invoice_no(this.invoice_no).with_otp_method(this.otp_method).with_otp(this.otp_view.getText().toString()).with_currency(this.currency);
        RechargeDenominations rechargeDenominations = this.recharge;
        if (rechargeDenominations != null) {
            with_currency.with_demonimation(rechargeDenominations);
        } else {
            with_currency.amount(this.amount);
        }
        this.btn_confirm.setEnabled(false);
        with_currency.build().execute();
    }

    private void handleTransferResponse(Payment payment) {
        try {
            PaymentActivity paymentActivity = (PaymentActivity) payment.paymentActivityList.get(0);
            this.mListener.onFragmentInteraction(TransactionReceiptFragment.newInstance(payment, this.pay_from, this.pay_to), false);
        } catch (Exception e) {
            setStatus("Unknown error", true);
            e.printStackTrace();
        }
    }

    private void handleTransferResponse(Transfer transfer) {
        try {
            TransferActivity transferActivity = (TransferActivity) transfer.transferActivityList.get(0);
            this.mListener.onFragmentInteraction(TransactionReceiptFragment.newInstance(transfer, this.pay_from, this.pay_to), false);
        } catch (Exception e) {
            setStatus("Unknown error", true);
            e.printStackTrace();
        }
    }

    public static PaymentConfirmFragment newInstance(Payable payable, AccountCommon accountCommon, String str, float f, String str2, Currency currency2, String str3, String str4, String str5) {
        PaymentConfirmFragment paymentConfirmFragment = new PaymentConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("param1", payable);
        bundle.putSerializable("param2", accountCommon);
        bundle.putString(ARG_OTP_METHOD, str);
        bundle.putFloat(ARG_PAY_AMNT, f);
        bundle.putString(ARG_REMARKS, str2);
        bundle.putString(ARG_INVOICENO, str3);
        bundle.putSerializable(ARG_CURRENCY, currency2);
        bundle.putString(ARG_BENEF_METHOD, str4);
        bundle.putString(ARG_BENEF_INST, str5);
        paymentConfirmFragment.setArguments(bundle);
        Log.d("FACTORY", bundle.toString());
        return paymentConfirmFragment;
    }

    public static PaymentConfirmFragment newInstance(Payable payable, AccountCommon accountCommon, String str, RechargeDenominations rechargeDenominations, Currency currency2) {
        PaymentConfirmFragment paymentConfirmFragment = new PaymentConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("param1", payable);
        bundle.putSerializable("param2", accountCommon);
        bundle.putString(ARG_OTP_METHOD, str);
        bundle.putSerializable(ARG_PAY_RCHG, rechargeDenominations);
        bundle.putSerializable(ARG_CURRENCY, currency2);
        paymentConfirmFragment.setArguments(bundle);
        Log.d("FACTORY", bundle.toString());
        return paymentConfirmFragment;
    }

    private void populateFromAccountView() {
        TextView textView = (TextView) ButterKnife.findById(this.from_account_view, (int) R.id.text_account_name);
        TextView textView2 = (TextView) ButterKnife.findById(this.from_account_view, (int) R.id.text_account_num);
        TextView textView3 = (TextView) ButterKnife.findById(this.from_account_view, (int) R.id.text_account_ccy);
        TextView textView4 = (TextView) ButterKnife.findById(this.from_account_view, (int) R.id.text_account_amount);
        View findById = ButterKnife.findById(this.from_account_view, (int) R.id.text_account_name_secondary);
        textView.setText(this.pay_from.accountAlias != null ? this.pay_from.accountAlias : this.pay_from.accountName);
        StringBuilder sb = new StringBuilder();
        sb.append(this.pay_from.accountNo);
        sb.append(" (");
        sb.append(this.pay_from.accountCcy);
        sb.append(")");
        textView2.setText(sb.toString());
        findById.setVisibility(0);
        textView3.setText(this.pay_from.accountCcy);
        textView3.setVisibility(8);
        double d = 0.0d;
        RechargeDenominations rechargeDenominations = this.recharge;
        if (rechargeDenominations != null) {
            this.amount = Float.parseFloat(rechargeDenominations.totalAmount);
        }
        AccountCommon accountCommon = this.pay_from;
        if (accountCommon instanceof AccountCASA) {
            d = ((AccountCASA) accountCommon).formattedAvailable != null ? ((AccountCASA) this.pay_from).available : ((AccountCASA) this.pay_from).availableBalance;
        } else if (accountCommon instanceof AccountCredit) {
            d = (double) ((AccountCredit) accountCommon).availableLimit;
        }
        float f = this.amount;
        Currency currency2 = this.currency;
        if (currency2 != null && !currency2.code.equalsIgnoreCase(this.pay_from.accountCcy)) {
            String str = "mvr";
            String str2 = "usd";
            if (this.currency.code.equalsIgnoreCase(str) && this.pay_from.accountCcy.equalsIgnoreCase(str2)) {
                f = this.amount / 15.21f;
            } else if (this.currency.code.equalsIgnoreCase(str2) && this.pay_from.accountCcy.equalsIgnoreCase(str)) {
                f = this.amount * 15.21f;
            }
        }
        double d2 = (double) f;
        Double.isNaN(d2);
        textView4.setText(MiscUtils.formatCurrency(d - d2));
        textView4.setTextColor(getResources().getColor(R.color.primary_grey));
        textView3.setTextColor(getResources().getColor(R.color.primary_grey));
    }

    private void populateToAccountView() {
        TextView textView = (TextView) ButterKnife.findById(this.to_account_view, (int) R.id.text_account_ccy);
        TextView textView2 = (TextView) ButterKnife.findById(this.to_account_view, (int) R.id.text_account_name);
        TextView textView3 = (TextView) ButterKnife.findById(this.to_account_view, (int) R.id.text_account_num);
        TextView textView4 = (TextView) ButterKnife.findById(this.to_account_view, (int) R.id.text_account_amount);
        RechargeDenominations rechargeDenominations = this.recharge;
        if (rechargeDenominations != null) {
            this.amount = Float.parseFloat(rechargeDenominations.totalAmount);
        }
        textView4.setText(MiscUtils.formatCurrency((double) this.amount));
        Currency currency2 = this.currency;
        if (currency2 != null) {
            textView.setText(currency2.code);
        } else {
            textView.setVisibility(8);
        }
        Payable payable = this.pay_to;
        String str = "";
        if (payable instanceof MerchantInfo) {
            textView2.setText(((MerchantInfo) payable).merchantEnrolAlias.toUpperCase());
            str = ((MerchantInfo) this.pay_to).accountNumber;
        } else if (payable instanceof BeneficiaryInfo) {
            textView2.setText(((BeneficiaryInfo) payable).beneficiaryAlias.toUpperCase());
            StringBuilder sb = new StringBuilder();
            sb.append(((BeneficiaryInfo) this.pay_to).accountNoCr);
            if (!TextUtils.isEmpty(((BeneficiaryInfo) this.pay_to).beneficiaryCcy)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" (");
                sb2.append(((BeneficiaryInfo) this.pay_to).beneficiaryCcy);
                sb2.append(")");
                str = sb2.toString();
            }
            sb.append(str);
            str = sb.toString();
        }
        textView3.setText(str);
        if (TextUtils.isEmpty(str)) {
            textView3.setVisibility(8);
        }
        textView4.setTextColor(getResources().getColor(R.color.primary_grey));
        textView.setTextColor(getResources().getColor(R.color.primary_grey));
    }

    private void sendOTPRequest() {
        this.mdialog.show();
        Builder with_currency = new Builder().from(this.pay_from).to(this.pay_to).with_remarks(this.remarks).with_benef_method(this.benef_method).with_benef_instructions(this.benef_inst).with_otp_method(this.otp_method).with_currency(this.currency);
        RechargeDenominations rechargeDenominations = this.recharge;
        if (rechargeDenominations != null) {
            with_currency.with_demonimation(rechargeDenominations);
        } else {
            with_currency.amount(this.amount);
        }
        with_currency.build().execute();
        OTPSMSReceiver.subscribe(getContext());
    }

    private void setDate() {
        this.text_date.setText(new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.UK).format(new Date()));
    }

    private void setStatus(String str, boolean z) {
        TextView textView;
        int i;
        Resources resources;
        int i2;
        TextView textView2;
        if (z) {
            textView = this.text_status;
            resources = getResources();
            i = R.color.primary_red;
        } else {
            textView = this.text_status;
            resources = getResources();
            i = R.color.primary_grey;
        }
        textView.setTextColor(resources.getColor(i));
        this.text_status.setText(str);
        if (TextUtils.isEmpty(str)) {
            textView2 = this.text_status;
            i2 = 8;
        } else {
            textView2 = this.text_status;
            i2 = 0;
        }
        textView2.setVisibility(i2);
    }

    @Subscribe
    public void failure(GenericError genericError) {
        this.mdialog.dismiss();
        setStatus(genericError.err_msg, true);
        this.btn_confirm.setEnabled(true);
    }

    public String getTitle() {
        return "Review";
    }

    @Subscribe
    public void handleOTPResponse(OTPRequestSent oTPRequestSent) {
        this.mdialog.dismiss();
        if (oTPRequestSent.response.result.contentEquals("MUST_SELECT_OTP_CHANNEL")) {
            setStatus("Invalid OTP Channel. Please go back.", true);
        }
        if (oTPRequestSent.response.result.contentEquals("GENERATED")) {
            setStatus((String) oTPRequestSent.response.supplementaryData.get("Generation-Msg"), false);
        }
        if (oTPRequestSent.response.result.contentEquals("OTP_VALUE_ALREADY_EXPIRED")) {
            setStatus("The Verification code already expired or is not valid. Please go back.", true);
            this.btn_confirm.setEnabled(false);
        }
        if (oTPRequestSent.response.result.contentEquals("AUTH_FAILED")) {
            StringBuilder sb = new StringBuilder();
            sb.append("The Verification code is not valid.");
            sb.append(this.auth_fail_count > 0 ? "Warning: Another incorrect attempt will block your account" : "");
            setStatus(sb.toString(), true);
            this.auth_fail_count++;
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            try {
                this.mListener = (OnFragmentInteractionListener) activity;
            } catch (ClassCastException unused) {
                StringBuilder sb = new StringBuilder();
                sb.append(activity.toString());
                sb.append(" must implement OnFragmentInteractionListener");
                throw new ClassCastException(sb.toString());
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        if (getArguments() != null) {
            this.pay_to = (Payable) getArguments().getSerializable("param1");
            this.pay_from = (AccountCommon) getArguments().getSerializable("param2");
            this.otp_method = getArguments().getString(ARG_OTP_METHOD);
            this.amount = getArguments().getFloat(ARG_PAY_AMNT);
            this.recharge = (RechargeDenominations) getArguments().getSerializable(ARG_PAY_RCHG);
            String str = "";
            this.remarks = getArguments().getString(ARG_REMARKS, str);
            this.invoice_no = getArguments().getString(ARG_INVOICENO, str);
            this.currency = (Currency) getArguments().getSerializable(ARG_CURRENCY);
            this.benef_method = getArguments().getString(ARG_BENEF_METHOD, str);
            this.benef_inst = getArguments().getString(ARG_BENEF_INST, str);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_payment_confirm, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.mdialog = new ProgressDialog(getActivity());
        this.mdialog.setMessage("Please wait");
        this.mdialog.setIndeterminate(true);
        this.mdialog.setCanceledOnTouchOutside(false);
        this.mdialog.setCancelable(false);
        this.btn_confirm.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PaymentConfirmFragment.this.commitTransaction();
            }
        });
        populateFromAccountView();
        populateToAccountView();
        setDate();
        if (!PaymentFactory.isOwnAccount(this.pay_to) || (this.pay_from instanceof AccountCredit)) {
            sendOTPRequest();
        } else {
            this.otp_view.setVisibility(8);
            setStatus(null, false);
        }
        return inflate;
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @Subscribe
    public void onOTPReceived(OTPSMSReceived oTPSMSReceived) {
        Log.d("OTP", "OTP RECEIVED INSIDE PAYMENT FRAGMENT");
        this.otp_view.setText(oTPSMSReceived.getOtpcode());
        setStatus(null, false);
    }

    public void onPause() {
        super.onPause();
        BMLApi.getEventBus().unregister(this);
        ProgressDialog progressDialog = this.mdialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mdialog.dismiss();
        }
    }

    public void onResume() {
        super.onResume();
        BMLApi.getEventBus().register(this);
    }

    @Subscribe
    public void success(PaymentTransferComplete paymentTransferComplete) {
        this.mdialog.dismiss();
        if (paymentTransferComplete.transfer instanceof Transfer) {
            handleTransferResponse((Transfer) paymentTransferComplete.transfer);
        } else if (paymentTransferComplete.transfer instanceof Payment) {
            handleTransferResponse((Payment) paymentTransferComplete.transfer);
        } else {
            setStatus("unknown response", true);
        }
        this.btn_confirm.setEnabled(true);
    }
}
