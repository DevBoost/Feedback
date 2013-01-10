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
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
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
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import de.devboost.eclipse.feedback.FeedbackPlugin;
import de.devboost.eclipse.feedback.util.CachedImageDescriptor;

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
	private Button sendEmailButton;

	public FeedbackConfigurationPage() {
		// TODO call super with image descriptor
		super(PAGE_TITLE);
		setTitle(PAGE_TITLE_2);

		URL url;
		try {
			url = new URL(DEVBOOST_LOGO);

			ImageDescriptor imageDescriptor = new CachedImageDescriptor(
					ImageDescriptor.createFromURL(url));

			if (imageDescriptor.getImageData() != null) {
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
						+ "we do creating them. To help us to continuously improve\n"
						+ "our tools, we kindly ask you to register this installation\n"
						+ "and to enable error feedback reporting. If you want to keep\n"
						+ "up to date with our latest progress you can also enter\n"
						+ "your Email address below.\n\n"

						+ "Of course, all this is optional. If you decide to cancel this\n"
						+ "dialog, you won't be bothered again.\n\n" + "Enjoy.");
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.heightHint = 300;
		messageText.setLayoutData(gd);
		createImagePanel(panel);

		createRegistrationComposite(panel);
		createDevBoostHyperlink(panel);

		setControl(panel);
		FeedbackConfigurationWizard feedbackConfigurationWizard = (FeedbackConfigurationWizard) getWizard();
		feedbackConfigurationWizard.addCancelListener(this);
	}

	private void createDevBoostHyperlink(Composite panel) {
		Link link = new Link(panel, SWT.NONE);
		link.setText("<a href=\"http://www.devboost.de\">www.devboost.de</a>");

		GridData gd = new GridData(SWT.RIGHT, SWT.TOP, true, true);
		gd.horizontalSpan = 2;
		gd.heightHint = 20;
		link.setLayoutData(gd);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("You have selected: " + e.text);
				try {
					// Open default external browser
					PlatformUI.getWorkbench().getBrowserSupport()
							.getExternalBrowser().openURL(new URL(e.text));
				} catch (Exception ex) {
					// do nothing
				}
			}
		});

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

		// createImmediateFeedbackPane(panel);
	}

	// private void createImmediateFeedbackPane(Composite panel) {
	// Text feedback = new Text(panel, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
	// feedback.setText("Here is some room for immediate feedback. If you like.");
	// GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
	// gd.horizontalSpan = 2;
	// gd.heightHint = 50;
	// feedback.setLayoutData(gd);
	// }

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
		// layout.marginTop = -6;
		panel.setLayout(layout);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		panel.setLayoutData(gd);
		sendEmailButton = new Button(panel, SWT.CHECK);
		sendEmailButton.setText("Send your Email address:");
		sendEmailButton
				.setToolTipText("Your Email address will be used to send you news on our tools.");
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
		emailText.setText(DEFAULT_EMAIL_ADDRESS);
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
