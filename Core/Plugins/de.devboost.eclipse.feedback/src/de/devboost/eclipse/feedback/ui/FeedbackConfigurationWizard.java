package de.devboost.eclipse.feedback.ui;

import java.util.Date;
import java.util.UUID;

import org.eclipse.jface.wizard.Wizard;

import de.devboost.eclipse.feedback.FeedbackConfiguration;
import de.devboost.eclipse.feedback.FeedbackConfigurationHandler;

public class FeedbackConfigurationWizard extends Wizard {
	
	private FeedbackConfigurationPage page = new FeedbackConfigurationPage();
	private CustomWizardDialog wizardDialog;
	
	public FeedbackConfigurationWizard() {
		super();
		setWindowTitle("Feedback configuration");
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
		handleResult(email, register, sendErrors);
		return true;
	}

	@Override
	public boolean performCancel() {
		handleResult("", false, false);
		return true;
	}

	private void handleResult(String email, boolean register, boolean sendErrors) {
		UUID uuid = UUID.randomUUID();
        String guid = uuid.toString();
		FeedbackConfiguration configuration = new FeedbackConfiguration(guid, email, register, sendErrors, new Date());
		new FeedbackConfigurationHandler().setConfiguration(configuration);
	}

	public void addCancelListener(ICancelListener listener) {
		this.getWizardDialog().addCancelListener(listener);
	}

	public CustomWizardDialog getWizardDialog() {
		return wizardDialog;
	}

	public void setWizardDialog(CustomWizardDialog wizardDialog) {
		this.wizardDialog = wizardDialog;
	}
}
