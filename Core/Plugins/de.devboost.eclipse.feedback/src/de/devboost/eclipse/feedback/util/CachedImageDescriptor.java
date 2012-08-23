package de.devboost.eclipse.feedback.util;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;

public class CachedImageDescriptor extends ImageDescriptor {

	private ImageDescriptor fDescriptor;
	private ImageData fData;

	public CachedImageDescriptor(ImageDescriptor descriptor) {
		fDescriptor = descriptor;
	}

	@Override
	public ImageData getImageData() {
		if (fData == null) {
			fData= fDescriptor.getImageData();
		}
		return fData;
	}


}
