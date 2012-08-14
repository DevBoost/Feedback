package de.devboost.eclipse.feedback.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


import de.devboost.eclipse.feedback.FeedbackPlugin;

// TODO fix texts
class FeedbackConfigurationPage extends WizardPage implements ICancelListener {

	private static final String PAGE_TITLE = "DevBoost Feedback Configuration";
	private static final String PAGE_TITLE_2 = "DevBoost Open-Source Software Feedback Configuration";
	private static final String DEFAULT_EMAIL_ADDRESS = "yourname@yourcompany.com";

	private Text emailText;
	private Button registerInstallationButton;
	private Button sendErrorsButton;
	private Image happyImage;
	private Image sadImage;
	private Label imageLabel;

	public FeedbackConfigurationPage() {
		// TODO call super with image descriptor
		super(PAGE_TITLE);
		setTitle(PAGE_TITLE_2);
		ImageData happyData = new ImageData(FeedbackPlugin.class.getResourceAsStream("HappyGirl.jpg"));
		happyImage = new Image(Display.getCurrent(), happyData);
		sadImage = new Image(Display.getDefault(),
				FeedbackPlugin.class.getResourceAsStream("SadBoy.jpg"));
	
	}

	@Override
	public void createControl(Composite parent) {
		Composite panel = new Composite(parent, SWT.FILL);
		GridLayout gl = new GridLayout(2, false);
		panel.setLayout(gl);
		createImagePanel(panel);

		Text messageText = new Text(panel, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
		messageText.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));
		messageText
				.setText("Congratulations! You're using Open-Source software provided by DevBoost.\n\n"
						+ "We hope you find our tools useful, that we can increase your productivity and\n"
						+ "that you enjoy using our tools as much as we do creating them.\n\n"
						+ "As a tiny bit of return, we kindly ask you to register this installation and enable\n"
						+ "error feedback reporting to help us making our tools even better. Of course, this\n"
						+ "is optional. If you decide to cancel this dialog, you won't "
						+ "be bothered again.\n\n" + "Thank you!");
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		messageText.setLayoutData(gd);
		createEmailPanel(panel);

		registerInstallationButton = new Button(panel, SWT.CHECK);
		registerInstallationButton
				.setText("Register this installation of DevBoost tools");
		registerInstallationButton
				.setToolTipText("No further information is required.");
		registerInstallationButton.setSelection(true);

		sendErrorsButton = new Button(panel, SWT.CHECK);
		sendErrorsButton.setText("Send reports about fatal errors to DevBoost");
		sendErrorsButton
				.setToolTipText("Error reports include stack trace information only. No personal data is sent to DevBoost.");
		sendErrorsButton.setSelection(true);

		setControl(panel);
		FeedbackConfigurationWizard feedbackConfigurationWizard = (FeedbackConfigurationWizard) getWizard();
		feedbackConfigurationWizard.addCancelListener(this);
	}

	private void createImagePanel(Composite panel) {
		
		imageLabel = new Label(panel, SWT.NONE);
		imageLabel.setImage(happyImage);
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.TOP;
		gd.verticalSpan = 4;
		imageLabel.setLayoutData(gd);

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

	@Override
	public void cancelExit() {
		imageLabel.setImage(happyImage);
	}

	@Override
	public void cancelEnter() {
		imageLabel.setImage(sadImage);
	}

}
