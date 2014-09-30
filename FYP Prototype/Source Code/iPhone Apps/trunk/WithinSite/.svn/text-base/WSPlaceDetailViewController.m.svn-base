//
//  WSPlacesListViewController.m
//  WithinSite
//
//  Created by Andrew on 02/02/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSPlaceDetailViewController.h"
#import "WSScollableTextBoxCell.h"
#import "WSFriendDetailCell.h"
#import "WSSplashBubbleMiniViewController.h"
#import <MapKit/MapKit.h>
#import "WSSimpleWebViewController.h"
#import <Social/Social.h>

@interface WSPlaceDetailViewController ()
@property (nonatomic, strong) NSMutableDictionary *detailsSectionsArrays;
- (NSArray *)sectionKeys;
@end

@implementation WSPlaceDetailViewController
@synthesize tableView;
@synthesize splashContainer;
@synthesize placeInquired;

@synthesize detailsSectionsArrays = _detailsSectionsArrays;

- (NSArray *)sectionKeys
{
    return [NSArray arrayWithObjects:
            @"Description",
            @"Connections",
            @"Links",
            @"Routing",
            @"Social", nil];
}
/*
 - (NSMutableDictionary *)detailsSectionArrays
 {
 if (!_detailsSectionsArrays) {
 _detailsSectionsArrays = [[NSMutableDictionary alloc] initWithCapacity:[self.sectionKeys count]];
 }
 return _detailsSectionsArrays;
 }
 
 -(void)setDetailsSectionsArrays:(NSMutableDictionary *)detailsSectionsArrays{
 _detailsSectionsArrays = detailsSectionsArrays;
 }*/

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated
{
    self.navigationController.navigationBar.hidden = NO;
    CGRect frame = self.navigationController.navigationBar.frame;
    frame.origin.y = 20;
    self.navigationController.navigationBar.frame = frame;
    //[self.navigationController.navigationBar setBounds:CGRectMake(0, 20, 320, 44)];
    
    for (UIViewController *childViewController in [self childViewControllers])
    {
        if ([childViewController isKindOfClass:[WSSplashBubbleMiniViewController class]])
        {
            WSSplashBubbleMiniViewController *bubble = (WSSplashBubbleMiniViewController *)childViewController;
            
            [bubble setupBubblewithWithinSitePlace:self.placeInquired];
            
            break;
        }
    }
    
    CLLocationCoordinate2D centerMapView = [self.placeInquired getPlaceLocation].coordinate;
    
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(centerMapView,
                                                                   400,
                                                                   400);
    [self.backgroundMap setRegion:region animated:YES];
    
    self.detailsSectionsArrays = [[NSMutableDictionary alloc] initWithCapacity:[self.sectionKeys count]];
    
    if ([placeInquired getPlaceDescription]) {
        if([placeInquired getPlaceDescription].length > 1){
            [self.detailsSectionsArrays setValue:[NSArray arrayWithObject:[placeInquired getPlaceDescription]]
                                          forKey:@"Description"];
        }
    }
    
    if ([placeInquired getPlaceSocialNameDrops]){
        [self.detailsSectionsArrays setValue:[placeInquired getPlaceSocialNameDrops]
                                      forKey:@"Connections"];
    }
    
    if([placeInquired getPlaceLinks]){
        [self.detailsSectionsArrays setValue:[placeInquired getPlaceLinks]
                                      forKey:@"Links"];
    }
    
    [self.detailsSectionsArrays setValue:[NSArray arrayWithObject:@"Get Directions with Maps"]
                                  forKey:@"Routing"];
    
    [self.detailsSectionsArrays setValue:[NSArray arrayWithObjects:@"Share on Facebook", @"Share on Twitter", nil]
                                  forKey:@"Social"];
    
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSArray *sectionArray = [self.detailsSectionsArrays objectForKey:[self.sectionKeys objectAtIndex:section]];
    return sectionArray.count;
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [self.sectionKeys count];
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return [self.sectionKeys objectAtIndex:section];
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([[self.sectionKeys objectAtIndex:indexPath.section] isEqual: @"Description"]) {
        WSScollableTextBoxCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ScollableTextBox"];
        
        NSArray *itemsInSection = [self.detailsSectionsArrays objectForKey:[self.sectionKeys objectAtIndex:indexPath.section]];
        
        cell.scrollText.text = [NSString stringWithFormat:@"%@", [itemsInSection objectAtIndex:indexPath.row]];
        
        return cell;
    } else if ([[self.sectionKeys objectAtIndex:indexPath.section] isEqual: @"Connections"]){
        WSFriendDetailCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FriendDetail"];
        
        NSArray *itemsInSection = [self.detailsSectionsArrays objectForKey:[self.sectionKeys objectAtIndex:indexPath.section]];
        
        NSDictionary *friend = [itemsInSection objectAtIndex:indexPath.row];
        
        cell.friendName.text = [NSString stringWithFormat:@"%@", friend[@"name"]];
        cell.friendRelation.text = [NSString stringWithFormat:@"%@ %@", friend[@"service"], friend[@"relation"]];
        cell.profilePic.image = [UIImage imageWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:
                                                                                      friend[@"profile_picture"]]]];
        
        return cell;
    }
    
    static NSString *simpleTableIdentifier = @"BasicRightDetail";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    
    NSArray *itemsInSection = [self.detailsSectionsArrays objectForKey:[self.sectionKeys objectAtIndex:indexPath.section]];
    
    if ([[self.sectionKeys objectAtIndex:indexPath.section] isEqual: @"Links"]) {
        
        NSArray *itemsInSection = [self.detailsSectionsArrays objectForKey:[self.sectionKeys objectAtIndex:indexPath.section]];
        
        NSDictionary *link = [itemsInSection objectAtIndex:indexPath.row];
        
        
        if ([[NSString stringWithFormat:@"%@", link[@"name"]] isEqual:@"url"]){
            cell.textLabel.text = @"Website";
            cell.detailTextLabel.text = [NSString stringWithFormat:@"%@", link[@"meta"]];
        } else if ([[NSString stringWithFormat:@"%@", link[@"name"]] isEqual:@"foursquare"]){
            cell.textLabel.text = @"Foursquare";
            cell.detailTextLabel.text = @"";
        } else if ([[NSString stringWithFormat:@"%@", link[@"name"]] isEqual:@"facebook"]){
            cell.textLabel.text = @"Offical Facebook Page";
            cell.detailTextLabel.text = @"";
        } else if ([[NSString stringWithFormat:@"%@", link[@"name"]] isEqual:@"twitter"]){
            cell.textLabel.text = @"Offical Twitter";
            cell.detailTextLabel.text = [NSString stringWithFormat:@"@%@", link[@"meta"]];
        } else {
            cell.textLabel.text = [NSString stringWithFormat:@"%@", link[@"name"]];
            cell.detailTextLabel.text = [NSString stringWithFormat:@"%@", link[@"meta"]];
        }
        
    } else {
        
        cell.textLabel.text = [NSString stringWithFormat:@"%@", [itemsInSection objectAtIndex:indexPath.row]];
        cell.detailTextLabel.text = @"";
    }
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([[self.sectionKeys objectAtIndex:indexPath.section] isEqual: @"Connections"]) {
        
    } else if ([[self.sectionKeys objectAtIndex:indexPath.section] isEqual: @"Links"]) {
        
        
        NSArray *itemsInSection = [self.detailsSectionsArrays objectForKey:[self.sectionKeys objectAtIndex:indexPath.section]];
        
        NSDictionary *link = [itemsInSection objectAtIndex:indexPath.row];
        
        
        if ([[NSString stringWithFormat:@"%@", link[@"name"]] isEqual:@"url"]){
            [self performSegueWithIdentifier:@"ShowWeb" sender:link[@"meta"]];
        } else if ([[NSString stringWithFormat:@"%@", link[@"name"]] isEqual:@"foursquare"]){
            [self performSegueWithIdentifier:@"ShowWeb" sender:link[@"meta"]];
        } else if ([[NSString stringWithFormat:@"%@", link[@"name"]] isEqual:@"facebook"]){
            [self performSegueWithIdentifier:@"ShowWeb" sender:link[@"meta"]];
        } else if ([[NSString stringWithFormat:@"%@", link[@"name"]] isEqual:@"twitter"]){
            [self performSegueWithIdentifier:@"ShowWeb" sender:[NSString stringWithFormat:@"http://twitter.com/%@",link[@"meta"]]];
        }
        
    } else if ([[self.sectionKeys objectAtIndex:indexPath.section] isEqual: @"Routing"]) {
        //Based on an answer from: http://stackoverflow.com/questions/576768/how-to-invoke-iphone-maps-for-directions-with-current-location-as-start-address
        
        MKPlacemark* place = [[MKPlacemark alloc] initWithCoordinate: placeInquired.getPlaceLocation.coordinate addressDictionary: nil];
        MKMapItem* destination = [[MKMapItem alloc] initWithPlacemark: place];
        destination.name = self.placeInquired.getPlaceName;
        NSArray* items = [[NSArray alloc] initWithObjects: destination, nil];
        NSDictionary* options = [[NSDictionary alloc] initWithObjectsAndKeys:
                                 MKLaunchOptionsDirectionsModeDriving,
                                 MKLaunchOptionsDirectionsModeKey, nil];
        [MKMapItem openMapsWithItems: items launchOptions: options];
        
    } else if ([[self.sectionKeys objectAtIndex:indexPath.section] isEqual: @"Social"]) {
        if (indexPath.row == 0) {//facebook
            
            
            //based on tutorial: http://soulwithmobiletechnology.blogspot.co.uk/2012/07/tutorial-how-to-use-inbuilt.html
            
            SLComposeViewController *fbController=[SLComposeViewController composeViewControllerForServiceType:SLServiceTypeFacebook];
            
            
            if([SLComposeViewController isAvailableForServiceType:SLServiceTypeFacebook])
            {
                SLComposeViewControllerCompletionHandler __block completionHandler=^(SLComposeViewControllerResult result){
                    
                    [fbController dismissViewControllerAnimated:YES completion:nil];
                    
                    switch(result){
                        case SLComposeViewControllerResultCancelled:
                        default:
                        {
                            NSLog(@"Cancelled.....");
                            
                        }
                            break;
                        case SLComposeViewControllerResultDone:
                        {
                            NSLog(@"Posted....");
                        }
                            break;
                    }};
                
                [fbController setInitialText:[NSString stringWithFormat:@"Check out this cool %@, %@ :)", placeInquired.getPlaceCategories[0], placeInquired.getPlaceName]];
                [fbController addURL:[NSURL URLWithString:self.placeInquired.getPlaceLinks[0][@"meta"]]];
                [fbController setCompletionHandler:completionHandler];
                [self presentViewController:fbController animated:YES completion:nil];
            }
        } else {
            SLComposeViewController *twitter=[SLComposeViewController composeViewControllerForServiceType:SLServiceTypeTwitter];
            
            
            if([SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter])
            {
                SLComposeViewControllerCompletionHandler __block completionHandler=^(SLComposeViewControllerResult result){
                    
                    [twitter dismissViewControllerAnimated:YES completion:nil];
                    
                    switch(result){
                        case SLComposeViewControllerResultCancelled:
                        default:
                        {
                            NSLog(@"Cancelled.....");
                            
                        }
                            break;
                        case SLComposeViewControllerResultDone:
                        {
                            NSLog(@"Posted....");
                        }
                            break;
                    }};
                
                [twitter setInitialText:[NSString stringWithFormat:@"Check out this cool %@, %@ :)", placeInquired.getPlaceCategories[0], placeInquired.getPlaceName]];
                [twitter addURL:[NSURL URLWithString:self.placeInquired.getPlaceLinks[0][@"meta"]]];
                [twitter setCompletionHandler:completionHandler];
                [self presentViewController:twitter animated:YES completion:nil];
            }
        }
    }
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if([segue.identifier isEqualToString:@"ShowWeb"]){
        NSString *url = sender;
        WSSimpleWebViewController *detailController = segue.destinationViewController;
        detailController.url = [NSURL URLWithString:url];
        
    }
    
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([[self.sectionKeys objectAtIndex:indexPath.section] isEqual: @"Description"]) {
        return 120.0;
    } else if ([[self.sectionKeys objectAtIndex:indexPath.section] isEqual: @"Connections"]){
        return 88.0;
    } else {
        return 44.0;
    }
}

@end
