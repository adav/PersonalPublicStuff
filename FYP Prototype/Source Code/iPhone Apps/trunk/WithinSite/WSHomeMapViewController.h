//
//  WSViewController.h
//  WithinSite
//
//  Created by Andrew on 13/01/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <ECSlidingViewController/ECSlidingViewController.h>

@interface WSHomeMapViewController : UIViewController<MKMapViewDelegate>

@property (weak, nonatomic) IBOutlet MKMapView *mapView;
- (IBAction)refreshPressed:(id)sender;
@property (weak, nonatomic) IBOutlet UIView *bubbleContainer;
- (IBAction)whatsNearbyButtonPressed:(id)sender;
- (IBAction)recommendationsButtonPressed:(id)sender;
- (IBAction)bubblePressed:(id)sender;
@property (weak, nonatomic) IBOutlet UIButton *demoButtonRefresh;
@property (weak, nonatomic) IBOutlet UIButton *demoButtonSplash;

@end
