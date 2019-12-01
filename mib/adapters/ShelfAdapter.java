package mv.com.bml.mib.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import mv.com.bml.mib.R;
import mv.com.bml.mibapi.models.payments.contacts.ShelfList.ShelfItem;

public class ShelfAdapter extends ArrayAdapter<ShelfItem> {
    public ShelfAdapter(Context context, ArrayList<ShelfItem> arrayList) {
        super(context, 0, arrayList);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_shelf_item, viewGroup, false);
        }
        ((TextView) view).setText(((ShelfItem) getItem(i)).shelfName);
        return view;
    }
}
