package com.dr.frappe.activity.expense;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dr.frappe.R;
import com.dr.frappe.model.ExpenseDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rohitman on 11/15/2015.
 */
public class ExpenseRecyclerAdapter extends RecyclerView.Adapter<ExpenseRecyclerAdapter.ViewHolder>{
    private List<ExpenseDTO> expenseDTOList;

    public ExpenseRecyclerAdapter(List<ExpenseDTO> expenseDTOList) {
        super();
        this.expenseDTOList = expenseDTOList;
    }

    /**
     * Create the View to add to the RecyclerView. Each View will be populated through the
     * BindViewHolder event
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ExpenseRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View expenseItemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.l_expense_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(expenseItemView);
        return viewHolder;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(this.getClass().getName(), "About to build view for #" + position);

        View expenseView = holder.itemView;
        ExpenseDTO oneExpenseToRender = expenseDTOList.get(position);

        // For the specific expense, build the view elements
        TextView expenseDescription = (TextView)expenseView.findViewById(R.id.lei_expense_description);
        expenseDescription.setText(oneExpenseToRender.getExpenseDescription());

        TextView expenseAmount = (TextView)expenseView.findViewById(R.id.lei_expense_amount);
        expenseAmount.setText(String.valueOf(oneExpenseToRender.getExpenseAmount()));

        TextView expenseCurrency = (TextView)expenseView.findViewById(R.id.lei_expense_currency);
        expenseCurrency.setText(oneExpenseToRender.getExpenseCurrency());

        TextView expenseDate = (TextView)expenseView.findViewById(R.id.lei_expense_date);
        Date dateOfExpense =  new Date(oneExpenseToRender.getExpenseRecordedAt());
        expenseDate.setText(new SimpleDateFormat("dd/mm/yy").format(dateOfExpense));
    }

    @Override
    public int getItemCount() {
        return expenseDTOList.size();
    }

    /**
     * Class: ViewHolder for the RecyclerView Adapter
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}