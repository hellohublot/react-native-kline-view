import React, { useState, useCallback } from "react";
import { View, StyleSheet, requireNativeComponent } from "react-native";
export {
  DrawStateConstants,
  DrawToolTypes,
  DrawTypeConstants,
  IndicatorTypes,
  TimeConstants,
  TimeTypes,
  getTargetList,
  indicatorSelecters,
  normalizeKLineModelArray,
} from "./contract";

const RNKLineViewNative = requireNativeComponent("RNKLineView");

const normalizeTradeMarkers = (markers) => {
  return (markers || [])
    .map((marker, index) => ({
      ...marker,
      originalIndex: index,
      timestamp:
        typeof marker?.timestamp === "number" ? marker.timestamp : index,
    }))
    .sort((left, right) => {
      if (left.timestamp !== right.timestamp) {
        return left.timestamp - right.timestamp;
      }
      return left.originalIndex - right.originalIndex;
    })
    .map((marker, index) => ({
      ...marker,
      zIndex: index + 1,
    }));
};

const RNKLineView = React.forwardRef(function RNKLineView(
  { tradeComponent, style, ...props },
  ref,
) {
  const [markers, setMarkers] = useState([]);

  const onTradeMarkersLayout = useCallback((event) => {
    setMarkers(normalizeTradeMarkers(event.nativeEvent.markers));
  }, []);

  return (
    <View style={style}>
      <RNKLineViewNative
        ref={ref}
        {...props}
        style={StyleSheet.absoluteFill}
        useCustomTradeMarker={!!tradeComponent}
        onTradeMarkersLayout={tradeComponent ? onTradeMarkersLayout : undefined}
      />
      {tradeComponent &&
        markers.map((marker) => {
          const trade = {
            id: String(marker.timestamp) + "_" + marker.type,
            timestamp: marker.timestamp,
            price: marker.price,
            type: marker.type,
          };
          return (
            <View
              key={trade.id}
              pointerEvents="none"
              style={{
                position: "absolute",
                left: marker.x,
                top: marker.y,
                zIndex: marker.zIndex,
                elevation: marker.zIndex,
              }}
            >
              {tradeComponent(trade, marker.count)}
            </View>
          );
        })}
    </View>
  );
});

export default RNKLineView;
