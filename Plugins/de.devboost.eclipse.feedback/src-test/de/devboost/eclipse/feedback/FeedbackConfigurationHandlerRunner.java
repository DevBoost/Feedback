package de.devboost.eclipse.feedback;

import java.io.IOException;

public class FeedbackConfigurationHandlerRunner {

	public static void main(String[] args) throws IOException {
		new FeedbackClient().sendXmlOverHttp("<test/>");
	}
}
