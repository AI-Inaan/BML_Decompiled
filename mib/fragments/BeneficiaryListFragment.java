package mv.com.bml.mib.fragments;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import mv.com.bml.mib.R;
import mv.com.bml.mib.adapters.BeneficiariesAdapter;
import mv.com.bml.mib.dialogs.ListSelectorDialog;
import mv.com.bml.mib.dialogs.ListSelectorDialog.OnListSelectorSelected;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mib.utils.PaymentFactory;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.accounts.AccountCASA;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfoList;
import mv.com.bml.mibapi.models.payments.MerchantInfoList;
import mv.com.bml.mibapi.models.payments.Payable;
import mv.com.bml.mibapi.models.payments.mobile.RechargeDenominations;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BeneficiaryListFragment extends AccountsFragment implements OnActionExpandListener, OnCloseListener, OnQueryTextListener, OnChildClickListener {
    private static final String ARG_PAY_FLOW = "param3";
    private static final String ARG_PAY_FROM = "param2";
    private static final String TAG = "BeneficiaryFragment";
    /* access modifiers changed from: private */
    public boolean flag_pay;
    protected BeneficiariesAdapter mAdapter;
    protected TreeMap<String, ArrayList<Payable>> mData;
    protected ExpandableListView mListView;
    /* access modifiers changed from: private */
    public AccountCommon pay_from;

    public static class TMComparator implements Comparator<String> {
        public int compare(String str, String str2) {
            String str3 = "my accounts";
            if (str.equalsIgnoreCase(str3)) {
                return -3;
            }
            if (str2.equalsIgnoreCase(str3)) {
                return 3;
            }
            String str4 = "my cards";
            if (str.equalsIgnoreCase(str4)) {
                return -2;
            }
            if (str2.equalsIgnoreCase(str4)) {
                return 2;
            }
            String str5 = "my loans";
            if (str.equalsIgnoreCase(str5)) {
                return -1;
            }
            if (str2.equalsIgnoreCase(str5)) {
                return 1;
            }
            return str.compareToIgnoreCase(str2);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void addCachedBeneficiariesList() {
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem("payments.beneficiaries")) {
            HashMap sortByShelf = BeneficiaryInfoList.sortByShelf(BeneficiaryInfoList.filterEligible((ArrayList) BMLApi.getInstance().getDataCache().getCacheItem("payments.beneficiaries")));
            for (String str : sortByShelf.keySet()) {
                ArrayList arrayList = new ArrayList((ArrayList) sortByShelf.get(str));
                if (this.pay_from != null || this.flag_pay) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        BeneficiaryInfo beneficiaryInfo = (BeneficiaryInfo) it.next();
                        if (this.pay_from == null || !this.pay_from.accountNo.equals(beneficiaryInfo.accountNoCr)) {
                            if (!(this.pay_from instanceof AccountCredit) || PaymentFactory.isOwnAccount(beneficiaryInfo)) {
                                if (!TextUtils.isEmpty(beneficiaryInfo.bankCode) && !beneficiaryInfo.bankCode.equals("BML")) {
                                    if (PaymentFactory.isDomesticAccount(beneficiaryInfo, getResources())) {
                                    }
                                }
                            }
                        }
                        it.remove();
                    }
                }
                if (arrayList.size() > 0) {
                    addAccountList(str, arrayList);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void addCachedMerchantList() {
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem("payments.merchants")) {
            HashMap merchantMap = ((MerchantInfoList) BMLApi.getInstance().getDataCache().getCacheItem("payments.merchants")).getMerchantMap();
            for (String str : merchantMap.keySet()) {
                addAccountList(str, (ArrayList) merchantMap.get(str));
            }
        }
    }

    /* access modifiers changed from: private */
    public void commitDeleteBeneficiary(BeneficiaryInfo beneficiaryInfo) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        BMLApi.getClient().deleteBeneficiary(beneficiaryInfo.beneficiaryId, beneficiaryInfo, new Callback<BeneficiaryInfo>() {
            public void failure(RetrofitError retrofitError) {
                retrofitError.printStackTrace();
                Toast.makeText(BeneficiaryListFragment.this.getActivity(), "Failed to delete.", 0).show();
                progressDialog.dismiss();
            }

            public void success(BeneficiaryInfo beneficiaryInfo, Response response) {
                if (beneficiaryInfo.status.equalsIgnoreCase("SUCCESS")) {
                    BMLApi.getInstance().getDataCache().invalidateCache("payments.beneficiaries");
                    BeneficiaryListFragment.this.reload_data();
                } else {
                    Toast.makeText(BeneficiaryListFragment.this.getActivity(), "Failed to delete.", 0).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */
    public void deleteBeneficiary(final BeneficiaryInfo beneficiaryInfo) {
        AnonymousClass5 r0 = new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    BeneficiaryListFragment.this.commitDeleteBeneficiary(beneficiaryInfo);
                }
            }
        };
        Builder builder = new Builder(getActivity());
        StringBuilder sb = new StringBuilder();
        sb.append("Are you sure you want to delete ");
        sb.append(beneficiaryInfo.beneficiaryAlias);
        sb.append(" from your contacts?");
        builder.setMessage(sb.toString()).setPositiveButton("Yes", r0).setNegativeButton("No", r0).show();
    }

    private void getBeneficiaries() {
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem("payments.beneficiaries")) {
            updateProcessing(-1);
            addCachedBeneficiariesList();
            return;
        }
        BMLApi.getClient().getBeneficiaryList(new Callback<ArrayList<BeneficiaryInfo>>() {
            public void failure(RetrofitError retrofitError) {
                try {
                    Log.d(BeneficiaryListFragment.TAG, "updating failed");
                    retrofitError.printStackTrace();
                    BeneficiaryListFragment.this.updateProcessing(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void success(ArrayList<BeneficiaryInfo> arrayList, Response response) {
                try {
                    BeneficiaryListFragment.this.updateProcessing(-1);
                    BMLApi.getInstance().getDataCache().setCacheItem("payments.beneficiaries", arrayList, false);
                    BeneficiaryListFragment.this.addCachedBeneficiariesList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getMerchants() {
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem("payments.merchants")) {
            updateProcessing(-1);
            addCachedMerchantList();
            return;
        }
        BMLApi.getClient().getMerchantList(new Callback<MerchantInfoList>() {
            public void failure(RetrofitError retrofitError) {
                Log.d(BeneficiaryListFragment.TAG, "updating failed cards");
                retrofitError.printStackTrace();
                BeneficiaryListFragment.this.updateProcessing(-1);
            }

            public void success(MerchantInfoList merchantInfoList, Response response) {
                BeneficiaryListFragment.this.updateProcessing(-1);
                BMLApi.getInstance().getDataCache().setCacheItem("payments.merchants", merchantInfoList, false);
                BeneficiaryListFragment.this.addCachedMerchantList();
            }
        });
    }

    public static BeneficiaryListFragment newInstance() {
        return new BeneficiaryListFragment();
    }

    public static BeneficiaryListFragment newInstance(AccountCommon accountCommon) {
        BeneficiaryListFragment beneficiaryListFragment = new BeneficiaryListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("param2", accountCommon);
        beneficiaryListFragment.setArguments(bundle);
        return beneficiaryListFragment;
    }

    public static BeneficiaryListFragment newInstance(boolean z) {
        BeneficiaryListFragment beneficiaryListFragment = new BeneficiaryListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_PAY_FLOW, z);
        beneficiaryListFragment.setArguments(bundle);
        return beneficiaryListFragment;
    }

    /* access modifiers changed from: protected */
    public void addAccountList(String str, ArrayList<Payable> arrayList) {
        if (this.mData.containsKey(str)) {
            ((ArrayList) this.mData.get(str)).addAll(arrayList);
        } else {
            this.mData.put(str, arrayList);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
        Payable payable = (Payable) this.mListView.getExpandableListAdapter().getChild(i, i2);
        if (this.pay_from == null && !this.flag_pay) {
            return false;
        }
        if (this.mListener != null) {
            this.mListener.onFragmentInteraction(PerformPaymentTransferFragment.newInstance(payable, this.pay_from), true);
        }
        return true;
    }

    public boolean onClose() {
        this.mAdapter.getFilter().filter(null);
        return false;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.pay_from = (AccountCommon) getArguments().getSerializable("param2");
            this.flag_pay = getArguments().getBoolean(ARG_PAY_FLOW, false);
        }
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_add_search, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), this);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_beneficiary_details, viewGroup, false);
        this.mSwipeLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_layout);
        this.mSwipeLayout.setOnRefreshListener(this);
        this.mListView = (ExpandableListView) inflate.findViewById(R.id.accounts_listview);
        if (this.pay_from != null || this.flag_pay) {
            this.mListView.addHeaderView(layoutInflater.inflate(R.layout.listitem_benefitiary_header, null, false), null, false);
        }
        this.mListView.setGroupIndicator(null);
        this.mData = new TreeMap<>(new TMComparator());
        this.mAdapter = new BeneficiariesAdapter(getActivity(), this.mData);
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setOnChildClickListener(this);
        this.mListView.setOnItemLongClickListener(this);
        return inflate;
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (ExpandableListView.getPackedPositionType(j) == 1) {
            Payable payable = (Payable) this.mListView.getExpandableListAdapter().getChild(ExpandableListView.getPackedPositionGroup(j), ExpandableListView.getPackedPositionChild(j));
            if ((payable instanceof BeneficiaryInfo) && !PaymentFactory.isOwnAccount(payable)) {
                final BeneficiaryInfo beneficiaryInfo = (BeneficiaryInfo) payable;
                ListSelectorDialog listSelectorDialog = new ListSelectorDialog();
                listSelectorDialog.setMethod(beneficiaryInfo.isLoanOrCard() ? 4 : 3);
                listSelectorDialog.setListener(new OnListSelectorSelected() {
                    public void onAccountSelected(AccountCommon accountCommon) {
                    }

                    public void onEditActionSelected(int i) {
                        OnFragmentInteractionListener onFragmentInteractionListener;
                        AddBeneficiaryFragment addBeneficiaryFragment;
                        if (i == 1) {
                            if (BeneficiaryListFragment.this.pay_from != null) {
                                onFragmentInteractionListener = BeneficiaryListFragment.this.mListener;
                                addBeneficiaryFragment = AddBeneficiaryFragment.newInstance(BeneficiaryListFragment.this.pay_from, beneficiaryInfo);
                            } else {
                                onFragmentInteractionListener = BeneficiaryListFragment.this.mListener;
                                addBeneficiaryFragment = AddBeneficiaryFragment.newInstance(BeneficiaryListFragment.this.flag_pay, beneficiaryInfo);
                            }
                            onFragmentInteractionListener.onFragmentInteraction(addBeneficiaryFragment, true);
                        } else if (i == 2) {
                            ClipboardManager clipboardManager = (ClipboardManager) BeneficiaryListFragment.this.getActivity().getSystemService("clipboard");
                            StringBuilder sb = new StringBuilder();
                            sb.append(beneficiaryInfo.beneficiaryAlias);
                            sb.append(" - ");
                            sb.append(beneficiaryInfo.accountNoCr);
                            clipboardManager.setPrimaryClip(ClipData.newPlainText("accountno", sb.toString()));
                            Toast.makeText(BeneficiaryListFragment.this.getActivity(), "Contact Copied", 0).show();
                        } else if (i == 3) {
                            BeneficiaryListFragment.this.deleteBeneficiary(beneficiaryInfo);
                        }
                    }

                    public void onOTPMethodSelected(String str) {
                    }

                    public void onPayableSelected(Payable payable) {
                    }

                    public void onRechargeAmountSelected(RechargeDenominations rechargeDenominations) {
                    }
                });
                listSelectorDialog.show(getActivity().getSupportFragmentManager(), TAG);
                return true;
            }
        }
        return false;
    }

    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        this.mAdapter.getFilter().filter(null);
        return true;
    }

    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        OnFragmentInteractionListener onFragmentInteractionListener;
        AddBeneficiaryFragment addBeneficiaryFragment;
        if (menuItem.getItemId() != R.id.action_add) {
            return super.onOptionsItemSelected(menuItem);
        }
        if (this.pay_from != null) {
            onFragmentInteractionListener = this.mListener;
            addBeneficiaryFragment = AddBeneficiaryFragment.newInstance(this.pay_from);
        } else {
            onFragmentInteractionListener = this.mListener;
            addBeneficiaryFragment = AddBeneficiaryFragment.newInstance(this.flag_pay);
        }
        onFragmentInteractionListener.onFragmentInteraction(addBeneficiaryFragment, true);
        return true;
    }

    public boolean onQueryTextChange(String str) {
        this.mAdapter.getFilter().filter(str);
        return false;
    }

    public boolean onQueryTextSubmit(String str) {
        return false;
    }

    public void onRefresh() {
        if (this.processing_request_count <= 0) {
            BMLApi.getInstance().getDataCache().invalidateCache("payments.merchants");
            BMLApi.getInstance().getDataCache().invalidateCache("payments.beneficiaries");
            reload_data();
        }
    }

    /* access modifiers changed from: protected */
    public void reload_data() {
        this.mData.clear();
        this.mAdapter.notifyDataSetChanged();
        BMLApi.getInstance().getDataCache().invalidateCache("payments.merchants");
        BMLApi.getInstance().getDataCache().invalidateCache("payments.beneficiaries");
        AccountCommon accountCommon = this.pay_from;
        if (accountCommon == null || (accountCommon instanceof AccountCASA)) {
            updateProcessing(2);
            getBeneficiaries();
            getMerchants();
            return;
        }
        updateProcessing(1);
        getBeneficiaries();
    }
}
