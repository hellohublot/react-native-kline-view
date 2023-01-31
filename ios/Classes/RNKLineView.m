#import "RNKLineView-Swift.h"
#import "RCTViewManager.h"


@interface RCT_EXTERN_MODULE(RNKLineView, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(onDrawItemDidTouch, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onDrawItemComplete, RCTBubblingEventBlock)

RCT_EXPORT_VIEW_PROPERTY(onDrawPointComplete, RCTBubblingEventBlock)

RCT_EXTERN_METHOD(reloadOptionList:(nonnull NSNumber *)reactTag optionList:(NSString *)optionList)







//RCT_CUSTOM_VIEW_PROPERTY(optionList, NSDictionary *, UIView) {

//    dispatch_async(dispatch_get_main_queue(), ^{
//        NSDictionary *optionList = [RCTConvert NSDictionary:json];
//
//        NSDictionary *configList = [optionList valueForKey:@"configList"];
//        [HTConfigManager reloadConfigWithDictionary:configList];
//
//
//        NSArray *modelArray = [optionList valueForKey:@"modelArray"];
//        NSMutableArray *reloadModelArray = [@[] mutableCopy];
//        [modelArray enumerateObjectsUsingBlock:^(NSDictionary *object, NSUInteger index, BOOL * _Nonnull stop) {
//            NSMutableArray *model = [@[] mutableCopy];
//            NSArray *modelKeyList = @[@"id", @"open", @"high", @"low", @"close", @"vol"];
//            [modelKeyList enumerateObjectsUsingBlock:^(NSString *key, NSUInteger index, BOOL * _Nonnull stop) {
//                [model addObject:[object valueForKey:key]];
//            }];
//            [reloadModelArray addObject:model];
//        }];
//        NSInteger primary = [[optionList valueForKey:@"primary"] integerValue];
//        NSInteger second = [[optionList valueForKey:@"second"] integerValue];
//        NSInteger time = [[optionList valueForKey:@"time"] integerValue];
//        BOOL shouldScrollToEnd = [[optionList valueForKey:@"shouldScrollToEnd"] boolValue];
//
//        Y_StockChartTargetLineStatus primaryStatus = Y_StockChartTargetLineStatusCloseMA;
//        Y_StockChartTargetLineStatus secondStatus = Y_StockChartTargetLineStatusAccessoryClose;
//        if (primary == 1) {
//            primaryStatus = Y_StockChartTargetLineStatusMA;
//        } else if (primary == 2) {
//            primaryStatus = Y_StockChartTargetLineStatusBOLL;
//        }
//        if (second == 3) {
//            secondStatus = Y_StockChartTargetLineStatusMACD;
//        } else if (second == 4) {
//            secondStatus = Y_StockChartTargetLineStatusKDJ;
//        } else if (second == 5) {
//            secondStatus = Y_StockChartTargetLineStatusRSI;
//        } else if (second == 6) {
//            secondStatus = Y_StockChartTargetLineStatusWR;
//        }
////        Y_StockChartTargetLineStatus status = primaryStatus | secondStatus;
//
//        Y_StockChartCenterViewType lineType = Y_StockChartcenterViewTypeKline;
//        if (time == -1) {
//            lineType = Y_StockChartcenterViewTypeTimeLine;
//        }
//
//        if (reloadModelArray.count > 0) {
//            view.shouldScrollToEnd = shouldScrollToEnd;
//            NSArray <Y_KLineModel *> *modelList = [Y_KLineGroupModel objectWithArray:reloadModelArray].models;
//            [modelList enumerateObjectsUsingBlock:^(Y_KLineModel *model, NSUInteger index, BOOL * _Nonnull stop) {
//                NSDictionary *dictionary = modelArray[index];
//                [model reloadModelWithDictionary:dictionary];
//            }];
//            view.kLineModels = modelList;
//            view.MainViewType = lineType;
//            view.mainTargetLineStatus = primaryStatus;
//            view.targetLineStatus = secondStatus;
//
//            view.lineKTime = time;
//            [view reDraw];
//        };
//    });
//};




@end

