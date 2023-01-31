package com.github.fujianlian.klinechart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.GestureDetector;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.view.GestureDetectorCompat;
import com.github.fujianlian.klinechart.draw.*;

import static android.graphics.Typeface.NORMAL;

/**
 * k线图
 * Created by tian on 2016/5/20.
 */
public class KLineChartView extends BaseKLineChartView {

    ProgressBar mProgressBar;
    private boolean isRefreshing = false;
    private boolean isLoadMoreEnd = false;
    private boolean mLastScrollEnable;
    private boolean mLastScaleEnable;

    private KChartRefreshListener mRefreshListener;

    private MACDDraw mMACDDraw;
    private RSIDraw mRSIDraw;
    private MainDraw mMainDraw;
    private KDJDraw mKDJDraw;
    private WRDraw mWRDraw;
    private VolumeDraw mVolumeDraw;

    public KLineChartView(Context context, HTKLineConfigManager configManager) {
        super(context, configManager);
        initView();
        reloadAttributedList(null);
    }

    public void reloadColor() {
        this.mMainDraw.reloadColor(this);
        this.mMACDDraw.reloadColor(this);
        this.mKDJDraw.reloadColor(this);
        this.mVolumeDraw.reloadColor(this);
    }

    public void changeSecondDrawType(SecondStatus secondStatus) {
        switch (secondStatus) {
            case MACD: {
                setChildDraw(0);
                break;
            }
            case KDJ: {
                setChildDraw(1);
                break;
            }
            case RSI: {
                setChildDraw(2);
                break;
            }
            case WR: {
                setChildDraw(3);
                break;
            }
            case NONE: {
                hideChildDraw();
                break;
            }
        }
    }

    private void initView() {
        mProgressBar = new ProgressBar(getContext());
        LayoutParams layoutParams = new LayoutParams(dp2px(50), dp2px(50));
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(mProgressBar, layoutParams);
        mProgressBar.setVisibility(GONE);
        mVolumeDraw = new VolumeDraw(this);
        mMACDDraw = new MACDDraw(this);
        mWRDraw = new WRDraw(this);
        mKDJDraw = new KDJDraw(this);
        mRSIDraw = new RSIDraw(this);
        mMainDraw = new MainDraw(this);
        addChildDraw(mMACDDraw);
        addChildDraw(mKDJDraw);
        addChildDraw(mRSIDraw);
        addChildDraw(mWRDraw);
        setVolDraw(mVolumeDraw);
        setMainDraw(mMainDraw);
    }

