package mv.com.bml.mib.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import mv.com.bml.mib.R;

public class RadioIcon extends ImageView {
    private boolean isSelected = false;
    private onRadioIconSelectedListener mListener;
    private Paint mPaint = new Paint();
    private String name;
    private int srcDefault;
    private int srcSelected;

    public interface onRadioIconSelectedListener {
        void radioIconSelected(RadioIcon radioIcon);
    }

    public RadioIcon(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setAntiAlias(true);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.RadioIcon, 0, 0);
        this.srcDefault = obtainStyledAttributes.getResourceId(0, 17170445);
        this.srcSelected = obtainStyledAttributes.getResourceId(2, 17170445);
        this.name = obtainStyledAttributes.getString(1);
        obtainStyledAttributes.recycle();
        setImageResource(this.srcDefault);
    }

    public String getName() {
        return this.name;
    }

    public boolean isItemSelected() {
        return this.isSelected;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float min = ((float) Math.min(getWidth(), getHeight())) / 2.0f;
        this.mPaint.setColor(this.isSelected ? Color.parseColor("#3C3C3C") : -3355444);
        canvas.drawCircle(min, min, min, this.mPaint);
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            onRadioIconSelectedListener onradioiconselectedlistener = this.mListener;
            if (onradioiconselectedlistener != null) {
                onradioiconselectedlistener.radioIconSelected(this);
            }
        }
        return true;
    }

    public void setIsSelected(boolean z) {
        this.isSelected = z;
        setImageResource(z ? this.srcSelected : this.srcDefault);
        postInvalidate();
    }

    public void setmListener(onRadioIconSelectedListener onradioiconselectedlistener) {
        this.mListener = onradioiconselectedlistener;
    }
}
