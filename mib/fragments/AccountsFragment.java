package mv.com.bml.mib.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import mv.com.bml.mib.R;
import mv.com.bml.mib.activities.AccountDetailActivity;
import mv.com.bml.mib.adapters.MyAccountsAdapter;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mib.models.HeaderItem;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.accounts.AccountCardList;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.accounts.AccountLoan;
import mv.com.bml.mibapi.models.accounts.AccountSummary;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.MerchantInfo;
import mv.com.bml.mibapi.models.payments.Payable;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountsFragment extends Fragment implements OnRefreshListener, OnItemClickListener, OnItemLongClickListener {
    public static final String ARG_PAY_TO = "arg.payto";
    private static final String TAG = "AccountsFragment";
    private MyAccountsAdapter mAdapter;
    private ArrayList<Object> mData;
    @BindView(2131361802)
    ListView mListView;
    protected OnFragmentInteractionListener mListener;
    @BindView(2131362049)
    SwipeRefreshLayout mSwipeLayout;
    private Payable pay_to;
    int processing_request_count = 0;
    private HashMap<HeaderItem, ArrayList<Object>> sortMap;

    /* access modifiers changed from: private */
    public void addCachedCASAandLoanAccounts() {
        String str = "accounts.account_summary";
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str)) {
            AccountSummary accountSummary = (AccountSummary) BMLApi.getInstance().getDataCache().getCacheItem(str);
            if (accountSummary.acctSummaryCASAList.size() > 0) {
                HeaderItem headerItem = new HeaderItem();
                headerItem.section_title = getString(R.string.current_and_savings);
                addAccountList(headerItem, accountSummary.acctSummaryCASAList);
            }
            if (accountSummary.acctSummaryLoanList.size() > 0) {
                HeaderItem headerItem2 = new HeaderItem();
                headerItem2.section_title = getString(R.string.loans);
                addAccountList(headerItem2, accountSummary.acctSummaryLoanList);
            }
        }
    }

    /* access modifiers changed from: private */
    public void addCachedCardList() {
        String str = "accounts.card_list";
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str)) {
            AccountCardList accountCardList = (AccountCardList) BMLApi.getInstance().getDataCache().getCacheItem(str);
            if (accountCardList.cardList.size() > 0) {
                AccountCredit accountCredit = (AccountCredit) accountCardList.cardList.get(0);
                HeaderItem headerItem = new HeaderItem();
                headerItem.section_title = getString(R.string.creditCards);
                headerItem.subtitle = getString(R.string.creditAvailableTitle);
                headerItem.subtitle_sub1 = accountCredit.customerCreditCcy;
                headerItem.subtitle_sub2_color = Color.parseColor("white");
                headerItem.subtitle_sub2 = accountCredit.formattedCustAvailableCreditLimit;
                addAccountList(headerItem, accountCardList.getNonPrepaidCards());
                HeaderItem headerItem2 = new HeaderItem();
                headerItem2.section_title = getString(R.string.prepaidCards);
                addAccountList(headerItem2, accountCardList.getPrepaidCards());
            }
        }
    }

    private void getAccountSummary() {
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem("accounts.account_summary")) {
            updateProcessing(-1);
            addCachedCASAandLoanAccounts();
            return;
        }
        BMLApi.getClient().getAccountSummary(new Callback<AccountSummary>() {
            public void failure(RetrofitError retrofitError) {
                try {
                    Log.d(AccountsFragment.TAG, "updating failed");
                    retrofitError.printStackTrace();
                    AccountsFragment.this.updateProcessing(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void success(AccountSummary accountSummary, Response response) {
                try {
                    AccountsFragment.this.updateProcessing(-1);
                    BMLApi.getInstance().getDataCache().setCacheItem("accounts.account_summary", accountSummary, false);
                    AccountsFragment.this.addCachedCASAandLoanAccounts();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCardList() {
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem("accounts.card_list")) {
            updateProcessing(-1);
            addCachedCardList();
            return;
        }
        BMLApi.getClient().getCardList(new Callback<AccountCardList>() {
            public void failure(RetrofitError retrofitError) {
                try {
                    Log.d(AccountsFragment.TAG, "updating failed cards");
                    retrofitError.printStackTrace();
                    AccountsFragment.this.updateProcessing(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void success(AccountCardList accountCardList, Response response) {
                try {
                    AccountsFragment.this.updateProcessing(-1);
                    BMLApi.getInstance().getDataCache().setCacheItem("accounts.card_list", accountCardList, false);
                    AccountsFragment.this.addCachedCardList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static AccountsFragment newInstance() {
        return new AccountsFragment();
    }

    public static AccountsFragment newInstance(Payable payable) {
        AccountsFragment accountsFragment = new AccountsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PAY_TO, payable);
        accountsFragment.setArguments(bundle);
        return accountsFragment;
    }

    private void rebuildBackingArray() {
        this.mData.clear();
        ArrayList arrayList = new ArrayList(this.sortMap.keySet());
        Collections.sort(arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            HeaderItem headerItem = (HeaderItem) it.next();
            this.mData.add(headerItem);
            this.mData.addAll((Collection) this.sortMap.get(headerItem));
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void addAccountList(HeaderItem headerItem, ArrayList<Object> arrayList) {
        ArrayList arrayList2 = new ArrayList(arrayList);
        if (this.pay_to != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next instanceof AccountLoan) {
                    it.remove();
                }
                if ((this.pay_to instanceof MerchantInfo) && (next instanceof AccountCredit)) {
                    it.remove();
                }
            }
        }
        if (arrayList2.size() > 0) {
            this.sortMap.put(headerItem, arrayList2);
        }
        rebuildBackingArray();
        this.mAdapter.notifyDataSetChanged();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(activity.toString());
            sb.append(" must implement OnFragmentInteractionListener");
            throw new ClassCastException(sb.toString());
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.pay_to = (Payable) getArguments().getSerializable(ARG_PAY_TO);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_accounts, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.sortMap = new HashMap<>();
        this.mSwipeLayout.setOnRefreshListener(this);
        this.mData = new ArrayList<>();
        this.mAdapter = new MyAccountsAdapter(getActivity(), this.mData);
        if (this.pay_to != null) {
            TextView textView = (TextView) layoutInflater.inflate(R.layout.listitem_benefitiary_header, null, false);
            Payable payable = this.pay_to;
            textView.setText(getText((payable instanceof MerchantInfo) && ((MerchantInfo) payable).merchantEnrolType.equals("PREPAID_CARD") ? R.string.account_header_choose_topup : R.string.account_header_choose));
            this.mListView.addHeaderView(textView, null, false);
        }
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnItemClickListener(this);
        this.mListView.setOnItemLongClickListener(this);
        return inflate;
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object item = this.mListView.getAdapter().getItem(i);
        if (!(item instanceof HeaderItem)) {
            Payable payable = this.pay_to;
            if (payable == null) {
                Intent intent = new Intent(getActivity(), AccountDetailActivity.class);
                intent.putExtra(AccountDetailActivity.ARG_ACCOUNT_OBJECT, (AccountCommon) item);
                startActivity(intent);
            } else {
                OnFragmentInteractionListener onFragmentInteractionListener = this.mListener;
                if (onFragmentInteractionListener != null) {
                    if ((payable instanceof BeneficiaryInfo) || (payable instanceof MerchantInfo)) {
                        this.mListener.onFragmentInteraction(PerformPaymentTransferFragment.newInstance(this.pay_to, (AccountCommon) item), true);
                    } else {
                        onFragmentInteractionListener.onFragmentInteraction(BeneficiaryListFragment.newInstance((AccountCommon) item), true);
                    }
                }
            }
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object item = this.mListView.getAdapter().getItem(i);
        if (item instanceof HeaderItem) {
            return false;
        }
        ((ClipboardManager) getActivity().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("accountno", ((AccountCommon) item).accountNo));
        Toast.makeText(getActivity(), "Account No. Copied", 0).show();
        return true;
    }

    public void onRefresh() {
        if (this.processing_request_count <= 0) {
            BMLApi.getInstance().getDataCache().invalidateCache("accounts.account_summary");
            BMLApi.getInstance().getDataCache().invalidateCache("accounts.card_list");
            reload_data();
        }
    }

    public void onResume() {
        super.onResume();
        reload_data();
    }

    /* access modifiers changed from: protected */
    public void reload_data() {
        this.mData.clear();
        this.sortMap.clear();
        this.mAdapter.notifyDataSetChanged();
        updateProcessing(2);
        getAccountSummary();
        getCardList();
    }

    /* access modifiers changed from: protected */
    public synchronized void updateProcessing(int i) {
        this.processing_request_count += i;
        this.mSwipeLayout.post(new Runnable() {
            public void run() {
                AccountsFragment.this.mSwipeLayout.setRefreshing(AccountsFragment.this.processing_request_count > 0);
            }
        });
    }
}
