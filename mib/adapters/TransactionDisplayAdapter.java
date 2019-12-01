package mv.com.bml.mib.adapters;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.util.ArrayList;
import mv.com.bml.mib.R;
import mv.com.bml.mib.utils.MiscUtils;
import mv.com.bml.mibapi.models.transactions.Txhistory.TxItem;

public class TransactionDisplayAdapter extends Adapter<TransHolder> {
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_WHATEVER = 2;
    private ArrayList<TxItem> _data;
    private final View empty_view;
    private View footer_view;
    private View footer_view_ref;
    /* access modifiers changed from: private */
    public ClickHandler handler;
    private View header_view;
    private View header_view_ref;

    public interface ClickHandler {
        void onClick(View view, int i);
    }

    public class TransHolder extends ViewHolder implements OnClickListener {
        @BindView(2131362060)
        TextView amnt;
        @BindView(2131362062)
        TextView ccy;
        @BindView(2131362065)
        TextView date;
        @BindView(2131361904)
        TextView dot;
        @BindView(2131362063)
        TextView title;

        TransHolder(View view, boolean z) {
            super(view);
            if (!z) {
                ButterKnife.bind((Object) this, view);
                view.setOnClickListener(this);
            }
        }

        public void onClick(View view) {
            if (TransactionDisplayAdapter.this.handler != null) {
                TransactionDisplayAdapter.this.handler.onClick(view, getAdapterPosition() - 1);
            }
        }

        /* access modifiers changed from: 0000 */
        public void setView(TxItem txItem, Context context) {
            if (txItem.dueType != null) {
                this.dot.setVisibility(8);
                this.ccy.setText(txItem.dueCcy);
                this.title.setText(MiscUtils.capitalizeFully(txItem.dueType));
                TextView textView = this.date;
                StringBuilder sb = new StringBuilder();
                sb.append("Due: ");
                sb.append(txItem.dueDt);
                textView.setText(sb.toString());
                this.amnt.setText(txItem.dueAmt);
                this.amnt.setTextColor(ContextCompat.getColor(context, R.color.primary_red));
                this.dot.setTextColor(ContextCompat.getColor(context, R.color.primary_red));
                return;
            }
            TextView textView2 = this.ccy;
            String str = !TextUtils.isEmpty(txItem.billCurrency) ? txItem.billCurrency : !TextUtils.isEmpty(txItem.tranCcy) ? txItem.tranCcy : txItem.accountCcy;
            textView2.setText(str);
            this.title.setText(MiscUtils.capitalizeFully(txItem.tranTypeDesc != null ? txItem.tranTypeDesc : txItem.txDescription2));
            this.date.setText(txItem.tranDt);
            String str2 = txItem.billAmount != null ? txItem.billAmount : txItem.tranAmount != null ? txItem.tranAmount : txItem.accountTranAmt != null ? txItem.accountTranAmt : "0.00";
            double parseDouble = Double.parseDouble(str2.replaceAll("[^\\d.\\-]", ""));
            this.amnt.setTextColor(MiscUtils.getColor(context.getResources(), parseDouble));
            this.dot.setTextColor(MiscUtils.getColor(context.getResources(), parseDouble));
            this.amnt.setText(MiscUtils.formatCurrency(parseDouble));
        }
    }

    public class TransHolder_ViewBinding implements Unbinder {
        private TransHolder target;

        @UiThread
        public TransHolder_ViewBinding(TransHolder transHolder, View view) {
            this.target = transHolder;
            transHolder.dot = (TextView) Utils.findRequiredViewAsType(view, R.id.dot, "field 'dot'", TextView.class);
            transHolder.title = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_name, "field 'title'", TextView.class);
            transHolder.date = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_num, "field 'date'", TextView.class);
            transHolder.amnt = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_amount, "field 'amnt'", TextView.class);
            transHolder.ccy = (TextView) Utils.findRequiredViewAsType(view, R.id.text_account_currency, "field 'ccy'", TextView.class);
        }

        @CallSuper
        public void unbind() {
            TransHolder transHolder = this.target;
            if (transHolder != null) {
                this.target = null;
                transHolder.dot = null;
                transHolder.title = null;
                transHolder.date = null;
                transHolder.amnt = null;
                transHolder.ccy = null;
                return;
            }
            throw new IllegalStateException("Bindings already cleared.");
        }
    }

    public TransactionDisplayAdapter(Context context, ArrayList<TxItem> arrayList, View view, View view2) {
        this._data = arrayList;
        this.header_view = view;
        this.footer_view = view2;
        this.header_view_ref = view;
        this.footer_view_ref = view2;
        this.empty_view = new View(context);
    }

    public TxItem getItem(int i) {
        return (TxItem) this._data.get(i);
    }

    public int getItemCount() {
        return this._data.size() + 2;
    }

    public int getItemViewType(int i) {
        if (i == 0) {
            return 0;
        }
        return ((this._data.size() == 0 || i != this._data.size() + 1) && (this._data.size() != 0 || i <= 0)) ? 2 : 1;
    }

    public void onBindViewHolder(TransHolder transHolder, int i) {
        if (getItemViewType(i) == 2) {
            transHolder.setView(getItem(i - 1), transHolder.itemView.getContext());
        }
    }

    public TransHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return i == 0 ? new TransHolder(this.header_view, true) : i == 1 ? new TransHolder(this.footer_view, true) : new TransHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_transaction_detail, viewGroup, false), false);
    }

    public void setClickHandler(ClickHandler clickHandler) {
        this.handler = clickHandler;
    }

    public void setFooterVisible(boolean z) {
        this.footer_view = z ? this.footer_view_ref : this.empty_view;
    }

    public void setHeaderVisible(boolean z) {
        this.header_view = z ? this.header_view_ref : this.empty_view;
    }
}
