//
//  HTDrawView.swift
//  Base64
//
//  Created by hublot on 2020/8/26.
//

import UIKit

class HTDrawContext {
    
    var configManager: HTKLineConfigManager
    
    weak var klineView: HTKLineView?
    

    lazy var drawItemList: [HTDrawItem] = {
        let drawItemList = [HTDrawItem]()
        return drawItemList
    }()
    
    init(_ klineView: HTKLineView, _ configManager: HTKLineConfigManager) {
        self.klineView = klineView
        self.configManager = configManager
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    var breakTouch = false
    
    func touchesGesture(_ location: CGPoint, _ translation: CGPoint, _ state: UIGestureRecognizerState) {
        guard let klineView = klineView, breakTouch == false else {
            if state == .ended {
                breakTouch = false
            }
            return
        }
        switch state {
        case .began:
            if (configManager.shouldReloadDrawItemIndex > HTDrawState.showContext.rawValue) {
                let selectedDrawItem = drawItemList[configManager.shouldReloadDrawItemIndex]
                if (selectedDrawItem.pointList.count >= selectedDrawItem.drawType.count) {
                    if (HTDrawItem.canResponseLocation(drawItemList, location, klineView) != selectedDrawItem) {
                        configManager.onDrawItemDidTouch?(nil, HTDrawState.showPencil.rawValue)
                        breakTouch = true
                        setNeedsDisplay()
                        return
                    }
                }
//            } else if (configManager.shouldReloadDrawItemIndex > HTDrawState.showPencil.rawValue) {
//                let selectedDrawItem = HTDrawItem.canResponseLocation(drawItemList, location, translation, state, klineView)
//                if let selectedDrawItem = selectedDrawItem, let selectedDrawItemIndex = drawItemList.index(of: selectedDrawItem) {
//                    configManager.onDrawItemDidTouch?(selectedDrawItem, selectedDrawItemIndex)
//                    setNeedsDisplay()
//                    return
//                } else {
//                    if HTDrawItem.canResponseTouch(drawItemList, location, translation, state, klineView) {
//                        setNeedsDisplay()
//                        return
//                    }
//                }
            }
        case .changed:
            break
        case .ended:
            break
        default:
            break
        }
        if HTDrawItem.canResponseTouch(drawItemList, location, translation, state, klineView) {
            if state == .began, let moveItem = HTDrawItem.findTouchMoveItem(drawItemList), let moveItemIndex = drawItemList.index(of: moveItem) {
                configManager.onDrawItemDidTouch?(moveItem, moveItemIndex)
            }
            setNeedsDisplay()
            return
        }
        if (configManager.drawType == .none) {
            return
        }
        
//        let moveDrawItem = HTDrawItem.findTouchMoveItem(drawItemList)
//        let canResponse = false
//        if (configManager.shouldReloadDrawItemIndex == HTDrawState.showPencil.rawValue && state == .ended && translation == CGPoint.zero) {
//            if moveDrawItem != nil {
//                configManager.shouldReloadDrawItemIndex = HTDrawState
//            }
//        }
//
//
//        // 能够处理点击, 改变拖动的点, 重新绘制
//        if let klineView = klineView, ) {
//            // 如果移动了或者点击了, 去弹起配置弹窗
//            if let moveDrawItem = moveDrawItem, let moveDrawItemIndex = drawItemList.firstIndex(of: moveDrawItem), state != .changed {
//                configManager.onDrawItemDidTouch?(moveDrawItem, moveDrawItemIndex)
//            }
//            setNeedsDisplay()
//            return
//        }
    
        
        let drawItem = drawItemList.last
        switch state {
        case .began:
            if (drawItem == nil || (drawItem?.pointList.count ?? 0) >= (drawItem?.drawType.count ?? 0)) {
                let drawItem = HTDrawItem.init(configManager.drawType, location)
                drawItem.drawColor = configManager.drawColor
                drawItem.drawLineHeight = configManager.drawLineHeight
                drawItem.drawDashWidth = configManager.drawDashWidth
                drawItem.drawDashSpace = configManager.drawDashSpace
                
                drawItemList.append(drawItem)
                configManager.onDrawItemDidTouch?(drawItem, drawItemList.count - 1)
            } else {
                drawItem?.pointList.append(location)
            }
        case .ended, .changed:
            let length = drawItem?.pointList.count ?? 0
            if length >= 1 {
                let index = length - 1
                drawItem?.pointList[index] = location
                // 最后一个点起笔
                if case .ended = state, let drawItem = drawItem {
                    configManager.onDrawPointComplete?(drawItem, drawItemList.count - 1)
                    if index == drawItem.drawType.count - 1 {
                        configManager.onDrawItemComplete?(drawItem, drawItemList.count - 1)
                        if configManager.drawShouldContinue {
                            configManager.shouldReloadDrawItemIndex = HTDrawState.showContext.rawValue
                        } else {
                            configManager.drawType = .none
                        }
                    }
                }
            }
        default:
            break
        }
        setNeedsDisplay()
    }
    
    func fixDrawItemList() {
        guard let drawItem = drawItemList.last else {
            return
        }
        if drawItem.pointList.count < drawItem.drawType.count {
            drawItemList.removeLast()
        }
        setNeedsDisplay()
    }
    
    func clearDrawItemList() {
        drawItemList = []
        setNeedsDisplay()
    }
    
    func drawLine(_ context: CGContext, _ drawItem: HTDrawItem, _ startPoint: CGPoint, _ endPoint: CGPoint) {
        context.move(to: startPoint)
        context.addLine(to: endPoint)
        context.setStrokeColor(drawItem.drawColor.cgColor)
        context.setLineWidth(drawItem.drawLineHeight)
        var dashList = [drawItem.drawDashWidth, drawItem.drawDashSpace]
        if drawItem.drawDashSpace == 0 {
            dashList = []
        }
        context.setLineDash(phase: 0, lengths: dashList)
        context.drawPath(using: .stroke)
    }
    
    func setNeedsDisplay() {
        klineView?.setNeedsDisplay()
    }

    func drawMapper(_ context: CGContext, _ drawItem: HTDrawItem, _ index: Int, _ itemIndex: Int) {
        guard let klineView = klineView else {
            return
        }
        let lineList = HTDrawItem.lineListWithIndex(drawItem, index, klineView)
        if index == 2, case .parallelLine = drawItem.drawType, let (startPoint, endPoint) = lineList.first {
            let firstPoint = drawItem.pointList[0]
            let secondPoint = drawItem.pointList[1]
            context.move(to: klineView.viewPointFromValuePoint(firstPoint))
            context.addLine(to: klineView.viewPointFromValuePoint(secondPoint))
            context.addLine(to: klineView.viewPointFromValuePoint(startPoint))
            context.addLine(to: klineView.viewPointFromValuePoint(endPoint))
            context.closePath()
            context.setFillColor(drawItem.drawColor.withAlphaComponent(0.5).cgColor)
            context.drawPath(using: .fill)
            let dashStartPoint = HTDrawItem.centerPoint(p1: firstPoint, p2: endPoint)
            let dashEndPoint = HTDrawItem.centerPoint(p1: secondPoint, p2: startPoint)
            context.move(to: klineView.viewPointFromValuePoint(dashStartPoint))
            context.addLine(to: klineView.viewPointFromValuePoint(dashEndPoint))
            context.setLineDash(phase: 0, lengths: [4, 4])
            context.setStrokeColor(drawItem.drawColor.withAlphaComponent(0.5).cgColor)
            context.setLineWidth(1)
            context.drawPath(using: .stroke)
        }
        for (startPoint, endPoint) in lineList {
            drawLine(context, drawItem, klineView.viewPointFromValuePoint(startPoint), klineView.viewPointFromValuePoint(endPoint))
        }
        let point = drawItem.pointList[index]
        
        if (itemIndex != configManager.shouldReloadDrawItemIndex) {
            return
        }
        
        context.addArc(center: klineView.viewPointFromValuePoint(point), radius: 10, startAngle: 0, endAngle: CGFloat(Double.pi * 2.0), clockwise: true)
        context.setFillColor(drawItem.drawColor.withAlphaComponent(0.5).cgColor)
        context.drawPath(using: .fill)
        context.addArc(center: klineView.viewPointFromValuePoint(point), radius: 4, startAngle: 0, endAngle: CGFloat(Double.pi * 2.0), clockwise: true)
        context.setFillColor(drawItem.drawColor.cgColor)
        context.drawPath(using: .fill)
    }
    
    func draw(_ contenOffset: CGFloat) {
        guard let context = UIGraphicsGetCurrentContext() else {
            return
        }
        for (itemIndex, drawItem) in drawItemList.enumerated() {
            for (index, _) in drawItem.pointList.enumerated() {
                drawMapper(context, drawItem, index, itemIndex)
            }
        }
    }

}
