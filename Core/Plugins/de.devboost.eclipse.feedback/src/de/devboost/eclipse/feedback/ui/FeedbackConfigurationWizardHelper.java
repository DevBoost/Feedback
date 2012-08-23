package de.devboost.eclipse.feedback.ui;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class FeedbackConfigurationWizardHelper {

	public void showFeedbackConfigurationWizardDialog(Shell shell) {
		WizardDialog wizardDialog = new CustomWizardDialog(shell, new FeedbackConfigurationWizard());
		wizardDialog.setBlockOnOpen(true);
		wizardDialog.open();
	}
}
