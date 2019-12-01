package mv.com.bml.mibapi.models.payments;

public class OtherBank {
    public String address1;
    public String address2;
    public String address3;
    public String bic;
    public String clearingCode;
    public String clearingType;
    public String countryCode;
    public String institutionId;
    public String label;
    public String name;
    public String value;

    public String toString() {
        return this.label;
    }
}
