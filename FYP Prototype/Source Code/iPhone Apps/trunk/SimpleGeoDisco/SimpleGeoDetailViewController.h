//
//  SimpleGeoDetailViewController.h
//  SimpleGeoDisco
//
//  Created by Andrew on 08/11/2012.
//  Copyright (c) 2012 AndrewDavidson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SimpleGeoRecommendedDataItem.h"
#import <CoreLocation/CoreLocation.h>

@interface SimpleGeoDetailViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, weak) SimpleGeoRecommendedDataItem *placeOfInterest;
@property (weak, nonatomic) IBOutlet UITableView *detailsTableView;
@property (nonatomic, weak) CLLocation *currentLocation;

@end
