package mv.com.bml.mib.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.Iterator;
import mv.com.bml.mib.R;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.BMLDataCache;
import mv.com.bml.mibapi.models.accounts.AccountCASA;
import mv.com.bml.mibapi.models.accounts.AccountCardList;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.accounts.AccountSummary;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.MerchantInfo;
import mv.com.bml.mibapi.models.payments.Payable;
import mv.com.bml.mibapi.models.payments.mobile.RechargeDenominations;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListSelectorDialog extends DialogFragment {
    public static final int ACTION_COPY = 2;
    public static final int ACTION_DELETE = 3;
    public static final int ACTION_EDIT = 1;
    public static final int METHOD_BEN_EDIT = 3;
    public static final int METHOD_CARD_EDIT = 4;
    public static final int METHOD_OTP = 1;
    public static final int METHOD_OWN_ACCOUNTS = 0;
    public static final int METHOD_RECHARGE = 2;
    @BindView(2131361901)
    ListView mListView;
    /* access modifiers changed from: private */
    public OnListSelectorSelected mListener;
    @BindView(2131361992)
    LinearLayout mProgressView;
    @BindView(2131361902)
    TextView mTitleView;
    private int method;
    /* access modifiers changed from: private */
    public String mobile_merchant_id;
    private Payable pay_to;

    public interface OnListSelectorSelected {
        void onAccountSelected(AccountCommon accountCommon);

        void onEditActionSelected(int i);

        void onOTPMethodSelected(String str);

        void onPayableSelected(Payable payable);

        void onRechargeAmountSelected(RechargeDenominations rechargeDenominations);
    }

    private void populateView() {
        int i = this.method;
        if (i == 0) {
            this.mTitleView.setText("Select Account");
            setAccounts();
        } else if (i == 1) {
            this.mTitleView.setText("Verification Method");
            setOTPMethod();
        } else if (i != 2) {
            String str = "Select Action";
            if (i == 3) {
                this.mTitleView.setText(str);
                setBenEdit();
            } else if (i == 4) {
                this.mTitleView.setText(str);
                setCardEdit();
            }
        } else {
            this.mTitleView.setText("Amount");
            setRecharge();
        }
    }

    /* access modifiers changed from: private */
    public void setAccounts() {
        String str = "accounts.account_summary";
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str)) {
            String str2 = "accounts.card_list";
            if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str2)) {
                ArrayList arrayList = new ArrayList();
                AccountSummary accountSummary = (AccountSummary) BMLApi.getInstance().getDataCache().getCacheItem(str);
                if (accountSummary.acctSummaryCASAList.size() > 0) {
                    Iterator it = accountSummary.acctSummaryCASAList.iterator();
                    while (it.hasNext()) {
                        AccountCASA accountCASA = (AccountCASA) it.next();
                        Payable payable = this.pay_to;
                        if (payable != null) {
                            if (!(payable instanceof BeneficiaryInfo) || !((BeneficiaryInfo) payable).accountNoCr.equalsIgnoreCase(accountCASA.accountNo)) {
                                Payable payable2 = this.pay_to;
                                if ((payable2 instanceof MerchantInfo) && ((MerchantInfo) payable2).accountNumber != null && ((MerchantInfo) this.pay_to).accountNumber.equalsIgnoreCase(accountCASA.accountNo)) {
                                }
                            }
                        }
                        arrayList.add(accountCASA);
                    }
                }
                AccountCardList accountCardList = (AccountCardList) BMLApi.getInstance().getDataCache().getCacheItem(str2);
                if (accountCardList.cardList.size() > 0) {
                    Iterator it2 = accountCardList.cardList.iterator();
                    while (it2.hasNext()) {
                        AccountCredit accountCredit = (AccountCredit) it2.next();
                        Payable payable3 = this.pay_to;
                        if (payable3 == null || !(payable3 instanceof MerchantInfo)) {
                            arrayList.add(accountCredit);
                        }
                    }
                }
                setAccounts(arrayList);
                return;
            }
            setProgress(true);
            BMLApi.getClient().getCardList(new Callback<AccountCardList>() {
                public void failure(RetrofitError retrofitError) {
                    try {
                        retrofitError.printStackTrace();
                        ListSelectorDialog.this.setProgress(false);
                        Toast.makeText(ListSelectorDialog.this.getActivity(), "Unable to retrieve Accounts", 1).show();
                        ListSelectorDialog.this.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void success(AccountCardList accountCardList, Response response) {
                    try {
                        ListSelectorDialog.this.setProgress(false);
                        BMLApi.getInstance().getDataCache().setCacheItem("accounts.card_list", accountCardList, false);
                        ListSelectorDialog.this.setAccounts();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return;
        }
        setProgress(true);
        BMLApi.getClient().getAccountSummary(new Callback<AccountSummary>() {
            public void failure(RetrofitError retrofitError) {
                try {
                    retrofitError.printStackTrace();
                    ListSelectorDialog.this.setProgress(false);
                    Toast.makeText(ListSelectorDialog.this.getActivity(), "Unable to retrieve Accounts", 1).show();
                    ListSelectorDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void success(AccountSummary accountSummary, Response response) {
                try {
                    BMLApi.getInstance().getDataCache().setCacheItem("accounts.account_summary", accountSummary, false);
                    ListSelectorDialog.this.setProgress(false);
                    ListSelectorDialog.this.setAccounts();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setAccounts(final ArrayList<AccountCommon> arrayList) {
        this.mListView.setAdapter(new ArrayAdapter(getActivity(), 17367043, 16908308, arrayList));
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (ListSelectorDialog.this.mListener != null) {
                    ListSelectorDialog.this.mListener.onAccountSelected((AccountCommon) arrayList.get(i));
                }
                ListSelectorDialog.this.dismiss();
            }
        });
    }

    private void setBenEdit() {
        this.mListView.setAdapter(new ArrayAdapter(getActivity(), 17367043, 16908308, new String[]{"Copy Contact", "Delete Contact"}));
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                int i2;
                OnListSelectorSelected onListSelectorSelected;
                if (ListSelectorDialog.this.mListener != null) {
                    if (i == 0) {
                        onListSelectorSelected = ListSelectorDialog.this.mListener;
                        i2 = 2;
                    } else if (i == 1) {
                        onListSelectorSelected = ListSelectorDialog.this.mListener;
                        i2 = 3;
                    }
                    onListSelectorSelected.onEditActionSelected(i2);
                }
                ListSelectorDialog.this.dismiss();
            }
        });
    }

    private void setCardEdit() {
        this.mListView.setAdapter(new ArrayAdapter(getActivity(), 17367043, 16908308, new String[]{"Copy Contact"}));
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (ListSelectorDialog.this.mListener != null && i == 0) {
                    ListSelectorDialog.this.mListener.onEditActionSelected(2);
                }
                ListSelectorDialog.this.dismiss();
            }
        });
    }

    private void setOTPMethod() {
        String str = "security.otp_channels";
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str)) {
            setOTPMethod((ArrayList) BMLApi.getInstance().getDataCache().getCacheItem(str));
            return;
        }
        setProgress(true);
        BMLApi.getClient().getOTPChannels(new Callback<ArrayList<String>>() {
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(ListSelectorDialog.this.getActivity(), "Failed to get OTP channels", 0).show();
                ListSelectorDialog.this.setProgress(false);
            }

            public void success(ArrayList<String> arrayList, Response response) {
                BMLApi.getInstance().getDataCache().setCacheItem("security.otp_channels", arrayList, true);
                if (arrayList != null) {
                    ListSelectorDialog.this.setOTPMethod(arrayList);
                }
                ListSelectorDialog.this.setProgress(false);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setOTPMethod(final ArrayList<String> arrayList) {
        this.mListView.setAdapter(new ArrayAdapter(getActivity(), 17367043, 16908308, arrayList));
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (ListSelectorDialog.this.mListener != null) {
                    ListSelectorDialog.this.mListener.onOTPMethodSelected((String) arrayList.get(i));
                }
                ListSelectorDialog.this.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */
    public void setProgress(boolean z) {
        this.mProgressView.setVisibility(z ? 0 : 8);
    }

    private void setRecharge() {
        BMLDataCache dataCache = BMLApi.getInstance().getDataCache();
        StringBuilder sb = new StringBuilder();
        String str = "mobile.denoms_";
        sb.append(str);
        sb.append(this.mobile_merchant_id);
        if (dataCache.hasValidCacheItem(sb.toString())) {
            BMLDataCache dataCache2 = BMLApi.getInstance().getDataCache();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(this.mobile_merchant_id);
            setRecharge((ArrayList) dataCache2.getCacheItem(sb2.toString()));
            return;
        }
        setProgress(true);
        BMLApi.getClient().getRechargeDenominations(this.mobile_merchant_id, new Callback<ArrayList<RechargeDenominations>>() {
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(ListSelectorDialog.this.getActivity(), "Failed to get OTP channels", 0).show();
                ListSelectorDialog.this.setProgress(false);
            }

            public void success(ArrayList<RechargeDenominations> arrayList, Response response) {
                BMLDataCache dataCache = BMLApi.getInstance().getDataCache();
                StringBuilder sb = new StringBuilder();
                sb.append("mobile.denoms_");
                sb.append(ListSelectorDialog.this.mobile_merchant_id);
                dataCache.setCacheItem(sb.toString(), arrayList, true);
                if (arrayList != null) {
                    ListSelectorDialog.this.setRecharge(arrayList);
                }
                ListSelectorDialog.this.setProgress(false);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setRecharge(final ArrayList<RechargeDenominations> arrayList) {
        this.mListView.setAdapter(new ArrayAdapter(getActivity(), 17367043, 16908308, arrayList));
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (ListSelectorDialog.this.mListener != null) {
                    ListSelectorDialog.this.mListener.onRechargeAmountSelected((RechargeDenominations) arrayList.get(i));
                }
                ListSelectorDialog.this.dismiss();
            }
        });
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_list_selector_dialog, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        getDialog().getWindow().requestFeature(1);
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        populateView();
    }

    public void setListener(OnListSelectorSelected onListSelectorSelected) {
        this.mListener = onListSelectorSelected;
    }

    public void setMethod(int i) {
        this.method = i;
    }

    public void setMobileMerchant(String str) {
        this.mobile_merchant_id = str;
    }

    public void setPayTo(Payable payable) {
        this.pay_to = payable;
    }
}
