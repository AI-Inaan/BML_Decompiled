package mv.com.bml.mibapi.models.misc;

import java.io.Serializable;
import java.util.ArrayList;

public class ValidationResult implements Serializable {
    public String boundToItem;
    public ArrayList<String> messages;
    public String status;
}
