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
package org.eclipse.servicesregistry.search.core.internal.resources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * This is an utility class which is meant to provide common functionality to save and modify resources. It is a wrapper of the eclipse API and
 * workarounds some DTR issues. Until DTR colleagues fix their coding, this manager must be used on each file modification occasion
 * 
 * @author Danail Branekov
 */
public class EditResourcesManager
{
	private static final EditResourcesManager instance = new EditResourcesManager();
	
	public static EditResourcesManager instance()
	{
		return instance;
	}
	
	/**
	 * Sets editablility of an array of files. In case all files specified are already writable, <code>Status.OK_STATUS</code> is returned
	 * 
	 * @param files
	 *            The array of files to be set editable
	 * @param context
	 *            either {@link IWorkspace#VALIDATE_PROMPT}, or the <code>org.eclipse.swt.widgets.Shell</code> that is to be used to parent any
	 *            dialogs with the user, or <code>null</code> if there is no UI context (declared as an <code>Object</code> to avoid any direct
	 *            references on the SWT component)
	 * @return the status of the operation
	 * @throws IllegalArgumentException
	 *             when the array is empty or null
	 */
	private IStatus setFilesEditable(IFile[] files, Object context)
	{
		if (files == null || files.length == 0)
		{
			throw new IllegalArgumentException("Array must not be empty nor null"); //$NON-NLS-1$
		}
		List<IFile> roFiles = new ArrayList<IFile>();
		for(IFile f : files)
		{
			if(f.isReadOnly())
			{
				roFiles.add(f);
			}
		}
		
		if(roFiles.size() == 0)
		{
			return Status.OK_STATUS;
		}

		IWorkspace workspace = roFiles.get(0).getWorkspace();
		return workspace.validateEdit(roFiles.toArray(new IFile[roFiles.size()]), context);
	}

	/**
	 * Sets editablility of an array of files. This is a convenience method equivalent to invoking
	 * <code>setFilesEditable(files, IWorkspace.VALIDATE_PROMPT)</code> 
	 * 
	 * @param files
	 *            The array of files to be set editable
	 * @return the status of the operation
	 * @throws IllegalArgumentException
	 *             when the array is empty or null
	 * @see EditResourcesManager#setFilesEditable(IFile[], Object)
	 */
	public IStatus setFilesEditable(IFile[] files)
	{
		return setFilesEditable(files, IWorkspace.VALIDATE_PROMPT);
	}
}
