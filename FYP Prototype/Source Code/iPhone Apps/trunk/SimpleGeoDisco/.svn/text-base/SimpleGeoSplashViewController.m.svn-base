//
//  SimpleGeoSplashViewController.m
//  SimpleGeoDisco
//
//  Created by Andrew on 04/12/2012.
//  Copyright (c) 2012 AndrewDavidson. All rights reserved.
//

#import "SimpleGeoSplashViewController.h"
#import "SimpleGeoModel.h"
#import "SimpleGeoRecommendedDataItem.h"
#import "SimpleGeoViewController.h"

@interface SimpleGeoSplashViewController ()
@property (nonatomic, strong) SimpleGeoModel *model;
@property (nonatomic) BOOL zoomedOnce;
@end

@implementation SimpleGeoSplashViewController
@synthesize mapView;
@synthesize refreshButton;
@synthesize showMoreRecsButton;
@synthesize model = _model;
@synthesize zoomedOnce;

- (SimpleGeoModel *) model
{
    if(!_model){
        _model = [[SimpleGeoModel alloc] init];
    }
    return _model;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	self.mapView.delegate = self;
    zoomedOnce = NO;

    if(self.locationManager == nil){
        self.locationManager = [[CLLocationManager alloc] init];
        self.locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
        self.locationManager.delegate = self;
    }
    [self.locationManager startUpdatingLocation];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)refreshView:(UIButton *)sender {
    NSLog(@"Refresh pressed on splash");
    zoomedOnce = NO;
    [self updateSplashDisplay];
    
    
}
- (IBAction)showMorePressed:(UIButton *)sender {
    //superflous thanks to segue, but might need it later
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if ([segue.identifier isEqualToString:@"showMoreRecs"]) {
        SimpleGeoViewController *destViewController = segue.destinationViewController;
        destViewController.model = self.model;
    }
}

-(void)updateSplashDisplay
{
    if (zoomedOnce) {
        return;
    }
    
    zoomedOnce = YES;
    
    CLLocation *location = self.locationManager.location;
    [self.model populateRecommendedItems:location];
    
    //clear map:
    for (MKPointAnnotation *annotation in [self.mapView annotations]) {
        if (![annotation isKindOfClass:[MKUserLocation class]]){//let's not delete the blue spot!
            [self.mapView removeAnnotation:annotation];
        }
    }
    
    if (self.model.recommendedItems.count > 0) {
        
        //distance between iphone and nearest recommended place, set map region shown
        SimpleGeoRecommendedDataItem *mostRecommendedItem = [[self.model recommendedItems] objectAtIndex:0];
        
        CLLocationDistance distanceBetweenMeAndPoint = [self.locationManager.location distanceFromLocation:mostRecommendedItem.location];
        CLLocationCoordinate2D centerMapView = mostRecommendedItem.location.coordinate;
        centerMapView.latitude = centerMapView.latitude + ((distanceBetweenMeAndPoint)/111000);//1 degree = 111000m
        
        MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(centerMapView,
                                                                       distanceBetweenMeAndPoint*3,
                                                                       distanceBetweenMeAndPoint*3);
        [self.mapView setRegion:[self.mapView regionThatFits:region] animated:YES];
        
        // Add an annotation
        MKPointAnnotation *point = [[MKPointAnnotation alloc] init];
        point.coordinate = mostRecommendedItem.location.coordinate;
        point.title = mostRecommendedItem.name;
        point.subtitle = mostRecommendedItem.twitterHandle;
        [self.mapView addAnnotation:point];
        
        [self.mapView selectAnnotation:point animated:YES];
    }
    if (self.model.recommendedItems.count > 1) {
        [self.showMoreRecsButton setTitle:[NSString stringWithFormat:@"Show %u other recommended places...", self.model.recommendedItems.count - 1] forState:UIControlStateNormal];
        [self.showMoreRecsButton setHidden:NO];
    } else {
        [self.showMoreRecsButton setHidden:YES];
    }
    
}

-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation
{
    [self.mapView setShowsUserLocation:YES];
    [self updateSplashDisplay];
}
- (void) locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations {
    //NSLog([NSString stringWithFormat:@"Location found, accuracy=%f", [self.locationManager.location horizontalAccuracy]]);
    //if ([self.locationManager.location horizontalAccuracy] < 500)
    [self updateSplashDisplay];

}

- (void) locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    NSLog(@"Can't get fix");
}

@end
