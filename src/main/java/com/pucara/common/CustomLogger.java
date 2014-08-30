package com.pucara.common;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class is intended to be used with the default logging clase of java. It
 * save the log in a log file and display a simple message to the user.
 * 
 * @author Maximiliano Fabian
 */
public class CustomLogger {

	private static final Logger logger = Logger.getLogger(String.format(
			"Pucara - %s", CustomLogger.class));

	public enum LoggerLevel {
		DEGUB, ERROR, INFO, SEVERE, WARNING, CONFIG
	}

	/**
	 * Enables to log all exceptions to a file and display simple message on
	 * demand.
	 * 
	 * @param exception
	 * @param level
	 * @param msg
	 */
	public static void log(Exception exception, LoggerLevel level, String msg) {
		FileHandler fh = null;

		try {
			fh = new FileHandler(CommonData.LOGGER_PATH, true);

			SimpleFormatter sp = new SimpleFormatter();
			fh.setFormatter(sp);
			logger.addHandler(fh);

			switch (level) {
			case SEVERE:
				logger.log(Level.SEVERE, msg, exception);
				break;
			case WARNING:
				logger.log(Level.WARNING, msg, exception);
				break;
			case INFO:
				logger.log(Level.INFO, msg, exception);
				break;
			case ERROR:
				logger.log(Level.SEVERE, msg, exception);
				break;
			default:
				logger.log(Level.CONFIG, msg);
				break;
			}
		} catch (SecurityException | IOException e) {
			logger.log(Level.SEVERE, null, e);
		} finally {
			if (fh != null) {
				fh.close();
			}
		}
	}

	/**
	 * Enables to log all exceptions to a file and display simple message on
	 * demand, without exception.
	 * 
	 * @param level
	 * @param msg
	 */
	public static void log(LoggerLevel level, String msg) {
		log(null, level, msg);
	}

	/**
	 * Enables to debug all exceptions to a file and display simple message on
	 * demand.
	 * 
	 * @param exception
	 * @param level
	 * @param msg
	 */
	public static void debug(Exception exception, LoggerLevel level, String msg) {
		FileHandler fh = null;

		try {
			fh = new FileHandler(CommonData.DEBUG_PATH, true);

			SimpleFormatter sp = new SimpleFormatter();
			fh.setFormatter(sp);
			logger.addHandler(fh);

			switch (level) {
			case SEVERE:
				logger.log(Level.SEVERE, msg, exception);
				break;
			case WARNING:
				logger.log(Level.WARNING, msg, exception);
				break;
			case INFO:
				logger.log(Level.INFO, msg, exception);
				break;
			case ERROR:
				logger.log(Level.SEVERE, msg, exception);
				break;
			default:
				logger.log(Level.CONFIG, msg);
				break;
			}
		} catch (SecurityException | IOException e) {
			logger.log(Level.SEVERE, null, e);
		} finally {
			if (fh != null) {
				fh.close();
			}
		}
	}

}
