package mv.com.bml.mib.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import mv.com.bml.mib.R;
import mv.com.bml.mib.adapters.ExchangeRateAdapter;
import mv.com.bml.mibapi.databases.AuxDataDB;
import mv.com.bml.mibapi.models.misc.ExchangeRateResponse.ExchangeCcy;

public class ExchangeRateActivity extends BasePinCodeActivity {
    EditText from_edit;
    @BindView(2131361870)
    View from_view;
    /* access modifiers changed from: private */
    public ArrayList<ExchangeCcy> mData;
    @BindView(2131361924)
    ListView mListView;
    /* access modifiers changed from: private */
    public ExchangeCcy to_ccy;
    EditText to_edit;
    @BindView(2131361873)
    View to_view;
    /* access modifiers changed from: private */
    public TextWatcher tw_from;
    /* access modifiers changed from: private */
    public TextWatcher tw_to;

    /* access modifiers changed from: private */
    public void calculate_from() {
        String obj = this.to_edit.getText().toString();
        if (TextUtils.isEmpty(obj) || obj.equals(".")) {
            obj = "0.00";
        }
        double parseDouble = Double.parseDouble(obj) * this.to_ccy.sellRate;
        this.from_edit.setText(String.format("%.2f", new Object[]{Double.valueOf(parseDouble)}));
    }

    /* access modifiers changed from: private */
    public void calculate_to() {
        String obj = this.from_edit.getText().toString();
        if (TextUtils.isEmpty(obj) || obj.equals(".")) {
            obj = "0.00";
        }
        double parseDouble = Double.parseDouble(obj) / this.to_ccy.sellRate;
        this.to_edit.setText(String.format("%.2f", new Object[]{Double.valueOf(parseDouble)}));
    }

    /* access modifiers changed from: private */
    public void setCurrencyView(View view, ExchangeCcy exchangeCcy) {
        ((ImageView) view.findViewById(R.id.flag_img)).setImageResource(getCcyFlagGraphic(exchangeCcy.ccy));
        ((TextView) view.findViewById(R.id.ccy_name)).setText(exchangeCcy.ccy);
    }

    /* access modifiers changed from: private */
    public void setSelect(View view, View view2) {
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.secondary_grey_light_darker));
        view2.setBackgroundColor(ContextCompat.getColor(this, 17170445));
        EditText editText = (EditText) view.findViewById(R.id.ccy_amnt);
        editText.setText("");
        if (editText.requestFocus()) {
            getWindow().setSoftInputMode(5);
        }
    }

    public int getCcyFlagGraphic(String str) {
        try {
            return getResources().getIdentifier(str.toLowerCase(), "drawable", getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return R.drawable.ic_add;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_exchange_rate);
        ButterKnife.bind((Activity) this);
        this.from_edit = (EditText) ButterKnife.findById(this.from_view, (int) R.id.ccy_amnt);
        this.to_edit = (EditText) ButterKnife.findById(this.to_view, (int) R.id.ccy_amnt);
        this.from_view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExchangeRateActivity exchangeRateActivity = ExchangeRateActivity.this;
                exchangeRateActivity.setSelect(exchangeRateActivity.from_view, ExchangeRateActivity.this.to_view);
            }
        });
        this.from_edit.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    ExchangeRateActivity exchangeRateActivity = ExchangeRateActivity.this;
                    exchangeRateActivity.setSelect(exchangeRateActivity.from_view, ExchangeRateActivity.this.to_view);
                }
            }
        });
        this.to_view.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExchangeRateActivity exchangeRateActivity = ExchangeRateActivity.this;
                exchangeRateActivity.setSelect(exchangeRateActivity.to_view, ExchangeRateActivity.this.from_view);
            }
        });
        this.to_edit.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    ExchangeRateActivity exchangeRateActivity = ExchangeRateActivity.this;
                    exchangeRateActivity.setSelect(exchangeRateActivity.to_view, ExchangeRateActivity.this.from_view);
                }
            }
        });
        this.tw_from = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                ExchangeRateActivity.this.to_edit.removeTextChangedListener(ExchangeRateActivity.this.tw_to);
                ExchangeRateActivity.this.calculate_to();
                ExchangeRateActivity.this.to_edit.addTextChangedListener(ExchangeRateActivity.this.tw_to);
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        };
        this.tw_to = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                ExchangeRateActivity.this.from_edit.removeTextChangedListener(ExchangeRateActivity.this.tw_from);
                ExchangeRateActivity.this.calculate_from();
                ExchangeRateActivity.this.from_edit.addTextChangedListener(ExchangeRateActivity.this.tw_from);
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        };
        this.from_edit.addTextChangedListener(this.tw_from);
        this.to_edit.addTextChangedListener(this.tw_to);
        this.mData = new AuxDataDB(this).getExchangeData();
        Collections.sort(this.mData, new Comparator<ExchangeCcy>() {
            public int compare(ExchangeCcy exchangeCcy, ExchangeCcy exchangeCcy2) {
                String str = "usd";
                if (exchangeCcy.ccy.equalsIgnoreCase(str)) {
                    return -1;
                }
                if (exchangeCcy2.ccy.equalsIgnoreCase(str)) {
                    return 1;
                }
                return exchangeCcy.ccyName.compareTo(exchangeCcy2.ccyName);
            }
        });
        this.mListView.setAdapter(new ExchangeRateAdapter(this, this.mData));
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                ExchangeRateActivity exchangeRateActivity = ExchangeRateActivity.this;
                exchangeRateActivity.to_ccy = (ExchangeCcy) exchangeRateActivity.mData.get(i);
                ExchangeRateActivity exchangeRateActivity2 = ExchangeRateActivity.this;
                exchangeRateActivity2.setCurrencyView(exchangeRateActivity2.to_view, ExchangeRateActivity.this.to_ccy);
                ExchangeRateActivity.this.to_edit.removeTextChangedListener(ExchangeRateActivity.this.tw_to);
                ExchangeRateActivity.this.calculate_to();
                ExchangeRateActivity.this.to_edit.addTextChangedListener(ExchangeRateActivity.this.tw_to);
                if (ExchangeRateActivity.this.to_edit.hasFocus()) {
                    Selection.setSelection(ExchangeRateActivity.this.to_edit.getText(), ExchangeRateActivity.this.to_edit.length());
                }
            }
        });
        View view = this.from_view;
        ExchangeCcy exchangeCcy = new ExchangeCcy("MVR", "Maldivian Rufiyaa", "Cash Market", 0.0d, 0.0d, 0.0d);
        setCurrencyView(view, exchangeCcy);
        this.to_ccy = (ExchangeCcy) this.mData.get(0);
        Iterator it = this.mData.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ExchangeCcy exchangeCcy2 = (ExchangeCcy) it.next();
            if (exchangeCcy2.ccy.equalsIgnoreCase("USD")) {
                this.to_ccy = exchangeCcy2;
                break;
            }
        }
        setCurrencyView(this.to_view, this.to_ccy);
    }
}
