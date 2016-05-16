package ro.tm.siit.expensemanager.expense;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

import ro.tm.siit.expensemanager.expense.Expense.ExpenseType;

/**
 * ExpenseManager class implements Serializable interface and models an expenses
 * manager
 * 
 * @author Radu
 *
 */
public class ExpenseManager implements Serializable {

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    private List<Expense> expenses;
    private float budgetPerMonth = 0;

    /**
     * constructor for expenses manager
     * 
     * @param expenses
     *            the list of expenses
     */
    public ExpenseManager(List<Expense> expenses) {
	this.expenses = expenses;
	LOGGER.info("the expenses list with " + expenses.size() + " has beed added to expense manager");
    }

    /**
     * add an expense to expenses list
     * 
     * @param expense
     *            the expense will be added
     * @param model
     *            the model of expenses table
     * @param addExpense
     *            the dialog window where are showing the messages
     */
    public void addExpense(Expense expense, DefaultTableModel model) {
	LOGGER.fine("adding expense " + expense.getName() + " in expenses table with model " + model.toString());
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	if (expense.getName().equals("")) {
	    LOGGER.warning("cannot set a null name of expense");
	    throw new IllegalArgumentException("Please enter a name of expense!");
	}
	if (expense.getValue() <= 0) {
	    LOGGER.warning("cannot set a negative number to value");
	    throw new IllegalArgumentException("Incorrect value! Value must be positive");
	}
	if (expense.getDate().isAfter(LocalDate.now())) {
	    LOGGER.warning("cannot set a date in the future");
	    throw new IllegalArgumentException("Date in the future is not possible!");
	}
	if (budgetPerMonth == 0) {
	    throw new IllegalArgumentException(
		    "The budget per month has not been set yet. Please set the budget first!");
	}
	expenses.add(expense);
	Object[] rowData = new Object[] { expense.getName(), expense.getValue(), expense.getDate().format(formatter),
		expense.getExpenseType() };
	model.addRow(rowData);
	model.fireTableStructureChanged();
	LOGGER.info("the expense " + expense.getName() + " has beed added");
    }

    /**
     * gets the budget per month
     * 
     * @return the budgetPerMonth
     */
    public float getBudgetPerMonth() {
	LOGGER.fine("getting budget per month");
	return this.budgetPerMonth;
    }

    /**
     * sets the budget per month
     * 
     * @param budgetPerMonth
     *            the budgetPerMonth to set
     */
    public void setBudgetPerMonth(float budget) {
	LOGGER.fine("setting value " + budget + " to budget per month for expense");
	if (budget < 0) {
	    LOGGER.warning("cannot set a negative budget per month");
	    throw new IllegalArgumentException("Negative budget is not allowed!");
	}
	if (budget == 0) {
	    LOGGER.warning("cannot set a null budget per month");
	    throw new IllegalArgumentException("Null budget is not allowed!");
	}
	this.budgetPerMonth = budget;
	LOGGER.info("the budget per month " + budgetPerMonth + " has been set");
    }

    /**
     * gets the biggest expense per month
     * 
     * @param yearMonth
     *            the month and year for which calculates
     * @return the biggest expense
     */
    public List<Expense> getBiggestPerMonth(YearMonth yearMonth) {
	LOGGER.fine("getting value of biggest expense per month " + yearMonth);
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	Expense biggest = null;
	boolean isFirst = true;
	for (Expense e : expenses) {
	    if (e.getValuePerMonth(yearMonth) > 0) {
		if (isFirst) {
		    biggest = e;
		    isFirst = false;
		}
		if (e.getValuePerMonth(yearMonth) > biggest.getValuePerMonth(yearMonth)) {
		    biggest = e;
		}
	    }
	}
	if (biggest == null) {
	    LOGGER.info("there are not expenses in month " + yearMonth);
	    throw new NullPointerException("There are not expenses in month " + yearMonth);
	} else {
	    expensesForDisplay.add(biggest);
	    LOGGER.info("the biggest expense per month " + yearMonth + " is " + biggest.getName()
		    + " and will be displayed it");
	}
	return expensesForDisplay;
    }

    /**
     * gets the biggest expense for year
     * 
     * @param year
     *            the year for which calculates
     * @return the biggest expense
     */
    public List<Expense> getBiggestPerYear(Year year) {
	LOGGER.fine("getting value of biggest expense per year " + year);
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	Expense biggest = null;
	boolean isFirst = true;
	for (Expense e : expenses) {
	    if (e.getValuePerYear(year) > 0) {
		if (isFirst) {
		    biggest = e;
		    isFirst = false;
		}
		if (e.getValuePerYear(year) > biggest.getValuePerYear(year)) {
		    biggest = e;
		}
	    }
	}
	if (biggest == null) {
	    LOGGER.info("there are not expenses in year " + year);
	    throw new NullPointerException("There are not expenses in year " + year);
	} else {
	    expensesForDisplay.add(biggest);
	    LOGGER.info(
		    "the biggest expense per year " + year + " is " + biggest.getName() + " and will be displayed it");
	}
	return expensesForDisplay;
    }

    /**
     * gets the forecast per month
     * 
     * @param yearMonth
     *            the month and year for which calculates
     * @return the forecast value
     */
    public double getForecastPerMonth(YearMonth yearMonth) {
	LOGGER.fine("getting value of forecast per month " + yearMonth);
	double forecast = 0;
	for (Expense e : expenses) {
	    forecast += e.getValuePerMonth(yearMonth.minusYears(1));
	}
	LOGGER.info("the forecast value of expenses per month " + yearMonth + " is " + (forecast * 1.05));
	return forecast * 1.05;
    }

