package ro.tm.siit.expensemanager.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ro.tm.siit.expensemanager.Persistence;
import ro.tm.siit.expensemanager.expense.Expense;
import ro.tm.siit.expensemanager.expense.ExpenseManager;

/**
 * @author Radu
 *
 */
public class ExpenseManagerFrame extends JFrame {
    
    public static final Logger LOGGER = Logger.getGlobal();
    
    //private List<Expense> expenses;
    //private JFrame expenseManagerFrame;
    private ExpenseManager expenseManager;
    private JPanel tablePanel;
    //private JPanel statisticsPanel;
    private JTable expensesTable;
    private DefaultTableModel model;
    private JMenuBar menu;
    

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
		LOGGER.info("window closed successfully and catalog saved");
	    }
	});
	createComponents();
    }

    private void createComponents() {
	tablePanel = new JPanel();
	
	Dimension dim = tablePanel.getPreferredSize();
	dim.width = 700;
	dim.height = 400;
	tablePanel.setPreferredSize(dim);
	tablePanel.setLayout(new BorderLayout());
	tablePanel.setBorder(BorderFactory.createEtchedBorder());
	
//	statisticsPanel = new StatisticsPanel(expenseManager);
//	Dimension dim1 = statisticsPanel.getPreferredSize();
//	dim1.width = 600;
//	dim1.height = 100;
//	statisticsPanel.setPreferredSize(dim1);
//	statisticsPanel.setBorder(BorderFactory.createEtchedBorder());
//	
	add(tablePanel, BorderLayout.CENTER);
	//add(statisticsPanel, BorderLayout.SOUTH);
	
	
	
	expensesTable = new JTable();

	//expenseTable.setBorder(BorderFactory.createEtchedBorder());
	expensesTable.setFillsViewportHeight(true);
	model = (DefaultTableModel) expensesTable.getModel();
	JScrollPane scrollPane = new JScrollPane(expensesTable);
	expensesTable.setAutoCreateRowSorter(true);

	tablePanel.add(scrollPane, BorderLayout.CENTER);
	createMenuBar();

	List<Expense> expensesToShow = expenseManager.getAll();
	expenseManager.displayExpenses(model, expensesToShow);
    }

    private void createMenuBar() {
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

	fileMenu.add(addItem);
	fileMenu.add(setBudgetItem);
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
	JDialog optionFilter = new OptionFilter(expenseManager, model);
	optionItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		optionFilter.setVisible(true);
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
    }

    protected void save(ExpenseManager expenseManager) {
	Persistence storage = new Persistence();
	storage.saveExpenses(expenseManager);
    }
}
