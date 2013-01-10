/*******************************************************************************
 * Copyright (c) 2012
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
package de.devboost.eclipse.feedback.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.devboost.eclipse.feedback.ConfigurationHandler;
import de.devboost.eclipse.feedback.IConfigurationHandler;
import de.devboost.eclipse.feedback.IOpenSourcePlugins;

public class FeedbackConfigurationWizardRunner {

	private Display display;

	public void run() {
		display = new Display();
		Shell shell = new Shell(display);

		shell.setLocation(200, 100);
		shell.setSize(150, 200);
		shell.open();

		String[] pluginPrefixes = IOpenSourcePlugins.DEVBOOST_OPEN_SOURCE_PLUGIN_PREFIXES;
		IConfigurationHandler configurationHandler = new ConfigurationHandler(pluginPrefixes);
		AbstractConfigurationWizard wizard = new FeedbackConfigurationWizard(configurationHandler);
		IConfigurationWizardOpener helper = new ConfigurationWizardOpener(wizard);
		helper.showConfigurationWizardDialog(shell);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void stop() {
		if (display != null) {
			display.asyncExec(new Runnable() {
				public void run() {
					display.dispose();
				}
			});
		}
	}
}
