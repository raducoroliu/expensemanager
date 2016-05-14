package ro.tm.siit.expensemanager.gui;

import java.awt.Color;
import java.awt.Font;
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
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
 * Forecast class extends JDialog and creates a dialog window to calculates and
 * displays the forecast per month or per year
 * 
 * @author Radu
 *
 */
public class Forecast extends JDialog {

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    private ExpenseManager expenseManager;
    private JRadioButton monthRadio;
    private JRadioButton yearRadio;
    private JLabel monthLabel;
    private JComboBox<Month> monthCombo;
    private JLabel yearLabel;
    private JSpinner yearSpinner;
    private JButton viewButton;
    private JButton cancelButton;
    private JLabel forecastLabel;
    private JTextField forecastText;
    private LocalDate now = LocalDate.now();

    /**
     * constructor for Forecast dialog window
     * 
     * @param expenseManager
     *            the expense manager
     */
    public Forecast(ExpenseManager expenseManager) {
	super();
	LOGGER.fine("creating the forecast dialog window for expense manager");
	this.expenseManager = expenseManager;
	setTitle("Forecast");
	setSize(300, 200);
	getContentPane().setLayout(new GridBagLayout());
	setLocationRelativeTo(null);
	addWindowListener(new WindowAdapter() {

	    @Override
	    public void windowClosing(WindowEvent e) {
		initializeComponents();
		LOGGER.info("the forecast dialog window closed");
	    }
	});

	createComponents();
	initializeComponents();
	arrangeComponents();
	LOGGER.info("the forecast dialog window created and is visible");
    }

    /**
     * creating the components for forecast dialog window
     */
    private void createComponents() {
	LOGGER.fine("creating the components for forecast dialog window");
	monthRadio = new JRadioButton(" by month-year ");
	monthRadio.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		monthCombo.setEnabled(true);
		forecastText.setText("");
	    }
	});

	yearRadio = new JRadioButton(" by year ");
	yearRadio.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		monthCombo.setEnabled(false);
		forecastText.setText("");
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

	forecastLabel = new JLabel("Value : ");
	forecastText = new JTextField(10);
	forecastText.setEditable(false);
	Font font = new Font("Verdana", Font.BOLD, 12);
	forecastText.setFont(font);
	forecastText.setBackground(Color.lightGray);
	forecastText.setForeground(Color.blue);

	viewButton = new JButton("View");
	cancelButton = new JButton("Cancel");

	viewButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (group.isSelected(monthRadio.getModel())) {
		    Month month = (Month) monthCombo.getSelectedItem();
		    int year = (int) yearSpinner.getValue();
		    YearMonth yearMonth = YearMonth.of(year, month);
		    double forecastPerMonth = expenseManager.getForecastPerMonth(yearMonth);
		    forecastText.setText(String.format("%.02f", forecastPerMonth));
		    LOGGER.info("the forecast for " + yearMonth + " is " + forecastPerMonth);
		} else {
		    Year year = Year.of((int) yearSpinner.getValue());
		    double forecastPerYear = expenseManager.getForecastPerYear(year);
		    forecastText.setText(String.format("%.02f", forecastPerYear));
		    LOGGER.info("the forecast for " + year + " is " + forecastPerYear);
		}
	    }
	});

	cancelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		initializeComponents();
		setVisible(false);
		LOGGER.info("the forecast dialog window became invisible");
	    }
	});
	LOGGER.info("the components for forecast dialog window has been created");
    }

    /**
     * initializing the components for initial viewing
     */
    private void initializeComponents() {
	monthCombo.setSelectedItem(now.getMonth());
	yearSpinner.setValue(now.getYear());
	forecastText.setText("");
	monthRadio.setSelected(true);
	monthCombo.setEnabled(true);
    }

    /**
     * arranges the components created in the window
     */
    private void arrangeComponents() {
	LOGGER.fine("arranging the components in forecast dialog window");
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
	c.gridx = 1;
	c.gridy++;
	c.anchor = GridBagConstraints.LINE_END;
	add(forecastLabel, c);

	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(forecastText, c);

	// fourth row
	c.gridx = 0;
	c.gridy++;
	c.gridwidth = 2;
	c.anchor = GridBagConstraints.CENTER;
	add(viewButton, c);

	c.gridx = 2;
	c.gridwidth = 1;
	c.anchor = GridBagConstraints.CENTER;
	add(cancelButton, c);

	LOGGER.info("the components has been arranged in forecast dialog window");
    }
}
