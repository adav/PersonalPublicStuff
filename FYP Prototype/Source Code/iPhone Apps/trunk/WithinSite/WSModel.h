//
//  WSModel.h
//  WithinSite
//
//  Created by Andrew on 12/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface WSModel : NSObject

- (NSMutableDictionary *) nearbyPlacesDictionary;
- (NSArray *) recommedationsArray;
-(void) updateNearbyPlacesDictionaryWithPositionLongitude:(double)longitude
                                                 Latitude:(double)latitude;
-(void) updateRecommendationListWithPositionLongitude:(double)longitude
                                                 Latitude:(double)latitude;
-(void) clearRecommendationsOnServerAndList;
-(void)showMessageOnMainThreadTitled:(NSString *)title withMessage:(NSString *)message;

@end
