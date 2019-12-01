package mv.com.bml.mibapi.models.accounts;

import java.util.ArrayList;
import java.util.Iterator;

public class AccountCardList {
    public ArrayList<AccountCredit> cardList;
    public ArrayList<AccountCredit> suppCardList;

    public AccountCredit getCardAccount(String str) {
        Iterator it = this.cardList.iterator();
        while (it.hasNext()) {
            AccountCredit accountCredit = (AccountCredit) it.next();
            if (str.equalsIgnoreCase(accountCredit.accountNo)) {
                return accountCredit;
            }
        }
        return null;
    }

    public ArrayList<AccountCredit> getNonPrepaidCards() {
        ArrayList arrayList = new ArrayList();
        ArrayList<AccountCredit> arrayList2 = this.cardList;
        if (arrayList2 == null) {
            return arrayList;
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            AccountCredit accountCredit = (AccountCredit) it.next();
            if (!accountCredit.isPrepaid()) {
                arrayList.add(accountCredit);
            }
        }
        return arrayList;
    }

    public ArrayList<AccountCredit> getPrepaidCards() {
        ArrayList arrayList = new ArrayList();
        ArrayList<AccountCredit> arrayList2 = this.cardList;
        if (arrayList2 == null) {
            return arrayList;
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            AccountCredit accountCredit = (AccountCredit) it.next();
            if (accountCredit.isPrepaid()) {
                arrayList.add(accountCredit);
            }
        }
        return arrayList;
    }
}
