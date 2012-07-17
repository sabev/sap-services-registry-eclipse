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
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.platform.discovery.util.api.env.IErrorHandler;
import org.eclipse.servicesregistry.core.internal.logging.Logger;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.IActionConfiguration;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.servicesregistry.wsdl.internal.util.FileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.IFileUtils;
import org.eclipse.servicesregistry.wsdl.walker.CancelWalking;
import org.eclipse.servicesregistry.wsdl.walker.WsdlVisitor;
import org.eclipse.servicesregistry.wsdl.walker.WsdlWalker;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * Abstract implementation for the {@link IActionConfiguration} interface which provides the common implementation
 * 
 * @author Danail Branekov
 */
public abstract class AbstractActionConfig implements IActionConfiguration
{
	private final IDiscoveryEnvironment env;

	public AbstractActionConfig(final IDiscoveryEnvironment env)
	{
		this.env = env;
	}

	@Override
	public IErrorHandler errorHandler()
	{
		return env.errorHandler();
	}

	protected IFileUtils fileUtils()
	{
		return FileUtils.getInstance();
	}

	protected void openWsdlInEditor(final IWsdlWtpDescriptorContainer wsdlContainer)
	{
		final File wsdlRootFile = wsdlContainer.getRootWsdlDefinition().getFile();
		executeRunnableInUI(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					openInEditor(wsdlRootFile);
				} catch (PartInitException e)
				{
					errorHandler().handleException(e);
				}
			}
		});
	}

	protected void executeRunnableInUI(final Runnable runnable)
	{
		PlatformUI.getWorkbench().getDisplay().asyncExec(runnable);
	}

	protected void setWsdlFilesDeleteOnExit(final IWsdlWtpDescriptorContainer wsdlContainer)
	{
		final WsdlVisitor<RuntimeException> visitor = new WsdlVisitor<RuntimeException>()
		{
			@Override
			public void visit(final IWsdlArtifact<?> currentArtifact) throws RuntimeException, CancelWalking
			{
				currentArtifact.getFile().deleteOnExit();
			}
		};
		(new WsdlWalker<RuntimeException>(visitor)).walk(wsdlContainer.getRootWsdlDefinition());
	}

	protected void scheduleWsdlDirectoryRefresh(final IWsdlWtpDescriptorContainer wsdlContainer)
	{
		final Set<IContainer> directories = findWsdlResourcesDirectories(wsdlContainer);
		refreshAsynchronously(directories.toArray(new IContainer[directories.size()]));
	}

	private Set<IContainer> findWsdlResourcesDirectories(final IWsdlWtpDescriptorContainer wsdlContainer)
	{
		final Set<IContainer> result = new HashSet<IContainer>();
		final WsdlVisitor<RuntimeException> visitor = new WsdlVisitor<RuntimeException>()
		{
			@Override
			public void visit(IWsdlArtifact<?> currentArtifact) throws RuntimeException, CancelWalking
			{
				for (IFile f : workspaceRoot().findFilesForLocationURI(currentArtifact.getFile().toURI()))
				{
					result.add(f.getParent());
				}
			}
		};
		(new WsdlWalker<RuntimeException>(visitor)).walk(wsdlContainer.getRootWsdlDefinition());

		return result;
	}

	private IWorkspaceRoot workspaceRoot()
	{
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	protected void refreshAsynchronously(final IResource... resources)
	{
		final Job refreshJob = new Job(SearchUIMessages.RefreshResourcesJobName)
		{
			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				try
				{
					for(IResource r : resources)
					{
						r.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					}
				} catch (CoreException e)
				{
					Logger.instance().logError(e.getMessage(), e);
				}

				return Status.OK_STATUS;
			}
		};
		refreshJob.setRule(ResourcesPlugin.getWorkspace().getRoot());
		refreshJob.schedule();
	}
	
	private void openInEditor(File file) throws PartInitException
	{
		assert file.isFile();
		
		final IWorkbenchPage page = activeWorkbenchPage();
		final IFileStore fileStore = fileSystem().getStore(file.toURI());
		openEditorOnFileStore(page, fileStore);
	}
	
	private void openEditorOnFileStore(final IWorkbenchPage wbPage, final IFileStore fileStore) throws PartInitException
	{
		IDE.openEditorOnFileStore(wbPage, fileStore);
	}
	
	private IFileSystem fileSystem()
	{
		return EFS.getLocalFileSystem();
	}
	
	private IWorkbenchPage activeWorkbenchPage()
	{
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
}
