package com.example.friendfinderakash;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoggedInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonOpenMap, buttonSignOut;
    private TextView textViewDisplayName;
    private FirebaseAuth mAuth;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    //Introduced this in case Loaction services are disabled
    private LocationManager mylocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        buttonOpenMap = (Button) findViewById(R.id.buttonOpenMap);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        textViewDisplayName = (TextView) findViewById(R.id.textViewDisplayName);
        buttonOpenMap.setOnClickListener(this);
        buttonSignOut.setOnClickListener(this);

        final Context context = this;
        mylocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //If the location services are not being accessed, it gives an alert with open settings option
        if(!(mylocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))){
            new AlertDialog.Builder(context)
                    .setMessage("Location Services Disabled")
                    .setPositiveButton("Open Location Services", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();

        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }

        //only if display name is null, we are passing data between intents
        if(mAuth.getCurrentUser().getDisplayName() == null)
        {
            Intent i = new Intent();
            String DisplayName =  getIntent().getExtras().getString("Name");
            textViewDisplayName.setText("Welcome" + " " +DisplayName+"!");
        }
        else {
            textViewDisplayName.setText("Welcome" + " "+mAuth.getCurrentUser().getDisplayName()+"!");
        }



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonOpenMap:
                startActivity(new Intent(this, MapsActivity.class));
                break;

            //User Signout
            case R.id.buttonSignOut:
                mAuth.signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;

        }

    }
}
