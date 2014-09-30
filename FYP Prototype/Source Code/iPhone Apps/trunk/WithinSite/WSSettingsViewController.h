//
//  WSLoginViewController.h
//  WithinSite
//
//  Created by Andrew on 14/01/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WSSettingsViewController : UIViewController

- (IBAction)loginWithFacebookPressed:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *errorLabel;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *activityIndicator;
@property (weak, nonatomic) IBOutlet UIButton *facebookLoginButton;

@property (weak, nonatomic) IBOutlet UITextField *debugPrefsServer;
@property (weak, nonatomic) IBOutlet UISwitch *debugDemoModeSwitch;
@property (weak, nonatomic) IBOutlet UITextField *debugPrefsLocationRadius;
- (IBAction)updateDebugPrefsPressed:(id)sender;
- (IBAction)closeButtonPressed:(id)sender;
@end
