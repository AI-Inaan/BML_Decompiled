package mv.com.bml.mibapi.models.payments;

import java.io.Serializable;
import java.util.ArrayList;
import mv.com.bml.mibapi.models.misc.ValidationResult;

public class Payment implements Serializable {
    public String availableBalance;
    public String availableBalanceCcy;
    public String bankClientId;
    public String basePaymentActivity;
    public String channelRefNo;
    public String crudOperation = "QUERY";
    public ArrayList<String> dataObjectKeyMethods;
    public String drawnFromAccount;
    public String formattedAvailableBalance;
    public String message = "";
    public ArrayList<PaymentActivity> paymentActivityList = new ArrayList<>();
    public String responseCode;
    public String status = "SUCCESS";
    public String userName;
    public ArrayList<ValidationResult> validationResultList = new ArrayList<>();

    public Payment() {
        PaymentActivity paymentActivity = new PaymentActivity();
        paymentActivity.message = "";
        paymentActivity.remarks = "";
        paymentActivity.validationResultList = new ArrayList<>();
        paymentActivity.dataObjectKeyMethods = new ArrayList<>();
        this.paymentActivityList.add(paymentActivity);
    }
}
