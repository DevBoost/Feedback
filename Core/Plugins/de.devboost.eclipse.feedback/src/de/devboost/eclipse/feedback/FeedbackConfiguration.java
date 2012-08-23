/*******************************************************************************
 * Copyright (c) 2012
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
package de.devboost.eclipse.feedback;

import java.util.Date;

public class FeedbackConfiguration {

	private String guid;
	private String email;
	private boolean registerInstallation;
	private boolean sendErrorReports;
	private Date date;
	
	public FeedbackConfiguration(String guid, String email, boolean registerInstallation,
			boolean sendErrorReports, Date date) {
		super();
		this.guid = guid;
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

	public String getGuid() {
		return guid;
	}
}
