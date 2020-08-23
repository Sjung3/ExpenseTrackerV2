package com.sjung3;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

//The class represents the budgets table in the DB

@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    //Primary key value is generated automatically
    //IDENTITY = auto-incremented
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budgetID", nullable = false)
    private Integer budgetID;
    @Column(name = "budgetName", nullable = false, length = 10)
    private String budgetName;
    @Column(name = "budgetStartDate", nullable = false)
    private Date budgetStartDate;
    @Column(name = "budgetEndDate", nullable = false)
    private Date budgetEndDate;
    @Column(name = "budgetAmount", nullable = false)
    private double budgetAmount;

    //Creates the objects after Hibernate has retrieved the records from the database
    public Budget() {
    }
    //budgetID excluded as parameter since it is on auto-incremented in in DB table
    public Budget(String budgetName, Date budgetStartDate, Date budgetEndDate, double budgetAmount) {
        this.budgetName = budgetName;
        this.budgetStartDate = budgetStartDate;
        this.budgetEndDate = budgetEndDate;
        this.budgetAmount = budgetAmount;
    }

    public Integer getBudgetID() {
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