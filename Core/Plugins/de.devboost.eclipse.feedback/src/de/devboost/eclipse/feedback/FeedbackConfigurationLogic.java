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

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class FeedbackConfigurationLogic {

	private IConfigurationHandler configurationHandler;
	
	public FeedbackConfigurationLogic(IConfigurationHandler configurationHandler) {
		super();
		this.configurationHandler = configurationHandler;
	}
	
	public IConfigurationHandler getConfigurationHandler() {
		return configurationHandler;
	}

	public void handleResult(String email, boolean register, boolean sendErrors) {
        // TODO add license properties
		Properties properties = new Properties();
		createNewConfiguration(email, register, sendErrors, properties);
	}

	protected void createNewConfiguration(String email, boolean register, 
			boolean sendErrors,
			Properties properties) {
		UUID uuid = UUID.randomUUID();
        String guid = uuid.toString();
		FeedbackConfiguration configuration = new FeedbackConfiguration(guid, email, register, sendErrors, new Date(), properties);
		configurationHandler.saveConfiguration(configuration);
	}
}
