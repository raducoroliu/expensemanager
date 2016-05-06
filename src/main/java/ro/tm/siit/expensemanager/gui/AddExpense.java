package ro.tm.siit.expensemanager.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import ro.tm.siit.expensemanager.datepicker.MyDatePicker;
import ro.tm.siit.expensemanager.expense.Expense;
import ro.tm.siit.expensemanager.expense.Expense.ExpenseType;
import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
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
    private JDatePickerImpl datePicker;
    private JLabel typeLabel;
    private JComboBox<ExpenseType> typeCombo;
    private JButton addButton;
    private JButton cancelButton;

    /**
     * 
     * @param expenseManager
     * @param model
     */
    public AddExpense(ExpenseManager expenseManager, DefaultTableModel model) {
	super();
	this.expenseManager = expenseManager;
	this.model = model;
	
	setTitle("Add expense");
	setSize(300, 300);
	getContentPane().setLayout(new GridBagLayout());
	setLocationRelativeTo(null);
	
	createComponents();
	initializeComponents();
	arrangeComponents();
    }

    /**
     * 
     */
    private void createComponents() {
	nameLabel = new JLabel("Name : ");
	nameText = new JTextField(18);

	valueLabel = new JLabel("Value : ");
	valueText = new JTextField(18);

	dateLabel = new JLabel("Date : ");

	MyDatePicker myDatePicker = new MyDatePicker();
	datePicker = myDatePicker.getDatePicker();
//	datePicker.getModel().setDate(LocalDate.now().getYear(),
//		LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
	
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
		    if (nameText.getText().equals("")) {
			throw new IllegalArgumentException("Please enter a name of expense!");
		    }
		    float value = Float.parseFloat(valueText.getText());
		    if (value <= 0) {
			throw new IllegalArgumentException("Incorrect value!");
		    }
		    LocalDate date = myDatePicker.getLocalDate();
		    if (date.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Incorrect date!");
		    }
		    ExpenseType expenseType = (ExpenseType) typeCombo.getSelectedItem();
		    Expense newExpense = new Expense(nameText.getText(), value, date, expenseType);
		    expenseManager.addExpense(newExpense, model, AddExpense.this);
		    initializeComponents();
		    setVisible(false);
		} catch (NumberFormatException ex) {
		    JOptionPane.showMessageDialog(AddExpense.this, "Value must be a number !");
		} catch (IllegalArgumentException ex) {
		    JOptionPane.showMessageDialog(AddExpense.this, ex.getMessage());
		} catch (NullPointerException ex) {
		    JOptionPane.showMessageDialog(AddExpense.this, "Please enter a date!");
		} catch (DateTimeParseException ex) {
		    JOptionPane.showMessageDialog(AddExpense.this, "Date must be in format dd/mm/yyyy");
		}
	    }

	});

	cancelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		initializeComponents();
		setVisible(false);
	    }
	});
    }

    private void initializeComponents() {
	nameText.setText("");
	valueText.setText("");
	typeCombo.setSelectedIndex(0);
	datePicker.getModel().setSelected(false);
//	datePicker.getModel().setDate(LocalDate.now().getYear(),
//		LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
    }

    private void arrangeComponents() {
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;

	c.weightx = 1;
	c.weighty = 0.2;
	c.gridx = 0;
	c.gridy = 0;
	c.weightx = 2;
	c.anchor = GridBagConstraints.LINE_END;
	add(nameLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(nameText, c);

	c.weightx = 1;
	c.weighty = 0.2;
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(valueLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(valueText, c);

	c.weightx = 1;
	c.weighty = 0.2;
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(dateLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(datePicker, c);

	c.weightx = 1;
	c.weighty = 0.2;
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(typeLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(typeCombo, c);

	c.weightx = 1;
	c.weighty = 0.5;
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(addButton, c);

	c.gridx++;
	c.anchor = GridBagConstraints.CENTER;
	add(cancelButton, c);
    }
}
