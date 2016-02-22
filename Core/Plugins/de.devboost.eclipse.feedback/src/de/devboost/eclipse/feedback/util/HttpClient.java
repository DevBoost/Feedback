/*******************************************************************************
 * Copyright (c) 2012-2016
 * DevBoost GmbH, Dresden, Amtsgericht Dresden, HRB 34001
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   DevBoost GmbH - Dresden, Germany
 *      - initial API and implementation
 ******************************************************************************/
package de.devboost.eclipse.feedback.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient {

	public String sendOverHttp(String url, HttpMethod method, String data) throws IOException, MalformedURLException,
			ProtocolException {
		URLConnection connection = new URL(url).openConnection();
		if (connection instanceof HttpURLConnection) {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setInstanceFollowRedirects(false);
			httpConnection.setRequestMethod(method.name());
			httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConnection.setRequestProperty("charset", "utf-8");
			httpConnection.setRequestProperty("Content-Length", "" + data.getBytes().length);
			httpConnection.setUseCaches(false);
			httpConnection.setConnectTimeout(2000);
			// connect
			httpConnection.connect();
			// send post request
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			// write parameters
			writer.write(data);
			writer.flush();

			StringBuilder response = new StringBuilder();
			InputStream inputStream = httpConnection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
			int next = -1;
			while ((next = reader.read()) >= 0) {
				response.append((char) next);
			}

			httpConnection.getResponseCode();
			// close connection
			httpConnection.disconnect();

			return response.toString();
		}
		return null;
	}
}
