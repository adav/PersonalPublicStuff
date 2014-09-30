//
//  WSSimpleWebViewController.h
//  WithinSite
//
//  Created by Andrew on 17/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WSSimpleWebViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIWebView *webView;
@property (weak, nonatomic) NSURL *url;

@end
