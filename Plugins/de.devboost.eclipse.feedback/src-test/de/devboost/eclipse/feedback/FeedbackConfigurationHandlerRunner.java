package de.devboost.eclipse.feedback;

import java.io.IOException;

import de.devboost.eclipse.feedback.FeedbackConfigurationHandler;

public class FeedbackConfigurationHandlerRunner {

	public static void main(String[] args) throws IOException {
		new FeedbackConfigurationHandler().sendXmlOverHttp("<test/>");
	}
}
