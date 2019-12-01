package mv.com.bml.mibapi.models.payments.mobile;

import java.io.Serializable;

public class RechargeDenominations implements Serializable {
    public String ccy;
    public String denomination;
    public String gstAmount;
    public String merchantId;
    public String mobileRechargeId;
    public String totalAmount;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.denomination);
        sb.append(" ");
        sb.append(this.ccy);
        return sb.toString();
    }
}
