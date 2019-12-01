package mv.com.bml.mib.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import mv.com.bml.mib.R;

public class ATMLocations_ViewBinding implements Unbinder {
    private ATMLocations target;

    @UiThread
    public ATMLocations_ViewBinding(ATMLocations aTMLocations) {
        this(aTMLocations, aTMLocations.getWindow().getDecorView());
    }

    @UiThread
    public ATMLocations_ViewBinding(ATMLocations aTMLocations, View view) {
        this.target = aTMLocations;
        aTMLocations.toggleImage = (ImageView) Utils.findRequiredViewAsType(view, R.id.toggle_location_list, "field 'toggleImage'", ImageView.class);
        aTMLocations.toggleHeader = Utils.findRequiredView(view, R.id.toggle_header, "field 'toggleHeader'");
        aTMLocations.locationList = (ListView) Utils.findRequiredViewAsType(view, R.id.location_list, "field 'locationList'", ListView.class);
    }

    @CallSuper
    public void unbind() {
        ATMLocations aTMLocations = this.target;
        if (aTMLocations != null) {
            this.target = null;
            aTMLocations.toggleImage = null;
            aTMLocations.toggleHeader = null;
            aTMLocations.locationList = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
