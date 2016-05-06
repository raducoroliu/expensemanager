package ro.tm.siit.expensemanager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ro.tm.siit.expensemanager.expense.ExpenseManager;

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

    public SetBudget(ExpenseManager expenseManager) {
	super();
	this.expenseManager = expenseManager;
	setTitle("Set budget limit per month");
	setSize(250, 150);
	getContentPane().setLayout(new GridBagLayout());
	setLocationRelativeTo(null);
	
	createComponents();
	arrangeComponents();

    }

    private void createComponents() {

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
		    expenseManager.setBudgetPerMonth(budgetPerMonth, SetBudget.this);
		    budgetText.setText(String.valueOf(expenseManager.getBudgetPerMonth()));
		    setVisible(false);
		} catch (NumberFormatException ex) {
		    JOptionPane.showMessageDialog(SetBudget.this, "Value must be a number !");
		} catch (IllegalArgumentException ex) {
		    JOptionPane.showMessageDialog(SetBudget.this, ex.getMessage());
		}
	    }
	});

	cancelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		budgetText.setText(String.valueOf(expenseManager.getBudgetPerMonth()));
		setVisible(false);
	    }
	});
    }

    private void arrangeComponents() {
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;

	c.weightx = 1;
	c.weighty = 0.2;
	c.gridx = 0;
	c.gridy = 0;

	c.anchor = GridBagConstraints.LINE_END;
	add(budgetLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(budgetText, c);

	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.CENTER;
	add(okButton, c);

	c.gridx++;
	c.gridwidth = 1;
	c.anchor = GridBagConstraints.CENTER;
	add(cancelButton, c);
    }
}
