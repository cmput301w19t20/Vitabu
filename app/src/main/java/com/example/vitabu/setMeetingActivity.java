/*
Vitabu is an Open Source application available under the Apache (Version 2.0) License.

Copyright 2019 Arseniy Kouzmenkov, Owen Randall, Ayooluwa Oladosu, Tristan Carlson, Jacob Paton,
Katherine Richards

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * This file contains the activity that sets the meeting between the owner and the borrower.
 *
 * Author: Tristan Carlson
 * Version: 1.0
 * Outstanding Issues: This file has no database connectivity yet.
 */
package com.example.vitabu;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Date;

public class setMeetingActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnMapReadyCallback {
    BorrowRecord borrowRecord;
    Date date = new Date();
    boolean timeset = false, dateset = false;
    String email, phone, notes;
    Button timeButton, dateButton;
    String logTag = "Set Meeting Activity";
    FirebaseAuth auth;
    private GoogleMap mMap;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 100;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_set_meeting);

        // Get Intent.
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.BORROWRECORD_MESSAGE);
        Gson gson = new Gson();
        borrowRecord = gson.fromJson(message, BorrowRecord.class);

        timeButton = (Button) findViewById(R.id.set_meeting_set_time_button);
        dateButton = (Button) findViewById(R.id.set_meeting_set_date_button);

        MapFragment mapFragment = (MapFragment) getFragmentManager() .findFragmentById(R.id.set_meeting_map);
        mapFragment.getMapAsync(this);
    }



    /**
     * Checks User's entered data is valid and submits.
     */
    public void continuePressed(View v){

        email = ((EditText) findViewById(R.id.set_meeting_email_edittext)).getText().toString();
        phone = ((EditText) findViewById(R.id.set_meeting_phone_edittext)).getText().toString();
        notes = ((EditText) findViewById(R.id.set_meeting_notes_edittext)).getText().toString();

        // check required fields.
        if (email.length() < 3 || ! email.contains("@")){
            Toast.makeText(getApplicationContext(), "Invalid email.", Toast.LENGTH_LONG).show();
            return;
        }

        if (phone.length() < 10 || ! phone.matches("[0123456789-]*")){
            Toast.makeText(getApplicationContext(), "Invalid Phone number.", Toast.LENGTH_LONG).show();
            return;
        }

        if (! timeset || ! dateset){
            Toast.makeText(getApplicationContext(), "Please enter a time and a date.", Toast.LENGTH_LONG).show();
            return;
        }

        if (mMarker == null) {
            Toast.makeText(this, "Click on the map to add a meeting location.", Toast.LENGTH_LONG).show();
            return;
        }

        borrowRecord.setDateBorrowed(date);
        borrowRecord.setOwnerEmail(email);
        borrowRecord.setOwnerPhoneNumber(phone);
        borrowRecord.setOwnerNotes(notes);
        borrowRecord.setApproved(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("borrowrecords").child(borrowRecord.getRecordid()).setValue(borrowRecord)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Suscsesfully borrow record to database.");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write borrow record to database", e);
                    }
                });

        this.finish();


    }

    /**
     * Shows Date picker dialog.  Called when date entry button is clicked.
     * @param v View
     */
    public void showDatePickerDialog(View v) {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "datePicker");

    }

    /**
     * Shows time picker dialog. Called when time entry button is clicked.
     * @param v View
     */
    public void showTimePickerDialog(View v) {
        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), "timePicker");

    }

    /**
     * Called when time has been set by useing timeFragment. Gets the time from the fragment and
     *      * updates the time button to display new time, and sets the time in the newEntry object.
     * @param view boilerplate-not used
     * @param hourOfDay int
     * @param minute int
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        timeset = true;
        date.setHours(hourOfDay);
        date.setMinutes(minute);
        String timeString = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
        Button timeButton = (Button) findViewById(R.id.set_meeting_set_time_button);
        timeButton.setText(timeString);

    }

    /**
     * Called when date has been set useing dateFragment.  Gets the date from the fragment and
     * updates the date button to display new date, and sets the date in the newEntry object.
     * @param view boilerplate-not used
     * @param year int
     * @param month int
     * @param day int
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        dateset = true;
        date.setYear(year);
        date.setMonth(month);
        date.setDate(day);
        String dateString = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        dateButton.setText(dateString);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        Get location permissions if they haven't been given yet.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION_PERMISSION);
        }

//        Add my location button and display user on map.
        mMap.setMyLocationEnabled(true);

//        Move map to user's current location and zoom.
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        android.location.Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
        } else {
            Toast.makeText(this, "Error getting location.", Toast.LENGTH_SHORT).show();
        }

//        Set clicking on the map to add/move the marker to that location.
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                If the marker has already been set, remove it.
                if (mMarker != null) {
                    mMarker.remove();
                }
//                Create new marker.
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Meeting Location"));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOCATION_PERMISSION) {
            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message and exit.
                Toast.makeText(this, "Need location permission to set meeting.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        }
    }
}
