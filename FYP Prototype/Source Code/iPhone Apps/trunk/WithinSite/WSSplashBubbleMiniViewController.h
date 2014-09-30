//
//  WSSplashBubbleMiniViewController.h
//  WithinSite
//
//  Created by Andrew on 13/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "WithinSitePlace.h"

@interface WSSplashBubbleMiniViewController : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *categoryLabel;
@property (weak, nonatomic) IBOutlet UIImageView *friendImage0;
@property (weak, nonatomic) IBOutlet UIImageView *friendImage1;
@property (weak, nonatomic) IBOutlet UIImageView *friendImage2;
@property (weak, nonatomic) IBOutlet UIImageView *friendImage3;
@property (weak, nonatomic) IBOutlet UILabel *friendsLeftLabel;
@property (weak, nonatomic) IBOutlet UILabel *descriptionLabel;
@property (weak, nonatomic) IBOutlet UIImageView *tokenImage;
@property (weak, nonatomic) IBOutlet UILabel *numCheckinsLabel;
@property (weak, nonatomic) IBOutlet UILabel *numFansLabel;
@property (weak, nonatomic) IBOutlet UILabel *numFootfallLabel;

-(void)setupBubblewithWithinSitePlace:(WithinSitePlace *)place;

@end
