/*******************************************************************************
 * Copyright (c) 2012-2015
 * DevBoost GmbH, Dresden, Amtsgericht Dresden, HRB 34001
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   DevBoost GmbH - Dresden, Germany
 *      - initial API and implementation
 ******************************************************************************/
package de.devboost.eclipse.feedback;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * The PropertyCreator wraps data into Properties objects. This way, information can be easily sent to the server.
 */
class PropertyCreator {

	private static final String KEY_BUNDLE = "bundle";

	private static final String KEY_EXCEPTION = "exception";
	private static final String KEY_EXCEPTION_CAUSE = "cause";
	private static final String KEY_EXCEPTION_TYPE = "type";
	private static final String KEY_EXCEPTION_MESSAGE = "message";
	private static final String KEY_EXCEPTION_STACKTRACE = "stacktrace";

	private List<String> pluginPrefixes;

	public PropertyCreator(List<String> pluginPrefixes) {
		super();
		this.pluginPrefixes = pluginPrefixes;
	}

	public void addInstalledBundles(Properties properties) {
		// send list of installed DevBoost plug-ins and versions
		FeedbackPlugin feedbackPlugin = FeedbackPlugin.getDefault();
		List<Bundle> bundlesSendingFeedback = new PluginFilter(pluginPrefixes).getMatchingBundles(feedbackPlugin);

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

		String stackTrace = new ExceptionHelper().getStackTrace(exception);

		String exceptionClassName = exception.getClass().getName();
		String message = exception.getMessage();
		properties.put(key + "." + KEY_EXCEPTION_TYPE, exceptionClassName == null ? "null" : exceptionClassName);
		properties.put(key + "." + KEY_EXCEPTION_MESSAGE, message == null ? "null" : exceptionClassName);
		properties.put(key + "." + KEY_EXCEPTION_STACKTRACE, stackTrace);

		addException(properties, key + "." + KEY_EXCEPTION_CAUSE, exception.getCause(), handledExceptions);
	}
}
