//
//  WSAppDelegate.h
//  WithinSite
//
//  Created by Andrew on 13/01/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "WSModel.h"

@interface WSAppDelegate : UIResponder <UIApplicationDelegate, CLLocationManagerDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, retain) CLLocationManager *locationManager;

@property (strong, nonatomic) WSModel *model;

- (void) setupLocationManager;
- (void) disableLocationManager;
- (BOOL) isLocationManagerMonitoringRegions;

@end
