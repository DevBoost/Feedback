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
		IConfigurationHandler configurationHandler = getConfigurationHandler();
		FeedbackConfiguration configuration = configurationHandler.loadConfiguration();
		// TODO this is wrong. we must show the Open Source Feedback dialog if
		// it was either completed before or cancelled. Checking the configuration
		// for null is not working, because there might be a configuration
		// create by commercial product configuration dialogs
		if (configuration != null) {
			return false;
		}
		return true;
	}

	@Override
	protected IConfigurationHandler createConfigurationHandler() {
		return new ConfigurationHandler(pluginPrefixes);
	}
}
