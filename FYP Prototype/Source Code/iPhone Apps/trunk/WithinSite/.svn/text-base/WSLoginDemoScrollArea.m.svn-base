//
//  WSLoginDemoScrollArea.m
//  WithinSite
//
//  Created by Andrew on 16/03/2013.
//  Copyright (c) 2013 AndrewDavidson. All rights reserved.
//

#import "WSLoginDemoScrollArea.h"

@implementation WSLoginDemoScrollArea

@synthesize scrollView = _scrollView;

- (UIView*)hitTest:(CGPoint)point withEvent:(UIEvent*)event {
    UIView *view = [super hitTest:point withEvent:event];
    if (view == self) {
        return _scrollView;
    }
    return view;
}

@end
