//
//  SimpleGeoDetailViewController.m
//  SimpleGeoDisco
//
//  Created by Andrew on 08/11/2012.
//  Copyright (c) 2012 AndrewDavidson. All rights reserved.
//

#import "SimpleGeoDetailViewController.h"

@interface SimpleGeoDetailViewController ()
@property (nonatomic, strong) NSMutableDictionary *detailsSectionsArrays;
@end

@implementation SimpleGeoDetailViewController
@synthesize detailsSectionsArrays = _detailsSectionsArrays;
@synthesize placeOfInterest;
@synthesize detailsTableView;
@synthesize currentLocation;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (NSMutableDictionary *) detailsSectionsArrays
{
    if(!_detailsSectionsArrays){
        _detailsSectionsArrays = [[NSMutableDictionary alloc] init];
    }
    return _detailsSectionsArrays;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
	//setup Dictionary to show:
    if ([self.detailsSectionsArrays count] > 0) {
        [self.detailsSectionsArrays removeAllObjects];
    }
    NSMutableArray *sections = [[NSMutableArray alloc] init];
    [sections addObject:@"Name"];
    [sections addObject:@"Location"];
    [sections addObject:@"Description"];
    [sections addObject:@"Recommendation"];
    [sections addObject:@"Categories"];
    [sections addObject:@"Foursquare Metadata"];
    //[self.detailsSectionsArrays setObject:sections forKey:@"sections"];
    
    NSMutableArray *nameSection = [[NSMutableArray alloc] init];
    [nameSection addObject:placeOfInterest.name];
    [self.detailsSectionsArrays setObject:nameSection forKey:@"Name"];
    
    NSMutableArray *locationSection = [[NSMutableArray alloc] init];
    CLLocationDistance distanceBetweenMeAndPoint = [currentLocation distanceFromLocation:placeOfInterest.location];
    
    [locationSection addObject:[NSString stringWithFormat:@"%fm away", distanceBetweenMeAndPoint]];
    [self.detailsSectionsArrays setObject:locationSection forKey:@"Location"];
    
    NSMutableArray *descSection = [[NSMutableArray alloc] init];
    [descSection addObject:@"Description here..."];
    [descSection addObject:[NSString stringWithFormat:@"Twitter @%@", placeOfInterest.twitterHandle]];
    [self.detailsSectionsArrays setObject:descSection forKey:@"Description"];
    
    NSMutableArray *recommendSection = [[NSMutableArray alloc] init];
    [recommendSection addObject:[NSString stringWithFormat:@"%d recommendation score", placeOfInterest.recommendationScore]];
    [self.detailsSectionsArrays setObject:recommendSection forKey:@"Recommendation"];
    
    NSMutableArray *catsSection = [[NSMutableArray alloc] init];
    for(NSString *cat in placeOfInterest.foursquareCategories){
        [catsSection addObject:cat];
    }
    [self.detailsSectionsArrays setObject:catsSection forKey:@"Categories"];
    
    NSMutableArray *fsSection = [[NSMutableArray alloc] init];
    if(placeOfInterest.twitterHandle){
        [fsSection addObject:[NSString stringWithFormat:@"Twitter @%@", placeOfInterest.twitterHandle]];
    }
    [self.detailsSectionsArrays setObject:fsSection forKey:@"Foursquare Metadata"];

    
    [self.detailsTableView reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)donePressed:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSArray *sectionArray = [[self.detailsSectionsArrays allValues] objectAtIndex:section];
    return [sectionArray count];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return [[self.detailsSectionsArrays allKeys] count];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    
    return [[self.detailsSectionsArrays allKeys] objectAtIndex:section];
}

- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"SimpleTableItem";
    
    UITableViewCell *cell = [self.detailsTableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if(cell == nil){
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:simpleTableIdentifier];
    }
    
    NSArray *sectionArray = [[self.detailsSectionsArrays allValues] objectAtIndex:indexPath.section];
    
    cell.textLabel.text = [sectionArray objectAtIndex:indexPath.row];
    /*
    cell.textLabel.text = recomendedItem.name;
    cell.detailTextLabel.text = recomendedItem.foursquareCategories[0];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    //TEST::: cell.textLabel.text = [tableData objectAtIndex:indexPath.row];
     */
    
    return cell;
}

@end
