package com.dr.frappe.activity.expense;

import android.os.AsyncTask;
import android.util.Log;

import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;

import java.util.List;

/**
 * Created by rohitman on 11/12/2015.
 */
public class ExpenseListAsyncTask extends AsyncTask<String, Integer, List<ExpenseDTO>> {
    private ExpenseListAdapter expenseListAdapter;

    public ExpenseListAsyncTask(ExpenseListAdapter expenseListAdapter) {
        this.expenseListAdapter = expenseListAdapter;
    }

    @Override
    protected List<ExpenseDTO> doInBackground(String... params) {
        Log.i(this.getClass().getName(), "About to fetch Expenses for: " + params[0]);

        List<ExpenseDTO> listExpenses = ClientController.getInstance().getExpenses(params[0]);
        return listExpenses;
    }

    @Override
    protected void onPostExecute(List<ExpenseDTO> expenseDTOs) {
        super.onPostExecute(expenseDTOs);
        expenseListAdapter.setExpenseDTOList(expenseDTOs);
    }
}
