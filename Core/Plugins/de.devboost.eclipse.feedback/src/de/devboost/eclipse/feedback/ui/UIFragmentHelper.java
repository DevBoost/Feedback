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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

public class UIFragmentHelper {

	public void createDevBoostHyperlink(Composite panel, int horizontalSpan) {
		String url = "http://www.devboost.de";
		createLink(panel, url, url, horizontalSpan);
	}

	public void createLink(Composite panel, String text, String url, int horizontalSpan) {
		Link link = new Link(panel, SWT.NONE);
		link.setText("<a href=\"" + url + "\">" + text + "</a>");

		GridData gd = new GridData(SWT.RIGHT, SWT.TOP, true, false);
		gd.horizontalSpan = horizontalSpan;
		gd.heightHint = 20;
		gd.verticalAlignment = SWT.TOP;
		link.setLayoutData(gd);

		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					// Open default external browser with URL
					Program.launch(e.text);
				} catch (Exception ex) {
					// do nothing
				}
			}
		});
	}
}
