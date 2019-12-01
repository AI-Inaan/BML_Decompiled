package mv.com.bml.mibapi.models.payments;

import java.io.Serializable;
import java.util.ArrayList;
import mv.com.bml.mibapi.models.misc.ValidationResult;

public class Transfer implements Serializable {
    public double availableBalance;
    public String availableBalanceCcy;
    public String bankClientId;
    public String baseTransferActivity;
    public String channelRefNo;
    public String crudOperation = "QUERY";
    public ArrayList<String> dataObjectKeyMethods;
    public String drawnFromAccount;
    public String formattedAvailableBalance;
    public String message = "";
    public String responseCode;
    public String status = "SUCCESS";
    public ArrayList<TransferActivity> transferActivityList = new ArrayList<>();
    public String userName;
    public ArrayList<ValidationResult> validationResultList = new ArrayList<>();

    public Transfer() {
        TransferActivity transferActivity = new TransferActivity();
        transferActivity.message = "";
        transferActivity.paymentDetails = "";
        transferActivity.remarks = "";
        transferActivity.validationResultList = new ArrayList<>();
        this.transferActivityList.add(transferActivity);
    }
}
