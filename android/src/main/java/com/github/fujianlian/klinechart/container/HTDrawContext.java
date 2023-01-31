package com.github.fujianlian.klinechart.container;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import com.github.fujianlian.klinechart.BaseKLineChartView;
import com.github.fujianlian.klinechart.HTKLineConfigManager;
import com.github.fujianlian.klinechart.KLineChartView;

import java.util.ArrayList;
import java.util.List;

public class HTDrawContext {

    public List<HTDrawItem> drawItemList = new ArrayList<HTDrawItem>();

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private BaseKLineChartView klineView;

    private HTKLineConfigManager configManager;

    private Boolean breakTouch = false;

    public HTDrawContext(BaseKLineChartView klineView, HTKLineConfigManager configManager) {
        this.klineView = klineView;
        this.configManager = configManager;
    }

    public void touchesGesture(HTPoint location, HTPoint translation, int state) {
        // 能够处理点击, 改变拖动的点, 重新绘制
        if (breakTouch == true) {
            if (state == MotionEvent.ACTION_UP) {
                breakTouch = false;
            }
            return;
        }
        switch (state) {
            case MotionEvent.ACTION_DOWN: {
                if (configManager.shouldReloadDrawItemIndex > HTDrawState.showContext) {
                    HTDrawItem selectedDrawItem = drawItemList.get(configManager.shouldReloadDrawItemIndex);
                    if (selectedDrawItem.pointList.size() >= selectedDrawItem.drawType.count()) {
                        if (HTDrawItem.canResponseLocation(drawItemList, location, klineView) != selectedDrawItem) {
                            configManager.onDrawItemDidTouch.invoke(null, HTDrawState.showPencil);
                            breakTouch = true;
                            invalidate();
                            return;
                        }
                    }
                }
                break;
            }
        }
        if (HTDrawItem.canResponseTouch(drawItemList, location, translation, state, klineView)) {
            if (state == MotionEvent.ACTION_DOWN) {
                HTDrawItem moveItem = HTDrawItem.findTouchMoveItem(drawItemList);
                if (moveItem != null) {
                    int moveItemIndex = drawItemList.indexOf(moveItem);
                    configManager.onDrawItemDidTouch.invoke(moveItem, moveItemIndex);
                }
            }
            invalidate();
            return;
        }
        if (configManager.drawType == HTDrawType.none) {
            return;
        }


        int size = drawItemList.size();
        HTDrawItem drawItem = size > 0 ? drawItemList.get(size - 1) : null;
        switch (state) {
            case MotionEvent.ACTION_DOWN:
            if (drawItem == null || (drawItem.pointList.size() >= drawItem.drawType.count())) {
                drawItem = new HTDrawItem(configManager.drawType, location);
                drawItem.drawColor = configManager.drawColor;
                drawItem.drawLineHeight = configManager.drawLineHeight;
                drawItem.drawDashWidth = configManager.drawDashWidth;
                drawItem.drawDashSpace = configManager.drawDashSpace;
                drawItemList.add(drawItem);
                configManager.onDrawItemDidTouch.invoke(drawItem, drawItemList.size() - 1);
            } else {
                drawItem.pointList.add(location);
            }
            case MotionEvent.ACTION_MOVE: {

            }
            case MotionEvent.ACTION_UP:
                int length = drawItem.pointList.size();
                if (length >= 1) {
                int index = length - 1;
                drawItem.pointList.set(index, location);
                if (state == MotionEvent.ACTION_UP) {
                    configManager.onDrawPointComplete.invoke(drawItem, drawItemList.size() - 1);
                    if (index == drawItem.drawType.count() - 1) {
                        configManager.onDrawItemComplete.invoke(drawItem, drawItemList.size() - 1);
                        if (configManager.drawShouldContinue) {
                            configManager.shouldReloadDrawItemIndex = HTDrawState.showContext;
                        } else {
                            configManager.drawType = HTDrawType.none;
                        }
                    }
                }
            }
            default:
                break;
        }
        invalidate();
    }

    public void invalidate() {
        klineView.invalidate();
    }

    public void fixDrawItemList() {
        int size = drawItemList.size();
        if (size <= 0) {
            return;
        }
        HTDrawItem drawItem = drawItemList.get(size - 1);
        if (drawItem.pointList.size() < drawItem.drawType.count()) {
            drawItemList.remove(drawItem);
        }
        invalidate();
    }

