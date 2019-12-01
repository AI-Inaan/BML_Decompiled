package mv.com.bml.mib.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mv.com.bml.mib.R;
import mv.com.bml.mib.adapters.AccountDetailItemAdapter;
import mv.com.bml.mib.interfaces.Titleable;
import mv.com.bml.mib.models.AccDetailItem;
import mv.com.bml.mib.utils.MiscUtils;
import mv.com.bml.mib.utils.PaymentFactory;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.misc.ValidationResult;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.MerchantInfo;
import mv.com.bml.mibapi.models.payments.Payable;
import mv.com.bml.mibapi.models.payments.Payment;
import mv.com.bml.mibapi.models.payments.PaymentActivity;
import mv.com.bml.mibapi.models.payments.Transfer;
import mv.com.bml.mibapi.models.payments.TransferActivity;

public class TransactionReceiptFragment extends Fragment implements OnClickListener, Titleable {
    protected static final String ARG_FROM_ACCOUNT = "param2";
    protected static final String ARG_TO_ACCOUNT = "param3";
    protected static final String ARG_TRANS_OBJ = "param1";
    protected static final String TAG = "Receipt";
    @BindView(2131361848)
    Button btn_copy_recharge;
    @BindView(2131361857)
    Button btn_save;
    @BindView(2131361861)
    Button btn_share;
    protected AccountDetailItemAdapter mAdapter;
    protected ArrayList<AccDetailItem> mDetails;
    protected AccountCommon pay_from;
    protected Payable pay_to;
    @BindView(2131362000)
    View receipt_container;
    @BindView(2131362001)
    ListView receipt_item_list;
    @BindView(2131362002)
    TextView receipt_title;
    private String recharge_code;
    protected Object transobj;

    private String getRefNo() {
        String str = null;
        try {
            if (this.transobj instanceof Payment) {
                str = ((PaymentActivity) ((Payment) this.transobj).paymentActivityList.get(0)).channelRefNo;
            } else if (this.transobj instanceof Transfer) {
                str = ((TransferActivity) ((Transfer) this.transobj).transferActivityList.get(0)).channelRefNo;
            }
        } catch (Exception unused) {
        }
        return str == null ? UUID.randomUUID().toString() : str;
    }

    public static TransactionReceiptFragment newInstance(Payment payment, AccountCommon accountCommon, Payable payable) {
        TransactionReceiptFragment transactionReceiptFragment = new TransactionReceiptFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("param1", payment);
        bundle.putSerializable("param2", accountCommon);
        bundle.putSerializable(ARG_TO_ACCOUNT, payable);
        transactionReceiptFragment.setArguments(bundle);
        return transactionReceiptFragment;
    }

    public static TransactionReceiptFragment newInstance(Transfer transfer, AccountCommon accountCommon, Payable payable) {
        TransactionReceiptFragment transactionReceiptFragment = new TransactionReceiptFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("param1", transfer);
        bundle.putSerializable("param2", accountCommon);
        bundle.putSerializable(ARG_TO_ACCOUNT, payable);
        transactionReceiptFragment.setArguments(bundle);
        return transactionReceiptFragment;
    }

