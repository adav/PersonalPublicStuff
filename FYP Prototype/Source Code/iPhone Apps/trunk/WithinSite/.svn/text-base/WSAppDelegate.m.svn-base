//
//  WSAppDelegate.m
//  WithinSite
//
//  Created by Andrew on 13/01/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSAppDelegate.h"
#import "WSUtility.h"

#define CURRENT_REGION @"current_region_buffer"
#define BACKGROUND_QUEUE "com.AndrewDavidson.WithinSite.backgroundPollingQueue"
#define DEVICE_APNS_TOKEN @"deviceApnsToken"

@interface WSAppDelegate ()
@property (nonatomic, strong) CLLocation *lastSubmittedLocation;
@property (nonatomic) double staticLocationPollCount;
@property (nonatomic) BOOL isInitialisingRegion;
@end


@implementation WSAppDelegate
@synthesize lastSubmittedLocation;
@synthesize staticLocationPollCount;
@synthesize isInitialisingRegion;
@synthesize model;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // Override point for customization after application launch.
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    if (![defaults objectForKey:@"debugPrefsLocationRadius"]) {
        [defaults setInteger:400 forKey:@"debugPrefsLocationRadius"];
    }
    if (![defaults objectForKey:@"debugPrefsServer"]) {
        [defaults setObject:@"http://studentweb.cs.bham.ac.uk/axd967/WithinSite/" forKey:@"debugPrefsServer"];
    }
    
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes:
     (UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    
    //TODO check NSUserDefaults to see if we are logged in. If so do this...
    [self setupLocationManager]; //let's start sending locations to the server
    
    self.model = [[WSModel alloc] init];
    
    return YES;
}

- (void)setupLocationManager
{
    if (self.locationManager == nil) {
        self.locationManager = [[CLLocationManager alloc] init];
        self.locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
        self.locationManager.distanceFilter = 50;
        self.locationManager.delegate = self;
        //TODO revise whether Apple's optimisation can be accomodate better:
        self.locationManager.activityType = CLActivityTypeOther;
        self.locationManager.pausesLocationUpdatesAutomatically = NO;
    }
    
    staticLocationPollCount = 1;
    isInitialisingRegion = YES;
    
    [self.locationManager startUpdatingLocation];
    NSLog(@"setupLocationManager method in app delegate called. Started Updating Location.");
}

- (void) disableLocationManager
{
    [self.locationManager stopUpdatingLocation];
    NSArray *regionArray = [[self.locationManager monitoredRegions] allObjects];
    for (int i = 0; i < [regionArray count]; i++) { // loop through regions, stopping each
        [self.locationManager stopMonitoringForRegion:[regionArray objectAtIndex:i]];
    }
}

- (BOOL) isLocationManagerMonitoringRegions
{
    if ([self.locationManager monitoredRegions].count > 0) {
        return YES;
    } else {
        return NO;
    }
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.

    
//    [self.locationManager stopUpdatingLocation];
//    
//    [self.locationManager startMonitoringSignificantLocationChanges];
//    UILocalNotification *testNotification = [[UILocalNotification alloc] init];
//    testNotification.alertBody = @"Started monitoring significant location changes";
//    [[UIApplication sharedApplication] presentLocalNotificationNow:testNotification];
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    [application setApplicationIconBadgeNumber:0];
    [application cancelAllLocalNotifications];
    
    

    [self.locationManager startUpdatingLocation]; //increase accuracy when active
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    if ( application.applicationState == UIApplicationStateActive ){
        [self.model showMessageOnMainThreadTitled:@"New Recommendation" withMessage:userInfo[@"aps"][@"alert"]];
    }
    
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    CLLocation *newLocation = (CLLocation *)[locations lastObject];
    
    if([newLocation horizontalAccuracy] <= 100)
    {
        @synchronized(self){
            if (isInitialisingRegion)
            {
                [self initialiseNewRegion:newLocation];
            }
            
            /*if(isBigBrotherPoll)
            {
                
            }*/
        }
    }

    /*BOOL isAppInBackground = [UIApplication sharedApplication].applicationState == UIApplicationStateBackground;
    
    NSLog(@"didUpdateLocations method in delegate called");
    
    if(isAppInBackground)
    {
        UILocalNotification *testNotification = [[UILocalNotification alloc] init];
        testNotification.alertBody = @"Significant Location Detected.";
        NSLog(@"Sig Location Fired");
        [[UIApplication sharedApplication] presentLocalNotificationNow:testNotification];
    } else {
        NSLog(@"location manager in delagate fired for some reason!");
    }*/
    
}

