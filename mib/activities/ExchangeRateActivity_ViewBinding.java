package mv.com.bml.mib.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class ExchangeRateActivity_ViewBinding implements Unbinder {
    private ExchangeRateActivity target;

    @UiThread
    public ExchangeRateActivity_ViewBinding(ExchangeRateActivity exchangeRateActivity) {
        this(exchangeRateActivity, exchangeRateActivity.getWindow().getDecorView());
    }

    @UiThread
    public ExchangeRateActivity_ViewBinding(ExchangeRateActivity exchangeRateActivity, View view) {
        this.target = exchangeRateActivity;
        exchangeRateActivity.from_view = Utils.findRequiredView(view, R.id.ccy_from, "field 'from_view'");
        exchangeRateActivity.to_view = Utils.findRequiredView(view, R.id.ccy_to, "field 'to_view'");
        exchangeRateActivity.mListView = (ListView) Utils.findRequiredViewAsType(view, R.id.exchange_list, "field 'mListView'", ListView.class);
    }

    @CallSuper
    public void unbind() {
        ExchangeRateActivity exchangeRateActivity = this.target;
        if (exchangeRateActivity != null) {
            this.target = null;
            exchangeRateActivity.from_view = null;
            exchangeRateActivity.to_view = null;
            exchangeRateActivity.mListView = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
