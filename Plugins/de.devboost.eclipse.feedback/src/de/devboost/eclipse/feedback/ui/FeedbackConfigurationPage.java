package de.devboost.eclipse.feedback.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

class FeedbackConfigurationPage extends WizardPage {

	private static final String PAGE_TITLE = "DevBoost Feedback Configuration";
	private static final String PAGE_TITLE_2 = "DevBoost Open-Source Software Feedback Configuration";
	private static final String DEFAULT_EMAIL_ADDRESS = "yourname@yourcompany.com";
	
	private Text emailText;
	private Button registerInstallationButton;
	private Button sendErrorsButton;

	public FeedbackConfigurationPage() {
		// TODO call super with image descriptor
		super(PAGE_TITLE);
		setTitle(PAGE_TITLE_2);
	}

	@Override
	public void createControl(Composite parent) {
		Composite panel = new Composite(parent, SWT.FILL);
		GridLayout gl = new GridLayout();
		panel.setLayout(gl);
		
		Text messageText = new Text(panel, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
		messageText.setText(
			"Congratulations! You're using Open-Source software provided by DevBoost.\n\n" +
			"We hope you find our tools useful, that we can increase your productivity and\n" +
			"that you enjoy using our tools as much as we do creating them.\n\n" +
			"As a tiny bit of return, we kindly ask you to register this installation and enable\n" +
			"error feedback reporting to help us making our tools even better. Of course, this\n" +
			"is optional. If you decide to cancel this dialog, you won't " +
			"be bothered again.\n\n" +
			"Thank you!"
		);
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		messageText.setLayoutData(gd);
		
		createEmailPanel(panel);

		registerInstallationButton = new Button(panel, SWT.CHECK);
		registerInstallationButton.setText("Register this installation of DevBoost tools");
		registerInstallationButton.setToolTipText("No further information is required.");
		registerInstallationButton.setSelection(true);

		sendErrorsButton = new Button(panel, SWT.CHECK);
		sendErrorsButton.setText("Send reports about fatal errors to DevBoost");
		sendErrorsButton.setToolTipText("Error reports include stack trace information only. No personal data is sent to DevBoost.");
		sendErrorsButton.setSelection(true);
		
		setControl(panel);
	}

	private Composite createEmailPanel(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayout(new GridLayout(2, false));

		Label emailLabel = new Label(panel, SWT.NONE);
		emailLabel.setText("Email address:");
		
		emailText = new Text(panel, SWT.BORDER);
		emailText.setText(DEFAULT_EMAIL_ADDRESS);
		emailText.setFocus();
		return panel;
	}
	
	public String getEmailAddress() {
		return emailText.getText();
	}
	
	public boolean isRegisterInstallationSelected() {
		return registerInstallationButton.getSelection();
	}
	
	public boolean isSendErrorReportsSelected() {
		return sendErrorsButton.getSelection();
	}
}
