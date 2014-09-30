//
//  WSFriendDetailCell.h
//  WithinSite
//
//  Created by Andrew on 17/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WSFriendDetailCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *profilePic;
@property (weak, nonatomic) IBOutlet UILabel *friendName;
@property (weak, nonatomic) IBOutlet UILabel *friendRelation;

@end
