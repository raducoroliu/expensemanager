package finalproject.ro.tm.siit.expensemanager;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

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
     * Tests whether the expense value per month for daily type for a month
     * after expense month is returned correctly
     */
    @Test
    public void testGetValuePerMonthDailyForBeforeDay() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(0, expense.getValuePerMonth(YearMonth.of(2016, 4)), 0);
    }

    /**
     * Tests whether the expense value per month for daily type for a different
     * year is returned correctly
     */
    @Test
    public void testGetValuePerMonthDailyDifferentYear() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(0, expense.getValuePerMonth(YearMonth.of(2015, 12)), 0);
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
	assertEquals(155, expense.getValuePerMonth(YearMonth.of(2016, 6)), 0);
    }

    /**
     * Tests whether the expense value per month for weekly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerMonthWeekly() {
	String name = "beer";
	float value = 40;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.WEEKLY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(160, expense.getValuePerMonth(YearMonth.of(2016, 6)), 0);
    }

    /**
     * Tests whether the expense value per month for monthly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerMonthMonthly() {
	String name = "gas";
	float value = 300;
	LocalDate date = LocalDate.of(2016, 5, 30);
	ExpenseType type = ExpenseType.MONTHLY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(300, expense.getValuePerMonth(YearMonth.of(2016, 6)), 0);
    }

    /**
     * Tests whether the expense value per month for yearly type with same month
     * is returned correctly
     */
    @Test
    public void testGetValuePerMonthYearlySameMonth() {
	String name = "Greece vacation";
	float value = 3000;
	LocalDate date = LocalDate.of(2015, 8, 10);
	ExpenseType type = ExpenseType.YEARLY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(3000, expense.getValuePerMonth(YearMonth.of(2015, 8)), 0);
    }

    /**
     * Tests whether the expense value per month for yearly type with different
     * month is returned correctly
     */
    @Test
    public void testGetValuePerMonthYearlyDifferentMonth() {
	String name = "Greece vacation";
	float value = 3000;
	LocalDate date = LocalDate.of(2015, 8, 10);
	ExpenseType type = ExpenseType.YEARLY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(0, expense.getValuePerMonth(YearMonth.of(2015, 11)), 0);
    }

    /**
     * Tests whether the expense value per year for daily type for a different
     * year is returned correctly
     */
    @Test
    public void testGetValuePerYearDailyForDifferentYear() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(0, expense.getValuePerYear(Year.of(2015)), 0);
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
	assertEquals(1825, expense.getValuePerYear(Year.of(2016)), 0);
    }

    /**
     * Tests whether the expense value per year for weekly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerYearWeekly() {
	String name = "beer";
	float value = 40;
	LocalDate date = LocalDate.of(2016, 5, 1);
	ExpenseType type = ExpenseType.WEEKLY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(2080, expense.getValuePerYear(Year.of(2016)), 0);
    }

    /**
     * Tests whether the expense value per year for monthly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerYearMonthly() {
	String name = "gas";
	float value = 300;
	LocalDate date = LocalDate.of(2016, 5, 30);
	ExpenseType type = ExpenseType.MONTHLY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(3600, expense.getValuePerYear(Year.of(2016)), 0);
    }

    /**
     * Tests whether the expense value per year for yearly type is returned
     * correctly
     */
    @Test
    public void testGetValuePerYearYearly() {
	String name = "Greece vacation";
	float value = 3000;
	LocalDate date = LocalDate.of(2015, 8, 10);
	ExpenseType type = ExpenseType.YEARLY;
	Expense expense = new Expense(name, value, date, type);
	assertEquals(3000, expense.getValuePerYear(Year.of(2015)), 0);
    }

    /**
     * Tests whether the expense with daily type was made in a day before
     * expense day
     */
    @Test
    public void testExistsInDayDailyForBeforeDay() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2016, 4, 12);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertFalse(expense.existsInDay(LocalDate.of(2016, 4, 11)));
    }

    /**
     * Tests whether the expense with daily type was made in a different year
     */
    @Test
    public void testExistsInDayDailyDiffernetYear() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2015, 5, 11);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertFalse(expense.existsInDay(LocalDate.of(2016, 6, 21)));
    }

    /**
     * Tests whether the expense with daily type was made in a specific day,
     * same year and after expense day
     */
    @Test
    public void testExistsInDayDaily() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2015, 5, 15);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertTrue(expense.existsInDay(LocalDate.of(2015, 6, 21)));
    }

    /**
     * Tests whether the expense with weekly type was made in a different day
     * from the expense day
     */
    @Test
    public void testExistsInDayWeeklyDifferentDay() {
	String name = "beer";
	float value = 40;
	LocalDate date = LocalDate.of(2016, 3, 18);
	ExpenseType type = ExpenseType.WEEKLY;
	Expense expense = new Expense(name, value, date, type);
	assertFalse(expense.existsInDay(LocalDate.of(2016, 3, 19)));
    }

    /**
     * Tests whether the expense with weekly type was made in same day whit
     * expense day
     */
    @Test
    public void testExistsInDayWeeklySameDay() {
	String name = "beer";
	float value = 40;
	LocalDate date = LocalDate.of(2016, 3, 18);
	ExpenseType type = ExpenseType.WEEKLY;
	Expense expense = new Expense(name, value, date, type);
	assertTrue(expense.existsInDay(LocalDate.of(2016, 3, 18)));
    }

    /**
     * Tests whether the expense with weekly type was made in a MONDAY
     */
    @Test
    public void testExistsInDayWeeklyForMonday() {
	String name = "beer";
	float value = 40;
	LocalDate date = LocalDate.of(2016, 3, 18);
	ExpenseType type = ExpenseType.WEEKLY;
	Expense expense = new Expense(name, value, date, type);
	// choose a day that is Monday
	assertTrue(expense.existsInDay(LocalDate.of(2016, 4, 4)));
    }

    /**
     * Tests whether the expense with monthly type was made in a different day
     */
    @Test
    public void testExistsInDayMontlyDiferentDay() {
	String name = "gas";
	float value = 300;
	LocalDate date = LocalDate.of(2016, 1, 30);
	ExpenseType type = ExpenseType.MONTHLY;
	Expense expense = new Expense(name, value, date, type);
	assertFalse(expense.existsInDay(LocalDate.of(2016, 2, 2)));
    }

    /**
     * Tests whether the expense with monthly type was made in same day with
     * expense day
     */
    @Test
    public void testExistsInDayMonthlySameDay() {
	String name = "gas";
	float value = 300;
	LocalDate date = LocalDate.of(2016, 1, 30);
	ExpenseType type = ExpenseType.MONTHLY;
	Expense expense = new Expense(name, value, date, type);
	assertTrue(expense.existsInDay(LocalDate.of(2016, 1, 30)));
    }

    /**
     * Tests whether the expense with monthly type was made in the first day of
     * month
     */
    @Test
    public void testExistsInDayMonthlyForFirstDayInMonth() {
	String name = "gas";
	float value = 300;
	LocalDate date = LocalDate.of(2016, 1, 30);
	ExpenseType type = ExpenseType.MONTHLY;
	Expense expense = new Expense(name, value, date, type);
	// choose a day that is the first day in month
	assertTrue(expense.existsInDay(LocalDate.of(2016, 4, 1)));
    }

    /**
     * Tests whether the expense with yearly type was made in same day with
     * expense day
     */
    @Test
    public void testExistsInDayYearlyForSameDay() {
	String name = "Greece vacation";
	float value = 3000;
	LocalDate date = LocalDate.of(2015, 8, 10);
	ExpenseType type = ExpenseType.YEARLY;
	Expense expense = new Expense(name, value, date, type);
	assertTrue(expense.existsInDay(LocalDate.of(2015, 8, 10)));
    }

    /**
     * Tests whether the expense with yearly type was made in different day
     */
    @Test
    public void testExistsInDayYearlyForDifferentDay() {
	String name = "Greece vacation";
	float value = 3000;
	LocalDate date = LocalDate.of(2015, 8, 10);
	ExpenseType type = ExpenseType.YEARLY;
	Expense expense = new Expense(name, value, date, type);
	assertFalse(expense.existsInDay(LocalDate.of(2015, 10, 21)));
    }

    /**
     * Tests whether the expense with monthly type was made in a specific month
     */
    @Test
    public void testExistsInMonthMonthly() {
	String name = "gas";
	float value = 300;
	LocalDate date = LocalDate.of(2016, 1, 30);
	ExpenseType type = ExpenseType.MONTHLY;
	Expense expense = new Expense(name, value, date, type);
	assertTrue(expense.existsInMonth(YearMonth.of(2016, 4)));
    }

    /**
     * Tests whether the expense with yearly type was made in same month with
     * expense month
     */
    @Test
    public void testExistsInMonthYearlyForSameMonth() {
	String name = "Greece vacation";
	float value = 3000;
	LocalDate date = LocalDate.of(2015, 8, 10);
	ExpenseType type = ExpenseType.YEARLY;
	Expense expense = new Expense(name, value, date, type);
	assertTrue(expense.existsInMonth(YearMonth.of(2015, 8)));
    }

    /**
     * Tests whether the expense with yearly type was made in a different month
     */
    @Test
    public void testExistsInMonthYearlyForDifferentMonth() {
	String name = "Greece vacation";
	float value = 3000;
	LocalDate date = LocalDate.of(2015, 8, 10);
	ExpenseType type = ExpenseType.YEARLY;
	Expense expense = new Expense(name, value, date, type);
	assertFalse(expense.existsInMonth(YearMonth.of(2015, 10)));
    }

    /**
     * Tests whether the expense with daily type was made in same year
     */
    @Test
    public void testExistsInYearDailyForSameYear() {
	String name = "Coffee";
	float value = 5;
	LocalDate date = LocalDate.of(2015, 5, 15);
	ExpenseType type = ExpenseType.DAILY;
	Expense expense = new Expense(name, value, date, type);
	assertTrue(expense.existsInYear(Year.of(2015)));
    }

    /**
     * Tests whether the expense with monthly type was made in a different year
     */
    @Test
    public void testExistsInYearMonthlyForDifferentYear() {
	String name = "gas";
	float value = 300;
	LocalDate date = LocalDate.of(2016, 1, 30);
	ExpenseType type = ExpenseType.MONTHLY;
	Expense expense = new Expense(name, value, date, type);
	assertFalse(expense.existsInYear(Year.of(2015)));
    }
}
