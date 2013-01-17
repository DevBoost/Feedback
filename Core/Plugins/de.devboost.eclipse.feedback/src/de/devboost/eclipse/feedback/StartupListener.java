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
	private FeedbackConfigurationLogic logic;

	@Override
	public void earlyStartup() {
		FeedbackPlugin plugin = FeedbackPlugin.getDefault();
		
		logic = new FeedbackConfigurationLogic(getConfigurationHandler());
		doEarlyStartup(plugin, pluginPrefixes);
	}

	@Override
	protected IConfigurationWizardOpener getWizardOpener() {
		FeedbackConfigurationWizard wizard = new FeedbackConfigurationWizard(logic);
		return new ConfigurationWizardOpener(wizard);
	}

	@Override
	protected IConfigurationHandler createConfigurationHandler() {
		return new ConfigurationHandler(pluginPrefixes);
	}

	@Override
	protected AbstractConfigurationLogic<?> getLogic() {
		return logic;
	}
}
