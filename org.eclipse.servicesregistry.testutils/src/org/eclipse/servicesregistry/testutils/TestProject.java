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
package org.eclipse.servicesregistry.testutils;

import java.lang.reflect.InvocationTargetException;
import java.util.ConcurrentModificationException;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class TestProject
{
	public final static char CLASS_SEPARATOR = '#';
	
	private int idInMillis = 0;
	
	private IProject project;

	private IJavaProject javaProject;

	private IPackageFragmentRoot sourceFolder;

	public TestProject() throws CoreException
	{
		this("");
	}
	
	public TestProject(String name) throws CoreException
	{
		this(name, true);
	}
	
	public TestProject(String name, boolean appendTimeStamp) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if(appendTimeStamp) {
			/*
			 * Calculate the name of the test project. The formula: JavaProj_ + <current_time_in_millis? + <idInMillis> is ussed. In this formula
			 * JavaProj_ is a string litteral, current_time_in_millis is obvious. idInMillis is an integer incremented each time a project name is
			 * calculated. This is necessary because the system timer has precision about 10 ms. Thus it's possible that two consecutive project creations
			 * give the same project name. For example the project is creted then the test executes very fast and a new project is created again. In the
			 * same time simply the id is not enough as a different test run might produce the same project name. Thus the combination tim_millis +
			 * idInMillis gives a unique project name
			 */
			
			name = "JavaProj_" + Long.toString(System.currentTimeMillis()) + "_" + idInMillis++ + name;
		}
		
		project = root.getProject(name);
		project.create(null);
		project.open(null);

		configureJavaProject();
	}
	
	private void configureJavaProject() throws CoreException
	{
		javaProject = JavaCore.create(project);

		IFolder binFolder = createBinFolder();

		setJavaNature();
		javaProject.setRawClasspath(new IClasspathEntry[0], null);

		createOutputFolder(binFolder);
		addSystemLibraries();
	}

	public TestProject(final IProject project) throws CoreException
	{
		if (project == null)
		{
			throw new NullPointerException("project should not be null");
		}

		this.project = project;
		this.javaProject = JavaCore.create(project);
		this.sourceFolder = findSourceFolder();
	}

	private IPackageFragmentRoot findSourceFolder() throws JavaModelException
	{
		for (IPackageFragmentRoot pfr : javaProject.getAllPackageFragmentRoots())
		{
			if (pfr.getKind() == IPackageFragmentRoot.K_SOURCE)
			{
				return pfr;
			}
		}

		return null;
	}

	public IProject getProject()
	{
		return project;
	}

	public IJavaProject getJavaProject()
	{
		return javaProject;
	}

	public IPackageFragment createPackage(String name) throws CoreException
	{
		if (sourceFolder == null)
			sourceFolder = createSourceFolder("src");
		return sourceFolder.createPackageFragment(name, false, null);
	}
	public void dispose() throws CoreException
	{
		final IRunnableWithProgress deleteProjectRunnable = new IRunnableWithProgress()
		{
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
			{
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
					project.delete(true, true, null);
				} catch (ResourceException re) {
					/*
					 * silently swallow the resource exception. For some reason this exception gets thrown
					 * from time to time and reports the test failing. The project deletion itself happens
					 * after the test has completed and a failure will not report a problem in the test.
			    	 * Only ResourceException is caught in order not to hide unexpected errors.	
					 */
					return;
				} catch (ConcurrentModificationException e) {
					/*
					 * silently swallow the ConcurrentModificationException exception. For some reason this exception gets thrown
					 * from time to time and reports the test failing. The project deletion itself happens
					 * after the test has completed and a failure will not report a problem in the test.
			    	 * Only ConcurrentModificationException is caught in order not to hide unexpected errors.	
					 */
					return;
				}
				catch(CoreException e)
				{
					throw new InvocationTargetException(e);
				}
			}
		};
		
		try
		{
			ModalContext.run(deleteProjectRunnable, true, new NullProgressMonitor(), PlatformUI.getWorkbench().getDisplay());
		} catch (InvocationTargetException e)
		{
			if(e.getTargetException() instanceof CoreException)
			{
				throw (CoreException)e.getTargetException();
			}
			throw new RuntimeException(e);
		} catch (InterruptedException e)
		{
			throw new IllegalStateException("Interruption not supported", e);
		}
	}
	
	public void close() throws CoreException
	{
		project.close(new NullProgressMonitor());		
	}

	private IFolder createBinFolder() throws CoreException
	{
		IFolder binFolder = project.getFolder("bin");
		binFolder.create(false, true, null);
		return binFolder;
	}

	private void setJavaNature() throws CoreException
	{
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, null);
	}

	private void createOutputFolder(IFolder binFolder) throws JavaModelException
	{
		IPath outputLocation = binFolder.getFullPath();
		javaProject.setOutputLocation(outputLocation, null);
	}

	public IPackageFragmentRoot createSourceFolder(String name) throws CoreException
	{
		IFolder folder = project.getFolder(name);
		folder.create(false, true, null);
		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(folder);

		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
		javaProject.setRawClasspath(newEntries, null);
		this.sourceFolder = root;
		
		String log = "\n"+javaProject.getProject().getName()+"\n"+String.valueOf(javaProject.getRawClasspath().length)+"\n"; 

		for(int ii=0; ii < javaProject.getRawClasspath().length; ii++)
		{
			log = log +  javaProject.getRawClasspath()[ii].getPath().toString()+"\n";
		}
		
		if(javaProject.getRawClasspath().length==0)
		{
			log = log + "Classpath not initialized !\n";
		}
		
		ResourcesPlugin.getPlugin().getLog().log(new Status(0,"testOutput",log));
		
		return root;
	}

	private void addSystemLibraries() throws JavaModelException
	{
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaRuntime.getDefaultJREContainerEntry();
		javaProject.setRawClasspath(newEntries, null);
		
		String log = "\n"+javaProject.getProject().getName()+"\n"+String.valueOf(javaProject.getRawClasspath().length)+"\n"; 

		for(int ii=0; ii < javaProject.getRawClasspath().length; ii++)
		{
			log = log +  javaProject.getRawClasspath()[ii].getPath().toString()+"\n";
		}
		
		if(javaProject.getRawClasspath().length==0)
		{
			log = log + "Classpath not initialized !\n";
		}
		
		ResourcesPlugin.getPlugin().getLog().log(new Status(0,"testOutput",log));
	}

	public IPackageFragmentRoot getSourceFolder()
	{
		return sourceFolder;
	}
	
	/**
	 * 
	 * @return the container corresponding to the package fragment root, i.e. the source folder.<br>
	 * If this project is a jar/zip (if this is possible at all), the method will return null.
	 */
	public IContainer getSourceFolderContainer()
	{
		try{
			IResource res = getSourceFolder().getCorrespondingResource();
			if(res==null) {
				return null;
			}else {
				IContainer retVal = (IContainer) res;
				return retVal;
			}
		}catch(JavaModelException ex) {
			return null;
		}catch(ClassCastException ex) {
			return null;
		}
	}

	public IFolder createFolder(final String folderName) throws CoreException
	{
		ContractChecker.nullCheckParam(folderName, "folderName");
		final IFolder folder = this.project.getFolder(folderName);
		folder.create(false, true, null);
		return folder;		
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("IProject: ").append(getProject().toString()).append("\n");
		result.append("IJavaProject: ").append(getJavaProject().toString()).append("\n");
		return result.toString();
	}
}
