package com.sjung3;

import org.owasp.encoder.Encode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//The MVC controller class of the App
//Handles all DB requests

@Controller
public class AppController {

    /**
     * All rest API's lives here
     */
    //Instance of TodoService is injected
    private AppService appService;
    private Admin admin;

    public AppController(AppService appService, Admin admin) {
        this.appService = appService;
        this.admin = admin;
    }

    //Renders the web app
    @GetMapping("/myExpenses")
    public String index() {
        return "index";
    }

    //Calls appService method to retrieve all budget data from the database when user opens the web app
    /*
     * @return: List of all budgets from the DB to To index.js. Used for rendering on Welcome page
     */
    @GetMapping("/api/getBudgets")
    @ResponseBody
    public ResponseEntity<List<Budget>> getBudgets() {
        List<Budget> budgets = appService.getBudgets();
        return ResponseEntity.ok(budgets);
    }

    /*
     * Calls appService method to retrieve all expense data from the database
     * Adds them to an ArrayList in the admin class
     *
     * @param data budgetID from index.js to specify what expenses to retrieve
     * @return: List of all specified expenses to To index.js that is then rendered under category breakdown and date breakdown
     */
    @PostMapping("/api/getExpenses")
    @ResponseBody
    public ResponseEntity<List<Expense>> getExpenses(@RequestBody BudgetData data) {
        admin.clearExpenseArray();
        List<Expense> expenses = appService.getExpenses(data.getBudgetID());
        for (Expense exp : expenses) {
            admin.addToExpenses(exp);
        }
        Collections.sort(expenses, Comparator.comparing(Expense::getExpenseDate));
        return ResponseEntity.ok(expenses);
    }

    /**
     * @return Today's date to index.js
     */
    @GetMapping("/api/todaysDate")
    @ResponseBody
    public ResponseEntity<String> getTodaysDate() {
        return ResponseEntity.ok(admin.todaysDate().toString());
    }

    /**
     * Calls appService method to retrieve specified budget data
     * Sets the value of the variables that are used in methods in the Admin class
     *
     * @param data budgetID to be used to retrieve specified budget items from DB
     * @return A summery over the trip to index.js that renders the data for the trip summery
     */
    @PostMapping("/api/getSummery")
    @ResponseBody
    public ResponseEntity<String[]> getSummery(@RequestBody BudgetData data) {
        List<Budget> budgets = appService.getSpecificBudget(data.getBudgetID());
        for (Budget budget : budgets) {
            admin.setBudgetName(budget.getBudgetName());
            admin.setBudgetStartDate(budget.getBudgetStartDate().toLocalDate());
            admin.setBudgetEndDate(budget.getBudgetEndDate().toLocalDate());
            admin.setBudgetTotal(budget.getBudgetAmount());
        }
        String[] getSummery = admin.getSummery();
        return ResponseEntity.ok(getSummery);
    }

    /**
     * @return The total sum spent per each category, then used in index.js to render
     */
    @PostMapping("/api/getCategorySummery")
    @ResponseBody
    public ResponseEntity<String[]> getSpentPerCategory() {
        return ResponseEntity.ok(admin.spentPerCategory());
    }

    /**
     * @return The total sum spent per day, then used in index.js to render
     */
    @PostMapping("/api/getSpentPerDay")
    @ResponseBody
    public ResponseEntity<ArrayList<String>> getSpentPerDay(@RequestBody BudgetData data) {
        return ResponseEntity.ok(admin.spentPerDay());
    }

    /**
     *
     * @param data is the user input when user adds a new budget
     * @return Budget object used to createElements in index.js
     */
    @PostMapping("/api/saveBudget")
    public ResponseEntity<Budget> saveBudget(@RequestBody BudgetData data) {
        Budget saved = appService.saveBudget(data.getBudgetName(), data.getBudgetStartDate(), data.getBudgetEndDate(), data.getBudgetAmount());
        return ResponseEntity.ok(saved);
    }

    /**
     *
     * @param data is the user input when user adds a new expense
     * @return Expense object used to createELements in index.js
     */
    @PostMapping("/api/saveExpense")
    public ResponseEntity<Expense> saveExpense(@RequestBody ExpenseData data) {
        Expense saved = appService.saveExpense(data.getBudgetID(), data.getExpenseDate(),
                data.getExpenseCategory(), data.getExpenseAmount(), data.getExpenseComment());
        return ResponseEntity.ok(saved);
    }

    /**
     *
     * @param data budgetID from index.js that is used to specify what budget item to be deleted from the DB
     * @return budgetID to index.js to confirm it has been deleted
     */
    @PostMapping("/api/deleteBudget")
    @ResponseBody
    public ResponseEntity<Integer> deleteBudget(@RequestBody BudgetData data) {
        int budgetID = appService.deleteItem("budget", data.getBudgetID());
        return ResponseEntity.ok(budgetID);
    }

    /**
     *
     * @param data expenseID from index.js that is used to specify what expense item to be deleted from the DB
     * @return expenseID to index.js to confirm it has been deleted
     */
    @PostMapping("/api/deleteExpense")
    @ResponseBody
    public ResponseEntity<Integer> deleteExpense(@RequestBody ExpenseData data) {
        int expenseID = appService.deleteItem("expense", data.getExpenseID());
        return ResponseEntity.ok(expenseID);
    }
}

