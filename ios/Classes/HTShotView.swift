//
//  HTShotView.swift
//  Base64
//
//  Created by hublot on 2020/8/28.
//

import UIKit

class HTShotView: UIView {
        
    var shotView: UIView?
    
    var shotColor: UIColor?
    
    var shotPoint: CGPoint? {
        didSet {
            reloadImage()
        }
    }
    
    var dimension: CGFloat = 100

    lazy var imageView: UIImageView = {
        let imageView = UIImageView.init(frame: CGRect.zero)
        imageView.contentMode = .center
        return imageView
    }()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        layer.borderColor = UIColor.clear.cgColor
        layer.borderWidth = 0.1
        
        layer.shadowColor = UIColor.init(white: 0, alpha: 0.12).cgColor
        layer.shadowOpacity = 1
        layer.shadowOffset = CGSize.init(width: 0, height: 2)
        layer.shadowRadius = 10
        layer.cornerRadius = dimension / 2.0
        
        
        addSubview(imageView)
        imageView.frame = bounds
        imageView.layer.cornerRadius = dimension / 2.0
        imageView.layer.masksToBounds = true
        isUserInteractionEnabled = false
    }
    
    override func reactSetFrame(_ frame: CGRect) {
        super.reactSetFrame(frame)
        imageView.frame = bounds
    }
    
    func reloadImage() -> Void {
        guard let shotView = shotView, let shotPoint = shotPoint else {
            imageView.isHidden = true
            return
        }
        let scale = UIScreen.main.scale
        
        UIGraphicsBeginImageContextWithOptions(shotView.bounds.size, false, scale)
        shotView.layer.render(in: UIGraphicsGetCurrentContext()!)
        var image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        let insetBounds = shotView.bounds.insetBy(dx: -dimension, dy: -dimension)
        UIGraphicsBeginImageContextWithOptions(insetBounds.size, false, scale)
        shotColor?.set()
        UIRectFill(insetBounds)
        image?.draw(at: CGPoint.init(x: dimension, y: dimension))
        image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        
        
        let radius = dimension / 2.0
        let rect = CGRect.init(x: shotPoint.x - radius + dimension, y: shotPoint.y - radius + dimension, width: dimension, height: dimension)
        let frame = CGRect.init(x: rect.origin.x * scale,
                                y: rect.origin.y * scale,
                                width: rect.size.width * scale,
                                height: rect.size.height * scale)
        guard let imageRef = image?.cgImage?.cropping(to: frame), let orientation = image?.imageOrientation else {
            return
        }
        
//        UIGraphicsBeginImageContextWithOptions(CGSize.init(width: dimension, height: dimension), false, scale)
//        guard let context = UIGraphicsGetCurrentContext() else {
//            return
//        }
//        context.scaleBy(x: 1, y: -1)
//        context.translateBy(x: 0, y: -dimension)
//
//        context.saveGState()
//        UIBezierPath.init(roundedRect: CGRect.init(x: 0, y: 0, width: dimension, height: dimension), cornerRadius: dimension).addClip()
//        context.draw(imageRef, in: CGRect.init(x: 0, y: 0, width: dimension, height: dimension))
//        context.restoreGState()
//
//
//        guard let cgimage = context.makeImage() else {
//            return
//        }
        image = UIImage.init(cgImage: imageRef, scale: scale / 1.5, orientation: orientation)
//        UIGraphicsEndImageContext()
        
        
        imageView.image = image
        imageView.isHidden = false
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    

}
