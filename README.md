# Native KLine View

<div align="center">
  <img src="./examples/react-native/logo.png" alt="React Native KLine View" width="120" height="120" style="border-radius: 60px;" />
</div>

**Professional K-Line (Candlestick) Chart Library for React Native, Native iOS/Android, and Flutter**

_Ultra-smooth rendering • Interactive drawing tools • Multiple technical indicators • Dark/Light themes_

English | [中文文档](./README.cn.md)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Platform](https://img.shields.io/badge/platform-ios%20%7C%20android-lightgrey)](https://reactnative.dev)

Native KLine View is a high-performance, feature-rich candlestick chart component designed for professional trading applications. Built with native optimization for both iOS and Android, it delivers smooth 60fps scrolling, zooming, and real-time data updates.

Perfect for cryptocurrency exchanges, stock trading apps, financial dashboards, and any application requiring professional-grade market data visualization.

## 🌟 Features

### 📈 **Advanced Charting**

- ✅ **Ultra-smooth scrolling** with native performance optimization
- ✅ **Pinch-to-zoom** with fluid gesture recognition
- ✅ **Long-press details** with animated info panels
- ✅ **Real-time updates** with efficient data management
- ✅ **Multiple timeframes** (1m, 5m, 15m, 30m, 1h, 4h, 1d, 1w)
- ✅ **Custom trade markers** rendered with React components (`buy`/`sell`, count, price)

### 📊 **Technical Analysis**

- ✅ **Main Chart Indicators**: MA (Moving Average), BOLL (Bollinger Bands)
- ✅ **Sub Chart Indicators**: MACD, KDJ, RSI, WR
- ✅ **Customizable parameters** for all indicators
- ✅ **Multi-color indicator lines** with smooth animations
- ✅ **Volume analysis** with dedicated volume chart

### ✏️ **Interactive Drawing Tools**

- ✅ **Trend Lines** - Diagonal support/resistance analysis
- ✅ **Horizontal Lines** - Price level marking
- ✅ **Vertical Lines** - Time-based event marking
- ✅ **Rectangles** - Range highlighting
- ✅ **Text Annotations** - Custom labels and notes
- ✅ **Drawing persistence** with touch-to-edit functionality

### 🎨 **Visual Excellence**

- ✅ **Dark/Light themes** with instant switching
- ✅ **Gradient backgrounds** for enhanced visual appeal
- ✅ **Customizable colors** for all chart elements
- ✅ **Responsive design** supporting both portrait and landscape
- ✅ **High-DPI support** for crisp rendering on all devices

### 📱 **Platform Support**

- ✅ **iOS & Android** with platform-specific optimizations
- ✅ **React Native New Architecture** compatible
- ✅ **Fabric renderer** support for enhanced performance
- ✅ **TypeScript** definitions included

## 🚀 Performance Demo

<div align="center">
  <img src="./examples/react-native/1.png" alt="Performance Demo" width="300" />
  <img src="./examples/react-native/2.png" alt="Performance Demo" width="300" style="margin-left: 50px;" />
  <img src="./examples/react-native/3.png" alt="Performance Demo" width="800" />
  <img src="./examples/react-native/4.gif" alt="Performance Demo" width="800" />
  
  *Smooth scrolling, zooming, and drawing operations at 60fps*
</div>

## 📦 Installation

### React Native (Git)

```bash
yarn add native-kline-view@https://github.com/hellohublot/native-kline-view.git
```

### iOS Setup

React Native iOS:

```bash
cd ios && pod install
```

### Android Setup

No additional setup required for Android.
React Native Android:
No additional setup required.

### Flutter (Git)

```yaml
dependencies:
  native_kline_view:
    git:
      url: https://github.com/hellohublot/native-kline-view.git
      path: flutter/native_kline_view
```

iOS:
Note: The Flutter plugin depends on the native pod. In your Flutter app Podfile, add one of the following:

Local repo path (recommended for this repo):

```ruby
pod 'NativeKLineView', :path => '../../../ios'
```

Or remote podspec (no local clone):

```ruby
pod 'NativeKLineView', :podspec => 'https://raw.githubusercontent.com/hellohublot/native-kline-view/main/ios/NativeKLineView.podspec'
```

```dart
NativeKLineView(
  optionList: optionListJson,
  onDrawItemDidTouch: (payload) {},
  onDrawItemComplete: () {},
  onDrawPointComplete: (count) {},
)
```

### Native iOS

Podfile via Git:

```ruby
pod 'NativeKLineView', :podspec => 'https://raw.githubusercontent.com/hellohublot/native-kline-view/main/ios/NativeKLineView.podspec'
```

Or use a local clone:

```ruby
pod 'NativeKLineView', :path => '../native-kline-view/ios'
```

### Native Android

Recommended: add as a git submodule (or clone) and point Gradle to the module.

```bash
git submodule add https://github.com/hellohublot/native-kline-view.git
```

```gradle
// settings.gradle
include(":native-kline-view")
project(":native-kline-view").projectDir = new File(rootDir, "../native-kline-view/android")
```

```gradle
// app/build.gradle
implementation project(":native-kline-view")
```

XML usage:

```xml
<com.github.fujianlian.klinechart.NativeKLineView
    android:id="@+id/klineView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

## ▶️ Examples

All examples use the local workspace library code (so you can modify and run quickly), while the install instructions above point to the remote repo.

React Native:

- App entry: [examples/react-native/App.js](./examples/react-native/App.js)

Native iOS:

```bash
cd examples/ios
pod install
open NativeKLineExample.xcworkspace
```

Native Android:

```bash
cd examples/android
./gradlew installDebug
```

Flutter (iOS):

```bash
cd examples/flutter
flutter pub get
flutter run -d ios
```

Flutter (Android):

```bash
cd examples/flutter
flutter pub get
flutter run -d android
```

## 🎯 Quick Start

For a comprehensive implementation with all features, please check **[example/App.js](./examples/react-native/App.js)**

The example app demonstrates:

- 🎛️ **Complete UI Controls** - Time period selector, indicator switcher, drawing tools
- 🎨 **Theme Management** - Dark/Light mode with smooth transitions
- 📊 **Indicator Management** - Dynamic indicator switching and configuration
- ✏️ **Drawing Tools** - Full-featured drawing interface with tool selection
- 📱 **Responsive Design** - Adapts to different screen sizes and orientations

## 🆕 Custom Trade Marker Component

You can now render trade markers with your own React component instead of the default native dots.

<div align="center">
  <img src="./examples/react-native/chart-trade.png" alt="Custom trade marker overlay" width="720" />

_Example: custom marker on top of minute chart_

</div>

### How it works

- Pass `tradeComponent` to `RNKLineView`
- The library emits marker positions from native iOS layout
- Your component is rendered as an absolute overlay and does not block gestures

### Usage

```tsx
import React from "react";
import { View, Text } from "react-native";
import RNKLineView from "react-native-kline-view";

const TradeMarker = (trade: { type: "buy" | "sell" }, count: number) => (
  <View
    style={{
      backgroundColor: trade.type === "buy" ? "#11c766" : "#ff5b6e",
      borderRadius: 10,
      paddingHorizontal: 6,
      paddingVertical: 2,
    }}
  >
    <Text style={{ color: "#fff", fontSize: 10, fontWeight: "700" }}>
      {trade.type.toUpperCase()} {count}
    </Text>
  </View>
);

export default function Chart() {
  return (
    <RNKLineView
      optionList={JSON.stringify(optionList)}
      tradeComponent={TradeMarker}
    />
  );
}
```

## 📊 Component Properties

### Core Properties

| Property              | Type                          | Required | Default | Description                                                                  |
| --------------------- | ----------------------------- | -------- | ------- | ---------------------------------------------------------------------------- |
| `optionList`          | string                        | ✅       | -       | JSON string containing all chart configuration and data                      |
| `tradeComponent`      | `(trade, count) => ReactNode` | ❌       | -       | Custom renderer for trade markers (`trade = { id, timestamp, price, type }`) |
| `onDrawItemDidTouch`  | function                      | ❌       | -       | Callback when a drawing item is touched                                      |
| `onDrawItemComplete`  | function                      | ❌       | -       | Callback when a drawing item is completed                                    |
| `onDrawPointComplete` | function                      | ❌       | -       | Callback when drawing point is completed                                     |

### Event Callbacks Detail

| Callback              | Parameters                                                                                           | Description                                                                                  |
| --------------------- | ---------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| `onDrawItemDidTouch`  | `{ shouldReloadDrawItemIndex, drawColor, drawLineHeight, drawDashWidth, drawDashSpace, drawIsLock }` | Triggered when user touches an existing drawing item. Returns drawing properties for editing |
| `onDrawItemComplete`  | `{}`                                                                                                 | Triggered when user completes creating a new drawing item                                    |
| `onDrawPointComplete` | `{ pointCount }`                                                                                     | Triggered when user completes adding points to a drawing (useful for multi-point drawings)   |

## 🔧 OptionList Configuration

The `optionList` is a JSON string containing all chart configuration. Here's the complete structure:

### Main Configuration

| Property            | Type    | Default | Description                                  |
| ------------------- | ------- | ------- | -------------------------------------------- |
| `modelArray`        | Array   | `[]`    | K-line data array (see Data Format below)    |
| `shouldScrollToEnd` | Boolean | `true`  | Whether to scroll to the latest data on load |
| `targetList`        | Object  | `{}`    | Technical indicator parameters               |
| `configList`        | Object  | `{}`    | Visual styling configuration                 |
| `drawList`          | Object  | `{}`    | Drawing tools configuration                  |

### Data Format (modelArray)

Each data point should contain the following fields:

- `id`: Timestamp
- `open`: Opening price
- `high`: Highest price
- `low`: Lowest price
- `close`: Closing price
- `vol`: Volume
- `dateString`: Formatted time string
- `selectedItemList`: Info panel data array
- `maList`: Moving average data (if enabled)
- `maVolumeList`: Volume moving average data
- Various technical indicator data (MACD, KDJ, RSI, etc.)

**For complete data structure examples, see [example/App.js](./examples/react-native/App.js)**

### Visual Configuration (configList)

| Property                     | Type   | Description                                           |
| ---------------------------- | ------ | ----------------------------------------------------- |
| `colorList`                  | Object | `{ increaseColor, decreaseColor }` - Bull/bear colors |
| `targetColorList`            | Array  | Colors for indicator lines                            |
| `backgroundColor`            | Color  | Chart background color                                |
| `textColor`                  | Color  | Global text color                                     |
| `gridColor`                  | Color  | Grid line color                                       |
| `candleTextColor`            | Color  | Candle label text color                               |
| `minuteLineColor`            | Color  | Minute chart line color                               |
| `minuteGradientColorList`    | Array  | Gradient colors for minute chart background           |
| `minuteGradientLocationList` | Array  | Gradient stop positions [0, 0.3, 0.6, 1]              |
| `mainFlex`                   | Number | Main chart height ratio (0.6 - 0.85)                  |
| `volumeFlex`                 | Number | Volume chart height ratio (0.15 - 0.25)               |
| `paddingTop`                 | Number | Top padding in pixels                                 |
| `paddingBottom`              | Number | Bottom padding in pixels                              |
| `paddingRight`               | Number | Right padding in pixels                               |
| `itemWidth`                  | Number | Total width per candle (including margins)            |
| `candleWidth`                | Number | Actual candle body width                              |
| `fontFamily`                 | String | Font family for all text                              |
| `headerTextFontSize`         | Number | Header text size                                      |
| `rightTextFontSize`          | Number | Right axis text size                                  |
| `candleTextFontSize`         | Number | Candle value text size                                |
| `panelTextFontSize`          | Number | Info panel text size                                  |
| `panelMinWidth`              | Number | Minimum info panel width                              |

### Drawing Configuration (drawList)

| Property                    | Type    | Description                                                     |
| --------------------------- | ------- | --------------------------------------------------------------- |
| `drawType`                  | Number  | Current drawing tool type (0=none, 1=trend, 2=horizontal, etc.) |
| `shouldReloadDrawItemIndex` | Number  | Drawing state management                                        |
| `drawShouldContinue`        | Boolean | Whether to continue drawing after completing one item           |
| `shouldClearDraw`           | Boolean | Flag to clear all drawings                                      |
| `shouldFixDraw`             | Boolean | Flag to finalize current drawing                                |
| `shotBackgroundColor`       | Color   | Drawing overlay background color                                |

### Technical Indicators (targetList)

Contains parameter settings for various technical indicators:

**Moving Average Settings**:

- `maList`: MA line configuration array
- `maVolumeList`: Volume MA configuration

**Bollinger Bands Parameters**:

- `bollN`: Period (default "20")
- `bollP`: Standard deviation multiplier (default "2")

**MACD Parameters**:

- `macdS`: Fast EMA period (default "12")
- `macdL`: Slow EMA period (default "26")
- `macdM`: Signal line period (default "9")

**KDJ Parameters**:

- `kdjN`: Period (default "9")
- `kdjM1`: K smoothing (default "3")
- `kdjM2`: D smoothing (default "3")

**RSI and WR Settings**:

- `rsiList`: RSI configuration array
- `wrList`: WR configuration array

**For complete configuration examples, see [example/App.js](./examples/react-native/App.js)**

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](./LICENSE) file for details.

## 🙏 Acknowledgments

This project is a significant evolution and enhancement of the original [KChartView](https://github.com/tifezh/KChartView) by [@tifezh](https://github.com/tifezh). While inspired by the original Android-only library, this React Native implementation has been completely rewritten and includes numerous additional features.

## 📞 Support

- 📧 **Email**: hellohublot@gmail.com
- 💬 **Issues**: [GitHub Issues](https://github.com/hellohublot/react-native-kline-view/issues)
- 🎯 **Examples**: Check out [example/App.js](./examples/react-native/App.js) for comprehensive usage

---

<div align="center">
  <p><strong>Built with ❤️ for the React Native community</strong></p>
  <p>
    <a href="#-features">Features</a> •
    <a href="#-installation">Installation</a> •
    <a href="#%EF%B8%8F-examples">Examples</a> •
    <a href="#-component-properties">API</a> •
    <a href="#-license">License</a>
  </p>
</div>
