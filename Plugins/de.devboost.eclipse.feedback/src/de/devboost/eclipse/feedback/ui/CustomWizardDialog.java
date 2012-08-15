package de.devboost.eclipse.feedback.ui;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

public class CustomWizardDialog extends WizardDialog {

	public CustomWizardDialog(Shell shell, FeedbackConfigurationWizard newWizard) {
		super(shell, newWizard);
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
