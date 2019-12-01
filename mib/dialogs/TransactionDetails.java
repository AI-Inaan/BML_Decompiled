package mv.com.bml.mib.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import mv.com.bml.mib.R;
import mv.com.bml.mib.adapters.AccountDetailItemAdapter;
import mv.com.bml.mib.utils.MiscUtils;
import mv.com.bml.mibapi.models.transactions.Txhistory.TxItem;

public class TransactionDetails extends DialogFragment {
    private static final String ARG_TRANSACTION_ITEM = "bflsdkjflkjdsf";
    private TxItem item;
    @BindView(2131361959)
    ListView list;

    public static TransactionDetails newInstance(TxItem txItem) {
        TransactionDetails transactionDetails = new TransactionDetails();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TRANSACTION_ITEM, txItem);
        transactionDetails.setArguments(bundle);
        return transactionDetails;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.item = (TxItem) getArguments().getSerializable(ARG_TRANSACTION_ITEM);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_transaction_details, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        getDialog().getWindow().requestFeature(1);
        ArrayList arrayList = new ArrayList();
        MiscUtils.addTxRowItem(arrayList, "Type", this.item.tranTypeDesc);
        String str = "Reference";
        MiscUtils.addTxRowItem(arrayList, str, this.item.reference);
        MiscUtils.addTxRowItem(arrayList, "Post Date", this.item.postDt);
        MiscUtils.addTxRowItem(arrayList, "Date", this.item.tranDt);
        MiscUtils.addTxRowItem(arrayList, str, this.item.channelRefNo);
        MiscUtils.addTxRowItem(arrayList, "Account", this.item.accountNo);
        String[] strArr = new String[2];
        strArr[0] = this.item.accountCcy != null ? this.item.accountCcy : this.item.tranCcy;
        strArr[1] = this.item.accountTranAmt != null ? this.item.accountTranAmt : this.item.tranAmount;
        MiscUtils.addTxRowItem(arrayList, "Amount", MiscUtils.nullFreeConcat(" ", strArr));
        MiscUtils.addTxRowItem(arrayList, "Description", MiscUtils.nullFreeConcat("\n", this.item.txDescription, this.item.txDescription2, this.item.txDescription3, this.item.txDescription4));
        this.list.setAdapter(new AccountDetailItemAdapter(getActivity(), arrayList));
        return inflate;
    }
}
