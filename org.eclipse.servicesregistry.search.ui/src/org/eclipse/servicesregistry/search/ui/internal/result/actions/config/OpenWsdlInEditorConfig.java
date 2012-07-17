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
import org.eclipse.servicesregistry.search.ui.internal.result.actions.StubProcessor;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;

public class OpenWsdlInEditorConfig extends AbstractActionConfig implements IActionConfiguration
{
	public interface IWsdlUrlProvider {
		String getWsdlUrl();
	}
	
	private final IWsdlUrlProvider wsdlUrlProvider;
	
	public OpenWsdlInEditorConfig(final IWsdlUrlProvider wsdlUrlProvider, IDiscoveryEnvironment env)
	{
		super(env);
		this.wsdlUrlProvider = wsdlUrlProvider;
	}
	
	@Override
	public IExistingFilesProcessor existingFilesProcessor()
	{
		// Download is performed in temporary directory => always return OK status
		return new StubProcessor();
	}

	@Override
	public File saveDestination()
	{
		// The temporary directory
		return fileUtils().systemTempDir();
	}

	@Override
	public void wsdlDownloaded(final IWsdlWtpDescriptorContainer wsdlContainer, final ILongOperationRunner operationRunner)
	{
		openWsdlInEditor(wsdlContainer);
		this.setWsdlFilesDeleteOnExit(wsdlContainer);
	}

	@Override
	public String wsdlUrl()
	{
		return wsdlUrlProvider.getWsdlUrl();
	}

	@Override
	public String rootWsdlFileName()
	{
		return "tmpwsdl_" + System.nanoTime() + ".wsdl";
	}
}
