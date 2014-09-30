//
//  WSRecommendationSidebarViewController.m
//  WithinSite
//
//  Created by Andrew on 12/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSRecommendationSidebarViewController.h"
#import <ECSlidingViewController.h>
#import "WSAppDelegate.h"
#import "WSModel.h"
#import "WithinSitePlace.h"
#import "WSPrevRecCell.h"
#import "MBProgressHUD.h"
#import "WSUtility.h"
#import "WSPlaceDetailViewController.h"

@interface WSRecommendationSidebarViewController ()

@end

@implementation WSRecommendationSidebarViewController
@synthesize recommendationsToggle;
@synthesize recommendationsTable;

- (WSModel *)model{
    WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
    return [appDelegate model];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    [self.slidingViewController setAnchorRightRevealAmount:250.0f];
    self.slidingViewController.underLeftWidthLayout = ECFullWidth;
    
    WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
    
    [recommendationsToggle setOn:[appDelegate isLocationManagerMonitoringRegions]];
    
    UIRefreshControl *refreshControl = [[UIRefreshControl alloc] init];
    refreshControl.attributedTitle = [[NSAttributedString alloc] initWithString:@"Pull to Refresh"];
    [refreshControl addTarget:self action:@selector(refreshRecommendations:)
             forControlEvents:UIControlEventValueChanged];
    [self.recommendationsTable addSubview:refreshControl];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)recommendationsToggleChanged:(UISwitch *)sender {
    WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
    if([sender isOn]){
        [appDelegate setupLocationManager];
    } else {
        [appDelegate disableLocationManager];
    }
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{  
    return 1;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [self.model recommedationsArray].count;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"PrevRecCell";
    
    WSPrevRecCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        
        NSArray* views = [[NSBundle mainBundle] loadNibNamed:@"WSPrevRecCell" owner:nil options:nil];
        
        for (UIView *view in views) {
            if([view isKindOfClass:[UITableViewCell class]])
            {
                cell = (WSPrevRecCell*)view;
            }
        }
    }
    
    
    NSDictionary *place = [[self.model recommedationsArray] objectAtIndex:indexPath.row];
    
    cell.title.text = place[@"name"];
    NSInteger recAge = [place[@"recommendation_age"] integerValue];
    NSString *recAgeString = [NSString stringWithFormat:@"%i days", recAge];
    
    if(recAge == 0){
        recAgeString = @"Today";
    } else if(recAge == 1){
        recAgeString = @"1 day";
    } else if(recAge == 7){
        recAgeString = @"1 week";
    } else if (recAge > 7){
        recAgeString = [NSString stringWithFormat:@"%d weeks", recAge/7];
    }
    
    cell.recAge.text = recAgeString;
    
    double recDist = [place[@"recommendation_distance"] doubleValue];
    NSString *recDistString = [NSString stringWithFormat:@"%dm", (int) recDist];
    if(recDist >= 1000){
            recDistString = [NSString stringWithFormat:@"%dkm", (int) recDist/1000];
    }    
    
    cell.recDistance.text = recDistString;

    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self performSegueWithIdentifier:@"ShowDetailRec" sender:indexPath];
}

- (IBAction)closeButtonPressed:(id)sender {
    [self.slidingViewController resetTopView];
}

- (void) refreshRecommendations:(UIRefreshControl *)refresh  {
    NSLog(@"refresh pulled");
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:@"Refreshing recommendations..."];
    
    // Refresh logic:
    MBProgressHUD *hud = [WSUtility createAndShowLoadingHudOnView:self.view withText:@"Loading" withDetailText:@"Recommendations"];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^(void) {
        
        WSAppDelegate *appDelegate = (WSAppDelegate *)[[UIApplication sharedApplication] delegate];
        CLLocation *location = [appDelegate locationManager].location;
        [self.model updateRecommendationListWithPositionLongitude:location.coordinate.longitude Latitude:location.coordinate.latitude];
        

        // Callback on the main queue to update UI.
        dispatch_async(dispatch_get_main_queue(), ^(void) {
            [self.recommendationsTable reloadData];
            [hud hide:YES];
        });
    });
        
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"MMM d, h:mm a"];
    NSString *lastUpdated = [NSString stringWithFormat:@"Last updated on %@", [formatter stringFromDate:[NSDate date]]];
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:lastUpdated];
    [refresh endRefreshing];
}
- (IBAction)clearListButtonPressed:(id)sender {
    NSLog(@"Clear Recommendations Button Pressed");
    
    // Refresh logic:
    MBProgressHUD *hud = [WSUtility createAndShowLoadingHudOnView:self.view withText:@"Clearing" withDetailText:@"Recommendations"];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^(void) {
        
        [self.model clearRecommendationsOnServerAndList];
        
        // Callback on the main queue to update UI.
        dispatch_async(dispatch_get_main_queue(), ^(void) {
            [self.recommendationsTable reloadData];
            [hud hide:YES];
        });
    });
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if([segue.identifier isEqualToString:@"ShowDetailRec"]){
        NSIndexPath *indexPath = sender;
        WSPlaceDetailViewController *detailController = segue.destinationViewController;
        
        NSDictionary *place = [[self.model recommedationsArray] objectAtIndex:indexPath.row];
        
        detailController.placeInquired = [WithinSitePlace initWithDictionary:place];
    }
    
    
}
@end
