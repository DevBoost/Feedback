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
package de.devboost.eclipse.feedback;

public class FeedbackConfigurationData extends AbstractConfigurationData {
	
	private String email;

	// null indicates that this property was not set before
	private Boolean register;
	private Boolean sendErrors;
	private Boolean sendEmail;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Boolean isRegister() {
		return register;
	}
	
	public void setRegister(Boolean register) {
		this.register = register;
	}
	
	public Boolean isSendErrors() {
		return sendErrors;
	}

	public void setSendErrors(Boolean sendErrors) {
		this.sendErrors = sendErrors;
	}
	
	public Boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
}