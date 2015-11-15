package com.dr.frappe.activity;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dr.frappe.R;
import com.dr.frappe.activity.expense.ExpenseListAsyncTask;
import com.dr.frappe.activity.expense.ExpenseRecyclerItemDecoration;
import com.dr.frappe.activity.expense.NewExpenseDialogFragment;
import com.dr.frappe.activity.expense.ExpenseRecyclerAdapter;
import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {
    private RecyclerView expenseRView;
    private RecyclerView.LayoutManager expenseRViewLayout;
    private RecyclerView.Adapter expenseRVAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        ClientController.createInstance();

        // Build the RecyclerView. Add an ItemDecorator so that each item in the list is decorated
        // (space, divider between items etc.). Finally set LayoutManager and Adapter of items
        expenseRView =  (RecyclerView)findViewById(R.id.ae_expenseview);
        expenseRViewLayout = new LinearLayoutManager(this);
        expenseRView.addItemDecoration(new ExpenseRecyclerItemDecoration(this));
        expenseRView.setLayoutManager(expenseRViewLayout);

        // The Adapter needs to be prepped with the initial list so getExpenses list and then build adapter
        getExpenses();
        expenseRView.setAdapter(expenseRVAdapter);

        // add a handler to create a new expense
        FloatingActionButton newExpense = (FloatingActionButton) findViewById(R.id.ae_add_new);
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

    /**
     *
     * @return List of Expenses for the user
     */
    private List<ExpenseDTO> getExpenses() {
        List<ExpenseDTO> tempList = new ArrayList<ExpenseDTO>();
        tempList.add(new ExpenseDTO(1));
        tempList.add(new ExpenseDTO(2));
        tempList.add(new ExpenseDTO(3));
        tempList.add(new ExpenseDTO(4));
        tempList.add(new ExpenseDTO(5));

        expenseRVAdapter = new ExpenseRecyclerAdapter(tempList);
        new ExpenseListAsyncTask((ExpenseRecyclerAdapter)expenseRVAdapter).execute("Rohit");

        return tempList;
    }
}
