//
//  BRViewController.h
//  Campus Tours
//
//  Created by Billy Rathje on 2/16/14.
//  Copyright (c) 2014 Billy Rathje. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <GoogleMaps/GoogleMaps.h>

@interface BRViewController : UIViewController<CLLocationManagerDelegate>

@property CLLocationManager *locationManager;
@property CLLocation *currentLocation;
@property CLHeading *currentHeading;
@property NSMutableArray *arrayOfMarkers;
@property NSMutableArray *nearby;
@property (strong, nonatomic) IBOutlet UILabel *label;
@property (strong, nonatomic) IBOutlet GMSMapView *mapview;
@property (strong, nonatomic) IBOutlet UIView *view;

@end
