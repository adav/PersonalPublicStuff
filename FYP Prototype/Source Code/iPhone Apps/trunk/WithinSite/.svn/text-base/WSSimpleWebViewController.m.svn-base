//
//  WSSimpleWebViewController.m
//  WithinSite
//
//  Created by Andrew on 17/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSSimpleWebViewController.h"

@interface WSSimpleWebViewController ()

@end

@implementation WSSimpleWebViewController
@synthesize url;
@synthesize webView;

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
	[webView loadRequest:[NSURLRequest requestWithURL:url]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
