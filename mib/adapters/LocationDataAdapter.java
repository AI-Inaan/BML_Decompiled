package mv.com.bml.mib.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import mv.com.bml.mib.R;
import mv.com.bml.mibapi.models.misc.LocationData;
import mv.com.bml.mibapi.models.misc.LocationData.LocationType;

public class LocationDataAdapter extends ArrayAdapter<LocationData> {
    public LocationDataAdapter(Context context, ArrayList<LocationData> arrayList) {
        super(context, 0, arrayList);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        int i2 = 0;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_location, viewGroup, false);
        }
        LocationData locationData = (LocationData) getItem(i);
        TextView textView = (TextView) view.findViewById(R.id.title);
        TextView textView2 = (TextView) view.findViewById(R.id.type_atm);
        TextView textView3 = (TextView) view.findViewById(R.id.type_branch);
        Iterator it = locationData.locationtype.iterator();
        boolean z = false;
        boolean z2 = false;
        while (it.hasNext()) {
            LocationType locationType = (LocationType) it.next();
            if (locationType.locationtype.equalsIgnoreCase("atm")) {
                z = true;
            }
            if (locationType.locationtype.equalsIgnoreCase("branch")) {
                z2 = true;
            }
        }
        textView2.setVisibility(z ? 0 : 8);
        if (!z2) {
            i2 = 8;
        }
        textView3.setVisibility(i2);
        textView.setText(locationData.name.replace("\n", ""));
        return view;
    }
}
