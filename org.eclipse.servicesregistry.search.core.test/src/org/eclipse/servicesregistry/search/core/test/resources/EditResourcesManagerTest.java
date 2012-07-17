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

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.platform.discovery.util.internal.StatusUtils;
import org.eclipse.servicesregistry.search.core.internal.resources.EditResourcesManager;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for the EditResourcesManager utility
 * 
 * @author Danail Branekov
 * 
 */
public class EditResourcesManagerTest
{
	private IWorkspace wsReturnOKOnValidate;

	private IWorkspace wsReturnErrorOnValidate;

	private IWorkspace wsReturnWarningOnValidate;

	private IFile fileReturnsErrorOnValidate;

	private IFile fileReturnsOKOnValidate;

	private IFile fileReturnsWarningOnValidate;

	@Before
	public void setUp() throws Exception
	{
		wsReturnErrorOnValidate = mockWorkspace(StatusUtils.statusError("ERROR"));
		wsReturnWarningOnValidate = mockWorkspace(StatusUtils.statusWarning("WARNING"));
		wsReturnOKOnValidate = mockWorkspace(StatusUtils.statusOk("OK"));

		fileReturnsErrorOnValidate = mockFile(wsReturnErrorOnValidate, true);
		fileReturnsWarningOnValidate = mockFile(wsReturnWarningOnValidate, true);
		fileReturnsOKOnValidate = mockFile(wsReturnOKOnValidate, true);
	}
	

	private IWorkspace mockWorkspace(final IStatus statusOnValidateEdit)
	{
		final IWorkspace ws = mock(IWorkspace.class);
		stub(ws.validateEdit((IFile[])anyObject(), anyObject())).toReturn(statusOnValidateEdit);
		return ws;
	}
	
	private IFile mockFile(final IWorkspace ws, final boolean readOnly)
	{
		final IFile file = mock(IFile.class);
		stub(file.getWorkspace()).toReturn(ws);
		stub(file.isReadOnly()).toReturn(readOnly);
		return file;
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetNullFileEditable()
	{
		EditResourcesManager.instance().setFilesEditable(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetEmptyFileArrEditable()
	{
		EditResourcesManager.instance().setFilesEditable(new IFile[0]);
	}
	
	@Test
	public void testSetEditableWithSuccess()
	{
		setEditableTest(fileReturnsOKOnValidate, IStatus.OK);
	}
	
	@Test 
	public void testSetFileEditableWithWarning()
	{
		setEditableTest(fileReturnsWarningOnValidate, IStatus.WARNING);
	}
	
	@Test 
	public void testSetFileEditableWithError()
	{
		setEditableTest(fileReturnsErrorOnValidate, IStatus.ERROR);
	}
	
	private void setEditableTest(final IFile fileToValidate, final int expectedStatusSeverity)
	{
		assertEquals("Unexpected status", expectedStatusSeverity, EditResourcesManager.instance().setFilesEditable(new IFile[]{fileToValidate}).getSeverity());
		verifyWorkspaceValidatedFileEdit(fileToValidate);
	}
	
	@Test
	public void testSetEditableOnWritableFile()
	{
		final IFile nonreadOnlyFile = mockFile(wsReturnOKOnValidate, false);
		assertEquals("Unexpected status", IStatus.OK, EditResourcesManager.instance().setFilesEditable(new IFile[]{nonreadOnlyFile}).getSeverity());
		Mockito.verify(nonreadOnlyFile.getWorkspace(), Mockito.never()).validateEdit(Mockito.any(IFile[].class), Mockito.anyObject());
	}
	
	@Test
	public void testSetEditableWithReadOnlyAndWritableFiles()
	{
		final IFile nonreadOnlyFile = mockFile(wsReturnOKOnValidate, false);
		assertEquals("Unexpected status", IStatus.OK, EditResourcesManager.instance().setFilesEditable(new IFile[]{nonreadOnlyFile, fileReturnsOKOnValidate}).getSeverity());
		verifyWorkspaceValidatedFileEdit(fileReturnsOKOnValidate);
	}
	
	
	private void verifyWorkspaceValidatedFileEdit(final IFile file)
	{
		Mockito.verify(file.getWorkspace(), Mockito.times(1)).validateEdit(Mockito.argThat(fileMatcher(file)), Mockito.anyObject());
	}


	private Matcher<IFile[]> fileMatcher(final IFile file)
	{
		return new BaseMatcher<IFile[]>()
		{
			@Override
			public boolean matches(Object item)
			{
				final IFile[] filesArg = (IFile[])item;
				return filesArg.length == 1 && filesArg[0] == file;
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}

	@Test
	public void testFromNonUiThread() throws InterruptedException
	{
		final Exception[] exceptions = new Exception[1];
		Runnable runnable = new Runnable(){
			public void run()
			{
				try
				{
					testSetEditableWithSuccess();
				}
				catch(Exception e)
				{
					exceptions[0] = e;
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
		thread.join();
		
		if(exceptions[0] != null)
		{
			fail(exceptions[0].getMessage());
		}
	}
}