- (void)initialiseNewRegion:(CLLocation *)newLocation
{
    @synchronized(self){
        isInitialisingRegion = NO; //Let's turn this off now we're here.
    }
    
    NSLog(@"[BACKGROUND LOCATIONS] Initialising a new region.");
    
    CLRegion *newRegion = [[CLRegion alloc] initCircularRegionWithCenter:[newLocation coordinate]
                                                                  radius:[WSUtility searchRadiusforSpeed:[newLocation speed]]
                                                              identifier:CURRENT_REGION];
    [self.locationManager startMonitoringForRegion:newRegion];
    
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(queue, ^{        
        [self sendLocationToServer:newLocation isRegionUpdate:YES]; //Run this on it's own thread to quicken UI.
    });
    
    //[self.locationManager stopUpdatingLocation];
    [self.locationManager setDesiredAccuracy:kCLLocationAccuracyKilometer];
    
    lastSubmittedLocation = newLocation;
    
    staticLocationPollCount = 0;
    
    dispatch_queue_t backgroundQueue = dispatch_queue_create(BACKGROUND_QUEUE, NULL);
    dispatch_time_t delay = dispatch_time(DISPATCH_TIME_NOW, 5 * pow(2,staticLocationPollCount) * 60 * NSEC_PER_SEC);
    dispatch_after(delay, backgroundQueue, ^(void){
        [self monitorPositionWithinRegion];
    });
    
    
    /*
    UILocalNotification *testNotification = [[UILocalNotification alloc] init];
    testNotification.alertBody = @"didUpdateLocations fired. Accuracy < 400. Started monitoring region.";
    testNotification.soundName = UILocalNotificationDefaultSoundName;
    [[UIApplication sharedApplication] presentLocalNotificationNow:testNotification];
     */
}

- (void)locationManager:(CLLocationManager *)manager didExitRegion:(CLRegion *)region
{
    if([[region identifier] isEqualToString:CURRENT_REGION])
    {
        
        @synchronized(self){
            isInitialisingRegion = YES; //Let's turn this back on for the update method.
        }
        
        NSLog(@"[BACKGROUND LOCATIONS] Exiting region. isInitialisingRegion set to YES. Calling startUpdatingLocation.");
        
        //[self.locationManager startUpdatingLocation];
        [self.locationManager setDesiredAccuracy:kCLLocationAccuracyNearestTenMeters];
        
        
        /*
         UILocalNotification *testNotification = [[UILocalNotification alloc] init];
        testNotification.alertBody = @"didExitRegion fired. Correct identifier. Started monitoring location.";
        testNotification.soundName = UILocalNotificationDefaultSoundName;
        [[UIApplication sharedApplication] presentLocalNotificationNow:testNotification];
         */
    } 
}

- (BOOL)locationIsValid:(CLLocation *)newLocation
{
    // Returns wether newLocation is worthy of being sent to the webserver according to it's accuracy and proximity to last sent location.
    return YES;
}

