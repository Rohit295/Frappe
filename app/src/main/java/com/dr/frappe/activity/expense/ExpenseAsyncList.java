package com.dr.frappe.activity.expense;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;

import java.util.List;

/**
 * Created by rohitman on 11/12/2015.
 */
public class ExpenseAsyncList extends AsyncTask<String, Integer, List<ExpenseDTO>> {
    private ArrayAdapter expenseListAdapter;

    public ExpenseAsyncList(ArrayAdapter expenseListAdapter) {
        this.expenseListAdapter = expenseListAdapter;
    }

    @Override
    protected List<ExpenseDTO> doInBackground(String... params) {
        Log.i(this.getClass().getName(), "About to fetch Expenses for: " + params[0]);

        List<ExpenseDTO> listExpenses = ClientController.getInstance().getExpenses(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(List<ExpenseDTO> expenseDTOs) {
        super.onPostExecute(expenseDTOs);

        expenseListAdapter.clear();
        expenseListAdapter.addAll(expenseDTOs);
    }
}
