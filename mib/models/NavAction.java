package mv.com.bml.mib.models;

public class NavAction {
    public String action_data;
    public int action_type;
    public int image_res;
    public String title;

    public NavAction(int i, String str, int i2, String str2) {
        this.title = str;
        this.action_type = i2;
        this.action_data = str2;
        this.image_res = i;
    }

    public NavAction(String str, int i, String str2) {
        this.title = str;
        this.action_type = i;
        this.action_data = str2;
    }

    public String toString() {
        return this.title;
    }
}
