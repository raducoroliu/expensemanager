package ro.tm.siit.expensemanager.expense;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.logging.Logger;

/**
 * Expense class implements Serializable interface and models an expense
 * 
 * @author Radu
 *
 */
public class Expense implements Serializable {

    /**
     * creating an enum for expense type
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
    public double getValuePerMonth(YearMonth yearMonth) {
	LOGGER.fine("calculating value per month " + yearMonth + " for expense " + name);
	double valuePerMonth = 0;
	if (date.getYear() == yearMonth.getYear() && date.getMonthValue() <= yearMonth.getMonthValue()) {
	    switch (expenseType) {
	    case YEARLY:
		if (date.getMonthValue() == yearMonth.getMonthValue()) {
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
	}
	LOGGER.info("value per month " + yearMonth + " of expense " + name + " is " + valuePerMonth);
	return valuePerMonth;
    }

    /**
     * calculates the value per year of this expense
     * 
     * @return the value per year
     */
    public double getValuePerYear(Year year) {
	LOGGER.fine("calculating value per year " + year + " for expense " + name);
	double valuePerYear = 0;
	if (date.getYear() == year.getValue()) {
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
	}
	LOGGER.info("value per year " + year + " of expense " + name + " is " + valuePerYear);
	return valuePerYear;
    }

    /**
     * Determines whether the expense exists in a specific date
     * 
     * @param day
     *            the specific date
     * @return true if exists and false is not exists
     */
    public boolean existsInDay(LocalDate day) {
	boolean isInDay = false;
	if (date.getYear() == day.getYear() && date.isBefore(day.plusDays(1))) {
	    switch (expenseType) {
	    case YEARLY:
		if (date.equals(day)) {
		    isInDay = true;
		}
		break;
	    case MONTHLY:
		if (date.equals(day) || day.getDayOfMonth() == 1) {
		    isInDay = true;
		}
		break;
	    case WEEKLY:
		if (date.equals(day) || day.getDayOfWeek().getValue() == 1) {
		    isInDay = true;
		}
		break;
	    case DAILY:
		isInDay = true;
		break;
	    }
	}
	return isInDay;
    }

    /**
     * Determines whether the expense exists in a specific month
     * 
     * @param yearMonth
     *            the specific month
     * @return true if exists and false is not exists
     */
    public boolean existsInMonth(YearMonth yearMonth) {
	boolean isInMonth = false;
	if (date.getYear() == yearMonth.getYear() && YearMonth.from(date).isBefore(yearMonth.plusMonths(1))) {
	    switch (expenseType) {
	    case YEARLY:
		if (date.getMonthValue() == yearMonth.getMonthValue()) {
		    isInMonth = true;
		}
		break;
	    case MONTHLY:
	    case WEEKLY:
	    case DAILY:
		isInMonth = true;
		break;
	    }
	}
	return isInMonth;
    }

    /**
     * Determines whether the expense exists in a specific year
     * 
     * @param year
     *            the specific year
     * @return true if exists and false is not exists
     */
    public boolean existsInYear(Year year) {
	boolean isInYear = false;
	if (date.getYear() == year.getValue()) {
	    isInYear = true;
	}
	return isInYear;
    }
}
