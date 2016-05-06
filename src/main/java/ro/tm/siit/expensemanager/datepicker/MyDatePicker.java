package ro.tm.siit.expensemanager.datepicker;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 * create a date picker
 * 
 * @author Radu
 *
 */
public class MyDatePicker {

    /**
     * logger for this class
     */
    public static final Logger LOGGER = Logger.getGlobal();

    public JDatePickerImpl datePicker;

    /**
     * constructor for date picker
     */
    public MyDatePicker() {
	LOGGER.fine("setting the date picker");
	UtilDateModel dateModel = new UtilDateModel();
	//dateModel.setSelected(true);
	Properties p = new Properties();
	p.put("text.today", "Today");
	p.put("text.month", "Month");
	p.put("text.year", "Year");
	JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
	datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
	datePicker.getComponent(0).setBackground(Color.white);
	// dateModel.setDate(LocalDate.now().getYear(),
	// LocalDate.now().getMonth().getValue(),
	// LocalDate.now().getDayOfMonth());
	// System.out.println(dateModel.getMonth());
	LOGGER.info("the date picker has been set");
    }

    /**
     * 
     * @return the datePicker
     */
    public JDatePickerImpl getDatePicker() {
	return datePicker;
    }

    /**
     * transforms the date from date picker to local date
     * 
     * @return the local date
     */
    public LocalDate getLocalDate() {
	LOGGER.fine("getting the local date from date picker");
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	Date selectedDate = (Date) datePicker.getModel().getValue();
	String reportDate = df.format(selectedDate);
	LocalDate date = LocalDate.parse(reportDate, formatter);
	LOGGER.info("the local date from date picker obtained");
	return date;
    }
}