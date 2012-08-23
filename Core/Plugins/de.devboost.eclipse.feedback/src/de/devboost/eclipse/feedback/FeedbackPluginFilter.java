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
package de.devboost.eclipse.feedback;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

class FeedbackPluginFilter {
	
	private final static String[] DEVBOOST_PLUGIN_PREFIXES = new String[] {
			"de.devboost.",
			"org.emftext.",
			"org.dropsbox.",
			"org.reuseware.",
			"org.jamopp.",
			"org.junitloop.",
			"org.hibernate-dsl.",
			"org.buildboost.",
		};

	public List<Bundle> getBundlesSendingFeedback(FeedbackPlugin feedbackPlugin) {
		List<Bundle> bundlesSendingFeedback = new ArrayList<Bundle>();
		if (feedbackPlugin != null) {
			BundleContext context = feedbackPlugin.getBundle().getBundleContext();
			Bundle[] bundles = context.getBundles();
			for (int i = 0; i < bundles.length; i++) {
				Bundle bundle = bundles[i];
				String symbolicName = bundle.getSymbolicName();
				if (symbolicName == null) {
					continue;
				}
				if (isFeedbackPlugin(symbolicName)) {
					bundlesSendingFeedback.add(bundle);
				}
			}
		}
		return bundlesSendingFeedback;
	}

	public boolean isFeedbackPlugin(String symbolicName) {
		if (symbolicName == null) {
			return false;
		}
		for (String prefix : DEVBOOST_PLUGIN_PREFIXES) {
			if (symbolicName.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsFeedbackPluginPrefix(String text) {
		if (text == null) {
			return false;
		}
		for (String prefix : DEVBOOST_PLUGIN_PREFIXES) {
			if (text.contains(prefix)) {
				return true;
			}
		}
		return false;
	}
}
