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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.platform.discovery.util.internal.StatusUtils;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.ICreatedObjectValidator;
import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.swt.widgets.Shell;


/**
 * Dialog for creating a new SR configuration
 * @author Danail Branekov
 *
 */
public class NewSrConfigDialog extends AbstractSrConfigDialog
{
	/**
	 * New SR config dialog
	 * @param parent 
	 * @param configValidator validator which will validate the created config
	 * @param existingConfigs a set of currently existing configs
	 */
	public NewSrConfigDialog(final Shell parent, final ICreatedObjectValidator<IServicesRegistrySystem> configValidator, final Set<IServicesRegistrySystem> existingConfigs)
	{
		super(parent, configValidator, existingConfigs);
	}
	
	@Override
	protected void configureShell(Shell shell)
	{
		super.configureShell(shell);
		shell.setText(SrUiMessages.NEW_CFG_DLG_TITLE);
	}
	
	@Override
	public void create()
	{
		super.create();
		updateStatus(StatusUtils.statusInfo(SrUiMessages.NEW_CFG_DLG_INFO));
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected void initializeProperties() {
	}

}
