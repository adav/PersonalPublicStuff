//
//  WSViewController.m
//  WithinSite
//
//  Created by Andrew on 13/01/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSHomeMapViewController.h"
#import "WSAppDelegate.h"
#import <ECSlidingViewController.h>
#import "WSRecommendationSidebarViewController.h"
#import "WSNearbyPlacesSidebarViewController.h"
#import "WSModel.h"
#import "WithinSitePlace.h"
#import "WSSplashBubbleMiniViewController.h"
#import "WSPlaceDetailViewController.h"
#import "MBProgressHUD.h"
#import "WSUtility.h"

@interface WSHomeMapViewController ()

@property (nonatomic) BOOL zoomedOnce;

@end

@implementation WSHomeMapViewController
@synthesize zoomedOnce;
@synthesize bubbleContainer;

@synthesize demoButtonRefresh;
@synthesize demoButtonSplash;

- (void)viewDidLoad
{
    
    [super viewDidLoad];
    
    //Setup sliding panels:
    self.view.layer.shadowOpacity = 0.75f;
    self.view.layer.shadowRadius = 10.0f;
    self.view.layer.shadowColor = [UIColor blackColor].CGColor;
    
    if(![self.slidingViewController.underLeftViewController isKindOfClass:[WSRecommendationSidebarViewController class]]){
        self.slidingViewController.underLeftViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"RecommendationsMenu"];
    }
    
    if(![self.slidingViewController.underRightViewController isKindOfClass:[WSNearbyPlacesSidebarViewController class]]){
        self.slidingViewController.underRightViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"NearbyMenu"];
    }
    
    //Setup a gesture to also control the sliding panels:
    [self.view addGestureRecognizer:self.slidingViewController.panGesture];
    
    
	
    self.mapView.delegate = self;
    zoomedOnce = NO;
    
    [self.mapView setShowsUserLocation:YES];
    
    //[self updateSplashDisplay];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    if (![defaults objectForKey:@"user_email"]) {
        [self performSegueWithIdentifier:@"LoginDemo" sender:self];
    }
}

- (void)viewWillAppear:(BOOL)animated
{
    
    self.navigationController.navigationBar.hidden = YES;
    [[UIApplication sharedApplication] setStatusBarHidden:NO withAnimation:NO];
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(queue, ^{
        
        WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
        
        CLLocationCoordinate2D coord = appDelegate.locationManager.location.coordinate;
        [self.model updateNearbyPlacesDictionaryWithPositionLongitude:coord.longitude Latitude:coord.latitude];
        [self.model updateRecommendationListWithPositionLongitude:coord.longitude Latitude:coord.latitude];
        
    });
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    if ([defaults objectForKey:@"debugDemoMode"]) {
        [demoButtonSplash setHidden:![[defaults objectForKey:@"debugDemoMode"] boolValue]];
        [demoButtonRefresh setHidden:![[defaults objectForKey:@"debugDemoMode"] boolValue]];
    }
    
    
    
}

