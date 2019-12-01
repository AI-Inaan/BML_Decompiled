package mv.com.bml.mibapi.models.payments;

import java.util.ArrayList;
import mv.com.bml.mibapi.models.misc.ValidationResult;

public class BeneficiaryInfo extends Payable {
    public String accountNoCr;
    public String accountOptionCr;
    public String accountOptionDesc;
    public String action;
    public String actionDesc;
    public String assignedCustomerNames;
    public String assignedCustomers;
    public String bankCity;
    public String bankCityDesc;
    public String bankClientCode;
    public String bankClientId;
    public String bankCode;
    public String bankCountry;
    public String bankCountryDesc;
    public String bankDesc;
    public String beneficiaryAddr1;
    public String beneficiaryAddr2;
    public String beneficiaryAddr3;
    public String beneficiaryAlias;
    public String beneficiaryBankAddr1;
    public String beneficiaryBankAddr2;
    public String beneficiaryBankAddr3;
    public String beneficiaryCcy;
    public String beneficiaryCcyDesc;
    public String beneficiaryCorrBankAddr1;
    public String beneficiaryCorrBankAddr2;
    public String beneficiaryCorrBankAddr3;
    public String beneficiaryEntityType;
    public String beneficiaryId;
    public String beneficiaryName;
    public String beneficiaryProduct;
    public String beneficiaryStatus;
    public String beneficiaryType;
    public String beneficiaryTypeDesc;
    public String channelRefNo;
    public String clearingCode;
    public String clearingCodeType;
    public String clientNo;
    public String companyList;
    public String corrBankCode;
    public String corrBankDesc;
    public String crudOperation;
    public ArrayList<String> dataObjectKeyMethods;
    public String entityTypeDiscriminator;
    public boolean fundTransferDisabled;
    public String image;
    public String imageString;
    public String message;
    public String responseCode;
    public String shelfCode;
    public String shelfDesc;
    public String shelfToDisplay;
    public String spendCategoryDesc;
    public String spendCategoryId;
    public String spendSubCategoryDesc;
    public String spendSubCategoryId;
    public String status;
    public String userName;
    public String validateAcctFromHost;
    public ArrayList<ValidationResult> validationResultList;

    public boolean isLoanOrCard() {
        String str = this.shelfDesc;
        return str != null && (str.toLowerCase().endsWith("cards") || this.shelfDesc.toLowerCase().endsWith("loans"));
    }
}
