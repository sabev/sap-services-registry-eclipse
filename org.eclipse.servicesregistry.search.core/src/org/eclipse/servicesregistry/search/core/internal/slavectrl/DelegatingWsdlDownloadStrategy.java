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
package org.eclipse.servicesregistry.search.core.internal.slavectrl;

import java.io.File;

import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IWsdlArtifactFileNameCalculator;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlWtpStrategy;

public class DelegatingWsdlDownloadStrategy extends WsdlWtpStrategy
{
	protected final WsdlWtpStrategy delegateStrategy;

	public IWsdlArtifactFileNameCalculator createWsdlFileNameCalculator()
	{
		return delegateStrategy.createWsdlFileNameCalculator();
	}

	public String getSchemaSubFolder()
	{
		return delegateStrategy.getSchemaSubFolder();
	}

	public boolean isSaveAllowed()
	{
		return delegateStrategy.isSaveAllowed();
	}

	public void postLoad(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer) throws WsdlStrategyException
	{
		delegateStrategy.postLoad(wsdlDescriptorsContainer);
	}

	public void postSave(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer) throws WsdlStrategyException
	{
		delegateStrategy.postSave(wsdlDescriptorsContainer);
	}

	public void preLoad() throws WsdlStrategyException
	{
		delegateStrategy.preLoad();
	}

	public void preSave(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer, File targetFolder) throws WsdlStrategyException
	{
		delegateStrategy.preSave(wsdlDescriptorsContainer, targetFolder);
	}

	public void setOverwriteFiles(boolean overwriteFiles)
	{
		delegateStrategy.setOverwriteFiles(overwriteFiles);
	}

	public DelegatingWsdlDownloadStrategy(final WsdlWtpStrategy delegateStrategy)
	{
		super();
		this.delegateStrategy = delegateStrategy;
	}
}
