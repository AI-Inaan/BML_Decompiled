package mv.com.bml.mib.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import mv.com.bml.mib.R;
import mv.com.bml.mibapi.models.misc.ExchangeRateResponse.ExchangeCcy;

public class ExchangeRateAdapter extends ArrayAdapter<ExchangeCcy> {
    public ExchangeRateAdapter(Context context, ArrayList<ExchangeCcy> arrayList) {
        super(context, R.layout.listitem_converter_item, arrayList);
    }

    public int getCcyFlagGraphic(String str) {
        try {
            return getContext().getResources().getIdentifier(str.toLowerCase(), "drawable", getContext().getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return R.drawable.ic_add;
        }
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.listitem_converter_item, viewGroup, false);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.flag_img);
        TextView textView = (TextView) view.findViewById(R.id.ccy_code);
        TextView textView2 = (TextView) view.findViewById(R.id.ccy_buy);
        TextView textView3 = (TextView) view.findViewById(R.id.ccy_sell);
        ExchangeCcy exchangeCcy = (ExchangeCcy) getItem(i);
        ((TextView) view.findViewById(R.id.ccy_name)).setText(exchangeCcy.ccyName);
        textView.setText(exchangeCcy.ccy);
        DecimalFormat decimalFormat = new DecimalFormat("00.0000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_DOWN);
        imageView.setImageResource(getCcyFlagGraphic(exchangeCcy.ccy));
        textView2.setText(decimalFormat.format(exchangeCcy.buyRate));
        textView3.setText(decimalFormat.format(exchangeCcy.sellRate));
        return view;
    }
}
