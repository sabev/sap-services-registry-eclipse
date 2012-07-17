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
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

public class GetEndpointWsdlDialogPO
{
	public static GetEndpointWsdlDialogPO findDialog(final String shellText)
	{
		return new GetEndpointWsdlDialogPO(new SWTBot().shell(shellText));
	}

	private final SWTBotShell shell;
	
	public GetEndpointWsdlDialogPO(final SWTBotShell shell)
	{
		this.shell = shell;
	}

	public boolean isOpenWsdlInEditorSelected()
	{
		return openWsdlInEditorCheckBox().isChecked();
	}
	
	private SWTBotCheckBox openWsdlInEditorCheckBox()
	{
		return checkBox(SearchUIMessages.GetWsdlAction_OpenWsdlInEditorCheckBox);
	}

	public boolean isSaveWsdlSelected()
	{
		return saveWsdlCheckBox().isChecked();
	}

	private SWTBotCheckBox saveWsdlCheckBox()
	{
		return checkBox(SearchUIMessages.GetWsdlAction_SaveWsdlCheckBox);
	}
	
	private SWTBotCheckBox checkBox(final String text)
	{
		return shell.bot().checkBox(text);
	}

	public boolean isResourceBrowserEnabled()
	{
		return resourceBrowser().isEnabled();
	}
	
	private ResourceBrowserPO resourceBrowser()
	{
		return new ResourceBrowserPO(shell);
	}

	public void deselectOpenWsdlInEditor()
	{
		if(openWsdlInEditorCheckBox().isChecked())
		{
			openWsdlInEditorCheckBox().click();
		}
	}

	public void selectSaveWsdl()
	{
		if(!saveWsdlCheckBox().isChecked())
		{
			saveWsdlCheckBox().click();
		}
	}

	public void selectProject(final IProject project)
	{
		resourceBrowser().selectProject(project);
	}

	public void cancel()
	{
		shell.bot().button(IDialogConstants.CANCEL_LABEL).click();
	}

	public boolean isOKEnabled()
	{
		return okButton().isEnabled();
	}

	private SWTBotButton okButton()
	{
		return shell.bot().button(IDialogConstants.OK_LABEL);
	}

	public void deselectSaveWsdl()
	{
		if(saveWsdlCheckBox().isChecked())
		{
			saveWsdlCheckBox().click();
		}
	}

	public void confirm()
	{
		okButton().click();
		// execute a dummy runnable synchronously to ensure event processing prior test continues
		shell.bot().getDisplay().syncExec(new Runnable()
		{
			@Override
			public void run()
			{
			}
		});
	}

	public boolean isOpened()
	{
		return shell.isOpen();
	}

	public void close()
	{
		shell.close();
	}

}
