package de.devboost.eclipse.feedback;

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

	// TODO use enumeration for method parameter
	public String sendOverHttp(String url, String method, String data)
			throws IOException, MalformedURLException, ProtocolException {
		URLConnection connection = new URL(url).openConnection();
		if (connection instanceof HttpURLConnection) {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.setInstanceFollowRedirects(false); 
			httpConnection.setRequestMethod(method); 
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
            
            StringBuilder response = new StringBuilder();
            InputStream inputStream = httpConnection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
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
