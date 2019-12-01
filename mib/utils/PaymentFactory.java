package mv.com.bml.mib.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import mv.com.bml.mib.R;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.BMLRestClient;
import mv.com.bml.mibapi.apievents.GenericError;
import mv.com.bml.mibapi.apievents.OTPRequestSent;
import mv.com.bml.mibapi.apievents.PaymentTransferComplete;
import mv.com.bml.mibapi.models.accounts.AccountCASA;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.Currency;
import mv.com.bml.mibapi.models.payments.MerchantInfo;
import mv.com.bml.mibapi.models.payments.OTPResponse;
import mv.com.bml.mibapi.models.payments.Payable;
import mv.com.bml.mibapi.models.payments.Payment;
import mv.com.bml.mibapi.models.payments.PaymentActivity;
import mv.com.bml.mibapi.models.payments.Transfer;
import mv.com.bml.mibapi.models.payments.mobile.RechargeDenominations;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class PaymentFactory {
    static String[] domestics;
    /* access modifiers changed from: private */
    public String OTP;
    /* access modifiers changed from: private */
    public String OTP_METHOD;
    /* access modifiers changed from: private */
    public float amount;
    /* access modifiers changed from: private */
    public String benef_info;
    /* access modifiers changed from: private */
    public String benef_method;
    /* access modifiers changed from: private */
    public Currency currency;
    /* access modifiers changed from: private */
    public AccountCommon from_account;
    /* access modifiers changed from: private */
    public String invoice_no;
    /* access modifiers changed from: private */
    public RechargeDenominations recharged;
    /* access modifiers changed from: private */
    public String remarks;
    /* access modifiers changed from: private */
    public Payable to_account;

    public static class Builder {
        PaymentFactory _instance = new PaymentFactory();

        public Builder amount(float f) {
            this._instance.amount = f;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(f);
            Log.d("FACTORY AMOUNT", sb.toString());
            return this;
        }

        public PaymentFactory build() {
            if (this._instance.to_account != null && this._instance.from_account != null && ((PaymentFactory.isOwnAccount(this._instance.to_account) || this._instance.OTP_METHOD != null) && (this._instance.amount > 0.0f || this._instance.recharged != null))) {
                return this._instance;
            }
            throw new IllegalArgumentException("You need to specifiy all parameters");
        }

        public Builder from(AccountCommon accountCommon) {
            this._instance.from_account = accountCommon;
            Log.d("FACTORY FROM", accountCommon.toString());
            return this;
        }

        public Builder to(Payable payable) {
            Log.d("FACTORY TO", payable.toString());
            this._instance.to_account = payable;
            return this;
        }

        public Builder with_benef_instructions(String str) {
            this._instance.benef_info = str;
            return this;
        }

        public Builder with_benef_method(String str) {
            this._instance.benef_method = str;
            return this;
        }

        public Builder with_currency(Currency currency) {
            this._instance.currency = currency;
            return this;
        }

        public Builder with_demonimation(RechargeDenominations rechargeDenominations) {
            this._instance.recharged = rechargeDenominations;
            Log.d("FACTORY RECHARGE", rechargeDenominations.toString());
            return this;
        }

        public Builder with_invoice_no(String str) {
            this._instance.invoice_no = str;
            return this;
        }

        public Builder with_otp(String str) {
            this._instance.OTP = str;
            return this;
        }

        public Builder with_otp_method(String str) {
            this._instance.OTP_METHOD = str;
            return this;
        }

        public Builder with_remarks(String str) {
            this._instance.remarks = str;
            return this;
        }
    }

    public PaymentFactory() {
        BMLApi.getEventBus().register(this);
    }

    /* access modifiers changed from: private */
    public void checkOTPFail(Response response) {
    }

    private void executePayment() {
        String str;
        Payment payment = new Payment();
        Double valueOf = Double.valueOf(((AccountCASA) this.from_account).available);
        Boolean valueOf2 = Boolean.valueOf(false);
        payment.availableBalance = String.format("%.2f", new Object[]{valueOf});
        payment.drawnFromAccount = this.from_account.accountId;
        payment.formattedAvailableBalance = ((AccountCASA) this.from_account).formattedAvailable;
        payment.availableBalanceCcy = this.from_account.accountCcy;
        String str2 = "QUERY";
        payment.crudOperation = str2;
        String str3 = "SUCCESS";
        payment.status = str3;
        PaymentActivity paymentActivity = (PaymentActivity) payment.paymentActivityList.get(0);
        MerchantInfo merchantInfo = (MerchantInfo) this.to_account;
        RechargeDenominations rechargeDenominations = this.recharged;
        paymentActivity.gstAmount = rechargeDenominations != null ? rechargeDenominations.gstAmount : null;
        RechargeDenominations rechargeDenominations2 = this.recharged;
        paymentActivity.amount = rechargeDenominations2 != null ? rechargeDenominations2.denomination : String.valueOf(this.amount);
        Currency currency2 = this.currency;
        paymentActivity.tranCcy = currency2 != null ? currency2.code : this.from_account.accountCcy;
        RechargeDenominations rechargeDenominations3 = this.recharged;
        paymentActivity.tranAmt = rechargeDenominations3 != null ? rechargeDenominations3.denomination : String.valueOf(this.amount);
        paymentActivity.cardNumber = merchantInfo.accountNumber;
        paymentActivity.merchantType = merchantInfo.merchantEnrolType;
        paymentActivity.beneficiaryId = String.valueOf(merchantInfo.merchantEnrolId);
        paymentActivity.accountNoCr = String.valueOf(merchantInfo.merchantEnrolId);
        paymentActivity.ccyAccountCr = merchantInfo.currency;
        paymentActivity.activityId = "activity1";
        String str4 = this.remarks;
        String str5 = "";
        if (str4 == null) {
            str4 = str5;
        }
        paymentActivity.remarks = str4;
        String str6 = this.invoice_no;
        if (str6 == null) {
            str6 = str5;
        }
        paymentActivity.invoiceNo = str6;
        paymentActivity.accountNameCr = merchantInfo.merchantEnrolAlias;
        paymentActivity.isFutureDatedTxn = valueOf2;
        paymentActivity.isRecurringTxn = valueOf2;
        paymentActivity.isBulkTransaction = valueOf2;
        paymentActivity.accountNoDr = this.from_account.accountId;
        paymentActivity.accountNameDr = this.from_account.accountName;
        paymentActivity.ccyAccountDr = this.from_account.accountCcy;
        paymentActivity.branch = this.from_account.branch;
        paymentActivity.crudOperation = str2;
        paymentActivity.status = str3;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Indian/Maldives"));
        String str7 = TextUtils.isEmpty(((MerchantInfo) this.to_account).merchantEnrolType) ? "Own" : ((MerchantInfo) this.to_account).merchantEnrolType;
        if (str7.equals("CREDIT_CARD") || str7.equals("PREPAID_CARD")) {
            str = "CPA";
        } else if (str7.equals("LOANS")) {
            str = "ALP";
        } else {
            String str8 = "BPR";
            if (str7.equals(str8)) {
                paymentActivity.crTranType = str8;
            }
            paymentActivity.effectDt = simpleDateFormat.format(new Date());
            paymentActivity.tranDt = simpleDateFormat.format(new Date());
            StringBuilder sb = new StringBuilder();
            sb.append(paymentActivity.accountNoDr);
            sb.append(paymentActivity.tranCcy);
            sb.append(paymentActivity.tranAmt);
            sb.append(paymentActivity.effectDt);
            sb.append(paymentActivity.beneficiaryId);
            sb.append(paymentActivity.merchantType);
            paymentActivity.hash = generateHash(sb.toString());
            makeRequest(payment);
        }
        paymentActivity.crTranType = str;
        paymentActivity.effectDt = simpleDateFormat.format(new Date());
        paymentActivity.tranDt = simpleDateFormat.format(new Date());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(paymentActivity.accountNoDr);
        sb2.append(paymentActivity.tranCcy);
        sb2.append(paymentActivity.tranAmt);
        sb2.append(paymentActivity.effectDt);
        sb2.append(paymentActivity.beneficiaryId);
        sb2.append(paymentActivity.merchantType);
        paymentActivity.hash = generateHash(sb2.toString());
        makeRequest(payment);
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0158  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x015b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void executeTransferOp() {
        /*
            r7 = this;
            mv.com.bml.mibapi.models.payments.Transfer r0 = new mv.com.bml.mibapi.models.payments.Transfer
            r0.<init>()
            mv.com.bml.mibapi.models.accounts.AccountCommon r1 = r7.from_account
            java.lang.String r1 = r1.accountId
            r0.drawnFromAccount = r1
            mv.com.bml.mibapi.models.accounts.AccountCommon r1 = r7.from_account
            boolean r2 = r1 instanceof mv.com.bml.mibapi.models.accounts.AccountCASA
            if (r2 == 0) goto L_0x001f
            mv.com.bml.mibapi.models.accounts.AccountCASA r1 = (mv.com.bml.mibapi.models.accounts.AccountCASA) r1
            double r1 = r1.availableBalance
            r0.availableBalance = r1
            mv.com.bml.mibapi.models.accounts.AccountCommon r1 = r7.from_account
            mv.com.bml.mibapi.models.accounts.AccountCASA r1 = (mv.com.bml.mibapi.models.accounts.AccountCASA) r1
            java.lang.String r1 = r1.formattedAvailableBalance
            r0.formattedAvailableBalance = r1
        L_0x001f:
            mv.com.bml.mibapi.models.accounts.AccountCommon r1 = r7.from_account
            java.lang.String r1 = r1.accountCcy
            r0.availableBalanceCcy = r1
            java.util.ArrayList<mv.com.bml.mibapi.models.payments.TransferActivity> r1 = r0.transferActivityList
            r2 = 0
            java.lang.Object r1 = r1.get(r2)
            mv.com.bml.mibapi.models.payments.TransferActivity r1 = (mv.com.bml.mibapi.models.payments.TransferActivity) r1
            mv.com.bml.mibapi.models.payments.Payable r3 = r7.to_account
            mv.com.bml.mibapi.models.payments.BeneficiaryInfo r3 = (mv.com.bml.mibapi.models.payments.BeneficiaryInfo) r3
            java.lang.String r3 = r3.beneficiaryId
            r1.accountNoCr = r3
            mv.com.bml.mibapi.models.payments.Currency r3 = r7.currency
            if (r3 == 0) goto L_0x003d
            java.lang.String r3 = r3.code
            goto L_0x0041
        L_0x003d:
            mv.com.bml.mibapi.models.accounts.AccountCommon r3 = r7.from_account
            java.lang.String r3 = r3.accountCcy
        L_0x0041:
            r1.tranCcy = r3
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]
            float r5 = r7.amount
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4[r2] = r5
            java.lang.String r5 = "%.2f"
            java.lang.String r4 = java.lang.String.format(r5, r4)
            r1.tranAmt = r4
            mv.com.bml.mibapi.models.accounts.AccountCommon r4 = r7.from_account
            java.lang.String r4 = r4.accountId
            r1.accountNoDr = r4
            java.lang.Object[] r3 = new java.lang.Object[r3]
            float r4 = r7.amount
            java.lang.Float r4 = java.lang.Float.valueOf(r4)
            r3[r2] = r4
            java.lang.String r3 = java.lang.String.format(r5, r3)
            r1.amount = r3
            mv.com.bml.mibapi.models.accounts.AccountCommon r3 = r7.from_account
            java.lang.String r3 = r3.accountCcy
            r1.ccyAccountCr = r3
            mv.com.bml.mibapi.models.payments.Payable r3 = r7.to_account
            mv.com.bml.mibapi.models.payments.BeneficiaryInfo r3 = (mv.com.bml.mibapi.models.payments.BeneficiaryInfo) r3
            java.lang.String r3 = r3.beneficiaryId
            r1.beneficiaryId = r3
            mv.com.bml.mibapi.models.accounts.AccountCommon r3 = r7.from_account
            java.lang.String r3 = r3.branch
            r1.branch = r3
            java.lang.String r3 = r7.benef_method
            java.lang.String r4 = ""
            if (r3 == 0) goto L_0x0087
            goto L_0x0088
        L_0x0087:
            r3 = r4
        L_0x0088:
            r1.chargesIncurredBy = r3
            java.lang.String r3 = r7.benef_info
            if (r3 == 0) goto L_0x008f
            goto L_0x0090
        L_0x008f:
            r3 = r4
        L_0x0090:
            r1.paymentDetails = r3
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r2)
            r1.isFutureDatedTxn = r3
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r2)
            r1.isRecurringTxn = r3
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)
            r1.isBulkTransaction = r2
            java.lang.String r2 = "TRF"
            r1.tranType = r2
            java.lang.String r2 = "P"
            r1.tranStatus = r2
            java.lang.String r2 = "QUERY"
            r1.crudOperation = r2
            java.lang.String r2 = "SUCCESS"
            r1.status = r2
            java.lang.String r2 = "activity1"
            r1.activityId = r2
            java.lang.String r2 = r7.remarks
            if (r2 == 0) goto L_0x00be
            r1.remarks = r2
        L_0x00be:
            java.text.SimpleDateFormat r2 = new java.text.SimpleDateFormat
            java.lang.String r3 = "dd/M/yyyy"
            r2.<init>(r3)
            java.lang.String r3 = "Indian/Maldives"
            java.util.TimeZone r3 = java.util.TimeZone.getTimeZone(r3)
            r2.setTimeZone(r3)
            mv.com.bml.mibapi.models.accounts.AccountCommon r3 = r7.from_account
            java.lang.String r3 = r3.productType
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            java.lang.String r4 = "Own"
            if (r3 == 0) goto L_0x00dc
            r3 = r4
            goto L_0x00e0
        L_0x00dc:
            mv.com.bml.mibapi.models.accounts.AccountCommon r3 = r7.from_account
            java.lang.String r3 = r3.productType
        L_0x00e0:
            mv.com.bml.mibapi.models.payments.Payable r5 = r7.to_account
            mv.com.bml.mibapi.models.payments.BeneficiaryInfo r5 = (mv.com.bml.mibapi.models.payments.BeneficiaryInfo) r5
            java.lang.String r5 = r5.shelfDesc
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 == 0) goto L_0x00ee
            r5 = r4
            goto L_0x00f4
        L_0x00ee:
            mv.com.bml.mibapi.models.payments.Payable r5 = r7.to_account
            mv.com.bml.mibapi.models.payments.BeneficiaryInfo r5 = (mv.com.bml.mibapi.models.payments.BeneficiaryInfo) r5
            java.lang.String r5 = r5.shelfDesc
        L_0x00f4:
            mv.com.bml.mibapi.models.payments.Payable r6 = r7.to_account
            boolean r6 = isOwnAccount(r6)
            if (r6 == 0) goto L_0x00ff
            java.lang.String r6 = "OAT"
            goto L_0x010c
        L_0x00ff:
            java.lang.String r6 = "domestic"
            boolean r6 = r5.equalsIgnoreCase(r6)
            if (r6 == 0) goto L_0x010a
            java.lang.String r6 = "DOT"
            goto L_0x010c
        L_0x010a:
            java.lang.String r6 = "IAT"
        L_0x010c:
            r1.crTranType = r6
            java.lang.String r6 = "My Cards"
            boolean r6 = r5.equals(r6)
            if (r6 == 0) goto L_0x011b
            java.lang.String r3 = "CPA"
        L_0x0118:
            r1.crTranType = r3
            goto L_0x012c
        L_0x011b:
            java.lang.String r6 = "CARD"
            boolean r3 = r3.equals(r6)
            if (r3 == 0) goto L_0x012c
            boolean r3 = r5.equals(r4)
            if (r3 == 0) goto L_0x012c
            java.lang.String r3 = "CAD"
            goto L_0x0118
        L_0x012c:
            java.util.Date r3 = new java.util.Date
            r3.<init>()
            java.lang.String r2 = r2.format(r3)
            r1.effectDt = r2
            java.lang.String r2 = r1.effectDt
            r1.tranDt = r2
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = r1.accountNoDr
            r2.append(r3)
            java.lang.String r3 = r1.tranCcy
            r2.append(r3)
            java.lang.String r3 = r1.tranAmt
            r2.append(r3)
            java.lang.String r3 = r1.effectDt
            r2.append(r3)
            java.lang.String r3 = r1.beneficiaryId
            if (r3 == 0) goto L_0x015b
            java.lang.String r3 = r1.beneficiaryId
            goto L_0x015d
        L_0x015b:
            java.lang.String r3 = r1.accountNoCr
        L_0x015d:
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r2 = generateHash(r2)
            r1.hash = r2
            r7.makeRequest(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: mv.com.bml.mib.utils.PaymentFactory.executeTransferOp():void");
    }

    private static String generateHash(String str) {
        String str2 = "HASH";
        Log.d(str2, str);
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.reset();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte valueOf : instance.digest(str.getBytes())) {
                stringBuffer.append(String.format("%02X", new Object[]{Byte.valueOf(valueOf)}).toLowerCase());
            }
            Log.d(str2, stringBuffer.toString());
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void handleError(RetrofitError retrofitError) {
        Response response = retrofitError.getResponse();
        if (response == null || response.getStatus() != 402) {
            BMLApi.getEventBus().post(new GenericError("Unkown response"));
            retrofitError.printStackTrace();
            return;
        }
        try {
            BMLApi.getEventBus().post(new OTPRequestSent((OTPResponse) new Gson().fromJson(new String(((TypedByteArray) response.getBody()).getBytes(), "UTF-8"), OTPResponse.class)));
        } catch (Exception e) {
            e.printStackTrace();
            BMLApi.getEventBus().post(new GenericError("Unkown error"));
        }
    }

    public static boolean isDomesticAccount(BeneficiaryInfo beneficiaryInfo, Resources resources) {
        if (domestics == null) {
            domestics = resources.getStringArray(R.array.domestic_bank_code_list);
        }
        if (beneficiaryInfo.shelfDesc.equalsIgnoreCase("domestic")) {
            return true;
        }
        for (String equalsIgnoreCase : domestics) {
            if (equalsIgnoreCase.equalsIgnoreCase(beneficiaryInfo.bankCode)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInternationalAccount(BeneficiaryInfo beneficiaryInfo, Resources resources) {
        return beneficiaryInfo.shelfDesc != null && beneficiaryInfo.shelfDesc.equalsIgnoreCase("international");
    }

    public static boolean isOwnAccount(Payable payable) {
        return (payable instanceof BeneficiaryInfo) && TextUtils.isEmpty(((BeneficiaryInfo) payable).shelfDesc);
    }

    private void makeRequest(Payment payment) {
        String str;
        BMLRestClient client = BMLApi.getClient();
        String str2 = this.OTP != null ? "VALIDATE" : "GENERATE";
        StringBuilder sb = new StringBuilder();
        sb.append("OTP-Channel=");
        sb.append(this.OTP_METHOD);
        String sb2 = sb.toString();
        if (this.OTP != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("2FA ");
            sb3.append(this.OTP);
            str = sb3.toString();
        } else {
            str = null;
        }
        client.doPayment(str2, sb2, str, payment, new Callback<Payment>() {
            public void failure(RetrofitError retrofitError) {
                Log.d("TRANSFERPAYMENT", "ERROR");
                PaymentFactory.this.handleError(retrofitError);
            }

            public void success(Payment payment, Response response) {
                Log.d("TRANSFERPAYMENT", "SUCCESS");
                PaymentFactory.this.checkOTPFail(response);
                BMLApi.getEventBus().post(new PaymentTransferComplete(payment));
            }
        });
    }

    private void makeRequest(Transfer transfer) {
        String str;
        BMLRestClient client = BMLApi.getClient();
        String str2 = null;
        String str3 = (!isOwnAccount(this.to_account) || (this.from_account instanceof AccountCredit)) ? this.OTP != null ? "VALIDATE" : "GENERATE" : null;
        if (!isOwnAccount(this.to_account) || (this.from_account instanceof AccountCredit)) {
            StringBuilder sb = new StringBuilder();
            sb.append("OTP-Channel=");
            sb.append(this.OTP_METHOD);
            str = sb.toString();
        } else {
            str = null;
        }
        if ((!isOwnAccount(this.to_account) || (this.from_account instanceof AccountCredit)) && this.OTP != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("2FA ");
            sb2.append(this.OTP);
            str2 = sb2.toString();
        }
        client.doTransfer(str3, str, str2, transfer, new Callback<Transfer>() {
            public void failure(RetrofitError retrofitError) {
                Log.d("TRANSFERPAYMENT", "ERROR");
                PaymentFactory.this.handleError(retrofitError);
            }

            public void success(Transfer transfer, Response response) {
                Log.d("TRANSFERPAYMENT", "SUCCESS");
                PaymentFactory.this.checkOTPFail(response);
                BMLApi.getEventBus().post(new PaymentTransferComplete(transfer));
            }
        });
    }

    public void execute() {
        if (this.to_account instanceof BeneficiaryInfo) {
            AccountCommon accountCommon = this.from_account;
            if ((accountCommon instanceof AccountCASA) || (accountCommon instanceof AccountCredit)) {
                executeTransferOp();
                return;
            }
        }
        if ((this.to_account instanceof MerchantInfo) && (this.from_account instanceof AccountCASA)) {
            executePayment();
        }
    }
}
