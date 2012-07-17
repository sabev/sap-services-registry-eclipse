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
package org.eclipse.servicesregistry.search.ui.test.result.actions;

import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.platform.discovery.util.api.env.IErrorHandler;
import org.eclipse.platform.discovery.util.internal.StringInputStreamAdapter;
import org.eclipse.platform.discovery.util.internal.longop.CurrentThreadOperationRunner;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.config.OpenWsdlInEditorConfig;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.config.OpenWsdlInEditorConfig.IWsdlUrlProvider;
import org.eclipse.servicesregistry.testutils.TestProject;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class OpenWsdlInEditorConfigTest
{
	@Mock
	private IDiscoveryEnvironment env;
	@Mock
	private IErrorHandler errHandler;

	private TestProject testProject;

	@Before
	public void setUp() throws CoreException
	{
		MockitoAnnotations.initMocks(this);
		Mockito.stub(env.errorHandler()).toReturn(errHandler);

		testProject = new TestProject();
	}

	@After
	public void tearDown() throws CoreException
	{
		testProject.dispose();
	}

	@Test
	public void testWsdlDownloadedOpensWsdlEditor() throws CoreException, MalformedURLException, InvocationTargetException, InterruptedException
	{
		final IFile wsdlFile = createFile("dummy.wsdl", "");
		final IWsdlWtpDescriptorContainer wsdlContainer = mockWsdlContainer(new File(wsdlFile.getLocation().toOSString()));
		signalWsdlDownloaded(wsdlFile, wsdlContainer);
		assertWsdlEditorOpenedFor(wsdlFile);
	}

	private void signalWsdlDownloaded(final IFile wsdlFile, final IWsdlWtpDescriptorContainer wsdlContainer) throws MalformedURLException, InvocationTargetException, InterruptedException
	{
		final IWsdlUrlProvider urlProvider = new IWsdlUrlProvider()
		{
			@Override
			public String getWsdlUrl()
			{
				try
				{
					return wsdlFile.getLocationURI().toURL().toExternalForm();
				}
				catch (MalformedURLException e)
				{
					throw new IllegalStateException(e);
				}
			}
		};
		final OpenWsdlInEditorConfig config = new OpenWsdlInEditorConfig(urlProvider, env)
		{
			protected void executeRunnableInUI(Runnable runnable)
			{
				runnable.run();
			};
		};

		config.wsdlDownloaded(wsdlContainer, new CurrentThreadOperationRunner(new NullProgressMonitor()));
	}

	private void assertWsdlEditorOpenedFor(final IFile wsdlFile) throws PartInitException
	{
		final IEditorReference[] openedEditors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		for (IEditorReference editorRef : openedEditors)
		{
			final IEditorInput editorInput = editorRef.getEditorInput();
			if (!(editorInput instanceof IFileEditorInput))
			{
				continue;
			}

			final IFileEditorInput fileInput = (IFileEditorInput) editorInput;
			if (fileInput.getFile().equals(wsdlFile))
			{
				// Found an editor for the file specified
				return;
			}
		}

		fail("No editor opened for file " + wsdlFile.getLocation());
	}

	private IFile createFile(final String fileName, final String content) throws CoreException
	{
		final IFile file = testProject.getProject().getFile(fileName);
		file.create(new StringInputStreamAdapter(content), true, new NullProgressMonitor());
		return file;
	}

	private IWsdlWtpDescriptorContainer mockWsdlContainer(final File rootWsdl)
	{
		final IWsdlDefinition rootWsdlDef = Mockito.mock(IWsdlDefinition.class);
		Mockito.stub(rootWsdlDef.getFile()).toReturn(rootWsdl);
		final IWsdlWtpDescriptorContainer container = Mockito.mock(IWsdlWtpDescriptorContainer.class);
		Mockito.stub(container.getRootWsdlDefinition()).toReturn(rootWsdlDef);
		return container;
	}

}
