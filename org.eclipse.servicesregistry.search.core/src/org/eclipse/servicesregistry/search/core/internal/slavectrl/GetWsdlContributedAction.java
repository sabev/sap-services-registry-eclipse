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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.platform.discovery.util.api.longop.ILongOperation;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.platform.discovery.util.api.longop.LongOpCanceledException;
import org.eclipse.servicesregistry.core.internal.logging.ILogger;
import org.eclipse.servicesregistry.core.internal.logging.Logger;
import org.eclipse.servicesregistry.search.core.internal.plugin.text.SearchCoreMessages;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlWtpStrategyDefault;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlWtpImportTool;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlWtpImportToolFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IWsdlArtifactFileNameCalculator;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlWtpStrategy;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;

/**
 * {@link IContributedAction} implementation which downloads a WSDL document on the file system. The action is configured via the
 * {@link IActionConfiguration} instance supplied as only object in the
 * <code>selectedObjects<code> parameter of {@link #perform(ILongOperationRunner, Set)}
 * 
 * @author Danail Branekov
 */
public abstract class GetWsdlContributedAction<Config extends IActionConfiguration> implements IContributedAction
{
	@SuppressWarnings("unchecked")
	@Override
	public void perform(final ILongOperationRunner operationRunner, final Set<Object> selectedObjects)
	{
		final Config actionConfig = (Config) selectedObjects.iterator().next();
		try
		{
			final ILongOperation<Void> downloadOperation = new ILongOperation<Void>()
			{
				@Override
				public Void run(final IProgressMonitor monitor) throws LongOpCanceledException, WsdlStrategyException, IOException
				{
					monitor.beginTask(SearchCoreMessages.CopyWsdlInWorkspaceAction_Downloading, IProgressMonitor.UNKNOWN);
					try
					{
						final URL wsdlUrl = new URL(actionConfig.wsdlUrl());
						final IWsdlWtpImportTool importTool = WsdlWtpImportToolFactory.createWsdlWtpImportTool(wsdlUrl, getWsdlDownloadStrategy(actionConfig, operationRunner));
						importTool.downloadWsdls(actionConfig.saveDestination());
						return null;
					}
					finally
					{
						monitor.done();
					}
				}
			};

			operationRunner.run(downloadOperation);
		}
		catch (InvocationTargetException e)
		{
			// Runnable should only throw WsdlStrategyException and IOException
			if ((e.getCause() instanceof WsdlStrategyException) || (e.getCause() instanceof IOException))
			{
				handleWsdlDownloadException(e.getCause(), actionConfig);
			}
			else
			{
				throw new RuntimeException(e.getCause());
			}
		}
		catch (LongOpCanceledException e)
		{
			// Runnable does not support cancellation
			throw new RuntimeException(e);
		}
	}

	private void handleWsdlDownloadException(final Throwable e, final Config actionConfig)
	{
		assert e instanceof IOException || e instanceof WsdlStrategyException;
		logger().logError(e.getMessage(), e);
		actionConfig.errorHandler().showError(SearchCoreMessages.SaveWsdlAction_ErrorTitle, MessageFormat.format(SearchCoreMessages.SaveWsdlAction_WsdlProcessingFailedMessage, actionConfig.wsdlUrl()));
	}

	protected ILogger logger()
	{
		return Logger.instance();
	}

	protected WsdlWtpStrategy getWsdlDownloadStrategy(final Config actionConfig, final ILongOperationRunner opRunner)
	{
		return new GetWsdlActionStrategy(actionConfig, opRunner);
	}

	private class GetWsdlActionStrategy extends WsdlWtpStrategyDefault
	{
		private final Config actionConfig;
		private final ILongOperationRunner operationRunner;

		public GetWsdlActionStrategy(final Config actionConfig, final ILongOperationRunner operationRunner)
		{
			this.actionConfig = actionConfig;
			this.operationRunner = operationRunner;
			setExistingFilesProcessor(actionConfig.existingFilesProcessor());
		}

		@Override
		public void postSave(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer) throws WsdlStrategyException
		{
			super.postSave(wsdlDescriptorsContainer);
			actionConfig.wsdlDownloaded(wsdlDescriptorsContainer, operationRunner);
		}

		@Override
		public IWsdlArtifactFileNameCalculator createWsdlFileNameCalculator()
		{
			final IWsdlArtifactFileNameCalculator parentCalc = super.createWsdlFileNameCalculator();
			return new IWsdlArtifactFileNameCalculator()
			{

				@Override
				public String proposeReferencedSchemaFileName(String rootWsdlFileName, URL artifactUrl, XSDSchema schema)
				{
					return parentCalc.proposeReferencedSchemaFileName(rootWsdlFileName, artifactUrl, schema);
				}

				@Override
				public String proposeReferencedWsdlFileName(String rootWsdlFileName, URL artifactUrl, Definition wsdlDefinition)
				{
					return parentCalc.proposeReferencedWsdlFileName(rootWsdlFileName, artifactUrl, wsdlDefinition);
				}

				@Override
				public String proposeRootWsdlFileName(URL artifactUrl, Definition wsdlDefinition)
				{
					return actionConfig.rootWsdlFileName();
				}

			};
		}
	}
}
