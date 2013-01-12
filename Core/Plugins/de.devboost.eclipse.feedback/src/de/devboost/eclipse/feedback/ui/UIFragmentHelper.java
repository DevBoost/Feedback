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
package de.devboost.eclipse.feedback.ui;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

public class UIFragmentHelper {

	public void createDevBoostHyperlink(Composite panel) {
		Link link = new Link(panel, SWT.NONE);
		link.setText("<a href=\"http://www.devboost.de\">www.devboost.de</a>");

		GridData gd = new GridData(SWT.RIGHT, SWT.TOP, true, true);
		gd.horizontalSpan = 2;
		gd.heightHint = 20;
		link.setLayoutData(gd);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					// Open default external browser
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWorkbenchBrowserSupport browserSupport = workbench.getBrowserSupport();
					IWebBrowser externalBrowser = browserSupport.getExternalBrowser();
					URL url = new URL(e.text);
					externalBrowser.openURL(url);
				} catch (Exception ex) {
					// do nothing
				}
			}
		});

	}
}
