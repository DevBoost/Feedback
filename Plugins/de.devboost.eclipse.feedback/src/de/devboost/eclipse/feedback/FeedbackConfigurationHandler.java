package de.devboost.eclipse.feedback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

public class FeedbackConfigurationHandler {

	private static final String FEEDBACK_URL = "http://www.devboost.de/eclipse-feedback/";
	
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
		properties.put(KEY_GUID, configuration.getGuid());
		properties.put(KEY_EMAIL, configuration.getEmail());
		properties.put(KEY_SEND_ERROR_REPORTS, Boolean.toString(configuration.isSendErrorReports()));
		
		// send list of installed DevBoost plug-ins and versions
		FeedbackPlugin feedbackPlugin = FeedbackPlugin.getDefault();
		List<Bundle> bundlesSendingFeedback = new FeedbackPluginFilter().getBundlesSendingFeedback(feedbackPlugin);
		
		for (int i = 0; i < bundlesSendingFeedback.size(); i++) {
			Bundle bundle = bundlesSendingFeedback.get(i);
			Version version = bundle.getVersion();
			String symbolicName = bundle.getSymbolicName();
			String versionString = version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
			String qualifierString = version.getQualifier();
			properties.put("bundle." + i + ".name", symbolicName == null ? "null" : symbolicName);
			properties.put("bundle." + i + ".version", versionString);
			properties.put("bundle." + i + ".qualifier", qualifierString == null ? "null" : qualifierString);
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			properties.storeToXML(baos, "");
			sendXmlOverHttp(baos.toString());
		} catch (IOException e) {
			FeedbackPlugin.logError("Could not send DevBoost feedback configuration", e);
		}
	}

	protected void sendXmlOverHttp(String xmlString) throws IOException {
		// set parameters
        String data = "data=" + URLEncoder.encode(xmlString, "UTF-8");

		URLConnection connection = new URL(FEEDBACK_URL).openConnection();
		if (connection instanceof HttpURLConnection) {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setInstanceFollowRedirects(false); 
			httpConnection.setRequestMethod("POST"); 
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			httpConnection.setRequestProperty("charset", "utf-8");
			httpConnection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
			httpConnection.setUseCaches(false);
			httpConnection.setConnectTimeout(2000);
            // connect
            httpConnection.connect();
			// send post request
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            // write parameters
            writer.write(data);
            writer.flush();
            
            httpConnection.getResponseCode();
			// close connection
    		httpConnection.disconnect();
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