- (void)sendLocationToServer:(CLLocation *)newLocation isRegionUpdate:(BOOL)regionFlag
{
    if(![[NSUserDefaults standardUserDefaults] stringForKey:@"user_email"]){
        return;
    }
    
    if (regionFlag && ![self locationIsValid:newLocation]) {
        return;
    }
    
    NSString *post = [NSString stringWithFormat:@"email=%@&longitude=%f&latitude=%f&accuracy=%f&region_update=%@",
                      [[NSUserDefaults standardUserDefaults] stringForKey:@"user_email"],
                      [newLocation coordinate].longitude,
                      [newLocation coordinate].latitude,
                      [newLocation horizontalAccuracy],
                      regionFlag ? @"true" : @"false"
                      ];
    
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL: [NSURL URLWithString:
                      [[[NSUserDefaults standardUserDefaults] stringForKey:@"debugPrefsServer"] stringByAppendingString:@"UpdateUserLocation"]]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    NSData *serverResponseData;
    NSURLResponse *response;
    serverResponseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:nil];
    
    //sort out the JSON response
    NSDictionary *returnedJSON = nil;
    
    if(serverResponseData){
        returnedJSON = [NSJSONSerialization JSONObjectWithData:serverResponseData options:NSJSONReadingAllowFragments error:nil];
    }
    
    
    if([[returnedJSON objectForKey:@"result"] isEqualToString:@"error"]){
        NSLog(@"[DEBUG] Location update Error: %@", [returnedJSON objectForKey:@"message"]);
    } else {
        NSLog(@"Location update sent.");
    }
    
}

- (void)processNewLocation:(CLLocation *)newLocation
{
    // Manages newly received locations and decides whether or not they are valid, then sends them to the webserver. Updates the lastSubmittedLocation variable if sucessful.
}

- (void)monitorPositionWithinRegion
{
    NSLog(@"[BACKGROUND LOCATIONS] monitorPositionWithinRegion called. Last delay was %f minutes.", 5 * pow(2,staticLocationPollCount));
    [self.locationManager startUpdatingLocation];
    [self.locationManager setDesiredAccuracy:kCLLocationAccuracyBest];
    
    CLLocation *currentPosition = [self.locationManager location];
    if ([currentPosition horizontalAccuracy] < 60) {
        NSLog(@"[BACKGROUND LOCATIONS] monitorPositionWithinRegion detected user is still at the same location. Will update habits on server.");
        
        [self sendLocationToServer:currentPosition isRegionUpdate:NO];
        
        staticLocationPollCount++;
        
        dispatch_queue_t backgroundQueue = dispatch_queue_create(BACKGROUND_QUEUE, NULL);
        dispatch_time_t delay = dispatch_time(DISPATCH_TIME_NOW, 5 * pow(2,staticLocationPollCount) * 60 * NSEC_PER_SEC);
        dispatch_after(delay, backgroundQueue, ^(void){
            [self monitorPositionWithinRegion];
        });
    } else {
        staticLocationPollCount = 0;
    }
    
    [self.locationManager setDesiredAccuracy:kCLLocationAccuracyKilometer];
    [self.locationManager stopUpdatingLocation];
}

- (void)application:(UIApplication*)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData*)deviceToken
{;
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
	NSString *token = [deviceToken description];
    token = [token stringByReplacingOccurrencesOfString: @"<" withString: @""];
    token = [token stringByReplacingOccurrencesOfString: @">" withString: @""];
    token = [token stringByReplacingOccurrencesOfString: @" " withString: @""];
    
    if ([defaults stringForKey: DEVICE_APNS_TOKEN])
    {
        if (![[defaults stringForKey: DEVICE_APNS_TOKEN] isEqualToString: token])
        {
            [defaults setObject: token forKey: DEVICE_APNS_TOKEN];
            [defaults synchronize];

        }
    }
    else
    {
        [defaults setObject: token forKey: DEVICE_APNS_TOKEN];
        [defaults synchronize];
    }
    
    NSLog(@"%@",token);
}

- (void)application:(UIApplication*)application didFailToRegisterForRemoteNotificationsWithError:(NSError*)error
{
	NSLog(@"Failed to get token, error: %@", error);
}
@end
