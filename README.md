## react-native-kline-view

react-native-kline-view is a pixel-level imitation of Huobi's k-line chart library. It is completely done natively, so while providing high-performance charts, it can also realize scrolling, zooming, long-pressing, finger drawing marks, etc.

<image src="./example/1.png" width="300">
<image src="./example/2.png" width="300">

## Features

- [x] Support ScrollView native high-performance scrolling
- [x] Support the calculation and display of various indicators such as MA, BOLL, MACD, KDJ, RSI, and WR
- [x] Support gesture zoom chart
- [x] Support long press to display details panel
- [x] Support custom colors and font sizes
- [x] Support square mirror display
- [x] Support for finger marker drawing
- [x] Support color and pen customization for finger marker drawing




## Install

```bash
yarn add 'https://github.com/hellohublot/react-native-kline-view.git'
```

## Usage

[View Full Example](./example/App.js)


```javascript
import RNKLineView from 'react-native-kline-view'

UIManager.dispatchViewManagerCommand(
			findNodeHandle(this.kLineView),
			'reloadOptionList',
			[JSON.stringify(optionList)]
)

```

### OptionList

| OptionList| Class  | Default Value | Description |
| -------------- | ---| --- |------------------------------------------------------------------------------------------------------------------------------------ |
| modelArray          |array | `[]`    | k-line data list, See ModalArray |
| shouldScrollToEnd    | boolean  | `true`  | Whether to scroll to the last item after refreshing the data |
| targetList           | object | `[]`    | Indicator Calculation Parameters |
| price                | number | `2`     | Price decimal places |
| volume               | number | `2`     | Volume decimal places |
| primary              | number | `0`     | Main chart type |
| second               | number | `0`     | Child chart type |
| time                 | number | `0`     | Time type |
| configList           | object | `{}`    | Chart Draw Parameters, See ConfigList |
| drawList             | object | `{}`    | Finger Draw Parameters, See DrawList |

### ModalArray
| ModelArray | Class  | Default Value | Description |
| -------------- | ---| --- |------------------------------------------------------------------------------------------------------------------------------------ |
| id          |number | `0`    | time | 
| high          |number | `0`    | high price | 
| low          |number | `0`    | low price | 
| open          |number | `0`    | open price | 
| close          |number | `0`    | current price | 
| volume          |number | `0`    | current trade volume | 

### ConfigList

| ConfigList   | Class   | Default Value | Description |
| -------------- | ---| --- |------------------------------------------------------------------------------------------------------------------------------------ |
| colorList        | object | `{}`    |  increase and decrease theme color list |
| targetColorList       | object | `{}`    | indictor line color list |
| minuteLineColor        | int_color | `0`    |  minute time line color   |
| minuteGradientColorList      | object | `{}`    | minute time background gradient color list  |
| mainFlex        | number | `0`    |  main chart flex |
| volumeFlex        | number | `0`    | volume chart flex |
| minuteGradientLocationList    | object   | `{}`    | minute time background gradient location list |
| paddingRight      | number | `0`    |  padding right |
| paddingTop      |  number | `0`    | padding top  |
| paddingBottom    | number   | `0`    | padding bottom  |
| itemWidth      | number | `0`    |  candle and margin width |
| candleWidth      | number | `0`    | only candle width  |
| minuteVolumeCandleColor     | int_color  | `0`    |  minute time volume candle color |
| minuteVolumeCandleWidth     | number  | `0`    |  minute time volume candle width |
| macdCandleWidth     |  number  | `0`    |  macd candle width |
| fontFamily      | string |     | global font family |
| textColor      |  int_color | `0`    | global text color |
| headerTextFontSize      |  number | `0`    | main chart header text size  |
| rightTextFontSize      | number | `0`    |  ruler text size |
| candleTextFontSize    |  number  | `0`    |  candle high low price text size |
| candleTextColor     |  int_color  | `0`    | candle high low price text color  |
| panelGradientColorList     |  object  | `{}`    |  detail panel gradient color list |
| panelGradientLocationList     |  object  | `{}`    | detail panel gradient location list  |
| panelBackgroundColor    |   int_color  | `0`    |  detail panel background color  |
| panelBorderColor      |  int_color  | `0`    | detail panel border color  |
| selectedPointContainerColor     |  int_color  | `0`    |  long press selected background color |
| panelMinWidth      |  number | `0`    | detail panel min width |
| panelTextFontSize      | number | `0`    | detail panel text font size  |
| closePriceCenterSeparatorColor     |  int_color   | `0`    | close price at center line color  |
| closePriceCenterBackgroundColor    |  int_color  | `0`    |  close price at center background color |
| closePriceCenterBorderColor    |   int_color | `0`    |  close price at center border color |
| closePriceCenterTriangleColor    |   int_color  | `0`    | close price at center triangle color  |
| closePriceRightSeparatorColor      |  int_color | `0`    | close price at right line color  |
| closePriceRightBackgroundColor      |  int_color | `0`    | close price at right background color  |
| closePriceRightLightLottieFloder    |  string    |     | minute time fading point file floder |
| closePriceRightLightLottieScale     |  string | `0`    |  minute time fading point file scale size |
| closePriceRightLightLottieSource      |  string |     |  minute time fading point file resource int flag |


### DrawList
| DrawList  | Class | Default Value | Description |
| --------------| --- | --- |------------------------------------------------------------------------------------------------------------------------------------ |
| shotBackgroundColor   | int_color | `0`    | shot backgroundColor |

## Contact

hellohublot, hublot@aliyun.com
