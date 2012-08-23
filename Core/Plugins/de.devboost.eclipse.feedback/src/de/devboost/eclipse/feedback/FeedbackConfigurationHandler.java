/*******************************************************************************
 * Copyright (c) 2012
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;

public class FeedbackConfigurationHandler {

	private static final String CONFIG_FILE_NAME = ".devboost-open-source-tools";
	private static final String SYSTEM_PROPERTY_USER_DIR = "user.home";
	
	private static final String KEY_GUID = "guid";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_REGISTER_INSTALLATION = "register_installation";
	private static final String KEY_SEND_ERROR_REPORTS = "send_error_reports";
	private static final String KEY_DATE = "date";

	public void setConfiguration(FeedbackConfiguration configuration) {
		saveConfiguration(configuration);
		if (configuration.isRegisterInstallation()) {
			sendConfigurationToServer(configuration);
		}
	}

	private void sendConfigurationToServer(FeedbackConfiguration configuration) {
		// register installation
		Properties properties = new Properties();
		properties.put(FeedbackClient.KEY_FEEDBACK_TYPE, "registration");
		properties.put(KEY_GUID, configuration.getGuid());
		properties.put(KEY_EMAIL, configuration.getEmail());
		properties.put(KEY_SEND_ERROR_REPORTS, Boolean.toString(configuration.isSendErrorReports()));
		
		new PropertyCreator().addInstalledBundles(properties);
		
		new FeedbackClient().sendPropertiesToServer(properties);
	}

	/**
	 * Saves the given configuration to a properties file in 'user.home'.
	 */
	private void saveConfiguration(FeedbackConfiguration configuration) {
		String email = configuration.getEmail();
		String guid = configuration.getGuid();
		boolean register = configuration.isRegisterInstallation();
		boolean sendErrors = configuration.isSendErrorReports();

		Properties properties = new Properties();
		properties.setProperty(KEY_EMAIL, email);
		properties.setProperty(KEY_GUID, guid);
		properties.setProperty(KEY_REGISTER_INSTALLATION, Boolean.toString(register));
		properties.setProperty(KEY_SEND_ERROR_REPORTS, Boolean.toString(sendErrors));
		properties.setProperty(KEY_DATE, Long.toString(new Date().getTime()));
		
		File file = getConfigFile();
		try {
			Writer out = new FileWriter(file);
			properties.store(out, "");
		} catch (IOException e) {
			FeedbackPlugin.logError("Could not save DevBoost feedback configuration", e);
		}
	}

	private File getConfigFile() {
		File userDir = new File(System.getProperty(SYSTEM_PROPERTY_USER_DIR));
		File file = new File(userDir, CONFIG_FILE_NAME);
		return file;
	}

	/**
	 * Reads the configuration from a properties file in 'user.home'.
	 * 
	 * @param email
	 * @param register
	 * @param sendErrors
	 */
	public FeedbackConfiguration loadConfiguration() {
		File file = getConfigFile();
		if (!file.exists()) {
			return null;
		}
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));

			String guid = properties.getProperty(KEY_GUID);
			String email = properties.getProperty(KEY_EMAIL);
			boolean register = Boolean.parseBoolean(properties.getProperty(KEY_REGISTER_INSTALLATION));
			boolean sendErrors = Boolean.parseBoolean(properties.getProperty(KEY_SEND_ERROR_REPORTS));
			Date date = new Date(Long.parseLong(properties.getProperty(KEY_DATE)));
			return new FeedbackConfiguration(guid, email, register, sendErrors, date);
		} catch (IOException e) {
			FeedbackPlugin.logInfo("Could not load DevBoost feedback configuration", e);
			return null;
		}
	}
}
