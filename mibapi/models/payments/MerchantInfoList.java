package mv.com.bml.mibapi.models.payments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MerchantInfoList {
    public ArrayList<MerchantInfo> merchantEnrolInfoList;

    private ArrayList<MerchantInfo> filterMerchants() {
        return this.merchantEnrolInfoList;
    }

    public HashMap<String, ArrayList<MerchantInfo>> getMerchantMap() {
        HashMap<String, ArrayList<MerchantInfo>> hashMap = new HashMap<>();
        Iterator it = filterMerchants().iterator();
        while (it.hasNext()) {
            MerchantInfo merchantInfo = (MerchantInfo) it.next();
            String str = merchantInfo.shelfDesc;
            if (str == null) {
                str = "Others";
            }
            if (!hashMap.containsKey(str)) {
                hashMap.put(str, new ArrayList());
            }
            ((ArrayList) hashMap.get(str)).add(merchantInfo);
        }
        return hashMap;
    }
}
