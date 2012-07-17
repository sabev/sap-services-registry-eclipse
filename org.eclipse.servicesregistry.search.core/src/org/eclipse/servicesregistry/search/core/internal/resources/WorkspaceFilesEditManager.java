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

import java.io.File;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;

/**
 * Class that manages set of files by checking if they exist in workspace and if they are writable. 
 * 
 * @author Georgi Vachkov
 */
public class WorkspaceFilesEditManager 
{
	/**
	 * Method that checks if some of <code>allFiles</code> provider exist in the workspace.
	 * If there are some existing the provided {@link IExistingFilesProcessor} is called to
	 * define if files should be overwritten. For files that will be overwritten {@link IWorkspace#validateEdit(IFile[], Object)}
	 * is called in order to make them editable (checked-out in source control case).
	 * 
	 * @param allFiles
	 * @return
	 */
	public IStatus validateEdit(final Set<File> allFiles)
	{
		final Set<File> existingFiles = selectExistingFiles(allFiles);
		if (existingFiles.size() == 0) {
			return Status.OK_STATUS;
		}
		
		return makeEditable(existingFiles);
	}

	protected IStatus makeEditable(final Set<File> existingFiles) 
	{
		final Set<IFile> listIFiles = new HashSet<IFile>(existingFiles.size());  
		for (File file : existingFiles) 
		{
			final IFile iFile = findFileResource(file);
			if (iFile == null) {
				throw new IllegalStateException( MessageFormat.format("IFile {0} doest not exist", file.getAbsolutePath())); //$NON-NLS-1$
			}
			
			listIFiles.add(iFile);
		}
		
		return validateEdit(listIFiles.toArray(new IFile[listIFiles.size()]));
	}
	
	protected IStatus validateEdit(final IFile [] existingFiles) {
		return EditResourcesManager.instance().setFilesEditable(existingFiles);
	}
	
	protected IFile findFileResource(final File file) 
	{
		final IPath path = new Path(file.getAbsolutePath());
		
		return getWorkspace().getRoot().getFileForLocation(path);
	}
	
	protected Set<File> selectExistingFiles(final Set<File> allFiles)
	{
		final Set<File> problemFiles = new HashSet<File>();
		for (File file : allFiles)
		{
			if (file.exists()) {
				problemFiles.add(file);
			}
		}
		
		return problemFiles;
	}
	
	protected IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	

}
