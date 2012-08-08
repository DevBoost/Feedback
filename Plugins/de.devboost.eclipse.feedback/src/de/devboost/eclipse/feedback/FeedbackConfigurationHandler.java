package de.devboost.eclipse.feedback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;

public class FeedbackConfigurationHandler {

	private static final String SYSTEM_PROPERTY_USER_DIR = "user.dir";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_REGISTER_INSTALLATION = "register_installation";
	private static final String KEY_SEND_ERROR_REPORTS = "send_error_reports";
	private static final String KEY_DATE = "date";

	public void setConfiguration(FeedbackConfiguration configuration) {
		saveConfiguration(configuration);
		if (configuration.isRegisterInstallation()) {
			// TODO register installation
		}
	}

	/**
	 * Saves the given configuration to a properties file in 'user.home'.
	 */
	private void saveConfiguration(FeedbackConfiguration configuration) {
		String email = configuration.getEmail();
		boolean register = configuration.isRegisterInstallation();
		boolean sendErrors = configuration.isSendErrorReports();

		Properties properties = new Properties();
		properties.setProperty(KEY_EMAIL, email);
		properties.setProperty(KEY_REGISTER_INSTALLATION, Boolean.toString(register));
		properties.setProperty(KEY_SEND_ERROR_REPORTS, Boolean.toString(sendErrors));
		properties.setProperty(KEY_DATE, Long.toString(new Date().getTime()));
		
		File file = getConfigFile();
		try {
			Writer out = new FileWriter(file);
			properties.store(out, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private File getConfigFile() {
		File userDir = new File(System.getProperty(SYSTEM_PROPERTY_USER_DIR));
		File file = new File(userDir, ".devboost-os-tools");
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
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));

			String email = properties.getProperty(KEY_EMAIL);
			boolean register = Boolean.parseBoolean(properties.getProperty(KEY_REGISTER_INSTALLATION));
			boolean sendErrors = Boolean.parseBoolean(properties.getProperty(KEY_SEND_ERROR_REPORTS));
			Date date = new Date(Long.parseLong(properties.getProperty(KEY_DATE)));
			return new FeedbackConfiguration(email, register, sendErrors, date);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
