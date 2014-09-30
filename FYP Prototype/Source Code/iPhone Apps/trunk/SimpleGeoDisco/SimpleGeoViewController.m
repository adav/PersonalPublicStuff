//
//  SimpleGeoViewController.m
//  SimpleGeoDisco
//
//  Created by Andrew on 23/10/2012.
//  Copyright (c) 2012 AndrewDavidson. All rights reserved.
//

#import "SimpleGeoViewController.h"
#import "SimpleGeoDetailViewController.h"
#import "SimpleGeoRecommendedDataItem.h"

@interface SimpleGeoViewController ()

@end

@implementation SimpleGeoViewController

@synthesize model = _model;
@synthesize tableView;

- (SimpleGeoModel *) model
{
    if(!_model){
        _model = [[SimpleGeoModel alloc] init];
    }
    return _model;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    	// Do any additional setup after loading the view, typically from a nib.
    
    
    if(self.locationManager == nil){
        self.locationManager = [[CLLocationManager alloc] init];
        self.locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
        self.locationManager.delegate = self;
    }
    [self.locationManager startUpdatingLocation];
    
    
    UIRefreshControl *refresh = [[UIRefreshControl alloc] init];
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:@"Pull to Refresh"];
    [refresh addTarget:self action:@selector(refreshView:) forControlEvents:UIControlEventValueChanged];
    [tableView addSubview:refresh]; //add pull down refresh action
        
}

-(void)refreshView:(UIRefreshControl *)refresh {
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:@"Refreshing recommendations..."];

    // Refresh logic:
    CLLocation *location = self.locationManager.location;
    [self.model populateRecommendedItems:location];
    [tableView reloadData];
    

    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"MMM d, h:mm a"];
    NSString *lastUpdated = [NSString stringWithFormat:@"Last updated on %@", [formatter stringFromDate:[NSDate date]]];
    refresh.attributedTitle = [[NSAttributedString alloc] initWithString:lastUpdated];
    [refresh endRefreshing];
}

//- (IBAction)goToTableView:(UIButton*)sender {
//    
//    
//    
//    CLLocation *currentPosition = self.locationManager.location;
//    
//    NSString *latitude = [[NSNumber numberWithDouble:currentPosition.coordinate.latitude] stringValue];
//    NSString *longitude = [[NSNumber numberWithDouble:currentPosition.coordinate.longitude] stringValue];
//    NSLog(@"Lat: %@", latitude);
//    NSLog(@"Long: %@", longitude);
//    //@"http://api.geonames.org/findNearbyWikipediaJSON?lat=%@&lng=%@&username=demo"
//    NSData *wikiData = [NSData dataWithContentsOfURL:
//                        [NSURL URLWithString:[NSString stringWithFormat:@"http://wwwteach.cs.bham.ac.uk/axd967/WebApplication1/NearbyFourSquareLocations?longitude=%@&latitude=%@", longitude, latitude]]
//                        ];
//    
//    NSDictionary *returnedJSON = nil;
//    if(wikiData){
//        returnedJSON = [NSJSONSerialization JSONObjectWithData:wikiData options:kNilOptions error:nil];
//    }
//    
//    //[self.boringLabel setText:[NSString stringWithFormat:@"Nearest place is %@", returnedJSON[@"places"][0][@"name"]]];
//    
//    //[tableData removeAllObjects];
//    //for (NSDictionary *place in returnedJSON[@"places"]) {
//    ///    [tableData addObject:place[@"name"]];
//    //}
//    
//    [self.tableView reloadData];
//    
//
//}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [[self.model recommendedItems] count];
}

- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"SimpleTableItem";
    
    UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if(cell == nil){
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:simpleTableIdentifier];
    }
    
    SimpleGeoRecommendedDataItem *recomendedItem = [[self.model recommendedItems] objectAtIndex:indexPath.row];
    
    cell.textLabel.text = recomendedItem.name;
    @try {
        cell.detailTextLabel.text = recomendedItem.foursquareCategories[0];
    } @catch (NSException *exception){
        NSLog(@"Reason: %@", exception.reason);
    }
    
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    //TEST::: cell.textLabel.text = [tableData objectAtIndex:indexPath.row];
    return cell;
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    NSIndexPath *indexPath = sender;
    if ([segue.identifier isEqualToString:@"showDetail"]) {
        SimpleGeoDetailViewController *destViewController = segue.destinationViewController;
        destViewController.placeOfInterest = [[self.model recommendedItems] objectAtIndex:indexPath.row];
        destViewController.currentLocation = [self.locationManager location];
    }
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [self performSegueWithIdentifier:@"showDetail" sender:indexPath];
    
}

- (void) locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations {
    //NSLog(@"Location found");
}

- (void) locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    NSLog(@"Can't get fix");
}

@end