    private void populateView(Payment payment) {
        String[] strArr;
        String str;
        PaymentActivity paymentActivity = (PaymentActivity) payment.paymentActivityList.get(0);
        MiscUtils.addTxRowItem(this.mDetails, "Status", paymentActivity.status, getResources().getColor((paymentActivity.status == null || !paymentActivity.status.contentEquals("SUCCESS")) ? R.color.primary_red : R.color.primary_green));
        MiscUtils.addTxRowItem(this.mDetails, "Message", (paymentActivity.validationResultList == null || paymentActivity.validationResultList.size() <= 0) ? paymentActivity.message : TextUtils.join("\n", ((ValidationResult) paymentActivity.validationResultList.get(0)).messages));
        String str2 = null;
        if (paymentActivity.mobileRechargeSuccessMessage == null || TextUtils.isEmpty(paymentActivity.mobileRechargeSuccessMessage)) {
            this.btn_copy_recharge.setVisibility(8);
        } else {
            MiscUtils.addTxRowItem(this.mDetails, null, paymentActivity.mobileRechargeSuccessMessage);
            Matcher matcher = Pattern.compile("recharge code\\s?:\\s?(\\d+)", 2).matcher(paymentActivity.mobileRechargeSuccessMessage);
            if (matcher.find()) {
                this.recharge_code = matcher.group(1);
            }
        }
        String format = new SimpleDateFormat("HH:mm", Locale.UK).format(new Date());
        MiscUtils.addTxRowItem(this.mDetails, "Ref #", paymentActivity.channelRefNo);
        MiscUtils.addTxRowItem(this.mDetails, "Invoice #", paymentActivity.invoiceNo);
        ArrayList<AccDetailItem> arrayList = this.mDetails;
        StringBuilder sb = new StringBuilder();
        sb.append(paymentActivity.tranDt);
        String str3 = " ";
        sb.append(str3);
        sb.append(format);
        MiscUtils.addTxRowItem(arrayList, "Date", sb.toString());
        AccountCommon accountCommon = this.pay_from;
        String str4 = "<br>";
        if (accountCommon instanceof AccountCredit) {
            strArr = new String[2];
            strArr[0] = accountCommon.accountName != null ? this.pay_from.accountName : this.pay_from.accountAlias;
            if (PaymentFactory.isOwnAccount(this.pay_to)) {
                str2 = MiscUtils.maskAccNum(this.pay_from.accountNo);
            }
            strArr[1] = str2;
        } else {
            strArr = new String[2];
            strArr[0] = accountCommon.accountName != null ? this.pay_from.accountName : this.pay_from.accountAlias;
            if (PaymentFactory.isOwnAccount(this.pay_to)) {
                str2 = this.pay_from.accountNo;
            }
            strArr[1] = str2;
        }
        MiscUtils.addTxRowItem(this.mDetails, "From", MiscUtils.nullFreeConcat(str4, strArr));
        Payable payable = this.pay_to;
        if (payable instanceof MerchantInfo) {
            String[] strArr2 = new String[2];
            strArr2[0] = ((MerchantInfo) payable).merchantEnrolAlias != null ? ((MerchantInfo) this.pay_to).merchantEnrolAlias : paymentActivity.accountNameCr;
            strArr2[1] = ((MerchantInfo) this.pay_to).accountNumber;
            str = MiscUtils.nullFreeConcat(str4, strArr2);
        } else {
            str = paymentActivity.accountNameCr;
        }
        MiscUtils.addTxRowItem(this.mDetails, "To", str);
        MiscUtils.addTxRowItem(this.mDetails, "Amount", MiscUtils.nullFreeConcat(str3, paymentActivity.tranCcy, paymentActivity.tranAmt));
        MiscUtils.addTxRowItem(this.mDetails, "Remarks", paymentActivity.remarks);
    }

    private void populateView(Transfer transfer) {
        String[] strArr;
        String str;
        TransferActivity transferActivity = (TransferActivity) transfer.transferActivityList.get(0);
        this.btn_copy_recharge.setVisibility(8);
        MiscUtils.addTxRowItem(this.mDetails, "Status", transferActivity.status, getResources().getColor((transferActivity.status == null || !transferActivity.status.contentEquals("SUCCESS")) ? R.color.primary_red : R.color.primary_green));
        String join = (transferActivity.validationResultList == null || transferActivity.validationResultList.size() <= 0) ? transferActivity.message : TextUtils.join("\n", ((ValidationResult) transferActivity.validationResultList.get(0)).messages);
        String format = new SimpleDateFormat("HH:mm", Locale.UK).format(new Date());
        MiscUtils.addTxRowItem(this.mDetails, "Message", join);
        MiscUtils.addTxRowItem(this.mDetails, "Ref #", transferActivity.channelRefNo);
        ArrayList<AccDetailItem> arrayList = this.mDetails;
        StringBuilder sb = new StringBuilder();
        sb.append(transferActivity.tranDt);
        String str2 = " ";
        sb.append(str2);
        sb.append(format);
        MiscUtils.addTxRowItem(arrayList, "Date", sb.toString());
        AccountCommon accountCommon = this.pay_from;
        String str3 = null;
        String str4 = "<br>";
        if (accountCommon instanceof AccountCredit) {
            strArr = new String[2];
            strArr[0] = accountCommon.accountName != null ? this.pay_from.accountName : this.pay_from.accountAlias;
            if (PaymentFactory.isOwnAccount(this.pay_to)) {
                str3 = MiscUtils.maskAccNum(this.pay_from.accountNo);
            }
            strArr[1] = str3;
        } else {
            strArr = new String[2];
            strArr[0] = accountCommon.accountName != null ? this.pay_from.accountName : this.pay_from.accountAlias;
            if (PaymentFactory.isOwnAccount(this.pay_to)) {
                str3 = this.pay_from.accountNo;
            }
            strArr[1] = str3;
        }
        MiscUtils.addTxRowItem(this.mDetails, "From", MiscUtils.nullFreeConcat(str4, strArr));
        Payable payable = this.pay_to;
        if (payable instanceof BeneficiaryInfo) {
            str = MiscUtils.nullFreeConcat(str4, ((BeneficiaryInfo) payable).beneficiaryAlias != null ? ((BeneficiaryInfo) this.pay_to).beneficiaryAlias : transferActivity.accountNameCr, ((BeneficiaryInfo) this.pay_to).accountNoCr);
        } else {
            str = MiscUtils.nullFreeConcat(str4, transferActivity.accountNameCr, transferActivity.accountNoCr);
        }
        MiscUtils.addTxRowItem(this.mDetails, "To", str);
        MiscUtils.addTxRowItem(this.mDetails, "Amount", MiscUtils.nullFreeConcat(str2, transferActivity.tranCcy, transferActivity.tranAmt));
        MiscUtils.addTxRowItem(this.mDetails, "Remarks", transferActivity.remarks);
    }

