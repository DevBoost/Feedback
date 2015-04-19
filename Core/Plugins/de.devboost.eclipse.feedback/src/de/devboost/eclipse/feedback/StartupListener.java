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

import de.devboost.eclipse.feedback.ui.ConfigurationWizardOpener;
import de.devboost.eclipse.feedback.ui.FeedbackConfigurationWizard;
import de.devboost.eclipse.feedback.ui.IConfigurationWizardOpener;

public class StartupListener extends AbstractStartupListener {

	private FeedbackConfigurationLogic logic;

	public StartupListener() {
		super();
	}

	@Override
	public void earlyStartup() {
		FeedbackPlugin plugin = FeedbackPlugin.getDefault();

		logic = new FeedbackConfigurationLogic(getConfigurationHandler());
		doEarlyStartup(plugin, getPluginPrefixes());
	}

	@Override
	protected IConfigurationWizardOpener getWizardOpener() {
		FeedbackConfigurationWizard wizard = new FeedbackConfigurationWizard(logic);
		return new ConfigurationWizardOpener(wizard);
	}

	@Override
	protected IConfigurationHandler createConfigurationHandler() {
		return new ConfigurationHandler(getPluginPrefixes());
	}

	private List<String> getPluginPrefixes() {
		return IOpenSourcePlugins.DEVBOOST_OPEN_SOURCE_PLUGIN_PREFIXES;
	}

	@Override
	protected AbstractConfigurationLogic<?> getLogic() {
		return logic;
	}
}
