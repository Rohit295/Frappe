package com.dr.frappe.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dr.frappe.R;
import com.dr.frappe.activity.expense.NewExpenseAsyncTask;
import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

    /**
     * Build the Menu. Main Options are (1) Back Button which comes by default & (2) Save Action
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.m_save) {
            // Create an Expense Object and save it using the ClientController. However since
            // HTTP operations cannot be run on the main thread, do it through an AsyncTask
            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setExpenseHead(
                    ((EditText) findViewById(R.id.ane_head)).getText().toString());
            expenseDTO.setExpenseDescription(
                    ((EditText) findViewById(R.id.ane_description)).getText().toString());
            expenseDTO.setExpenseCurrency(
                    (String) (((Spinner) findViewById(R.id.ane_currency)).getSelectedItem()));
            expenseDTO.setExpenseAmount(
                    Double.parseDouble(((EditText) findViewById(R.id.ane_amount)).getText().toString()));
            new NewExpenseAsyncTask("Rohit", this).execute(expenseDTO);

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * use this method to pass back the Expense that was just added, to the calling Activity.
     * Caller decides what to do with the data.
     * @param expenseDTO
     */
    public void updateExpenseMainList(ExpenseDTO expenseDTO) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("savedExpense", expenseDTO);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * DatePicker
     */
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