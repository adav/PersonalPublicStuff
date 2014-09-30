//
//  WSPrevRecCell.m
//  WithinSite
//
//  Created by Andrew on 14/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSPrevRecCell.h"

@implementation WSPrevRecCell
@synthesize recDistance;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void)drawRect:(CGRect)rect
{
    [recDistance setTransform:CGAffineTransformMakeRotation(-M_PI / 2)];
    [super drawRect:rect];
}

@end
