package com.caue.splitter.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * Created by Caue on 9/20/2016.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    OnDateSetListener mDateSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // create mListener to do the button actions in the activities

        try{
            mDateSetListener = (OnDateSetListener) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("The hosting activity of the Fragment" +
                    "forgot to implement OnFragmentInteractionListener");
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        mDateSetListener.OnDateSet(year, month+1, day);
    }

    // Interactions ocurring on AccountInfoFragment
    public interface OnDateSetListener{
        void OnDateSet(int year, int month, int day);
    }
}
