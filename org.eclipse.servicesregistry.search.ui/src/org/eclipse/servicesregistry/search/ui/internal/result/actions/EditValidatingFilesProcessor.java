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

import java.io.File;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.servicesregistry.search.core.internal.resources.WorkspaceFilesEditManager;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;

public abstract class EditValidatingFilesProcessor implements IExistingFilesProcessor {

	@Override
	public IStatus process(Set<File> files) {
		if (confirmOverwrite()) {
			return getEditManager().validateEdit(files);
		}
		return Status.CANCEL_STATUS;
	
	}
	
	protected abstract boolean confirmOverwrite();

	private WorkspaceFilesEditManager getEditManager() {
		return new WorkspaceFilesEditManager();
	}

}