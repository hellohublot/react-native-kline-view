# feat: add custom React trade markers for KLine chart

## Summary

This PR adds support for rendering trade markers with a custom React component in `react-native-kline-view`.

Instead of only drawing native marker dots, consumers can now provide `tradeComponent(trade, count)` and fully control the marker UI.

## What changed

- Added JS wrapper component in `index.js` to:
  - Accept `tradeComponent` prop
  - Enable native custom marker mode via `useCustomTradeMarker`
  - Receive marker layout events via `onTradeMarkersLayout`
  - Render marker overlays in React with absolute positioning and `pointerEvents="none"`
- Updated TypeScript types in `index.d.ts`:
  - Added `TTrade`
  - Added `tradeComponent?: (trade: TTrade, count: number) => ReactNode`
  - Updated default export type to functional component signature
- iOS native updates:
  - Added `useCustomTradeMarker` and `onTradeMarkersLayout` to config/bridge
  - Emitted visible marker positions from `HTKLineView` (debounced)
  - Disabled default native trade marker drawing in custom mode
  - Exported new view props in `RNKLineView.m`
- Android gradle repository remains on `mavenCentral()`.

## New feature

### Custom trade marker rendering

You can now render trade markers using your own React UI.

`trade` payload shape:

- `id: string`
- `timestamp: number`
- `price: number`
- `type: "buy" | "sell"`

`count` is provided as the second argument.

## Screenshot

![Custom trade marker overlay](./examples/react-native/chart-trade.png.png)

## Notes

- Marker overlay rendering is non-interactive (`pointerEvents="none"`) to preserve chart gestures.
- iOS marker layout emission is debounced to reduce event pressure during scroll and redraw.

## Testing checklist

- [ ] iOS: custom markers appear at expected candle positions
- [ ] iOS: chart pan/zoom/long-press still work with overlays
- [ ] iOS: disabling `tradeComponent` falls back to native markers
- [ ] TypeScript: `tradeComponent` prop and `TTrade` type are available
- [ ] Android: no regression in build and chart rendering
