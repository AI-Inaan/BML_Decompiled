package mv.com.bml.mibapi.models.transactions;

import java.io.Serializable;
import java.util.ArrayList;

public class TxhistoryLocked {
    public int code;
    public String message;
    public ArrayList<TxItemLocked> payload;
    public boolean success;

    public static class TxItemLocked implements Serializable {
        public String Description;
        public String FromDate;
        public float LockedAmount;
        public String LockedID;
    }
}
