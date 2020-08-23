package com.sjung3;

import java.sql.Date;

//The API's request data is defines in this class
public class BudgetData {
    private int budgetID;
    private String budgetName;
    private java.sql.Date budgetStartDate;
    private java.sql.Date budgetEndDate;
    private double budgetAmount;

    //budgetID excluded as parameter since it is on auto-incremented in in DB table
    public BudgetData(String budgetName, Date budgetStartDate, Date budgetEndDate, double budgetAmount) {
        this.budgetID = budgetID;
        this.budgetName = budgetName;
        this.budgetStartDate = budgetStartDate;
        this.budgetEndDate = budgetEndDate;
        this.budgetAmount = budgetAmount;
    }

    public int getBudgetID() {
        return budgetID;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public Date getBudgetStartDate() {
        return budgetStartDate;
    }

    public Date getBudgetEndDate() {
        return budgetEndDate;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

}
