package com.dr.frappe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dr.frappe.R;
import com.dr.frappe.activity.expense.ExpenseListAsyncTask;
import com.dr.frappe.activity.expense.ExpenseRecyclerItemDecoration;
import com.dr.frappe.activity.expense.ExpenseRecyclerAdapter;
import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;
import com.dr.frappe.utils.ExpenseStringifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpenseActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView expenseRView;
    private RecyclerView.LayoutManager expenseRViewLayout;
    private RecyclerView.Adapter expenseRVAdapter;

    private final int EXPENSE_ACTIVITY_NEW_EXPENSE = 1407;
    private final String EXPENSE_ACTIVITY_SAVE_KEY = "expenseStateSave";

    private final String EXPENSE_SHARED_PREFERENCE_FILE = "expensePreferenceFile";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(getClass().getName(), "Entered onCreate()");
        setContentView(R.layout.activity_expense);

        ClientController.createInstance();

        // Build the RecyclerView. Add an ItemDecorator so that each item in the list is decorated
        // (space, divider between items etc.). Finally set LayoutManager and Adapter of items
        expenseRView =  (RecyclerView)findViewById(R.id.ae_expenseview);
        expenseRViewLayout = new LinearLayoutManager(this);
        expenseRView.addItemDecoration(new ExpenseRecyclerItemDecoration(this));
        expenseRView.setLayoutManager(expenseRViewLayout);

        // The Adapter needs to be prepped with the initial list so getExpenses list and then build adapter
        List<ExpenseDTO> tempList = getExpenses(savedInstanceState);
        expenseRVAdapter = new ExpenseRecyclerAdapter(tempList);
        new ExpenseListAsyncTask((ExpenseRecyclerAdapter)expenseRVAdapter).execute("Rohit");
        expenseRView.setAdapter(expenseRVAdapter);

        // add a handler to create a new expense
        FloatingActionButton newExpense = (FloatingActionButton) findViewById(R.id.ae_add_new);
        newExpense.setOnClickListener(this);
    }

    /**
     * Decide how to get the Expenses List. If there is already something saved by way of expenses
     * then reload that
     * @param savedInstanceState
     */
    private List<ExpenseDTO> getExpenses(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "Entered getExpenses");

        List<ExpenseDTO> tempList;
        if ((savedInstanceState != null) && (savedInstanceState.getStringArrayList(EXPENSE_ACTIVITY_SAVE_KEY) != null)) {
            List<String> listSavedExpenses = savedInstanceState.getStringArrayList(EXPENSE_ACTIVITY_SAVE_KEY);
            tempList = new ArrayList<ExpenseDTO>(listSavedExpenses.size());

            for (int i=0; i<tempList.size(); i++) {
                tempList.add(i, ExpenseStringifier.inflateExpense(listSavedExpenses.get(i)));
            }
        } else {
            tempList = getExpensesFromStorage();
        }

        return tempList;
    }

    /**
     * Helper to get any Expenses List that has been stored in Storage. Currently this is based of
     * SharedPreferences but might need to come back and revisit
     * @return
     */
    private List<ExpenseDTO> getExpensesFromStorage() {
        Log.i(getClass().getName(), ": getExpensesFromStorage - reading Expenses List");

        // create a TempList variable and initialize with something
        List<ExpenseDTO> tempList = new ArrayList<ExpenseDTO>();
        tempList.add(new ExpenseDTO(1));

        // Todo is SharedPreference the best way?
        SharedPreferences settings =  getSharedPreferences(EXPENSE_SHARED_PREFERENCE_FILE, MODE_PRIVATE);

        // check the number of expenses. If a 'set' of expenses is found, read each one and
        // prep into Expenses list
        if (settings.getInt("NUMBER_EXPENSES", 98989898) != 98989898) {
            int numberExpenses = settings.getInt("NUMBER_EXPENSES", 98989898);
            tempList = new ArrayList<ExpenseDTO>(numberExpenses);
            for (int i=0; i<numberExpenses; i++) {
                String savedExpense = settings.getString("Expense_" + i, "DEFAULT_VALUE");
                tempList.add(ExpenseStringifier.inflateExpense(savedExpense));
            }
        }

        return tempList;
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(getClass().getName(), "Entered onStop() and saving Expenses to Storage");
        saveExpensesToStorage(((ExpenseRecyclerAdapter) expenseRVAdapter).getExpenseDTOList());
    }

    /**
     * Helper to save an Expense List to Storage. Currently this is based of SharedPreferences but
     * might need to come back and visit
     * @param tempList
     */
    private void saveExpensesToStorage(List<ExpenseDTO> tempList) {
        SharedPreferences settings =  getSharedPreferences(EXPENSE_SHARED_PREFERENCE_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // save the number of expenses
        editor.putInt("NUMBER_EXPENSES", tempList.size());

        // Read each of the expenses and store it in a separate entry in the Preferences file
        // Reason for doing each separately and not as a set is to preserve the sorting order which
        // a set would not, forcing for an additional sort cost before rendering
        for (int i=0; i<tempList.size(); i++) {
            String savedExpense = ExpenseStringifier.deflateExpense(tempList.get(i));
            editor.putString("Expense_" + i, savedExpense);
        }
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(getClass().getName(), "Entered onStart() and about to read Expenses from Storage");

        // The Adapter needs to be prepped with the initial list so getExpenses list and then build adapter
        List<ExpenseDTO> tempList = getExpensesFromStorage();
        expenseRVAdapter = new ExpenseRecyclerAdapter(tempList);
        new ExpenseListAsyncTask((ExpenseRecyclerAdapter)expenseRVAdapter).execute("Rohit");
        expenseRView.setAdapter(expenseRVAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current expense list
        // (1) convert to an ArrayList of Strings, for real quick retrieval
        // (2) save to the Bundle so Droid can store it somewhere
        List<ExpenseDTO> listExpenses = ((ExpenseRecyclerAdapter)expenseRVAdapter).getExpenseDTOList();
        ArrayList<String> listExpensesToSave = new ArrayList<String>(listExpenses.size());

        for (int i=0; i<listExpenses.size(); i++) {
            listExpensesToSave.add(i, ExpenseStringifier.deflateExpense(listExpenses.get(i)));
        }
        outState.putStringArrayList(EXPENSE_ACTIVITY_SAVE_KEY, listExpensesToSave);
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
     * Use this method to
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the Request made was to add a new Expense and the Response was OK, then look
        // for an Expense and add to the list
        if (requestCode == EXPENSE_ACTIVITY_NEW_EXPENSE) {
            if (resultCode == RESULT_OK) {
                if (data.getSerializableExtra("savedExpense") != null) {
                    ExpenseDTO newExpense = (ExpenseDTO) data.getSerializableExtra("savedExpense");

                    // Add the new Expense and Notify the Change so that it kicks in
                    ((ExpenseRecyclerAdapter) expenseRVAdapter).getExpenseDTOList().add(newExpense);
                    expenseRVAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * Use the onClick Handler to handle the FAB New Expense Action
     * Every time this is pressed, create a new Activity to accept new expense as input
     * @param v
     */
    @Override
    public void onClick(View v) {
        // create the intent and add the List of Expenses to the Activity
        Intent newExpenseActivity = new Intent(this, NewExpenseActivity.class);
        startActivityForResult(newExpenseActivity, EXPENSE_ACTIVITY_NEW_EXPENSE);
    }
}
