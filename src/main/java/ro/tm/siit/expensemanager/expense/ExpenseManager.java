package ro.tm.siit.expensemanager.expense;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import ro.tm.siit.expensemanager.expense.Expense.ExpenseType;
import ro.tm.siit.expensemanager.gui.AddExpense;
import ro.tm.siit.expensemanager.gui.SetBudget;

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
    public void addExpense(Expense expense, DefaultTableModel model, AddExpense addExpense) {
	LOGGER.fine("adding expense " + expense.getName() + " in expenses table model " + model.toString()
		+ " and messages will appear in window " + addExpense.getTitle());
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	double currentValue = this.getCurrentValuePerMonth(expense.getDate().getMonth())
		+ expense.getValuePerMonth(expense.getDate().getMonth());
	if (currentValue > this.getBudgetPerMonth()) {
	    int choice = JOptionPane.showOptionDialog(addExpense,
		    "The budget per month will be exceeded. Do you want to continue?", "Warning",
		    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

	    if (choice == JOptionPane.YES_OPTION) {
		LOGGER.fine("the budget per month exceeded");
		expenses.add(expense);
		Object[] rowData = new Object[] { expense.getName(), expense.getValue(),
			expense.getDate().format(formatter), expense.getExpenseType() };
		model.addRow(rowData);
		model.fireTableStructureChanged();
	    }
	} else {
	    expenses.add(expense);
	    Object[] rowData = new Object[] { expense.getName(), expense.getValue(),
		    expense.getDate().format(formatter), expense.getExpenseType() };
	    model.addRow(rowData);
	    model.fireTableStructureChanged();
	}
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
    public void setBudgetPerMonth(float budget, SetBudget setBudget) {
	LOGGER.fine("setting value " + budget + " to budget per month for expense and messages will appear in window "
		+ setBudget.getTitle());
	if (budget < 0) {
	    LOGGER.warning("cannot set the budget negative per month");
	    throw new IllegalArgumentException("Negative number is not allowed!");
	}
	if (budget == 0) {
	    LOGGER.warning("cannot set the null budget per month");
	    throw new IllegalArgumentException("Null budget is not allowed!");
	}
	if (budgetPerMonth > 0 && budget != budgetPerMonth) {
	    int choice = JOptionPane.showOptionDialog(setBudget, "The budget will be changed. Do you want to continue?",
		    "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	    if (choice == JOptionPane.NO_OPTION) {
		budget = budgetPerMonth;
		LOGGER.fine("the budget per month has been changed");
	    }
	}
	this.budgetPerMonth = budget;
	LOGGER.info("the budget per month " + budgetPerMonth + " has been set");
    }

    /**
     * gets the biggest expense for year
     * 
     * @param year
     *            the year for which calculates
     * @return the biggest expense
     */
    public List<Expense> getBiggestPerYear(Year year) {
	LOGGER.fine("getting value of biggest expense per year " + year.getValue());
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	Expense biggest = null;
	boolean isFirst = true;
	for (Expense e : expenses) {
	    if (e.getDate().getYear() > year.getValue()) {
		continue;
	    }
	    if (isFirst) {
		biggest = e;
		isFirst = false;
	    }
	    if (e.getValuePerYear() > biggest.getValuePerYear()) {
		biggest = e;
	    }
	}
	expensesForDisplay.add(biggest);
	LOGGER.info("the biggest expense per year is " + biggest.getName() + " and will be displayed it");
	return expensesForDisplay;
    }

    /**
     * gets the biggest expense per month
     * 
     * @param yearMonth
     *            the month and year for which calculates
     * @return the biggest expense
     */
    public List<Expense> getBiggestPerMonth(YearMonth yearMonth) {
	LOGGER.fine("getting value of biggest expense per month " + yearMonth.getMonth() + " and year "
		+ yearMonth.getYear());
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), yearMonth.lengthOfMonth());
	Expense biggest = null;
	boolean isFirst = true;
	for (Expense e : expenses) {
	    if (e.getDate().isAfter(date)) {
		continue;
	    }
	    if (isFirst) {
		biggest = e;
		isFirst = false;
	    }
	    if (e.getValuePerMonth(yearMonth.getMonth()) > biggest.getValuePerMonth(yearMonth.getMonth())) {
		biggest = e;
	    }
	}
	expensesForDisplay.add(biggest);
	LOGGER.info("the biggest expense per month is " + biggest.getName() + " and will be displayed it");
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
	LOGGER.fine("getting value of forecast per month " + yearMonth.getMonth() + " and year " + yearMonth.getYear());
	double forecast = 0;
	LocalDate date = LocalDate.of(yearMonth.getYear() - 1, yearMonth.getMonthValue() + 1, 1);
	for (Expense e : expenses) {
	    if (e.getDate().isBefore(date)) {
		forecast += e.getValuePerMonth(yearMonth.getMonth());
	    }
	}
	LOGGER.info("the forecast value of expenses per month is " + forecast * 1.05);
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
	LOGGER.fine("getting value of forecast per year " + year.getValue());
	double forecast = 0;
	for (Expense e : expenses) {
	    if (e.getDate().getYear() < (year.getValue())) {
		forecast += e.getValuePerYear();
	    }
	}
	LOGGER.info("the forecast value of expenses per year is " + forecast * 1.05);
	return forecast * 1.05;
    }

    /**
     * gets the current value per month of expenses
     * 
     * @param month
     *            the month which is calculated
     * @return the current value per month
     */
    public double getCurrentValuePerMonth(Month month) {
	LOGGER.fine("calculating current value of expenses per month " + month);
	double currentValue = 0;
	for (Expense e : expenses) {
	    currentValue += e.getValuePerMonth(month);
	}
	LOGGER.info("the current value of expenses per month is " + currentValue);
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
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	for (Expense e : expenses) {
	    if (!date.equals(e.getDate())) {
		continue;
	    }
	    if (type.equals("all types") || e.getExpenseType().equals(ExpenseType.valueOf(type))) {
		expensesForDisplay.add(e);
	    }
	}
	LOGGER.info("the list of " + expensesForDisplay.size()
		+ " expenses filtered by type and date is created and returned");
	return expensesForDisplay;
    }

    /**
     * gets the expenses list filtered by type and period
     * 
     * @param type
     *            the expense type to filtered
     * @param start
     *            the month and year period where it begins
     * @param to
     *            the month and year period where it ends
     * @return the expenses list for displayed
     */
    public List<Expense> getExpensesByTypeAndPeriod(String type, YearMonth start, YearMonth to) {
	LOGGER.fine(
		"creating the expenses list filtered by type " + type + " and the period from " + start + " to " + to);
	List<Expense> expensesForDisplay = new ArrayList<Expense>();
	for (Expense e : expenses) {
	    if (e.getDate().isBefore(start.atDay(1)) || e.getDate().isAfter(to.atDay(to.lengthOfMonth()))) {
		continue;
	    }
	    if (type.equals("all types") || e.getExpenseType().equals(ExpenseType.valueOf(type))) {
		expensesForDisplay.add(e);
	    }
	}
	LOGGER.info("the list of " + expensesForDisplay.size()
		+ " expenses filtered by type and period is created and returned");
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
