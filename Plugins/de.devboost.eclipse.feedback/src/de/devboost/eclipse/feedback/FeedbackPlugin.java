package de.devboost.eclipse.feedback;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.devboost.eclipse.feedback.ui.FeedbackConfigurationWizardHelper;

// TODO check if global log listener is possible
// TODO write GUID to local properties file
// TODO add company logo to wizard page
// TODO add link to website
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

	/**
	 * Checks whether the current user has configured his feedback preferences
	 * yet. If not, a respective dialog is shown.
	 */
	public void configureFeedbackIfRequired() {
		synchronized (FeedbackPlugin.class) {
			// check whether feedback was configured before
			FeedbackConfiguration configuration = getConfigurationHandler().loadConfiguration();
			if (configuration != null) {
				return;
			}
			// otherwise show configuration wizard
			final Display display = Display.getDefault();
			display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					FeedbackConfigurationWizardHelper helper = new FeedbackConfigurationWizardHelper();
					helper.showFeedbackConfigurationWizardDialog(display.getActiveShell());
				}
			});
		}
	}

	// TODO move the log methods to some shared plug-in
	public static void logInfo(String message, Throwable cause) {
		log(IStatus.INFO, message, cause);
	}

	public static void logError(String message, Throwable cause) {
		log(IStatus.ERROR, message, cause);
	}
	
	/**
	 * Helper method for logging.
	 * 
	 * @param type the type of the message to log
	 * @param message the message to log
	 * @param throwable the exception that describes the error in detail (can be null)
	 * 
	 * @return the status object describing the error
	 */
	protected static org.eclipse.core.runtime.IStatus log(int type, String message, Throwable throwable) {
		org.eclipse.core.runtime.IStatus status;
		if (throwable != null) {
			status = new org.eclipse.core.runtime.Status(type, FeedbackPlugin.PLUGIN_ID, 0, message, throwable);
		} else {
			status = new org.eclipse.core.runtime.Status(type, FeedbackPlugin.PLUGIN_ID, message);
		}
		final FeedbackPlugin pluginInstance = FeedbackPlugin.getDefault();
		if (pluginInstance == null) {
			System.err.println(message);
			if (throwable != null) {
				throwable.printStackTrace();
			}
		} else {
			pluginInstance.getLog().log(status);
		}
		return status;
	}
}
