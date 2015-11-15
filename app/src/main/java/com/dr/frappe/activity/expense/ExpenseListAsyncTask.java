package com.dr.frappe.activity.expense;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;

import java.util.List;

/**
 * Created by rohitman on 11/12/2015.
 */
public class ExpenseListAsyncTask extends AsyncTask<String, Integer, List<ExpenseDTO>> {
    private ExpenseListAdapter expenseListAdapter;
    private ExpenseRecyclerAdapter expenseRecyclerAdapter;

    public ExpenseListAsyncTask(ExpenseListAdapter expenseListAdapter) {
        this.expenseListAdapter = expenseListAdapter;
    }

    public ExpenseListAsyncTask(ExpenseRecyclerAdapter expenseRecyclerAdapter) {
        this.expenseRecyclerAdapter = expenseRecyclerAdapter;
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

        // Todo - one of these has to go
        if (expenseListAdapter != null)
            expenseListAdapter.setExpenseDTOList(expenseDTOs);
        else if (expenseRecyclerAdapter != null)
            expenseRecyclerAdapter.setExpenseDTOList(expenseDTOs);
    }
}
