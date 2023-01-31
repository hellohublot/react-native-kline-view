//
//  HTKLineProtocol.swift
//  HTKLineView
//
//  Created by hublot on 2020/3/20.
//  Copyright © 2020 hublot. All rights reserved.
//

import UIKit

protocol HTKLineDrawProtocol: class {

    func minMaxRange(_ visibleModelArray: [HTKLineModel], _ configManager: HTKLineConfigManager) -> Range<CGFloat>

    func drawCandle(_ model: HTKLineModel, _ index: Int, _ maxValue: CGFloat, _ minValue: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ context: CGContext, _ configManager: HTKLineConfigManager)

    func drawLine(_ model: HTKLineModel, _ lastModel: HTKLineModel, _ maxValue: CGFloat, _ minValue: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ index: Int, _ lastIndex: Int, _ context: CGContext, _ configManager: HTKLineConfigManager)

    func drawText(_ model: HTKLineModel, _ baseX: CGFloat, _ baseY: CGFloat, _ context: CGContext, _ configManager: HTKLineConfigManager)

    func drawValue(_ maxValue: CGFloat, _ minValue: CGFloat, _ baseX: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ context: CGContext, _ configManager: HTKLineConfigManager)

}

extension HTKLineDrawProtocol {

    func drawCandle(high: CGFloat, low: CGFloat, maxValue: CGFloat, minValue: CGFloat, baseY: CGFloat, height: CGFloat, index: Int, width: CGFloat, color: UIColor, verticalAlignBottom: Bool, context: CGContext, configManager: HTKLineConfigManager) {
        let itemWidth = configManager.itemWidth
        let scale = (maxValue - minValue) / height
        let paddingHorizontal = (itemWidth - width) / 2.0
        let x = CGFloat(index) * itemWidth + paddingHorizontal
        let y = baseY + (maxValue - high) / scale
        let height = (high - (!verticalAlignBottom ? low : minValue)) / scale
        context.setFillColor(color.cgColor)
        context.fill(CGRect.init(x: x, y: y, width: width, height: height))
    }

    func createLinePath(value: CGFloat, lastValue: CGFloat, maxValue: CGFloat, minValue: CGFloat, baseY: CGFloat, height: CGFloat, index: Int, lastIndex: Int, isBezier: Bool, existPath: UIBezierPath?, context: CGContext, configManager: HTKLineConfigManager) -> UIBezierPath {
        let itemWidth = configManager.itemWidth
        let width = configManager.lineWidth
        let scale = (maxValue - minValue) / height
        let paddingHorizontal = (itemWidth - width) / 2.0
        let createPoint: (Int, CGFloat) -> CGPoint = { (index, value) in
            let x = CGFloat(index) * itemWidth + paddingHorizontal
            let y = baseY + (maxValue - value) / scale
            return CGPoint.init(x: x, y: y)
        }
        let point = createPoint(index, value)
        let lastPoint = createPoint(lastIndex, lastValue)
        let path = existPath ?? UIBezierPath.init()
        if (isBezier) {
            let centerX = (point.x - lastPoint.x) / 2 + lastPoint.x
            if (existPath == nil || index == 0) {
                path.move(to: lastPoint)
            }
            path.addCurve(to: point, controlPoint1: CGPoint.init(x: centerX, y: lastPoint.y), controlPoint2: CGPoint.init(x: centerX, y: point.y))
        } else {
            if (existPath == nil || index == 0) {
                path.move(to: lastPoint)
            }
            path.addLine(to: point)
        }
        return path
    }

    func drawLine(value: CGFloat, lastValue: CGFloat, maxValue: CGFloat, minValue: CGFloat, baseY: CGFloat, height: CGFloat, index: Int, lastIndex: Int, color: UIColor, isBezier: Bool, context: CGContext, configManager: HTKLineConfigManager) {
        let width = configManager.lineWidth
        let path = createLinePath(value: value, lastValue: lastValue, maxValue: maxValue, minValue: minValue, baseY: baseY, height: height, index: index, lastIndex: lastIndex, isBezier: isBezier, existPath: nil, context: context, configManager: configManager)
        context.addPath(path.cgPath)
        context.setStrokeColor(color.cgColor)
        context.setLineWidth(width)
        context.drawPath(using: .stroke)
    }

    func textWidth(title: String, font: UIFont) -> CGFloat {
        let titleString = title as NSString
        let attributedList: [NSAttributedStringKey: Any] = [
            .font: font,
        ]
        let width = titleString.boundingRect(with: CGSize.init(width: 0, height: CGFloat.greatestFiniteMagnitude), options: .usesLineFragmentOrigin, attributes: attributedList, context: nil).width
        return width
    }

    func textHeight(font: UIFont) -> CGFloat {
        let titleString = "你好" as NSString
        let attributedList: [NSAttributedStringKey: Any] = [
            .font: font,
        ]
        let height = titleString.boundingRect(with: CGSize.init(width: CGFloat.greatestFiniteMagnitude, height: 0), options: .usesLineFragmentOrigin, attributes: attributedList, context: nil).height
        return height

    }

    @discardableResult
    func drawText(title: String, point: CGPoint, color: UIColor, font: UIFont, context: CGContext, configManager: HTKLineConfigManager) -> CGFloat {
        context.setFillColor(color.cgColor)
        let titleString = title as NSString
        let attributedList: [NSAttributedStringKey: Any] = [
            .font: font,
            .foregroundColor: color,
        ]
        let width = textWidth(title: title, font: font)
        titleString.draw(at: point, withAttributes: attributedList)
        return width
    }

    func drawValue(_ maxValue: CGFloat, _ minValue: CGFloat, _ baseX: CGFloat, _ baseY: CGFloat, _ height: CGFloat, _ count: Int, _ precision: Int, _ context: CGContext, _ configManager: HTKLineConfigManager) {
        let font = configManager.createFont(configManager.rightTextFontSize)
        let distance = (maxValue - minValue) / CGFloat(max(1, count - 1))
        let reloadHeightDistance = height / CGFloat(max(1, count - 1))
        for i in 0..<count {
            let value = maxValue - distance * CGFloat(i)
            let title = configManager.precision(value, precision)
            var y = baseY + reloadHeightDistance * CGFloat(i)
            let width = textWidth(title: title, font: font)
            let height = textHeight(font: font)
            y -= height / 2
            drawText(title: title, point: CGPoint.init(x: baseX - width, y: y), color: configManager.textColor, font: font, context: context, configManager: configManager)
        }

    }

}
