package mv.com.bml.mib.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import com.squareup.picasso.Transformation;

public class CircleTransform implements Transformation {
    public String key() {
        return "circle";
    }

    public Bitmap transform(Bitmap bitmap) {
        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth() - min) / 2, (bitmap.getHeight() - min) / 2, min, min);
        if (createBitmap != bitmap) {
            bitmap.recycle();
        }
        Bitmap createBitmap2 = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        float f = ((float) min) / 2.0f;
        Canvas canvas = new Canvas(createBitmap2);
        Paint paint = new Paint();
        paint.setColor(-3355444);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(f, f, f, paint);
        Paint paint2 = new Paint();
        paint2.setShader(new BitmapShader(createBitmap, TileMode.CLAMP, TileMode.CLAMP));
        paint2.setAntiAlias(true);
        canvas.drawCircle(f, f, f, paint2);
        createBitmap.recycle();
        return createBitmap2;
    }
}
