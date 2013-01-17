/*******************************************************************************
 * Copyright (c) 2012-2013
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

import org.eclipse.core.runtime.Plugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

import de.devboost.eclipse.feedback.ui.IConfigurationWizardOpener;

public abstract class AbstractStartupListener implements IStartup {
	
	private IConfigurationHandler configurationHandler;

	public AbstractStartupListener() {
		super();
		configurationHandler = createConfigurationHandler();
	}

	protected IConfigurationHandler getConfigurationHandler() {
		return configurationHandler;
	}

	protected void doEarlyStartup(Plugin plugin, String[] pluginPrefixes) {
		if (plugin == null) {
			return;
		}
		
		registerLogListener(plugin, pluginPrefixes);
		
		configureFeedbackIfRequired();
	}

	/**
	 * Checks whether the current user has configured his feedback preferences
	 * yet. If not, a respective dialog is shown.
	 */
	public void configureFeedbackIfRequired() {
		synchronized (StartupListener.class) {
			// check whether feedback was configured before
			boolean showDialog = isShowingDialogRequired();
			if (!showDialog) {
				return;
			}
			// otherwise show configuration wizard
			final Display display = Display.getDefault();
			display.asyncExec(new Runnable() {
				
				@Override
				public void run() {
					IConfigurationWizardOpener wizardOpener = getWizardOpener();
					wizardOpener.showConfigurationWizardDialog(display.getActiveShell());
				}
			});
		}
	}
	
	protected abstract boolean isShowingDialogRequired();

	protected abstract IConfigurationHandler createConfigurationHandler();

	/**
	 * This is a template method that allows sub classes to open custom 
	 * configuration wizard.
	 */
	protected abstract IConfigurationWizardOpener getWizardOpener();

	private void registerLogListener(Plugin plugin, String[] pluginPrefixes) {
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
			logReaderService.addLogListener(new FeedbackLogListener(pluginPrefixes, configurationHandler));
		}
	}
}
