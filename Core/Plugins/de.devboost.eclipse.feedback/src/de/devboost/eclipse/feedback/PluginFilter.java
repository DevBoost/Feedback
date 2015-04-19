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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * A {@link PluginFilter} can be used to check whether a running Eclipse instance
 * contains plug-ins matching a given set of prefixes.
 */
public class PluginFilter {
	
	private List<String> pluginPrefixes;
	
	/**
	 * Creates a plug-in filter that accepts plug-ins with the given prefixes.
	 */
	public PluginFilter(List<String> pluginPrefixes) {
		super();
		this.pluginPrefixes = pluginPrefixes;
	}

	public List<String> getInstalledProductBundleNames(Plugin plugin) {
		List<Bundle> matchingBundles = getMatchingBundles(plugin);
		List<String> productIDs = new ArrayList<String>();
		for (Bundle bundle : matchingBundles) {
			String bundleName = bundle.getSymbolicName();
			productIDs.add(bundleName);
		}
		return productIDs;
	}

	public List<Bundle> getMatchingBundles(Plugin contextPlugin) {
		List<Bundle> matchingBundles = new ArrayList<Bundle>();
		if (contextPlugin != null) {
			BundleContext context = contextPlugin.getBundle().getBundleContext();
			Bundle[] bundles = context.getBundles();
			for (int i = 0; i < bundles.length; i++) {
				Bundle bundle = bundles[i];
				String symbolicName = bundle.getSymbolicName();
				if (symbolicName == null) {
					continue;
				}
				if (isMatchingPlugin(symbolicName)) {
					matchingBundles.add(bundle);
				}
			}
		}
		return matchingBundles;
	}

	public boolean isMatchingPlugin(String symbolicName) {
		if (symbolicName == null) {
			return false;
		}
		for (String prefix : pluginPrefixes) {
			if (symbolicName.equals(prefix)) {
				return true;
			}
			if (symbolicName.startsWith(prefix + ".")) {
				return true;
			}
		}
		return false;
	}

	public boolean containsPluginPrefix(String text) {
		if (text == null) {
			return false;
		}
		for (String prefix : pluginPrefixes) {
			if (text.contains(prefix + ".")) {
				return true;
			}
		}
		return false;
	}
}
