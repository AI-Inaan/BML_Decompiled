package mv.com.bml.mib.models;

public class AccDetailItem {
    public String detail;
    public int detail_color;
    public String title;

    public AccDetailItem(String str, String str2) {
        this.title = str;
        this.detail = str2;
    }

    public AccDetailItem(String str, String str2, int i) {
        this.title = str;
        this.detail = str2;
        this.detail_color = i;
    }

    public boolean has_color() {
        return this.detail_color != 0;
    }
}
