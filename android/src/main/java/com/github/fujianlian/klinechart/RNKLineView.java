package com.github.fujianlian.klinechart;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.github.fujianlian.klinechart.container.HTKLineContainerView;
import com.github.fujianlian.klinechart.draw.PrimaryStatus;
import com.github.fujianlian.klinechart.draw.SecondStatus;
import com.github.fujianlian.klinechart.formatter.DateFormatter;
import com.github.fujianlian.klinechart.formatter.ValueFormatter;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class RNKLineView extends SimpleViewManager<HTKLineContainerView> {

	public static String onDrawItemDidTouchKey = "onDrawItemDidTouch";

	public static String onDrawItemCompleteKey = "onDrawItemComplete";

	public static String onDrawPointCompleteKey = "onDrawPointComplete";

    @Nonnull
    @Override
    public String getName() {
        return "RNKLineView";
    }

    @Nonnull
    @Override
    protected HTKLineContainerView createViewInstance(@Nonnull ThemedReactContext reactContext) {
    	HTKLineContainerView containerView = new HTKLineContainerView(reactContext);
    	return containerView;
    }

	@Override
	public Map getExportedCustomDirectEventTypeConstants() {
		return MapBuilder.of(
				onDrawItemDidTouchKey, MapBuilder.of("registrationName", onDrawItemDidTouchKey),
				onDrawItemCompleteKey, MapBuilder.of("registrationName", onDrawItemCompleteKey),
				onDrawPointCompleteKey, MapBuilder.of("registrationName", onDrawPointCompleteKey)
		);
	}





    @Override
    public void receiveCommand(final HTKLineContainerView containerView, String commandId, ReadableArray optionArray) {
    	final BaseKLineChartView lineChartView = containerView.klineView;
    	final ReadableArray optionList = optionArray;
        switch (commandId) {
            case "reloadOptionList": {
            	new Thread(new Runnable() {
			        @Override
			        public void run() {
			        	int disableDecimalFeature = JSON.DEFAULT_PARSER_FEATURE & ~Feature.UseBigDecimal.getMask();
		            	Map optionMap = (Map)JSON.parse((String)optionList.toArrayList().get(0), disableDecimalFeature);
		            	containerView.configManager.reloadOptionList(optionMap);
						containerView.post(new Runnable() {
							@Override
							public void run() {
								containerView.reloadConfigManager();
							}
						});
			        }
			    }).start();
            	
                break;
            }
        }
    }



}
