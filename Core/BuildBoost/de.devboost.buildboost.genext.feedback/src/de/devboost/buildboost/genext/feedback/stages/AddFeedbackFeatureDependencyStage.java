/*******************************************************************************
 * Copyright (c) 2006-2013
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
package de.devboost.buildboost.genext.feedback.stages;

import java.io.File;

import de.devboost.buildboost.AutoBuilder;
import de.devboost.buildboost.BuildContext;
import de.devboost.buildboost.BuildException;
import de.devboost.buildboost.IConstants;
import de.devboost.buildboost.ant.AntScript;
import de.devboost.buildboost.discovery.EclipseFeatureFinder;
import de.devboost.buildboost.discovery.EclipseTargetPlatformAnalyzer;
import de.devboost.buildboost.discovery.PluginFinder;
import de.devboost.buildboost.genext.feedback.steps.AddFeedbackFeatureDependencyStepProvider;
import de.devboost.buildboost.model.IUniversalBuildStage;
import de.devboost.buildboost.stages.AbstractBuildStage;

public class AddFeedbackFeatureDependencyStage extends AbstractBuildStage implements IUniversalBuildStage {

	private String artifactsFolder;

	public void setArtifactsFolder(String artifactsFolder) {
		this.artifactsFolder = artifactsFolder;
	}

	@Override
	public AntScript getScript() throws BuildException {
		File artifactsDir = new File(artifactsFolder);

		BuildContext context = createContext(false);
		context.setIgnoreUnresolvedDependencies(true);
		
		File projectsFolder = new File(artifactsFolder, IConstants.PROJECTS_FOLDER);
		File targetPlatformFolder = new File(artifactsFolder, IConstants.TARGET_PLATFORM_FOLDER);

		context.addBuildParticipant(new EclipseTargetPlatformAnalyzer(targetPlatformFolder));
		context.addBuildParticipant(new PluginFinder(projectsFolder));
		context.addBuildParticipant(new EclipseFeatureFinder(artifactsDir));
		context.addBuildParticipant(new AddFeedbackFeatureDependencyStepProvider());
		
		AutoBuilder builder = new AutoBuilder(context);
		
		AntScript script = new AntScript();
		script.setName("Add dependency to DevBoost feedback feature to DevBoost features");
		script.addTargets(builder.generateAntTargets());
		
		return script;
	}
	
	@Override
	public int getPriority() {
		return 10000;
	}
}
