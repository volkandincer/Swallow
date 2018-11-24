package com.example.ahmet.swallow;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };
    Button buton1;
    TextView enlem, boylam;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase db =  FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Kullanici");
        ref.child("ahmet").child("Ad").setValue("Ahmet");
        ref.child("ahmet").child("Soyad").setValue("Tuztaşı");

        mTextMessage =  findViewById(R.id.message);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //*****************
        buton1 = findViewById(R.id.button);

        enlem =  findViewById(R.id.textView4);
        enlem.setText("ahmet");

        boylam = (TextView) findViewById(R.id.textView5);
        boylam.setText("tuztasi");

        //client = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();

        buton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                LocationListener mLocListener = new MyLocationListener();
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
                }


                /*if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            enlem.setText(location.toString());
                            boylam.setText("location isn't null");
                        }
                    }
                });*/
            }
        });


        //******************
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    class MyLocationListener implements LocationListener{

        public void onLocationChanged(Location loc) {
            String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    loc.getLongitude(), loc.getLatitude()
            );
            enlem.setText(message);
        }
        public void onProviderDisabled(String arg0) {

        }
        public void onProviderEnabled(String provider) {

        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}

/*----------Listener class to get coordinates ------------- */
