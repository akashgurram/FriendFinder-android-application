# Friend Finder Application

This is an android app that will take location of your friends and would display the friends that are within a 1 mile radius of you. The app will display the friends (their names) and their distance from you (In miles) on a Map fragment
#### The app uses the following components:
##### 1. Firebase Authentication 
 - Firebase is a web service provided by Google for Mobile Application Development. 
 - The application starts with a login page. A user has to first sign up before logging in. The user details will be stored in the Firebase authentication server. 
##### 2. GPS
 - The location of user is obtained using the GPS in the device.
##### 3. Firebase Realtime Database
 - Once a user logs in, his location information along with his current time will be stored in the firebase realtime database.
 
All the registered users that are with in the 1 mile radius of the current user will be displayed on the map.

###### Note: It is assumed that everyone that is using this app is a friend

