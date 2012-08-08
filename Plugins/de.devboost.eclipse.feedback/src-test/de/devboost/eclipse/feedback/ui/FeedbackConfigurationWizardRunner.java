package de.devboost.eclipse.feedback.ui;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FeedbackConfigurationWizardRunner {

	private Display display;

	public void run() {
		display = new Display();
		Shell shell = new Shell(display);

		shell.setLocation(200, 100);
		shell.setSize(150, 200);
		shell.open();

		WizardDialog wizdialog = new WizardDialog(shell, new FeedbackConfigurationWizard());
		wizdialog.setBlockOnOpen(true);
		wizdialog.open();
		
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
