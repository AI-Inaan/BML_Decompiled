package mv.com.bml.mibapi.models.misc;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;

public class LocationData {
    public String address;
    public ArrayList<ATM> atm;
    public String email;
    public String fax;
    public int id;
    private double lat = 0.0d;
    private double lng = 0.0d;
    public String location;
    public ArrayList<LocationType> locationtype;
    public String name;
    public String phone;

    public static class ATM {
        public int id;
        public int locationid;
        public String name;
        public String status;

        public ATM(int i, String str, String str2, int i2) {
            this.id = i;
            this.name = str;
            this.status = str2;
            this.locationid = i2;
        }
    }

    public static class LocationType {
        public int locationid;
        public String locationtype;

        public LocationType(int i, String str) {
            this.locationid = i;
            this.locationtype = str;
        }
    }

    public LocationData(int i, String str, String str2, String str3, String str4, String str5, String str6, ArrayList<LocationType> arrayList, ArrayList<ATM> arrayList2) {
        this.id = i;
        this.name = str;
        this.location = str2;
        this.address = str3;
        this.phone = str4;
        this.fax = str5;
        this.email = str6;
        this.locationtype = arrayList;
        this.atm = arrayList2;
    }

    private void append(StringBuilder sb, String str) {
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            sb.append("\n");
        }
    }

    private void parseLatLng() {
        String[] split = this.location.trim().split(",");
        if (split.length == 2) {
            this.lat = Double.parseDouble(split[0].trim());
            this.lng = Double.parseDouble(split[1].trim());
        }
    }

    public double getLat() {
        if (this.lat == 0.0d) {
            parseLatLng();
        }
        return this.lat;
    }

    public double getLng() {
        if (this.lng == 0.0d) {
            parseLatLng();
        }
        return this.lng;
    }

    public String getSnippet() {
        StringBuilder sb = new StringBuilder();
        append(sb, this.address);
        append(sb, this.phone);
        append(sb, this.email);
        Iterator it = this.locationtype.iterator();
        while (it.hasNext()) {
            sb.append(((LocationType) it.next()).locationtype);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
