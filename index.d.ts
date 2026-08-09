import type React from "react";
import type {
  HostComponent,
  NativeSyntheticEvent,
  ReactNode,
  ViewProps,
} from "react-native";

export type RNKLineColorValue = number | string;

export interface RNKLineDrawItemDidTouchPayload {
  shouldReloadDrawItemIndex: number;
  drawColor?: number[];
  drawLineHeight?: number;
  drawDashWidth?: number;
  drawDashSpace?: number;
  drawIsLock?: boolean;
}

export interface RNKLineDrawItemCompletePayload {
  [key: string]: never;
}

export interface RNKLineDrawPointCompletePayload {
  pointCount: number;
}

export interface RNKLineTargetItem {
  title: string;
  selected: boolean;
  index: number;
}

export interface TSelectedIndicators {
  selectedMainIndicator?: number;
  selectedSubIndicator?: number;
}

export type RNKLineDateFormatter = (timestamp: number) => string;

export interface RNKLineModelItem {
  id?: number;
  time?: number;
  open?: number;
  high?: number;
  low?: number;
  close?: number;
  vol?: number;
  volume?: number;
  dateString?: string;
  selectedItemList?: Array<Record<string, unknown>>;
  maList?: RNKLineTargetItem[];
  maVolumeList?: RNKLineTargetItem[];
  openTradePrice?: number;
  closeTradePrice?: number;
  openTradeCount?: number;
  closeTradeCount?: number;
  openTradeTimestamp?: number;
  closeTradeTimestamp?: number;
  [key: string]: unknown;
}

export interface RNKLineTargetList {
  maList?: RNKLineTargetItem[];
  maVolumeList?: RNKLineTargetItem[];
  bollN?: string;
  bollP?: string;
  macdS?: string;
  macdL?: string;
  macdM?: string;
  kdjN?: string;
  kdjM1?: string;
  kdjM2?: string;
  rsiList?: RNKLineTargetItem[];
  wrList?: RNKLineTargetItem[];
}

export interface RNKLineConfigColorList {
  increaseColor?: RNKLineColorValue;
  decreaseColor?: RNKLineColorValue;
}

export interface RNKLineConfigList {
  colorList?: RNKLineConfigColorList;
  targetColorList?: RNKLineColorValue[];
  backgroundColor?: RNKLineColorValue;
  textColor?: RNKLineColorValue;
  gridColor?: RNKLineColorValue;
  candleTextColor?: RNKLineColorValue;
  minuteLineColor?: RNKLineColorValue;
  minuteGradientColorList?: RNKLineColorValue[];
  minuteGradientLocationList?: number[];
  panelBackgroundColor?: RNKLineColorValue;
  panelBorderColor?: RNKLineColorValue;
  panelTextColor?: RNKLineColorValue;
  selectedPointContainerColor?: RNKLineColorValue;
  selectedPointContentColor?: RNKLineColorValue;
  closePriceCenterBackgroundColor?: RNKLineColorValue;
  closePriceCenterBorderColor?: RNKLineColorValue;
  closePriceCenterTriangleColor?: RNKLineColorValue;
  closePriceCenterSeparatorColor?: RNKLineColorValue;
  closePriceRightBackgroundColor?: RNKLineColorValue;
  closePriceRightSeparatorColor?: RNKLineColorValue;
  closePriceRightLightLottieFloder?: string;
  closePriceRightLightLottieScale?: number;
  closePriceRightLightLottieSource?: string;
  panelGradientColorList?: RNKLineColorValue[];
  panelGradientLocationList?: number[];
  mainFlex?: number;
  volumeFlex?: number;
  paddingTop?: number;
  paddingBottom?: number;
  paddingRight?: number;
  itemWidth?: number;
  candleWidth?: number;
  minuteVolumeCandleColor?: RNKLineColorValue;
  minuteVolumeCandleWidth?: number;
  macdCandleWidth?: number;
  headerTextFontSize?: number;
  rightTextFontSize?: number;
  candleTextFontSize?: number;
  panelTextFontSize?: number;
  panelMinWidth?: number;
  fontFamily?: string;
  [key: string]: unknown;
}

export interface RNKLineDrawList {
  drawType?: number;
  shouldReloadDrawItemIndex?: number;
  drawShouldContinue?: boolean;
  drawShouldTrash?: boolean;
  shouldFixDraw?: boolean;
  shouldClearDraw?: boolean;
  shotBackgroundColor?: RNKLineColorValue;
  drawColor?: RNKLineColorValue;
  drawLineHeight?: number;
  drawDashWidth?: number;
  drawDashSpace?: number;
  drawIsLock?: boolean;
  [key: string]: unknown;
}

