//
//  WSLoginViewController.m
//  WithinSite
//
//  Created by Andrew on 14/01/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSSettingsViewController.h"
#import "WSAppDelegate.h"
#import <Accounts/Accounts.h>
#import <Social/Social.h>


@interface WSSettingsViewController ()
@property (nonatomic, strong) ACAccountStore *accountStore;
@property (nonatomic, strong) ACAccount *facebookAccount;
@end

@implementation WSSettingsViewController
@synthesize accountStore;
@synthesize facebookAccount;
@synthesize activityIndicator;
@synthesize errorLabel;
@synthesize facebookLoginButton;

@synthesize debugPrefsServer;
@synthesize debugPrefsLocationRadius;
@synthesize debugDemoModeSwitch;

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [debugPrefsServer setText:[defaults objectForKey:@"debugPrefsServer"]];
    [debugPrefsLocationRadius setText:[NSString stringWithFormat:@"%@", [defaults objectForKey:@"debugPrefsLocationRadius"]]];
    
    if ([defaults stringForKey:@"user_fb_access_token"]) {
        [facebookLoginButton setTitle:@"Facebook Logged In" forState:UIControlStateNormal];
    } else {
        [facebookLoginButton setTitle:@"Login with Facebook" forState:UIControlStateNormal];
    }

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void) registerUserOnBackendByEmail:(NSString *)email
                 withFacebookAccessToken:(NSString *)fbToken
               withPushNotificationToken:(NSString *)apnsToken
{
    /*
     user.put("email", request.getParameter("email"));
     user.put("fb_access_token", request.getParameter("fb_access_token"));
     user.put("push_token", request.getParameter("push_token"));
     user.put("device_registered", request.getParameter("device_registered"));
     */
    
    NSString *post = [NSString stringWithFormat:@"email=%@&fb_access_token=%@&push_token=%@&device_registered=APPLE_IPHONE",
                      email,
                      fbToken,
                      apnsToken
                      ];
    
    NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    
    NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL: [NSURL URLWithString:
      [[[NSUserDefaults standardUserDefaults] stringForKey:@"debugPrefsServer"] stringByAppendingString:@"RegisterUser"]]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    NSData *serverResponseData;
    NSURLResponse *response;
    serverResponseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:nil];
    
    
    //NSLog(serverResponseData);
    
    //sort out the JSON response
    NSDictionary *returnedJSON = nil;
    
    if(serverResponseData){
        returnedJSON = [NSJSONSerialization JSONObjectWithData:serverResponseData options:NSJSONReadingAllowFragments error:nil];
    }
    
    [activityIndicator setHidden:YES];
    
    if([[returnedJSON objectForKey:@"result"] isEqualToString:@"error"]){
        NSLog(@"[DEBUG] Error: %@", [returnedJSON objectForKey:@"message"]);
        [errorLabel setText:[returnedJSON objectForKey:@"message"]];
        [errorLabel setHidden:NO];
    } else {
    
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        
        [defaults setObject:[returnedJSON objectForKey:@"_id"] forKey:@"user_mongo_id"];
        [defaults setObject:[returnedJSON objectForKey:@"email"] forKey:@"user_email"];
        [defaults setObject:[returnedJSON objectForKey:@"fb_access_token"] forKey:@"user_fb_access_token"];
        [defaults setObject:[returnedJSON objectForKey:@"push_token"] forKey:@"user_push_token"];
        
    }

}

