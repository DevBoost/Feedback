/*******************************************************************************
 * Copyright (c) 2012-2013
 * DevBoost GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   DevBoost GmbH - Berlin, Germany
 *      - initial API and implementation
 ******************************************************************************/
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

	private String[] pluginPrefixes;
	private IConfigurationHandler configurationHandler;
	
	public FeedbackLogListener(String[] pluginPrefixes, IConfigurationHandler configurationHandler) {
		super();
		this.pluginPrefixes = pluginPrefixes;
		this.configurationHandler = configurationHandler;
	}

	@Override
	public void logged(LogEntry entry) {
		if (entry == null) {
			return;
		}
		Bundle bundle = entry.getBundle();
		if (bundle == null) {
			return;
		}
		// check whether error reporting is enabled
		FeedbackConfiguration configuration = configurationHandler.loadConfiguration();
		if (configuration == null) {
			return;
		}
		if (!configuration.isSendErrorReports()) {
			return;
		}
		// filter log entries for DevBoost plug-ins
		String symbolicName = bundle.getSymbolicName();
		String stackTrace = new ExceptionHelper().getStackTrace(entry.getException());
		// do not try to process log entries of the feedback plug-in itself,
		// because if this fails, a new log entry is created, which leads to
		// an infinite loop, because this listener is notified again.
		if ("de.devboost.eclipse.feedback".equals(symbolicName)) {
			return;
		}
		if (!new PluginFilter(pluginPrefixes).containsPluginPrefix(stackTrace)) {
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
		
		PropertyCreator propertyCreator = new PropertyCreator(pluginPrefixes);
		propertyCreator.addInstalledBundles(properties);
		propertyCreator.addException(properties, exception);
		
		new FeedbackClient().sendPropertiesToServer(properties);
	}
}
