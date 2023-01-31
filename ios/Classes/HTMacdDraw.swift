//
//  HTMacdDraw.swift
//  HTKLineView
//
//  Created by hublot on 2020/3/17.
//  Copyright © 2020 hublot. All rights reserved.
//

import UIKit

class HTMacdDraw: NSObject, HTKLineDrawProtocol {

    func minMaxRange(_ visibleModelArray: [HTKLineModel], _ configManager: HTKLineConfigManager) -> Range<CGFloat> {
        var maxValue = CGFloat.leastNormalMagnitude
        var minValue = CGFloat.greatestFiniteMagnitude

        for model in visibleModelArray {
            let valueList = [model.macdValue, model.macdDif, model.macdDea]
            maxValue = max(maxValue, valueList.max() ?? 0)
            minValue = min(minValue, valueList.min() ?? 0)
        }
        return Range<CGFloat>.init(uncheckedBounds: (lower: minValue, upper: maxValue))
    }

    func drawCandle(_ model: HTKLineModel, _ index: Int, _ maxValue: CGFloat, _ minValue: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ context: CGContext, _ configManager: HTKLineConfigManager) {
        let color = model.macdValue > 0 ? configManager.increaseColor : configManager.decreaseColor
        let valueList = [model.macdValue, 0]
        let high = valueList.max() ?? 0
        let low = valueList.min() ?? 0
        drawCandle(high: high, low: low, maxValue: maxValue, minValue: minValue, baseY: baseY, height: height, index: index, width: configManager.macdCandleWidth, color: color, verticalAlignBottom: false, context: context, configManager: configManager)
    }

    func drawLine(_ model: HTKLineModel, _ lastModel: HTKLineModel, _ maxValue: CGFloat, _ minValue: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ index: Int, _ lastIndex: Int, _ context: CGContext, _ configManager: HTKLineConfigManager) {
        let itemList = [
            ["value": model.macdDif, "lastValue": lastModel.macdDif, "color": configManager.targetColorList[0]],
            ["value": model.macdDea, "lastValue": lastModel.macdDea, "color": configManager.targetColorList[1]],
        ]
        for item in itemList {
            drawLine(value: item["value"] as? CGFloat ?? 0, lastValue: item["lastValue"] as? CGFloat ?? 0, maxValue: maxValue, minValue: minValue, baseY: baseY, height: height, index: index, lastIndex: lastIndex, color: item["color"] as? UIColor ?? UIColor.orange, isBezier: false, context: context, configManager: configManager)
        }
    }

    func drawText(_ model: HTKLineModel, _ baseX: CGFloat, _ baseY: CGFloat, _ context: CGContext, _ configManager: HTKLineConfigManager) {
        var x = baseX
        let itemList = [
            ["title": String(format: "MACD(%@,%@,%@)", configManager.macdS, configManager.macdL, configManager.macdM), "color": configManager.textColor],
            ["title": String(format: "MACD:%@", configManager.precision(model.macdValue, -1)), "color": configManager.targetColorList[5]],
            ["title": String(format: "DIF:%@", configManager.precision(model.macdDif, -1)), "color": configManager.targetColorList[0]],
            ["title": String(format: "DEA:%@", configManager.precision(model.macdDea, -1)), "color": configManager.targetColorList[1]],
        ]
        let font = configManager.createFont(configManager.headerTextFontSize)
        for item in itemList {
            x += drawText(title: item["title"] as? String ?? "", point: CGPoint.init(x: x, y: baseY), color: item["color"] as? UIColor ?? UIColor.orange, font: font, context: context, configManager: configManager)
            x += 5
        }
    }

    func drawValue(_ maxValue: CGFloat, _ minValue: CGFloat, _ baseX: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ context: CGContext, _ configManager: HTKLineConfigManager) {
        drawValue(maxValue, minValue, baseX, baseY, height, 0, -1, context, configManager)
    }

}
