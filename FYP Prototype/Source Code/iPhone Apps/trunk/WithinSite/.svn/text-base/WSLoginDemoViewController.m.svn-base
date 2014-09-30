//
//  WSLoginDemoViewController.m
//  WithinSite
//
//  Created by Andrew on 16/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSLoginDemoViewController.h"
#import "WSAppDelegate.h"
#import <Accounts/Accounts.h>
#import <Social/Social.h>
#import "WSModel.h"
#import "MBProgressHUD.h"
#import "WSUtility.h"

@interface WSLoginDemoViewController ()

@property (nonatomic, strong) NSArray *pageImages;
@property (nonatomic, strong) NSMutableArray *pageViews;

- (void)loadVisiblePages;
- (void)loadPage:(NSInteger)page;
- (void)purgePage:(NSInteger)page;

@property (nonatomic, strong) ACAccountStore *accountStore;
@property (nonatomic, strong) ACAccount *facebookAccount;

@property (nonatomic)  WSModel *model;

@end

@implementation WSLoginDemoViewController
@synthesize scrollView = _scrollView;
@synthesize pageControl = _pageControl;

@synthesize pageImages = _pageImages;
@synthesize pageViews = _pageViews;

@synthesize signInWithFacebookButton;
@synthesize accountStore;
@synthesize facebookAccount;

