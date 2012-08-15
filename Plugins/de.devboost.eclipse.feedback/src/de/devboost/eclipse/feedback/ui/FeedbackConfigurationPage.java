package de.devboost.eclipse.feedback.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
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

	private static final String DEVBOOST_LOGO = "http://www.devboost.de/eclipse-feedback/logo/";
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

		URL url;
		try {
			url = new URL(DEVBOOST_LOGO);
			ImageDescriptor imageDescriptor = ImageDescriptor
					.createFromURL(url);

			if (imageDescriptor != null
					&& imageDescriptor.getImageData() != null) {
				super.setImageDescriptor(imageDescriptor);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ImageData happyData = new ImageData(
				FeedbackPlugin.class.getResourceAsStream("HappyGirl.jpg"));
		happyImage = new Image(Display.getCurrent(), happyData);
		sadImage = new Image(Display.getDefault(),
				FeedbackPlugin.class.getResourceAsStream("SadBoy.jpg"));

	}

	@Override
	public void createControl(Composite parent) {
		Composite panel = new Composite(parent, SWT.FILL);
		GridLayout gl = new GridLayout(2, false);
		panel.setLayout(gl);

		Text messageText = new Text(panel, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
		messageText.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));
		messageText
				.setText("Thanks a lot!\n\n"
						+ "You're using Open-Source software provided by DevBoost.\n"
						+ "We hope you find our tools useful, that we can increase your\n"
						+ "productivity, and that you enjoy using our tools as much as\n"
						+ "we do creating them. To help us to continuiously improve\n"
						+ "our tools, we kindly ask you to register this installation\n"
						+ "and to enable error feedback reporting.\n\n"

						+ "Of course, this is optional. If you decide to cancel this dialog, \n"
						+ "you won't be bothered again.\n\n" + "Enjoy.");
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		messageText.setLayoutData(gd);
		createImagePanel(panel);

		registerInstallationButton = new Button(panel, SWT.CHECK);
		registerInstallationButton
				.setText("Register this installation of DevBoost tools");
		registerInstallationButton
				.setToolTipText("No further information is required.");
		registerInstallationButton.setSelection(true);
		gd = new GridData();
		gd.horizontalSpan = 1;
		registerInstallationButton.setLayoutData(gd);
		createEmailPanel(panel);

		sendErrorsButton = new Button(panel, SWT.CHECK);
		sendErrorsButton.setText("Send reports about fatal errors to DevBoost");
		sendErrorsButton
				.setToolTipText("Error reports include stack trace information only. No personal data is sent to DevBoost.");
		sendErrorsButton.setSelection(true);
		gd = new GridData();
		gd.horizontalSpan = 2;
		sendErrorsButton.setLayoutData(gd);
		// createImmediateFeedbackPane(panel);

		setControl(panel);
		FeedbackConfigurationWizard feedbackConfigurationWizard = (FeedbackConfigurationWizard) getWizard();
		feedbackConfigurationWizard.addCancelListener(this);
	}

//	private void createImmediateFeedbackPane(Composite panel) {
//		Text feedback = new Text(panel, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
//		feedback.setText("Here is some room for immediate feedback. If you like.");
//		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
//		gd.horizontalSpan = 2;
//		gd.heightHint = 50;
//		feedback.setLayoutData(gd);
//	}

	private void createImagePanel(Composite panel) {
		imageLabel = new Label(panel, SWT.NONE);
		imageLabel.setImage(happyImage);
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.TOP;
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
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		panel.setLayoutData(gd);
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
