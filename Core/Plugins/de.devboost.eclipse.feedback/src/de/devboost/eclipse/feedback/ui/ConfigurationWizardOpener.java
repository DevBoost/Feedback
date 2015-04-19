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
package de.devboost.eclipse.feedback.ui;

import org.eclipse.swt.widgets.Shell;

public class ConfigurationWizardOpener implements IConfigurationWizardOpener {

	private AbstractConfigurationWizard wizard;

	public ConfigurationWizardOpener(AbstractConfigurationWizard wizard) {
		super();
		this.wizard = wizard;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.devboost.eclipse.feedback.ui.ConfigurationWizardOpener#showConfigurationWizardDialog(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	public void showConfigurationWizardDialog(Shell shell) {
		CustomWizardDialog wizardDialog = new CustomWizardDialog(shell, wizard);
		wizardDialog.setBlockOnOpen(true);
		if (wizard instanceof IShowWizardPageListener) {
			IShowWizardPageListener nextPageListener = (IShowWizardPageListener) wizard;
			wizardDialog.setNextPageListener(nextPageListener);
		}
		wizardDialog.open();
	}
}
