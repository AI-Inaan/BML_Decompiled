package mv.com.bml.mibapi.models.accounts;

import java.io.Serializable;

public abstract class AccountCommon implements Serializable {
    public String accountAlias;
    public String accountCcy;
    public String accountCcyDesc;
    public String accountDisplayName;
    public String accountId;
    public String accountName;
    public String accountNo;
    public String branch;
    public String branchDesc;
    public String icon;
    public String image;
    public String product;
    public String productClass;
    public String productName;
    public String productSubType;
    public String productType;

    public boolean isPrepaid() {
        String str = this.productType;
        if (str == null || !str.toLowerCase().contains("prepaid")) {
            String str2 = this.productName;
            if (str2 == null || !str2.toLowerCase().contains("prepaid")) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.accountNo);
        sb.append(" (");
        sb.append(this.accountCcy);
        sb.append(")");
        return sb.toString();
    }
}
