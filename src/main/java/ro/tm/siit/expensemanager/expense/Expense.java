package ro.tm.siit.expensemanager.expense;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.logging.Logger;

/**
 * Expense class implements Serializable interface and models an expense
 * 
 * @author Radu
 *
 */
public class Expense implements Serializable {

    /**
     * creating an enum for type of expense
     * 
     * @author Radu
     *
     */
    public enum ExpenseType {
	DAILY, WEEKLY, MONTHLY, YEARLY;
    }

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    private String name;
    private float value;
    private LocalDate date;
    private ExpenseType expenseType;

    /**
     * constructor for expense
     * 
     * @param name
     *            - the name of expense
     * @param value
     *            - the value of expense
     * @param date
     *            - the date of expense
     * @param expenseType
     *            - the type of expense
     */
    public Expense(String name, float value, LocalDate date, ExpenseType expenseType) {
	LOGGER.fine("creating expense with name " + name + " with value " + value + " at " + date + " with type "
		+ expenseType);
	this.name = name;
	this.value = value;
	this.date = date;
	this.expenseType = expenseType;
	LOGGER.info("expense with name " + name + " created");
    }

    /**
     * returns the type of expense
     * 
     * @return the expenseType
     */
    public ExpenseType getExpenseType() {
	LOGGER.fine("type " + expenseType + " of expense " + name + " returned");
	return expenseType;
    }

    /**
     * returns the value of expense
     * 
     * @return the value
     */
    public float getValue() {
	LOGGER.fine("value " + value + " of expense " + name + " returned");
	return value;
    }

    /**
     * returns the name of expense
     * 
     * @return the name
     */
    public String getName() {
	LOGGER.fine("name of expense " + name + " returned");
	return name;
    }

    /**
     * returns the date of expense
     * 
     * @return the date
     */
    public LocalDate getDate() {
	LOGGER.fine("date " + date + " of expense " + name + " returned");
	return date;
    }

    /**
     * calculates the value per month of this expense
     * 
     * @param month
     *            the month for which is calculated
     * @return the value per month
     */
    public double getValuePerMonth(Month month) {
	LOGGER.fine("calculating value per month " + month + " for expense " + name);
	double valuePerMonth = 0;
	switch (expenseType) {
	case YEARLY:
	    if (date.getMonthValue() == month.getValue()) {
		valuePerMonth = value;
	    }
	    break;
	case MONTHLY:
	    valuePerMonth = value;
	    break;
	case WEEKLY:
	    valuePerMonth = value * 4;
	    break;
	case DAILY:
	    valuePerMonth = value * 31;
	    break;
	}
	LOGGER.info("value per month of expense " + name + " is " + valuePerMonth);
	return valuePerMonth;
    }

    /**
     * calculates the value per year of this expense
     * 
     * @return the value per year
     */
    public double getValuePerYear() {
	LOGGER.fine("calculating value per year for expense " + name);
	double valuePerYear = 0;
	switch (expenseType) {
	case YEARLY:
	    valuePerYear = value;
	    break;
	case MONTHLY:
	    valuePerYear = value * 12;
	    break;
	case WEEKLY:
	    valuePerYear = value * 52;
	    break;
	case DAILY:
	    valuePerYear = value * 365;
	    break;
	}
	LOGGER.info("value per year of expense " + name + " is " + valuePerYear);
	return valuePerYear;
    }
}
