package mv.com.bml.mib.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ToolTip extends TextView implements OnClickListener {
    public ToolTip(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            setVisibility(8);
        }
        setOnClickListener(this);
    }

    public void onClick(View view) {
        view.setVisibility(8);
    }

    public void showAndVanishWithTime(int i) {
        setVisibility(0);
        postDelayed(new Runnable() {
            public void run() {
                ToolTip.this.setVisibility(8);
            }
        }, (long) i);
    }
}