    private void reloadAttributedList(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.KLineChartView);
        if (array != null) {
            try {
                //public
                setPointWidth(array.getDimension(R.styleable.KLineChartView_kc_point_width, getDimension(R.dimen.chart_point_width)));
                setTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.chart_text_size)));
                setTextColor(array.getColor(R.styleable.KLineChartView_kc_text_color, getColor(R.color.chart_text)));
                setMTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.chart_text_size)));
                setMTextColor(array.getColor(R.styleable.KLineChartView_kc_text_color, getColor(R.color.chart_white)));
                setLineWidth(array.getDimension(R.styleable.KLineChartView_kc_line_width, getDimension(R.dimen.chart_line_width)));
                setBackgroundColor(array.getColor(R.styleable.KLineChartView_kc_background_color, getColor(R.color.chart_bac)));
                setSelectPointColor(array.getColor(R.styleable.KLineChartView_kc_background_color, getColor(R.color.chart_point_bac)));

                setSelectedXLineColor(Color.WHITE);
                setSelectedXLineWidth(getDimension(R.dimen.chart_line_width));

                setSelectedYLineColor(Color.parseColor("#8040424D"));
                setSelectedYLineWidth(getDimension(R.dimen.chart_point_width));

                setGridLineWidth(array.getDimension(R.styleable.KLineChartView_kc_grid_line_width, getDimension(R.dimen.chart_grid_line_width)));
                setGridLineColor(array.getColor(R.styleable.KLineChartView_kc_grid_line_color, getColor(R.color.chart_grid_line)));
                //macd
                setMACDWidth(array.getDimension(R.styleable.KLineChartView_kc_macd_width, getDimension(R.dimen.chart_candle_width)));
                setDIFColor(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                setDEAColor(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.chart_ma10)));
                setMACDColor(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.chart_ma30)));
                //kdj
                setKColor(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                setDColor(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.chart_ma10)));
                setJColor(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.chart_ma30)));
                //wr
                setRColor(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                //rsi
                setRSI1Color(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                setRSI2Color(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.chart_ma10)));
                setRSI3Color(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.chart_ma30)));
                //main
                setMa5Color(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.chart_ma5)));
                setMa10Color(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.chart_ma10)));
                setMa30Color(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.chart_ma30)));
                setCandleWidth(array.getDimension(R.styleable.KLineChartView_kc_candle_width, getDimension(R.dimen.chart_candle_width)));
                setCandleLineWidth(array.getDimension(R.styleable.KLineChartView_kc_candle_line_width, getDimension(R.dimen.chart_candle_line_width)));
                setSelectorBackgroundColor(array.getColor(R.styleable.KLineChartView_kc_selector_background_color, getColor(R.color.chart_selector)));
                setSelectorTextSize(array.getDimension(R.styleable.KLineChartView_kc_selector_text_size, getDimension(R.dimen.chart_selector_text_size)));
                setCandleSolid(array.getBoolean(R.styleable.KLineChartView_kc_candle_solid, true));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                array.recycle();
            }
        }
    }

    private float getDimension(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    private int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    @Override
    public void onLeftSide() {
        // showLoading();
    }

    @Override
    public void onRightSide() {
    }

    public void showLoading() {
        if (!isLoadMoreEnd && !isRefreshing) {
            isRefreshing = true;
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            if (mRefreshListener != null) {
                mRefreshListener.onLoadMoreBegin(this);
            }
            mLastScaleEnable = isScaleEnable();
            mLastScrollEnable = isScrollEnable();
            super.setScrollEnable(false);
            super.setScaleEnable(false);
        }
    }

    public void justShowLoading() {
        if (!isRefreshing) {
            isLongPress = false;
            isRefreshing = true;
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            if (mRefreshListener != null) {
                mRefreshListener.onLoadMoreBegin(this);
            }
            mLastScaleEnable = isScaleEnable();
            mLastScrollEnable = isScrollEnable();
            super.setScrollEnable(false);
            super.setScaleEnable(false);
        }
    }

    private void hideLoading() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        super.setScrollEnable(mLastScrollEnable);
        super.setScaleEnable(mLastScaleEnable);
    }

    /**
     * 隐藏选择器内容
     */
    public void hideSelectData() {
        isLongPress = false;
        // invalidate();
    }

    /**
     * 刷新完成
     */
    public void refreshComplete() {
        isRefreshing = false;
        hideLoading();
    }

    /**
     * 刷新完成，没有数据
     */
    public void refreshEnd() {
        isLoadMoreEnd = true;
        isRefreshing = false;
        hideLoading();
    }

    /**
     * 重置加载更多
     */
    public void resetLoadMoreEnd() {
        isLoadMoreEnd = false;
    }

    public void setLoadMoreEnd() {
        isLoadMoreEnd = true;
    }

    public interface KChartRefreshListener {
        /**
         * 加载更多
         *
         * @param chart
         */
        void onLoadMoreBegin(KLineChartView chart);
    }

    @Override
    public void setScaleEnable(boolean scaleEnable) {
        if (isRefreshing) {
            throw new IllegalStateException("请勿在刷新状态设置属性");
        }
        super.setScaleEnable(scaleEnable);

    }

    @Override
    public void setScrollEnable(boolean scrollEnable) {
        if (isRefreshing) {
            throw new IllegalStateException("请勿在刷新状态设置属性");
        }
        super.setScrollEnable(scrollEnable);
    }

    /**
     * 设置DIF颜色
     */
    public void setDIFColor(int color) {
        mMACDDraw.setDIFColor(color);
    }

    /**
     * 设置DEA颜色
     */
    public void setDEAColor(int color) {
        mMACDDraw.setDEAColor(color);
    }

    /**
     * 设置MACD颜色
     */
    public void setMACDColor(int color) {
        mMACDDraw.setMACDColor(color);
    }

    /**
     * 设置MACD的宽度
     *
     * @param MACDWidth
     */
    public void setMACDWidth(float MACDWidth) {
        mMACDDraw.setMACDWidth(MACDWidth);
    }

    /**
     * 设置K颜色
     */
    public void setKColor(int color) {
        mKDJDraw.setKColor(color);
    }

    /**
     * 设置D颜色
     */
    public void setDColor(int color) {
        mKDJDraw.setDColor(color);
    }

    /**
     * 设置J颜色
     */
    public void setJColor(int color) {
        mKDJDraw.setJColor(color);
    }

    /**
     * 设置R颜色
     */
    public void setRColor(int color) {
        mWRDraw.setRColor(color);
    }

    /**
     * 设置ma5颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        mMainDraw.setMa5Color(color);
        mVolumeDraw.setMa5Color(color);
    }

    /**
     * 设置ma10颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        mMainDraw.setMa10Color(color);
        mVolumeDraw.setMa10Color(color);
    }

    /**
     * 设置ma20颜色
     *
     * @param color
     */
    public void setMa30Color(int color) {
        mMainDraw.setMa30Color(color);
    }

    /**
     * 设置选择器文字大小
     *
     * @param textSize
     */
    public void setSelectorTextSize(float textSize) {
        mMainDraw.setSelectorTextSize(textSize);
    }

    /**
     * 设置选择器背景
     *
     * @param color
     */
    public void setSelectorBackgroundColor(int color) {
        mMainDraw.setSelectorBackgroundColor(color);
    }

    /**
     * 设置蜡烛宽度
     *
     * @param candleWidth
     */
    public void setCandleWidth(float candleWidth) {
        mMainDraw.setCandleWidth(candleWidth);
    }

    /**
     * 设置蜡烛线宽度
     *
     * @param candleLineWidth
     */
    public void setCandleLineWidth(float candleLineWidth) {
        mMainDraw.setCandleLineWidth(candleLineWidth);
    }

    /**
     * 蜡烛是否空心
     */
    public void setCandleSolid(boolean candleSolid) {
        mMainDraw.setCandleSolid(candleSolid);
    }

    public void setRSI1Color(int color) {
        mRSIDraw.setRSI1Color(color);
    }

    public void setRSI2Color(int color) {
        mRSIDraw.setRSI2Color(color);
    }

    public void setRSI3Color(int color) {
        mRSIDraw.setRSI3Color(color);
    }

    @Override
    public void setTextSize(float textSize) {
        super.setTextSize(textSize);
        mMainDraw.setTextSize(textSize);
        mRSIDraw.setTextSize(textSize);
        mMACDDraw.setTextSize(textSize);
        mKDJDraw.setTextSize(textSize);
        mWRDraw.setTextSize(textSize);
        mVolumeDraw.setTextSize(textSize);
    }

    public void setTextFontFamily(String fontFamily) {
        super.setTextFontFamily(fontFamily);
        mMainDraw.setTextFontFamily(fontFamily);
        mRSIDraw.setTextFontFamily(fontFamily);
        mMACDDraw.setTextFontFamily(fontFamily);
        mKDJDraw.setTextFontFamily(fontFamily);
        mWRDraw.setTextFontFamily(fontFamily);
        mVolumeDraw.setTextFontFamily(fontFamily);
    }

    @Override
    public void setLineWidth(float lineWidth) {
        super.setLineWidth(lineWidth);
        mMainDraw.setLineWidth(lineWidth);
        mRSIDraw.setLineWidth(lineWidth);
        mMACDDraw.setLineWidth(lineWidth);
        mKDJDraw.setLineWidth(lineWidth);
        mWRDraw.setLineWidth(lineWidth);
        mVolumeDraw.setLineWidth(lineWidth);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        mMainDraw.setSelectorTextColor(color);
    }

    /**
     * 设置刷新监听
     */
    public void setRefreshListener(KChartRefreshListener refreshListener) {
        mRefreshListener = refreshListener;
    }

    public void setMainDrawLine(boolean isLine) {
        this.isMinute = isLine;
        // invalidate();
    }

    private int startX;
    private int startY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dX = (int) (ev.getX() - startX);
                int dY = (int) (ev.getY() - startX);
                if (Math.abs(dX) > Math.abs(dY)) {
                    //左右滑动
                    return true;
                } else {
                    //上下滑动
                    return false;
                }
            case MotionEvent.ACTION_UP:
                break;
            default:
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (!isRefreshing) {
            super.onLongPress(e);
        }
    }
}
