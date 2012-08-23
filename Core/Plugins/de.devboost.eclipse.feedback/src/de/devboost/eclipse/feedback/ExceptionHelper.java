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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Set;

public class ExceptionHelper {

	public String getStackTrace(Throwable exception) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exception.printStackTrace(new PrintStream(baos));
		return baos.toString();
	}

	public String getStackTraceIncludingCauses(Throwable exception) {
		StringBuilder stackTrace = new StringBuilder();
		
		Set<Throwable> handledExceptions = new LinkedHashSet<Throwable>();
		Throwable next = exception;
		while (next != null && !handledExceptions.contains(next)) {
			stackTrace.append(getStackTrace(next));
			handledExceptions.add(next);
			next = next.getCause();
		}
		return stackTrace.toString();
	}
}
