package mv.com.bml.mib.widgets;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import mv.com.bml.mib.R;

public class CircleLayout extends ViewGroup {
    /* access modifiers changed from: private */
    public float angle;
    private ObjectAnimator animator;
    private int centreId;
    /* access modifiers changed from: private */
    public int childHeight;
    /* access modifiers changed from: private */
    public int childWidth;
    /* access modifiers changed from: private */
    public int circleHeight;
    /* access modifiers changed from: private */
    public int circleWidth;
    private boolean didMove;
    private int dividerId;
    private Bitmap dividerScaled;
    /* access modifiers changed from: private */
    public float firstChildPos;
    private GestureDetector gestureDetector;
    private Bitmap imageScaled;
    /* access modifiers changed from: private */
    public boolean isRotating;
    private Matrix matrix;
    private int maxChildHeight;
    private int maxChildWidth;
    /* access modifiers changed from: private */
    public OnCenterClickListener onCenterClickListener;
    /* access modifiers changed from: private */
    public OnItemClickListener onItemClickListener;
    /* access modifiers changed from: private */
    public OnItemSelectedListener onItemSelectedListener;
    /* access modifiers changed from: private */
    public OnRotationFinishedListener onRotationFinishedListener;
    private int picId;
    /* access modifiers changed from: private */
    public boolean[] quadrantTouched;
    private int radius;
    /* access modifiers changed from: private */
    public int selected;
    /* access modifiers changed from: private */
    public int speed;
    /* access modifiers changed from: private */
    public View tappedView;
    /* access modifiers changed from: private */
    public int tappedViewsPostition;
    private double touchStartAngle;

    private class MyGestureListener extends SimpleOnGestureListener {
        private MyGestureListener() {
        }

        private float getCenteredAngle(float f) {
            float childCount = (float) (360 / CircleLayout.this.getChildCount());
            float f2 = f % 360.0f;
            if (f2 < 0.0f) {
                f2 += 360.0f;
            }
            for (float access$1000 = CircleLayout.this.firstChildPos; access$1000 < CircleLayout.this.firstChildPos + 360.0f; access$1000 += childCount) {
                float f3 = f2 - (access$1000 % 360.0f);
                if (Math.abs(f3) < childCount / 2.0f) {
                    return f - f3;
                }
            }
            return f;
        }