- (IBAction)loginWithFacebookPressed:(id)sender {
    
    [errorLabel setHidden:YES];
    
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    [defaults removeObjectForKey:@"user_mongo_id"];
    [defaults removeObjectForKey:@"user_email"];
    [defaults removeObjectForKey:@"user_fb_access_token"];
    [defaults removeObjectForKey:@"user_push_token"];
    
    
    self.accountStore = [[ACAccountStore alloc]init];
    ACAccountType *FBaccountType= [self.accountStore accountTypeWithAccountTypeIdentifier:
                                   ACAccountTypeIdentifierFacebook];
    NSString *key = @"325368314245378"; //put your own key from FB here
    NSMutableDictionary *dictFB = [NSMutableDictionary dictionaryWithObjectsAndKeys:key,ACFacebookAppIdKey,@[@"email"],ACFacebookPermissionsKey, nil];
    
    if([[self.accountStore accountsWithAccountType:FBaccountType] count] > 0){
        NSArray *accounts = [self.accountStore accountsWithAccountType:FBaccountType];
        //it will always be the last object with SSO
        self.facebookAccount = [accounts lastObject];
        
        [self.accountStore renewCredentialsForAccount:self.facebookAccount completion:^(ACAccountCredentialRenewResult renewResult, NSError *error) {
            NSLog(@"Renew result = %d",renewResult);
        } ];
    }
    
    [self.accountStore requestAccessToAccountsWithType:FBaccountType options:dictFB
                                            completion: ^(BOOL granted, NSError *e) {
                                                if (granted) {
                                                    NSArray *accounts = [self.accountStore accountsWithAccountType:FBaccountType];
                                                    //it will always be the last object with SSO
                                                    self.facebookAccount = [accounts lastObject];
                                                    NSLog(@"LOGGED IN!");
                                                    ACAccountCredential *fbCredential = facebookAccount.credential;
                                                    NSLog(@"Facebook credential is: %@", fbCredential.oauthToken);

                                                    [[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%@", fbCredential.oauthToken] forKey:@"deviceFbToken"];
                                                    
                                                    NSArray *additionalPermissions = @[@"user_activities", @"user_interests", @"user_likes", @"user_location", @"user_status", @"user_checkins", @"friends_activities", @"friends_interests", @"friends_likes", @"friends_location", @"friends_status", @"friends_checkins"];
                                                    
                                                    [dictFB setObject:additionalPermissions forKey:ACFacebookPermissionsKey];
                    
                                                    [self.accountStore requestAccessToAccountsWithType:FBaccountType options:dictFB completion:^(BOOL granted, NSError *error) {
                                                        if(granted && error == nil) {
                                                            NSURL *requestURL = [NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/me"]];
                                                            SLRequest *request = [SLRequest requestForServiceType:SLServiceTypeFacebook
                                                                                                    requestMethod:SLRequestMethodGET
                                                                                                              URL:requestURL
                                                                                                       parameters:nil];
                                                            request.account = self.facebookAccount;
                                                            [request performRequestWithHandler:^(NSData *data,
                                                                                                 NSHTTPURLResponse *response,
                                                                                                 NSError *error) {
                                                                
                                                                if(!error){
                                                                    NSDictionary *list =[NSJSONSerialization JSONObjectWithData:data
                                                                                                                        options:kNilOptions error:&error];
                                                                    //NSLog(@"Dictionary contains: %@", list );
                                                                    
                                                                    NSString *email = [list objectForKey:@"email"];
                                                                    NSString *fbToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceFbToken"];
                                                                    NSString *apnsToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceApnsToken"];
                                                                    
                                                                    
                                                                    [self registerUserOnBackendByEmail:email
                                                                               withFacebookAccessToken:fbToken
                                                                             withPushNotificationToken:apnsToken];
                                                                    
                                                                    
                                                                    
                                                                    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
                                                                
                                                                    if([defaults objectForKey:@"user_mongo_id"]){
                                                                        
                                                                        [self dismissViewControllerAnimated:YES completion:^{
                                                                            WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
                                                                            [appDelegate setupLocationManager];
                                                                        }];
                                                                    }
                                                                    
                                                                    
                                                                }
                                                                else{
                                                                    //handle error gracefully
                                                                }
                                                                
                                                            }];
                                                            
                                                            
                                                            
                                                            
                                                            
                                                            
                                                        } else {
                                                            NSLog(@"error is: %@",[error description]);
                                                        }
                                                    }];
                                                    
                                                } else {
                                                    //Fail gracefully...
                                                    NSLog(@"error getting permission %@",e);
                                                } }];
    
        [activityIndicator setHidden:NO];

}
- (IBAction)updateDebugPrefsPressed:(id)sender {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:[debugPrefsServer text] forKey:@"debugPrefsServer"];
    NSInteger *radius = [[debugPrefsLocationRadius text] integerValue];
    [defaults setInteger:radius forKey:@"debugPrefsLocationRadius"];
}
- (IBAction)updateDemoMode:(UISwitch *)sender {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:[NSNumber numberWithBool:[sender isOn]] forKey:@"debugDemoMode"];
}

- (IBAction)closeButtonPressed:(id)sender {
    [self dismissViewControllerAnimated:YES completion:^{}];
}
@end
