package com.dr.frappe.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * This DatePicker wiorks like a regular DatePicker, except that it needs the DateSetListener
 * to be passed in and for the caller to handle the date set event
 *
 * Created by rohitman on 11/15/2015.
 */
public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener listener;

    /**
     * Creating the Empty Constructor for recreation of the dialog when system deems it necessary
     */
    public DatePickerFragment() {

    }

    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
