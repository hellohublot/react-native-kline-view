package com.github.fujianlian.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.fujianlian.klinechart.HTKLineConfigManager;
import com.github.fujianlian.klinechart.BaseKLineChartView;
import com.github.fujianlian.klinechart.HTKLineTargetItem;
import com.github.fujianlian.klinechart.KLineEntity;
import com.github.fujianlian.klinechart.base.IChartDraw;
import com.github.fujianlian.klinechart.base.IValueFormatter;
import com.github.fujianlian.klinechart.entity.IVolume;
import com.github.fujianlian.klinechart.formatter.ValueFormatter;
import com.github.fujianlian.klinechart.utils.ViewUtil;

import static android.graphics.Typeface.NORMAL;

/**
 * Created by hjm on 2017/11/14 17:49.
 */
public class VolumeDraw implements IChartDraw<IVolume> {

    private Context mContext;

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public VolumeDraw(BaseKLineChartView view) {
        mContext = view.getContext();
    }

    public void reloadColor(BaseKLineChartView view) {
        mRedPaint.setColor(view.configManager.increaseColor);
        mGreenPaint.setColor(view.configManager.decreaseColor);
    }

    @Override
    public void drawTranslated(
            @Nullable IVolume lastPoint, @NonNull IVolume curPoint, float lastX, float curX,
            @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {

        drawHistogram(canvas, curPoint, lastPoint, curX, view, position);
        KLineEntity lastItem = (KLineEntity) lastPoint;
        KLineEntity currentItem = (KLineEntity) curPoint;
        for (int i = 0; i < view.configManager.maVolumeList.size(); i++) {
            HTKLineTargetItem currentTargetItem = (HTKLineTargetItem) currentItem.maVolumeList.get(i);
            HTKLineTargetItem lastTargetItem = (HTKLineTargetItem) lastItem.maVolumeList.get(i);
            primaryPaint.setColor(view.configManager.targetColorList[view.configManager.maVolumeList.get(i).index]);
            view.drawVolLine(canvas, primaryPaint, lastX, lastTargetItem.value, curX, currentTargetItem.value);
        }
    }

    private void drawHistogram(
            Canvas canvas, IVolume curPoint, IVolume lastPoint, float curX,
            BaseKLineChartView view, int position) {

        float candleWidth = view.isMinute ? view.configManager.minuteVolumeCandleWidth : view.configManager.candleWidth;
        int candleColor = view.configManager.minuteVolumeCandleColor;
        primaryPaint.setColor(candleColor);

        float r = candleWidth / 2;
        float top = view.getVolY(curPoint.getVolume());
        int bottom = view.getVolRect().bottom;
        if (curPoint.getClosePrice() >= curPoint.getOpenPrice()) {//涨
            canvas.drawRect(curX - r, top, curX + r, bottom, view.isMinute ? primaryPaint : mRedPaint);
        } else {
            canvas.drawRect(curX - r, top, curX + r, bottom, view.isMinute ? primaryPaint : mGreenPaint);
        }

    }

    @Override
    public void drawText(
            @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        IValueFormatter formatter = getValueFormatter();
        KLineEntity point = (KLineEntity) view.getItem(position);
        if (formatter instanceof ValueFormatter) {
            ValueFormatter valueFormatter = (ValueFormatter)formatter;
            String space = "  ";
            String text = "VOL:" + valueFormatter.formatVolume(point.getVolume()) + "  ";
            primaryPaint.setColor(view.configManager.targetColorList[5]);
            canvas.drawText(text, x, y, primaryPaint);
            x += view.getTextPaint().measureText(text);
            for (int i = 0; i < view.configManager.maVolumeList.size(); i++) {
                HTKLineTargetItem targetItem = (HTKLineTargetItem) point.maVolumeList.get(i);
                primaryPaint.setColor(view.configManager.targetColorList[view.configManager.maVolumeList.get(i).index]);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MA");
                stringBuilder.append(targetItem.title);
                stringBuilder.append(":");
                stringBuilder.append(valueFormatter.formatVolume(targetItem.value));
                stringBuilder.append(space);
                text = stringBuilder.toString();
                canvas.drawText(text, x, y, this.primaryPaint);
                x += primaryPaint.measureText(text);
            }
        }
    }

    @Override
    public float getMaxValue(IVolume point) {
        KLineEntity item = (KLineEntity) point;
        return Math.max(point.getVolume(), item.targetListISMax(item.maVolumeList, true));
    }

    @Override
    public float getMinValue(IVolume point) {
        KLineEntity item = (KLineEntity) point;
        return Math.min(point.getVolume(), item.targetListISMax(item.maVolumeList, false));
    }

    @Override
    public IValueFormatter getValueFormatter() {
//        return new BigValueFormatter();
        return new ValueFormatter();
    }

    /**
     * 设置 MA5 线的颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置 MA10 线的颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    public void setLineWidth(float width) {
        this.ma5Paint.setStrokeWidth(width);
        this.ma10Paint.setStrokeWidth(width);
        this.primaryPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.ma5Paint.setTextSize(textSize);
        this.ma10Paint.setTextSize(textSize);
        this.primaryPaint.setTextSize(textSize);
    }

    public void setTextFontFamily(String fontFamily) {
        Typeface typeface = HTKLineConfigManager.findFont(mContext, fontFamily);
        mRedPaint.setTypeface(typeface);
        mGreenPaint.setTypeface(typeface);
        ma5Paint.setTypeface(typeface);
        ma10Paint.setTypeface(typeface);
        primaryPaint.setTypeface(typeface);
    }

}
