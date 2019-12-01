package mv.com.bml.mibapi.models.misc;

import java.util.ArrayList;

public class ExchangeRateResponse {
    public String lud;
    public ArrayList<ExchangeCcy> rates;

    public static class ExchangeCcy {
        public double buyRate;
        public String ccy;
        public String ccyName;
        public String market;
        public double midRate;
        public double sellRate;

        public ExchangeCcy(String str, String str2, String str3, double d, double d2, double d3) {
            this.ccy = str;
            this.ccyName = str2;
            this.market = str3;
            this.buyRate = d;
            this.sellRate = d2;
            this.midRate = d3;
        }
    }
}