        private int pointToPosition(float f, float f2) {
            for (int i = 0; i < CircleLayout.this.getChildCount(); i++) {
                View childAt = CircleLayout.this.getChildAt(i);
                if (((float) childAt.getLeft()) < f) {
                    boolean z = true;
                    boolean z2 = ((float) childAt.getRight()) > f;
                    if (((float) childAt.getTop()) >= f2) {
                        z = false;
                    }
                    if ((z2 && z) && ((float) childAt.getBottom()) > f2) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            float f3;
            CircleLayout circleLayout;
            if (!CircleLayout.this.isRotating) {
                return false;
            }
            int access$500 = CircleLayout.getPositionQuadrant((double) (motionEvent.getX() - ((float) (CircleLayout.this.circleWidth / 2))), (double) ((((float) CircleLayout.this.circleHeight) - motionEvent.getY()) - ((float) (CircleLayout.this.circleHeight / 2))));
            int access$5002 = CircleLayout.getPositionQuadrant((double) (motionEvent2.getX() - ((float) (CircleLayout.this.circleWidth / 2))), (double) ((((float) CircleLayout.this.circleHeight) - motionEvent2.getY()) - ((float) (CircleLayout.this.circleHeight / 2))));
            if ((access$500 == 2 && access$5002 == 2 && Math.abs(f) < Math.abs(f2)) || ((access$500 == 3 && access$5002 == 3) || ((access$500 == 1 && access$5002 == 3) || ((access$500 == 4 && access$5002 == 4 && Math.abs(f) > Math.abs(f2)) || ((access$500 == 2 && access$5002 == 3) || ((access$500 == 3 && access$5002 == 2) || ((access$500 == 3 && access$5002 == 4) || ((access$500 == 4 && access$5002 == 3) || ((access$500 == 2 && access$5002 == 4 && CircleLayout.this.quadrantTouched[3]) || (access$500 == 4 && access$5002 == 2 && CircleLayout.this.quadrantTouched[3])))))))))) {
                circleLayout = CircleLayout.this;
                f3 = circleLayout.angle - ((f + f2) / 25.0f);
            } else {
                circleLayout = CircleLayout.this;
                f3 = circleLayout.angle + ((f + f2) / 25.0f);
            }
            circleLayout.animateTo(getCenteredAngle(f3), (long) (25000 / CircleLayout.this.speed));
            return true;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:20:0x00c3, code lost:
            if (mv.com.bml.mib.widgets.CircleLayout.access$1700(r5.this$0) != null) goto L_0x00c5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0111, code lost:
            if (mv.com.bml.mib.widgets.CircleLayout.access$1700(r5.this$0) != null) goto L_0x00c5;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onSingleTapUp(android.view.MotionEvent r6) {
            /*
                r5 = this;
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                float r1 = r6.getX()
                float r2 = r6.getY()
                int r1 = r5.pointToPosition(r1, r2)
                r0.tappedViewsPostition = r1
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                int r0 = r0.tappedViewsPostition
                r1 = 1
                if (r0 < 0) goto L_0x0031
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                int r2 = r0.tappedViewsPostition
                android.view.View r2 = r0.getChildAt(r2)
                r0.tappedView = r2
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                android.view.View r0 = r0.tappedView
                r0.setPressed(r1)
                goto L_0x009d
            L_0x0031:
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                int r0 = r0.circleWidth
                int r0 = r0 / 2
                float r0 = (float) r0
                mv.com.bml.mib.widgets.CircleLayout r2 = mv.com.bml.mib.widgets.CircleLayout.this
                int r2 = r2.circleHeight
                int r2 = r2 / 2
                float r2 = (float) r2
                float r3 = r6.getX()
                mv.com.bml.mib.widgets.CircleLayout r4 = mv.com.bml.mib.widgets.CircleLayout.this
                int r4 = r4.childWidth
                int r4 = r4 / 2
                float r4 = (float) r4
                float r4 = r4 + r0
                int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                if (r3 >= 0) goto L_0x009d
                float r3 = r6.getX()
                mv.com.bml.mib.widgets.CircleLayout r4 = mv.com.bml.mib.widgets.CircleLayout.this
                int r4 = r4.childWidth
                int r4 = r4 / 2
                float r4 = (float) r4
                float r0 = r0 - r4
                int r0 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
                if (r0 <= 0) goto L_0x009d
                float r0 = r6.getY()
                mv.com.bml.mib.widgets.CircleLayout r3 = mv.com.bml.mib.widgets.CircleLayout.this
                int r3 = r3.childHeight
                int r3 = r3 / 2
                float r3 = (float) r3
                float r3 = r3 + r2
                int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
                if (r0 >= 0) goto L_0x009d
                float r0 = r6.getY()
                mv.com.bml.mib.widgets.CircleLayout r3 = mv.com.bml.mib.widgets.CircleLayout.this
                int r3 = r3.childHeight
                int r3 = r3 / 2
                float r3 = (float) r3
                float r2 = r2 - r3
                int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r0 <= 0) goto L_0x009d
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                mv.com.bml.mib.widgets.CircleLayout$OnCenterClickListener r0 = r0.onCenterClickListener
                if (r0 == 0) goto L_0x009d
                mv.com.bml.mib.widgets.CircleLayout r6 = mv.com.bml.mib.widgets.CircleLayout.this
                mv.com.bml.mib.widgets.CircleLayout$OnCenterClickListener r6 = r6.onCenterClickListener
                r6.onCenterClick()
                return r1
            L_0x009d:
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                android.view.View r0 = r0.tappedView
                if (r0 == 0) goto L_0x0115
                mv.com.bml.mib.widgets.CircleLayout r6 = mv.com.bml.mib.widgets.CircleLayout.this
                android.view.View r6 = r6.tappedView
                mv.com.bml.mib.widgets.CircleImageView r6 = (mv.com.bml.mib.widgets.CircleImageView) r6
                mv.com.bml.mib.widgets.CircleImageView r6 = (mv.com.bml.mib.widgets.CircleImageView) r6
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                int r0 = r0.selected
                mv.com.bml.mib.widgets.CircleLayout r2 = mv.com.bml.mib.widgets.CircleLayout.this
                int r2 = r2.tappedViewsPostition
                if (r0 != r2) goto L_0x00d9
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                mv.com.bml.mib.widgets.CircleLayout$OnItemClickListener r0 = r0.onItemClickListener
                if (r0 == 0) goto L_0x0114
            L_0x00c5:
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                mv.com.bml.mib.widgets.CircleLayout$OnItemClickListener r0 = r0.onItemClickListener
                mv.com.bml.mib.widgets.CircleLayout r2 = mv.com.bml.mib.widgets.CircleLayout.this
                android.view.View r2 = r2.tappedView
                java.lang.String r6 = r6.getName()
                r0.onItemClick(r2, r6)
                goto L_0x0114
            L_0x00d9:
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                r0.rotateViewToCenter(r6)
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                mv.com.bml.mib.widgets.CircleLayout$MyGestureListener$1 r2 = new mv.com.bml.mib.widgets.CircleLayout$MyGestureListener$1
                r2.<init>()
                r0.setOnRotationFinishedListener(r2)
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                boolean r0 = r0.isRotating
                if (r0 != 0) goto L_0x0114
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                mv.com.bml.mib.widgets.CircleLayout$OnItemSelectedListener r0 = r0.onItemSelectedListener
                if (r0 == 0) goto L_0x010b
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                mv.com.bml.mib.widgets.CircleLayout$OnItemSelectedListener r0 = r0.onItemSelectedListener
                mv.com.bml.mib.widgets.CircleLayout r2 = mv.com.bml.mib.widgets.CircleLayout.this
                android.view.View r2 = r2.tappedView
                java.lang.String r3 = r6.getName()
                r0.onItemSelected(r2, r3)
            L_0x010b:
                mv.com.bml.mib.widgets.CircleLayout r0 = mv.com.bml.mib.widgets.CircleLayout.this
                mv.com.bml.mib.widgets.CircleLayout$OnItemClickListener r0 = r0.onItemClickListener
                if (r0 == 0) goto L_0x0114
                goto L_0x00c5
            L_0x0114:
                return r1
            L_0x0115:
                boolean r6 = super.onSingleTapUp(r6)
                return r6
            */
            throw new UnsupportedOperationException("Method not decompiled: mv.com.bml.mib.widgets.CircleLayout.MyGestureListener.onSingleTapUp(android.view.MotionEvent):boolean");
        }
    }

    public interface OnCenterClickListener {
        void onCenterClick();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String str);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(View view, String str);
    }

    public interface OnRotationFinishedListener {
        void onRotationFinished(View view, String str);
    }

    public CircleLayout(Context context) {
        this(context, null);
    }

    public CircleLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.onItemClickListener = null;
        this.onItemSelectedListener = null;
        this.onCenterClickListener = null;
        this.onRotationFinishedListener = null;
        this.radius = 0;
        this.maxChildWidth = 0;
        this.maxChildHeight = 0;
        this.childWidth = 0;
        this.childHeight = 0;
        this.speed = 25;
        this.angle = 90.0f;
        this.firstChildPos = 90.0f;
        this.isRotating = true;
        this.tappedViewsPostition = -1;
        this.tappedView = null;
        this.selected = 0;
        this.didMove = false;
        init(attributeSet);
    }

