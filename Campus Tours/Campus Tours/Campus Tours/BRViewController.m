//
//  BRViewController.m
//  Campus Tours
//
//  Created by Billy Rathje on 2/16/14.
//  Copyright (c) 2014 Billy Rathje. All rights reserved.
//

#import "BRViewController.h"
<<<<<<< HEAD
=======

#define LOCATIONS 20
>>>>>>> 127eec836715416d4554a56fc0e7567743a67a2c

@interface BRViewController ()
{

}

@end

@implementation BRViewController
<<<<<<< HEAD
=======
{
    CLLocationCoordinate2D theLocations[LOCATIONS];
    

}
>>>>>>> 127eec836715416d4554a56fc0e7567743a67a2c

- (void)viewDidLoad
{
    [super viewDidLoad];
<<<<<<< HEAD
	// Do any additional setup after loading the view, typically from a nib.
=======
    
    
    
    CLLocationCoordinate2D pugetsound = CLLocationCoordinate2DMake(47.2626, -122.4817);
    CLLocationCoordinate2D sub = CLLocationCoordinate2DMake(47.263144, -122.478944);
    CLLocationCoordinate2D jones = CLLocationCoordinate2DMake(47.263635, -122.481138);
    CLLocationCoordinate2D mcintyre = CLLocationCoordinate2DMake(47.264021, -122.480403);
    CLLocationCoordinate2D howarth = CLLocationCoordinate2DMake(47.263257, -122.480435);
    CLLocationCoordinate2D music = CLLocationCoordinate2DMake(47.263631, -122.48235);
    CLLocationCoordinate2D thompson = CLLocationCoordinate2DMake(47.263668, -122.482882);
    CLLocationCoordinate2D harned = CLLocationCoordinate2DMake(47.263668, -122.483466);
    CLLocationCoordinate2D collins = CLLocationCoordinate2DMake(47.264389, -122.481723);
    CLLocationCoordinate2D wyatt = CLLocationCoordinate2DMake(47.261859, -122.482608);
    CLLocationCoordinate2D pool = CLLocationCoordinate2DMake(47.261276, -122.481685);
    CLLocationCoordinate2D weyerhaseuser = CLLocationCoordinate2DMake(47.260482, -122.480537);
    CLLocationCoordinate2D todd = CLLocationCoordinate2DMake(47.262404, -122.480832);
    CLLocationCoordinate2D regester = CLLocationCoordinate2DMake(47.261938, -122.480628);
    CLLocationCoordinate2D seward = CLLocationCoordinate2DMake(47.262004, -122.480081);
    CLLocationCoordinate2D trimble = CLLocationCoordinate2DMake(47.26279, -122.480392);
    CLLocationCoordinate2D kilworth = CLLocationCoordinate2DMake(47.26512, -122.481744);
    CLLocationCoordinate2D al = CLLocationCoordinate2DMake(47.264843, -122.480779);
    CLLocationCoordinate2D schiff = CLLocationCoordinate2DMake(47.265246, -122.480095);
    CLLocationCoordinate2D kittredge = CLLocationCoordinate2DMake(47.263905, -122.478966);
    
    
    // Dumb way of initializing locations, but can't think of anything better at the moment.
    CLLocationCoordinate2D initialize[] = {pugetsound, sub, jones, mcintyre, howarth, music, thompson, harned, collins,
        wyatt, pool, weyerhaseuser, todd, regester, seward, trimble, kilworth, al, schiff, kittredge};
    
    for(int i = 0; i < LOCATIONS; i++)
    {
        theLocations[i] = initialize[i];
    }
    
    NSArray *names = @[@"pugetsound", @"sub", @"jones", @"mcintyre", @"howarth", @"music", @"thompson", @"harned", @"collins", @"wyatt", @"pool", @"weyerhauser", @"todd", @"regester", @"seward", @"trimble", @"kilworth", @"al", @"schiff", @"kittredge"];


    
    
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithTarget: pugetsound zoom: 15];
    _mapview = [GMSMapView mapWithFrame: CGRectZero camera:camera];
    _mapview.myLocationEnabled = YES;
    self.view = _mapview;
    
    // Add markers
    
    _arrayOfMarkers = [[NSMutableArray alloc] init]; // Array of all markers
    
    GMSMarker *marker;
    for(int i = 0; i < names.count; i++)
    {
        marker = [[GMSMarker alloc] init];
        marker.position = theLocations[i];
        marker.title = names[i];
        marker.snippet = names[i];
        marker.map = _mapview;
        
        [_arrayOfMarkers addObject: marker];
    }
    
    _currentLocation = _mapview.myLocation;
    
    _nearby = [[NSMutableArray alloc] init];

    
    [self startStandardUpdates];
    
>>>>>>> 127eec836715416d4554a56fc0e7567743a67a2c
}

- (void)startStandardUpdates
{
    // Create the location manager if this object does not
    // already have one.

    _locationManager = [[CLLocationManager alloc] init];
    
    _locationManager.delegate = self;
    _locationManager.desiredAccuracy = kCLLocationAccuracyKilometer;
    
    // Set a movement threshold for new events.
    _locationManager.distanceFilter = 100; // meters
    
    [_locationManager startUpdatingLocation];
}

- (void) locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    _currentLocation = locations[0];
    
    if(_nearby == nil)
        _nearby = [[NSMutableArray alloc] init];
    
    [_nearby removeAllObjects];
    
    for(GMSMarker *m in _arrayOfMarkers)
    {
        if(ABS(m.position.latitude - _currentLocation.coordinate.latitude) < .001
           && ABS(m.position.longitude - _currentLocation.coordinate.longitude) < .001)
        {
            [_nearby addObject: m];
            _label.text = m.title;
        }
    }
            [_mapview addSubview: _label];
    
}

- (void) locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading
{
    _currentHeading = newHeading;
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
