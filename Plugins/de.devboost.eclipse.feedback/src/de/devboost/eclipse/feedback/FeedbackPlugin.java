package de.devboost.eclipse.feedback;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class FeedbackPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.devboost.eclipse.feedback"; //$NON-NLS-1$

	// The shared instance
	private static FeedbackPlugin plugin;

	/**
	 * The constructor
	 */
	public FeedbackPlugin() {
		super();
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static FeedbackPlugin getDefault() {
		return plugin;
	}

	public FeedbackConfigurationHandler getConfigurationHandler() {
		return new FeedbackConfigurationHandler();
	}
}
