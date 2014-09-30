//
//  WSSplashBubbleMiniViewController.m
//  WithinSite
//
//  Created by Andrew on 13/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSSplashBubbleMiniViewController.h"

@interface WSSplashBubbleMiniViewController ()

@end

@implementation WSSplashBubbleMiniViewController

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
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)setupBubblewithWithinSitePlace:(WithinSitePlace *)place{
    self.titleLabel.text = place.getPlaceName;
    self.categoryLabel.text = place.getPlaceCategories[0];
    
    int friends = [place.getPlaceSocialNameDrops count];
    
    if(friends > 0){
        self.friendImage0.hidden = NO;
        self.friendImage0.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:place.getPlaceSocialNameDrops[0][@"profile_picture"]]]];
        
        if(friends > 1){
            self.friendImage1.hidden = NO;
            self.friendImage1.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:place.getPlaceSocialNameDrops[1][@"profile_picture"]]]];
        } else {
            self.friendImage1.hidden = YES;
            self.friendImage2.hidden = YES;
            self.friendImage3.hidden = YES;
            self.friendsLeftLabel.hidden = YES;
        }
        
        if(friends > 2){
            self.friendImage2.hidden = NO;
            self.friendImage2.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:place.getPlaceSocialNameDrops[2][@"profile_picture"]]]];
        } else {
            self.friendImage2.hidden = YES;
            self.friendImage3.hidden = YES;
            self.friendsLeftLabel.hidden = YES;
        }
        
        if(friends > 3){
            self.friendImage3.hidden = NO;
            self.friendImage3.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:place.getPlaceSocialNameDrops[3][@"profile_picture"]]]];
        } else {
            self.friendImage3.hidden = YES;
            self.friendsLeftLabel.hidden = YES;
        }
        
        if(friends > 4){
            self.friendsLeftLabel.hidden = NO;
            self.friendsLeftLabel.text = [NSString stringWithFormat:@"+%d", friends-4];
        } else {
            self.friendsLeftLabel.hidden = YES;
        }
        
    } else {
        self.friendImage0.hidden = YES;
        self.friendImage1.hidden = YES;
        self.friendImage2.hidden = YES;
        self.friendImage3.hidden = YES;
        self.friendsLeftLabel.hidden = YES;
    }
    
    self.descriptionLabel.text = place.getPlaceDescription;

    if(place.getPlacePictures.count > 0){
        self.tokenImage.hidden = NO;
        self.tokenImage.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:place.getPlacePictures[0]]]];
    } else {
        self.tokenImage.hidden = YES;
    }

    self.numCheckinsLabel.text = [NSString stringWithFormat:@"%i", place.getPlaceCheckins];

    self.numFansLabel.text = [NSString stringWithFormat:@"%i", place.getPlaceFans];

    self.numFootfallLabel.text = [NSString stringWithFormat:@"%i", place.getPlaceFootfall];
    
}

@end
