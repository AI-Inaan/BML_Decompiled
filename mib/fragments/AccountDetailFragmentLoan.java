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
import mv.com.bml.mibapi.models.accounts.AccountLoan;
import mv.com.bml.mibapi.models.accounts.AccountLoanDetails;
import mv.com.bml.mibapi.models.payments.MerchantInfo;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountDetailFragmentLoan extends Fragment implements OnRefreshListener, Titleable {
    @BindView(2131362062)
    TextView acc_curr;
    @BindView(2131362065)
    TextView acc_num;
    AccountLoan account;
    AccountLoan account_sum;
    @BindView(2131362060)
    TextView am_t;
    @BindView(2131361855)
    Button btn_pay_to_this;
    @BindView(2131361897)
    ListView list;
    @BindView(2131361898)
    ListView list2;
    private OnFragmentInteractionListener mListener;
    @BindView(2131362063)
    TextView tv_name;
    @BindView(2131362064)
    TextView tv_name2;

    private void initViewItems(View view) {
        this.tv_name2.setVisibility(0);
        this.btn_pay_to_this.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MerchantInfo merchantInfo = new MerchantInfo();
                merchantInfo.merchantEnrolId = AccountDetailFragmentLoan.this.account_sum.accountId;
                merchantInfo.merchantEnrolAlias = AccountDetailFragmentLoan.this.account_sum.accountAlias;
                merchantInfo.merchantEnrolType = "LOANS";
                merchantInfo.accountNumber = AccountDetailFragmentLoan.this.account_sum.accountNo;
                merchantInfo.imageUrl = AccountDetailFragmentLoan.this.account.image;
                merchantInfo.isOwnAccount = true;
                merchantInfo.currency = AccountDetailFragmentLoan.this.account_sum.accountCcy;
                Intent intent = new Intent(AccountDetailFragmentLoan.this.getActivity(), PayOrTransferActivity.class);
                intent.putExtra(PayOrTransferActivity.ARG_PAY_TO, merchantInfo);
                AccountDetailFragmentLoan.this.startActivity(intent);
            }
        });
    }

    public static AccountDetailFragmentLoan newInstance(AccountLoan accountLoan) {
        AccountDetailFragmentLoan accountDetailFragmentLoan = new AccountDetailFragmentLoan();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", accountLoan);
        accountDetailFragmentLoan.setArguments(bundle);
        return accountDetailFragmentLoan;
    }

    private void populateHead() {
        this.tv_name.setText(this.account.accountAlias != null ? this.account.accountAlias : this.account.accountName);
        this.tv_name2.setText(MiscUtils.capitalizeFully(this.account.productType));
        this.acc_num.setText(this.account.loanNo);
        this.am_t.setText(this.account.formattedOutstandingAmount);
        this.acc_curr.setText(this.account.loanCcy);
        this.am_t.setTextColor(ContextCompat.getColor(getContext(), R.color.bml_red));
    }

    /* access modifiers changed from: private */
    public void populateView() {
        populateHead();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AccDetailItem("Loan Amount", this.account.totLoanAmt, MiscUtils.getColor(getResources(), this.account.totLoanAmt)));
        arrayList.add(new AccDetailItem("Interest Rate", this.account.interestRate));
        arrayList.add(new AccDetailItem("Repayment Amount", this.account.repaymentAmt, MiscUtils.getColor(getResources(), this.account.repaymentAmt)));
        arrayList.add(new AccDetailItem("Start Date", this.account.startDt));
        arrayList.add(new AccDetailItem("End Date", this.account.endDt));
        arrayList.add(new AccDetailItem("Status", this.account.loanStatus));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new AccDetailItem("Outstanding Amount", this.account.formattedOutstandingAmount, ContextCompat.getColor(getContext(), R.color.primary_red)));
        arrayList2.add(new AccDetailItem("Overdue Amount", String.valueOf(this.account.overdueAmount), MiscUtils.getColor(getResources(), (double) this.account.overdueAmount)));
        arrayList2.add(new AccDetailItem("No. of Repayments Overdue", this.account.overdueCount));
        this.list.setAdapter(new AccountDetailItemAdapter(getActivity(), arrayList));
        this.list2.setAdapter(new AccountDetailItemAdapter(getActivity(), arrayList2));
        MiscUtils.setListViewHeightBasedOnChildren(this.list);
        MiscUtils.setListViewHeightBasedOnChildren(this.list2);
    }

    public String getTitle() {
        return "Details";
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(getString(R.string.loan_account));
        this.mListener = (OnFragmentInteractionListener) activity;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.account = (AccountLoan) getArguments().getSerializable("account");
        this.account_sum = this.account;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_account_loan, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        initViewItems(inflate);
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
        BMLApi.getClient().getLoanAccount(this.account.accountId, new Callback<AccountLoanDetails>() {
            public void failure(RetrofitError retrofitError) {
                try {
                    Toast.makeText(AccountDetailFragmentLoan.this.getActivity(), "Failed", 1).show();
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void success(AccountLoanDetails accountLoanDetails, Response response) {
                AccountDetailFragmentLoan.this.account = accountLoanDetails.loanAcct;
                AccountDetailFragmentLoan.this.getArguments().putSerializable("account", AccountDetailFragmentLoan.this.account);
                try {
                    AccountDetailFragmentLoan.this.populateView();
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
