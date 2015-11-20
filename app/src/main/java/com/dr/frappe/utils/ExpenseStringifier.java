package com.dr.frappe.utils;

import com.dr.frappe.model.ExpenseDTO;

/**
 * Created by rohitman on 11/17/2015.
 */

// Todo - this class should move to ExpenseDTO

public class ExpenseStringifier {
    public static String deflateExpense(ExpenseDTO expenseDTO) {
        String flattenedExpense = expenseDTO.getExpenseDescription() + ":/:" +
                expenseDTO.getExpenseHead() + ":/:" +
                expenseDTO.getExpenseCurrency() + ":/:" +
                String.valueOf(expenseDTO.getExpenseAmount());

        return flattenedExpense;
    }

    public static ExpenseDTO inflateExpense(String expenseAsString) {
        String[] expenseElements = expenseAsString.split(":/:");
        ExpenseDTO expenseDTO = new ExpenseDTO();

        expenseDTO.setExpenseDescription(expenseElements[0]);
        expenseDTO.setExpenseHead(expenseElements[1]);
        expenseDTO.setExpenseCurrency(expenseElements[2]);
        expenseDTO.setExpenseAmount(Double.parseDouble(expenseElements[3]));

        return expenseDTO;
    }
}
