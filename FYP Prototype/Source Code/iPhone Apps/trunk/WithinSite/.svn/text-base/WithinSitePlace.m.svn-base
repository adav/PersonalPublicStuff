//
//  WithinSitePlace.m
//  WithinSite
//
//  Created by Andrew on 13/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WithinSitePlace.h"

@interface WithinSitePlace ()

@property (nonatomic) NSDictionary *dictionary;
@end

@implementation WithinSitePlace
@synthesize dictionary = _dictionary;

+(WithinSitePlace *)initWithDictionary:(NSDictionary *)dictionary{
    WithinSitePlace *wsp = [[WithinSitePlace alloc] init];
    [wsp setDictionary:dictionary];
    return wsp;
}

-(void)setDictionary:(NSDictionary *)dictionary{
    _dictionary = dictionary;
}

-(NSDictionary *)getDictionary{
    return _dictionary;
}

-(NSString *)getPlaceName{
    return _dictionary[@"name"];
}

-(NSString *)getPlaceDescription{
    return _dictionary[@"description"];
}

-(CLLocation *)getPlaceLocation{
    CLLocationDegrees longitude = [_dictionary[@"location"][@"longitude"] doubleValue];
    CLLocationDegrees latitude = [_dictionary[@"location"][@"latitude"] doubleValue];
    return [[CLLocation alloc] initWithLatitude:latitude longitude:longitude];
}

-(NSArray *)getPlaceCategories{
    return _dictionary[@"categories"];
}

-(int)getPlaceCheckins{
    return [_dictionary[@"checkins"] integerValue];
}

-(int)getPlaceFans{
    return [_dictionary[@"fans"] integerValue];
}

-(int)getPlaceFootfall{
    return [_dictionary[@"footfall"] integerValue];
}

-(NSArray *)getPlaceSocialNameDrops{
    return _dictionary[@"social_name_drop"];
}

-(NSArray *)getPlaceLinks{
    return _dictionary[@"links"];
}

-(NSArray *)getPlacePictures{
    return _dictionary[@"pictures"];
}

-(NSString *)getPlaceServiceName{
    return _dictionary[@"service_name"];
}

-(NSString *)getPlaceServiceId{
    return _dictionary[@"service_id"];
}

-(bool)getPlaceVerified{
    return _dictionary[@"verified"];
}
-(int)getPlaceRanking{
    return [_dictionary[@"ranking"] integerValue];
}

-(int)getRecommendationDaysOld{
    return [_dictionary[@"recommendation_age"] integerValue];
}

-(double)getRecommendationDistance{
    return [[_dictionary objectForKey:@"recommendation_distance"] doubleValue];
}

@end
