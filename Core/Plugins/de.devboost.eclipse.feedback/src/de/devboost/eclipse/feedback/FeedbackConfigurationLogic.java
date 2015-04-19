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

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class FeedbackConfigurationLogic extends AbstractConfigurationLogic<FeedbackConfigurationData> {

	public FeedbackConfigurationLogic(IConfigurationHandler configurationHandler) {
		super(configurationHandler, getFeedbackConfigurationData(configurationHandler));
	}
	
	@Override
	public boolean isShowingDialogRequired() {
		FeedbackPlugin plugin = FeedbackPlugin.getDefault();
		if (plugin == null) {
			return false;
		}
		
		if (isFeedbackDialogReplaced()) {
			return false;
		}
		
		List<String> pluginPrefixes = IOpenSourcePlugins.DEVBOOST_OPEN_SOURCE_PLUGIN_PREFIXES;
		List<Bundle> bundlesSendingFeedback = new PluginFilter(pluginPrefixes).getMatchingBundles(plugin);
		if (bundlesSendingFeedback.isEmpty()) {
			return false;
		}
		
		IConfigurationHandler configurationHandler = getConfigurationHandler();
		FeedbackConfiguration configuration = configurationHandler.loadConfiguration();
		
		String key = IConfigurationConstants.KEY_SHOWED_OPEN_SOURCE_DIALOG;

		// We must show the Open Source Feedback dialog if it was not shown 
		// before.
		if (configuration != null) {
			Boolean showedDailogBefore = configuration.getBooleanProperty(key);
			if (showedDailogBefore != null && showedDailogBefore) {
				return false;
			}
		} else {
			configuration = new FeedbackConfiguration();
		}
		
		// remember that we've show this dialog
		configuration.getProperties().setProperty(key, Boolean.TRUE.toString());
		configurationHandler.saveConfiguration(configuration);
		return true;
	}

	private boolean isFeedbackDialogReplaced() {
		if (!Platform.isRunning()) {
			return false;
		}
		
		// find dialog replacement deciders
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IConfigurationElement configurationElements[] = extensionRegistry.getConfigurationElementsFor("de.devboost.eclipse.feedback.replacement");
		for (org.eclipse.core.runtime.IConfigurationElement element : configurationElements) {
			try {
				Object extension = element.createExecutableExtension("class");
				if (extension instanceof IFeedbackDialogReplacementDecider) {
					IFeedbackDialogReplacementDecider provider = (IFeedbackDialogReplacementDecider) extension;
					if (provider.isReplaced()) {
						return true;
					}
				}
			} catch (CoreException ce) {
				FeedbackPlugin.logError("Exception while loading feedback replacement decider.", ce);
			}
		}
		
		// no replacement decider found or none of the deciders did replace the
		// feedback dialog
		return false;
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
