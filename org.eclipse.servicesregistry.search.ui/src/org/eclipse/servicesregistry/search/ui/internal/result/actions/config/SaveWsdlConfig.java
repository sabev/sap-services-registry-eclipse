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
package org.eclipse.servicesregistry.search.ui.internal.result.actions.config;

import java.io.File;

import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.IActionConfiguration;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.InteractiveExistingFilesProcessor;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;

public abstract class SaveWsdlConfig extends AbstractActionConfig implements IActionConfiguration
{
	private File saveDestination;

	public SaveWsdlConfig(final IServiceDefinition definition, final IDiscoveryEnvironment env)
	{
		super(env);
	}

	@Override
	public IExistingFilesProcessor existingFilesProcessor()
	{
		return new InteractiveExistingFilesProcessor();
	}

	@Override
	public void wsdlDownloaded(IWsdlWtpDescriptorContainer wsdlContainer, ILongOperationRunner operationRunner)
	{
		scheduleWsdlDirectoryRefresh(wsdlContainer);
		openWsdlInEditor(wsdlContainer);
	}
	
	@Override
	public File saveDestination()
	{
		return this.saveDestination;
	}
	
	public void setFileDestination(final File destination)
	{
		this.saveDestination = destination;
	}

}
