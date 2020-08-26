package com.sjung3;

import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Component
public class AppService {
    //Instance of AppRepository is injected to the class
    private AppRepository repository;

    public AppService(AppRepository repository) {
        this.repository = repository;
    }

    //Transactions for RDB
    //Parameters origins from AppController and methods return then response from the App-repository method

    /**
     * All transactions are initiated in the AppController class
     * All methods return then response from the App-repository class where transactions are executed
     * @return
     */

    @Transactional(readOnly = true)
    public List<Budget> getBudgets() {
        return repository.getBudget();
    }

    @Transactional(readOnly = true)
    public List<Budget> getSpecificBudget(int id) {
        List<Budget> budgets= repository.getSpecificBudget(id);
        return budgets;
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpenses(int id) {
        List<Expense> expenses = repository.getExpenses(id);
        return expenses;
    }

    @Transactional
    public Budget saveBudget(String budgetName, java.sql.Date budgetStartDate, Date budgetEndDate, double budgetAmount) {
        return repository.saveBudget(new Budget(budgetName, budgetStartDate, budgetEndDate, budgetAmount));
    }

    @Transactional
    public Expense saveExpense(int budgetID, java.sql.Date expenseDate, String expenseCategory, double expenseAmount, String expenseComment) {
        return repository.saveExpense(new Expense(budgetID, expenseDate, expenseCategory, expenseAmount, expenseComment));
    }

    @Transactional
    public int deleteItem(String deleteFrom, int id) {
        int deletedID = repository.deleteItem(deleteFrom, id);
        return deletedID;
    }
}