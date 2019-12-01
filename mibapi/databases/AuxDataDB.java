package mv.com.bml.mibapi.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import mv.com.bml.mibapi.models.misc.ExchangeRateResponse.ExchangeCcy;
import mv.com.bml.mibapi.models.misc.LocationData;
import mv.com.bml.mibapi.models.misc.LocationData.ATM;
import mv.com.bml.mibapi.models.misc.LocationData.LocationType;

public class AuxDataDB extends SQLiteAssetHelper {
    private static final String DB_NAME = "bml_aux.db";
    private static final int DB_VERSION = 1;
    public static final String ENDPOINT_EXCHANGE = "exchange";
    public static final String ENDPOINT_LOCATIONS = "locations";
    private static final String TABLE_EXCHANGE = "currency_exchange";
    private static final String TABLE_LOC_ATM = "location_atm";
    private static final String TABLE_LOC_INFO = "location_info";
    private static final String TABLE_LOC_TYPE = "location_type";
    private static final String TABLE_UPDATE = "update_info";

    public AuxDataDB(Context context) {
        super(context, DB_NAME, null, 1);
        setForcedUpgrade(1);
    }

    public ArrayList<ExchangeCcy> getExchangeData() {
        ArrayList<ExchangeCcy> arrayList = new ArrayList<>();
        Cursor query = getReadableDatabase().query(TABLE_EXCHANGE, new String[]{"ccy", "ccyName", "market", "buyRate", "sellRate", "midRate"}, null, null, null, null, null, null);
        while (query.moveToNext()) {
            arrayList.add(new ExchangeCcy(query.getString(0), query.getString(1), query.getString(2), query.getDouble(3), query.getDouble(4), query.getDouble(5)));
        }
        query.close();
        return arrayList;
    }

    public int getLastUpdated(String str) {
        Cursor query = getReadableDatabase().query(TABLE_UPDATE, new String[]{"endpoint", "update_time"}, "endpoint=?", new String[]{str}, null, null, null, null);
        int i = query.moveToFirst() ? query.getInt(1) : 0;
        query.close();
        return i;
    }

    public ArrayList<ATM> getLocationATM(int i) {
        ArrayList<ATM> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("locationid=");
        sb.append(Integer.toString(i));
        String sb2 = sb.toString();
        Cursor query = readableDatabase.query(TABLE_LOC_ATM, new String[]{"id", "name", NotificationCompat.CATEGORY_STATUS, "locationid"}, sb2, null, null, null, null, null);
        while (query.moveToNext()) {
            arrayList.add(new ATM(query.getInt(0), query.getString(1), query.getString(2), query.getInt(3)));
        }
        query.close();
        return arrayList;
    }

    public ArrayList<LocationData> getLocationData() {
        ArrayList<LocationData> arrayList = new ArrayList<>();
        Cursor query = getReadableDatabase().query(TABLE_LOC_INFO, new String[]{"id", "name", "location", "address", "phone", "fax", "email"}, null, null, null, null, null, null);
        while (query.moveToNext()) {
            arrayList.add(new LocationData(query.getInt(0), query.getString(1), query.getString(2), query.getString(3), query.getString(4), query.getString(5), query.getString(6), getLocationType(query.getInt(0)), getLocationATM(query.getInt(0))));
        }
        query.close();
        return arrayList;
    }

    public ArrayList<LocationType> getLocationType(int i) {
        ArrayList<LocationType> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("locationid=");
        sb.append(Integer.toString(i));
        String sb2 = sb.toString();
        Cursor query = readableDatabase.query(TABLE_LOC_TYPE, new String[]{"locationid", "locationtype"}, sb2, null, null, null, null, null);
        while (query.moveToNext()) {
            arrayList.add(new LocationType(query.getInt(0), query.getString(1)));
        }
        query.close();
        return arrayList;
    }

    public void setLastUpdated(String str) {
        setLastUpdated(str, Integer.parseInt(new SimpleDateFormat("yyyyMMddHH").format(new Date())));
    }

    public void setLastUpdated(String str, int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("endpoint", str);
        contentValues.put("update_time", Integer.valueOf(i));
        writableDatabase.replace(TABLE_UPDATE, null, contentValues);
        writableDatabase.close();
    }

    public void updateExchangeData(ArrayList<ExchangeCcy> arrayList) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(TABLE_EXCHANGE, null, null);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ExchangeCcy exchangeCcy = (ExchangeCcy) it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ccy", exchangeCcy.ccy);
            contentValues.put("ccyName", exchangeCcy.ccyName);
            contentValues.put("market", exchangeCcy.market);
            contentValues.put("buyRate", Double.valueOf(exchangeCcy.buyRate));
            contentValues.put("sellRate", Double.valueOf(exchangeCcy.sellRate));
            contentValues.put("midRate", Double.valueOf(exchangeCcy.midRate));
            writableDatabase.insert(TABLE_EXCHANGE, null, contentValues);
        }
        writableDatabase.close();
    }

    public void updateLocationData(ArrayList<LocationData> arrayList) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(TABLE_LOC_INFO, null, null);
        writableDatabase.delete(TABLE_LOC_TYPE, null, null);
        writableDatabase.delete(TABLE_LOC_ATM, null, null);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            LocationData locationData = (LocationData) it.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", Integer.valueOf(locationData.id));
            contentValues.put("name", locationData.name);
            contentValues.put("location", locationData.location);
            contentValues.put("address", locationData.address);
            contentValues.put("phone", locationData.phone);
            contentValues.put("fax", locationData.fax);
            contentValues.put("email", locationData.email);
            writableDatabase.insert(TABLE_LOC_INFO, null, contentValues);
            Iterator it2 = locationData.locationtype.iterator();
            while (it2.hasNext()) {
                LocationType locationType = (LocationType) it2.next();
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("locationid", Integer.valueOf(locationType.locationid));
                contentValues2.put("locationtype", locationType.locationtype);
                writableDatabase.insert(TABLE_LOC_TYPE, null, contentValues2);
            }
            Iterator it3 = locationData.atm.iterator();
            while (it3.hasNext()) {
                ATM atm = (ATM) it3.next();
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("id", Integer.valueOf(atm.id));
                contentValues3.put("name", atm.name);
                contentValues3.put(NotificationCompat.CATEGORY_STATUS, atm.status);
                contentValues3.put("locationid", Integer.valueOf(atm.locationid));
                writableDatabase.insert(TABLE_LOC_ATM, null, contentValues3);
            }
        }
        writableDatabase.close();
    }
}
