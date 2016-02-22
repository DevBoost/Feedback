/*******************************************************************************
 * Copyright (c) 2012-2016
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
package de.devboost.eclipse.feedback.ui;

import java.util.List;

import de.devboost.eclipse.feedback.ConfigurationHandler;
import de.devboost.eclipse.feedback.FeedbackConfigurationLogic;
import de.devboost.eclipse.feedback.IConfigurationHandler;

public class FeedbackConfigurationWizardRunner extends AbstractWizardRunner {

	@Override
	public IConfigurationWizardOpener getWizardOpener(List<String> pluginPrefixes) {
		IConfigurationHandler configurationHandler = new ConfigurationHandler(pluginPrefixes);
		FeedbackConfigurationLogic logic = new FeedbackConfigurationLogic(configurationHandler);
		AbstractConfigurationWizard wizard = new FeedbackConfigurationWizard(logic);
		IConfigurationWizardOpener helper = new ConfigurationWizardOpener(wizard);
		return helper;
	}
}