    private Uri saveImage() {
        String str;
        String str2 = "BMLMobileBanking";
        Bitmap createBitmap = Bitmap.createBitmap(this.receipt_container.getWidth(), this.receipt_container.getHeight(), Config.ARGB_8888);
        this.receipt_container.draw(new Canvas(createBitmap));
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), str2);
            if (!file.exists()) {
                file.mkdirs();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(getRefNo());
            sb.append(".png");
            File file2 = new File(file, sb.toString());
            str = file2.getAbsolutePath();
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            createBitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            MediaScannerConnection.scanFile(getActivity(), new String[]{file2.toString()}, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            str = Media.insertImage(getActivity().getContentResolver(), createBitmap, str2, "BML");
        }
        Toast.makeText(getActivity(), str != null ? "Saved to Gallery" : "Failed to save image.", 0).show();
        if (str != null) {
            return Uri.parse(str);
        }
        return null;
    }

    private void shareImage() {
        String str = "images";
        Bitmap createBitmap = Bitmap.createBitmap(this.receipt_container.getWidth(), this.receipt_container.getHeight(), Config.ARGB_8888);
        this.receipt_container.draw(new Canvas(createBitmap));
        try {
            String format = String.format("image-%s.png", new Object[]{UUID.randomUUID().toString()});
            File file = new File(getContext().getCacheDir(), str);
            file.mkdirs();
            StringBuilder sb = new StringBuilder();
            sb.append(file);
            sb.append("/");
            sb.append(format);
            FileOutputStream fileOutputStream = new FileOutputStream(sb.toString());
            createBitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Uri uriForFile = FileProvider.getUriForFile(getContext(), "mv.com.bml.mib.activities.fileprovider", new File(new File(getContext().getCacheDir(), str), format));
            if (uriForFile != null) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.addFlags(1);
                intent.setDataAndType(uriForFile, getContext().getContentResolver().getType(uriForFile));
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                startActivity(Intent.createChooser(intent, "Choose an app"));
                createBitmap.recycle();
                return;
            }
            throw new IOException(NotificationCompat.CATEGORY_ERROR);
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Failed to save image", 1).show();
            e.printStackTrace();
        } catch (Throwable th) {
            createBitmap.recycle();
            throw th;
        }
    }

    public String getTitle() {
        return TAG;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_copy_recharge) {
            if (this.recharge_code != null) {
                ((ClipboardManager) getActivity().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("recharge", this.recharge_code));
            }
            Toast.makeText(getActivity(), this.recharge_code != null ? "Code copied" : "Error, try manually", 0).show();
        } else if (id == R.id.btn_save) {
            String str = "android.permission.WRITE_EXTERNAL_STORAGE";
            if (ContextCompat.checkSelfPermission(getContext(), str) == 0) {
                saveImage();
            } else {
                requestPermissions(new String[]{str}, 666);
            }
        } else if (id == R.id.btn_share) {
            shareImage();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        if (getArguments() != null) {
            this.transobj = getArguments().getSerializable("param1");
            this.pay_from = (AccountCommon) getArguments().getSerializable("param2");
            this.pay_to = (Payable) getArguments().getSerializable(ARG_TO_ACCOUNT);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_payment_receipt, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.btn_save.setOnClickListener(this);
        this.btn_share.setOnClickListener(this);
        this.btn_copy_recharge.setOnClickListener(this);
        this.mDetails = new ArrayList<>();
        this.mAdapter = new AccountDetailItemAdapter(getActivity(), this.mDetails);
        this.receipt_item_list.setAdapter(this.mAdapter);
        Object obj = this.transobj;
        if (obj instanceof Payment) {
            populateView((Payment) obj);
        } else if (obj instanceof Transfer) {
            populateView((Transfer) obj);
        }
        this.mAdapter.notifyDataSetChanged();
        MiscUtils.setListViewHeightBasedOnChildren(this.receipt_item_list);
        BMLApi.getInstance().getDataCache().invalidateCache("accounts.account_summary");
        BMLApi.getInstance().getDataCache().invalidateCache("accounts.card_list");
        return inflate;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 666 && iArr.length > 0 && iArr[0] == 0) {
            saveImage();
        }
    }
}
