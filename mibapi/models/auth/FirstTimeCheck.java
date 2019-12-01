package mv.com.bml.mibapi.models.auth;

import java.util.ArrayList;
import mv.com.bml.mibapi.models.misc.ValidationResult;

public class FirstTimeCheck {
    public boolean firstTimeLogin;
    public boolean loginUserIdChangeMandatory;
    public boolean passwordExpired;
    public String status;
    public boolean twoFaChallengeRequired;
    public ArrayList<ValidationResult> validationResultList;
}
