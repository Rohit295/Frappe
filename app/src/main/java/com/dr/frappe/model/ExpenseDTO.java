package com.dr.frappe.model;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by rohitman on 8/16/2015.
 */
public class ExpenseDTO implements Serializable, Comparable<ExpenseDTO> {

    private String expenseHead;
    private double expenseAmount;
    private String expenseCurrency;

    private Location expenseLocation;
    private long expenseRecordedAt;


    public ExpenseDTO(int counter) {
        expenseRecordedAt = System.currentTimeMillis();

        if (counter == 1) {
            expenseHead = "Groceries shopping";
            expenseAmount = 132.54;
            expenseCurrency = "INR";
        } else if (counter == 2) {
            expenseHead = "Sweets at Sweet Basket";
            expenseAmount = 87.50;
            expenseCurrency = "INR";
        } else if (counter == 3) {
            expenseHead = "Bata Shoes for all 3 of us";
            expenseAmount = 4965.99;
            expenseCurrency = "INR";
        }
    }


    public String getExpenseHead() {
        return expenseHead;
    }

    public void setExpenseHead(String expenseHead) {
        this.expenseHead = expenseHead;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseCurrency() {
        return expenseCurrency;
    }

    public void setExpenseCurrency(String expenseCurrency) {
        this.expenseCurrency = expenseCurrency;
    }

    public Location getExpenseLocation() {
        return expenseLocation;
    }

    public void setExpenseLocation(Location expenseLocation) {
        this.expenseLocation = expenseLocation;
    }

    public long getExpenseRecordedAt() {
        return expenseRecordedAt;
    }

    public void setExpenseRecordedAt(long expenseRecordedAt) {
        this.expenseRecordedAt = expenseRecordedAt;
    }

    /**
     *
     * @param another
     * @return
     */
    @Override
    public int compareTo(ExpenseDTO another) {
        return 0;
    }
}