-(void)updateSplashDisplay
{
    if (zoomedOnce) {
        return;
    }
    
    zoomedOnce = YES;
    
    MBProgressHUD *hud = [WSUtility createAndShowLoadingHudOnView:self.view withText:@"Refreshing" withDetailText:@"Finding nearest interesting places..."];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^(void) {
        
        
        WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
        
        CLLocation *coord = appDelegate.locationManager.location;
        [self.model updateNearbyPlacesDictionaryWithPositionLongitude:coord.coordinate.longitude Latitude:coord.coordinate.latitude];
        
        //clear map:
        //    for (MKPointAnnotation *annotation in [self.mapView annotations]) {
        //        if (![annotation isKindOfClass:[MKUserLocation class]]){//let's not delete the blue spot!
        //            [self.mapView removeAnnotation:annotation];
        //        }
        //    }
        
        if (self.model.nearbyPlacesDictionary != nil) {
            
            //distance between iphone and nearest recommended place, set map region shown
            
            NSDictionary *serviceModuleResult = [self.model nearbyPlacesDictionary][@"results"][0];
            NSArray *placesArray = serviceModuleResult[@"places"];
            
            WithinSitePlace *place = [WithinSitePlace initWithDictionary:[placesArray objectAtIndex:0]];
            
            CLLocationDistance distanceBetweenMeAndPoint = [coord distanceFromLocation:[place getPlaceLocation]];
            CLLocationCoordinate2D centerMapView = [place getPlaceLocation].coordinate;
            centerMapView.latitude = centerMapView.latitude + ((distanceBetweenMeAndPoint)/111000);//1 degree = 111000m
            
            MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(centerMapView,
                                                                           distanceBetweenMeAndPoint*3,
                                                                           distanceBetweenMeAndPoint*3);
            
            
            // Callback on the main queue to update UI.
            dispatch_async(dispatch_get_main_queue(), ^(void) {
                [self.mapView setRegion:[self.mapView regionThatFits:region] animated:YES];
                [bubbleContainer setHidden:NO];
                //        // Add an annotation
                //        MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
                //        point.coordinate = [place getPlaceLocation].coordinate;
                //        point.title = [place getPlaceName];
                //        point.subtitle = [place getPlaceDescription];
                //        [self.mapView addAnnotation:point];
                //
                //        [self.mapView selectAnnotation:point animated:YES];
                //
                //        CGPoint annotationCenter = [self.mapView convertCoordinate:[place getPlaceLocation].coordinate toPointToView:self.view];
                //        annotationCenter.x = 160.0f;
                //        annotationCenter.y = annotationCenter.y - 180.0f;
                //
                //        [bubbleContainer setCenter:annotationCenter];
                
                for (UIViewController *childViewController in [self childViewControllers])
                {
                    if ([childViewController isKindOfClass:[WSSplashBubbleMiniViewController class]])
                    {
                        WSSplashBubbleMiniViewController *bubble = (WSSplashBubbleMiniViewController *)childViewController;
                        
                        [bubble setupBubblewithWithinSitePlace:place];
                        
                        break;
                    }
                }
                
                [hud hide:YES];
            });
        }
    });
    
    //NSLog(@"dist from top: %f", [bubbleContainer frame].origin.y);
    
    //        //clear map:
    //        for (MKPointAnnotation *annotation in [self.mapView annotations]) {
    //            if (![annotation isKindOfClass:[MKUserLocation class]]){//let's not delete the blue spot!
    //                [self.mapView removeAnnotation:annotation];
    //            }
    //        }
    
    
    /*
     if (self.model.recommendedItems.count > 1) {
     [self.showMoreRecsButton setTitle:[NSString stringWithFormat:@"Show %u other recommended places...", self.model.recommendedItems.count - 1] forState:UIControlStateNormal];
     [self.showMoreRecsButton setHidden:NO];
     } else {
     [self.showMoreRecsButton setHidden:YES];
     }
     */
    
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if (self.model.nearbyPlacesDictionary != nil) {
        if([segue.identifier isEqualToString:@"ShowDetailSplash"]){
            WSPlaceDetailViewController *detailController = segue.destinationViewController;
            
            NSDictionary *serviceModuleResult = [self.model nearbyPlacesDictionary][@"results"][0];
            NSArray *placesArray = serviceModuleResult[@"places"];
            
            detailController.placeInquired = [WithinSitePlace initWithDictionary:[placesArray objectAtIndex:0]];
        }
    }
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)pressedRefreshNearbyButton:(UIButton *)sender {
    [[self model] updateNearbyPlacesDictionaryWithPositionLongitude:self.mapView.centerCoordinate.longitude
                                                           Latitude:self.mapView.centerCoordinate.latitude];
}


- (WSModel *)model{
    WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
    return [appDelegate model];
}

- (IBAction)refreshPressed:(id)sender {
    zoomedOnce = NO;
    [self updateSplashDisplay];
}
- (IBAction)whatsNearbyButtonPressed:(id)sender {
    [self.slidingViewController anchorTopViewTo:ECLeft];
}

- (IBAction)recommendationsButtonPressed:(id)sender {
    [self.slidingViewController anchorTopViewTo:ECRight];
}

- (IBAction)bubblePressed:(id)sender {
    [self performSegueWithIdentifier:@"ShowDetailSplash" sender:nil];
}
@end
