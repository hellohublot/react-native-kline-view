package com.github.fujianlian.klinechart.container;


import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import com.github.fujianlian.klinechart.BaseKLineChartView;

import java.util.ArrayList;
import java.util.List;

public class HTDrawItem {

    public HTDrawType drawType = HTDrawType.none;

    public int drawColor = Color.RED;

    public float drawLineHeight = 1;

    public float drawDashWidth = 1;

    public float drawDashSpace = 1;

    public Boolean drawIsLock = false;

    public List<HTPoint> pointList = new ArrayList<>();

    private List<Integer> touchMoveIndexList = new ArrayList<>();

    public HTDrawItem(HTDrawType drawType, HTPoint startPoint) {
        this.drawType = drawType;
        this.pointList.add(startPoint);
    }

    // 找到谁正在被拖动
    public static HTDrawItem findTouchMoveItem(List<HTDrawItem> drawItemList) {
        for (HTDrawItem drawItem: drawItemList) {
            if (drawItem.touchMoveIndexList.size() > 0) {
                return drawItem;
            }
        }
        return null;
    }

    // 如果是线段, 填充所有的点到 touchMoveIndexList
    public static void fillAllTouchMoveItem(HTDrawItem drawItem) {
        drawItem.touchMoveIndexList.clear();
        for (int i = 0; i < drawItem.pointList.size(); i ++) {
            drawItem.touchMoveIndexList.add(i);
        }
    }

    // 计算某个点到另外两个点连成的线之间的垂直距离
    public static float pedalPoint(HTPoint p1, HTPoint p2, HTPoint x0) {
        float a = p2.y - p1.y;
        float b = p1.x - p2.x;
        float c = p2.x * p1.y - p1.x * p2.y;
//        let x = (b * b * x0.x - a * b * x0.y - a * c) / (a * a + b * b)
//        let y = (-a * b * x0.x + a * a * x0.y - b * c) / (a * a + b * b)
        float d = Math.abs((a * x0.x + b * x0.y + c)) / (float)(Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)));
