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

public class FeedbackConfigurationLogic extends AbstractConfigurationLogic {

	public FeedbackConfigurationLogic(IConfigurationHandler configurationHandler) {
		super(configurationHandler);
	}
	
	public void handleResult(String email, boolean register, boolean sendErrors) {
		Properties properties = new Properties();
		createNewConfiguration(email, register, sendErrors, properties);
	}

	private void createNewConfiguration(
			String email, 
			boolean register, 
			boolean sendErrors,
			Properties properties) {
		
		UUID uuid = UUID.randomUUID();
        String guid = uuid.toString();
        
        properties.put(IConfigurationConstants.KEY_EMAIL, email);
        properties.put(IConfigurationConstants.KEY_REGISTER_INSTALLATION, Boolean.toString(register));
        properties.put(IConfigurationConstants.KEY_SEND_ERROR_REPORTS, Boolean.toString(sendErrors));
        properties.put(IConfigurationConstants.KEY_GUID, guid);
        
		FeedbackConfiguration configuration = new FeedbackConfiguration(properties);
		IConfigurationHandler configurationHandler = getConfigurationHandler();
		configurationHandler.saveConfiguration(configuration);
		Boolean isRegisterInstallation = configuration.getBooleanProperty(IConfigurationConstants.KEY_REGISTER_INSTALLATION);
		if (isRegisterInstallation != null && isRegisterInstallation) {
			configurationHandler.sendConfigurationToServer(configuration);
		}
	}
}
