package ro.tm.siit.expensemanager.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePickerImpl;

import ro.tm.siit.expensemanager.datepicker.MyDatePicker;
import ro.tm.siit.expensemanager.expense.Expense;
import ro.tm.siit.expensemanager.expense.Expense.ExpenseType;
import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
 * AddExpense class extends JDialog and creates a dialog window for add expense
 * to expense manager
 * 
 * @author Radu
 *
 */
public class AddExpense extends JDialog {

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    private ExpenseManager expenseManager;
    private DefaultTableModel model;
    private JLabel nameLabel;
    private JTextField nameText;
    private JLabel valueLabel;
    private JTextField valueText;
    private JLabel dateLabel;
    private MyDatePicker myDatePicker;
    private JDatePickerImpl datePicker;
    private JLabel typeLabel;
    private JComboBox<ExpenseType> typeCombo;
    private JButton addButton;
    private JButton cancelButton;

    /**
     * constructor for addExpense
     * 
     * @param expenseManager
     *            the expense manager
     * @param model
     *            the expenses table model
     */
    public AddExpense(ExpenseManager expenseManager, DefaultTableModel model) {
	super();
	LOGGER.fine(
		"creating the add expense dialog window for expense manager and displays the added expense to expenses table");
	this.expenseManager = expenseManager;
	this.model = model;

	setTitle("Add expense");
	setSize(300, 300);
	getContentPane().setLayout(new GridBagLayout());
	setLocationRelativeTo(null);
	addWindowListener(new WindowAdapter() {

	    @Override
	    public void windowClosing(WindowEvent e) {
		initializeComponents();
		LOGGER.info("the addExpense dialog window closed");
	    }
	});

	createComponents();
	initializeComponents();
	arrangeComponents();
	LOGGER.info("add expense dialog window has been created and is visible");
    }

    /**
     * creating the components for add expense window
     */
    private void createComponents() {
	LOGGER.fine("creating the components for add expense dialog window");
	nameLabel = new JLabel("Name : ");
	nameText = new JTextField(18);

	valueLabel = new JLabel("Value : ");
	valueText = new JTextField(18);

	dateLabel = new JLabel("Date : ");

	myDatePicker = new MyDatePicker();
	datePicker = myDatePicker.getDatePicker();

	typeLabel = new JLabel("Type : ");
	typeCombo = new JComboBox<ExpenseType>();
	typeCombo.setModel(new DefaultComboBoxModel<ExpenseType>(ExpenseType.values()));
	typeCombo.setSelectedIndex(0);
	typeCombo.setBackground(Color.white);

	addButton = new JButton("Add");
	cancelButton = new JButton("Cancel");

	addButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    float value = Float.parseFloat(valueText.getText());
		    LocalDate date = myDatePicker.getLocalDate();
		    ExpenseType expenseType = (ExpenseType) typeCombo.getSelectedItem();
		    Expense newExpense = new Expense(nameText.getText(), value, date, expenseType);
		    expenseManager.addExpense(newExpense, model);
		    YearMonth yearMonth = YearMonth.from(date);
		    if (expenseManager.getCurrentValuePerMonth(yearMonth) > expenseManager.getBudgetPerMonth()) {
			JOptionPane.showMessageDialog(AddExpense.this, "The budget per month was exceeded!");
		    }
		    initializeComponents();
		    setVisible(false);
		    LOGGER.info("the biggest expense window dialog became invisible");
		} catch (NumberFormatException ex) {
		    LOGGER.warning("failed adding expense " + ex);
		    JOptionPane.showMessageDialog(AddExpense.this, "Value must be a number !");
		} catch (IllegalArgumentException ex) {
		    LOGGER.warning("failed adding expense " + ex);
		    JOptionPane.showMessageDialog(AddExpense.this, ex.getMessage());
		} catch (NullPointerException ex) {
		    LOGGER.warning("failed adding expense " + ex);
		    JOptionPane.showMessageDialog(AddExpense.this, "Please enter date in all fields!");
		}
	    }
	});

	cancelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		initializeComponents();
		setVisible(false);
		LOGGER.info("the biggest expense dialog window became invisible");
	    }

	});
	LOGGER.info("the components for add expense dialog window has been created");
    }

    /**
     * initializing the components for initial viewing
     */
    private void initializeComponents() {
	LOGGER.fine("initializing the components for initial viewing of add expense dialog window");
	nameText.setText("");
	valueText.setText("");
	typeCombo.setSelectedIndex(0);
	myDatePicker.setInitialDate();
	LOGGER.info("the components has been initialized");
    }

    /**
     * arranges the components created in the window
     */
    private void arrangeComponents() {
	LOGGER.fine("arranging the components in add expense dialog window");
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;

	c.weightx = 1;
	c.weighty = 0.2;

	// first row
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 2;
	c.anchor = GridBagConstraints.LINE_END;
	add(nameLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(nameText, c);

	// second row
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(valueLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(valueText, c);

	// third row
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(dateLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(datePicker, c);

	// fourth row
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(typeLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(typeCombo, c);

	// fifth row
	c.weightx = 1;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(addButton, c);

	c.gridx++;
	c.anchor = GridBagConstraints.CENTER;
	add(cancelButton, c);

	LOGGER.info("the components has been arranged in add expense dialog window");
    }
}