    /* access modifiers changed from: private */
    public void animateTo(float f, long j) {
        ObjectAnimator objectAnimator = this.animator;
        if ((objectAnimator == null || !objectAnimator.isRunning()) && Math.abs(this.angle - f) >= 1.0f) {
            this.animator = ObjectAnimator.ofFloat(this, "angle", new float[]{this.angle, f});
            this.animator.setDuration(j);
            this.animator.setInterpolator(new DecelerateInterpolator());
            this.animator.addListener(new AnimatorListener() {
                private boolean wasCanceled = false;

                public void onAnimationCancel(Animator animator) {
                    this.wasCanceled = true;
                }

                public void onAnimationEnd(Animator animator) {
                    if (!this.wasCanceled && CircleLayout.this.onRotationFinishedListener != null) {
                        CircleImageView circleImageView = (CircleImageView) CircleLayout.this.getSelectedItem();
                        CircleLayout.this.onRotationFinishedListener.onRotationFinished(circleImageView, circleImageView.getName());
                    }
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }
            });
            this.animator.start();
        }
    }

    public static int calculateInSampleSize(Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 > i2 && i7 / i5 > i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    private int calculatePosition(int i, int i2, float f, float f2) {
        double d = (double) ((i / 2) - (i2 / 2));
        double d2 = (double) (((float) this.radius) * f);
        double cos = Math.cos(Math.toRadians((double) f2));
        Double.isNaN(d2);
        double d3 = d2 * cos;
        Double.isNaN(d);
        return Math.round((float) (d + d3));
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int i, int i2, int i3) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = calculateInSampleSize(options, i2, i3);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, i, options);
    }

