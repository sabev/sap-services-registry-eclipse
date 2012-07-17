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
package org.eclipse.servicesregistry.ui.test.prefpage.pageobj;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

public class DeleteConfigConfirmDlgPO
{
	public static DeleteConfigConfirmDlgPO find()
	{
		return new DeleteConfigConfirmDlgPO(new SWTBot().shell(SrUiMessages.SrPreferencesController_DeleteConfigConfirmTitle));
	}

	private final SWTBotShell shell;
	
	public DeleteConfigConfirmDlgPO(SWTBotShell shell)
	{
		this.shell = shell;
	}

	public void cancel()
	{
		shell.bot().button(SrUiMessages.SrPreferencesController_NoButton).click();
		syncExecRunnable();
	}
	
	private void syncExecRunnable()
	{
		syncExec(new VoidResult()
		{
			@Override
			public void run()
			{
			}
		});
	}

	public void confirm()
	{
		shell.bot().button(SrUiMessages.SrPreferencesController_YesButton).click();
		syncExecRunnable();
	}
}
