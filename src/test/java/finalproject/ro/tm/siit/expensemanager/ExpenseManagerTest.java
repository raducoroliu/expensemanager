package finalproject.ro.tm.siit.expensemanager;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;

import ro.tm.siit.expensemanager.expense.Expense;
import ro.tm.siit.expensemanager.expense.Expense.ExpenseType;
import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
 * Testing the methods of ExpenseManager class
 * 
 * @author Radu
 *
 */
public class ExpenseManagerTest {
    private ExpenseManager expenseManager;
    private List<Expense> expenses;

    /**
     * Creates some expenses, add them to expenses list and creates an
     * expenseManager
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	Expense e1 = new Expense("cofee", 5, LocalDate.of(2015, 5, 20), ExpenseType.DAILY);
	Expense e2 = new Expense("beer", 40, LocalDate.of(2015, 2, 12), ExpenseType.WEEKLY);
	Expense e3 = new Expense("gas", 300, LocalDate.of(2015, 4, 8), ExpenseType.MONTHLY);
	Expense e4 = new Expense("vacation", 3000, LocalDate.of(2015, 8, 30), ExpenseType.YEARLY);
	expenses = new ArrayList<Expense>();
	expenses.add(e1);
	expenses.add(e2);
	expenses.add(e3);
	expenses.add(e4);
	expenseManager = new ExpenseManager(expenses);
    }

    /**
     * Tests whether the expense is added without name
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddExpenseWithNullName() {
	DefaultTableModel model = new DefaultTableModel();
	Expense e = new Expense("", 8, LocalDate.of(2015, 10, 22), ExpenseType.DAILY);
	expenseManager.addExpense(e, model);
    }

    /**
     * Tests whether the expense is added with negative value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddExpenseWithNegativeValue() {
	DefaultTableModel model = new DefaultTableModel();
	Expense e = new Expense("bread", -8, LocalDate.of(2015, 10, 22), ExpenseType.DAILY);
	expenseManager.addExpense(e, model);
    }

    /**
     * Tests whether the expense is added with date in the future
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddExpenseWithFutureDate() {
	DefaultTableModel model = new DefaultTableModel();
	Expense e = new Expense("bread", 8, LocalDate.of(2017, 10, 22), ExpenseType.DAILY);
	expenseManager.addExpense(e, model);
    }

    /**
     * Tests whether the expense is added with null budget
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddExpenseWithNullBudget() {
	DefaultTableModel model = new DefaultTableModel();
	Expense e = new Expense("bread", 8, LocalDate.of(2015, 10, 22), ExpenseType.DAILY);
	expenseManager.addExpense(e, model);
    }

    /**
     * Tests whether the expense is added correctly
     */
    @Test()
    public void testAddExpense() {
	DefaultTableModel model = new DefaultTableModel();
	Expense e = new Expense("bread", 8, LocalDate.of(2015, 10, 22), ExpenseType.DAILY);
	expenseManager.setBudgetPerMonth(2500);
	expenseManager.addExpense(e, model);
	assertEquals(5, expenses.size(), 0);
    }

    /**
     * Tests whether the budget is set with negative value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetBudgetPerMonthNegative() {
	float budget = -200;
	expenseManager.setBudgetPerMonth(budget);
    }

    /**
     * Tests whether the budget is set with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetBudgetPerMonthNull() {
	float budget = 0;
	expenseManager.setBudgetPerMonth(budget);
    }

    /**
     * Tests whether the budget is set correctly
     */
    @Test()
    public void testSetBudgetPerMonth() {
	float budget = 3000;
	expenseManager.setBudgetPerMonth(budget);
	assertEquals(3000, expenseManager.getBudgetPerMonth(), 0);
    }

    /**
     * Tests whether the biggest expense per year is calculated for a year
     * without expenses
     */
    @Test(expected = NullPointerException.class)
    public void testGetBiggestPerYearNull() {
	Year year = Year.of(2014);
	expenseManager.getBiggestPerYear(year);
    }

    /**
     * Tests whether the biggest expense per year is calculated correctly
     */
    @Test()
    public void testGetBiggestPerYear() {
	Year year = Year.of(2015);
	List<Expense> list = expenseManager.getBiggestPerYear(year);
	assertEquals("gas", list.get(0).getName());
    }

    /**
     * Tests whether the biggest expense per month is calculated for a month
     * without expenses
     */
    @Test(expected = NullPointerException.class)
    public void testGetBiggestPerMonthNull() {
	YearMonth yearMonth = YearMonth.of(2015, 1);
	expenseManager.getBiggestPerMonth(yearMonth);
    }

    /**
     * Tests whether the biggest expense per month is calculated for a month
     * with a yearly expense
     */
    @Test()
    public void testGetBiggestPerMonthWithYearly() {
	YearMonth yearMonth = YearMonth.of(2015, 8);
	List<Expense> list = expenseManager.getBiggestPerMonth(yearMonth);
	assertEquals("vacation", list.get(0).getName());
    }

    /**
     * Tests whether the biggest expense per month is calculated correctly
     */
    @Test()
    public void testGetBiggestPerMonth() {
	YearMonth yearMonth = YearMonth.of(2015, 6);
	List<Expense> list = expenseManager.getBiggestPerMonth(yearMonth);
	assertEquals("gas", list.get(0).getName());
    }

    /**
     * Tests whether the forecast per month is calculated correctly for a month
     * with a yearly expense
     */
    @Test()
    public void testGetForecastPerMonthWithYearly() {
	YearMonth yearMonth = YearMonth.of(2016, 8);
	double forecast = expenseManager.getForecastPerMonth(yearMonth);
	assertEquals(3795.75, forecast, 0.01);
    }

