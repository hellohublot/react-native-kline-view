package com.github.fujianlian.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.fujianlian.klinechart.BaseKLineChartView;
import com.github.fujianlian.klinechart.HTKLineConfigManager;
import com.github.fujianlian.klinechart.base.IChartDraw;
import com.github.fujianlian.klinechart.base.IValueFormatter;
import com.github.fujianlian.klinechart.entity.IKDJ;
import com.github.fujianlian.klinechart.formatter.ValueFormatter;

import static android.graphics.Typeface.NORMAL;

/**
 * KDJ实现类
 * Created by tifezh on 2016/6/19.
 */

public class KDJDraw implements IChartDraw<IKDJ> {

    private Context mContext = null;

    private Paint mKPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mJPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public KDJDraw(BaseKLineChartView view) {
        mContext = view.getContext();
    }

    public void reloadColor(BaseKLineChartView view) {
        mKPaint.setColor(view.configManager.targetColorList[0]);
        mDPaint.setColor(view.configManager.targetColorList[1]);
        mJPaint.setColor(view.configManager.targetColorList[2]);
    }

    @Override
    public void drawTranslated(@Nullable IKDJ lastPoint, @NonNull IKDJ curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        if (lastPoint.getK() != 0) {
            view.drawChildLine(canvas, mKPaint, lastX, lastPoint.getK(), curX, curPoint.getK());
        }
        if (lastPoint.getD() != 0) {
            view.drawChildLine(canvas, mDPaint, lastX, lastPoint.getD(), curX, curPoint.getD());
        }
        if (lastPoint.getJ() != 0) {
            view.drawChildLine(canvas, mJPaint, lastX, lastPoint.getJ(), curX, curPoint.getJ());
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        IKDJ point = (IKDJ) view.getItem(position);
        if (point.getK() != 0) {
            String text = String.format("KDJ(%s,%s,%s)  ", view.configManager.kdjN, view.configManager.kdjM1, view.configManager.kdjM2);
            canvas.drawText(text, x, y, view.getTextPaint());
            x += view.getTextPaint().measureText(text);
            text = "K:" + view.formatValue(point.getK()) + "  ";
            canvas.drawText(text, x, y, mKPaint);
            x += mKPaint.measureText(text);
            if (point.getD() != 0) {
                text = "D:" + view.formatValue(point.getD()) + "  ";
                canvas.drawText(text, x, y, mDPaint);
                x += mDPaint.measureText(text);
                text = "J:" + view.formatValue(point.getJ()) + "  ";
                canvas.drawText(text, x, y, mJPaint);
            }
        }
    }

    @Override
    public float getMaxValue(IKDJ point) {
        return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
    }

    @Override
    public float getMinValue(IKDJ point) {
        return Math.min(point.getK(), Math.min(point.getD(), point.getJ()));
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 设置K颜色
     */
    public void setKColor(int color) {
        mKPaint.setColor(color);
    }

    /**
     * 设置D颜色
     */
    public void setDColor(int color) {
        mDPaint.setColor(color);
    }

    /**
     * 设置J颜色
     */
    public void setJColor(int color) {
        mJPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mKPaint.setStrokeWidth(width);
        mDPaint.setStrokeWidth(width);
        mJPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mKPaint.setTextSize(textSize);
        mDPaint.setTextSize(textSize);
        mJPaint.setTextSize(textSize);
    }

    public void setTextFontFamily(String fontFamily) {
        Typeface typeface = HTKLineConfigManager.findFont(mContext, fontFamily);
        mKPaint.setTypeface(typeface);
        mKPaint.setTypeface(typeface);
        mKPaint.setTypeface(typeface);
    }

}
