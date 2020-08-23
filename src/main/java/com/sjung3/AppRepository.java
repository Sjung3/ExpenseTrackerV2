package com.sjung3;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppRepository {
    /**
     * Handles all MySQL queries
     * Instance of sessionFactory is injected for a database connection
     */
    private SessionFactory sessionFactory;

    public AppRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Saves data to DB
     * session object opens a physicle connection with the DB
     *
     * @param budget the input from user when creating a new trip budget
     * @return budget to AppController
     */
    public Budget saveBudget(Budget budget) {
        Session session = sessionFactory.getCurrentSession();
        session.save(budget);
        return budget;
    }

    public Expense saveExpense(Expense expense) {
        Session session = sessionFactory.getCurrentSession();
        session.save(expense);
        return expense;
    }

    /**
     * @return A list of all budget items from the DB
     */
    public List<Budget> getBudget() {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Budget";
        Query<Budget> query = session.createQuery(hql, Budget.class);
        return query.list();
    }

    /**
     * @param id specifies what budget that should be retrieved from the DB
     * @return the budget item that was specified byt the id
     */
    public List<Budget> getSpecificBudget(int id) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Budget where budgetID = :id";
        Query<Budget> query = session.createQuery(hql, Budget.class);
        query.setParameter("id", id);
        return query.list();
    }

    /**
     *
     * @param id specifies what expenses that should be retrieved from the DB
     * @return the retrieved expense items
     */

    public List<Expense> getExpenses(int id) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "from Expense where budgetID = :id";
        Query<Expense> query = session.createQuery(hql, Expense.class);
        query.setParameter("id", id);
        return query.list();
    }

    /**
     * Deletes item from DB
     *
     * @param id  expenseID or budgetID to be deleted
     * @param deleteFrom if item is to be deleted from the expense or the budget table
     * @return The id that has been deleted
     */
    public int deleteItem(String deleteFrom, int id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = null;
       if (deleteFrom.equals("expense")) {
            query = session.createQuery("DELETE Expense WHERE id = :id");
        } else if (deleteFrom.equals("budget")){
            query = session.createQuery("DELETE Budget WHERE id = :id");
        }
        query.setParameter( "id", id);
        query.executeUpdate();
        return id;
    }
}
