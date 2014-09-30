//
//  WSUtility.m
//  WithinSite
//
//  Created by Andrew on 31/01/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSUtility.h"

@implementation WSUtility

+(NSInteger)searchRadiusforSpeed:(double)metersPerSecond
{ //y=1.037513153(1.000639812^x)
    
    /* TODO clever implementation
    float a = 1.037513153;
    float b = 1.000639812;
    float x = (float) metersPerSecond;
    return [[NSNumber numberWithFloat:a*(powf(b, x))] integerValue];
     
     */

    return [[NSUserDefaults standardUserDefaults] integerForKey:@"debugPrefsLocationRadius"];
}

+(MBProgressHUD *)createAndShowLoadingHudOnView:(UIView *)view withText:(NSString *)labelText withDetailText:(NSString *)labelDetailText
{
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.customView = [WSUtility spinningOwlLogo];
    hud.mode = MBProgressHUDModeCustomView;
    hud.labelText = labelText;
    hud.labelFont = [UIFont fontWithName:@"Gill Sans" size:13];
    hud.detailsLabelText = labelDetailText;
    hud.detailsLabelFont = [UIFont fontWithName:@"Gill Sans" size:11];
    
    return hud;
}

+(UIImageView *)spinningOwlLogo
{
    UIImage *statusImage = [UIImage imageNamed:@"withinsitelogorotate0.png"];
    UIImageView *activityImageView = [[UIImageView alloc]
                                      initWithImage:statusImage];
    
    
    //Add frames for annimation:
    activityImageView.animationImages = [NSArray arrayWithObjects:
                                         [UIImage imageNamed:@"withinsitelogorotate0.png"],
                                         [UIImage imageNamed:@"withinsitelogorotate1.png"],
                                         [UIImage imageNamed:@"withinsitelogorotate2.png"],
                                         [UIImage imageNamed:@"withinsitelogorotate3.png"],
                                         [UIImage imageNamed:@"withinsitelogorotate4.png"],
                                         [UIImage imageNamed:@"withinsitelogorotate5.png"],
                                         [UIImage imageNamed:@"withinsitelogorotate6.png"],
                                         [UIImage imageNamed:@"withinsitelogorotate7.png"],
                                         nil];
    
    activityImageView.animationDuration = 0.5;
    
    [activityImageView startAnimating];
    
    
    return activityImageView;
}

@end