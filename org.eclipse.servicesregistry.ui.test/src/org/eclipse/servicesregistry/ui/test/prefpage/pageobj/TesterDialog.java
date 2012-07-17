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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

class TesterDialog extends Dialog
{
	private final IDialogPage dlgPage;

	TesterDialog(Shell parentShell, final IDialogPage dlgPage)
	{
		super(parentShell);
		this.dlgPage = dlgPage;
	}

	@Override
	protected Control createContents(Composite parent)
	{
		dlgPage.createControl(parent);
		return parent;
	}
}