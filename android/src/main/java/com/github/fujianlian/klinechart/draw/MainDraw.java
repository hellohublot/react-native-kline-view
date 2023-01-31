package com.github.fujianlian.klinechart.draw;

import android.content.Context;
import android.graphics.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.fujianlian.klinechart.*;
import com.github.fujianlian.klinechart.base.IChartDraw;
import com.github.fujianlian.klinechart.base.IValueFormatter;
import com.github.fujianlian.klinechart.entity.ICandle;
import com.github.fujianlian.klinechart.formatter.ValueFormatter;
import com.github.fujianlian.klinechart.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 主图的实现类
 * Created by tifezh on 2016/6/14.
 */
public class MainDraw implements IChartDraw<ICandle> {

    private float mCandleWidth = 0;
    private float mCandleLineWidth = 0;

    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma30Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint minuteGradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context mContext;

    private boolean mCandleSolid = true;

    private PrimaryStatus primaryStatus = PrimaryStatus.MA;
    private KLineChartView kChartView;

    public MainDraw(BaseKLineChartView view) {
        Context context = view.getContext();
        kChartView = (KLineChartView) view;
        mContext = context;


        mLinePaint.setColor(ContextCompat.getColor(context, R.color.chart_line));
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStyle(Paint.Style.STROKE);


        minuteGradientPaint.setStrokeJoin(Paint.Join.ROUND);
        minuteGradientPaint.setStrokeCap(Paint.Cap.ROUND);
        minuteGradientPaint.setStyle(Paint.Style.FILL);
    }

    public void reloadColor(BaseKLineChartView view) {
        mRedPaint.setColor(view.configManager.increaseColor);
        mGreenPaint.setColor(view.configManager.decreaseColor);
        mLinePaint.setColor(view.configManager.minuteLineColor);
    }

    public void setPrimaryStatus(PrimaryStatus primaryStatus) {
        this.primaryStatus = primaryStatus;
    }

    public PrimaryStatus getPrimaryStatus() {
        return primaryStatus;
    }


    public void drawMinuteMinute(float top, int startIndex, float bottom, int stopIndex, @NonNull Canvas canvas, @NonNull BaseKLineChartView view) {
        if (!view.isMinute) {
            return;
        }
        float r = mCandleWidth / 2;
        LinearGradient linearGradient = new LinearGradient(
                0,
                0,
                0,
                bottom - top,
                view.configManager.minuteGradientColorList,
                view.configManager.minuteGradientLocationList,
                Shader.TileMode.CLAMP
        );
//        minuteGradientPaint.setColor(Color.BLUE);
        minuteGradientPaint.setShader(linearGradient);

        Path path = new Path();
        for (int i = startIndex; i <= stopIndex; i++) {
            ICandle currentPoint = (ICandle) view.getItem(i);
            float currentX = view.getItemMiddleScrollX(i);
            float currentY = view.yFromValue(currentPoint.getClosePrice());
            ICandle lastPoint = i == 0 ? currentPoint : (ICandle) view.getItem(i - 1);

            float lastX = i == 0 ? currentX : view.getItemMiddleScrollX(i - 1);
            float lastY = view.yFromValue(lastPoint.getClosePrice());
            float centerX = (currentX - lastX) / 2 + lastX;
            float centerY = (currentY - lastY) / 2 + lastY;
            if (i == startIndex) {
                path.moveTo(lastX, lastY);
            }
            path.cubicTo(centerX, lastY, centerX, currentY, currentX, currentY);
        }
        Path gradientPath = new Path(path);
        gradientPath.lineTo(view.getItemMiddleScrollX(stopIndex), view.getMainBottom());
        gradientPath.lineTo(view.getItemMiddleScrollX(startIndex), view.getMainBottom());
//        gradientPath.lineTo(view.getX(startIndex), top);
        gradientPath.close();
        canvas.drawPath(gradientPath, minuteGradientPaint);
        canvas.drawPath(path, mLinePaint);

    }