    private void drawDividers(Canvas canvas) {
        int i;
        float f;
        long j;
        long j2;
        long j3;
        long childCount = (long) getChildCount();
        long width = (long) this.dividerScaled.getWidth();
        long height = (long) this.dividerScaled.getHeight();
        float f2 = (360.0f / ((float) childCount)) / 2.0f;
        Matrix matrix2 = new Matrix();
        int i2 = 0;
        while (((long) i2) < childCount) {
            CircleImageView circleImageView = (CircleImageView) getChildAt(i2);
            if (circleImageView.getVisibility() == 8) {
                j3 = childCount;
                j = width;
                j2 = height;
                f = f2;
                i = i2;
                Canvas canvas2 = canvas;
            } else {
                float angle2 = circleImageView.getAngle() + f2;
                j3 = childCount;
                long j4 = width / 2;
                double width2 = (double) (((long) (getWidth() / 2)) - j4);
                double d = (double) this.radius;
                Double.isNaN(d);
                j = width;
                double d2 = (double) angle2;
                double cos = d * 0.9d * Math.cos(Math.toRadians(d2));
                Double.isNaN(width2);
                long round = (long) Math.round((float) (width2 + cos));
                f = f2;
                i = i2;
                long j5 = height / 2;
                double height2 = (double) (((long) (getHeight() / 2)) - j5);
                j2 = height;
                double d3 = (double) this.radius;
                Double.isNaN(d3);
                double sin = d3 * 0.9d * Math.sin(Math.toRadians(d2));
                Double.isNaN(height2);
                long round2 = (long) Math.round((float) (height2 + sin));
                float f3 = (float) j4;
                float f4 = (float) j5;
                matrix2.setTranslate(f3, f4);
                matrix2.preRotate(angle2 - 90.0f, f3, f4);
                matrix2.postTranslate((float) (round - j4), (float) (round2 - j5));
                canvas.drawBitmap(this.dividerScaled, matrix2, null);
            }
            i2 = i + 1;
            childCount = j3;
            height = j2;
            width = j;
            f2 = f;
        }
    }

    private double getPositionAngle(double d, double d2) {
        double d3 = (double) this.circleWidth;
        Double.isNaN(d3);
        double d4 = d - (d3 / 2.0d);
        int i = this.circleHeight;
        double d5 = (double) i;
        Double.isNaN(d5);
        double d6 = d5 - d2;
        double d7 = (double) i;
        Double.isNaN(d7);
        double d8 = d6 - (d7 / 2.0d);
        int positionQuadrant = getPositionQuadrant(d4, d8);
        if (positionQuadrant == 1) {
            return (Math.asin(d8 / Math.hypot(d4, d8)) * 180.0d) / 3.141592653589793d;
        }
        if (positionQuadrant == 2 || positionQuadrant == 3) {
            return 180.0d - ((Math.asin(d8 / Math.hypot(d4, d8)) * 180.0d) / 3.141592653589793d);
        }
        if (positionQuadrant != 4) {
            return 0.0d;
        }
        return ((Math.asin(d8 / Math.hypot(d4, d8)) * 180.0d) / 3.141592653589793d) + 360.0d;
    }

