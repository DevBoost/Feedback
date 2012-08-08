package de.devboost.eclipse.feedback.ui;

import java.util.Date;

import org.eclipse.jface.wizard.Wizard;

import de.devboost.eclipse.feedback.FeedbackConfiguration;
import de.devboost.eclipse.feedback.FeedbackPlugin;

public class FeedbackConfigurationWizard extends Wizard {
	
	private FeedbackConfigurationPage page = new FeedbackConfigurationPage();

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
		handleResult(email, register, sendErrors);
		return true;
	}

	@Override
	public boolean performCancel() {
		handleResult("", false, false);
		return true;
	}

	private void handleResult(String email, boolean register, boolean sendErrors) {
		FeedbackConfiguration configuration = new FeedbackConfiguration(email, register, sendErrors, new Date());
		FeedbackPlugin feedbackPlugin = FeedbackPlugin.getDefault();
		if (feedbackPlugin != null) {
			feedbackPlugin.getConfigurationHandler().setConfiguration(configuration );
		}
	}
}
