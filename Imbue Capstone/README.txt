README
Within the Android src are the java files of the Android application. The MainActivity.java is the launcher of the project and contains the mapview code. The CameraActivity.java contains the augmented reality code. The other classes range from providing encapsulation to act as controllers to views. 
Within the Android layouts folder, the points of interest are in the camera_layout, building_info_activity, and the main xml files. They are not too interesting but are important to the project and displaying information.
The Android Manifest defines what permissions the application is requesting and permissions for each activity. It also determines which Activity is started up on launch.  The mainifest specifies the min sdk version and the target sdk this app is aimed at.

==

The iOS version of Imbue is contained in the folder “Campus Tours.” This folder contains the entire Xcode project and all of its resources. The source code is contained in the subfolder, “Campus Tours.”

The key source files are:

BRBearing.h
BRBearing.m

BRContentViewController.h
BRContentViewController.m

BRViewController.h
BRViewController.m

Registration of Parse and Google Maps API keys takes place in the file “BRAppDelegate.m;” the remainder of the file is auto-generated.

Some Facebook setup data (specifically the URL for FQL requests) appears in “Campus Tours-Info.plist.”

Storyboards are in the folder: “Base.lproj.”
