package ro.tm.siit.expensemanager;

import java.util.logging.Logger;

import ro.tm.siit.expensemanager.expense.ExpenseManager;
import ro.tm.siit.expensemanager.gui.ExpenseManagerFrame;

/**
 * @author Radu
 *
 */
public class ExpenseManagerApp {
    
    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    /**
     * @param args
     */
    public static void main(String[] args) {
	new Logging().configure("expensemanager.log");
	Persistence storage = new Persistence();
	ExpenseManager expenseManager = storage.loadExpenses();
	
	javax.swing.SwingUtilities.invokeLater(new Runnable() {

		@Override
		public void run() {
		    ExpenseManagerFrame expenseManagerFrame = new ExpenseManagerFrame(expenseManager);
		    expenseManagerFrame.setLocationRelativeTo(null);
		    expenseManagerFrame.setVisible(true);
		}
	});
    }
}
