package de.devboost.eclipse.feedback;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;

class FeedbackLogListener implements LogListener {

	private static final String KEY_BUNDLE = "bundle";
	private static final String KEY_TIME = "time";
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_EXCEPTION = "exception";
	private static final String KEY_EXCEPTION_CAUSE = "cause";
	private static final String KEY_EXCEPTION_TYPE = "type";
	private static final String KEY_EXCEPTION_MESSAGE = "message";
	private static final String KEY_EXCEPTION_STACKTRACE = "stacktrace";

	@Override
	public void logged(LogEntry entry) {
		if (entry == null) {
			return;
		}
		Bundle bundle = entry.getBundle();
		if (bundle == null) {
			return;
		}
		// filter log entries for DevBoost plug-ins
		String symbolicName = bundle.getSymbolicName();
		if (!new FeedbackPluginFilter().isFeedbackPlugin(symbolicName)) {
			return;
		}
		if (entry.getLevel() != LogService.LOG_ERROR) {
			return;
		}
		
		logInternal(symbolicName, entry.getTime(), entry.getMessage(), entry.getException());
	}

	private void logInternal(String symbolicName, long time, String message,
			Throwable exception) {
		// send errors to server
		Properties properties = new Properties();
		properties.put(FeedbackClient.KEY_FEEDBACK_TYPE, "errorreport");
		properties.put(KEY_BUNDLE, symbolicName);
		properties.put(KEY_TIME, Long.toString(time));
		properties.put(KEY_MESSAGE, message);
		
		addExceptionProperties(properties, KEY_EXCEPTION, exception, new LinkedHashSet<Throwable>());
		
		new FeedbackClient().sendPropertiesToServer(properties);
	}

	private void addExceptionProperties(Properties properties, String key, Throwable exception, Set<Throwable> handledExceptions) {
		if (exception == null) {
			return;
		}
		if (handledExceptions.contains(exception)) {
			return;
		}
		handledExceptions.add(exception);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(baos));

		properties.put(key + KEY_EXCEPTION_TYPE, exception.getClass().getName());
		properties.put(key + KEY_EXCEPTION_MESSAGE, exception.getMessage());
		properties.put(key + KEY_EXCEPTION_STACKTRACE, baos.toString());
		
		addExceptionProperties(properties, key + KEY_EXCEPTION_CAUSE, exception.getCause(), handledExceptions);
	}
}