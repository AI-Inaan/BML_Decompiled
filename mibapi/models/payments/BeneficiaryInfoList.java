package mv.com.bml.mibapi.models.payments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BeneficiaryInfoList {
    public static ArrayList<BeneficiaryInfo> filterEligible(ArrayList<BeneficiaryInfo> arrayList) {
        return arrayList;
    }

    public static HashMap<String, ArrayList<BeneficiaryInfo>> sortByShelf(ArrayList<BeneficiaryInfo> arrayList) {
        HashMap<String, ArrayList<BeneficiaryInfo>> hashMap = new HashMap<>();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            BeneficiaryInfo beneficiaryInfo = (BeneficiaryInfo) it.next();
            String str = beneficiaryInfo.shelfDesc;
            if (str == null) {
                str = "My Accounts";
            }
            if (!hashMap.containsKey(str)) {
                hashMap.put(str, new ArrayList());
            }
            ((ArrayList) hashMap.get(str)).add(beneficiaryInfo);
        }
        return hashMap;
    }
}
