//
//  BRViewController.m
//  Campus Tours
//
//  Created by Billy Rathje on 2/16/14.
//  Copyright (c) 2014 Billy Rathje. All rights reserved.
//

#import "BRViewController.h"
#import <GoogleMaps/GoogleMaps.h>

@interface BRViewController ()

@end

@implementation BRViewController
{
    GMSMapView *mapView_;
    
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // Create a GMSCameraPosition that tells the map to display the
    // coordinate -33.86,151.20 at zoom level 6.
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:-33.86
                                                            longitude:151.20
                                                                 zoom:6];
    mapView_ = [GMSMapView mapWithFrame:CGRectZero camera:camera];
    mapView_.myLocationEnabled = YES;
    self.view = mapView_;
    
    // Creates a marker in the center of the map.
    GMSMarker *marker = [[GMSMarker alloc] init];
    marker.position = CLLocationCoordinate2DMake(-33.86, 151.20);
    marker.title = @"Sydney";
    marker.snippet = @"Australia";
    marker.map = mapView_;
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
