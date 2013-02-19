/*******************************************************************************
 * Copyright (c) 2006-2012
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Berlin, Germany
 *      - initial API and implementation
 ******************************************************************************/
package de.devboost.buildboost.genext.feedback.steps;

import java.util.Collection;
import java.util.Collections;

import de.devboost.buildboost.BuildException;
import de.devboost.buildboost.ant.AbstractAntTargetGenerator;
import de.devboost.buildboost.ant.AntTarget;
import de.devboost.buildboost.artifacts.EclipseFeature;
import de.devboost.buildboost.util.XMLContent;

/**
 * The {@link AddFeedbackFeatureDependencyStep} adds a dependency to the
 * DevBoost Feedback feature to all DevBoost and Dropsbox features.
 */
public class AddFeedbackFeatureDependencyStep extends AbstractAntTargetGenerator {
	
	private EclipseFeature feature;

	public AddFeedbackFeatureDependencyStep(EclipseFeature feature) {
		super();
		this.feature = feature;
	}
	
	@Override
	public Collection<AntTarget> generateAntTargets() throws BuildException {
		XMLContent content = new XMLContent();
		
		String feedbackFeatureID = "de.devboost.eclipse.feedback";
		String featureID = feature.getIdentifier();
		if (!feedbackFeatureID.equals(featureID) &&
			isFeatureRequiringFeedback(featureID)) {
			content.append("<replace file='" + feature.getFile().getAbsolutePath() + "' token='&lt;requires&gt;' value='&lt;requires&gt;&lt;import feature=\"" + feedbackFeatureID + "\"/&gt;'/>");			
		}

		AntTarget target = new AntTarget("add-feedback-dependency-to-" + featureID, content);
		return Collections.singletonList(target);
	}

	private boolean isFeatureRequiringFeedback(String featureID) {
		if (featureID == null) {
			return false;
		}
		if (featureID.startsWith("de.devboost.")) {
			return true;
		}
		if (featureID.startsWith("org.dropsbox.")) {
			return true;
		}
		return false;
	}
}
