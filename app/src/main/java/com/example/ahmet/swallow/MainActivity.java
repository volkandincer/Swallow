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

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

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
    //private FusedLocationProviderClient client;

    private void requestPermission(){ // konumu kullanabilmek için izin alır.
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseDatabase db =  FirebaseDatabase.getInstance();
        final DatabaseReference ref = db.getReference().child("Gps");

        mTextMessage =  findViewById(R.id.message);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //*****************
        buton1 = findViewById(R.id.button);

        final TextView enlem =  findViewById(R.id.textView4);
        final TextView boylam = (TextView) findViewById(R.id.textView5);

        Button konumCek = (Button) findViewById(R.id.konumCek);
        konumCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    final MyLocationListener locListener = new MyLocationListener();
                    mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
                }
            }
        });


        buton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText sehirCek = (EditText) findViewById(R.id.sehirTxt);
                final String sehir = sehirCek.getText().toString();
                ref.child(sehir).child("Longitude").setValue(boylam.getText());
                ref.child(sehir).child("Latitude").setValue(enlem.getText());
            }

        });
    }

    class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location loc) {

            String tempEnlem,tempBoylam;
            tempEnlem = Double.toString(loc.getLatitude());
            tempBoylam = Double.toString(loc.getLongitude());

            TextView enlem =  MainActivity.this.findViewById(R.id.textView4);
            TextView boylam = (TextView) MainActivity.this.findViewById(R.id.textView5);
            enlem.setText(tempEnlem); //veri tabanından aldığı latitude'u ekrana yazdırır
            boylam.setText(tempBoylam); //veri tabanından aldığı longitude'u ekrana yazdırır
        }
        public void onProviderDisabled(String arg0) {

        }
        public void onProviderEnabled(String provider) {

        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}

