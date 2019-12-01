package mv.com.bml.mibapi.apievents;

import mv.com.bml.mibapi.models.payments.OTPResponse;

public class OTPRequestSent {
    public OTPResponse response;

    public OTPRequestSent(OTPResponse oTPResponse) {
        this.response = oTPResponse;
    }
}
