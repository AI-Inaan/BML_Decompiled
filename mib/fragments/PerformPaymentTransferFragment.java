package mv.com.bml.mib.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import mv.com.bml.mib.R;
import mv.com.bml.mib.adapters.CurrencySpinnerAdapter;
import mv.com.bml.mib.adapters.OTPSpinner;
import mv.com.bml.mib.dialogs.ListSelectorDialog;
import mv.com.bml.mib.dialogs.ListSelectorDialog.OnListSelectorSelected;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mib.interfaces.Titleable;
import mv.com.bml.mib.utils.CircleTransform;
import mv.com.bml.mib.utils.PaymentFactory;
import mv.com.bml.mib.widgets.ToolTip;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.accounts.AccountCardList;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.accounts.AccountLoan;
import mv.com.bml.mibapi.models.accounts.AccountSummary;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.Currency;
import mv.com.bml.mibapi.models.payments.MerchantInfo;
import mv.com.bml.mibapi.models.payments.Payable;
import mv.com.bml.mibapi.models.payments.mobile.RechargeDenominations;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PerformPaymentTransferFragment extends Fragment implements OnListSelectorSelected, Titleable {
    protected static final String ARG_PAY_FROM = "param2";
    protected static final String ARG_PAY_TO = "param1";
    protected static final String TAG = "PerformPay";
    protected OTPSpinner benef_inst_adapter;
    protected ArrayList<String> benef_inst_methods;
    @BindView(2131361837)
    Spinner beneficiary_inst_spinner;
    @BindView(2131361838)
    View beneficiary_inst_view;
    @BindView(2131361839)
    View beneficiary_view;
    @BindView(2131361886)
    Spinner currency_spinner;
    protected ProgressDialog dialog;
    @BindView(2131361936)
    View from_account_view;
    @BindView(2131361948)
    View invoice_view;
    protected OnFragmentInteractionListener mListener;
    private RechargeDenominations mRechargeDenom;
    @BindView(2131361972)
    View mobile_recharge_view;
    protected OTPSpinner otp_adapter;
    protected ArrayList<String> otp_methods;
    @BindView(2131361981)
    Spinner otp_spinner;
    @BindView(2131361982)
    View otp_view;
    protected AccountCommon pay_from;
    protected Payable pay_to;
    protected int processing_count = 0;
    @BindView(2131361999)
    View radio_view;
    @BindView(2131362003)
    View remarks_view;
    protected String selectedOTPMethod;
    @BindView(2131362080)
    ToolTip tip;
    @BindView(2131362076)
    View to_account_container;
    @BindView(2131362075)
    View to_account_view;

    private int findIndexInArray(ArrayList<Currency> arrayList, String str) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Currency currency = (Currency) it.next();
            if (currency.code.equalsIgnoreCase(str)) {
                return arrayList.indexOf(currency);
            }
        }
        return 0;
    }

    private void getAccounts() {
        getCASA();
    }

    private String getBeneficiaryInstructions() {
        return ((EditText) ButterKnife.findById(this.beneficiary_inst_view, (int) R.id.edit_benef_instruction)).getText().toString();
    }

    private String getBeneficiaryMethod() {
        if (this.beneficiary_inst_view.getVisibility() == 0) {
            int selectedItemPosition = this.beneficiary_inst_spinner.getSelectedItemPosition();
            if (selectedItemPosition == -1) {
                selectedItemPosition = 0;
            }
            if (((String) this.benef_inst_methods.get(selectedItemPosition)).equalsIgnoreCase("BENEFICIARY")) {
                return "BEN";
            }
            if (((String) this.benef_inst_methods.get(selectedItemPosition)).equalsIgnoreCase("REMITTER")) {
                return "OWN";
            }
            if (((String) this.benef_inst_methods.get(selectedItemPosition)).equalsIgnoreCase("SHARED PARTIES")) {
                return "SHA";
            }
        }
        return null;
    }

    private void getCASA() {
        if (!BMLApi.getInstance().getDataCache().hasValidCacheItem("accounts.account_summary")) {
            updateProcessing(1);
            BMLApi.getClient().getAccountSummary(new Callback<AccountSummary>() {
                public void failure(RetrofitError retrofitError) {
                    try {
                        retrofitError.printStackTrace();
                        PerformPaymentTransferFragment.this.updateProcessing(-1);
                        Toast.makeText(PerformPaymentTransferFragment.this.getActivity(), "Unable to retrieve Accounts info", 1).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void success(AccountSummary accountSummary, Response response) {
                    PerformPaymentTransferFragment.this.updateProcessing(-1);
                    try {
                        BMLApi.getInstance().getDataCache().setCacheItem("accounts.account_summary", accountSummary, false);
                        PerformPaymentTransferFragment.this.getCards();
                        PerformPaymentTransferFragment.this.setFirstDebitable();
                        PerformPaymentTransferFragment.this.populateCurrencyValues();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return;
        }
        getCards();
        setFirstDebitable();
    }

    /* access modifiers changed from: private */
    public void getCards() {
        if (!BMLApi.getInstance().getDataCache().hasValidCacheItem("accounts.card_list")) {
            updateProcessing(1);
            BMLApi.getClient().getCardList(new Callback<AccountCardList>() {
                public void failure(RetrofitError retrofitError) {
                    PerformPaymentTransferFragment.this.updateProcessing(-1);
                    try {
                        retrofitError.printStackTrace();
                        Toast.makeText(PerformPaymentTransferFragment.this.getActivity(), "Unable to retrieve Accounts", 1).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void success(AccountCardList accountCardList, Response response) {
                    PerformPaymentTransferFragment.this.updateProcessing(-1);
                    try {
                        BMLApi.getInstance().getDataCache().setCacheItem("accounts.card_list", accountCardList, false);
                        PerformPaymentTransferFragment.this.populateToAccountView();
                        PerformPaymentTransferFragment.this.populateCurrencyValues();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return;
        }
        populateToAccountView();
    }

    private float getPaymentAmount() {
        if (this.beneficiary_view.getVisibility() == 0) {
            String obj = ((EditText) ButterKnife.findById(this.beneficiary_view, (int) R.id.edit_amount)).getText().toString();
            if (!TextUtils.isEmpty(obj)) {
                return Float.parseFloat(obj);
            }
        } else {
            String charSequence = ((TextView) ButterKnife.findById(this.mobile_recharge_view, (int) R.id.topup_amount)).getText().toString();
            if (!TextUtils.isEmpty(charSequence)) {
                return Float.parseFloat(charSequence);
            }
        }
        return 0.0f;
    }

    private String getRemarks() {
        return ((EditText) ButterKnife.findById(this.remarks_view, (int) R.id.edit_remarks)).getText().toString();
    }

    public static PerformPaymentTransferFragment newInstance(Payable payable, AccountCommon accountCommon) {
        PerformPaymentTransferFragment performPaymentTransferFragment = new PerformPaymentTransferFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("param1", payable);
        bundle.putSerializable("param2", accountCommon);
        performPaymentTransferFragment.setArguments(bundle);
        return performPaymentTransferFragment;
    }

    /* access modifiers changed from: private */
    public void populateCurrencyValues() {
        Currency currency;
        Currency currency2;
        String str;
        ArrayList arrayList = new ArrayList();
        Payable payable = this.pay_to;
        if (payable == null || !(payable instanceof MerchantInfo) || !((MerchantInfo) payable).merchantEnrolType.equals("CREDIT_CARD")) {
            AccountCommon accountCommon = this.pay_from;
            if (accountCommon != null && (accountCommon instanceof AccountCredit)) {
                Payable payable2 = this.pay_to;
                if (payable2 != null && PaymentFactory.isOwnAccount(payable2)) {
                    Payable payable3 = this.pay_to;
                    if (payable3 == null || !(payable3 instanceof MerchantInfo)) {
                        Payable payable4 = this.pay_to;
                        str = (payable4 == null || !(payable4 instanceof BeneficiaryInfo)) ? "" : ((BeneficiaryInfo) payable4).beneficiaryCcy;
                    } else {
                        str = ((MerchantInfo) payable3).currency;
                    }
                    currency2 = new Currency(str);
                    arrayList.add(currency2);
                    setCurrency(arrayList);
                    this.currency_spinner.setEnabled(false);
                    this.currency_spinner.setBackground(new ColorDrawable(0));
                }
            }
            AccountCommon accountCommon2 = this.pay_from;
            if (accountCommon2 != null) {
                currency2 = new Currency(accountCommon2.accountCcy, this.pay_from.accountCcyDesc);
                arrayList.add(currency2);
                setCurrency(arrayList);
                this.currency_spinner.setEnabled(false);
                this.currency_spinner.setBackground(new ColorDrawable(0));
            }
            currency = new Currency("NULL");
        } else {
            currency = new Currency(((MerchantInfo) this.pay_to).currency);
        }
        arrayList.add(currency);
        setCurrency(arrayList);
        this.currency_spinner.setEnabled(false);
        this.currency_spinner.setBackground(new ColorDrawable(0));
    }

    private void populateOTPMethod() {
        this.otp_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                PerformPaymentTransferFragment performPaymentTransferFragment = PerformPaymentTransferFragment.this;
                performPaymentTransferFragment.selectedOTPMethod = (String) performPaymentTransferFragment.otp_methods.get(i);
                PreferenceManager.getDefaultSharedPreferences(PerformPaymentTransferFragment.this.getActivity()).edit().putString("defaultOTPmethod", (String) PerformPaymentTransferFragment.this.otp_methods.get(i)).apply();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                PerformPaymentTransferFragment.this.otp_spinner.setSelection(0);
            }
        });
        String str = "security.otp_channels";
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str)) {
            setOTPMethod((ArrayList) BMLApi.getInstance().getDataCache().getCacheItem(str));
            return;
        }
        updateProcessing(1);
        BMLApi.getClient().getOTPChannels(new Callback<ArrayList<String>>() {
            public void failure(RetrofitError retrofitError) {
                try {
                    Toast.makeText(PerformPaymentTransferFragment.this.getActivity(), "Failed to get OTP channels", 0).show();
                    Log.w(PerformPaymentTransferFragment.TAG, "error retreiving OTPChannels");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PerformPaymentTransferFragment.this.updateProcessing(-1);
            }

            public void success(ArrayList<String> arrayList, Response response) {
                BMLApi.getInstance().getDataCache().setCacheItem("security.otp_channels", arrayList, true);
                if (arrayList != null) {
                    PerformPaymentTransferFragment.this.setOTPMethod(arrayList);
                }
                PerformPaymentTransferFragment.this.updateProcessing(-1);
            }
        });
    }

    private void populateRadioView(AccountCommon accountCommon) {
        final float f;
        final float f2;
        final float f3;
        String str;
        String str2 = "%.2f";
        if (accountCommon instanceof AccountCredit) {
            AccountCredit accountCredit = (AccountCredit) accountCommon;
            float abs = Math.abs(accountCredit.minAmtDue);
            float abs2 = Math.abs(accountCredit.totalAmtDue);
            StringBuilder sb = new StringBuilder();
            sb.append("Minimum Due ");
            sb.append(String.format(str2, new Object[]{Float.valueOf(abs)}));
            str = sb.toString();
            f2 = abs2;
            f3 = abs;
            f = 0.0f;
        } else if (accountCommon instanceof AccountLoan) {
            AccountLoan accountLoan = (AccountLoan) accountCommon;
            float abs3 = Math.abs(accountLoan.overdueAmount);
            float abs4 = Math.abs(accountLoan.outstandingAmount);
            float abs5 = !TextUtils.isEmpty(accountLoan.repaymentAmt) ? Math.abs(Float.valueOf(accountLoan.repaymentAmt).floatValue()) : -1.0f;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Overdue ");
            sb2.append(String.format(str2, new Object[]{Float.valueOf(abs3)}));
            f = abs5;
            f3 = abs3;
            f2 = abs4;
            str = sb2.toString();
        } else {
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Total Due ");
        sb3.append(String.format(str2, new Object[]{Float.valueOf(f2)}));
        String sb4 = sb3.toString();
        RadioGroup radioGroup = (RadioGroup) ButterKnife.findById(this.radio_view, (int) R.id.radio_group);
        final EditText editText = (EditText) ButterKnife.findById(this.beneficiary_view, (int) R.id.edit_amount);
        editText.setText(String.format(str2, new Object[]{Float.valueOf(f3)}));
        editText.setEnabled(false);
        RadioButton radioButton = (RadioButton) ButterKnife.findById(this.radio_view, (int) R.id.radio1);
        radioButton.setSelected(true);
        radioButton.setText(str);
        ((RadioButton) ButterKnife.findById(this.radio_view, (int) R.id.radio2)).setText(sb4);
        RadioButton radioButton2 = (RadioButton) ButterKnife.findById(this.radio_view, (int) R.id.radio4);
        if (f > 0.0f) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Repayment Amount");
            sb5.append(String.format(str2, new Object[]{Float.valueOf(f)}));
            radioButton2.setText(sb5.toString());
            radioButton2.setVisibility(0);
        }
        ((RadioButton) ButterKnife.findById(this.radio_view, (int) R.id.radio3)).setText("Any amount");
        AnonymousClass3 r7 = new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                EditText editText;
                Object[] objArr;
                String str = "%.2f";
                switch (i) {
                    case R.id.radio1 /*2131361994*/:
                        editText = editText;
                        objArr = new Object[]{Float.valueOf(f3)};
                        break;
                    case R.id.radio2 /*2131361995*/:
                        editText = editText;
                        objArr = new Object[]{Float.valueOf(f2)};
                        break;
                    case R.id.radio4 /*2131361997*/:
                        editText = editText;
                        objArr = new Object[]{Float.valueOf(f)};
                        break;
                    default:
                        editText.setEnabled(true);
                        editText.setText("");
                        editText.requestFocusFromTouch();
                        return;
                }
                editText.setText(String.format(str, objArr));
                editText.setEnabled(false);
            }
        };
        radioGroup.setOnCheckedChangeListener(r7);
    }

    private void setCurrency(ArrayList<Currency> arrayList) {
        this.currency_spinner.setAdapter(new CurrencySpinnerAdapter(getActivity(), arrayList));
    }

    /* access modifiers changed from: private */
    public void setFirstDebitable() {
        AccountCommon accountCommon;
        if (this.pay_from == null) {
            String str = "accounts.account_summary";
            if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str)) {
                AccountSummary accountSummary = (AccountSummary) BMLApi.getInstance().getDataCache().getCacheItem(str);
                if (accountSummary.acctSummaryCASAList.size() > 0) {
                    Iterator it = accountSummary.acctSummaryCASAList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        accountCommon = (AccountCommon) it.next();
                        Payable payable = this.pay_to;
                        if (payable != null && (payable instanceof BeneficiaryInfo) && ((BeneficiaryInfo) payable).accountNoCr != null) {
                            if (!((BeneficiaryInfo) this.pay_to).accountNoCr.equals(accountCommon.accountNo)) {
                                break;
                            }
                        } else {
                            this.pay_from = accountCommon;
                            populateFromAccountView();
                            this.tip.showAndVanishWithTime(4000);
                        }
                    }
                    this.pay_from = accountCommon;
                    populateFromAccountView();
                    this.tip.showAndVanishWithTime(4000);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void setOTPMethod(ArrayList<String> arrayList) {
        String string = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("defaultOTPmethod", null);
        if (this.otp_methods == null) {
            this.otp_methods = new ArrayList<>();
        }
        this.otp_methods.clear();
        this.otp_methods.addAll(arrayList);
        OTPSpinner oTPSpinner = this.otp_adapter;
        if (oTPSpinner == null) {
            this.otp_adapter = new OTPSpinner(getActivity(), this.otp_methods);
            this.otp_spinner.setAdapter(this.otp_adapter);
        } else {
            oTPSpinner.notifyDataSetChanged();
        }
        if (string == null || !arrayList.contains(string)) {
            string = (String) arrayList.get(0);
        }
        setOTPMethod(string);
    }

    private void setOTPMethodVisibility() {
        Payable payable = this.pay_to;
        if (payable != null && PaymentFactory.isOwnAccount(payable)) {
            AccountCommon accountCommon = this.pay_from;
            if (accountCommon != null && !(accountCommon instanceof AccountCredit)) {
                this.otp_view.setVisibility(8);
                return;
            }
        }
        this.otp_view.setVisibility(0);
        populateOTPMethod();
    }

    private void setTopupAmount(RechargeDenominations rechargeDenominations) {
        TextView textView = (TextView) ButterKnife.findById(this.mobile_recharge_view, (int) R.id.topup_amount);
        this.mRechargeDenom = rechargeDenominations;
        textView.setText(rechargeDenominations.denomination);
    }

    private void setViewVisibility(boolean z, View... viewArr) {
        for (View visibility : viewArr) {
            visibility.setVisibility(z ? 0 : 8);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void updateProcessing(int i) {
        if (this.dialog == null) {
            this.dialog = new ProgressDialog(getActivity());
            this.dialog.setIndeterminate(true);
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage("Please wait.");
        }
        this.processing_count += i;
        if (this.processing_count < 0) {
            this.processing_count = 0;
        }
        if (this.processing_count == 0 && this.dialog.isShowing()) {
            this.dialog.dismiss();
        } else if (this.processing_count > 0 && !this.dialog.isShowing()) {
            this.dialog.show();
        }
    }

    public String getTitle() {
        Payable payable = this.pay_to;
        return (payable == null || !(payable instanceof MerchantInfo) || !((MerchantInfo) payable).merchantEnrolType.equals("PREPAID_CARD")) ? "Transfer" : "Top Up";
    }

    public void onAccountSelected(AccountCommon accountCommon) {
        this.pay_from = accountCommon;
        populateFromAccountView();
        populateCurrencyValues();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            ((Activity) context).setTitle(getTitle());
            try {
                this.mListener = (OnFragmentInteractionListener) context;
            } catch (ClassCastException unused) {
                StringBuilder sb = new StringBuilder();
                sb.append(context.toString());
                sb.append(" must implement OnFragmentInteractionListener");
                throw new ClassCastException(sb.toString());
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.pay_to = (Payable) getArguments().getSerializable("param1");
            this.pay_from = (AccountCommon) getArguments().getSerializable("param2");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_perform_payment_transfer, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.from_account_view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ListSelectorDialog listSelectorDialog = new ListSelectorDialog();
                listSelectorDialog.setMethod(0);
                listSelectorDialog.setPayTo(PerformPaymentTransferFragment.this.pay_to);
                listSelectorDialog.setListener(PerformPaymentTransferFragment.this);
                listSelectorDialog.show(PerformPaymentTransferFragment.this.getActivity().getSupportFragmentManager(), PerformPaymentTransferFragment.TAG);
                PerformPaymentTransferFragment.this.tip.setVisibility(8);
            }
        });
        this.benef_inst_methods = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.benef_inst_methods)));
        this.benef_inst_adapter = new OTPSpinner(getActivity(), this.benef_inst_methods);
        this.beneficiary_inst_spinner.setAdapter(this.benef_inst_adapter);
        populateFromAccountView();
        populateToAccountView();
        getAccounts();
        setOTPMethodVisibility();
        populateCurrencyValues();
        if (this.pay_from instanceof AccountCredit) {
            Toast makeText = Toast.makeText(getActivity(), getString(R.string.warn_cash_advance), 1);
            makeText.setGravity(48, 0, 70);
            makeText.show();
        }
        return inflate;
    }

    public void onDestroy() {
        ProgressDialog progressDialog = this.dialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.dialog.dismiss();
        }
        super.onDestroy();
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onEditActionSelected(int i) {
    }

    public void onOTPMethodSelected(String str) {
        setOTPMethod(str);
    }

    public void onPayableSelected(Payable payable) {
        this.pay_to = payable;
        getActivity().setTitle(getTitle());
        populateToAccountView();
        populateCurrencyValues();
    }

    public void onRechargeAmountSelected(RechargeDenominations rechargeDenominations) {
        setTopupAmount(rechargeDenominations);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131361864})
    public void performConfirm() {
        FragmentActivity activity;
        String str;
        float abs = Math.abs(getPaymentAmount());
        if (abs <= 0.0f) {
            activity = getActivity();
            str = "Enter Amount";
        } else if (this.selectedOTPMethod == null && !PaymentFactory.isOwnAccount(this.pay_to)) {
            activity = getActivity();
            str = "Set OTP Method";
        } else if (this.pay_from == null) {
            activity = getActivity();
            str = "Select debit account";
        } else {
            Currency currency = (Currency) this.currency_spinner.getSelectedItem();
            String obj = ((EditText) this.invoice_view.findViewById(R.id.invoice_number)).getText().toString();
            OnFragmentInteractionListener onFragmentInteractionListener = this.mListener;
            if (onFragmentInteractionListener != null) {
                RechargeDenominations rechargeDenominations = this.mRechargeDenom;
                onFragmentInteractionListener.onFragmentInteraction(rechargeDenominations == null ? PaymentConfirmFragment.newInstance(this.pay_to, this.pay_from, this.selectedOTPMethod, abs, getRemarks(), currency, obj, getBeneficiaryMethod(), getBeneficiaryInstructions()) : PaymentConfirmFragment.newInstance(this.pay_to, this.pay_from, this.selectedOTPMethod, rechargeDenominations, currency), true);
                return;
            }
            return;
        }
        Toast.makeText(activity, str, 0).show();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x009f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void populateFromAccountView() {
        /*
            r6 = this;
            android.view.View r0 = r6.from_account_view
            r1 = 2131362063(0x7f0a010f, float:1.8343896E38)
            android.view.View r0 = butterknife.ButterKnife.findById(r0, r1)
            android.widget.TextView r0 = (android.widget.TextView) r0
            android.view.View r1 = r6.from_account_view
            r2 = 2131362065(0x7f0a0111, float:1.83439E38)
            android.view.View r1 = butterknife.ButterKnife.findById(r1, r2)
            android.widget.TextView r1 = (android.widget.TextView) r1
            android.view.View r2 = r6.from_account_view
            r3 = 2131362060(0x7f0a010c, float:1.834389E38)
            android.view.View r2 = butterknife.ButterKnife.findById(r2, r3)
            android.widget.TextView r2 = (android.widget.TextView) r2
            android.view.View r3 = r6.from_account_view
            r4 = 2131362062(0x7f0a010e, float:1.8343894E38)
            android.view.View r3 = butterknife.ButterKnife.findById(r3, r4)
            android.widget.TextView r3 = (android.widget.TextView) r3
            mv.com.bml.mibapi.models.accounts.AccountCommon r4 = r6.pay_from
            if (r4 == 0) goto L_0x00b1
            java.lang.String r4 = r4.accountAlias
            if (r4 == 0) goto L_0x0039
            mv.com.bml.mibapi.models.accounts.AccountCommon r4 = r6.pay_from
            java.lang.String r4 = r4.accountAlias
            goto L_0x003d
        L_0x0039:
            mv.com.bml.mibapi.models.accounts.AccountCommon r4 = r6.pay_from
            java.lang.String r4 = r4.accountName
        L_0x003d:
            r0.setText(r4)
            mv.com.bml.mibapi.models.accounts.AccountCommon r0 = r6.pay_from
            java.lang.String r0 = r0.accountNo
            r1.setText(r0)
            mv.com.bml.mibapi.models.accounts.AccountCommon r0 = r6.pay_from
            java.lang.String r0 = r0.accountCcy
            r3.setText(r0)
            mv.com.bml.mibapi.models.accounts.AccountCommon r0 = r6.pay_from
            boolean r1 = r0 instanceof mv.com.bml.mibapi.models.accounts.AccountCASA
            r3 = 0
            if (r1 == 0) goto L_0x007f
            mv.com.bml.mibapi.models.accounts.AccountCASA r0 = (mv.com.bml.mibapi.models.accounts.AccountCASA) r0
            java.lang.String r0 = r0.formattedAvailable
            if (r0 == 0) goto L_0x0063
            mv.com.bml.mibapi.models.accounts.AccountCommon r0 = r6.pay_from
            mv.com.bml.mibapi.models.accounts.AccountCASA r0 = (mv.com.bml.mibapi.models.accounts.AccountCASA) r0
            double r0 = r0.available
            goto L_0x0069
        L_0x0063:
            mv.com.bml.mibapi.models.accounts.AccountCommon r0 = r6.pay_from
            mv.com.bml.mibapi.models.accounts.AccountCASA r0 = (mv.com.bml.mibapi.models.accounts.AccountCASA) r0
            double r0 = r0.availableBalance
        L_0x0069:
            mv.com.bml.mibapi.models.accounts.AccountCommon r5 = r6.pay_from
            mv.com.bml.mibapi.models.accounts.AccountCASA r5 = (mv.com.bml.mibapi.models.accounts.AccountCASA) r5
            java.lang.String r5 = r5.formattedAvailable
            if (r5 == 0) goto L_0x0078
            mv.com.bml.mibapi.models.accounts.AccountCommon r5 = r6.pay_from
            mv.com.bml.mibapi.models.accounts.AccountCASA r5 = (mv.com.bml.mibapi.models.accounts.AccountCASA) r5
            java.lang.String r5 = r5.formattedAvailable
            goto L_0x008e
        L_0x0078:
            mv.com.bml.mibapi.models.accounts.AccountCommon r5 = r6.pay_from
            mv.com.bml.mibapi.models.accounts.AccountCASA r5 = (mv.com.bml.mibapi.models.accounts.AccountCASA) r5
            java.lang.String r5 = r5.formattedAvailableBalance
            goto L_0x008e
        L_0x007f:
            boolean r1 = r0 instanceof mv.com.bml.mibapi.models.accounts.AccountCredit
            if (r1 == 0) goto L_0x0092
            mv.com.bml.mibapi.models.accounts.AccountCredit r0 = (mv.com.bml.mibapi.models.accounts.AccountCredit) r0
            float r0 = r0.availableLimit
            double r0 = (double) r0
            mv.com.bml.mibapi.models.accounts.AccountCommon r5 = r6.pay_from
            mv.com.bml.mibapi.models.accounts.AccountCredit r5 = (mv.com.bml.mibapi.models.accounts.AccountCredit) r5
            java.lang.String r5 = r5.formattedAvailableLimit
        L_0x008e:
            r2.setText(r5)
            goto L_0x0093
        L_0x0092:
            r0 = r3
        L_0x0093:
            int r5 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r5 <= 0) goto L_0x009f
            android.content.res.Resources r0 = r6.getResources()
            r1 = 2131099746(0x7f060062, float:1.7811854E38)
            goto L_0x00a6
        L_0x009f:
            android.content.res.Resources r0 = r6.getResources()
            r1 = 2131099750(0x7f060066, float:1.7811862E38)
        L_0x00a6:
            int r0 = r0.getColor(r1)
            r2.setTextColor(r0)
            r6.setOTPMethodVisibility()
            goto L_0x00b6
        L_0x00b1:
            java.lang.String r1 = "Select Account"
            r0.setText(r1)
        L_0x00b6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: mv.com.bml.mib.fragments.PerformPaymentTransferFragment.populateFromAccountView():void");
    }

    /* access modifiers changed from: protected */
    public void populatePaymentView() {
        Payable payable = this.pay_to;
        if (payable instanceof MerchantInfo) {
            populatePaymentView((MerchantInfo) payable);
        }
        Payable payable2 = this.pay_to;
        if (payable2 instanceof BeneficiaryInfo) {
            populatePaymentView((BeneficiaryInfo) payable2);
        }
    }

    /* access modifiers changed from: protected */
    public void populatePaymentView(BeneficiaryInfo beneficiaryInfo) {
        setViewVisibility(false, this.invoice_view, this.radio_view, this.mobile_recharge_view, this.beneficiary_inst_view);
        setViewVisibility(true, this.beneficiary_view, this.remarks_view);
        if (PaymentFactory.isInternationalAccount(beneficiaryInfo, getResources())) {
            setViewVisibility(true, this.beneficiary_inst_view);
        }
    }

    /* access modifiers changed from: protected */
    public void populatePaymentView(final MerchantInfo merchantInfo) {
        if (merchantInfo.isOwnAccount) {
            String str = "CREDIT_CARD";
            String str2 = "PREPAID_CARD";
            String str3 = "LOANS";
            if (merchantInfo.merchantEnrolType.equals(str) || merchantInfo.merchantEnrolType.equals(str3) || merchantInfo.merchantEnrolType.equals(str2)) {
                setViewVisibility(false, this.invoice_view, this.mobile_recharge_view, this.beneficiary_inst_view);
                setViewVisibility(true, this.beneficiary_view, this.radio_view);
                this.currency_spinner.setEnabled(false);
                this.currency_spinner.setBackground(new ColorDrawable(0));
                if (merchantInfo.merchantEnrolType.equals(str) || merchantInfo.merchantEnrolType.equals(str2)) {
                    String str4 = "accounts.card_list";
                    if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str4)) {
                        AccountCredit cardAccount = ((AccountCardList) BMLApi.getInstance().getDataCache().getCacheItem(str4)).getCardAccount(merchantInfo.accountNumber);
                        if (cardAccount != null) {
                            if (!cardAccount.isPrepaid()) {
                                populateRadioView(cardAccount);
                            } else {
                                setViewVisibility(false, this.radio_view);
                            }
                            return;
                        }
                    }
                } else if (merchantInfo.merchantEnrolType.equals(str3)) {
                    String str5 = "accounts.account_summary";
                    if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str5) && ((AccountSummary) BMLApi.getInstance().getDataCache().getCacheItem(str5)).getLoanAccount(merchantInfo.accountNumber) != null) {
                        setViewVisibility(false, this.radio_view);
                        return;
                    }
                }
            }
        } else if (merchantInfo.merchantEnrolType.equals("MOBILE_RECHARGE")) {
            setViewVisibility(false, this.invoice_view, this.beneficiary_view, this.radio_view, this.beneficiary_inst_view);
            setViewVisibility(true, this.mobile_recharge_view, this.remarks_view);
            ((TextView) ButterKnife.findById(this.mobile_recharge_view, (int) R.id.topup_amount)).setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ListSelectorDialog listSelectorDialog = new ListSelectorDialog();
                    listSelectorDialog.setMethod(2);
                    listSelectorDialog.setMobileMerchant(String.valueOf(merchantInfo.merchantEnrolId));
                    listSelectorDialog.setListener(PerformPaymentTransferFragment.this);
                    listSelectorDialog.show(PerformPaymentTransferFragment.this.getActivity().getSupportFragmentManager(), PerformPaymentTransferFragment.TAG);
                }
            });
            return;
        }
        setViewVisibility(false, this.radio_view, this.mobile_recharge_view, this.beneficiary_inst_view);
        setViewVisibility(true, this.beneficiary_view, this.invoice_view);
    }

    /* access modifiers changed from: protected */
    public void populateToAccountView() {
        TextView textView = (TextView) ButterKnife.findById(this.to_account_view, (int) R.id.text_account_name);
        TextView textView2 = (TextView) ButterKnife.findById(this.to_account_view, (int) R.id.text_account_num);
        ImageView imageView = (ImageView) ButterKnife.findById(this.to_account_view, (int) R.id.bene_image);
        Payable payable = this.pay_to;
        String str = "";
        if (payable != null) {
            if (payable instanceof BeneficiaryInfo) {
                textView.setText(((BeneficiaryInfo) payable).beneficiaryAlias);
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
                textView2.setText(sb.toString());
            }
            Payable payable2 = this.pay_to;
            if (payable2 instanceof MerchantInfo) {
                textView.setText(((MerchantInfo) payable2).merchantEnrolAlias);
                textView2.setText(((MerchantInfo) this.pay_to).accountNumber);
                if (TextUtils.isEmpty(((MerchantInfo) this.pay_to).accountNumber)) {
                    textView2.setVisibility(8);
                }
            }
            if (this.pay_to.imageUrl != null) {
                String str2 = "/";
                if (!this.pay_to.imageUrl.startsWith(str2)) {
                    Payable payable3 = this.pay_to;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str2);
                    sb3.append(this.pay_to.imageUrl);
                    payable3.imageUrl = sb3.toString();
                }
            }
            Picasso authedPicasso = BMLApi.getInstance().getAuthedPicasso(getActivity());
            StringBuilder sb4 = new StringBuilder();
            sb4.append(BMLApi.BML_API_ENDPOINT);
            sb4.append(this.pay_to.imageUrl);
            authedPicasso.load(sb4.toString()).placeholder((int) R.drawable.ic_person_round).error((int) R.drawable.ic_person_round).transform((Transformation) new CircleTransform()).fit().centerInside().into(imageView);
            setOTPMethodVisibility();
        } else {
            textView.setText("Select Payee");
            textView2.setText(str);
        }
        this.to_account_container.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (PerformPaymentTransferFragment.this.mListener != null) {
                    PerformPaymentTransferFragment.this.mListener.onFragmentInteraction(BeneficiaryListFragment.newInstance(PerformPaymentTransferFragment.this.pay_from), false);
                }
            }
        });
        populatePaymentView();
    }

    /* access modifiers changed from: protected */
    public void setOTPMethod(String str) {
        if (str == null) {
            this.otp_spinner.setSelection(0);
            this.selectedOTPMethod = (String) this.otp_methods.get(0);
        } else {
            this.otp_spinner.setSelection(this.otp_methods.indexOf(str));
            this.selectedOTPMethod = str;
        }
        try {
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("defaultOTPmethod", str).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
