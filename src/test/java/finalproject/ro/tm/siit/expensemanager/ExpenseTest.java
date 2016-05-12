package finalproject.ro.tm.siit.expensemanager;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Test;

import ro.tm.siit.expensemanager.expense.Expense;
import ro.tm.siit.expensemanager.expense.Expense.ExpenseType;

/**
 * Testing the methods of Expense class
 * 
 * @author Radu
 *
 */
public class ExpenseTest {

    /**
     * Tests whether the name is returned correctly
     */
    @Test
    public void testExpenseName() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals("Coffee", expense.getName());
    }

    /**
     * Tests whether the value is returned correctly
     */
    @Test
    public void testExpenseValue() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(5, expense.getValue(), 0);
    }

    /**
     * Tests whether the date is returned correctly
     */
    @Test
    public void testExpenseDate() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals("2016-05-01", expense.getDate().toString());
    }

    /**
     * Tests whether the expense type is returned correctly
     */
    @Test
    public void testExpenseType() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals("DAILY", expense.getExpenseType().name());
    }

    /**
     * Tests whether the expense value per month for daily type is returned
     * correctly
     */
    @Test
    public void testGetValuePerMonthDaily() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(155, expense.getValuePerMonth(Month.APRIL), 0);
    }

    /**
     * Tests whether the expense value per month for weekly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerMonthWeekly() {
	String newName = "beer";
	float newValue = 40;
	LocalDate newDate = LocalDate.of(2016, 5, 1);
	ExpenseType newType = ExpenseType.WEEKLY;
	Expense newExpense = new Expense(newName, newValue, newDate, newType);
	assertEquals(160, newExpense.getValuePerMonth(Month.APRIL), 0);
    }

    /**
     * Tests whether the expense value per month for monthly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerMonthMonthly() {
	String newName = "gas";
	float newValue = 300;
	LocalDate newDate = LocalDate.of(2016, 5, 30);
	ExpenseType newType = ExpenseType.MONTHLY;
	Expense newExpense = new Expense(newName, newValue, newDate, newType);
	assertEquals(300, newExpense.getValuePerMonth(Month.APRIL), 0);
    }

    /**
     * Tests whether the expense value per month for yearly type with same month
     * is returned correctly
     */
    @Test
    public void testGetValuePerMonthYearlySameMonth() {
	String newName = "Greece vacation";
	float newValue = 3000;
	LocalDate newDate = LocalDate.of(2015, 8, 10);
	ExpenseType newType = ExpenseType.YEARLY;
	Expense newExpense = new Expense(newName, newValue, newDate, newType);
	assertEquals(3000, newExpense.getValuePerMonth(Month.AUGUST), 0);
    }

    /**
     * Tests whether the expense value per month for yearly type with different
     * month is returned correctly
     */
    @Test
    public void testGetValuePerMonthYearlyDifferentMonth() {
	String newName = "Greece vacation";
	float newValue = 3000;
	LocalDate newDate = LocalDate.of(2015, 8, 10);
	ExpenseType newType = ExpenseType.YEARLY;
	Expense newExpense = new Expense(newName, newValue, newDate, newType);
	assertEquals(0, newExpense.getValuePerMonth(Month.SEPTEMBER), 0);
    }

    /**
     * Tests whether the expense value per year for daily type is returned
     * correctly
     */
    @Test
    public void testGetValuePerYearDaily() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(1825, expense.getValuePerYear(), 0);
    }

    /**
     * Tests whether the expense value per year for weekly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerYearWeekly() {
	String newName = "beer";
	float newValue = 40;
	LocalDate newDate = LocalDate.of(2016, 5, 1);
	ExpenseType newType = ExpenseType.WEEKLY;
	Expense newExpense = new Expense(newName, newValue, newDate, newType);
	assertEquals(2080, newExpense.getValuePerYear(), 0);
    }

    /**
     * Tests whether the expense value per year for monthly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerYearMonthly() {
	String newName = "gas";
	float newValue = 300;
	LocalDate newDate = LocalDate.of(2016, 5, 30);
	ExpenseType newType = ExpenseType.MONTHLY;
	Expense newExpense = new Expense(newName, newValue, newDate, newType);
	assertEquals(3600, newExpense.getValuePerYear(), 0);
    }

    /**
     * Tests whether the expense value per year for yearly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerYearYearly() {
	String newName = "Greece vacation";
	float newValue = 3000;
	LocalDate newDate = LocalDate.of(2015, 8, 10);
	ExpenseType newType = ExpenseType.YEARLY;
	Expense newExpense = new Expense(newName, newValue, newDate, newType);
	assertEquals(3000, newExpense.getValuePerYear(), 0);
    }
}
