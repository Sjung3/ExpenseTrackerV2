package com.sjung3;

import java.sql.Date;

//The API's request data is defines in this class
public class ExpenseData {
    private int budgetID;
    private int expenseID;
    private java.sql.Date expenseDate;
    private Category expenseCategory;
    private double expenseAmount;
    private String expenseComment;

    public ExpenseData(int budgetID, Date expenseDate, Category expenseCategory, double expenseAmount, String expenseComment) {
        this.budgetID = budgetID;
        this.expenseID = expenseID;
        this.expenseDate = expenseDate;
        this.expenseCategory = expenseCategory;
        this.expenseAmount = expenseAmount;
        this.expenseComment = expenseComment;
    }

    public int getBudgetID() {
        return budgetID;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public Category getExpenseCategory() {
        return expenseCategory;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public String getExpenseComment() {
        return expenseComment;
    }
}

