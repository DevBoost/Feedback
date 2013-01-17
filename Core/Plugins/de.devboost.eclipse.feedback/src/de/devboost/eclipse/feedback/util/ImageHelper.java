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
