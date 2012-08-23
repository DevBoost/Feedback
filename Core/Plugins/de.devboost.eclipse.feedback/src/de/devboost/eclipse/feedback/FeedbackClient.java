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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

class FeedbackClient {

	private static final String FEEDBACK_URL = "http://www.devboost.de/eclipse-feedback/";
	public static final String KEY_FEEDBACK_TYPE = "feedbacktype";

	public void sendPropertiesToServer(Properties properties) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			properties.storeToXML(baos, "");
			sendXmlOverHttp(baos.toString());
		} catch (IOException e) {
			FeedbackPlugin.logError("Could not send DevBoost feedback configuration", e);
		}
	}

	protected void sendXmlOverHttp(String xmlString) throws IOException {
		// set parameters
        String data = "data=" + URLEncoder.encode(xmlString, "UTF-8");

		URLConnection connection = new URL(FEEDBACK_URL).openConnection();
		if (connection instanceof HttpURLConnection) {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setInstanceFollowRedirects(false); 
			httpConnection.setRequestMethod("POST"); 
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			httpConnection.setRequestProperty("charset", "utf-8");
			httpConnection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));
			httpConnection.setUseCaches(false);
			httpConnection.setConnectTimeout(2000);
            // connect
            httpConnection.connect();
			// send post request
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            // write parameters
            writer.write(data);
            writer.flush();
            
            httpConnection.getResponseCode();
			// close connection
    		httpConnection.disconnect();
		}
	}
}
