package mv.com.bml.mib.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.payments.MerchantInfo;

public class AccountDetailFragmentCredit extends Fragment implements Titleable {
    @BindView(2131362062)
    TextView acc_curr;
    @BindView(2131362065)
    TextView acc_num;
    AccountCredit account;
    @BindView(2131362060)
    TextView am_t;
    @BindView(2131361854)
    Button btn_pay_to_this;
    @BindView(2131361897)
    ListView list;
    @BindView(2131361898)
    ListView list2;
    private OnFragmentInteractionListener mListener;
    @BindView(2131361862)
    Button make_transfer;
    @BindView(2131362073)
    TextView title_list2;
    @BindView(2131362063)
    TextView tv_name;
    @BindView(2131362064)
    TextView tv_name2;

    private void initViewItems() {
        this.tv_name2.setVisibility(0);
        this.make_transfer.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AccountDetailFragmentCredit.this.getActivity(), PayOrTransferActivity.class);
                intent.putExtra(PayOrTransferActivity.ARG_PAY_FROM, AccountDetailFragmentCredit.this.account);
                AccountDetailFragmentCredit.this.startActivity(intent);
                AccountDetailFragmentCredit.this.getActivity().finish();
            }
        });
        this.btn_pay_to_this.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MerchantInfo merchantInfo = new MerchantInfo();
                merchantInfo.merchantEnrolId = AccountDetailFragmentCredit.this.account.accountId;
                merchantInfo.merchantEnrolAlias = AccountDetailFragmentCredit.this.account.accountAlias;
                merchantInfo.merchantEnrolType = AccountDetailFragmentCredit.this.account.isPrepaid() ? "PREPAID_CARD" : "CREDIT_CARD";
                merchantInfo.accountNumber = AccountDetailFragmentCredit.this.account.accountNo;
                merchantInfo.imageUrl = AccountDetailFragmentCredit.this.account.icon;
                merchantInfo.isOwnAccount = true;
                merchantInfo.currency = AccountDetailFragmentCredit.this.account.accountCcy;
                Intent intent = new Intent(AccountDetailFragmentCredit.this.getActivity(), PayOrTransferActivity.class);
                intent.putExtra(PayOrTransferActivity.ARG_PAY_TO, merchantInfo);
                AccountDetailFragmentCredit.this.startActivity(intent);
                AccountDetailFragmentCredit.this.getActivity().finish();
            }
        });
    }

    public static AccountDetailFragmentCredit newInstance(AccountCredit accountCredit) {
        AccountDetailFragmentCredit accountDetailFragmentCredit = new AccountDetailFragmentCredit();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", accountCredit);
        accountDetailFragmentCredit.setArguments(bundle);
        return accountDetailFragmentCredit;
    }

    private void populateHead() {
        double d = (double) (this.account.usedAmount + this.account.outstandingAuthorization);
        this.tv_name.setText(this.account.accountAlias != null ? this.account.accountAlias : this.account.accountName);
        this.tv_name2.setText(MiscUtils.capitalizeFully(this.account.productName));
        this.acc_num.setText(MiscUtils.maskAccNum(this.account.accountNo));
        this.am_t.setText(MiscUtils.formatCurrency(d));
        this.acc_curr.setText(this.account.accountCcy);
        this.am_t.setTextColor(ContextCompat.getColor(getContext(), d >= 0.0d ? R.color.money_green : R.color.bml_red));
    }

    private void populateView() {
        populateHead();
        ArrayList arrayList = new ArrayList();
        if (!this.account.isPrepaid()) {
            arrayList.add(new AccDetailItem("Credit Limit (All Cards)", this.account.formattedCreditLimit, MiscUtils.getColor(getResources(), (double) this.account.creditLimit)));
            arrayList.add(new AccDetailItem("Current Balance", this.account.formattedUsedAmount, MiscUtils.getColor(getResources(), (double) this.account.usedAmount)));
        }
        arrayList.add(new AccDetailItem("Net Authorisations", this.account.formattedOutstandingAuthorization, MiscUtils.getColor(getResources(), (double) this.account.outstandingAuthorization)));
        double d = (double) this.account.availableLimit;
        arrayList.add(new AccDetailItem("Available Amount", MiscUtils.formatCurrency(d), MiscUtils.getColor(getResources(), d)));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new AccDetailItem("Balance", this.account.formattedTotalAmtDue, MiscUtils.getColor(getResources(), (double) this.account.totalAmtDue)));
        arrayList2.add(new AccDetailItem("Minimum Payment", this.account.formattedMinAmtDue, MiscUtils.getColor(getResources(), (double) this.account.minAmtDue)));
        arrayList2.add(new AccDetailItem("Due Date", this.account.dueDate));
        this.list.setAdapter(new AccountDetailItemAdapter(getActivity(), arrayList));
        this.list2.setAdapter(new AccountDetailItemAdapter(getActivity(), arrayList2));
        if (this.account.isPrepaid()) {
            this.list2.setVisibility(8);
            this.title_list2.setVisibility(8);
            this.make_transfer.setVisibility(8);
            this.btn_pay_to_this.setText(R.string.topup_card);
            try {
                getActivity().setTitle(R.string.prepaid_account);
            } catch (Exception unused) {
            }
        }
        MiscUtils.setListViewHeightBasedOnChildren(this.list);
        MiscUtils.setListViewHeightBasedOnChildren(this.list2);
    }

    public String getTitle() {
        return "Details";
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(getString(R.string.credit_account));
        this.mListener = (OnFragmentInteractionListener) activity;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.account = (AccountCredit) getArguments().getSerializable("account");
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_account_credit, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        initViewItems();
        populateView();
        return inflate;
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}
