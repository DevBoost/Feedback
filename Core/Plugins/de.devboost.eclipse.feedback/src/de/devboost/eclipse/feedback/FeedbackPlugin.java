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
package de.devboost.eclipse.feedback;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
	 * @param type
	 *            the type of the message to log
	 * @param message
	 *            the message to log
	 * @param throwable
	 *            the exception that describes the error in detail (can be null)
	 * 
	 * @return the status object describing the error
	 */
	protected static IStatus log(int type, String message, Throwable throwable) {
		IStatus status;
		if (throwable != null) {
			status = new Status(type, FeedbackPlugin.PLUGIN_ID, 0, message, throwable);
		} else {
			status = new Status(type, FeedbackPlugin.PLUGIN_ID, message);
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
