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
import java.util.UUID;

public class FeedbackConfigurationLogic extends AbstractConfigurationLogic<FeedbackConfigurationData> {

	public FeedbackConfigurationLogic(IConfigurationHandler configurationHandler) {
		super(configurationHandler, getFeedbackConfigurationData(configurationHandler));
	}
	
	private static FeedbackConfigurationData getFeedbackConfigurationData(
			IConfigurationHandler configurationHandler) {
		
		FeedbackConfigurationData data = new FeedbackConfigurationData();

		FeedbackConfiguration configuration = configurationHandler.loadConfiguration();
		if (configuration == null) {
			// no configuration found
			return data;
		}
		
		String email = configuration.getStringProperty(IConfigurationConstants.KEY_EMAIL);
		Boolean register = configuration.getBooleanProperty(IConfigurationConstants.KEY_REGISTER_INSTALLATION);
		Boolean sendEmail = configuration.getBooleanProperty(IConfigurationConstants.KEY_SEND_EMAIL);
		Boolean sendErrors = configuration.getBooleanProperty(IConfigurationConstants.KEY_SEND_ERROR_REPORTS);
		
		data.setEmail(email);
		data.setRegister(register);
		data.setSendEmail(sendEmail);
		data.setSendErrors(sendErrors);
		return data;
	}

	public void performFinish() {
		FeedbackConfigurationData data = getData();
		// make sure not to send the email address is sending
		// email was not selected
		String email = data.getEmail();
		Boolean sendEmail = data.isSendEmail();
		if (sendEmail == null || sendEmail == false) {
			email = "";
		}

		updateConfiguration(email, data.isRegister(), data.isSendErrors());
	}

	private void updateConfiguration(
			String email, 
			Boolean register, 
			Boolean sendErrors) {

		IConfigurationHandler configurationHandler = getConfigurationHandler();
		FeedbackConfiguration configuration = configurationHandler.loadConfiguration();
		if (configuration == null) {
			// no configuration found. create a new one.
			configuration = new FeedbackConfiguration();
		}
		

		UUID uuid = UUID.randomUUID();
        String guid = uuid.toString();
        
		Properties properties = configuration.getProperties();
        properties.put(IConfigurationConstants.KEY_EMAIL, email);
        if (register != null) {
            properties.put(IConfigurationConstants.KEY_REGISTER_INSTALLATION, Boolean.toString(register));
		}
        if (sendErrors != null) {
            properties.put(IConfigurationConstants.KEY_SEND_ERROR_REPORTS, Boolean.toString(sendErrors));
		}
        properties.put(IConfigurationConstants.KEY_GUID, guid);
        
		configurationHandler.saveConfiguration(configuration);
		
		Boolean isRegisterInstallation = configuration.getBooleanProperty(IConfigurationConstants.KEY_REGISTER_INSTALLATION);
		if (isRegisterInstallation != null && isRegisterInstallation) {
			configurationHandler.sendConfigurationToServer(configuration);
		}
	}

	@Override
	public void performCancel() {
		// do nothing
	}
}
