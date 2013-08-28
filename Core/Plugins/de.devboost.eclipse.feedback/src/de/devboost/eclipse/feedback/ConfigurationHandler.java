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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;

/**
 * A {@link ConfigurationHandler} can be used to load and store configurations.
 * It can also send configuration data to the DevBoost server.
 */
public class ConfigurationHandler implements IConfigurationHandler {

	private String[] pluginPrefixes;
	
	public ConfigurationHandler(String[] pluginPrefixes) {
		super();
		this.pluginPrefixes = pluginPrefixes;
	}

	@Override
	public void sendConfigurationToServer(FeedbackConfiguration configuration) {
		// register installation. we do not send all properties!
		Properties properties = new Properties();
		properties.put(FeedbackClient.KEY_FEEDBACK_TYPE, "registration");
		properties.put(IConfigurationConstants.KEY_GUID, configuration.getStringProperty(IConfigurationConstants.KEY_GUID));
		properties.put(IConfigurationConstants.KEY_EMAIL, configuration.getStringProperty(IConfigurationConstants.KEY_EMAIL));
		properties.put(IConfigurationConstants.KEY_SEND_ERROR_REPORTS, Boolean.toString(configuration.getBooleanProperty(IConfigurationConstants.KEY_SEND_ERROR_REPORTS)));
		
		new PropertyCreator(pluginPrefixes).addInstalledBundles(properties);
		
		new FeedbackClient().sendPropertiesToServer(properties);
	}

	/**
	 * Saves the given configuration to a properties file in 'user.home'.
	 */
	@Override
	public void saveConfiguration(FeedbackConfiguration configuration) {
		Properties properties = configuration.getProperties();
		// refresh save date
		properties.setProperty(IConfigurationConstants.KEY_DATE, Long.toString(new Date().getTime()));
		
		File file = getConfigFile();
		try {
			Writer writer = new FileWriter(file);
			properties.store(writer, "");
		} catch (IOException e) {
			FeedbackPlugin.logError("Could not save DevBoost configuration", e);
		}
	}

	private File getConfigFile() {
		File userDir = new File(System.getProperty(IConfigurationConstants.SYSTEM_PROPERTY_USER_DIR));
		File file = new File(userDir, IConfigurationConstants.CONFIG_FILE_NAME);
		return file;
	}

	@Override
	public FeedbackConfiguration loadConfiguration() {
		File file = getConfigFile();
		if (!file.exists()) {
			return null;
		}

		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInputStream);
			FeedbackConfiguration configuration = new FeedbackConfiguration();
			configuration.getProperties().putAll(properties);
			return configuration;
		} catch (IOException e) {
			FeedbackPlugin.logInfo("Could not load DevBoost feedback configuration", e);
			return null;
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					FeedbackPlugin.logInfo("Could not close input stream for DevBoost feedback configuration", e);
				}
			}
		}
	}
}
