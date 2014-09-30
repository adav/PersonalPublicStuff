//
//  WSRootViewController.m
//  WithinSite
//
//  Created by Andrew on 12/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSRootViewController.h"

@interface WSRootViewController ()

@end

@implementation WSRootViewController

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
    
    self.topViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"Home"];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
