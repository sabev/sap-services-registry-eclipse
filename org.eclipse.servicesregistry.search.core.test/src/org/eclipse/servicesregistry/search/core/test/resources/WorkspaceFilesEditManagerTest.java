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
package org.eclipse.servicesregistry.search.core.test.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.platform.discovery.util.internal.StringInputStreamAdapter;
import org.eclipse.servicesregistry.search.core.internal.resources.WorkspaceFilesEditManager;
import org.eclipse.servicesregistry.testutils.TestProject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link WorkspaceFilesEditManager} class
 * 
 * @author Georgi Vachkov
 */
@SuppressWarnings("nls")
public class WorkspaceFilesEditManagerTest
{
	private TestProject testProject;
	private IFolder folder;
	private MyWorkspaceFilesEditManager manager;
	
	@Before
	public void setUp() throws CoreException 
	{
		testProject = new TestProject();
		folder = testProject.createFolder("Test Folder");
		manager = new MyWorkspaceFilesEditManager();
	}
	
	@After
	public void tearDown() throws CoreException 
	{
		testProject.dispose();
	}
	
	@Test
	public void testSelectExistingFiles() throws CoreException
	{
		int existing = 2;
		int notExisting = 3;
		
		final Set<File> allFiles = createFiles(existing, notExisting);
		final Set<File> existingFiles = manager.selectExistingFiles(allFiles);
		assertEquals("Selection of existing files incorrect", existing, existingFiles.size());
	}
	
	@Test
	public void testFindFileResource() throws CoreException
	{
		final Set<File> files = createFiles(3, 0);
		for (File file : files) {
			final IFile iFile = manager.findFileResource(file);
			assertNotNull("IFile for existing file cannot be found", iFile);
		}
	}
	
	@Test
	public void testMakeFilesEditable() throws CoreException
	{
		final Set<File> existingFiles = createFiles(3, 0);
		final Map<File, IFile> files = new HashMap<File, IFile>();
		final boolean [] called = {false};  
		
		manager = new MyWorkspaceFilesEditManager() {
			@Override
			protected IStatus validateEdit(final IFile [] existingFiles) {
				called[0] = true;
				assertEquals("Incorrect files count provided", 3, existingFiles.length);
				return Status.OK_STATUS;
			}
		};
		
		for (File file : existingFiles) {
			final IFile iFile = manager.findFileResource(file);
			files.put(file, iFile);
		}
		
		manager.makeEditable(existingFiles);
		assertTrue("validateEdit method was not called at all", called[0]);
	}
	
	@Test
	public void testValidateEditNoExistinfFiles() throws CoreException 
	{
		final Set<File> allFiles = createFiles(0, 3);
		assertEquals("Incorrect status", Status.OK_STATUS, manager.validateEdit(allFiles));
	}
	
	@Test
	public void testValidateEdit() throws CoreException
	{
		final Set<File> allFiles = createFiles(2, 3);
		manager.checkMakeEditableCalled = true;
		manager.validateEdit(allFiles);
		assertTrue(manager.makeEditableCalled);
	}
	
	
	// # utilities section
	
	private Set<File> createFiles(int existing, int notExisting) throws CoreException 
	{
		final Set<File> files = new TreeSet<File>();
		for (int i = 0; i < existing; i++) {
			files.add(new File(createFile("F" + i + ".txt", "").getLocation().toOSString()));
		}
		
		for (int i = 0; i < notExisting; i++) {		
			files.add(new File("C:/U" + i + ".txt"));
		}
		
		return files;
	}
	
	private IFile createFile(final String path, final String content) throws CoreException 
	{
		IFile file = folder.getFile(path);
		file.create(new StringInputStreamAdapter(content), true, null);
		
		return file;
	}
	
	private class MyWorkspaceFilesEditManager extends WorkspaceFilesEditManager
	{
		IWorkspace workspace;
		Map<File, IFile> filesMap;
		boolean checkMakeEditableCalled;
		boolean makeEditableCalled;
		
		@Override
		protected Set<File> selectExistingFiles(final Set<File> allFiles) {
			return super.selectExistingFiles(allFiles);
		}
		
		@Override
		protected IFile findFileResource(final File file) {
			if (filesMap != null) {
				return filesMap.get(file);
			}
			return super.findFileResource(file);
		}
		
		@Override
		protected IStatus makeEditable(final Set<File> existingFiles)  {
			if (checkMakeEditableCalled) {
				makeEditableCalled = true; 
				return Status.OK_STATUS;
			}
			return super.makeEditable(existingFiles);
		}
		
		@Override
		protected IWorkspace getWorkspace() {
			if (workspace == null) {
				return super.getWorkspace();
			}

			return workspace;
		}
	}
}