export interface RNKLineOptionList {
  modelArray?: RNKLineModelItem[];
  shouldScrollToEnd?: boolean;
  scrollEnabled?: boolean;
  fitBarsCount?: number;
  targetList?: RNKLineTargetList;
  configList?: RNKLineConfigList;
  drawList?: RNKLineDrawList;
  primary?: number;
  second?: number;
  time?: number;
  price?: number;
  volume?: number;
  [key: string]: unknown;
}

export type RNKLineOptionListSerialized = string;

export interface TTrade {
  id: string;
  timestamp: number;
  price: number;
  type: "sell" | "buy";
}

export interface RNKLineViewProps extends ViewProps {
  optionList?: RNKLineOptionListSerialized | null;
  tradeComponent?: (trade: TTrade, count: number) => ReactNode;
  onDrawItemDidTouch?: (
    event: NativeSyntheticEvent<RNKLineDrawItemDidTouchPayload>,
  ) => void;
  onDrawItemComplete?: (
    event: NativeSyntheticEvent<RNKLineDrawItemCompletePayload>,
  ) => void;
  onDrawPointComplete?: (
    event: NativeSyntheticEvent<RNKLineDrawPointCompletePayload>,
  ) => void;
}

export type RNKLineViewComponent = HostComponent<RNKLineViewProps>;

export declare const TimeConstants: {
  readonly oneMinute: 1;
  readonly threeMinute: 2;
  readonly fiveMinute: 3;
  readonly fifteenMinute: 4;
  readonly thirtyMinute: 5;
  readonly oneHour: 6;
  readonly fourHour: 7;
  readonly sixHour: 8;
  readonly oneDay: 9;
  readonly oneWeek: 10;
  readonly oneMonth: 11;
  readonly minuteHour: -1;
};

export declare const TimeTypes: {
  readonly 1: { readonly label: "Minute"; readonly value: -1 };
  readonly 3: { readonly label: "3 min"; readonly value: 2 };
  readonly 4: { readonly label: "5 min"; readonly value: 3 };
  readonly 5: { readonly label: "15 min"; readonly value: 4 };
  readonly 6: { readonly label: "30 min"; readonly value: 5 };
  readonly 7: { readonly label: "1 hour"; readonly value: 6 };
};

export declare const DrawTypeConstants: {
  readonly none: 0;
  readonly show: -1;
  readonly line: 1;
  readonly horizontalLine: 2;
  readonly verticalLine: 3;
  readonly halfLine: 4;
  readonly parallelLine: 5;
  readonly rectangle: 101;
  readonly parallelogram: 102;
};

export declare const DrawStateConstants: {
  readonly none: -3;
  readonly showPencil: -2;
  readonly showContext: -1;
};

export declare const IndicatorTypes: {
  readonly main: {
    readonly 1: { readonly label: "MA"; readonly value: "ma" };
    readonly 2: { readonly label: "BOLL"; readonly value: "boll" };
    readonly 0: { readonly label: "NONE"; readonly value: "none" };
  };
  readonly sub: {
    readonly 3: { readonly label: "MACD"; readonly value: "macd" };
    readonly 4: { readonly label: "KDJ"; readonly value: "kdj" };
    readonly 5: { readonly label: "RSI"; readonly value: "rsi" };
    readonly 6: { readonly label: "WR"; readonly value: "wr" };
    readonly 0: { readonly label: "NONE"; readonly value: "none" };
  };
};

export declare const indicatorSelecters: {
  isMASelected: (selectedIndicators: TSelectedIndicators) => boolean;
  isBOLLSelected: (selectedIndicators: TSelectedIndicators) => boolean;
  isMACDSelected: (selectedIndicators: TSelectedIndicators) => boolean;
  isKDJSelected: (selectedIndicators: TSelectedIndicators) => boolean;
  isRSISelected: (selectedIndicators: TSelectedIndicators) => boolean;
  isWRSelected: (selectedIndicators: TSelectedIndicators) => boolean;
};

export declare const DrawToolTypes: Record<
  number,
  {
    readonly label: string;
    readonly value: number;
  }
>;

export declare const getTargetList: (
  selectedIndicators: TSelectedIndicators,
) => RNKLineTargetList;

export declare const normalizeKLineModelArray: (
  modelArray: RNKLineModelItem[],
  getDateString: RNKLineDateFormatter,
) => RNKLineModelItem[];

declare const RNKLineView: React.ForwardRefExoticComponent<
  RNKLineViewProps &
    React.RefAttributes<React.ComponentRef<RNKLineViewComponent>>
>;

export default RNKLineView;
