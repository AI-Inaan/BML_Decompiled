package mv.com.bml.mibapi.models.payments.contacts;

public class CreateBen {
    public String accountNoCr;
    public String accountOptionCr = "O";
    public String accountOptionDesc;
    public String bankCode;
    public String bankDesc;
    public String beneficiaryAddr1;
    public String beneficiaryAddr2;
    public String beneficiaryAddr3;
    public String beneficiaryAlias;
    public String beneficiaryCcy;
    public String beneficiaryCorrBankAddr1;
    public String beneficiaryCorrBankAddr2;
    public String beneficiaryCorrBankAddr3;
    public String beneficiaryId;
    public String beneficiaryName;
    public String corrBankCode;
    public String corrBankDesc;
    public String imageUrl;
    public String shelfCode;
    public String spendCategoryId;
    public String spendSubCategoryId;

    public CreateBen(String str, String str2) {
        this.beneficiaryAlias = str;
        this.accountNoCr = str2;
        this.accountOptionDesc = "BML Account";
        this.spendCategoryId = null;
        this.spendSubCategoryId = null;
    }
}
