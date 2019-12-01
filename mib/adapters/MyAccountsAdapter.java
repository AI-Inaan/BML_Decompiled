package mv.com.bml.mib.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import mv.com.bml.mib.R;
import mv.com.bml.mib.models.HeaderItem;
import mv.com.bml.mib.utils.MiscUtils;
import mv.com.bml.mibapi.models.accounts.AccountCASA;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.accounts.AccountCredit;
import mv.com.bml.mibapi.models.accounts.AccountLoan;

public class MyAccountsAdapter extends ArrayAdapter<Object> {
    public MyAccountsAdapter(Context context, ArrayList<Object> arrayList) {
        super(context, 0, arrayList);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        int i2;
        Context context;
        Object item = getItem(i);
        if (item instanceof AccountCommon) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.listitem_account_item, viewGroup, false);
            TextView textView = (TextView) inflate.findViewById(R.id.text_account_name);
            TextView textView2 = (TextView) inflate.findViewById(R.id.text_account_amount);
            TextView textView3 = (TextView) inflate.findViewById(R.id.text_account_num);
            AccountCommon accountCommon = (AccountCommon) item;
            ((TextView) inflate.findViewById(R.id.text_account_currency)).setText(accountCommon.accountCcy);
            boolean z = item instanceof AccountCASA;
            int i3 = R.color.bml_red;
            if (z) {
                AccountCASA accountCASA = (AccountCASA) item;
                double d = accountCASA.available;
                textView.setText(accountCASA.accountName != null ? accountCASA.accountName : accountCASA.accountAlias);
                textView3.setText(accountCommon.accountNo);
                textView2.setText(accountCASA.formattedAvailable);
                context = getContext();
                if (d >= 0.0d) {
                    i3 = R.color.money_green;
                }
            } else if (item instanceof AccountLoan) {
                AccountLoan accountLoan = (AccountLoan) item;
                textView.setText(accountLoan.accountName != null ? accountLoan.accountName : accountLoan.accountAlias);
                textView3.setText(accountCommon.accountNo);
                textView2.setText(accountLoan.formattedOutstandingAmount);
                context = getContext();
            } else if (!(item instanceof AccountCredit)) {
                return inflate;
            } else {
                AccountCredit accountCredit = (AccountCredit) item;
                textView.setText(accountCredit.accountName != null ? accountCredit.accountName : accountCredit.accountAlias);
                StringBuilder sb = new StringBuilder();
                sb.append("*");
                sb.append(accountCredit.accountNo.substring(accountCredit.accountNo.length() - 4));
                textView3.setText(sb.toString());
                double d2 = (double) (accountCredit.usedAmount + accountCredit.outstandingAuthorization);
                textView2.setText(new DecimalFormat("#,###,###.##").format(d2));
                i2 = MiscUtils.getColor(getContext().getResources(), d2);
                textView2.setTextColor(i2);
                return inflate;
            }
            i2 = ContextCompat.getColor(context, i3);
            textView2.setTextColor(i2);
            return inflate;
        } else if (!(item instanceof HeaderItem)) {
            return new View(getContext());
        } else {
            View inflate2 = LayoutInflater.from(getContext()).inflate(R.layout.listitem_account_header, viewGroup, false);
            HeaderItem headerItem = (HeaderItem) item;
            ((TextView) inflate2.findViewById(R.id.text1)).setText(headerItem.section_title);
            if (!headerItem.hasSubtitle()) {
                return inflate2;
            }
            inflate2.findViewById(R.id.subheader_container).setVisibility(0);
            ((TextView) inflate2.findViewById(R.id.text_account_name)).setText(headerItem.subtitle);
            ((TextView) inflate2.findViewById(R.id.text_account_currency)).setText(headerItem.subtitle_sub1);
            ((TextView) inflate2.findViewById(R.id.text_account_amount)).setText(headerItem.subtitle_sub2);
            ((TextView) inflate2.findViewById(R.id.text_account_amount)).setTextColor(headerItem.subtitle_sub2_color);
            return inflate2;
        }
    }
}
