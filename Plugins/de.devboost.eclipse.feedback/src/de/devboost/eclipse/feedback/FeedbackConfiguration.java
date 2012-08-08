package de.devboost.eclipse.feedback;

import java.util.Date;

public class FeedbackConfiguration {

	private String email;
	private boolean registerInstallation;
	private boolean sendErrorReports;
	private Date date;
	
	public FeedbackConfiguration(String email, boolean registerInstallation,
			boolean sendErrorReports, Date date) {
		super();
		this.email = email;
		this.registerInstallation = registerInstallation;
		this.sendErrorReports = sendErrorReports;
		this.date = date;
	}

	public String getEmail() {
		return email;
	}

	public boolean isRegisterInstallation() {
		return registerInstallation;
	}

	public boolean isSendErrorReports() {
		return sendErrorReports;
	}

	public Date getDate() {
		return date;
	}
}