- (WSModel *)model{
    WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
    return [appDelegate model];
}

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
	self.pageImages = [NSArray arrayWithObjects:
                       [UIImage imageNamed:@"DemoSplash0@2x.png"],
                       [UIImage imageNamed:@"DemoSplash1@2x.png"],
                       [UIImage imageNamed:@"DemoSplash2@2x.png"],
                       [UIImage imageNamed:@"DemoSplash3@2x.png"],
                       nil];
    
    NSInteger pageCount = self.pageImages.count;
    
    // Set up the page control
    self.pageControl.currentPage = 0;
    self.pageControl.numberOfPages = pageCount;
    
    // Set up the array to hold the views for each page
    self.pageViews = [[NSMutableArray alloc] init];
    for (NSInteger i = 0; i < pageCount; ++i) {
        [self.pageViews addObject:[NSNull null]];
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    // Set up the content size of the scroll view
    CGSize pagesScrollViewSize = self.scrollView.frame.size;
    self.scrollView.contentSize = CGSizeMake(pagesScrollViewSize.width * self.pageImages.count, pagesScrollViewSize.height);
    
    // Load the initial set of pages that are on screen
    [self loadVisiblePages];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    if ([defaults stringForKey:@"user_fb_access_token"]) {
        [signInWithFacebookButton setTitle:@"Signed in with Facebook" forState:UIControlStateNormal];
        [signInWithFacebookButton setEnabled:NO];
    } else {
        [signInWithFacebookButton setTitle:@"Sign in with Facebook" forState:UIControlStateNormal];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)loadVisiblePages {
    // First, determine which page is currently visible
    CGFloat pageWidth = self.scrollView.frame.size.width;
    NSInteger page = (NSInteger)floor((self.scrollView.contentOffset.x * 2.0f + pageWidth) / (pageWidth * 2.0f));
    
    // Update the page control
    self.pageControl.currentPage = page;
    
    // Work out which pages you want to load
    NSInteger firstPage = page - 1;
    NSInteger lastPage = page + 1;
    
    // Purge anything before the first page
    for (NSInteger i=0; i<firstPage; i++) {
        [self purgePage:i];
    }
    for (NSInteger i=firstPage; i<=lastPage; i++) {
        [self loadPage:i];
    }
    for (NSInteger i=lastPage+1; i<self.pageImages.count; i++) {
        [self purgePage:i];
    }
}

- (void)loadPage:(NSInteger)page {
    if (page < 0 || page >= self.pageImages.count) {
        // If it's outside the range of what we have to display, then do nothing
        return;
    }
    
    // Load an individual page, first checking if you've already loaded it
    UIView *pageView = [self.pageViews objectAtIndex:page];
    if ((NSNull*)pageView == [NSNull null]) {
        CGRect frame = self.scrollView.bounds;
        frame.origin.x = frame.size.width * page;
        frame.origin.y = 0.0f;
        frame = CGRectInset(frame, 10.0f, 0.0f);
        
        UIImageView *newPageView = [[UIImageView alloc] initWithImage:[self.pageImages objectAtIndex:page]];
        newPageView.contentMode = UIViewContentModeScaleAspectFit;
        newPageView.frame = frame;
        [self.scrollView addSubview:newPageView];
        [self.pageViews replaceObjectAtIndex:page withObject:newPageView];
    }
}

- (void)purgePage:(NSInteger)page {
    if (page < 0 || page >= self.pageImages.count) {
        // If it's outside the range of what you have to display, then do nothing
        return;
    }
    
    // Remove a page from the scroll view and reset the container array
    UIView *pageView = [self.pageViews objectAtIndex:page];
    if ((NSNull*)pageView != [NSNull null]) {
        [pageView removeFromSuperview];
        [self.pageViews replaceObjectAtIndex:page withObject:[NSNull null]];
    }
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    // Load the pages that are now on screen
    [self loadVisiblePages];
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
    
    
    if([[returnedJSON objectForKey:@"result"] isEqualToString:@"error"]){
        NSLog(@"[DEBUG] Error: %@", [returnedJSON objectForKey:@"message"]);
        
        //ERROR!
        
    } else {
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        
        [defaults setObject:[returnedJSON objectForKey:@"_id"] forKey:@"user_mongo_id"];
        [defaults setObject:[returnedJSON objectForKey:@"email"] forKey:@"user_email"];
        [defaults setObject:[returnedJSON objectForKey:@"fb_access_token"] forKey:@"user_fb_access_token"];
        [defaults setObject:[returnedJSON objectForKey:@"push_token"] forKey:@"user_push_token"];
        
    }
    
}

- (IBAction)signInWithFbPressed:(id)sender
{
    MBProgressHUD *hud = [WSUtility createAndShowLoadingHudOnView:self.view withText:@"Loading" withDetailText:@"Nearby places of interest..."];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^(void) {
        
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
                                                                            // Callback on the main queue to update UI.
                                                                            dispatch_async(dispatch_get_main_queue(), ^(void) {
                                                                                [hud hide:YES];
                                                                                [self dismissViewControllerAnimated:YES completion:^{
                                                                                    WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
                                                                                    [appDelegate setupLocationManager];
                                                                                }];
                                                                            });
                                                                            
                                                                        }
                                                                        
                                                                        
                                                                    }
                                                                    else{
                                                                        [hud hide:YES];
                                                                        [self.model showMessageOnMainThreadTitled:@"Error" withMessage:@"Failed to receive Facebook permissions."];
                                                                    }
                                                                    
                                                                }];
                                                                
                                                                
                                                                
                                                                
                                                                
                                                                
                                                            } else {
                                                                dispatch_async(dispatch_get_main_queue(), ^(void) {
                                                                    [hud hide:YES];
                                                                });
                                                                [self.model showMessageOnMainThreadTitled:@"Error" withMessage:@"Failed to receive Facebook permissions."];
                                                                NSLog(@"error is: %@",[error description]);
                                                            }
                                                        }];
                                                        
                                                    } else {
                                                        dispatch_async(dispatch_get_main_queue(), ^(void) {
                                                            [hud hide:YES];
                                                        });
                                                        [self.model showMessageOnMainThreadTitled:@"Error" withMessage:@"Not granted access to Facebook account stored in iOS. Please login by going to the Settings app."];
                                                        //Fail gracefully...
                                                        NSLog(@"error getting permission %@",e);
                                                    } }];
        dispatch_async(dispatch_get_main_queue(), ^(void) {
            [hud hide:YES];
        });

    });

}

- (IBAction)closeButtonPressed:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}
@end