    /* access modifiers changed from: private */
    public static int getPositionQuadrant(double d, double d2) {
        if (d >= 0.0d) {
            return d2 >= 0.0d ? 1 : 4;
        }
        return d2 >= 0.0d ? 2 : 3;
    }

    private void rotateButtons(float f) {
        this.angle += f;
        setChildAngles();
        invalidate();
    }

    /* access modifiers changed from: private */
    public void rotateViewToCenter(CircleImageView circleImageView) {
        if (this.isRotating) {
            float angle2 = this.firstChildPos - circleImageView.getAngle();
            if (angle2 < 0.0f) {
                angle2 += 360.0f;
            }
            if (angle2 > 180.0f) {
                angle2 = (360.0f - angle2) * -1.0f;
            }
            animateTo(this.angle + angle2, (long) (7500 / this.speed));
        }
    }

    private void setChildAngles() {
        int childCount = getChildCount();
        float f = 360.0f;
        float f2 = 360.0f / ((float) childCount);
        float f3 = this.angle;
        int i = 0;
        while (i < childCount) {
            if (f3 > f) {
                f3 -= f;
            } else if (f3 < 0.0f) {
                f3 += f;
            }
            CircleImageView circleImageView = (CircleImageView) getChildAt(i);
            if (circleImageView.getVisibility() != 8) {
                double d = (double) ((this.circleWidth / 2) - (this.childWidth / 2));
                double d2 = (double) this.radius;
                Double.isNaN(d2);
                double d3 = (double) f3;
                double cos = d2 * 0.9d * Math.cos(Math.toRadians(d3));
                Double.isNaN(d);
                int round = Math.round((float) (d + cos));
                double d4 = (double) ((this.circleHeight / 2) - (this.childHeight / 2));
                float f4 = f3;
                double d5 = (double) this.radius;
                Double.isNaN(d5);
                double sin = d5 * 0.9d * Math.sin(Math.toRadians(d3));
                Double.isNaN(d4);
                int round2 = Math.round((float) (d4 + sin));
                float f5 = f4;
                circleImageView.setAngle(f5);
                circleImageView.setRotation(f5 - 90.0f);
                if (Math.abs(f5 - this.firstChildPos) < f2 / 2.0f && this.selected != circleImageView.getPosition()) {
                    this.selected = circleImageView.getPosition();
                    OnItemSelectedListener onItemSelectedListener2 = this.onItemSelectedListener;
                    if (onItemSelectedListener2 != null && this.isRotating) {
                        onItemSelectedListener2.onItemSelected(circleImageView, circleImageView.getName());
                    }
                }
                circleImageView.layout(round, round2, this.childWidth + round, this.childHeight + round2);
                f3 = f5 + f2;
            }
            i++;
            f = 360.0f;
        }
    }

    private void stopAnimation() {
        ObjectAnimator objectAnimator = this.animator;
        if (objectAnimator != null && objectAnimator.isRunning()) {
            this.animator.cancel();
            this.animator = null;
        }
    }

    public float getAngle() {
        return this.angle;
    }

