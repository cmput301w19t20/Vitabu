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
    This file contains the code to implement the logic and UI to display to the borrower the details
    about the meeting details provided by the owner.

    Author: Arseniy Kouzmenkov

    Version: 1.0

    Outstanding Issues: Default behavior when no location is provided may be buggy.
 */

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

    //This method will create the activity on startup and will populate all the information in the
    //activity from the borrow record passed to this activity through the intent. This screen is
    //essentially a static page and that is it.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_meeting);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.BORROWRECORD_MESSAGE);
        Gson gson = new Gson();
        borrowRecord = gson.fromJson(message, BorrowRecord.class);

        //Presents time in the correct format.
        TextView time = (TextView) findViewById(R.id.get_meeting_time);
        String temp = "Time: ";
        temp += (borrowRecord.getDateBorrowed().getHours() < 10) ? ("0" + Integer.toString(borrowRecord.getDateBorrowed().getHours()) + ":") : (Integer.toString(borrowRecord.getDateBorrowed().getHours()) + ":");
        temp += (borrowRecord.getDateBorrowed().getMinutes() < 10) ? ("0" + Integer.toString(borrowRecord.getDateBorrowed().getMinutes())) : (Integer.toString(borrowRecord.getDateBorrowed().getMinutes())) ;
        time.setText(temp);

        //Presents date in the specified format (currently hardcoded).
        TextView date = (TextView) findViewById(R.id.get_meeting_date);
        temp = "Date: " + Integer.toString(borrowRecord.getDateBorrowed().getYear()) + "-" +
                Integer.toString(borrowRecord.getDateBorrowed().getMonth() + 1) + "-" +
                Integer.toString(borrowRecord.getDateBorrowed().getDate());
        date.setText(temp);

        TextView ownerEmail = (TextView) findViewById(R.id.get_meeting_owner_email_email);
        ownerEmail.setText(borrowRecord.getOwnerEmail());

        TextView ownerPhone = (TextView) findViewById(R.id.get_meeting_owner_phone_phone);
        ownerPhone.setText(borrowRecord.getOwnerPhoneNumber());

        TextView ownerNotes = (TextView) findViewById(R.id.get_meeting_owner_notes_notes);
        ownerNotes.setText(borrowRecord.getOwnerNotes());

        //Activates the Google Map fragment.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.get_meeting_map);
        mapFragment.getMapAsync(this);
    }


    //This method will start the google map, zoom in on the right location and put down the marker there.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(borrowRecord.getPickUpLocation().getLat(), borrowRecord.getPickUpLocation().getLng()), 12.0f));
        mMap.addMarker(new MarkerOptions().position(new LatLng(borrowRecord.getPickUpLocation().getLat(), borrowRecord.getPickUpLocation().getLng())).title("Meeting Location"));

    }
}
