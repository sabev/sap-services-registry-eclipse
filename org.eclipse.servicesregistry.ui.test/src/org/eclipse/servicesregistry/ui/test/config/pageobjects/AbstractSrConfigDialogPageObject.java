/*******************************************************************************
 * Copyright (c) 2012 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.servicesregistry.ui.test.config.pageobjects;

import java.util.Set;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystemValidator;
import org.eclipse.servicesregistry.ui.internal.config.AbstractSrConfigDialog;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

public class AbstractSrConfigDialogPageObject extends BaseSrConfigDialogPageObject<AbstractSrConfigDialog, Void>{

	public AbstractSrConfigDialogPageObject(ServicesRegistrySystemValidator servicesRegistrySystemValidator, Set<IServicesRegistrySystem> existingConfigs) {
		super(servicesRegistrySystemValidator, existingConfigs, null);
	}

	@Override
	protected AbstractSrConfigDialog createDialog(ServicesRegistrySystemValidator servicesRegistrySystemValidator, Set<IServicesRegistrySystem> existingConfigs, final IWorkbenchHelpSystem wbHelpSystem, Void additionalData) {
		return new AbstractSrConfigDialog(null, servicesRegistrySystemValidator, existingConfigs) {
			@Override
			protected void initializeProperties() {
			}
			
			@Override
			protected IWorkbenchHelpSystem wbHelpSystem() {
				return wbHelpSystem;
			}
		};
	}

}