//        let pt = CGPoint(x: x, y: y)
        return d;
    }

    // 计算某个点到另一个点的距离
    public static float distance(HTPoint p1, HTPoint p2) {
        float a = p2.y - p1.y;
        float b = p1.x - p2.x;
        float d = (float)(Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)));
        return d;
    }

    // 计算两个点的中心点
    public static HTPoint centerPoint(HTPoint p1, HTPoint p2) {
        float a = p2.x + p1.x;
        float b = p1.y + p2.y;
        HTPoint p = new HTPoint(a / 2.0f, b / 2.0f);
        return p;
    }

    private static List<HTPoint> createLine(HTPoint startPoint, HTPoint endPoint) {
        List<HTPoint> line = new ArrayList<>();
        line.add(startPoint);
        line.add(endPoint);
        return line;
    }

    public static List<List<HTPoint>> lineListWithIndex(HTDrawItem drawItem, int index, BaseKLineChartView klineView) {
        if (index <= 0 || index > drawItem.pointList.size()) {
            return new ArrayList<>();
        }
        List<List<HTPoint>> lineList = new ArrayList<>();
        HTPoint point = drawItem.pointList.get(index);
        HTPoint lastPoint = drawItem.pointList.get(index - 1);
        switch (drawItem.drawType) {
            case horizontalLine: {
                point.y = lastPoint.y;
                drawItem.pointList.set(index, point);
                break;
            }
            case verticalLine: {
                point.x = lastPoint.x;
                drawItem.pointList.set(index, point);
                break;
            }
            case halfLine: {
                HTPoint viewPoint = klineView.viewPointFromValuePoint(point);
                HTPoint lastViewPoint = klineView.viewPointFromValuePoint(lastPoint);
                HTPoint outPoint = new HTPoint(viewPoint.x, viewPoint.y);
                float xDistance = viewPoint.x - lastViewPoint.x;
                float yDistance = viewPoint.y - lastViewPoint.y;
                float append = klineView.getWidth() + klineView.getHeight();
                float k = 0;
                if (xDistance != 0) {
                    k = yDistance / xDistance;
                }
                if (Math.abs(k) > 1) {
                    append *= yDistance < 0 ? -1 : 1;
                    if (yDistance != 0) {
                        outPoint.x += append / k;
                        outPoint.y += append;
                    } else {
                        outPoint.x += append;
                    }
                } else {
                    if (xDistance == 0 && yDistance < 0) {
                        append *= -1;
                    } else {
                        append *= xDistance < 0 ? -1 : 1;
                    }
                    if (xDistance != 0) {
                        outPoint.x += append;
                        outPoint.y += append * k;
                    } else {
                        outPoint.y += append;
                    }
                }
                lineList.add(createLine(lastPoint, klineView.valuePointFromViewPoint(outPoint)));
                return lineList;
            }
            case parallelLine: {
                if (index == 2) {
                    HTPoint firstPoint = drawItem.pointList.get(0);
                    point.x = Math.min(Math.max(point.x, firstPoint.x), lastPoint.x);
                    drawItem.pointList.set(index, point);

                    float base = (lastPoint.x - firstPoint.x);
                    float k = 1.0f;
                    if (base != 0) {
                        k = (lastPoint.y - firstPoint.y) / base;
                    }
                    float b = point.y - point.x * k;
                    HTPoint previousPoint = new HTPoint(lastPoint.x, (k * lastPoint.x + b));
                    HTPoint nextPoint = new HTPoint(firstPoint.x, (k * firstPoint.x + b));

                    lineList.add(createLine(previousPoint, nextPoint));
                    return lineList;
                }
                break;
            }
            case rectangle: {
                HTPoint previousPoint = new HTPoint(point.x, lastPoint.y);
                HTPoint nextPoint = new HTPoint(lastPoint.x, point.y);
                lineList.add(createLine(lastPoint, previousPoint));
                lineList.add(createLine(previousPoint, point));
                lineList.add(createLine(point, nextPoint));
                lineList.add(createLine(nextPoint, lastPoint));
                return lineList;
            }
            case parallelogram: {
                if (index == 2) {
                    HTPoint firstPoint = drawItem.pointList.get(0);

                    float base = (lastPoint.x - firstPoint.x);
                    float k = 1.0f;
                    if (base != 0) {
                        k = (lastPoint.y - firstPoint.y) / base;
                    }
                    float b = point.y - point.x * k;
                    float nextPointX = firstPoint.x + (point.x - lastPoint.x);
                    HTPoint nextPoint = new HTPoint(nextPointX, (k * nextPointX + b));
                    lineList.add(createLine(lastPoint, point));
                    lineList.add(createLine(point, nextPoint));
                    lineList.add(createLine(nextPoint, firstPoint));
                }
            }
            default: {
                break;
            }
        }
        lineList.add(createLine(point, lastPoint));
        return lineList;
    }

    public static Boolean beganFillTouchMoveItemPointMapper(HTDrawItem drawItem, HTPoint location, BaseKLineChartView klineView) {
        for (int index = 0; index < drawItem.pointList.size(); index ++) {
            HTPoint point = drawItem.pointList.get(index);
            if (distance(klineView.viewPointFromValuePoint(point), klineView.viewPointFromValuePoint(location)) <= 30) {
                drawItem.touchMoveIndexList.clear();
                drawItem.touchMoveIndexList.add(index);
                return true;
            }
        }
        return false;
    }

    public static Boolean beganFillTouchMoveItemMapper(HTDrawItem drawItem, int index, HTPoint location, BaseKLineChartView klineView) {
        HTPoint point = drawItem.pointList.get(index);
        List<List<HTPoint>> lineList = lineListWithIndex(drawItem, index, klineView);
        for (List<HTPoint> lineItem: lineList) {
            HTPoint startPoint = lineItem.get(0);
            HTPoint endPoint = lineItem.get(1);

            HTPoint startViewPoint = klineView.viewPointFromValuePoint(startPoint);
            HTPoint endViewPoint = klineView.viewPointFromValuePoint(endPoint);
            HTPoint viewLocation = klineView.viewPointFromValuePoint(location);

            float distance = pedalPoint(startViewPoint, endViewPoint, viewLocation);
            float minX = Math.min(startViewPoint.x, endViewPoint.x) - 15;
            float maxX = Math.max(startViewPoint.x, endViewPoint.x) + 15;
            float minY = Math.min(startViewPoint.y, endViewPoint.y) - 15;
            float maxY = Math.max(startViewPoint.y, endViewPoint.y) + 15;
            if (distance <= 30 && viewLocation.x > minX && viewLocation.x < maxX && viewLocation.y > minY && viewLocation.y < maxY) {
                fillAllTouchMoveItem(drawItem);
                return true;
            }
        }
        if (index == 2 && drawItem.drawType == HTDrawType.parallelLine) {
            HTPoint firstPoint = drawItem.pointList.get(0);
            HTPoint secondPoint = drawItem.pointList.get(1);
            float minX = Math.min(firstPoint.x, secondPoint.x);
            float maxX = Math.max(firstPoint.x, secondPoint.x);

            float base = (firstPoint.x - secondPoint.x);
            float k = 1.0f;
            if (base != 0) {
                k = (firstPoint.y - secondPoint.y) / base;
            }
            float b1 = firstPoint.y - firstPoint.x * k;
            float b2 = point.y - point.x * k;
            float minB = Math.min(b1, b2);
            float maxB = Math.max(b1, b2);
            if (location.x > minX && location.x < maxX && location.y > k * location.x + minB && location.y < k * location.x + maxB) {
                fillAllTouchMoveItem(drawItem);
                return true;
            }
        }
        return false;
    }

    public static void beganFillTouchMoveItem(List<HTDrawItem> drawItemList, HTPoint location, BaseKLineChartView klineView) {
        clearAllTouchMoveIndexList(drawItemList);
        for (int i = drawItemList.size() - 1; i >= 0; i --) {
            HTDrawItem drawItem = drawItemList.get(i);
            if (beganFillTouchMoveItemPointMapper(drawItem, location, klineView)) {
                return;
            }
        }
        for (int i = drawItemList.size() - 1; i >= 0; i --) {
            HTDrawItem drawItem = drawItemList.get(i);
            for (int index = 0; index < drawItem.pointList.size(); index ++ ) {
                if (beganFillTouchMoveItemMapper(drawItem, index, location, klineView)) {
                    return;
                }
            }
        }
    }

    public static Boolean canResponseTranslation(List<HTDrawItem> drawItemList, HTPoint translation) {
        HTDrawItem touchMoveItem = findTouchMoveItem(drawItemList);
        if (touchMoveItem != null) {
            if (touchMoveItem.drawIsLock) {
                return true;
            }
            for (Integer touchMoveIndex: touchMoveItem.touchMoveIndexList) {
                touchMoveItem.pointList.get(touchMoveIndex).x += translation.x;
                touchMoveItem.pointList.get(touchMoveIndex).y += translation.y;
            }
            return true;
        }
        return false;
    }

    public static void clearAllTouchMoveIndexList(List<HTDrawItem> drawItemList) {
        for (HTDrawItem drawItem: drawItemList) {
            drawItem.touchMoveIndexList = new ArrayList<>();
        }
    }

    public static HTDrawItem canResponseLocation(List<HTDrawItem> drawItemList, HTPoint locatoin, BaseKLineChartView klineView) {
        beganFillTouchMoveItem(drawItemList, locatoin, klineView);
        HTDrawItem drawItem = findTouchMoveItem(drawItemList);
        clearAllTouchMoveIndexList(drawItemList);
        return drawItem;
    }

    public static Boolean canResponseTouch(List<HTDrawItem> drawItemList, HTPoint location, HTPoint translation, int state, BaseKLineChartView klineView) {
        switch (state) {
            case MotionEvent.ACTION_DOWN: {
                beganFillTouchMoveItem(drawItemList, location, klineView);
                return canResponseTranslation(drawItemList, translation);
            }
            case MotionEvent.ACTION_MOVE: {
                return canResponseTranslation(drawItemList, translation);
            }
            case MotionEvent.ACTION_UP: {
                Boolean shouldResponseTranslation = canResponseTranslation(drawItemList, translation);
                clearAllTouchMoveIndexList(drawItemList);
                return shouldResponseTranslation;
            }
        }
        return false;
    }

}
