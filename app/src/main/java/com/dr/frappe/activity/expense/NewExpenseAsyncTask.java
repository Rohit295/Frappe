package com.dr.frappe.activity.expense;

import android.os.AsyncTask;
import android.util.Log;

import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;

/**
 * AsyncTask to save a new Expense DTO. Keeping the logic simple - for the current user, save this
 * expenseDTO. Once saved, the expense needs to get added to the ExpenseListAdapter, at the bottom
 * Created by rohitman on 11/13/2015.
 */
public class NewExpenseAsyncTask extends AsyncTask<ExpenseDTO, Integer, Boolean> {
    private String userId;
    private ExpenseListAdapter expenseListAdapter;

    public NewExpenseAsyncTask(String userId, ExpenseListAdapter expenseListAdapter) {
        this.userId = userId;
        this.expenseListAdapter = expenseListAdapter;
    }

    @Override
    protected Boolean doInBackground(ExpenseDTO... params) {
        ExpenseDTO expenseToSave = (ExpenseDTO)params[0];
        Log.i(this.getClass().getName(), "About to save a new Expense: " +
                        expenseToSave.getExpenseHead() + "/" +
                        expenseToSave.getExpenseDescription() + "/" +
                        expenseToSave.getExpenseCurrency() + "/" +
                        expenseToSave.getExpenseAmount());

        ClientController.getInstance().addExpense(expenseToSave, userId);
        return Boolean.TRUE;
    }

    /**
     * postExecute is important here. Once the ExpenseDTO is successfully saved, the same Expense
     * needs to be added to the ListView so that user can start seeing it
     * @param state
     */
    @Override
    protected void onPostExecute(Boolean state) {
        super.onPostExecute(state);

        expenseListAdapter.getExpenseDTOList();
        //expenseListAdapter.setExpenseDTOList(expenseDTOs);
    }

}
