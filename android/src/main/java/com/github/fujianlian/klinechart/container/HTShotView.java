package com.github.fujianlian.klinechart.container;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class HTShotView extends View {

    private View shotView;

    public int dimension;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap bitmap;

    private float scale = 1.5f;

    @Override
    public void requestLayout() {
        super.requestLayout();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        int left = layoutParams.leftMargin;
        int top = layoutParams.topMargin;
        int width = layoutParams.width;
        int height = layoutParams.height;
        measure(width, height);
        layout(left, top, left + width, top + height);
    }

    public HTShotView(Context context, View shotView) {
        super(context);
        this.shotView = shotView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (shotView != null && bitmap != null) {
            Path path = new Path();
            RectF round = new RectF(0, 0, dimension, dimension);
            path.addRoundRect(round, dimension / 2, dimension / 2, Path.Direction.CW);
            canvas.clipPath(path);
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }

    public void setPoint(HTPoint point) {
        if (point != null) {
            bitmap = loadBitmapFromView(shotView);
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            float left = point.x - dimension / 2 + dimension / 4 * (scale - 1);
            left = Math.max(0, Math.min(left, bitmap.getWidth()));
            float top = (point.y - dimension / 2 + dimension / 4 * (scale - 1));
            top = Math.max(0, Math.min(top, bitmap.getHeight()));
            float width = Math.max(0, Math.min(dimension, bitmap.getWidth() - left));
            float height = Math.max(0, Math.min(dimension, bitmap.getHeight() - top));
            if (width <= 0 || height <= 0) {
                bitmap = null;
            } else {
                bitmap = Bitmap.createBitmap(bitmap,
                        (int)left,
                        (int)top,
                        (int)width,
                        (int)height,
                        matrix,
                        false
                );
            }
        } else {
            bitmap = null;
        }
        invalidate();
    }

    private Bitmap loadBitmapFromView(View view) {
        if (view == null) {
            return null;
        }
        Bitmap screenshot = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenshot);
//        int count = viewGroup.getChildCount();
//        for (int index = 2; index < 4; index ++) {
//            View childView = viewGroup.getChildAt(index);
//            canvas.translate(-childView.getX(), -childView.getY());
//            childView.draw(canvas);
//        }
        view.draw(canvas);
        return screenshot;
    }

}
