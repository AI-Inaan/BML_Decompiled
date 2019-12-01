package mv.com.bml.mib.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import mv.com.bml.mib.R;
import mv.com.bml.mibapi.models.payments.Currency;

public class CurrencySpinnerAdapter extends ArrayAdapter<Currency> {
    public CurrencySpinnerAdapter(Context context, ArrayList<Currency> arrayList) {
        super(context, 0, arrayList);
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_currency_dropdown, viewGroup, false);
        }
        ((TextView) view).setText(((Currency) getItem(i)).code);
        return view;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_currency_dropdown, viewGroup, false);
        }
        ((TextView) view).setText(((Currency) getItem(i)).code);
        return view;
    }
}
