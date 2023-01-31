//
//  HTVolueDraw.swift
//  HTKLineView
//
//  Created by hublot on 2020/3/17.
//  Copyright © 2020 hublot. All rights reserved.
//

import UIKit

class HTVolumeDraw: NSObject, HTKLineDrawProtocol {

    func minMaxRange(_ visibleModelArray: [HTKLineModel], _ configManager: HTKLineConfigManager) -> Range<CGFloat> {
        var maxValue = CGFloat.leastNormalMagnitude
        var minValue = CGFloat.greatestFiniteMagnitude

        for model in visibleModelArray {
            var valueList = [model.volume]
            valueList.append(contentsOf: model.maVolumeList.map({ (item) -> CGFloat in
                return item.value
            }))
            maxValue = max(maxValue, valueList.max() ?? 0)
            minValue = min(minValue, valueList.min() ?? 0)
        }
        let distance = (maxValue - minValue) / 10
        minValue -= distance
        minValue = max(0, minValue)
        return Range<CGFloat>.init(uncheckedBounds: (lower: minValue, upper: maxValue))
    }

    func drawCandle(_ model: HTKLineModel, _ index: Int, _ maxValue: CGFloat, _ minValue: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ context: CGContext, _ configManager: HTKLineConfigManager) {
        var color = model.increment ? configManager.increaseColor : configManager.decreaseColor
        var width = configManager.candleWidth
        if (configManager.isMinute) {
            color = configManager.minuteVolumeCandleColor
            width = configManager.minuteVolumeCandleWidth
        }
        drawCandle(high: model.volume, low: minValue, maxValue: maxValue, minValue: minValue, baseY: baseY, height: height, index: index, width: width, color: color, verticalAlignBottom: true, context: context, configManager: configManager)
    }

    func drawLine(_ model: HTKLineModel, _ lastModel: HTKLineModel, _ maxValue: CGFloat, _ minValue: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ index: Int, _ lastIndex: Int, _ context: CGContext, _ configManager: HTKLineConfigManager) {
        if (configManager.isMinute) {

        } else {
            for itemModel in configManager.maVolumeList {
                let color = configManager.targetColorList[itemModel.index]
                drawLine(value: model.maVolumeList[itemModel.index].value, lastValue: lastModel.maVolumeList[itemModel.index].value, maxValue: maxValue, minValue: minValue, baseY: baseY, height: height, index: index, lastIndex: lastIndex, color: color, isBezier: false, context: context, configManager: configManager)
            }
        }
    }

    func drawText(_ model: HTKLineModel, _ baseX: CGFloat, _ baseY: CGFloat, _ context: CGContext, _ configManager: HTKLineConfigManager) {
        var x = baseX
        let font = configManager.createFont(configManager.headerTextFontSize)
        x += drawText(title: String(format: "VOL:%@", configManager.precision(model.volume, configManager.volume)), point: CGPoint.init(x: x, y: baseY), color: configManager.targetColorList[5], font: font, context: context, configManager: configManager)
        x += 5
        for itemModel in configManager.maVolumeList {
            let item = model.maVolumeList[itemModel.index]
            let title = String(format: "MA%@:%@", item.title, configManager.precision(item.value, configManager.volume))
            let color = configManager.targetColorList[itemModel.index]
            x += drawText(title: title, point: CGPoint.init(x: x, y: baseY), color: color, font: font, context: context, configManager: configManager)
            x += 5
        }
    }

    func drawValue(_ maxValue: CGFloat, _ minValue: CGFloat, _ baseX: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ context: CGContext, _ configManager: HTKLineConfigManager) {
        drawValue(maxValue, minValue, baseX, baseY, height, 1, configManager.volume, context, configManager)
    }


}
