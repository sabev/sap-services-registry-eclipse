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


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Existing files processor that opens dialog to the user to ask if he wants the
 * files to be overwritten
 * 
 * @author Georgi Vachkov
 */
public class InteractiveExistingFilesProcessor extends EditValidatingFilesProcessor implements IExistingFilesProcessor {
	
	protected boolean confirmOverwrite() {
		final boolean[] resultHolder = new boolean[1];
		final Runnable confirmRunnable = confirmRunnable(resultHolder);
		if (Display.getCurrent() == null) {
			PlatformUI.getWorkbench().getDisplay().syncExec(confirmRunnable);
		} else {
			confirmRunnable.run();
		}
		return resultHolder[0];
	}

	private Runnable confirmRunnable(final boolean[] resultHolder) {
		return new Runnable() {
			@Override
			public void run() {
				final Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				resultHolder[0] = MessageDialog
						.openQuestion(
								shell,
								SearchUIMessages.FilesToOverwriteWithDialog_DialogTitle,
								SearchUIMessages.FilesToOverwriteWithDialog_DialogMessage);
			}
		};
	}
}