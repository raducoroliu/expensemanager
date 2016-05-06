package ro.tm.siit.expensemanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ro.tm.siit.expensemanager.expense.Expense;
import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
 * @author Radu
 *
 */
public class Persistence {

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    private Path file = Paths.get("expense_manager.data");

    public void saveExpenses(ExpenseManager expenseManager) {
	LOGGER.fine("started saving expense manager to file " + file);
	try (OutputStream os = Files.newOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(os)) {
	    oos.writeObject(expenseManager);
	    LOGGER.info("expense manager saved ");
	} catch (IOException e) {
	    LOGGER.warning("cannot saving expense manager" + e.getMessage());
	}
    }

    public ExpenseManager loadExpenses() {
	LOGGER.fine("started loading expense manager from file " + file);
	ExpenseManager expenseManager = null;
	if (Files.exists(file)) {
	    try (InputStream is = Files.newInputStream(file); ObjectInputStream ois = new ObjectInputStream(is)) {
		expenseManager = (ExpenseManager) ois.readObject();
		LOGGER.info("expense manager loaded ");
	    } catch (IOException e) {
		LOGGER.warning("problem loading expense manager data " + e.getMessage());
	    } catch (ClassNotFoundException e) {
		LOGGER.warning("problem loading expense manager data " + e.getMessage());
	    }
	} else {
	    List<Expense> expenses = new ArrayList<Expense>();
	    expenseManager = new ExpenseManager(expenses);
	    LOGGER.info("expense manager created");
	}
	return expenseManager;
    }

}
