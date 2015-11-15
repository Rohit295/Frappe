package com.dr.frappe.activity.expense;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.dr.frappe.R;
import com.dr.frappe.activity.Main;
import com.dr.frappe.api.ClientController;
import com.dr.frappe.model.ExpenseDTO;

import java.util.zip.Inflater;

/**
 * Dialog Fragment to define experience for adding a new Experience. This dialog needs to take the
 * input, validate it, update the expense and refresh the Expesnes list
 *
 * Created by rohitman on 10/30/2015.
 */
public class NewExpenseDialogFragment extends DialogFragment {
    private Activity holdingActivity;
    private ExpenseListAdapter expenseListAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        holdingActivity = this.getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View newExpenseView = inflater.inflate(R.layout.l_new_expense, null);
        builder.setView(newExpenseView);

        builder.setTitle("Enter expense"); //TODO replace with setting

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Create an Expense Object and save it using the ClientController. However since
                // HTTP operations cannot be run on the main thread, do it through an AsyncTask
                ExpenseDTO expenseDTO = new ExpenseDTO();
                expenseDTO.setExpenseHead(
                        ((EditText) newExpenseView.findViewById(R.id.lne_head)).getText().toString());
                expenseDTO.setExpenseDescription(
                        ((EditText) newExpenseView.findViewById(R.id.lne_description)).getText().toString());
                expenseDTO.setExpenseCurrency(
                        ((EditText) newExpenseView.findViewById(R.id.lne_currency)).getText().toString());
                expenseDTO.setExpenseAmount(
                        Double.parseDouble(((EditText) newExpenseView.findViewById(R.id.lne_amount)).getText().toString()));
                expenseListAdapter = ((Main)holdingActivity).getExpenseListAdapter();
                new NewExpenseAsyncTask("Rohit", expenseListAdapter).execute(expenseDTO);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NewExpenseDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}
