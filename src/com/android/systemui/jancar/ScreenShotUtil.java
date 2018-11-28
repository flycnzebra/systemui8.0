package com.android.systemui.jancar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceControl;
import android.view.WindowManager;

public class ScreenShotUtil {
    public static Bitmap takeScreenShot(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        final float[] dims = {displayMetrics.widthPixels, displayMetrics.heightPixels};
        Bitmap bitmap = SurfaceControl.screenshot((int) dims[0], (int) dims[1]);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = SurfaceControl.screenshot((int) dims[1], (int) dims[0]);
        Matrix matrix = new Matrix();
        matrix.setRotate(-90);
        if (bitmap == null) {
            return null;
        }
        Bitmap newBM = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        bitmap.recycle();
        return newBM;
    }
}
