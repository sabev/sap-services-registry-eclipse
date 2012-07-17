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
import org.eclipse.servicesregistry.ui.internal.config.EditSrConfigDialog;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

public class EditSrConfigDialogPageObject extends BaseSrConfigDialogPageObject<EditSrConfigDialog, IServicesRegistrySystem> {

	public EditSrConfigDialogPageObject(ServicesRegistrySystemValidator servicesRegistrySystemValidator, Set<IServicesRegistrySystem> existingConfigs, IServicesRegistrySystem systemToEdit) {
		super(servicesRegistrySystemValidator, existingConfigs, systemToEdit);
	}

	@Override
	protected EditSrConfigDialog createDialog(ServicesRegistrySystemValidator servicesRegistrySystemValidator, Set<IServicesRegistrySystem> existingConfigs, IWorkbenchHelpSystem helpSystem,
			IServicesRegistrySystem systemToEdit) {
		return new EditSrConfigDialog(null, systemToEdit, servicesRegistrySystemValidator, existingConfigs);
	}
	
}