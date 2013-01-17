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
package de.devboost.eclipse.feedback;

public abstract class AbstractConfigurationLogic<DataType extends AbstractConfigurationData> {

	private IConfigurationHandler configurationHandler;
	private DataType data;
	
	public AbstractConfigurationLogic(IConfigurationHandler configurationHandler,
		DataType data) {
		super();
		this.configurationHandler = configurationHandler;
		this.data = data;
	}
	
	public IConfigurationHandler getConfigurationHandler() {
		return configurationHandler;
	}
	
	public abstract void performFinish();
	public abstract void performCancel();
	public abstract boolean isShowingDialogRequired();

	public boolean isEmailModifiable() {
		return getData().getEmail() == null;
	}

	public DataType getData() {
		return data;
	}
}
