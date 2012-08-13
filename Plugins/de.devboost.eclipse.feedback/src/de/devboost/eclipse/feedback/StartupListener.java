package de.devboost.eclipse.feedback;

import java.util.List;

import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;

public class StartupListener implements IStartup {

	@Override
	public void earlyStartup() {
		FeedbackPlugin plugin = FeedbackPlugin.getDefault();
		if (plugin == null) {
			return;
		}
		
		List<Bundle> bundlesSendingFeedback = new FeedbackPluginFilter().getBundlesSendingFeedback(plugin);
		if (bundlesSendingFeedback.isEmpty()) {
			return;
		}
		
		plugin.configureFeedbackIfRequired();
	}
}
