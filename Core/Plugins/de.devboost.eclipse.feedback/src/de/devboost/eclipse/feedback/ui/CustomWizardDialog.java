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

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

public class CustomWizardDialog extends WizardDialog {

	public CustomWizardDialog(Shell shell, AbstractConfigurationWizard newWizard) {
		super(shell, newWizard);
		setHelpAvailable(false);
		newWizard.setWizardDialog(this);
	}

	public void addCancelListener(final ICancelListener listener) {
		Button button = this.getButton(CANCEL);
		button.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent e) {
				// do nothing
			}

			@Override
			public void mouseExit(MouseEvent e) {
				listener.cancelExit();
			}

			@Override
			public void mouseEnter(MouseEvent e) {
				listener.cancelEnter();
			}
		});
	}

}