    /**
     * Tests whether the forecast per month is calculated for a month without
     * expenses
     */
    @Test()
    public void testGetForecastPerMonthNull() {
	YearMonth yearMonth = YearMonth.of(2015, 12);
	double forecast = expenseManager.getForecastPerMonth(yearMonth);
	assertEquals(0, forecast, 0);
    }

    /**
     * Tests whether the forecast per month is calculated correctly
     */
    @Test()
    public void testGetForecastPerMonth() {
	YearMonth yearMonth = YearMonth.of(2016, 4);
	double forecast = expenseManager.getForecastPerMonth(yearMonth);
	assertEquals(483, forecast, 0);
    }

    /**
     * Tests whether the forecast per year is calculated for a year without
     * expenses
     */
    @Test()
    public void testGetForecastPerYearNull() {
	Year year = Year.of(2015);
	double forecast = expenseManager.getForecastPerYear(year);
	assertEquals(0, forecast, 0);
    }

    /**
     * Tests whether the forecast per year is calculated correctly
     */
    @Test()
    public void testGetForecastPerYear() {
	Year year = Year.of(2016);
	double forecast = expenseManager.getForecastPerYear(year);
	assertEquals(11030.25, forecast, 0.01);
    }

    /**
     * Tests whether the current value of expenses per month is calculated
     * correctly
     */
    @Test()
    public void testGetCurrentValuePerMonth() {
	YearMonth yearMonth = YearMonth.of(2015, 5);
	double currentValue = expenseManager.getCurrentValuePerMonth(yearMonth);
	assertEquals(615, currentValue, 0);
    }

    /**
     * Tests whether the list of expenses filtered by type and day is obtained
     * for a day in the future
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetExpensesByTypeAndDayInFuture() {
	String type = "DAILY";
	LocalDate date = LocalDate.of(2017, 5, 2);
	expenseManager.getExpensesByTypeAndDay(type, date);
    }

    /**
     * Tests whether the list of expenses filtered by type and day is obtained
     * for a day without expenses
     */
    @Test()
    public void testGetExpensesByTypeAndDayNull() {
	String type = "DAILY";
	LocalDate date = LocalDate.of(2015, 3, 25);
	List<Expense> expensesForDisplay = expenseManager.getExpensesByTypeAndDay(type, date);
	assertEquals(0, expensesForDisplay.size(), 0);
    }

    /**
     * Tests whether the list of expenses filtered by type and day is obtained
     * correctly
     */
    @Test()
    public void testGetExpensesByTypeAndDay() {
	String type = "DAILY";
	LocalDate date = LocalDate.of(2016, 2, 2);
	List<Expense> expensesForDisplay = expenseManager.getExpensesByTypeAndDay(type, date);
	assertEquals(1, expensesForDisplay.size(), 0);
    }

    /**
     * Tests whether the list of expenses filtered by type and month is obtained
     * for a month in the future
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetExpensesByTypeAndMonthInFuture() {
	String type = "MONTHLY";
	YearMonth yearMonth = YearMonth.of(2016, 12);
	expenseManager.getExpensesByTypeAndMonth(type, yearMonth);
    }

    /**
     * Tests whether the list of expenses filtered by type and month is obtained
     * for a month without expenses
     */
    @Test()
    public void testGetExpensesByTypeAndMonthNull() {
	String type = "MONTHLY";
	YearMonth yearMonth = YearMonth.of(2014, 12);
	List<Expense> expensesForDisplay = expenseManager.getExpensesByTypeAndMonth(type, yearMonth);
	assertEquals(0, expensesForDisplay.size(), 0);
    }

    /**
     * Tests whether the list of expenses filtered by type and month is obtained
     * correctly
     */
    @Test()
    public void testGetExpensesByTypeAndMonth() {
	String type = "MONTHLY";
	YearMonth yearMonth = YearMonth.of(2015, 6);
	List<Expense> expensesForDisplay = expenseManager.getExpensesByTypeAndMonth(type, yearMonth);
	assertEquals(1, expensesForDisplay.size(), 0);
    }

    /**
     * Tests whether the list of expenses filtered by type and year is obtained
     * for a year in the future
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetExpensesByTypeAndYearInFuture() {
	String type = "WEEKLY";
	Year year = Year.of(2020);
	expenseManager.getExpensesByTypeAndYear(type, year);
    }

    /**
     * Tests whether the list of expenses filtered by type and year is obtained
     * for a year without expenses
     */
    @Test()
    public void testGetExpensesByTypeAndYearNull() {
	String type = "WEEKLY";
	Year year = Year.of(2014);
	List<Expense> expensesForDisplay = expenseManager.getExpensesByTypeAndYear(type, year);
	assertEquals(0, expensesForDisplay.size(), 0);
    }

    /**
     * Tests whether the list of expenses filtered by type and year is obtained
     * correctly
     */
    @Test()
    public void testGetExpensesByTypeAndYear() {
	String type = "WEEKLY";
	Year year = Year.of(2015);
	List<Expense> expensesForDisplay = expenseManager.getExpensesByTypeAndYear(type, year);
	assertEquals(1, expensesForDisplay.size(), 0);
    }

    /**
     * Tests whether the list of expenses filtered by type is obtained correctly
     * for all types
     */
    @Test()
    public void testGetExpensesByTypeAll() {
	String type = "all types";
	List<Expense> expensesForDisplay = expenseManager.getExpensesByType(type);
	assertEquals(4, expensesForDisplay.size(), 0);
    }

    /**
     * Tests whether the list of expenses filtered by type is obtained correctly
     * for one types
     */
    @Test()
    public void testGetExpensesByType() {
	String type = "YEARLY";
	List<Expense> expensesForDisplay = expenseManager.getExpensesByType(type);
	assertEquals(1, expensesForDisplay.size(), 0);
    }
}
