package ro.tm.siit.expensemanager.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import org.jdatepicker.impl.JDatePickerImpl;

import ro.tm.siit.expensemanager.datepicker.MyDatePicker;
import ro.tm.siit.expensemanager.expense.Expense;
import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
 * @author Radu
 *
 */
public class OptionFilter extends JDialog {
    
    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();
    
    private ExpenseManager expenseManager;
    private DefaultTableModel model;
    private JLabel typeLabel;
    private JComboBox<String> typeCombo;
    private JCheckBox dateCheck;
    private JDatePickerImpl datePicker;
    private JCheckBox periodCheck;
    private JLabel fromLabel;
    private JComboBox<Month> startCombo;
    private JSpinner startSpinner;
    private JLabel toLabel;
    private JComboBox<Month> toCombo;
    private JSpinner toSpinner;
    private JButton okButton;
    private JButton cancelButton;
    private LocalDate currentDate = LocalDate.now();

    public OptionFilter(ExpenseManager expenseManager, DefaultTableModel model) {
	super();
	this.expenseManager = expenseManager;
	this.model = model;

	setTitle("Filter options");
	setSize(300, 300);
	getContentPane().setLayout(new GridBagLayout());
	setLocationRelativeTo(null);

	createComponents();
	initializeComponents();
	arrangeComponents();
    }