    public View getSelectedItem() {
        int i = this.selected;
        if (i >= 0) {
            return getChildAt(i);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void init(AttributeSet attributeSet) {
        this.gestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        this.quadrantTouched = new boolean[]{false, false, false, false, false};
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.CircleLayout);
            this.angle = (float) obtainStyledAttributes.getInt(3, (int) this.angle);
            this.firstChildPos = this.angle;
            this.speed = obtainStyledAttributes.getInt(5, this.speed);
            this.isRotating = obtainStyledAttributes.getBoolean(4, this.isRotating);
            this.picId = obtainStyledAttributes.getResourceId(1, -1);
            this.dividerId = obtainStyledAttributes.getResourceId(2, -1);
            obtainStyledAttributes.recycle();
            Matrix matrix2 = this.matrix;
            if (matrix2 == null) {
                this.matrix = new Matrix();
            } else {
                matrix2.reset();
            }
            setWillNotDraw(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.circleHeight = getHeight();
        this.circleWidth = getWidth();
        Bitmap bitmap = this.imageScaled;
        if (bitmap != null) {
            int width = (this.circleWidth - bitmap.getWidth()) / 2;
            int width2 = (this.circleHeight - this.imageScaled.getWidth()) / 2;
            canvas.rotate(0.0f, (float) (this.circleWidth / 2), (float) (this.circleHeight / 2));
            canvas.drawBitmap(this.imageScaled, (float) width, (float) width2, null);
        }
        if (this.dividerScaled != null) {
            drawDividers(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f;
        int i5 = i3 - i;
        int i6 = i4 - i2;
        int childCount = getChildCount();
        this.radius = i5 <= i6 ? i5 / 3 : i6 / 3;
        int i7 = this.radius;
        double d = (double) i7;
        Double.isNaN(d);
        this.childWidth = (int) (d / 1.7d);
        double d2 = (double) i7;
        Double.isNaN(d2);
        this.childHeight = (int) (d2 / 1.7d);
        float childCount2 = 360.0f / ((float) getChildCount());
        for (int i8 = 0; i8 < childCount; i8++) {
            CircleImageView circleImageView = (CircleImageView) getChildAt(i8);
            if (circleImageView.getVisibility() != 8) {
                float f2 = this.angle;
                if (f2 > 360.0f) {
                    f = f2 - 360.0f;
                } else {
                    if (f2 < 0.0f) {
                        f = f2 + 360.0f;
                    }
                    circleImageView.setAngle(this.angle);
                    circleImageView.setPosition(i8);
                    circleImageView.setRotation(this.angle - 90.0f);
                    double d3 = (double) ((i5 / 2) - (this.childWidth / 2));
                    double d4 = (double) this.radius;
                    Double.isNaN(d4);
                    double cos = d4 * 0.9d * Math.cos(Math.toRadians((double) this.angle));
                    Double.isNaN(d3);
                    int round = Math.round((float) (d3 + cos));
                    double d5 = (double) ((i6 / 2) - (this.childHeight / 2));
                    double d6 = (double) this.radius;
                    Double.isNaN(d6);
                    double sin = d6 * 0.9d * Math.sin(Math.toRadians((double) this.angle));
                    Double.isNaN(d5);
                    int round2 = Math.round((float) (d5 + sin));
                    circleImageView.layout(round, round2, this.childWidth + round, this.childHeight + round2);
                    this.angle += childCount2;
                }
                this.angle = f;
                circleImageView.setAngle(this.angle);
                circleImageView.setPosition(i8);
                circleImageView.setRotation(this.angle - 90.0f);
                double d32 = (double) ((i5 / 2) - (this.childWidth / 2));
                double d42 = (double) this.radius;
                Double.isNaN(d42);
                double cos2 = d42 * 0.9d * Math.cos(Math.toRadians((double) this.angle));
                Double.isNaN(d32);
                int round3 = Math.round((float) (d32 + cos2));
                double d52 = (double) ((i6 / 2) - (this.childHeight / 2));
                double d62 = (double) this.radius;
                Double.isNaN(d62);
                double sin2 = d62 * 0.9d * Math.sin(Math.toRadians((double) this.angle));
                Double.isNaN(d52);
                int round22 = Math.round((float) (d52 + sin2));
                circleImageView.layout(round3, round22, this.childWidth + round3, this.childHeight + round22);
                this.angle += childCount2;
            }
        }
        if (this.imageScaled == null && this.picId != -1) {
            Bitmap decodeSampledBitmapFromResource = decodeSampledBitmapFromResource(getResources(), this.picId, i5, i6);
            int i9 = this.radius;
            double d7 = (double) (this.childWidth + i9);
            Double.isNaN(d7);
            int i10 = (int) (d7 * 1.9d);
            double d8 = (double) (i9 + this.childHeight);
            Double.isNaN(d8);
            this.imageScaled = Bitmap.createScaledBitmap(decodeSampledBitmapFromResource, i10, (int) (d8 * 1.9d), true);
            decodeSampledBitmapFromResource.recycle();
        }
        if (this.dividerScaled == null && this.dividerId != -1) {
            Bitmap decodeSampledBitmapFromResource2 = decodeSampledBitmapFromResource(getResources(), this.dividerId, this.childWidth, this.childHeight);
            float height = (((float) this.childHeight) * 1.5f) / ((float) decodeSampledBitmapFromResource2.getHeight());
            this.matrix = new Matrix();
            this.matrix.postScale(height / 2.0f, height);
            this.dividerScaled = Bitmap.createBitmap(decodeSampledBitmapFromResource2, 0, 0, decodeSampledBitmapFromResource2.getWidth(), decodeSampledBitmapFromResource2.getHeight(), this.matrix, false);
            decodeSampledBitmapFromResource2.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        this.maxChildWidth = 0;
        this.maxChildHeight = 0;
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), Integer.MIN_VALUE);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), Integer.MIN_VALUE);
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                childAt.measure(makeMeasureSpec, makeMeasureSpec2);
                this.maxChildWidth = Math.max(this.maxChildWidth, childAt.getMeasuredWidth());
                this.maxChildHeight = Math.max(this.maxChildHeight, childAt.getMeasuredHeight());
            }
        }
        int makeMeasureSpec3 = MeasureSpec.makeMeasureSpec(this.maxChildWidth, 1073741824);
        int makeMeasureSpec4 = MeasureSpec.makeMeasureSpec(this.maxChildHeight, 1073741824);
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt2 = getChildAt(i4);
            if (childAt2.getVisibility() != 8) {
                childAt2.measure(makeMeasureSpec3, makeMeasureSpec4);
            }
        }
        setMeasuredDimension(resolveSize(this.maxChildWidth, i), resolveSize(this.maxChildHeight, i2));
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        this.gestureDetector.onTouchEvent(motionEvent);
        if (this.isRotating) {
            int action = motionEvent.getAction();
            if (action == 0) {
                int i = 0;
                while (true) {
                    boolean[] zArr = this.quadrantTouched;
                    if (i >= zArr.length) {
                        break;
                    }
                    zArr[i] = false;
                    i++;
                }
                stopAnimation();
                this.touchStartAngle = getPositionAngle((double) motionEvent.getX(), (double) motionEvent.getY());
                this.didMove = false;
            } else if (action != 1) {
                if (action == 2) {
                    double positionAngle = getPositionAngle((double) motionEvent.getX(), (double) motionEvent.getY());
                    rotateButtons((float) (this.touchStartAngle - positionAngle));
                    this.touchStartAngle = positionAngle;
                    this.didMove = true;
                }
            } else if (this.didMove) {
                rotateViewToCenter((CircleImageView) getChildAt(this.selected));
            }
        }
        this.quadrantTouched[getPositionQuadrant((double) (motionEvent.getX() - ((float) (this.circleWidth / 2))), (double) ((((float) this.circleHeight) - motionEvent.getY()) - ((float) (this.circleHeight / 2))))] = true;
        return true;
    }

    public void setAngle(float f) {
        this.angle = f % 360.0f;
        setChildAngles();
    }

    public void setOnCenterClickListener(OnCenterClickListener onCenterClickListener2) {
        this.onCenterClickListener = onCenterClickListener2;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener2) {
        this.onItemSelectedListener = onItemSelectedListener2;
    }

    public void setOnRotationFinishedListener(OnRotationFinishedListener onRotationFinishedListener2) {
        this.onRotationFinishedListener = onRotationFinishedListener2;
    }
}
