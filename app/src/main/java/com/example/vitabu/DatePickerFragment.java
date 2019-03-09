package com.example.vitabu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * DialogFragment to allow the user to pick the date of their entry.
 * @author Tristan Carlson.
 */
public class DatePickerFragment extends DialogFragment {
    private Activity myActivity;
    private DatePickerDialog.OnDateSetListener mylistener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        myActivity = activity;
        mylistener = (DatePickerDialog.OnDateSetListener) activity;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(myActivity, mylistener, year, month, day);

    }


}