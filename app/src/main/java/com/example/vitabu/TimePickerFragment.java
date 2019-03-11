/*
 * This file contains the Time Picker fragment that is used by the setMeetingActivity.
 *
 * Author: Tristan Carlson
 * Version: 1.0
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    private Activity myActivity;
    private TimePickerDialog.OnTimeSetListener mylistener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        myActivity = activity;
        mylistener = (TimePickerDialog.OnTimeSetListener) activity;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        return new TimePickerDialog(myActivity, mylistener, hour, minute, DateFormat.is24HourFormat(getActivity()));

    }

}
