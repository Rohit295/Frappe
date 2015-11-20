package com.dr.frappe.activity.expense;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dr.frappe.activity.NewExpenseActivity;
import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;

import java.util.List;

/**
 * AsyncTask to save a new Expense DTO. Keeping the logic simple - for the current user, save this
 * expenseDTO. Once saved, the expense needs to get added to the ExpenseListAdapter, at the bottom
 * Created by rohitman on 11/13/2015.
 */
public class NewExpenseAsyncTask extends AsyncTask<ExpenseDTO, Integer, ExpenseDTO> {
    private String userId;
    private Context invokingActivity;

    public NewExpenseAsyncTask(String userId, Context invokingActivity) {
        this.userId = userId;
        this.invokingActivity = invokingActivity;
    }

    @Override
    protected ExpenseDTO doInBackground(ExpenseDTO... params) {
        ExpenseDTO expenseToSave = (ExpenseDTO)params[0];
        Log.i(this.getClass().getName(), "About to save a new Expense: " +
                        expenseToSave.getExpenseHead() + "/" +
                        expenseToSave.getExpenseDescription() + "/" +
                        expenseToSave.getExpenseCurrency() + "/" +
                        expenseToSave.getExpenseAmount());

        ClientController.getInstance().addExpense(expenseToSave, userId);
        return expenseToSave;
    }

    /**
     * postExecute is important here. Once the ExpenseDTO is successfully saved, the same Expense
     * needs to be added to the ListView so that user can start seeing it
     * @param expenseDTO
     */
    @Override
    protected void onPostExecute(ExpenseDTO expenseDTO) {
        super.onPostExecute(expenseDTO);
        if (invokingActivity instanceof NewExpenseActivity) {
            ((NewExpenseActivity)invokingActivity).updateExpenseMainList(expenseDTO);
        }
    }

}
