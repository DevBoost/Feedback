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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

class FeedbackConfigurationPage extends WizardPage implements ICancelListener {

	private static final String PAGE_TITLE = "DevBoost Feedback Configuration";
	private static final String PAGE_TITLE_2 = "DevBoost Open-Source Software Feedback Configuration";

	private Text emailText;
	private Button registerInstallationButton;
	private Button sendErrorsButton;
	private Image happyImage;
	private Image sadImage;
	private Label imageLabel;
	private Button sendEmailButton;

	public FeedbackConfigurationPage(ImageDescriptor imageDescriptor) {
		super(PAGE_TITLE, PAGE_TITLE_2, imageDescriptor);

		// TODO Why are these two images created in a different way?
		ImageData happyData = new ImageData(
				FeedbackPlugin.class.getResourceAsStream("HappyGirl.jpg"));
		happyImage = new Image(Display.getCurrent(), happyData);
		sadImage = new Image(Display.getDefault(),
				FeedbackPlugin.class.getResourceAsStream("SadBoy.jpg"));
	}

	@Override
	public void createControl(Composite parent) {
		final int columnCount = 2;
		Composite panel = new Composite(parent, SWT.FILL);
		GridLayout gl = new GridLayout(columnCount, false);
		panel.setLayout(gl);

		Text messageText = new Text(panel, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
		messageText.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));
		messageText
				.setText("Thanks a lot!\n\n"
						+ "You're using Open-Source software provided by DevBoost.\n"
						+ "We hope you find our tools useful, that we can increase your\n"
						+ "productivity, and that you enjoy using our tools as much as\n"
						+ "we do creating them. To help us to continuously improve\n"
						+ "our tools, we kindly ask you to register this installation\n"
						+ "and to enable error feedback reporting. If you want to keep\n"
						+ "up to date with our latest progress you can also enter\n"
						+ "your email address below.\n\n"

						+ "Of course, all this is optional. If you decide to cancel this\n"
						+ "dialog, you won't be bothered again.\n\n" + "Enjoy.");
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.heightHint = 300;
		messageText.setLayoutData(gd);
		createImagePanel(panel);

		createRegistrationComposite(panel);
		new UIFragmentHelper().createDevBoostHyperlink(panel, columnCount);

		setControl(panel);
		FeedbackConfigurationWizard feedbackConfigurationWizard = (FeedbackConfigurationWizard) getWizard();
		feedbackConfigurationWizard.addCancelListener(this);
	}

	private void createRegistrationComposite(Composite panel) {

		GridData gd;
		registerInstallationButton = new Button(panel, SWT.CHECK);
		registerInstallationButton
				.setText("Send list of installed DevBoost tools");
		registerInstallationButton
				.setToolTipText("No further information is required.");
		registerInstallationButton.setSelection(true);
		gd = new GridData();
		gd.horizontalSpan = 2;
		registerInstallationButton.setLayoutData(gd);

		sendErrorsButton = new Button(panel, SWT.CHECK);
		sendErrorsButton.setText("Send reports about fatal errors to DevBoost");
		sendErrorsButton
				.setToolTipText("Error reports include stack trace information only. No personal data is sent to DevBoost.");
		sendErrorsButton.setSelection(true);
		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.verticalIndent = 6;
		sendErrorsButton.setLayoutData(gd);

		createEmailPanel(panel);
	}

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
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		panel.setLayout(layout);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		panel.setLayoutData(gd);
		sendEmailButton = new Button(panel, SWT.CHECK);
		sendEmailButton.setText("Send your email address:");
		sendEmailButton
				.setToolTipText("Your email address will be used to send you news on our tools.");
		sendEmailButton.setSelection(true);
		gd = new GridData();
		gd.horizontalIndent = 0;
		gd.horizontalSpan = 1;
		sendEmailButton.setLayoutData(gd);
		sendEmailButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				toggleEmailText();
			}

			private void toggleEmailText() {
				emailText.setEditable(sendEmailButton.getSelection());
				emailText.setEnabled(sendEmailButton.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				toggleEmailText();
			}
		});

		emailText = new Text(panel, SWT.BORDER);
		emailText.setText(UIConstants.DEFAULT_EMAIL_ADDRESS);
		emailText.setFocus();
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		emailText.setLayoutData(gd);
		return parent;
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

	public boolean isSendEmailSelected() {
		return sendEmailButton.getSelection();
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
