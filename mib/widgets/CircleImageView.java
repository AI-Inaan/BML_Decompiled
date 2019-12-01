package mv.com.bml.mib.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import mv.com.bml.mib.R;

public class CircleImageView extends AppCompatImageView {
    private float angle;
    private boolean isCenter;
    private Paint mPaint;
    private String name;
    private int position;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.angle = 0.0f;
        this.position = 0;
        this.isCenter = false;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.CircleImageView);
            this.name = obtainStyledAttributes.getString(1);
            this.isCenter = obtainStyledAttributes.getBoolean(0, false);
            obtainStyledAttributes.recycle();
        }
    }

    public float getAngle() {
        return this.angle;
    }

    public String getName() {
        return this.name;
    }

    public Paint getPaint() {
        if (this.mPaint == null) {
            this.mPaint = new Paint();
            this.mPaint.setColor(Color.parseColor("#d8d8d8"));
            this.mPaint.setStyle(Style.FILL);
            this.mPaint.setAntiAlias(true);
        }
        return this.mPaint;
    }

    public int getPosition() {
        return this.position;
    }

    public boolean isCenter() {
        return this.isCenter;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setAngle(float f) {
        this.angle = f;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPosition(int i) {
        this.position = i;
    }
}
