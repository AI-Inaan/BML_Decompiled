package mv.com.bml.mib.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import mv.com.bml.mib.R;
import mv.com.bml.mib.adapters.TransactionDisplayAdapter;
import mv.com.bml.mib.adapters.TransactionDisplayAdapter.ClickHandler;
import mv.com.bml.mib.dialogs.TransactionDetails;
import mv.com.bml.mib.interfaces.Titleable;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.BMLRestClient;
import mv.com.bml.mibapi.models.accounts.AccountCASA;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.accounts.AccountLoan;
import mv.com.bml.mibapi.models.transactions.LoanScheduleList;
import mv.com.bml.mibapi.models.transactions.Txhistory;
import mv.com.bml.mibapi.models.transactions.Txhistory.TxItem;
import mv.com.bml.mibapi.models.transactions.TxhistoryLocked;
import mv.com.bml.mibapi.models.transactions.TxhistoryLocked.TxItemLocked;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TxHistoryFragment extends Fragment implements OnRefreshListener, ClickHandler, Titleable {
    private static final String ARG_ACCOUNT = "mv.bml.accountarg";
    private static final String ARG_MODE = "mv.bml.modearg";
    public static final int CASA_MODE_LOCKED = 3;
    public static final int CC_MODE_PENDING = 2;
    public static final int CC_MODE_UNBILLED = 1;
    /* access modifiers changed from: private */
    public AccountCommon account;
    TextView header_view;
    /* access modifiers changed from: private */
    public Date lastStartDate;
    @BindView(2131361897)
    RecyclerView list;
    TransactionDisplayAdapter mAdapter;
    ArrayList<TxItem> mData;
    View mFooterView;
    @BindView(2131362049)
    SwipeRefreshLayout mSwipeView;
    /* access modifiers changed from: private */
    public int mode;
    /* access modifiers changed from: private */
    public String period;
    /* access modifiers changed from: private */
    public int retry_count = 0;

    static /* synthetic */ int access$104(TxHistoryFragment txHistoryFragment) {
        int i = txHistoryFragment.retry_count + 1;
        txHistoryFragment.retry_count = i;
        return i;
    }

    private void initViewItems(View view) {
        this.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mData = new ArrayList<>();
        this.mAdapter = new TransactionDisplayAdapter(getActivity(), this.mData, this.header_view, this.mFooterView);
        this.mAdapter.setClickHandler(this);
        this.list.setAdapter(this.mAdapter);
        this.mSwipeView.setOnRefreshListener(this);
        AccountCommon accountCommon = this.account;
        boolean z = true;
        if (accountCommon instanceof AccountCASA) {
            TransactionDisplayAdapter transactionDisplayAdapter = this.mAdapter;
            if (this.mode == 3) {
                z = false;
            }
            transactionDisplayAdapter.setFooterVisible(z);
        } else {
            if (accountCommon != null) {
                int i = this.mode;
                if (!(i == 2 || i == 1)) {
                    this.header_view.setText(getString(R.string.history_header_cc));
                    this.mAdapter.setHeaderVisible(true);
                    this.mAdapter.setFooterVisible(false);
                }
            }
            this.mAdapter.setHeaderVisible(false);
            this.mAdapter.setFooterVisible(false);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void loadData() {
        setRefreshing(true);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar instance = Calendar.getInstance();
        instance.setTime(this.lastStartDate);
        instance.add(5, -7);
        final Date time = instance.getTime();
        if (!(this.account instanceof AccountCASA) || this.mode == 3) {
            AccountCommon accountCommon = this.account;
            if (accountCommon instanceof AccountCASA) {
                BMLApi.getClient().getTxHistorLocked(this.account.accountId, new Callback<TxhistoryLocked>() {
                    public void failure(RetrofitError retrofitError) {
                        try {
                            Toast.makeText(TxHistoryFragment.this.getActivity(), "Failed", 0).show();
                            TxHistoryFragment.this.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void success(TxhistoryLocked txhistoryLocked, Response response) {
                        TxHistoryFragment.this.setRefreshing(false);
                        if (txhistoryLocked.payload != null && txhistoryLocked.payload.size() > 0) {
                            Iterator it = txhistoryLocked.payload.iterator();
                            while (it.hasNext()) {
                                TxItemLocked txItemLocked = (TxItemLocked) it.next();
                                TxItem txItem = new TxItem();
                                txItem.tranTypeDesc = txItemLocked.Description;
                                txItem.billAmount = String.valueOf(txItemLocked.LockedAmount);
                                txItem.tranDt = txItemLocked.FromDate;
                                TxHistoryFragment.this.mData.add(txItem);
                            }
                            TxHistoryFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            } else if (accountCommon instanceof AccountCredit) {
                BMLRestClient client = BMLApi.getClient();
                String str = this.account.accountId;
                String str2 = this.account.accountNo;
                int i = this.mode;
                String str3 = i == 2 ? "outstandingAuth" : i == 1 ? "unbilledTx" : "currStmt";
                client.getCardTxHistory(str, str2, str3, new Callback<Txhistory>() {
                    public void failure(RetrofitError retrofitError) {
                        try {
                            Toast.makeText(TxHistoryFragment.this.getActivity(), "Failed", 0).show();
                            TxHistoryFragment.this.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void success(Txhistory txhistory, Response response) {
                        try {
                            if (txhistory.transactionHistoryList != null && txhistory.transactionHistoryList.size() > 0) {
                                TxHistoryFragment.this.mData.addAll(txhistory.transactionHistoryList);
                                TxHistoryFragment.this.mAdapter.notifyDataSetChanged();
                            } else if (txhistory.transactionHistoryList != null) {
                                TextView textView = TxHistoryFragment.this.header_view;
                                TxHistoryFragment txHistoryFragment = TxHistoryFragment.this;
                                int i = TxHistoryFragment.this.mode == 2 ? R.string.no_pending_authorizations : TxHistoryFragment.this.account.isPrepaid() ? R.string.no_unbilled_tx_prepaid : R.string.no_unbilled_tx;
                                textView.setText(txHistoryFragment.getString(i));
                                TxHistoryFragment.this.mAdapter.setHeaderVisible(true);
                            }
                            TxHistoryFragment.this.lastStartDate = time;
                            TxHistoryFragment.this.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (accountCommon instanceof AccountLoan) {
                BMLApi.getClient().getLoanSchedule(this.account.accountId, 1, new Callback<LoanScheduleList>() {
                    public void failure(RetrofitError retrofitError) {
                        try {
                            Toast.makeText(TxHistoryFragment.this.getActivity(), "Failed", 0).show();
                            TxHistoryFragment.this.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void success(LoanScheduleList loanScheduleList, Response response) {
                        try {
                            if (loanScheduleList.loanRepaymentScheduleList != null && loanScheduleList.loanRepaymentScheduleList.size() > 0) {
                                TxHistoryFragment.this.mData.addAll(loanScheduleList.loanRepaymentScheduleList);
                            } else if (loanScheduleList.loanRepaymentScheduleList != null) {
                                TxHistoryFragment.this.header_view.setText(TxHistoryFragment.this.getString(R.string.no_pending_payments));
                                TxHistoryFragment.this.mAdapter.setHeaderVisible(true);
                            }
                            TxHistoryFragment.this.mAdapter.notifyDataSetChanged();
                            TxHistoryFragment.this.lastStartDate = time;
                            TxHistoryFragment.this.setRefreshing(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else {
            BMLRestClient client2 = BMLApi.getClient();
            String str4 = this.account.accountId;
            String str5 = this.period;
            String str6 = null;
            String format = str5 != null ? null : simpleDateFormat.format(time);
            if (this.period == null) {
                str6 = simpleDateFormat.format(this.lastStartDate);
            }
            client2.getTxHistory(str4, str5, format, str6, new Callback<Txhistory>() {
                public void failure(RetrofitError retrofitError) {
                    try {
                        Toast.makeText(TxHistoryFragment.this.getActivity(), "Failed", 0).show();
                        TxHistoryFragment.this.setRefreshing(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void success(Txhistory txhistory, Response response) {
                    try {
                        if (txhistory.transactionHistoryList != null && txhistory.transactionHistoryList.size() > 0) {
                            TxHistoryFragment.this.mData.addAll(txhistory.transactionHistoryList);
                            TxHistoryFragment.this.mAdapter.notifyDataSetChanged();
                            TxHistoryFragment.this.retry_count = 0;
                        }
                        TxHistoryFragment.this.lastStartDate = TxHistoryFragment.this.period != null ? TxHistoryFragment.this.lastStartDate : time;
                        Calendar instance = Calendar.getInstance();
                        instance.setTime(TxHistoryFragment.this.lastStartDate);
                        instance.add(5, -1);
                        TxHistoryFragment.this.lastStartDate = instance.getTime();
                        TxHistoryFragment.this.period = null;
                        TxHistoryFragment.this.setRefreshing(false);
                        if (txhistory.transactionHistoryList != null && txhistory.transactionHistoryList.size() == 0 && TxHistoryFragment.access$104(TxHistoryFragment.this) < 4) {
                            TxHistoryFragment.this.loadData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static TxHistoryFragment newInstance(AccountCommon accountCommon) {
        TxHistoryFragment txHistoryFragment = new TxHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ACCOUNT, accountCommon);
        txHistoryFragment.setArguments(bundle);
        return txHistoryFragment;
    }

    public static TxHistoryFragment newInstance(AccountCommon accountCommon, int i) {
        TxHistoryFragment newInstance = newInstance(accountCommon);
        newInstance.getArguments().putInt(ARG_MODE, i);
        return newInstance;
    }

    private void populateView() {
        onRefresh();
    }

    /* access modifiers changed from: private */
    public void setRefreshing(final boolean z) {
        this.mFooterView.findViewById(R.id.btn_load_more).setVisibility(z ? 4 : 0);
        this.mSwipeView.post(new Runnable() {
            public void run() {
                TxHistoryFragment.this.mSwipeView.setRefreshing(z);
            }
        });
    }

    public String getTitle() {
        if (this.account instanceof AccountLoan) {
            return "Pending Payments";
        }
        int i = this.mode;
        return i == 2 ? "Outstanding Authorizations" : i == 1 ? "Unbilled Transactions" : "History";
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onClick(View view, int i) {
        TxItem item = this.mAdapter.getItem(i);
        if (item != null && !(this.account instanceof AccountLoan)) {
            TransactionDetails.newInstance(item).show(getActivity().getSupportFragmentManager(), "Detail");
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.account = (AccountCommon) getArguments().getSerializable(ARG_ACCOUNT);
            this.mode = getArguments().getInt(ARG_MODE, 0);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_tx_history, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.header_view = (TextView) layoutInflater.inflate(R.layout.listitem_disclaimer_header, null);
        this.mFooterView = layoutInflater.inflate(R.layout.listitem_loadmore_footerview, null);
        this.mFooterView.findViewById(R.id.btn_load_more).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TxHistoryFragment.this.loadData();
            }
        });
        initViewItems(inflate);
        populateView();
        return inflate;
    }

    public void onRefresh() {
        this.mData.clear();
        this.mAdapter.notifyDataSetChanged();
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        this.lastStartDate = instance.getTime();
        this.period = "1d";
        loadData();
    }
}
