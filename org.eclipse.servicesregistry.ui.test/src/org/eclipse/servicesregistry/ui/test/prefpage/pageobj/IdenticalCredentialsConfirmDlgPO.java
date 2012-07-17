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

import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

public class IdenticalCredentialsConfirmDlgPO
{
	public static IdenticalCredentialsConfirmDlgPO find()
	{
		return new IdenticalCredentialsConfirmDlgPO(new SWTBot().shell(SrUiMessages.SrPreferencePage_IdenticalCredentialsDialogTitle));
	}

	private final SWTBotShell shell;
	
	public IdenticalCredentialsConfirmDlgPO(final SWTBotShell shell)
	{
		this.shell = shell;
	}

	public void reuse()
	{
		shell.bot().button(SrUiMessages.SrPreferencePage_ReuseButton).click();
	}

}
