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

import java.util.List;

import org.osgi.framework.Bundle;

import de.devboost.eclipse.feedback.ui.ConfigurationWizardOpener;
import de.devboost.eclipse.feedback.ui.FeedbackConfigurationWizard;
import de.devboost.eclipse.feedback.ui.IConfigurationWizardOpener;

public class StartupListener extends AbstractStartupListener {

	private String[] pluginPrefixes = IOpenSourcePlugins.DEVBOOST_OPEN_SOURCE_PLUGIN_PREFIXES;

	@Override
	public void earlyStartup() {
		FeedbackPlugin plugin = FeedbackPlugin.getDefault();
		
		doEarlyStartup(plugin, pluginPrefixes);
	}

	@Override
	protected IConfigurationWizardOpener getWizardOpener() {
		FeedbackConfigurationWizard wizard = new FeedbackConfigurationWizard(getConfigurationHandler());
		return new ConfigurationWizardOpener(wizard);
	}

	@Override
	protected boolean isShowingDialogRequired() {
		FeedbackPlugin plugin = FeedbackPlugin.getDefault();
		if (plugin == null) {
			return false;
		}
		
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

	@Override
	protected IConfigurationHandler createConfigurationHandler() {
		return new ConfigurationHandler(pluginPrefixes);
	}
}
