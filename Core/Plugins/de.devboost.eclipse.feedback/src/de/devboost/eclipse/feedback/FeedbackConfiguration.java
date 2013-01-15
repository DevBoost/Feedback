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

import java.util.Date;
import java.util.Properties;

public class FeedbackConfiguration {

	private Properties properties;
	
	public FeedbackConfiguration() {
		super();
		this.properties = new Properties();
	}
	
	public Boolean getBooleanProperty(String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			return null;
		}
		return Boolean.parseBoolean(value);
	};

	public String getStringProperty(String key) {
		String value = properties.getProperty(key);
		return value;
	}

	public Date getDateProperty(String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			return null;
		}
		Date date = new Date(Long.parseLong(value));
		return date;
	}

	public Properties getProperties() {
		return properties;
	}
}
