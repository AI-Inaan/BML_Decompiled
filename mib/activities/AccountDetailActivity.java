package mv.com.bml.mib.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import mv.com.bml.mib.R;
import mv.com.bml.mib.fragments.AccountDetailFragmentCASA;
import mv.com.bml.mib.fragments.AccountDetailFragmentCredit;
import mv.com.bml.mib.fragments.AccountDetailFragmentLoan;
import mv.com.bml.mib.fragments.TxHistoryFragment;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mibapi.models.accounts.AccountCASA;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.accounts.AccountLoan;

public class AccountDetailActivity extends BasePinCodeActivity implements OnFragmentInteractionListener {
    public static final String ARG_ACCOUNT_OBJECT = "com.bml.account_object_var";
    /* access modifiers changed from: private */
    public ArrayList<Fragment> mData;
    /* access modifiers changed from: private */
    public ArrayList<String> mTitles;
    @BindView(2131361882)
    ViewPager mViewPager;
    @BindView(2131362051)
    TabLayout tabs;

    public class TwoItemPager extends FragmentStatePagerAdapter {
        public TwoItemPager() {
            super(AccountDetailActivity.this.getSupportFragmentManager());
        }

        public int getCount() {
            return AccountDetailActivity.this.mData.size();
        }

        public Fragment getItem(int i) {
            return (Fragment) AccountDetailActivity.this.mData.get(i);
        }

        public CharSequence getPageTitle(int i) {
            return (CharSequence) AccountDetailActivity.this.mTitles.get(i);
        }
    }

    /* access modifiers changed from: protected */
    public int getThemeEliteTheme() {
        return R.style.AppTheme_NoActionBar_NotHorrible;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        ArrayList<Fragment> arrayList;
        Object newInstance;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_account_details);
        ButterKnife.bind((Activity) this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mData = new ArrayList<>();
        this.mTitles = new ArrayList<>();
        TwoItemPager twoItemPager = new TwoItemPager();
        AccountCommon accountCommon = (AccountCommon) getIntent().getSerializableExtra(ARG_ACCOUNT_OBJECT);
        String str = "History";
        String str2 = "Details";
        if (accountCommon instanceof AccountCASA) {
            this.mTitles.add(str2);
            this.mData.add(AccountDetailFragmentCASA.newInstance((AccountCASA) accountCommon));
            this.mTitles.add(str);
            this.mData.add(TxHistoryFragment.newInstance(accountCommon));
            this.mTitles.add("Pending");
            arrayList = this.mData;
            newInstance = TxHistoryFragment.newInstance(accountCommon, 3);
        } else if (accountCommon instanceof AccountCredit) {
            setTitle("Credit Card");
            this.mTitles.add(str2);
            this.mData.add(AccountDetailFragmentCredit.newInstance((AccountCredit) accountCommon));
            this.mTitles.add(getString(R.string.account_header_view_pending));
            this.mData.add(TxHistoryFragment.newInstance(accountCommon, 2));
            this.mTitles.add(getString(accountCommon.isPrepaid() ? R.string.account_header_view_unbilled_prepaid : R.string.account_header_view_unbilled));
            this.mData.add(TxHistoryFragment.newInstance(accountCommon, 1));
            this.mTitles.add(str);
            arrayList = this.mData;
            newInstance = TxHistoryFragment.newInstance(accountCommon);
        } else {
            if (accountCommon instanceof AccountLoan) {
                setTitle("Loan");
                this.mTitles.add(str2);
                arrayList = this.mData;
                newInstance = AccountDetailFragmentLoan.newInstance((AccountLoan) accountCommon);
            }
            this.mViewPager.setAdapter(twoItemPager);
            this.tabs.setupWithViewPager(this.mViewPager);
        }
        arrayList.add(newInstance);
        this.mViewPager.setAdapter(twoItemPager);
        this.tabs.setupWithViewPager(this.mViewPager);
    }

    public void onFragmentInteraction(Fragment fragment, boolean z) {
        this.mViewPager.setCurrentItem(1);
    }
}
