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
package org.eclipse.servicesregistry.search.ui.test.result.actions.pageobjects;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

public class ResourceBrowserPO
{
	public static ResourceBrowserPO find(final String shellText)
	{
		return new ResourceBrowserPO(new SWTBot().shell(shellText));
	}
	
	private final SWTBotTree resourcesTree;
	private final SWTBotShell shell;

	public ResourceBrowserPO(final SWTBotShell shell)
	{
		this.shell = shell;
		this.resourcesTree = shell.bot().tree();
	}

	public boolean isEnabled()
	{
		return resourcesTree.isEnabled();
	}

	public void selectProject(final IProject project)
	{
		final SWTBotTreeItem projectItem = resourcesTree.getTreeItem(project.getName());
		resourcesTree.select(projectItem);
	}

	public void cancel()
	{
		shell.bot().button(IDialogConstants.CANCEL_LABEL).click();
	}

	public void confirm()
	{
		shell.bot().button(IDialogConstants.OK_LABEL).click();
		// execute a dummy runnable synchronously to ensure event processing prior test continues
		shell.bot().getDisplay().syncExec(new Runnable()
		{
			@Override
			public void run()
			{
			}
		});
	}
}
