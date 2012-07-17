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
package org.eclipse.servicesregistry.ui.internal.config;

import java.util.Set;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.ICreatedObjectValidator;
import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog for editing a SR configuration
 * @author Danail Branekov
 */
public class EditSrConfigDialog extends AbstractSrConfigDialog
{
	private final IServicesRegistrySystem configToEdit;

	/**
	 * Dialog for editing SR configurations
	 * @param parent 
	 * @param configToEdit the SR configuration to edit
	 * @param configValidator validator to validate the edited configuration
	 * @param currentlyExistingConfigs a set of currently existing configurations. This set must not contain the configuration which is currently edited  
	 */
	public EditSrConfigDialog(final Shell parent, final IServicesRegistrySystem configToEdit, final ICreatedObjectValidator<IServicesRegistrySystem> configValidator, final Set<IServicesRegistrySystem> currentlyExistingConfigs)
	{
		super(parent, configValidator, currentlyExistingConfigs);
		this.configToEdit = configToEdit;
	}

	@Override
	protected void initializeProperties()
	{
		connectionName.set(configToEdit.displayName());
		serverHost.set(configToEdit.host());
		serverPort.set(Integer.toString(configToEdit.port()));
		useHttps.set(configToEdit.useHttps());
		storeCredentials.set(configToEdit.areCredentialsStored());
		if(configToEdit.areCredentialsStored())
		{
			userName.set(configToEdit.userName());
			password.set(configToEdit.password());
		}
	}

	@Override
	protected void configureShell(Shell shell)
	{
		super.configureShell(shell);
		shell.setText(SrUiMessages.EDIT_CFG_DLG_TITLE);
	}
}
