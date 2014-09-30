//
//  WSNearbyPlacesSidebarViewController.m
//  WithinSite
//
//  Created by Andrew on 12/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSNearbyPlacesSidebarViewController.h"
#import <ECSlidingViewController.h>
#import "WSAppDelegate.h"
#import "WSModel.h"
#import "WSPlaceCell.h"
#import "MBProgressHUD.h"
#import "WSUtility.h"
#import "WSPlaceDetailViewController.h"
#import "WithinSitePlace.h"

@interface WSNearbyPlacesSidebarViewController ()
@property (nonatomic)  WSModel *model;
@end

@implementation WSNearbyPlacesSidebarViewController
@synthesize nearbyPlacesTableView;

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
	// Do any additional setup after loading the view.
    [self.slidingViewController setAnchorLeftRevealAmount:250.0f];
    self.slidingViewController.underRightWidthLayout = ECFullWidth;
    
    
    
    [self.nearbyPlacesTableView setDataSource:self];
    
    UIRefreshControl *refreshControl = [[UIRefreshControl alloc] init];
    refreshControl.attributedTitle = [[NSAttributedString alloc] initWithString:@"Pull to Refresh"];
    [refreshControl addTarget:self action:@selector(refreshNearbyPlacesCollectionView:)
             forControlEvents:UIControlEventValueChanged];
    [self.nearbyPlacesTableView addSubview:refreshControl];

    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) refreshNearbyPlacesCollectionView:(UIRefreshControl *)refresh  {
    NSLog(@"refresh pulled");
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:@"Refreshing recommendations..."];
    
    
    // Refresh logic:
    MBProgressHUD *hud = [WSUtility createAndShowLoadingHudOnView:self.view withText:@"Loading" withDetailText:@"Nearby places of interest..."];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^(void) {
        
        WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
        CLLocation *location = [appDelegate locationManager].location;
        [self.model updateNearbyPlacesDictionaryWithPositionLongitude:location.coordinate.longitude Latitude:location.coordinate.latitude];        
        
        // Callback on the main queue to update UI.
        dispatch_async(dispatch_get_main_queue(), ^(void) {
            [self.nearbyPlacesTableView reloadData];
            [hud hide:YES];
        });
    });
    
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"MMM d, h:mm a"];
    NSString *lastUpdated = [NSString stringWithFormat:@"Last updated on %@", [formatter stringFromDate:[NSDate date]]];
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:lastUpdated];
    [refresh endRefreshing];
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    NSArray *serviceModuleResults = [self.model nearbyPlacesDictionary][@"results"];
    return serviceModuleResults.count;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    NSDictionary *serviceModuleResult = [self.model nearbyPlacesDictionary][@"results"][section];
    NSArray *placesArray = serviceModuleResult[@"places"];
    return placesArray.count;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"PlaceCell";
    
    WSPlaceCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        
        NSArray* views = [[NSBundle mainBundle] loadNibNamed:@"WSPlaceCell" owner:nil options:nil];
        
        for (UIView *view in views) {
            if([view isKindOfClass:[UITableViewCell class]])
            {
                cell = (WSPlaceCell*)view;
            }
        }
    }
    
    NSDictionary *serviceModuleResult = [self.model nearbyPlacesDictionary][@"results"][indexPath.section];
    NSArray *placesArray = serviceModuleResult[@"places"];
    NSDictionary *place = [placesArray objectAtIndex:indexPath.row];
    
    
    cell.titleLabel.text = [NSString stringWithFormat:@"%@", place[@"name"]];
    cell.featureLabel.text = [NSString stringWithFormat:@"Checkins: %@", place[@"checkins"]];
    cell.categoryLabel.text = place[@"categories"][0] ? [NSString stringWithFormat:@"%@", place[@"categories"][0]] : @"Uncategorised";
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self performSegueWithIdentifier:@"ShowDetail" sender:indexPath];
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if([segue.identifier isEqualToString:@"ShowDetail"]){
        NSIndexPath *indexPath = sender;
        WSPlaceDetailViewController *detailController = segue.destinationViewController;
        
        NSDictionary *serviceModuleResult = [self.model nearbyPlacesDictionary][@"results"][indexPath.section];
        NSArray *placesArray = serviceModuleResult[@"places"];
        NSDictionary *place = [placesArray objectAtIndex:indexPath.row];
        
        detailController.placeInquired = [WithinSitePlace initWithDictionary:place];
    }
    
    
}

- (IBAction)closeButtonPressed:(id)sender {
    [self.slidingViewController resetTopView];
}
@end
