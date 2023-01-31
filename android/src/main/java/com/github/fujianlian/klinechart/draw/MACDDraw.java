package com.github.fujianlian.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.fujianlian.klinechart.HTKLineConfigManager;
import com.github.fujianlian.klinechart.BaseKLineChartView;
import com.github.fujianlian.klinechart.base.IChartDraw;
import com.github.fujianlian.klinechart.base.IValueFormatter;
import com.github.fujianlian.klinechart.entity.IMACD;
import com.github.fujianlian.klinechart.formatter.ValueFormatter;

import static android.graphics.Typeface.NORMAL;

/**
 * macd实现类
 * Created by tifezh on 2016/6/19.
 */

public class MACDDraw implements IChartDraw<IMACD> {

    private Context mContext = null;

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDIFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDEAPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMACDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * macd 中柱子的宽度
     */
    private float mMACDWidth = 0;

    public MACDDraw(BaseKLineChartView view) {
        mContext = view.getContext();
    }

    public void reloadColor(BaseKLineChartView view) {
        mRedPaint.setColor(view.configManager.increaseColor);
        mGreenPaint.setColor(view.configManager.decreaseColor);
        mMACDPaint.setColor(view.configManager.targetColorList[5]);
        mDIFPaint.setColor(view.configManager.targetColorList[0]);
        mDEAPaint.setColor(view.configManager.targetColorList[1]);
    }

    @Override
    public void drawTranslated(@Nullable IMACD lastPoint, @NonNull IMACD curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        drawMACD(canvas, view, curX, curPoint.getMacd());
        view.drawChildLine(canvas, mDIFPaint, lastX, lastPoint.getDif(), curX, curPoint.getDif());
        view.drawChildLine(canvas, mDEAPaint, lastX, lastPoint.getDea(), curX, curPoint.getDea());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        IMACD point = (IMACD) view.getItem(position);
        String text = String.format("MACD(%s,%s,%s)  ", new Object[]{view.configManager.macdS, view.configManager.macdL, view.configManager.macdM});
        canvas.drawText(text, x, y, view.getTextPaint());
        x += view.getTextPaint().measureText(text);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MACD:");
        stringBuilder.append(view.formatValue(point.getMacd()));
        String str = "  ";
        stringBuilder.append(str);
        text = stringBuilder.toString();

        canvas.drawText(text, x, y, this.mMACDPaint);
        x += this.mMACDPaint.measureText(text);
        stringBuilder = new StringBuilder();
        stringBuilder.append("DIF:");
        stringBuilder.append(view.formatValue(point.getDif()));
        stringBuilder.append(str);
        text = stringBuilder.toString();
        canvas.drawText(text, x, y, this.mDIFPaint);
        x += this.mDIFPaint.measureText(text);
        stringBuilder = new StringBuilder();
        stringBuilder.append("DEA:");
        stringBuilder.append(view.formatValue(point.getDea()));
        canvas.drawText(stringBuilder.toString(),  x, y, this.mDEAPaint);
    }

    @Override
    public float getMaxValue(IMACD point) {
        return Math.max(point.getMacd(), Math.max(point.getDea(), point.getDif()));
    }

    @Override
    public float getMinValue(IMACD point) {
        return Math.min(point.getMacd(), Math.min(point.getDea(), point.getDif()));
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 画macd
     *
     * @param canvas
     * @param x
     * @param macd
     */
    private void drawMACD(Canvas canvas, BaseKLineChartView view, float x, float macd) {
        float macdy = view.getChildY(macd);
        float candleWidth = view.configManager.macdCandleWidth;
        float r = candleWidth / 2;
        float zeroy = view.getChildY(0);
        if (macd > 0) {
            //               left   top   right  bottom
            canvas.drawRect(x - r, macdy, x + r, zeroy, mRedPaint);
        } else {
            canvas.drawRect(x - r, zeroy, x + r, macdy, mGreenPaint);
        }
    }

    /**
     * 设置DIF颜色
     */
    public void setDIFColor(int color) {
        this.mDIFPaint.setColor(color);
    }

    /**
     * 设置DEA颜色
     */
    public void setDEAColor(int color) {
        this.mDEAPaint.setColor(color);
    }

    /**
     * 设置MACD颜色
     */
    public void setMACDColor(int color) {
        this.mMACDPaint.setColor(color);
    }

    /**
     * 设置MACD的宽度
     *
     * @param MACDWidth
     */
    public void setMACDWidth(float MACDWidth) {
        mMACDWidth = MACDWidth;
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mDEAPaint.setStrokeWidth(width);
        mDIFPaint.setStrokeWidth(width);
        mMACDPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mDEAPaint.setTextSize(textSize);
        mDIFPaint.setTextSize(textSize);
        mMACDPaint.setTextSize(textSize);
    }

    public void setTextFontFamily(String fontFamily) {
        Typeface typeface = HTKLineConfigManager.findFont(mContext, fontFamily);
        mRedPaint.setTypeface(typeface);
        mGreenPaint.setTypeface(typeface);
        mDIFPaint.setTypeface(typeface);
        mDEAPaint.setTypeface(typeface);
        mMACDPaint.setTypeface(typeface);
        primaryPaint.setTypeface(typeface);
    }


}
