package de.devboost.eclipse.feedback;

import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;

class FeedbackLogListener implements LogListener {

	private static final String KEY_BUNDLE = "bundle";
	private static final String KEY_TIME = "time";
	private static final String KEY_MESSAGE = "message";

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
		
		PropertyCreator propertyCreator = new PropertyCreator();
		propertyCreator.addInstalledBundles(properties);
		propertyCreator.addException(properties, exception);
		
		new FeedbackClient().sendPropertiesToServer(properties);
	}
}