//
//  WSLoginDemoViewController.h
//  WithinSite
//
//  Created by Andrew on 16/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WSLoginDemoViewController : UIViewController<UIScrollViewDelegate>

@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;
@property (nonatomic, strong) IBOutlet UIPageControl *pageControl;

@property (weak, nonatomic) IBOutlet UIButton *signInWithFacebookButton;

- (IBAction)signInWithFbPressed:(id)sender;

- (IBAction)closeButtonPressed:(id)sender;

@end
