/*
 * This file contains the activity that sets the meeting between the owner and the borrower.
 *
 * Author: Tristan Carlson
 * Version: 1.0
 * Outstanding Issues: This file has no database connectivity yet.
 */
package com.example.vitabu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Date;

public class setMeetingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    BorrowRecord borrowRecord;
    Date date = new Date();
    boolean timeset = false;
    boolean dateset = false;
    String email;
    String phone;
    String notes;
    Button timeButton;
    Button dateButton;
    String logTag = "set Meeting Activity";
    FirebaseAuth auth;

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

    }



    /**
     * checks User's entrered data is valid and submits.
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
        }

        // TODO Add check for google maps.
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


}
