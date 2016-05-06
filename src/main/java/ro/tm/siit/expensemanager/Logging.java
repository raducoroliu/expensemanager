package ro.tm.siit.expensemanager;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Models a logger
 * @author Radu
 *
 */
public class Logging {

	/**
	 * logger for this class
	 */
	public static final Logger LOGGER = Logger.getGlobal();

	/**
	 * configures logging system to write also in file filename, logging
	 * everything
	 */
	public void configure(String filename) {
		try {
			Handler fileHandler = configureFileLog(filename);
			Logger.getGlobal().addHandler(fileHandler);
			Handler consoleHandler = configureConsoleLog();
			Logger.getGlobal().addHandler(consoleHandler);
			Logger.getGlobal().setLevel(Level.ALL);
		} catch (SecurityException | IOException e) {
			LOGGER.severe("cannot init logging system");
		}
		
		LOGGER.info("Logging configured properly for console and file: " + filename);
	}

	/**
	 * configure file handler to write into file for level.info
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private Handler configureFileLog(String filename) throws IOException {
		Handler handler = new FileHandler(filename);
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.INFO);
		LOGGER.info("FileHandler created for file: " + filename + " " + handler);
		return handler;
	}

	private Handler configureConsoleLog() {
		Handler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		LOGGER.info("ConsoleHandler created for " + handler);
		return handler;
	}

}
