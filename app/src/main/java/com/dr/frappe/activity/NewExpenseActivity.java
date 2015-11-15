package com.dr.frappe.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dr.frappe.R;
import com.dr.frappe.api.ClientController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class NewExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        ClientController.createInstance();

        // since this activity is always called from ExpenseList, provide the way to navigate
        // back to the parent
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set default value for date. Since the date is not directly editable, it will support
        // showing a date picker
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy");
        TextView expenseDate = (TextView)findViewById(R.id.ane_date);
        expenseDate.setText(dateFormatter.format(cal.getTime()));
        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new ExpenseDatePickerDialog();
                datePickerFragment.show(getFragmentManager(), "Date Picker");
            }
        });

    }

    public static class ExpenseDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // TODO see if the Calendar View for Date Picker, can be replaced by a Spinner view
            //return new DatePickerDialog(getActivity(), this, year, month, day);

            // Create a simple Date Picker dialog but set it such that it does not show the Calendar
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            datePickerDialog.getDatePicker().setSpinnersShown(true);
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            TextView expenseDate = (TextView) getActivity().findViewById(R.id.ane_date);
            Calendar selectedDate  = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            DateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy");
            expenseDate.setText(dateFormatter.format(selectedDate.getTime()));
        }
    }
}