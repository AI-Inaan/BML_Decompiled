package mv.com.bml.mibapi.models.payments;

import java.io.Serializable;

public class Currency implements Serializable {
    public String ccyPrecision;
    public String code;
    public String format;
    public String icon;
    public String name;
    public String roundTrunc;

    public Currency() {
    }

    public Currency(String str) {
        this.code = str;
    }

    public Currency(String str, String str2) {
        this.code = str;
        this.name = str2;
    }

    public String toString() {
        return String.format("%s (%s)", new Object[]{this.name, this.code.toUpperCase()});
    }
}
