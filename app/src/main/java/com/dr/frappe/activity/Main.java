package com.dr.frappe.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dr.frappe.R;
import com.dr.frappe.activity.expense.ExpenseListAsyncTask;
import com.dr.frappe.activity.expense.ExpenseListAdapter;
import com.dr.frappe.activity.expense.NewExpenseDialogFragment;
import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listExpensesView;
    private ExpenseListAdapter expenseListAdapter;

    public ExpenseListAdapter getExpenseListAdapter() {return expenseListAdapter; }
    public void setExpenseListAdapter(ExpenseListAdapter expenseListAdapter) {
        this.expenseListAdapter = expenseListAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClientController.createInstance();

        // lets load the initial set of expenses for this user. getExpenses will return an initial
        // list to show (blank list basically) and load the real list asynchronously
        listExpensesView = (ListView) findViewById(R.id.main_expense_list);
        List<ExpenseDTO> listExpenses = getExpenses();
        listExpensesView.setAdapter(expenseListAdapter);

        // add a handler to create a new expense
        FloatingActionButton newExpense = (FloatingActionButton) findViewById(R.id.main_expense_add_new);
        newExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewExpenseDialogFragment newExpenseFragment = new NewExpenseDialogFragment();
                newExpenseFragment.show(getFragmentManager(), "New_Expense");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private List<ExpenseDTO> getExpenses() {
        List<ExpenseDTO> tempList = new ArrayList<ExpenseDTO>();
        tempList.add(new ExpenseDTO(1));
        /*
        tempList.add(new ExpenseDTO(2));
        tempList.add(new ExpenseDTO(3));
        tempList.add(new ExpenseDTO(4));
        tempList.add(new ExpenseDTO(5));
        */

        expenseListAdapter = new ExpenseListAdapter(this, tempList);
        new ExpenseListAsyncTask(expenseListAdapter).execute("rohit");
        return tempList;
    }
}