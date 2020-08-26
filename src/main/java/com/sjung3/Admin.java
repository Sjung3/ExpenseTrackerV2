package com.sjung3;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class Admin {
    /**
     * Handles all calculations of data that is then used in index.js
     */
    DecimalFormat df = new DecimalFormat("#.00");
    private LocalDate budgetStartDate;
    private LocalDate budgetEndDate;
    private String budgetName;
    private double budgetTotal;

    private ArrayList<Expense> expenseArray;
    private TreeMap<Date, Double> dateMap;

    public Admin() {
        this.expenseArray = new ArrayList<Expense>();
        this.dateMap = new TreeMap<Date, Double>();
    }

    public ArrayList<Expense> getExpenseArray() {
        return expenseArray;
    }

    public void setBudgetStartDate(LocalDate budgetStartDate) {
        this.budgetStartDate = budgetStartDate;
    }

    public void setBudgetEndDate(LocalDate budgetEndDate) {
        this.budgetEndDate = budgetEndDate;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public void setBudgetTotal(double budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    public void addToExpenses(Expense exp) {
        expenseArray.add(exp);
    }

    public void clearExpenseArray() {
        expenseArray.clear();
    }

    public void clearDateMap() {
        dateMap.clear();
    }

    /**
     * @return Returns today's date
     * @usage daysOnTrip(), spentToday()
     */
    public LocalDate todaysDate() {
        return LocalDate.now();
    }

    /**
     * @return Returns a String Array containing a summery of the KPI's for the trip
     * @usage AppController.getSummery() (then returned  to index.js for rendering the summery of the trip)
     */
    //TODO changed this....
    public String[] getSummery() {
        return new String[]{budgetStartDate.toString(), budgetEndDate.toString(), budgetName,
                df.format(budgetTotal), spentToday(), dailyBudget(), df.format(avSpent()),
                df.format(totalSPent()), remaining(), String.valueOf(daysOnTrip())};
    }

    /**
     * @return Number of days budgeted
     * @usage dailyBudget(), daysOnTrip(), getSummery()
     */
    public int daysBudgeted() {
        return (int) ChronoUnit.DAYS.between(budgetStartDate, budgetEndDate) + 1;
    }

    /**
     * @return Number of days travelled
     * <p>
     * MaxDays ensures that maximum of days shown is no longer than days budgeted for
     * @usage avSpent(), getSPent()
     */
    public int daysOnTrip() {
        int maxDays = daysBudgeted();
        int daysTravelled = (int) ChronoUnit.DAYS.between(budgetStartDate, todaysDate()) + 1;
        if (daysTravelled > maxDays) {
            return maxDays;
        } else if (daysTravelled < 0) {
            return 0;
        }
        return daysTravelled;
    }

    /**
     * @return Returns average budget per day
     * @usage getSummery()
     */
    public String dailyBudget() {
        double budgetPerDay = 0;
        budgetPerDay = budgetTotal / daysBudgeted();
        return df.format(budgetPerDay);
    }

    /**
     * @return Returns total spent on trip
     * @usage getSummery(), avSpent(), remaining()
     */
    public double totalSPent() {
        return expenseArray.stream()
                .mapToDouble(Expense::getExpenseAmount)
                .sum();
    }

    /**
     * @return Returns how much is left of budget
     * @usage getSummery()
     */
    public String remaining() {
        return df.format(budgetTotal - totalSPent());
    }

    /**
     * @return Returns average spent per day
     * @usage getSummery()
     */
    public double avSpent() {
        double avSpent = 0;
        if (daysOnTrip() != 0) {
            avSpent = totalSPent() / daysOnTrip();
        }
        return avSpent;
    }

    /**
     * Compares the date of spent with today's date and sums the values if there has been anything spent today
     *
     * @return Returns the sum of what has been spent today
     * @usage getSummery()
     */
    public String spentToday() {
        double spentToday = 0;
        for (Expense expense : expenseArray) {
            if (expense.getExpenseDate().toLocalDate().equals(todaysDate())) {
                spentToday += expense.getExpenseAmount();
            }
        }
        return df.format(spentToday);
    }

    /**
     * @return Returns a String Array containing the sum of each category
     * @usage getSummery()
     */

    //TODO CHANGE TO SWITCH (FROM IF)
    public String[] spentPerCategory() {
        double food = 0;
        double entertainment = 0;
        double accommodation = 0;
        double transport = 0;
        double misc = 0;
        for (Expense expense : expenseArray) {
            if (expense.getExpenseCategory().equals("food")) {
                food += expense.getExpenseAmount();
            } else if (expense.getExpenseCategory().equals("entertainment")) {
                entertainment += expense.getExpenseAmount();
            } else if (expense.getExpenseCategory().equals("accommodation")) {
                accommodation += expense.getExpenseAmount();
            } else if (expense.getExpenseCategory().equals("transport")) {
                transport += expense.getExpenseAmount();
            } else if (expense.getExpenseCategory().equals("misc")) {
                misc += expense.getExpenseAmount();
            } else {
                System.out.println("Something went wrong");
            }
        }
        return new String[]{"Food €" + df.format(food), "Entertainment €" + df.format(entertainment), "Accommodation €" +
                df.format(accommodation), "Transport €" + df.format(transport), "Misc €" + df.format(misc)};
    }

    /**
     * @return Returns a String ArrayList containing the sum of spent each day
     * @usage getSummery()
     */
    public ArrayList<String> spentPerDay() {
        clearDateMap();
        ArrayList<String> spentPerDay = new ArrayList<String>();

        for (Expense expense : expenseArray) {
            dateMap.put(expense.getExpenseDate(), 0.0);
        }
        for (Expense expense : expenseArray) {
            double dateSum = dateMap.get(expense.getExpenseDate());
            dateMap.put(expense.getExpenseDate(), dateSum + expense.getExpenseAmount());
        }

        Set set = dateMap.entrySet();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry) itr.next();
            String dateExp = me.getKey() + " € " + df.format(me.getValue());
            spentPerDay.add(dateExp);
        }
        return spentPerDay;
    }
}