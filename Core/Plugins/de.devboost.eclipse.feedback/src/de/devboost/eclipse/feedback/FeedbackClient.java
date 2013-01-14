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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import de.devboost.eclipse.feedback.util.HttpClient;
import de.devboost.eclipse.feedback.util.HttpMethod;

class FeedbackClient {

	private static final String FEEDBACK_URL = "http://www.devboost.de/eclipse-feedback/";
	public static final String KEY_FEEDBACK_TYPE = "feedbacktype";

	public void sendPropertiesToServer(Properties properties) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			properties.storeToXML(baos, "");
			sendXmlOverHttp(baos.toString());
		} catch (IOException e) {
			FeedbackPlugin.logError("Could not send DevBoost feedback", e);
		}
	}

	protected void sendXmlOverHttp(String xmlString) throws IOException {
		// set parameters
        String data = "data=" + URLEncoder.encode(xmlString, "UTF-8");
		String url = FEEDBACK_URL;
		HttpMethod method = HttpMethod.POST;
		
		// send request
		new HttpClient().sendOverHttp(url, method, data);
	}
}
