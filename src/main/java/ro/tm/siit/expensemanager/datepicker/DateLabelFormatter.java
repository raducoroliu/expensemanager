package ro.tm.siit.expensemanager.datepicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * DateLabelFormatter extends AbstractFormatter and models a date formatter for
 * date picker
 * 
 * @author Radu
 *
 */
public class DateLabelFormatter extends AbstractFormatter {

    private String datePattern = "dd-MM-yyyy";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.JFormattedTextField.AbstractFormatter#stringToValue(java.lang
     * .String)
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
	return dateFormatter.parseObject(text);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.JFormattedTextField.AbstractFormatter#valueToString(java.lang
     * .Object)
     */
    @Override
    public String valueToString(Object value) throws ParseException {
	if (value != null) {
	    Calendar cal = (Calendar) value;
	    return dateFormatter.format(cal.getTime());
	}
	return "";
    }
}
