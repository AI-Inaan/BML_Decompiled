package mv.com.bml.mib.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import mv.com.bml.mib.R;

public class OTPSpinner extends ArrayAdapter<String> {
    public OTPSpinner(Context context, ArrayList<String> arrayList) {
        super(context, 0, arrayList);
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_otp_dropdown, viewGroup, false);
        }
        TextView textView = (TextView) view;
        textView.setText((String) getItem(i));
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_grey));
        return view;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_otp_dropdown, viewGroup, false);
        }
        TextView textView = (TextView) view;
        textView.setText((String) getItem(i));
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.secondary_blue));
        view.setPadding(view.getPaddingLeft(), 0, view.getPaddingRight(), 0);
        return view;
    }
}
