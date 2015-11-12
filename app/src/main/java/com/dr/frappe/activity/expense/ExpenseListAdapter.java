package com.dr.frappe.activity.expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dr.frappe.R;
import com.dr.frappe.model.ExpenseDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rohitman on 8/16/2015.
 */
public class ExpenseListAdapter extends ArrayAdapter<ExpenseDTO> {
    private Context context;
    private List<ExpenseDTO> expenseDTOList;

    public List<ExpenseDTO> getExpenseDTOList() {
        return expenseDTOList;
    }
    public void setExpenseDTOList(List<ExpenseDTO> expenseDTOList) {
        this.expenseDTOList = expenseDTOList;
        notifyDataSetChanged();
    }

    // Todo: can ExpenseListAdapter do without the list being passed in through constructor
    public ExpenseListAdapter(Context context, List<ExpenseDTO> objects) {
        super(context, R.layout.l_expense_item, objects);
        this.context = context;
        this.expenseDTOList = objects;
    }

    /**
     * Return the List of Expense Items
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // create the view of an expense item
        // TODO: avoid creating a new view each time. Use the getView method to recycle views
        View viewOneExpense = null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewOneExpense = inflater.inflate(R.layout.l_expense_item, parent, false);

        // now set the different data points
        ExpenseDTO oneExpenseToRender = expenseDTOList.get(position);

        TextView expenseDescription = (TextView)viewOneExpense.findViewById(R.id.lei_expense_description);
        expenseDescription.setText(oneExpenseToRender.getExpenseHead());

        TextView expenseAmount = (TextView)viewOneExpense.findViewById(R.id.lei_expense_amount);
        expenseAmount.setText(String.valueOf(oneExpenseToRender.getExpenseAmount()));

        TextView expenseCurrency = (TextView)viewOneExpense.findViewById(R.id.lei_expense_currency);
        expenseCurrency.setText(oneExpenseToRender.getExpenseCurrency());

        TextView expenseDate = (TextView)viewOneExpense.findViewById(R.id.lei_expense_date);
        Date dateOfExpense =  new Date(oneExpenseToRender.getExpenseRecordedAt());
        expenseDate.setText(new SimpleDateFormat("dd/mm/yy").format(dateOfExpense));

        return viewOneExpense;
    }
}
