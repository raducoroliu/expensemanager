package ro.tm.siit.expensemanager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
 * SetBudget class extends JDialog and creates a dialog window to sets the limit
 * budget per month
 * 
 * @author Radu
 *
 */
public class SetBudget extends JDialog {

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    private ExpenseManager expenseManager;
    private JLabel budgetLabel;
    private JTextField budgetText;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * constructor for SetBudget
     * 
     * @param expenseManager
     *            the expense manager
     */
    public SetBudget(ExpenseManager expenseManager) {
	super();
	LOGGER.fine("creating the SetBudget dialog window for expense manager");
	this.expenseManager = expenseManager;
	setTitle("Set budget limit per month");
	setSize(250, 150);
	getContentPane().setLayout(new GridBagLayout());
	setLocationRelativeTo(null);
	addWindowListener(new WindowAdapter() {

	    @Override
	    public void windowClosing(WindowEvent e) {
		budgetText.setText(String.valueOf(expenseManager.getBudgetPerMonth()));
		LOGGER.info("the setBudget dialog window closed");
	    }
	});

	createComponents();
	arrangeComponents();
	LOGGER.info("the SetBudget dialog window created and is visible");
    }

    /**
     * creating the components for set budget dialog window
     */
    private void createComponents() {
	LOGGER.fine("creating the components for SetBudget dialog window");
	budgetLabel = new JLabel("Budget value : ");
	budgetText = new JTextField(10);
	budgetText.setText(String.valueOf(expenseManager.getBudgetPerMonth()));

	okButton = new JButton("OK");
	cancelButton = new JButton("Cancel");

	okButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    float budgetPerMonth = Float.parseFloat(budgetText.getText());
		    if (budgetPerMonth != expenseManager.getBudgetPerMonth()
			    && expenseManager.getBudgetPerMonth() > 0) {
			int choice = JOptionPane.showOptionDialog(SetBudget.this,
				"The budget will be changed. Do you want to continue?", "Warning",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (choice == JOptionPane.NO_OPTION) {
			    budgetPerMonth = expenseManager.getBudgetPerMonth();
			}
		    }
		    expenseManager.setBudgetPerMonth(budgetPerMonth);
		    setVisible(false);
		    LOGGER.info("the limit budget per month is " + budgetPerMonth
			    + " the SetBudget dialog window became invisible");
		} catch (NumberFormatException ex) {
		    LOGGER.warning("set budget failed " + ex);
		    JOptionPane.showMessageDialog(SetBudget.this, "Value must be a number !");
		} catch (IllegalArgumentException ex) {
		    LOGGER.warning("set budget failed " + ex);
		    JOptionPane.showMessageDialog(SetBudget.this, ex.getMessage());
		}
		budgetText.setText(String.valueOf(expenseManager.getBudgetPerMonth()));
	    }
	});

	cancelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		budgetText.setText(String.valueOf(expenseManager.getBudgetPerMonth()));
		setVisible(false);
		LOGGER.info("the SetBudget dialog window became invisible");
	    }
	});
	LOGGER.info("the components for SetBudget dialog window has been created");
    }

    /**
     * arranges the components created in the window
     */
    private void arrangeComponents() {
	LOGGER.fine("arranging the components in set budget dialog window");
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;

	c.weightx = 1;
	c.weighty = 0.2;

	// first row
	c.gridx = 0;
	c.gridy = 0;
	c.anchor = GridBagConstraints.LINE_END;
	add(budgetLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(budgetText, c);

	// second row
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.CENTER;
	add(okButton, c);

	c.gridx++;
	c.gridwidth = 1;
	c.anchor = GridBagConstraints.CENTER;
	add(cancelButton, c);

	LOGGER.info("the components has been arranged in set budget dialog window");
    }
}
