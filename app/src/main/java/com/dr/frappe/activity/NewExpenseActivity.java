package com.dr.frappe.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dr.frappe.R;
import com.dr.frappe.api.ClientController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        ClientController.createInstance();

        // since this activity is always called from ExpenseList, provide the way to navigate
        // back to the parent
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set default value for date. Even though date is not directly editable, it shoul support
        // showing a date picker
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy");
        TextView expenseDate = (TextView)findViewById(R.id.ane_date);
        expenseDate.setText(dateFormatter.format(cal.getTime()));
        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rt = 10;
                rt++;
            }
        });

    }
}
