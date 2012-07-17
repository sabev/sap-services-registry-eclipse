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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.platform.discovery.util.api.env.IErrorHandler;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.platform.discovery.util.internal.longop.CurrentThreadOperationRunner;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.IActionConfiguration;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.GetEndpointWsdlAction;
import org.eclipse.servicesregistry.search.ui.test.result.actions.pageobjects.GetEndpointWsdlDialogPO;
import org.eclipse.servicesregistry.testutils.TestProject;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;
import org.eclipse.swt.widgets.Display;
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

public class GetEndpointWsdlActionTest
{
	private static final String ACTION_NAME = "MyTestAction";
	private GetEndpointWsdlAction action;

	@Mock
	private IContributedAction wsdlAction;
	@Mock
	private IDiscoveryEnvironment environment;
	@Mock
	private IErrorHandler errorHandler;
	@Mock
	private IExistingFilesProcessor existingFilesProcessor;
	@Mock
	private IServiceEndpoint serviceEndpoint;

	private GetEndpointWsdlDialogPO epWsdlDialogPO;
	private TestProject targetProject;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		Mockito.stub(environment.operationRunner()).toReturn(new CurrentThreadOperationRunner(new NullProgressMonitor()));
		Mockito.stub(environment.errorHandler()).toReturn(errorHandler);

		action = new GetEndpointWsdlAction(serviceEndpoint, wsdlAction, environment)
		{
			@Override
			public String getText()
			{
				return ACTION_NAME;
			}

			@Override
			protected IExistingFilesProcessor getExistingFilesProcessor()
			{
				return existingFilesProcessor;
			}
		};

		targetProject = new TestProject("WsdlImportProject");
		epWsdlDialogPO = startDialog();
	}

	@After
	public void tearDown() throws Exception
	{
		targetProject.dispose();
		if (epWsdlDialogPO.isOpened())
		{
			epWsdlDialogPO.close();
		}
	}

	@Test
	public void testDefaultUiState()
	{
		assertTrue("Open WSDL checkbox should be selected by default", epWsdlDialogPO.isOpenWsdlInEditorSelected());
		assertFalse("Save WSDL checkbox should be deselected by default", epWsdlDialogPO.isSaveWsdlSelected());
		assertFalse("Resource tree should be disabled by default", epWsdlDialogPO.isResourceBrowserEnabled());
	}

	@Test
	public void testOpenWsdl()
	{
		confirmDialogAndVerifyWsdlAction();
	}

	@Test
	public void testSaveWsdl()
	{
		epWsdlDialogPO.deselectOpenWsdlInEditor();
		epWsdlDialogPO.selectSaveWsdl();
		assertTrue("Resources tree should be enabled now", epWsdlDialogPO.isResourceBrowserEnabled());
		epWsdlDialogPO.selectProject(targetProject.getProject());

		confirmDialogAndVerifyWsdlAction();
	}

	@Test
	public void testOpenSaveWsdl()
	{
		epWsdlDialogPO.selectSaveWsdl();
		assertTrue("Resources tree should be enabled now", epWsdlDialogPO.isResourceBrowserEnabled());

		epWsdlDialogPO.selectProject(targetProject.getProject());
		confirmDialogAndVerifyWsdlAction();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCancelDialog()
	{
		epWsdlDialogPO.cancel();
		Mockito.verify(wsdlAction, Mockito.never()).perform(Mockito.any(ILongOperationRunner.class), Mockito.anySet());
	}

	@Test
	public void testDeselectingBothCheckboxesDisablesOk()
	{
		epWsdlDialogPO.deselectOpenWsdlInEditor();
		assertFalse(epWsdlDialogPO.isOKEnabled());
	}

	@Test
	public void testSelectingSaveWsdlCheckboxEnablesResourceBrowser()
	{
		epWsdlDialogPO.selectSaveWsdl();
		assertTrue("Resource tree should be enabled", epWsdlDialogPO.isResourceBrowserEnabled());
		epWsdlDialogPO.deselectSaveWsdl();
		assertFalse("Resource tree should be diabled", epWsdlDialogPO.isResourceBrowserEnabled());
	}

	private Matcher<Set<Object>> saveDestinationMatcher(final File saveWsdlTarget)
	{
		return new BaseMatcher<Set<Object>>()
		{

			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(Object item)
			{
				final IActionConfiguration actionConfigArg = (IActionConfiguration) ((Set<Object>) item).iterator().next();
				return saveWsdlTarget.equals(actionConfigArg.saveDestination());
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}

	private GetEndpointWsdlDialogPO startDialog()
	{
		getDisplay().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				action.run();
			}
		});
		return GetEndpointWsdlDialogPO.findDialog(ACTION_NAME);
	}

	private void confirmDialogAndVerifyWsdlAction()
	{
		epWsdlDialogPO.confirm();
		Mockito.verify(wsdlAction, Mockito.times(1)).perform(Mockito.same(environment.operationRunner()), Mockito.argThat(saveDestinationMatcher(targetProject.getProject().getLocation().toFile())));
	}

	private Display getDisplay()
	{
		return PlatformUI.getWorkbench().getDisplay();
	}
}
