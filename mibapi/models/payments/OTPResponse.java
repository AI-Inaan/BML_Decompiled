package mv.com.bml.mibapi.models.payments;

import java.util.HashMap;

public class OTPResponse {
    public boolean _2FAServiceResponse;
    public String process;
    public String result;
    public String resultCode;
    public HashMap<String, String> supplementaryData;
    public String twoFaMechanism;
    public boolean twoFaServiceResponse;
}
