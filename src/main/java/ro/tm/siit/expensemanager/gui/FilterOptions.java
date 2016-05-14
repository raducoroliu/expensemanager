package ro.tm.siit.expensemanager.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
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
 * FilterOptions class extends JDialog and creates a dialog window for sets the
 * filter options
 * 
 * @author Radu
 *
 */
public class FilterOptions extends JDialog {

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
    private MyDatePicker myDatePicker;
    private JCheckBox monthCheck;
    private JComboBox<Month> monthCombo;
    private JCheckBox yearCheck;
    private JSpinner yearSpinner;
    private JButton okButton;
    private JButton cancelButton;
    private LocalDate now = LocalDate.now();

    /**
     * constructor for FilterOptions
     * 
     * @param expenseManager
     * @param model
     */
    public FilterOptions(ExpenseManager expenseManager, DefaultTableModel model) {
	super();
	LOGGER.fine(
		"creating the filter options dialog window for expense manager and displays the expenses filtered to expenses table");
	this.expenseManager = expenseManager;
	this.model = model;

	setTitle("Filter options");
	setSize(350, 300);
	getContentPane().setLayout(new GridBagLayout());
	setLocationRelativeTo(null);
	addWindowListener(new WindowAdapter() {

	    @Override
	    public void windowClosing(WindowEvent e) {
		initializeComponents();
		LOGGER.info("the filter option dialog window closed");
	    }
	});

	createComponents();
	initializeComponents();
	arrangeComponents();
	LOGGER.info("filter options dialog window has been created and is visible");
    }

    /**
     * creates the components for filter options dialog window
     */
    private void createComponents() {
	LOGGER.fine("creating the components for filter options dialog window");
	String[] type = { "all types", "DAILY", "WEEKLY", "MONTHLY", "YEARLY" };

	typeLabel = new JLabel("Type : ");

	typeCombo = new JComboBox<String>();
	typeCombo.setModel(new DefaultComboBoxModel<String>(type));
	typeCombo.setBackground(Color.white);

	dateCheck = new JCheckBox("by date : ", false);
	monthCheck = new JCheckBox("by month-year : ", false);
	yearCheck = new JCheckBox("by year : ", false);

	myDatePicker = new MyDatePicker();
	datePicker = myDatePicker.getDatePicker();
	datePicker.getComponent(1).setEnabled(false);

	monthCombo = new JComboBox<Month>();
	monthCombo.setModel(new DefaultComboBoxModel<Month>(Month.values()));
	monthCombo.setBackground(Color.white);

	SpinnerModel yearSpinnerModel = new SpinnerNumberModel(now.getYear(), now.getYear() - 50, now.getYear() + 50,
		1);
	yearSpinner = new JSpinner(yearSpinnerModel);
	yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
	JFormattedTextField yearFormatText = ((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField();
	yearFormatText.setEditable(false);
	yearFormatText.setBackground(Color.white);

	okButton = new JButton("OK");
	cancelButton = new JButton("Cancel");

	dateCheck.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		monthCheck.setSelected(false);
		yearCheck.setSelected(false);
		monthCombo.setEnabled(false);
		yearSpinner.setEnabled(false);
		myDatePicker.setInitialDate();
		datePicker.getComponent(1).setEnabled(dateCheck.isSelected());
	    }
	});

	monthCheck.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dateCheck.setSelected(false);
		yearCheck.setSelected(false);
		datePicker.getComponent(1).setEnabled(false);
		monthCombo.setSelectedIndex(0);
		monthCombo.setEnabled(monthCheck.isSelected());
		yearSpinner.setEnabled(monthCheck.isSelected());
		yearSpinner.setValue(now.getYear());
	    }
	});

	yearCheck.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dateCheck.setSelected(false);
		monthCheck.setSelected(false);
		datePicker.getComponent(1).setEnabled(false);
		monthCombo.setEnabled(false);
		yearSpinner.setEnabled(yearCheck.isSelected());
		yearSpinner.setValue(now.getYear());
	    }
	});

	okButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    List<Expense> expensesForDisplay;
		    String type = (String) typeCombo.getSelectedItem();
		    if (dateCheck.isSelected()) {
			LocalDate date = myDatePicker.getLocalDate();
			expensesForDisplay = expenseManager.getExpensesByTypeAndDay(type, date);

		    } else if (monthCheck.isSelected()) {
			Month month = (Month) monthCombo.getSelectedItem();
			int year = (int) yearSpinner.getValue();
			YearMonth yearMonth = YearMonth.of(year, month);
			expensesForDisplay = expenseManager.getExpensesByTypeAndMonth(type, yearMonth);
		    } else if (yearCheck.isSelected()) {
			Year year = Year.of((int) yearSpinner.getValue());
			expensesForDisplay = expenseManager.getExpensesByTypeAndYear(type, year);
		    } else {
			expensesForDisplay = expenseManager.getExpensesByType(type);
		    }
		    expenseManager.displayExpenses(model, expensesForDisplay);
		    LOGGER.info("the " + expensesForDisplay.size() + " expenses are displayed after filtering");
		    initializeComponents();
		    setVisible(false);
		    LOGGER.info("the filter options dialog window became invisible");
		} catch (IllegalArgumentException ex) {
		    LOGGER.warning("failed lookup expenses " + ex);
		    JOptionPane.showMessageDialog(FilterOptions.this, ex.getMessage());
		} catch (NullPointerException ex) {
		    LOGGER.warning("failed lookup expenses " + ex);
		    JOptionPane.showMessageDialog(FilterOptions.this, "Please enter a date!");
		}
	    }
	});

	cancelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		initializeComponents();
		setVisible(false);
		LOGGER.info("the filter options dialog window became invisible");
	    }
	});
	LOGGER.info("the components for filter options dialog window has been created");
    }

    /**
     * initializing the components for initial viewing
     */
    private void initializeComponents() {
	LOGGER.fine("initializing the components for initial viewing of filter options dialog window");
	dateCheck.setSelected(false);
	monthCheck.setSelected(false);
	yearCheck.setSelected(false);
	typeCombo.setSelectedIndex(0);
	myDatePicker.setInitialDate();
	datePicker.getComponent(1).setEnabled(false);
	monthCombo.setSelectedIndex(0);
	monthCombo.setEnabled(false);
	yearSpinner.setValue(now.getYear());
	yearSpinner.setEnabled(false);
	LOGGER.info("the components has been initialized");
    }

    /**
     * arranges the components created in the window
     */
    private void arrangeComponents() {
	LOGGER.fine("arranging the components in filter options dialog window");
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;

	c.weightx = 0.2;
	c.weighty = 1;

	// first row
	c.gridx = 0;
	c.gridy = 0;
	c.anchor = GridBagConstraints.LINE_END;
	add(typeLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(typeCombo, c);

	// second row
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_START;
	add(dateCheck, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(datePicker, c);

	// third row
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_START;
	add(monthCheck, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(monthCombo, c);

	// fourth row
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_START;
	add(yearCheck, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(yearSpinner, c);

	// fifth row
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(okButton, c);

	c.gridx++;
	c.anchor = GridBagConstraints.CENTER;
	add(cancelButton, c);

	LOGGER.info("the components has been arranged in filter options dialog window");
    }
}
