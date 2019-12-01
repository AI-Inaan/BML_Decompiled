package mv.com.bml.mib.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;
import mv.com.bml.mib.R;

public class PincodeEntry extends FrameLayout {
    /* access modifiers changed from: private */
    public OnPincodeEntryListener mListener;
    private GridLayout mNumberGrid;
    /* access modifiers changed from: private */
    public TextView mPinText;
    private TextView mStatusText;
    private TextView mSubStatusText;
    /* access modifiers changed from: private */
    public int pinLength;
    /* access modifiers changed from: private */
    public StringBuilder pin_buffer = new StringBuilder(this.pinLength);

    public interface OnPincodeEntryListener {
        void onPincodeEntryChanged();

        void onPincodeEntryComplete(int i);
    }

    public PincodeEntry(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.PincodeEntry);
        this.pinLength = obtainStyledAttributes.getInteger(2, 4);
        obtainStyledAttributes.recycle();
        View inflate = LayoutInflater.from(context).inflate(R.layout.widget_pin_entry, null);
        this.mStatusText = (TextView) inflate.findViewById(R.id.status_text);
        this.mSubStatusText = (TextView) inflate.findViewById(R.id.status_text_sub);
        this.mPinText = (TextView) inflate.findViewById(R.id.pin_entry_text);
        this.mNumberGrid = (GridLayout) inflate.findViewById(R.id.button_grid);
        setPinDots();
        for (int i = 0; i < this.mNumberGrid.getChildCount(); i++) {
            View childAt = this.mNumberGrid.getChildAt(i);
            if (childAt instanceof Button) {
                childAt.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        String charSequence = ((Button) view).getText().toString();
                        ((Vibrator) PincodeEntry.this.getContext().getSystemService("vibrator")).vibrate(20);
                        PincodeEntry.this.setSubStatus(null);
                        if (PincodeEntry.this.mPinText.isEnabled()) {
                            if (charSequence.equalsIgnoreCase("⌫")) {
                                int length = PincodeEntry.this.pin_buffer.length();
                                StringBuilder access$100 = PincodeEntry.this.pin_buffer;
                                int i = length - 1;
                                if (i < 0) {
                                    i = 0;
                                }
                                access$100.setLength(i);
                            } else if (charSequence.equalsIgnoreCase("clear")) {
                                PincodeEntry.this.pin_buffer.setLength(0);
                            } else {
                                PincodeEntry.this.pin_buffer.append(charSequence);
                                if (PincodeEntry.this.mListener != null) {
                                    if (PincodeEntry.this.pin_buffer.length() == PincodeEntry.this.pinLength) {
                                        PincodeEntry.this.mListener.onPincodeEntryComplete(Integer.parseInt(PincodeEntry.this.pin_buffer.toString()));
                                    } else {
                                        PincodeEntry.this.mListener.onPincodeEntryChanged();
                                    }
                                }
                            }
                            PincodeEntry.this.setPinDots();
                        }
                    }
                });
            }
        }
        addView(inflate);
    }

    /* access modifiers changed from: private */
    public void setPinDots() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.pinLength; i++) {
            try {
                this.pin_buffer.charAt(i);
                sb.append("●");
            } catch (StringIndexOutOfBoundsException unused) {
                sb.append("○");
            }
        }
        this.mPinText.setText(sb.toString());
    }

    public void clearText() {
        this.pin_buffer.setLength(0);
        setPinDots();
    }

    public void clearText(boolean z) {
        clearText();
        if (z) {
            this.mPinText.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
            ((Vibrator) getContext().getSystemService("vibrator")).vibrate(80);
        }
    }

    public String getPincode() {
        return this.pin_buffer.toString();
    }

    public void setEnabled(boolean z) {
        this.mPinText.setEnabled(z);
    }

    public void setPincodeEntryListener(OnPincodeEntryListener onPincodeEntryListener) {
        this.mListener = onPincodeEntryListener;
    }

    public void setStatus(String str) {
        this.mStatusText.setText(str);
    }

    public void setSubStatus(String str) {
        setSubStatus(str, ContextCompat.getColor(getContext(), R.color.primary_grey));
    }

    public void setSubStatus(String str, int i) {
        this.mSubStatusText.setTextColor(i);
        this.mSubStatusText.setText(str);
    }
}
