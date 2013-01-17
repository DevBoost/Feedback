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
package de.devboost.eclipse.feedback.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

public class ImageHelper {

	public ImageDescriptor getImage(String urlText) throws MalformedURLException {
		URL url = new URL(urlText);

		ImageDescriptor rawDescriptor = ImageDescriptor.createFromURL(url);
		ImageDescriptor cachedDescriptor = new CachedImageDescriptor(rawDescriptor);

		if (cachedDescriptor.getImageData() != null) {
			return cachedDescriptor;
		}
		return null;
	}
}
