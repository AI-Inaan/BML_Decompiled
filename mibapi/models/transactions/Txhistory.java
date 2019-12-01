package mv.com.bml.mibapi.models.transactions;

import java.io.Serializable;
import java.util.ArrayList;

public class Txhistory {
    public ArrayList<TxItem> transactionHistoryList;

    public static class TxItem implements Serializable {
        public String accountCcy;
        public String accountName;
        public String accountNo;
        public String accountTranAmt;
        public String billAmount;
        public String billCurrency;
        public String channelRefNo;
        public String dueAmt;
        public String dueCcy;
        public String dueDt;
        public String dueType;
        public String postDt;
        public String reference;
        public String remarks;
        public String seqNo;
        public String tranAmount;
        public String tranCcy;
        public String tranDt;
        public String tranTypeDesc;
        public String txDescription;
        public String txDescription2;
        public String txDescription3;
        public String txDescription4;
    }
}
