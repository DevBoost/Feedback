package de.devboost.eclipse.feedback;

import java.util.List;

import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

public class StartupListener implements IStartup {

	@Override
	public void earlyStartup() {
		FeedbackPlugin plugin = FeedbackPlugin.getDefault();
		if (plugin == null) {
			return;
		}
		
		registerLogListener(plugin);
		
		List<Bundle> bundlesSendingFeedback = new FeedbackPluginFilter().getBundlesSendingFeedback(plugin);
		if (bundlesSendingFeedback.isEmpty()) {
			return;
		}
		
		plugin.configureFeedbackIfRequired();
	}

	private void registerLogListener(FeedbackPlugin plugin) {
		Bundle bundle = plugin.getBundle();
		BundleContext bundleContext = bundle.getBundleContext();
		ServiceTracker<LogReaderService, Object> logReaderTracker = new ServiceTracker<LogReaderService, Object>(
				bundleContext, 
				LogReaderService.class.getName(), 
				null);
		logReaderTracker.open();
		Object service = logReaderTracker.getService();
		if (service != null && service instanceof LogReaderService) {
			LogReaderService logReaderService = (LogReaderService) service;
			logReaderService.addLogListener(new FeedbackLogListener());
		}
	}
}