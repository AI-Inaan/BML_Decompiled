package mv.com.bml.mib.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import java.util.ArrayList;
import mv.com.bml.mib.R;
import mv.com.bml.mib.activities.PayOrTransferActivity;
import mv.com.bml.mib.adapters.AccountDetailItemAdapter;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mib.interfaces.Titleable;
import mv.com.bml.mib.models.AccDetailItem;
import mv.com.bml.mib.utils.MiscUtils;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.accounts.AccountCASA;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountDetailFragmentCASA extends Fragment implements OnRefreshListener, Titleable {
    @BindView(2131362062)
    TextView acc_curr;
    @BindView(2131362065)
    TextView acc_num;
    AccountCASA account;
    @BindView(2131362060)
    TextView am_t;
    @BindView(2131361897)
    ListView list;
    private OnFragmentInteractionListener mListener;
    @BindView(2131361862)
    Button make_transfer;
    @BindView(2131362063)
    TextView tv_name;

    public static AccountDetailFragmentCASA newInstance(AccountCASA accountCASA) {
        AccountDetailFragmentCASA accountDetailFragmentCASA = new AccountDetailFragmentCASA();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", accountCASA);
        accountDetailFragmentCASA.setArguments(bundle);
        return accountDetailFragmentCASA;
    }

    private void populateHead() {
        double d = this.account.formattedAvailableBalance != null ? this.account.availableBalance : this.account.available;
        this.tv_name.setText(this.account.accountAlias != null ? this.account.accountAlias : this.account.accountName);
        this.acc_num.setText(this.account.accountNo);
        this.am_t.setText(this.account.formattedAvailableBalance != null ? this.account.formattedAvailableBalance : this.account.formattedAvailable);
        this.acc_curr.setText(this.account.accountCcy);
        this.am_t.setTextColor(ContextCompat.getColor(getContext(), d >= 0.0d ? R.color.money_green : R.color.bml_red));
    }

    /* access modifiers changed from: private */
    public void populateView() {
        populateHead();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AccDetailItem("Branch", this.account.branchDesc));
        arrayList.add(new AccDetailItem("Reserved Amount", this.account.totalReserveAmount));
        arrayList.add(new AccDetailItem("Uncleared Amount", this.account.totalFloatAmount));
        arrayList.add(new AccDetailItem("Balance As Of", this.account.balanceAsOf));
        this.list.setAdapter(new AccountDetailItemAdapter(getActivity(), arrayList));
        MiscUtils.setListViewHeightBasedOnChildren(this.list);
    }

    public String getTitle() {
        return "Details";
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(getString(R.string.current_and_savings));
        this.mListener = (OnFragmentInteractionListener) activity;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.account = (AccountCASA) getArguments().getSerializable("account");
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_account_casa, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.make_transfer.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AccountDetailFragmentCASA.this.getActivity(), PayOrTransferActivity.class);
                intent.putExtra(PayOrTransferActivity.ARG_PAY_FROM, AccountDetailFragmentCASA.this.account);
                AccountDetailFragmentCASA.this.startActivity(intent);
                AccountDetailFragmentCASA.this.getActivity().finish();
            }
        });
        populateHead();
        onRefresh();
        return inflate;
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onRefresh() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        BMLApi.getClient().getCASAAccount(this.account.accountId, new Callback<AccountCASA>() {
            public void failure(RetrofitError retrofitError) {
                try {
                    Toast.makeText(AccountDetailFragmentCASA.this.getActivity(), "Failed", 1).show();
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void success(AccountCASA accountCASA, Response response) {
                AccountDetailFragmentCASA accountDetailFragmentCASA = AccountDetailFragmentCASA.this;
                accountDetailFragmentCASA.account = accountCASA;
                try {
                    accountDetailFragmentCASA.populateView();
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
