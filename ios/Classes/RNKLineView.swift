//
//  HTKLineViewManager.swift
//  Base64
//
//  Created by hublot on 2020/4/3.
//

import UIKit

@objc(RNKLineView)
@objcMembers
class RNKLineView: RCTViewManager {

    static let queue = DispatchQueue.init(label: "com.hublot.klinedata")

    override func view() -> UIView! {
        return HTKLineContainerView()
    }

    override class func requiresMainQueueSetup() -> Bool {
        return true
    }

    @objc func reloadOptionList(_ reactTag: NSNumber, optionList: String) {
        bridge.uiManager.addUIBlock { (uimanager, viewRegistry) in
            guard let viewRegistry = viewRegistry, let containerView = viewRegistry[reactTag] as? HTKLineContainerView else {
                return
            }
            type(of: self).queue.async {
                do {
                    guard let optionList = try JSONSerialization.jsonObject(with: optionList.data(using: .utf8) ?? Data(), options: .allowFragments) as? [String: Any] else {
                        return
                    }
                    containerView.configManager.reloadOptionList(optionList)
                    DispatchQueue.main.async {
                        containerView.reloadConfigManager(containerView.configManager)
                    }
                } catch(_) {

                }
            }
        }
    }

}
