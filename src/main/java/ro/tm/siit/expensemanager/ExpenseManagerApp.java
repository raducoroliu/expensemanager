package ro.tm.siit.expensemanager;

import java.util.logging.Logger;

import ro.tm.siit.expensemanager.expense.ExpenseManager;
import ro.tm.siit.expensemanager.gui.ExpenseManagerFrame;

/**
 * Creates a desktop gui application to manage personal expenses that support
 * add expense , lookup expense by filter options, calculates a forecast per
 * month and per year and determines the biggest expense per month or per year.
 * 
 * @author Radu
 *
 */
public class ExpenseManagerApp {

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    /**
     * the start point of application
     * 
     * @param args
     */
    public static void main(String[] args) {
	LOGGER.fine("starting application");
	new Logging().configure("expensemanager.log");
	Persistence storage = new Persistence();
	ExpenseManager expenseManager = storage.loadExpenses();

	javax.swing.SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		ExpenseManagerFrame expenseManagerFrame = new ExpenseManagerFrame(expenseManager);
		expenseManagerFrame.setLocationRelativeTo(null);
		expenseManagerFrame.setVisible(true);
		LOGGER.info("expense manager frame is visible");
	    }
	});
    }
}