    /**
     * gets the forecast per year
     * 
     * @param year
     *            the year to calculated
     * @return the forecast value
     */
    public double getForecastPerYear(Year year) {
	LOGGER.fine("getting value of forecast per year " + year);
	double forecast = 0;
	for (Expense e : expenses) {
	    forecast += e.getValuePerYear(year.minusYears(1));
	}
	LOGGER.info("the forecast value of expenses per year " + year + " is " + (forecast * 1.05));
	return forecast * 1.05;
    }

    /**
     * gets the current value per month of expenses
     * 
     * @param month
     *            the month which is calculated
     * @return the current value per month
     */
    public double getCurrentValuePerMonth(YearMonth yearMonth) {
	LOGGER.fine("calculating current value of expenses per month " + yearMonth);
	double currentValue = 0;
	for (Expense e : expenses) {
	    currentValue += e.getValuePerMonth(yearMonth);
	}
	LOGGER.info("the current value of expenses per month " + yearMonth + " is " + currentValue);
	return currentValue;
    }

    /**
     * models expenses table for display on the screen
     * 
     * @param model
     *            the table model
     * @param expensesForDisplay
     *            the expenses list for displayed
     */
    public void displayExpenses(DefaultTableModel model, List<Expense> expensesForDisplay) {
	LOGGER.fine("displaying list espenses with " + expensesForDisplay.size() + " expenses in table model "
		+ model.toString());
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	model.setColumnCount(0);
	model.setRowCount(0);
	model.addColumn("Name");
	model.addColumn("Value");
	model.addColumn("Date");
	model.addColumn("Type");
	for (Expense e : expensesForDisplay) {
	    Object[] rowData = new Object[] { e.getName(), e.getValue(), e.getDate().format(formatter),
		    e.getExpenseType() };
	    model.addRow(rowData);
	}
	model.fireTableStructureChanged();
	LOGGER.info("the list of " + expensesForDisplay.size() + " expenses is displayed");
    }

    /**
     * gets the default expenses list
     * 
     * @return the expenses list for displayed
     */
    public List<Expense> getAll() {
	LOGGER.fine("getting the list of all expenses for display");
	return this.expenses;
    }

    /**
     * gets the expenses list filtered by type and day
     * 
     * @param type
     *            the expense type to filtered
     * @param date
     *            the date to filtered
     * @return the expenses list for displayed
     */
    public List<Expense> getExpensesByTypeAndDay(String type, LocalDate date) {
	LOGGER.fine("creating the expenses list filtered by type " + type + " and date " + date + " for display");
	if (date.isAfter(LocalDate.now())) {
	    LOGGER.warning("cannot set a date in the future");
	    throw new IllegalArgumentException("Date in the future is not possible!");
	}
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	List<Expense> eType = this.getExpensesByType(type);
	for (Expense e : eType) {
	    if (e.isMadeInDay(date)) {
		expensesForDisplay.add(e);
	    }
	}
	LOGGER.info("the list of " + expensesForDisplay.size()
		+ " expenses filtered by type and date is created and returned");
	return expensesForDisplay;
    }

    /**
     * gets the expenses list filtered by type and month
     * 
     * @param type
     *            the expense type to filtered
     * @param yearMont
     *            the month and year to filtered
     * @return the expenses list for displayed
     */
    public List<Expense> getExpensesByTypeAndMonth(String type, YearMonth yearMonth) {
	LOGGER.fine("creating the expenses list filtered by type " + type + " and the month " + yearMonth);
	if (yearMonth.isAfter(YearMonth.now())) {
	    LOGGER.warning("cannot set a month in the future");
	    throw new IllegalArgumentException("Month in the future is not possible!");
	}
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	List<Expense> eType = this.getExpensesByType(type);
	for (Expense e : eType) {
	    if (e.isMadeInMonth(yearMonth)) {
		expensesForDisplay.add(e);
	    }
	}
	LOGGER.info("the list of " + expensesForDisplay.size()
		+ " expenses filtered by type and month is created and returned");
	return expensesForDisplay;
    }

    /**
     * gets the expenses list filtered by type and year
     * 
     * @param type
     *            the expense type to filtered
     * @param year
     *            the year to filtered
     * @return the expenses list for displayed
     */
    public List<Expense> getExpensesByTypeAndYear(String type, Year year) {
	LOGGER.fine("creating the expenses list filtered by type " + type + " and the year " + year);
	if (year.isAfter(Year.now())) {
	    LOGGER.warning("cannot set a year in the future");
	    throw new IllegalArgumentException("Year in the future is not possible!");
	}
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	List<Expense> eType = this.getExpensesByType(type);
	for (Expense e : eType) {
	    if (e.isMadeInYear(year)) {
		expensesForDisplay.add(e);
	    }
	}
	LOGGER.info("the list of " + expensesForDisplay.size()
		+ " expenses filtered by type and year is created and returned");
	return expensesForDisplay;
    }

    /**
     * gets the list of expenses filtered by type
     * 
     * @param type
     *            the expense type to filtered
     * @return the expenses list for displayed
     */
    public List<Expense> getExpensesByType(String type) {
	LOGGER.fine("creating the expenses list filtered by type " + type + " for display");
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	if (type.equals("all types")) {
	    return expenses;
	} else {
	    for (Expense e : expenses) {
		if (e.getExpenseType().equals(ExpenseType.valueOf(type))) {
		    expensesForDisplay.add(e);
		}
	    }
	    LOGGER.info(
		    "the list of " + expensesForDisplay.size() + " expenses filtered by type is created and returned");
	    return expensesForDisplay;
	}
    }
}
