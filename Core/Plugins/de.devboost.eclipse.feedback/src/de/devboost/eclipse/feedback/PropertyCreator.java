package de.devboost.eclipse.feedback;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * The PropertyCreator wraps data into Properties objects. This way, information
 * can be easily sent to the server.
 */
class PropertyCreator {

	private static final String KEY_BUNDLE = "bundle";

	private static final String KEY_EXCEPTION = "exception";
	private static final String KEY_EXCEPTION_CAUSE = "cause";
	private static final String KEY_EXCEPTION_TYPE = "type";
	private static final String KEY_EXCEPTION_MESSAGE = "message";
	private static final String KEY_EXCEPTION_STACKTRACE = "stacktrace";

	public void addInstalledBundles(Properties properties) {
		// send list of installed DevBoost plug-ins and versions
		FeedbackPlugin feedbackPlugin = FeedbackPlugin.getDefault();
		List<Bundle> bundlesSendingFeedback = new FeedbackPluginFilter().getBundlesSendingFeedback(feedbackPlugin);
		
		for (int i = 0; i < bundlesSendingFeedback.size(); i++) {
			Bundle bundle = bundlesSendingFeedback.get(i);
			Version version = bundle.getVersion();
			String symbolicName = bundle.getSymbolicName();
			String versionString = version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
			String qualifierString = version.getQualifier();
			properties.put(KEY_BUNDLE + "." + i + ".name", symbolicName == null ? "null" : symbolicName);
			properties.put(KEY_BUNDLE + "." + i + ".version", versionString);
			properties.put(KEY_BUNDLE + "." + i + ".qualifier", qualifierString == null ? "null" : qualifierString);
		}
	}
	
	public void addException(Properties properties, Throwable exception) {
		addException(properties, KEY_EXCEPTION, exception, new LinkedHashSet<Throwable>());
	}

	private void addException(Properties properties, String key, Throwable exception, Set<Throwable> handledExceptions) {
		if (exception == null) {
			return;
		}
		if (handledExceptions.contains(exception)) {
			return;
		}
		handledExceptions.add(exception);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(baos));

		properties.put(key + "." + KEY_EXCEPTION_TYPE, exception.getClass().getName());
		properties.put(key + "." + KEY_EXCEPTION_MESSAGE, exception.getMessage());
		properties.put(key + "." + KEY_EXCEPTION_STACKTRACE, baos.toString());
		
		addException(properties, key + "." + KEY_EXCEPTION_CAUSE, exception.getCause(), handledExceptions);
	}
}