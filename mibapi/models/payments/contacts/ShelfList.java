package mv.com.bml.mibapi.models.payments.contacts;

import java.util.ArrayList;

public class ShelfList {
    public ArrayList<ShelfItem> shelfInfoList;

    public static class ShelfItem {
        public String recordStatus;
        public int shelfId;
        public String shelfName;
        public String shelfType;
    }
}
