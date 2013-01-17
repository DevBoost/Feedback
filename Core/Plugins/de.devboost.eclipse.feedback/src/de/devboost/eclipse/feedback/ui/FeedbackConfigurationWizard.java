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

import java.net.MalformedURLException;

import org.eclipse.jface.resource.ImageDescriptor;

import de.devboost.eclipse.feedback.FeedbackConfigurationLogic;
import de.devboost.eclipse.feedback.FeedbackPlugin;
import de.devboost.eclipse.feedback.IConfigurationHandler;
import de.devboost.eclipse.feedback.util.ImageHelper;

public class FeedbackConfigurationWizard extends AbstractConfigurationWizard 
	implements ICancelListenable {
	
	private static final String DEVBOOST_LOGO = "http://www.devboost.de/eclipse-feedback/logo/";

	private FeedbackConfigurationPage page;
	private FeedbackConfigurationLogic logic;
	
	public FeedbackConfigurationWizard(IConfigurationHandler configurationHandler) {
		super();
		setWindowTitle("Feedback configuration");
		
		ImageDescriptor devBoostLogo = null;
		try {
			devBoostLogo = new ImageHelper().getImage(DEVBOOST_LOGO);
		} catch (MalformedURLException e) {
			FeedbackPlugin.logError("Can't load DevBoost logo: " + e.getMessage(), e);
		}
		page = new FeedbackConfigurationPage(devBoostLogo);

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
		boolean register = page.isRegisterInstallationSelected();
		boolean sendErrors = page.isSendErrorReportsSelected();
		
		logic.getData().setSendEmail(page.isSendEmailSelected());
		logic.getData().setEmail(email);
		logic.getData().setRegister(register);
		logic.getData().setSendErrors(sendErrors);
		
		logic.performFinish();
		return true;
	}

	@Override
	public boolean performCancel() {
		logic.performCancel();
		return true;
	}

	public void addCancelListener(ICancelListener listener) {
		getWizardDialog().addCancelListener(listener);
	}
}
