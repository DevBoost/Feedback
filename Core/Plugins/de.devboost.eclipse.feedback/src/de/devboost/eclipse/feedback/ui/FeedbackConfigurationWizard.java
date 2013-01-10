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

import de.devboost.eclipse.feedback.FeedbackConfigurationLogic;
import de.devboost.eclipse.feedback.IConfigurationHandler;

public class FeedbackConfigurationWizard extends AbstractConfigurationWizard {
	
	private FeedbackConfigurationPage page = new FeedbackConfigurationPage();
	private FeedbackConfigurationLogic logic;
	
	public FeedbackConfigurationWizard(IConfigurationHandler configurationHandler) {
		super();
		setWindowTitle("Feedback configuration");
		
		// initialize logic
		logic = new FeedbackConfigurationLogic(configurationHandler);
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		String email = page.getEmailAddress();
		if (!page.isSendEmailSelected()) {
			email = "";
		}
		boolean register = page.isRegisterInstallationSelected();
		boolean sendErrors = page.isSendErrorReportsSelected();
		logic.handleResult(email, register, sendErrors);
		return true;
	}

	@Override
	public boolean performCancel() {
		logic.handleResult("", false, false);
		return true;
	}

	public void addCancelListener(ICancelListener listener) {
		getWizardDialog().addCancelListener(listener);
	}
}
