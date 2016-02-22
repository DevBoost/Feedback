/*******************************************************************************
 * Copyright (c) 2012-2016
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class ExceptionHelper {

	private static final String UTF_8 = "UTF-8";

	public String getStackTrace(Throwable exception) {
		if (exception == null) {
			return "";
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			exception.printStackTrace(new PrintStream(baos, true, UTF_8));
			return baos.toString(UTF_8);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
