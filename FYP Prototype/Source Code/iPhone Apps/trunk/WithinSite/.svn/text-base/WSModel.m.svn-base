//
//  WSModel.m
//  WithinSite
//
//  Created by Andrew on 12/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSModel.h"

@interface WSModel()
@property (strong, nonatomic) NSMutableDictionary *nearbyPlacesDictionary;
@property (strong, nonatomic) NSArray *recommedationsArray;
@end

@implementation WSModel
@synthesize nearbyPlacesDictionary = _nearbyPlacesDictionary;
@synthesize recommedationsArray = _recommedationsArray;

- (NSMutableDictionary *) nearbyPlacesDictionary {
    if(!_nearbyPlacesDictionary){
        _nearbyPlacesDictionary = [[NSMutableDictionary alloc] init];
    }
    return _nearbyPlacesDictionary;
}

- (void) setNearbyPlacesDictionary:(NSMutableDictionary *)newNearbyPlacesDictionary{
    _nearbyPlacesDictionary = newNearbyPlacesDictionary;
}

- (NSArray *) recommedationsArray {
    if(!_recommedationsArray){
        _recommedationsArray = [[NSArray alloc] init];
    }
    return _recommedationsArray;
}

- (void) setRecommedationsArray:(NSArray *)newRecommedationsArray{
    _recommedationsArray = newRecommedationsArray;
}

- (void)updateNearbyPlacesDictionaryWithPositionLongitude:(double)longitude Latitude:(double)latitude
{
    NSString *get = [NSString stringWithFormat:@"latitude=%f&longitude=%f",
                     latitude,
                     longitude
                     ];    
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    if([defaults objectForKey:@"user_fb_access_token"]
       && [defaults objectForKey:@"user_email"]){
        
        get = [get stringByAppendingString:
               [NSString stringWithFormat:
                @"&email=%@",
                [defaults stringForKey:@"user_email"]]];
        
    }
    
    NSString *url = [[[defaults stringForKey:@"debugPrefsServer"] stringByAppendingString:@"Nearby?"] stringByAppendingString:get];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setHTTPMethod:@"GET"];
    [request setURL:[NSURL URLWithString:url]];
    [request setTimeoutInterval:120];
    
    NSError *error = [[NSError alloc] init];
    NSHTTPURLResponse *responseCode = nil;
    
    NSData *serverResponse = [NSURLConnection sendSynchronousRequest:request returningResponse:&responseCode error:&error];
    
    if([responseCode statusCode] != 200){
        NSLog(@"Error getting %@, HTTP status code %i", url, [responseCode statusCode]);

    }
    
    //NSData *serverResponse = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]]; //the old way of doing it...

    NSError *jsonError = nil;
    
    NSDictionary *returnedJSON = nil;
    
    if(serverResponse){
        returnedJSON = [NSJSONSerialization JSONObjectWithData:serverResponse options:NSJSONReadingAllowFragments error:&jsonError];
        [self.nearbyPlacesDictionary setDictionary:returnedJSON];
    }
    
}

- (void)updateRecommendationListWithPositionLongitude:(double)longitude Latitude:(double)latitude
{
    NSString *get = [NSString stringWithFormat:@"latitude=%f&longitude=%f",
                     latitude,
                     longitude
                     ];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    if([defaults objectForKey:@"user_fb_access_token"]
       && [defaults objectForKey:@"user_email"]){
        
        get = [get stringByAppendingString:
               [NSString stringWithFormat:
                @"&email=%@",
                [defaults stringForKey:@"user_email"]]];
        
    }
    
    NSString *url = [[[defaults stringForKey:@"debugPrefsServer"] stringByAppendingString:@"UserRecommendations?"] stringByAppendingString:get];
    
    
    NSError *error = nil;
    NSData *serverResponse = [NSData dataWithContentsOfURL:[NSURL URLWithString:url] options:NSDataReadingUncached error:&error];
    
    NSDictionary *returnedJSON = nil;
    
    if(serverResponse){
        returnedJSON = [NSJSONSerialization JSONObjectWithData:serverResponse options:NSJSONReadingAllowFragments error:&error];
        _recommedationsArray = returnedJSON[@"places"];
    } else {        
        NSLog(@"Error in updateRecommendationListWithPositionLongitude(): %@", error);
        [self showMessageOnMainThreadTitled:@"Error" withMessage:[NSString stringWithFormat:@"Unable to retrieve recommendations. DEBUG: %@", error]];
        
    }

}

-(void)clearRecommendationsOnServerAndList
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *url = [[defaults stringForKey:@"debugPrefsServer"] stringByAppendingString:
                     [NSString stringWithFormat:@"UserRecommendations?email=%@",
                      [defaults stringForKey:@"user_email"]]];
    
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc]
                                    initWithURL:[NSURL URLWithString:url]];
    
    [request setHTTPMethod:@"DELETE"];
    
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self startImmediately:YES];
    
    NSLog(@"Model.clearRecommendationsOnServerAndList(): %@", connection);
    
    _recommedationsArray = nil;
    
}

- (void)showMessageOnMainThreadTitled:(NSString *)title withMessage:(NSString *)message
{
    dispatch_async(dispatch_get_main_queue(), ^{
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title
                                                            message:message
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
        [alertView show];
    });
}

@end
