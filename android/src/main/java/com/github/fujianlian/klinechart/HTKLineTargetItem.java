package com.github.fujianlian.klinechart;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class HTKLineTargetItem {

    private boolean selected = false;
    public int index = 0;
    public String title = "";
    public float value = 0.0f;


    public HTKLineTargetItem(Map valueList) {
        String title = valueList.get("title").toString();
        Object object = valueList.get("value");
        if (object == null) {
            object = new Double(0);
        }
        float value = ((Number) object).floatValue();
        object = valueList.get("selected");
        if (object == null) {
            object = new Boolean(true);
        }
        boolean selected = ((Boolean) object).booleanValue();
        object = valueList.get("index");
        if (object == null) {
            object = new Double(0);
        }
        int index = ((Number) object).intValue();
        this.title = title;
        this.value = value;
        this.selected = selected;
        this.index = index;
    }

    public static ArrayList<HTKLineTargetItem> packModelArray(List<Map> valueList) {
        ArrayList<HTKLineTargetItem> modelArray = new ArrayList();
        for (Object object: valueList) {
            HTKLineTargetItem item = new HTKLineTargetItem((Map) object);
            if (item.selected) {
                modelArray.add(item);
            }
        }
        return modelArray;
    }

}
