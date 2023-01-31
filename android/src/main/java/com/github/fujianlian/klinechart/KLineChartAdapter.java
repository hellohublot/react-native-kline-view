package com.github.fujianlian.klinechart;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据适配器
 * Created by tifezh on 2016/6/18.
 */
public class KLineChartAdapter extends BaseKLineChartAdapter {

    private List<KLineEntity> datas = new ArrayList<>();

    public KLineChartAdapter() {

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public String getDate(int position) {
        return datas.get(position).Date;
    }

    /**
     * 向头部添加数据
     */
    public void addHeaderData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.clear();
            datas.addAll(data);
        }
    }

    /**
     * 向尾部添加数据
     */
    public void addFooterData(List<KLineEntity> data) {
        if (data != null) {
            datas.clear();
            datas.addAll(0, data);
        }
    }

    public void reloadKLineItem(KLineEntity entity) {
    	int findIndex = -1;
    	for (int i = 0; i < datas.size(); i ++) {
    		KLineEntity model = datas.get(i);
    		if (model.getDate().equals(entity.getDate())) {
    			findIndex = i;
    			break;
    		}
    	}
    	if (findIndex != -1) {
    		datas.set(findIndex, entity);
    	} else {
    		datas.add(entity);
    	}
    }

    /**
     * 改变某个点的值
     *
     * @param position 索引值
     */
    public void changeItem(int position, KLineEntity data) {
        datas.set(position, data);
        notifyDataSetChanged();
    }

    /**
     * 数据清除
     */
    public void clearData() {
        datas.clear();
        notifyDataSetChanged();
    }
}
