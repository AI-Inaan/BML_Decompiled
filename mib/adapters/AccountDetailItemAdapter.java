package mv.com.bml.mib.adapters;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.util.ArrayList;
import mv.com.bml.mib.R;
import mv.com.bml.mib.models.AccDetailItem;

public class AccountDetailItemAdapter extends ArrayAdapter<AccDetailItem> {

    static class AccountHolder {
        @BindView(2131362056)
        TextView detail;
        @BindView(2131362055)
        TextView title;

        public AccountHolder(View view) {
            ButterKnife.bind((Object) this, view);
        }
    }

    public class AccountHolder_ViewBinding implements Unbinder {
        private AccountHolder target;

        @UiThread
        public AccountHolder_ViewBinding(AccountHolder accountHolder, View view) {
            this.target = accountHolder;
            accountHolder.title = (TextView) Utils.findRequiredViewAsType(view, R.id.text1, "field 'title'", TextView.class);
            accountHolder.detail = (TextView) Utils.findRequiredViewAsType(view, R.id.text2, "field 'detail'", TextView.class);
        }

        @CallSuper
        public void unbind() {
            AccountHolder accountHolder = this.target;
            if (accountHolder != null) {
                this.target = null;
                accountHolder.title = null;
                accountHolder.detail = null;
                return;
            }
            throw new IllegalStateException("Bindings already cleared.");
        }
    }

    public AccountDetailItemAdapter(Context context, ArrayList<AccDetailItem> arrayList) {
        super(context, R.layout.listitem_account_detail, arrayList);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        AccountHolder accountHolder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_account_detail, viewGroup, false);
            accountHolder = new AccountHolder(view);
            view.setTag(accountHolder);
        } else {
            accountHolder = (AccountHolder) view.getTag();
        }
        AccDetailItem accDetailItem = (AccDetailItem) getItem(i);
        accountHolder.title.setText(accDetailItem.title);
        accountHolder.detail.setText(Html.fromHtml(accDetailItem.detail != null ? accDetailItem.detail : ""));
        if (accDetailItem.has_color()) {
            accountHolder.detail.setTextColor(accDetailItem.detail_color);
        } else {
            accountHolder.detail.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_grey));
        }
        return view;
    }
}