    public void clearDrawItemList() {
        drawItemList = new ArrayList<>();
        invalidate();
    }

    private int colorWithAlphaComponent(int color, double alpha) {
        int reloadColor = (color & 0x00FFFFFF) | ((int)(alpha * 255) << 24);
        return reloadColor;
    }

    private void drawLine(Canvas canvas, HTDrawItem drawItem, HTPoint startPoint, HTPoint endPoint) {
        paint.setColor(drawItem.drawColor);
        paint.setPathEffect(new DashPathEffect(new float[] { drawItem.drawDashWidth, drawItem.drawDashSpace }, 0));
        paint.setStrokeWidth(drawItem.drawLineHeight);
        Path path = new Path();
        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(endPoint.x, endPoint.y);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }

    public void drawMapper(Canvas canvas, HTDrawItem drawItem, int index, int itemIndex) {
        HTPoint point = drawItem.pointList.get(index);
        List<List<HTPoint>> lineList = HTDrawItem.lineListWithIndex(drawItem, index, klineView);
        if (index == 2 && drawItem.drawType == HTDrawType.parallelLine) {
            List<HTPoint> firstLine = lineList.get(0);
            HTPoint startPoint = firstLine.get(0);
            HTPoint endPoint = firstLine.get(1);
            HTPoint firstPoint = drawItem.pointList.get(0);
            HTPoint secondPoint = drawItem.pointList.get(1);
            Path path = new Path();

            HTPoint firstViewPoint = klineView.viewPointFromValuePoint(firstPoint);
            HTPoint secondViewPoint = klineView.viewPointFromValuePoint(secondPoint);
            HTPoint startViewPoint = klineView.viewPointFromValuePoint(startPoint);
            HTPoint endViewPoint = klineView.viewPointFromValuePoint(endPoint);

            path.moveTo(firstViewPoint.x, firstViewPoint.y);
            path.lineTo(secondViewPoint.x, secondViewPoint.y);
            path.lineTo(startViewPoint.x, startViewPoint.y);
            path.lineTo(endViewPoint.x, endViewPoint.y);
            path.close();
            paint.setColor(colorWithAlphaComponent(drawItem.drawColor, 0.5));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, paint);

            HTPoint dashStartPoint = HTDrawItem.centerPoint(firstPoint, endPoint);
            HTPoint dashEndPoint = HTDrawItem.centerPoint(secondPoint, startPoint);
            path = new Path();

            HTPoint dashStartViewPoint = klineView.viewPointFromValuePoint(dashStartPoint);
            HTPoint dashEndViewPoint = klineView.viewPointFromValuePoint(dashEndPoint);

            path.moveTo(dashStartViewPoint.x, dashStartViewPoint.y);
            path.lineTo(dashEndViewPoint.x, dashEndViewPoint.y);
            paint.setColor(colorWithAlphaComponent(drawItem.drawColor, 0.5));
            paint.setPathEffect(new DashPathEffect(new float[] { 4, 4 }, 0));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            canvas.drawPath(path, paint);
        }
        for (List<HTPoint> pointList: lineList) {
            HTPoint startPoint = pointList.get(0);
            HTPoint endPoint = pointList.get(1);
            drawLine(canvas, drawItem, klineView.viewPointFromValuePoint(startPoint), klineView.viewPointFromValuePoint(endPoint));
        }

        if (itemIndex != configManager.shouldReloadDrawItemIndex) {
            return;
        }



        HTPoint viewPoint = klineView.viewPointFromValuePoint(point);

        Path path = new Path();
        paint.setStyle(Paint.Style.FILL);
        path.addCircle(viewPoint.x, viewPoint.y, 20, Path.Direction.CW);
        paint.setColor(colorWithAlphaComponent(drawItem.drawColor, 0.5));
        canvas.drawPath(path, paint);

        path = new Path();
        paint.setStyle(Paint.Style.FILL);
        path.addCircle(viewPoint.x, viewPoint.y, 8, Path.Direction.CW);
        paint.setColor(drawItem.drawColor);
        canvas.drawPath(path, paint);
    }


    public void onDraw(Canvas canvas) {
        for (int itemIndex = 0; itemIndex < drawItemList.size(); itemIndex ++) {
            HTDrawItem drawItem = drawItemList.get(itemIndex);
            for (int index = 0; index < drawItem.pointList.size(); index ++) {
                drawMapper(canvas, drawItem, index, itemIndex);
            }
        }
    }
}
