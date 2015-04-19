/*******************************************************************************
 * Copyright (c) 2012-2015
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

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogReaderService;
import org.osgi.util.tracker.ServiceTracker;

import de.devboost.eclipse.feedback.ui.IConfigurationWizardOpener;

public abstract class AbstractStartupListener implements IStartup {

	public static class MutexRule implements ISchedulingRule {

		public boolean isConflicting(ISchedulingRule rule) {
			return rule == this;
		}

		public boolean contains(ISchedulingRule rule) {
			return rule == this;
		}
	}

	private final static MutexRule DIALOG_MUTEX = new MutexRule();

	private IConfigurationHandler configurationHandler;

	public AbstractStartupListener() {
		super();
		configurationHandler = createConfigurationHandler();
	}

	protected IConfigurationHandler getConfigurationHandler() {
		return configurationHandler;
	}

	protected void doEarlyStartup(Plugin plugin, List<String> pluginPrefixes) {
		if (plugin == null) {
			return;
		}

		registerLogListener(plugin, pluginPrefixes);
		showConfigurationDialogIfRequired();
	}

	/**
	 * Checks whether the current user has configured his preferences yet. If not, a respective dialog is shown. The
	 * concrete dialog is obtained using template method {@link #getWizardOpener()}.
	 */
	public void showConfigurationDialogIfRequired() {
		// use a job to prevent Eclipse from opening multiple dialogs in
		// parallel (use workspace root as scheduling rule)
		final Display display = Display.getDefault();
		UIJob job = new UIJob(display, "Show dialog") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				boolean showDialog = getLogic().isShowingDialogRequired();
				if (!showDialog) {
					return Status.OK_STATUS;
				}

				// otherwise show configuration wizard
				IConfigurationWizardOpener wizardOpener = getWizardOpener();
				wizardOpener.showConfigurationWizardDialog(display.getActiveShell());
				return Status.OK_STATUS;
			}
		};

		job.setRule(DIALOG_MUTEX);
		job.schedule();
	}

	protected abstract AbstractConfigurationLogic<?> getLogic();

	protected abstract IConfigurationHandler createConfigurationHandler();

	/**
	 * This is a template method that allows sub classes to open custom configuration wizard.
	 */
	protected abstract IConfigurationWizardOpener getWizardOpener();

	private void registerLogListener(Plugin plugin, List<String> pluginPrefixes) {
		Bundle bundle = plugin.getBundle();
		BundleContext bundleContext = bundle.getBundleContext();
		ServiceTracker<LogReaderService, Object> logReaderTracker = new ServiceTracker<LogReaderService, Object>(
				bundleContext, LogReaderService.class.getName(), null);
		logReaderTracker.open();
		Object service = logReaderTracker.getService();
		if (service != null && service instanceof LogReaderService) {
			LogReaderService logReaderService = (LogReaderService) service;
			logReaderService.addLogListener(new FeedbackLogListener(pluginPrefixes, configurationHandler));
		}
	}
}