    private void createComponents() {
	String[] type = { "all types", "DAILY", "WEEKLY", "MONTHLY", "YEARLY" };

	typeLabel = new JLabel("Type : ");

	typeCombo = new JComboBox<String>();
	typeCombo.setModel(new DefaultComboBoxModel<String>(type));
	typeCombo.setBackground(Color.white);

	dateCheck = new JCheckBox("By date : ", false);
	periodCheck = new JCheckBox(" Period : ", false);

	fromLabel = new JLabel(" from : ");

	MyDatePicker myDatePicker = new MyDatePicker();
	datePicker = myDatePicker.getDatePicker();
	// datePicker.getModel().setDate(currentDate.getYear(),
	// currentDate.getMonthValue(), currentDate.getDayOfMonth());
	datePicker.getComponent(1).setEnabled(false);

	startCombo = new JComboBox<Month>();
	startCombo.setModel(new DefaultComboBoxModel<Month>(Month.values()));
	startCombo.setBackground(Color.white);

	SpinnerModel startSpinnerModel = new SpinnerNumberModel(currentDate.getYear(), currentDate.getYear() - 50,
		currentDate.getYear() + 50, 1);
	startSpinner = new JSpinner(startSpinnerModel);
	startSpinner.setEditor(new JSpinner.NumberEditor(startSpinner, "#"));
	JFormattedTextField startFormatText = ((JSpinner.DefaultEditor) startSpinner.getEditor()).getTextField();
	startFormatText.setEditable(false);
	startFormatText.setBackground(Color.white);

	toLabel = new JLabel(" to : ");

	toCombo = new JComboBox<Month>();
	toCombo.setModel(new DefaultComboBoxModel<Month>(Month.values()));
	toCombo.setBackground(Color.white);

	SpinnerModel toSpinnerModel = new SpinnerNumberModel(currentDate.getYear(), currentDate.getYear() - 50,
		currentDate.getYear() + 50, 1);
	toSpinner = new JSpinner(toSpinnerModel);
	toSpinner.setEditor(new JSpinner.NumberEditor(toSpinner, "#"));
	JFormattedTextField toFormatText = ((JSpinner.DefaultEditor) toSpinner.getEditor()).getTextField();
	toFormatText.setEditable(false);
	toFormatText.setBackground(Color.white);

	okButton = new JButton("OK");
	cancelButton = new JButton("Cancel");

	dateCheck.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		periodCheck.setSelected(false);
		startCombo.setEnabled(false);
		toCombo.setEnabled(false);
		startSpinner.setEnabled(false);
		toSpinner.setEnabled(false);
		// datePicker.getModel().setDate(currentDate.getYear(),
		// currentDate.getMonthValue(),
		// currentDate.getDayOfMonth());
		datePicker.getComponent(1).setEnabled(dateCheck.isSelected());
	    }
	});

	periodCheck.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dateCheck.setSelected(false);
		datePicker.getComponent(1).setEnabled(false);
		startCombo.setSelectedIndex(0);
		startCombo.setEnabled(periodCheck.isSelected());
		toCombo.setSelectedIndex(0);
		toCombo.setEnabled(periodCheck.isSelected());
		startSpinner.setEnabled(periodCheck.isSelected());
		startSpinner.setValue(currentDate.getYear());
		toSpinner.setEnabled(periodCheck.isSelected());
		toSpinner.setValue(currentDate.getYear());
	    }
	});

	okButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    String type = (String) typeCombo.getSelectedItem();
		    if (dateCheck.isSelected()) {
			LocalDate date = myDatePicker.getLocalDate();
			if (date.isAfter(LocalDate.now())) {
			    throw new IllegalArgumentException("Incorrect date!");
			}
			List<Expense> expensesForDisplay = expenseManager.getExpensesByTypeAndDay(type, date);
			expenseManager.displayExpenses(model, expensesForDisplay);
		    } else if (periodCheck.isSelected()) {
			Month startMonth = (Month) startCombo.getSelectedItem();
			Month toMonth = (Month) toCombo.getSelectedItem();
			int startYear = (int) startSpinner.getValue();
			int toYear = (int) toSpinner.getValue();
			YearMonth start = YearMonth.of(startYear, startMonth);
			YearMonth to = YearMonth.of(toYear, toMonth);
			if (start.isAfter(to)) {
			    throw new IllegalArgumentException("Incorrect period!");
			}
			List<Expense> expensesForDisplay = expenseManager.getExpensesByTypeAndPeriod(type, start, to);
			expenseManager.displayExpenses(model, expensesForDisplay);
		    } else {
			List<Expense> expensesForDisplay = expenseManager.getExpensesByType(type);
			expenseManager.displayExpenses(model, expensesForDisplay);
		    }
		    initializeComponents();
		    setVisible(false);
		} catch (IllegalArgumentException ex) {
		    JOptionPane.showMessageDialog(OptionFilter.this, ex.getMessage());
		} catch (NullPointerException ex) {
		    JOptionPane.showMessageDialog(OptionFilter.this, "Please enter a date!");
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
	dateCheck.setSelected(false);
	periodCheck.setSelected(false);
	typeCombo.setSelectedIndex(0);
	// datePicker.getModel().setDate(currentDate.getYear(),
	// currentDate.getMonthValue(), currentDate.getDayOfMonth());
	datePicker.getComponent(1).setEnabled(false);
	startCombo.setSelectedIndex(0);
	startCombo.setEnabled(false);
	toCombo.setSelectedIndex(0);
	toCombo.setEnabled(false);
	startSpinner.setEnabled(false);
	startSpinner.setValue(currentDate.getYear());
	toSpinner.setEnabled(false);
	toSpinner.setValue(currentDate.getYear());
    }

    private void arrangeComponents() {
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;

	c.weightx = 0.2;
	c.weighty = 1;

	c.gridx = 0;
	c.gridy = 0;
	c.anchor = GridBagConstraints.LINE_END;
	add(typeLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(typeCombo, c);

	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_START;
	add(dateCheck, c);

	c.gridx++;
	c.gridwidth = 2;
	c.anchor = GridBagConstraints.LINE_START;
	add(datePicker, c);

	c.gridx = 0;
	c.gridy++;
	c.gridwidth = 1;
	c.anchor = GridBagConstraints.LINE_START;
	add(periodCheck, c);

	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(fromLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(startCombo, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(startSpinner, c);

	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(toLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(toCombo, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(toSpinner, c);

	c.gridx = 1;
	c.gridy++;
	c.anchor = GridBagConstraints.CENTER;
	add(okButton, c);

	c.gridx = 2;
	c.anchor = GridBagConstraints.LINE_START;
	add(cancelButton, c);
    }
}
