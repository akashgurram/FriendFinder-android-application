package com.example.friendfinderakash;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager mylocationManager;
    private Handler myHandler;
    private NewLatLng currLoc;
    private LatLng googleCurrLoc;
    private double latitude, longitude;
    private boolean zoom = true;
    private double latitude_, longitude_;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    DatabaseReference userref;
    private FirebaseAuth mAuth;
    private List<Users> userlist_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }

        myHandler = new Handler();
        mylocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        userref = FirebaseDatabase.getInstance().getReference("Users");
        //Initialize mAuth
        mAuth = FirebaseAuth.getInstance();
        userlist_ = new ArrayList<>();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //Requesting location updates every time the user moves 100 meters
        mylocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, this);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(userlist_!=null){
                    userlist_.clear();
                }

                for(DataSnapshot user:dataSnapshot.getChildren()){
                    userlist_.add(user.getValue(Users.class)); //returns an object of type users rather than a generic object

                }

                for(int i=0; i<userlist_.size();i++){

                    NewLatLng someCurrLoc = userlist_.get(i).getLocation_();
                    LatLng anotherCurrLoc = new LatLng(someCurrLoc.getLatitude(),someCurrLoc.getLongitude());
                    float[] results = new float[1];

                if(googleCurrLoc!=null) {
                    Location.distanceBetween(googleCurrLoc.latitude, googleCurrLoc.longitude,
                            anotherCurrLoc.latitude, anotherCurrLoc.longitude,
                            results);

                    float x = results[0];
                    if(x<1609.34) { //Since 1 Mile is 1609.3 Meters
                        mMap.addMarker(new MarkerOptions().position(anotherCurrLoc).title(userlist_.get(i).getName_() + " " + String.format("%.3f", x/1609) +" Miles Away"));
                    }
                }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mylocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, this);

                } else {
                    Toast.makeText(getApplicationContext(), "Location permission needed for the application to work", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }




    private class LocationWork implements Runnable {



        public LocationWork(double lat_, double long_) {
            latitude_ = lat_;
            longitude_ = long_;
        }

        @Override
        public void run() {
            googleCurrLoc = new LatLng(latitude_, longitude_);
            currLoc = new NewLatLng(latitude_, longitude_);

            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String userDisplayName = mAuth.getCurrentUser().getDisplayName();
            Users users = new Users(userDisplayName,currLoc,ts.toString());

            //Retrieve the userID
            String userID = mAuth.getCurrentUser().getUid();
            userref.child(userID).setValue(users);

            mMap.clear();
            //mMap.setMyLocationEnabled(true);

            mMap.addMarker(new MarkerOptions().position(googleCurrLoc).title("My Location "+"("+latitude_ +","  +longitude_+ ")")).showInfoWindow();

            mMap.addCircle(new CircleOptions().center(googleCurrLoc).radius(1609.34).strokeWidth(3f).strokeColor(Color.TRANSPARENT)
                    .fillColor(Color.argb(70,50,150,50)));
            if(zoom) {
                float zoomLevel = 13.75f; //This goes up to 21
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleCurrLoc, zoomLevel));
                zoom = false;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LocationWork myWork = new LocationWork(latitude, longitude);
        myHandler.post(myWork);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onStop() {
        mylocationManager.removeUpdates(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mylocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, this);
        super.onResume();
    }
}
