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
package org.eclipse.servicesregistry.search.ui.internal.result.actions;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.config.SaveWsdlConfig;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * This action copies the provided WSDL in specified by the user 
 * location in workspace 
 * 
 * @author Georgi Vachkov
 */
public class SaveWsdlInWorkspaceAction extends Action 
{
	private final IDiscoveryEnvironment env;
	private final IContributedAction saveAction;
	private final SaveWsdlConfig actionConfig;
	
	public SaveWsdlInWorkspaceAction(final SaveWsdlConfig actionConfig, final IContributedAction saveAction, final IDiscoveryEnvironment env)
	{
		this.env = env;
		this.actionConfig = actionConfig;
		this.saveAction = saveAction;
	}
	
	@Override
	public String getId() {
		return "org.eclipse.servicesregistry.search.ui.internal.result.actions.save.wsdl.in.workspace";//$NON-NLS-1$
	}
	
	@Override
	public String getText() {
		return SearchUIMessages.CopyWsdlInWorkspaceAction_MenuItemText;
	}

	@Override
	public void run() 
	{
		final IResource targetResource = selectTargetResource();
		if(targetResource == null)
		{
			// user cancelled resource selection
			return;
		}
		
		assert targetResource instanceof IContainer;
		actionConfig.setFileDestination(targetResource.getLocation().toFile());
		final Set<Object> configs = new HashSet<Object>();
		configs.add(actionConfig);
		saveAction.perform(env.operationRunner(), configs);
	}
	
	/**
	 * @return selected resource or <code>null</code> in case the user cancel selection
	 * or there are no projects in the workspace. 
	 */
	protected IResource selectTargetResource() 
	{
		final Shell shell = getDisplay().getActiveShell();
		final WorkspaceResourceBrowser resBrowser = new WorkspaceResourceBrowser(shell);
		if (resBrowser.open() != Window.OK) {
			return null;
		}

		return resBrowser.getSelection();
	}

	private Display getDisplay()
	{
		return PlatformUI.getWorkbench().getDisplay();
	}

	protected IExistingFilesProcessor getExistingFilesProcessor()
	{
		return new InteractiveExistingFilesProcessor();
	}
}