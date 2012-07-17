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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.SaveWsdlInWorkspaceAction;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.config.SaveWsdlConfig;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.servicesregistry.search.ui.test.result.actions.pageobjects.ResourceBrowserPO;
import org.eclipse.servicesregistry.testutils.TestProject;
import org.eclipse.ui.PlatformUI;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SaveWsdlInWorkspaceActionTest
{
	private static final String ROOT_WSDL_FILE_NAME = "root.wsdl";

	@Mock
	private IServiceDefinition serviceDef;
	@Mock
	private IDiscoveryEnvironment env;
	@Mock
	private IContributedAction contributedAction;

	private TestProject testProject;
	private SaveWsdlInWorkspaceAction action;
	private File wsdlSourceFile;

	@Before
	public void setUp() throws CoreException, IOException
	{
		MockitoAnnotations.initMocks(this);
		testProject = new TestProject();
		action = new SaveWsdlInWorkspaceAction(actionConfig(), contributedAction, env);
		wsdlSourceFile = File.createTempFile("tempwsdl", "wsdl");
		wsdlSourceFile.deleteOnExit();
	}

	private SaveWsdlConfig actionConfig()
	{
		return new SaveWsdlConfig(serviceDef, env)
		{
			@Override
			public String wsdlUrl()
			{
				try
				{
					return wsdlSourceFile.toURI().toURL().toExternalForm();
				}
				catch (MalformedURLException e)
				{
					throw new IllegalStateException(e);
				}
			}

			@Override
			public String rootWsdlFileName()
			{
				return ROOT_WSDL_FILE_NAME;
			}
		};
	}

	@After
	public void tearDown() throws CoreException
	{
		testProject.dispose();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCancel()
	{
		runAction();
		final ResourceBrowserPO resourceBrowser = findResourceBrowser();
		resourceBrowser.cancel();
		Mockito.verify(contributedAction, Mockito.never()).perform(Mockito.any(ILongOperationRunner.class), Mockito.anySet());
	}

	@Test
	public void testSave()
	{
		runAction();
		final ResourceBrowserPO resourceBrowser = findResourceBrowser();
		resourceBrowser.selectProject(testProject.getProject());
		resourceBrowser.confirm();
		Mockito.verify(contributedAction, Mockito.times(1)).perform(Mockito.any(ILongOperationRunner.class), Mockito.argThat(configWithProjectDownloadLocation()));
	}

	private Matcher<Set<Object>> configWithProjectDownloadLocation()
	{
		return new BaseMatcher<Set<Object>>()
		{
			@Override
			public boolean matches(Object item)
			{
				@SuppressWarnings("unchecked")
				final SaveWsdlConfig configArg = (SaveWsdlConfig) (((Set<Object>) item).iterator().next());
				return configArg.saveDestination().equals(new File(testProject.getProject().getLocation().toOSString()));
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}

	private void runAction()
	{
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				action.run();
			}
		});
	}

	private ResourceBrowserPO findResourceBrowser()
	{
		return ResourceBrowserPO.find(SearchUIMessages.WorkspaceResourceBrowser_BROWSER_SHELL_TITLE);
	}

}
