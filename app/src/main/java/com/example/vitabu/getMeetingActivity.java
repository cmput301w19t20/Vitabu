package com.example.vitabu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class getMeetingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private BorrowRecord borrowRecord;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_meeting);


        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.BORROWRECORD_MESSAGE);
        Gson gson = new Gson();
        borrowRecord = gson.fromJson(message, BorrowRecord.class);

        TextView time = (TextView) findViewById(R.id.get_meeting_time);
        String temp = "Time: ";
        temp += (borrowRecord.getDateBorrowed().getHours() < 10) ? ("0" + Integer.toString(borrowRecord.getDateBorrowed().getHours()) + ":") : (Integer.toString(borrowRecord.getDateBorrowed().getHours()) + ":");
        temp += (borrowRecord.getDateBorrowed().getMinutes() < 10) ? ("0" + Integer.toString(borrowRecord.getDateBorrowed().getMinutes())) : (Integer.toString(borrowRecord.getDateBorrowed().getMinutes())) ;
        time.setText(temp);

        TextView date = (TextView) findViewById(R.id.get_meeting_date);
        temp = "Date: " + Integer.toString(borrowRecord.getDateBorrowed().getYear()) + "-" +
                Integer.toString(borrowRecord.getDateBorrowed().getMonth() + 1) + "-" +
                Integer.toString(borrowRecord.getDateBorrowed().getDay());
        date.setText(temp);

        TextView ownerEmail = (TextView) findViewById(R.id.get_meeting_owner_email_email);
        ownerEmail.setText(borrowRecord.getOwnerEmail());

        TextView ownerPhone = (TextView) findViewById(R.id.get_meeting_owner_phone_phone);
        ownerPhone.setText(borrowRecord.getOwnerPhoneNumber());

        TextView ownerNotes = (TextView) findViewById(R.id.get_meeting_owner_notes_notes);
        ownerNotes.setText(borrowRecord.getOwnerNotes());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.get_meeting_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(borrowRecord.getPickUpLocation().getLat(), borrowRecord.getPickUpLocation().getLng()), 12.0f));
        mMap.addMarker(new MarkerOptions().position(new LatLng(borrowRecord.getPickUpLocation().getLat(), borrowRecord.getPickUpLocation().getLng())).title("Meeting Location"));

    }
}
