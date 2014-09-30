//
//  SimpleGeoModel.m
//  SimpleGeoDisco
//
//  Created by Andrew on 02/12/2012.
//  Copyright (c) 2012 AndrewDavidson. All rights reserved.
//

#import "SimpleGeoModel.h"
#import "SimpleGeoRecommendedDataItem.h"

@interface SimpleGeoModel()
@property (nonatomic, strong) NSMutableArray *recommendedItems;
@end

@implementation SimpleGeoModel

@synthesize recommendedItems = _recommendedItems;

-(NSMutableArray *)recommendedItems
{
    if(!_recommendedItems){
        _recommendedItems = [[NSMutableArray alloc] init];
    }
    return _recommendedItems;
}

-(void)setRecommendedItems:(NSMutableArray *)newRecommendedItems{
    _recommendedItems = newRecommendedItems;
}

-(void)populateRecommendedItems:(CLLocation *)userLocation
{
    [self.recommendedItems removeAllObjects];
    
    //get lat and long
    NSString *latitude = [[NSNumber numberWithDouble:userLocation.coordinate.latitude] stringValue];
    NSString *longitude = [[NSNumber numberWithDouble:userLocation.coordinate.longitude] stringValue];
    
    //make call to web server 
    // (LOCAL TEST SWAP) http://10.2.2.254:8080/WebApplication1/NearbyFourSquareLocations?longitude=%@&latitude=%@
    NSData *serverResponse = [NSData dataWithContentsOfURL:
                        [NSURL URLWithString:[NSString stringWithFormat:@"http://wwwteach.cs.bham.ac.uk/axd967/WebApplication1/NearbyFourSquareLocations?longitude=%@&latitude=%@", longitude, latitude]]
                        ];
    
    NSLog(@"serverResponse:");
    //NSLog(serverResponse);
    
    //sort out the JSON response
    NSDictionary *returnedJSON = nil;
    
    if(serverResponse){
        returnedJSON = [NSJSONSerialization JSONObjectWithData:serverResponse options:NSJSONReadingAllowFragments error:nil];
    }
    NSLog(@"returnedJSON:");
    //NSLog(returnedJSON);
    
    NSArray *placesArray = [returnedJSON valueForKeyPath:@"places"];
    placesArray = [placesArray objectAtIndex:0];
    for (NSDictionary *place in placesArray) {
        SimpleGeoRecommendedDataItem *newItem = [SimpleGeoRecommendedDataItem new];
        newItem.foursquareVerified = [[place valueForKey:@"fsVerified"] boolValue];
        newItem.facebookProfile = place[@"facebook"];
        newItem.twitterHandle = place[@"twitter"];
        newItem.location = [[CLLocation alloc] initWithLatitude:[[place valueForKey:@"latitude"] doubleValue]
                                                      longitude:[[place valueForKey:@"longitude"] doubleValue]];
        newItem.recommendationScore = [[place valueForKey:@"recScore"] integerValue];
        newItem.foursquareCategories = place[@"categories"];
        newItem.name = place[@"name"];
        [self.recommendedItems addObject:newItem];

    }

    
    
    
    //old:
    
    /*
    CLLocation *currentPosition = self.locationManager.location;
    
    NSString *latitude = [[NSNumber numberWithDouble:currentPosition.coordinate.latitude] stringValue];
    NSString *longitude = [[NSNumber numberWithDouble:currentPosition.coordinate.longitude] stringValue];
    NSLog(@"Lat: %@", latitude);
    NSLog(@"Long: %@", longitude);
    //@"http://api.geonames.org/findNearbyWikipediaJSON?lat=%@&lng=%@&username=demo"
    NSData *wikiData = [NSData dataWithContentsOfURL:
                        [NSURL URLWithString:[NSString stringWithFormat:@"http://wwwteach.cs.bham.ac.uk/axd967/WebApplication1/NearbyFourSquareLocations?longitude=%@&latitude=%@", longitude, latitude]]
                        ];
    
    NSDictionary *returnedJSON = nil;
    if(wikiData){
        returnedJSON = [NSJSONSerialization JSONObjectWithData:wikiData options:kNilOptions error:nil];
    }
    
    [self.boringLabel setText:[NSString stringWithFormat:@"Nearest place is %@",
                               returnedJSON[@"places"][0][@"name"]]];
    
    [tableData removeAllObjects];
    for (NSDictionary *place in returnedJSON[@"places"]) {
        [tableData addObject:place[@"name"]];
    }
    
    [self.tableOfPlaces reloadData];
    */

}

@end
