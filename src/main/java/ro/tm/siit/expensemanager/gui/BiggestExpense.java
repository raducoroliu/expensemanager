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

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import ro.tm.siit.expensemanager.expense.Expense;
import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
 * BiggestExpense class extends JDialog and creates a dialog window in which
 * calculates and displays the biggest expense per month or per year
 * 
 * @author Radu
 *
 */
public class BiggestExpense extends JDialog {

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    private ExpenseManager expenseManager;
    private DefaultTableModel model;
    private JRadioButton monthRadio;
    private JRadioButton yearRadio;
    private JLabel monthLabel;
    private JLabel yearLabel;
    private JSpinner yearSpinner;
    private JComboBox<Month> monthCombo;
    private JButton viewButton;
    private JButton cancelButton;
    private LocalDate now = LocalDate.now();

    /**
     * constructor for BiggestExpense dialog window
     * 
     * @param expenseManager
     *            the expense manager
     * @param model
     *            the expenses table model
     */
    public BiggestExpense(ExpenseManager expenseManager, DefaultTableModel model) {
	super();
	LOGGER.fine(
		"creating the biggest expense dialog window for expense manager and displays the biggest expense to expenses table");
	this.expenseManager = expenseManager;
	this.model = model;
	setTitle("The biggest expense");
	setSize(300, 200);
	getContentPane().setLayout(new GridBagLayout());
	setLocationRelativeTo(null);
	addWindowListener(new WindowAdapter() {

	    @Override
	    public void windowClosing(WindowEvent e) {
		initializeComponents();
		LOGGER.info("biggest expense window dialog has been closed");
	    }
	});

	createComponents();
	initializeComponents();
	arrangeComponents();
	LOGGER.info("biggest expense window dialog has been created and is visible");
    }

    /**
     * creating the components for the biggest expense dialog window
     */
    private void createComponents() {
	LOGGER.fine("creating the components for biggest expense dialog window");
	monthRadio = new JRadioButton(" by month");
	monthRadio.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		monthCombo.setEnabled(true);
	    }
	});

	yearRadio = new JRadioButton(" by year ");
	yearRadio.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		monthCombo.setEnabled(false);

	    }
	});

	ButtonGroup group = new ButtonGroup();
	group.add(monthRadio);
	group.add(yearRadio);

	monthLabel = new JLabel("Month : ");

	monthCombo = new JComboBox<Month>();
	monthCombo.setModel(new DefaultComboBoxModel<Month>(Month.values()));
	monthCombo.setBackground(Color.white);

	yearLabel = new JLabel("Year : ");

	SpinnerModel yearSpinnerModel = new SpinnerNumberModel(now.getYear(), now.getYear() - 50, now.getYear() + 50,
		1);
	yearSpinner = new JSpinner(yearSpinnerModel);
	yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
	JFormattedTextField startFormatText = ((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField();
	startFormatText.setEditable(false);
	startFormatText.setBackground(Color.white);

	viewButton = new JButton("View");
	cancelButton = new JButton("Cancel");

	viewButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    List<Expense> expensesForDisplay;
		    if (group.isSelected(monthRadio.getModel())) {
			Month month = (Month) monthCombo.getSelectedItem();
			int year = (int) yearSpinner.getValue();
			YearMonth yearMonth = YearMonth.of(year, month);
			expensesForDisplay = expenseManager.getBiggestPerMonth(yearMonth);
		    } else {
			Year year = Year.of((int) yearSpinner.getValue());
			expensesForDisplay = expenseManager.getBiggestPerYear(year);
		    }
		    expenseManager.displayExpenses(model, expensesForDisplay);
		    setVisible(false);
		} catch (NullPointerException ex) {
		    LOGGER.warning("failed to find a biggest expense " + ex);
		    JOptionPane.showMessageDialog(BiggestExpense.this, ex.getMessage());
		}
		initializeComponents();
		LOGGER.info("the biggest expense has been displayed");
	    }
	});

	cancelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		initializeComponents();
		setVisible(false);
		LOGGER.info("the biggest expense window dialog became invisible");
	    }
	});
	LOGGER.info("the components for biggest expense window dialog has been created");
    }

    /**
     * initializing the components for initial viewing
     */
    private void initializeComponents() {
	monthCombo.setSelectedItem(now.getMonth());
	yearSpinner.setValue(now.getYear());
	monthRadio.setSelected(true);
    }

    /**
     * arranges the components created in the window
     */
    private void arrangeComponents() {
	LOGGER.fine("arranging the components in biggest expense window dialog");
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE;

	c.weightx = 1;
	c.weighty = 0.2;

	// first row
	c.gridx = 0;
	c.gridy = 0;
	c.anchor = GridBagConstraints.LINE_START;
	add(monthRadio, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_END;
	add(monthLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(monthCombo, c);

	// second row
	c.gridx = 0;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_START;
	add(yearRadio, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_END;
	add(yearLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(yearSpinner, c);

	// third row
	c.gridx = 0;
	c.gridy++;
	c.gridwidth = 2;
	c.anchor = GridBagConstraints.CENTER;
	add(viewButton, c);

	c.gridx = 2;
	c.gridwidth = 1;
	c.anchor = GridBagConstraints.CENTER;
	add(cancelButton, c);

	LOGGER.info("the components has been arranged in biggest expense window dialog");
    }
}