    @Override
    public void drawTranslated(@Nullable ICandle lastPoint, @NonNull ICandle curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {
        if (view.isMinute) {
            return;
        }
        drawCandle(view, canvas, curX, curPoint.getHighPrice(), curPoint.getLowPrice(), curPoint.getOpenPrice(), curPoint.getClosePrice());
        if (primaryStatus == PrimaryStatus.MA) {
            KLineEntity lastItem = (KLineEntity) lastPoint;
            KLineEntity currentItem = (KLineEntity) curPoint;
            for (int i = 0; i < view.configManager.maList.size(); i ++) {
                HTKLineTargetItem currentTargetItem = (HTKLineTargetItem) currentItem.maList.get(i);
                HTKLineTargetItem lastTargetItem = (HTKLineTargetItem) lastItem.maList.get(i);
                primaryPaint.setColor(view.configManager.targetColorList[view.configManager.maList.get(i).index]);
                view.drawMainLine(canvas, this.primaryPaint, lastX, lastTargetItem.value, curX, currentTargetItem.value);
            }
        } else if (primaryStatus == PrimaryStatus.BOLL) {
            //画boll
            if (lastPoint.getMb() != 0) {
                primaryPaint.setColor(view.configManager.targetColorList[0]);
                view.drawMainLine(canvas, primaryPaint, lastX, lastPoint.getMb(), curX, curPoint.getMb());
            }
            if (lastPoint.getUp() != 0) {
                primaryPaint.setColor(view.configManager.targetColorList[1]);
                view.drawMainLine(canvas, primaryPaint, lastX, lastPoint.getUp(), curX, curPoint.getUp());
            }
            if (lastPoint.getDn() != 0) {
                primaryPaint.setColor(view.configManager.targetColorList[2]);
                view.drawMainLine(canvas, primaryPaint, lastX, lastPoint.getDn(), curX, curPoint.getDn());
            }
        }

    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        KLineEntity point = (KLineEntity) view.getItem(position);
        String text = "";
        String space = "  ";
        if (view.isMinute) {

        } else {
            if (primaryStatus == PrimaryStatus.MA) {
                for (int i = 0; i < view.configManager.maList.size(); i ++) {
                    HTKLineTargetItem targetItem = (HTKLineTargetItem) point.maList.get(i);
                    this.primaryPaint.setColor(view.configManager.targetColorList[view.configManager.maList.get(i).index]);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("MA");
                    stringBuilder.append(targetItem.title);
                    stringBuilder.append(":");
                    stringBuilder.append(view.formatValue(targetItem.value));
                    stringBuilder.append(space);
                    text = stringBuilder.toString();
                    canvas.drawText(text, x, y, this.primaryPaint);
                    x += this.primaryPaint.measureText(text);
                }
            } else if (primaryStatus == PrimaryStatus.BOLL) {
                if (point.getMb() != 0) {
                    text = "BOLL:" + view.formatValue(point.getMb()) + space;
                    this.primaryPaint.setColor(view.configManager.targetColorList[0]);
                    canvas.drawText(text, x, y, primaryPaint);
                    x += ma5Paint.measureText(text);
                    text = "UB:" + view.formatValue(point.getUp()) + space;
                    this.primaryPaint.setColor(view.configManager.targetColorList[1]);
                    canvas.drawText(text, x, y, primaryPaint);
                    x += ma10Paint.measureText(text);
                    text = "LB:" + view.formatValue(point.getDn());
                    this.primaryPaint.setColor(view.configManager.targetColorList[2]);
                    canvas.drawText(text, x, y, primaryPaint);
                }
            }
        }
    }

    public float findIsMaxValue(ICandle point, final boolean isMax) {
        final KLineEntity item = (KLineEntity) point;
        ArrayList<Float> valueList = new ArrayList<Float>(){{
            add(item.getHighPrice());
            add(item.getLowPrice());
        }};
        if (primaryStatus == PrimaryStatus.MA) {
            valueList.add(item.targetListISMax(item.maList, isMax));
        } else if (primaryStatus == PrimaryStatus.BOLL) {
            valueList.add(item.getMb());
            valueList.add(item.getUp());
            valueList.add(item.getDn());
        }
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (float value: valueList) {
            if (isMax) {
                max = Math.max(max, value);
            } else {
                min = Math.min(min, value);
            }
        }
        if (isMax) {
            return max;
        }
        return min;

    }

    @Override
    public float getMaxValue(final ICandle point) {
        return findIsMaxValue(point, true);
    }

    @Override
    public float getMinValue(ICandle point) {
        return findIsMaxValue(point, false);
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    /**
     * 画Candle
     *
     * @param canvas
     * @param x      x轴坐标
     * @param high   最高价
     * @param low    最低价
     * @param open   开盘价
     * @param close  收盘价
     */
    private void drawCandle(BaseKLineChartView view, Canvas canvas, float x, float high, float low, float open, float close) {
        high = view.yFromValue(high);
        low = view.yFromValue(low);
        open = view.yFromValue(open);
        close = view.yFromValue(close);
        float r = mCandleWidth / 2;
        float lineR = mCandleLineWidth / 2;
        if (open > close) {
            //实心
            if (mCandleSolid) {
                canvas.drawRect(x - r, close, x + r, open, mRedPaint);
                canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint);
            } else {
                mRedPaint.setStrokeWidth(mCandleLineWidth);
                canvas.drawLine(x, high, x, close, mRedPaint);
                canvas.drawLine(x, open, x, low, mRedPaint);
                canvas.drawLine(x - r + lineR, open, x - r + lineR, close, mRedPaint);
                canvas.drawLine(x + r - lineR, open, x + r - lineR, close, mRedPaint);
                mRedPaint.setStrokeWidth(mCandleLineWidth * view.getScaleX());
                canvas.drawLine(x - r, open, x + r, open, mRedPaint);
                canvas.drawLine(x - r, close, x + r, close, mRedPaint);
            }

        } else if (open < close) {
            canvas.drawRect(x - r, open, x + r, close, mGreenPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, mGreenPaint);
        } else {
            canvas.drawRect(x - r, open, x + r, close + 1, mRedPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint);
        }
    }

    /**
     * draw选择器
     *
     * @param view
     * @param canvas
     */
    public void drawSelector(final BaseKLineChartView view, Canvas canvas) {
        if (view.isMinute) {
            return;
        }
        Paint.FontMetrics metrics = mSelectorTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        final int index = view.getSelectedIndex();
        float padding = ViewUtil.Dp2Px(mContext, 7);
        float lineHeight = ViewUtil.Dp2Px(mContext, 8);
        float margin = ViewUtil.Dp2Px(mContext, 5);
        float width = 0;
        float left;
        float top = margin + view.getTopPadding();
        final KLineEntity point = (KLineEntity) view.getItem(index);


        List<Map<String, Object>> itemList = point.selectedItemList;

        float height = padding * 2 + (textHeight + lineHeight) * itemList.size() - lineHeight;

        for (int i = 0; i < itemList.size(); i ++) {
            Map<String, Object> map = itemList.get(i);
            String leftString = (String) map.get("title");
            String rightString = (String) map.get("detail");
            width = Math.max(width, mSelectorTextPaint.measureText(leftString + rightString));
        }

        width += padding * 2;
        width = Math.max(width, view.configManager.panelMinWidth);

        float x = view.scrollXtoViewX(view.getItemMiddleScrollX(index));
        if (x > view.getChartWidth() / 2) {
            left = margin;
        } else {
            left = view.getChartWidth() - width - margin;
        }

        RectF r = new RectF(left, top, left + width, top + height);

        mSelectorBackgroundPaint.setStyle(Paint.Style.FILL);
        mSelectorBackgroundPaint.setColor(view.configManager.panelBackgroundColor);
        canvas.drawRoundRect(r, 5, 5, mSelectorBackgroundPaint);

        mSelectorBackgroundPaint.setStyle(Paint.Style.STROKE);
        mSelectorBackgroundPaint.setStrokeWidth(1);
        mSelectorBackgroundPaint.setColor(view.configManager.panelBorderColor);
        canvas.drawRoundRect(r, 5, 5, mSelectorBackgroundPaint);

        float y = top + padding + (textHeight - metrics.bottom - metrics.top) / 2;

        for (int i = 0; i < itemList.size(); i ++) {
            Map<String, Object> map = itemList.get(i);
            String leftString = (String) map.get("title");
            String rightString = (String) map.get("detail");
            mSelectorTextPaint.setTextSize(view.configManager.panelTextFontSize);

            int paintColor = view.configManager.candleTextColor;
            mSelectorTextPaint.setColor(paintColor);
            canvas.drawText(leftString, left + padding, y, mSelectorTextPaint);
            if (map.get("color") != null) {
                paintColor = new Integer(((Number) map.get("color")).intValue());
                mSelectorTextPaint.setColor(paintColor);
            }
            canvas.drawText(rightString, r.right - mSelectorTextPaint.measureText(rightString) - padding, y, mSelectorTextPaint);
            y += textHeight + lineHeight;
        }

    }

    /**
     * 设置蜡烛宽度
     *
     * @param candleWidth
     */
    public void setCandleWidth(float candleWidth) {
        mCandleWidth = candleWidth;
    }

    /**
     * 设置蜡烛线宽度
     *
     * @param candleLineWidth
     */
    public void setCandleLineWidth(float candleLineWidth) {
        mCandleLineWidth = candleLineWidth;
    }

    /**
     * 设置ma5颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置ma10颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    /**
     * 设置ma30颜色
     *
     * @param color
     */
    public void setMa30Color(int color) {
        this.ma30Paint.setColor(color);
    }

    /**
     * 设置选择器文字颜色
     *
     * @param color
     */
    public void setSelectorTextColor(int color) {
        mSelectorTextPaint.setColor(color);
    }

    /**
     * 设置选择器文字大小
     *
     * @param textSize
     */
    public void setSelectorTextSize(float textSize) {
        mSelectorTextPaint.setTextSize(textSize);
    }

    /**
     * 设置选择器背景
     *
     * @param color
     */
    public void setSelectorBackgroundColor(int color) {
        mSelectorBackgroundPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        ma30Paint.setStrokeWidth(width);
        ma10Paint.setStrokeWidth(width);
        ma5Paint.setStrokeWidth(width);
        primaryPaint.setStrokeWidth(width);
        mLinePaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        ma30Paint.setTextSize(textSize);
        ma10Paint.setTextSize(textSize);
        ma5Paint.setTextSize(textSize);
        primaryPaint.setTextSize(textSize);
    }

    public void setTextFontFamily(String fontFamily) {
        Typeface typeface = HTKLineConfigManager.findFont(mContext, fontFamily);
        mLinePaint.setTypeface(typeface);
        mRedPaint.setTypeface(typeface);
        mGreenPaint.setTypeface(typeface);
        ma5Paint.setTypeface(typeface);
        ma10Paint.setTypeface(typeface);
        ma30Paint.setTypeface(typeface);
        primaryPaint.setTypeface(typeface);

        minuteGradientPaint.setTypeface(typeface);

        mSelectorTextPaint.setTypeface(typeface);
        mSelectorBackgroundPaint.setTypeface(typeface);
    }

    /**
     * 蜡烛是否实心
     */
    public void setCandleSolid(boolean candleSolid) {
        mCandleSolid = candleSolid;
    }

}
