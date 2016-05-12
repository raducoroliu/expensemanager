package ro.tm.siit.expensemanager.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ro.tm.siit.expensemanager.Persistence;
import ro.tm.siit.expensemanager.expense.Expense;
import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
 * ExpenseManagerFrame class extends JFrame and creates a frame in which are
 * displayed the expenses and a menu for managing expenses
 * 
 * @author Radu
 *
 */
public class ExpenseManagerFrame extends JFrame {

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    public JLabel filterLabel;

    private ExpenseManager expenseManager;
    private JTable expensesTable;
    private DefaultTableModel model;
    private JMenuBar menu;

    /**
     * constructor for ExpenseManagerFrame
     * 
     * @param expenseManager
     *            the expense manager
     */
    public ExpenseManagerFrame(ExpenseManager expenseManager) {
	super("Expense Manager");
	this.expenseManager = expenseManager;
	setSize(700, 500);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	getContentPane().setLayout(new BorderLayout());

	addWindowListener(new WindowAdapter() {

	    @Override
	    public void windowClosing(WindowEvent e) {
		LOGGER.fine("saving expenseManager " + expenseManager);
		save(expenseManager);
		LOGGER.info("window closed successfully and expenseManager saved");
	    }
	});
	createComponents();
	LOGGER.info("expense manager window created");
    }

    /**
     * creates the components of the frame
     */
    private void createComponents() {
	LOGGER.fine("creating the components for expense manager window");
	expensesTable = new JTable();
	expensesTable.setBorder(BorderFactory.createEtchedBorder());
	expensesTable.setFillsViewportHeight(true);
	model = (DefaultTableModel) expensesTable.getModel();
	JScrollPane scrollPane = new JScrollPane(expensesTable);
	expensesTable.setAutoCreateRowSorter(true);

	add(scrollPane, BorderLayout.CENTER);
	createMenuBar();
	menu.setEnabled(false);

	List<Expense> expensesForDisplay = expenseManager.getAll();
	expenseManager.displayExpenses(model, expensesForDisplay);
	LOGGER.info("the components for expense manager window are created");
    }

    /**
     * creates a menu for managing expenses
     */
    private void createMenuBar() {
	LOGGER.fine("creating the menu for expense manager window");
	menu = new JMenuBar();
	menu.setBorder(BorderFactory.createEtchedBorder());

	JMenu fileMenu = new JMenu("File");

	JMenuItem addItem = new JMenuItem("Add expense...");
	JDialog addExpense = new AddExpense(expenseManager, model);
	addItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		addExpense.setVisible(true);
	    }
	});

	JMenuItem setBudgetItem = new JMenuItem("Set monthly budget...");
	JDialog setBudget = new SetBudget(expenseManager);
	setBudgetItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		setBudget.setVisible(true);
	    }
	});

	JMenuItem exitItem = new JMenuItem("Exit");
	exitItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		save(expenseManager);
		System.exit(0);
	    }
	});

	fileMenu.add(setBudgetItem);
	fileMenu.add(addItem);
	fileMenu.addSeparator();
	fileMenu.add(exitItem);

	JMenu viewMenu = new JMenu("View");

	JMenuItem defaultItem = new JMenuItem("View all");
	defaultItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		List<Expense> expensesToShow = expenseManager.getAll();
		expenseManager.displayExpenses(model, expensesToShow);
	    }
	});

	JMenuItem optionItem = new JMenuItem("Filter options...");
	JDialog filterOptions = new FilterOptions(expenseManager, model);
	optionItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		filterOptions.setVisible(true);
	    }
	});

	viewMenu.add(defaultItem);
	viewMenu.addSeparator();
	viewMenu.add(optionItem);

	JMenu statisticsMenu = new JMenu("Statistics");

	JMenuItem forecastItem = new JMenuItem("Forecast...");
	JDialog forecast = new Forecast(expenseManager);
	forecastItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		forecast.setVisible(true);
	    }
	});

	JMenuItem biggestExpenseItem = new JMenuItem("Biggest expense...");
	JDialog biggestExpense = new BiggestExpense(expenseManager, model);
	biggestExpenseItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		biggestExpense.setVisible(true);
	    }
	});

	statisticsMenu.add(forecastItem);
	statisticsMenu.add(biggestExpenseItem);

	menu.add(fileMenu);
	menu.add(viewMenu);
	menu.add(statisticsMenu);
	
	setJMenuBar(menu);
	LOGGER.info("the menu for expense manager window are created");
    }

    /**
     * saves the expense manager used Persistence class
     * 
     * @param expenseManager
     *            the expense manager
     */
    protected void save(ExpenseManager expenseManager) {
	Persistence storage = new Persistence();
	storage.saveExpenses(expenseManager);
    }
}
