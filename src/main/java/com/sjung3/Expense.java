package com.sjung3;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenseID", nullable = false)
    private Integer expenseID;
    //Primary key value is generated automatically
    //IDENTITY = auto-incremented
    @Column(name = "budgetID", nullable = false)
    private int budgetID;
    @Column(name = "expenseDate", nullable = false)
    private Date expenseDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "expenseCategory", nullable = false, length = 30)
    private Category expenseCategory;
    @Column(name = "expenseAmount", nullable = false)
    private double expenseAmount;
    @Column(name = "expenseComment", nullable = false, length = 30)
    private String expenseComment;

    public Expense() {
    }

    //expenseID excluded as parameter since it is on auto-incremented in in DB table
    public Expense(int budgetID, Date expenseDate, Category expenseCategory, double expenseAmount, String expenseComment) {
        this.expenseID = expenseID;
        this.budgetID = budgetID;
        this.expenseDate = expenseDate;
        this.expenseCategory = expenseCategory;
        this.expenseAmount = expenseAmount;
        this.expenseComment = expenseComment;
    }

    public Integer getExpenseID() {
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
