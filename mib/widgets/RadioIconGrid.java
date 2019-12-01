package mv.com.bml.mib.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import mv.com.bml.mib.widgets.RadioIcon.onRadioIconSelectedListener;

public class RadioIconGrid extends GridLayout implements onRadioIconSelectedListener {
    public RadioIconGrid(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private int getSelectedItemIndex() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if ((childAt instanceof RadioIcon) && ((RadioIcon) childAt).isItemSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void setChildListeners() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof RadioIcon) {
                ((RadioIcon) childAt).setmListener(this);
            }
        }
    }

    public void clearSelection() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof RadioIcon) {
                ((RadioIcon) childAt).setIsSelected(false);
            }
        }
        postInvalidate();
    }

    public int getIndexForItem(String str) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if ((childAt instanceof RadioIcon) && str.contains(((RadioIcon) childAt).getName())) {
                return i;
            }
        }
        return 0;
    }

    public String getSelectedIconName() {
        View childAt = getChildAt(getSelectedItemIndex());
        if (childAt instanceof RadioIcon) {
            return ((RadioIcon) childAt).getName();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setChildListeners();
    }

    public void radioIconSelected(RadioIcon radioIcon) {
        clearSelection();
        radioIcon.setIsSelected(true);
    }

    public void setSelectedItem(int i) {
        if (i < getChildCount()) {
            View childAt = getChildAt(i);
            if (childAt instanceof RadioIcon) {
                ((RadioIcon) childAt).setIsSelected(true);
            }
        }
    }
}
