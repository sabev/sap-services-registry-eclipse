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
package org.eclipse.servicesregistry.search.ui.test.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.WorkspaceResourceBrowser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WorkspaceResourceBrowserDestinationFilterTest
{
	private WorkspaceResourceBrowserTestHelper helper;

	private WorkspaceResourceBrowserTestHelper helper()
	{
		return helper;
	}

	@Before
	public void setUp()
	{
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable()
		{
			public void run()
			{
				helper = new WorkspaceResourceBrowserTestHelper();
			}
		});
	}

	@Test
	public void testFilterAddedToBrowser()
	{

		// create own filter
		final ViewerFilter filter = new ViewerFilter()
		{
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element)
			{
				return false;
			}
		};

		// create browser to return the above filter
		TestWorkspaceResourceBrowser browser = new TestWorkspaceResourceBrowser(helper().shell())
		{
			@Override
			public ViewerFilter createBrowseViewerFilter()
			{
				return filter;
			}
		};

		// execute the browser creation
		browser.createDialogArea(helper().comp(), -1);

		assertTrue("The destination filter was not added to the tree viewer", Arrays.asList(browser.viewer().getFilters()).contains(filter));
	}

	@Test
	public void testAllowedResourcesForImportDestination()
	{
		final ViewerFilter[] testedFilter = new ViewerFilter[] { null };
		
		TestWorkspaceResourceBrowser browser = createBrowserWithPreparedDestinationFilter(testedFilter);
		browser.createDialogArea(helper().comp(), -1);
		assertFilteredCreated(testedFilter);

		assertExpectedFilteringResult(false, new AP(testedFilter, browser, createResourceMock(IResource.FILE, false, false)));
		assertExpectedFilteringResult(false, new AP(testedFilter, browser, createResourceMock(IResource.FOLDER, true, false)));
		assertExpectedFilteringResult(false, new AP(testedFilter, browser, createResourceMock(IResource.FOLDER, false, true)));
		assertExpectedFilteringResult(true, new AP(testedFilter, browser, createResourceMock(IResource.FOLDER, false, false)));
	}

	private IResource createResourceMock(int resType, boolean derived, boolean teamPrivate)
	{
		IResource resMock = createBasicIResourceMock(resType);
		Mockito.stub(resMock.isDerived()).toReturn(derived);
		Mockito.stub(resMock.isTeamPrivateMember()).toReturn(teamPrivate);
		return resMock;
	}

	private IResource createBasicIResourceMock(int resType)
	{
		IResource resMock = Mockito.mock(IResource.class);
		Mockito.stub(resMock.getType()).toReturn(resType);
		return resMock;
	}

	private void assertFilteredCreated(final ViewerFilter[] testedFilter)
	{
		assertNotNull(testedFilter[0]);
	}

	private TestWorkspaceResourceBrowser createBrowserWithPreparedDestinationFilter(final ViewerFilter[] testedFilter)
	{
		TestWorkspaceResourceBrowser browser = new TestWorkspaceResourceBrowser(helper().shell())
		{
			@Override
			public ViewerFilter createBrowseViewerFilter()
			{
				testedFilter[0] = super.createBrowseViewerFilter();
				return testedFilter[0];
			}
		};
		return browser;
	}

	/**
	 * 
	 * @param expected -
	 *            the expected result from the filter's select method
	 * @param params -
	 *            the object parameter used for the filter's select parameters
	 */
	private void assertExpectedFilteringResult(boolean expected, AP params)
	{
		assertEquals(expected, params.testedFilter[0].select(params.browser.viewer(), params.browser.viewer(),
										params.resMock));
	}

	@After
	public void tearDown()
	{
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable()
		{
			public void run()
			{
				helper().dispose();
			}
		});
	}

	/**
	 * Utility class for the assert parameters
	 * 
	 * @author Stanislav Nichev
	 * 
	 */
	private class AP
	{
		public final ViewerFilter[] testedFilter;

		public final TestWorkspaceResourceBrowser browser;

		public final IResource resMock;

		public AP(ViewerFilter[] testedFilter, final TestWorkspaceResourceBrowser browser, final IResource resMock)
		{
			this.testedFilter = testedFilter;
			this.resMock = resMock;
			this.browser = browser;
		}

	}

	/**
	 * Helper class for the WorkspaceResourceBrowser. Exposes some protected methods
	 * 
	 * @author Stanislav Nichev
	 * 
	 */
	private class TestWorkspaceResourceBrowser extends WorkspaceResourceBrowser
	{
		public TestWorkspaceResourceBrowser(Shell shell)
		{
			super(shell);
		}

		@Override
		public ViewerFilter createBrowseViewerFilter()
		{
			return super.createBrowseViewerFilter();
		}

		public Control createDialogArea(final Composite parent, int _0)
		{
			final Control[] control = new Control[] { null };

			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable()
			{
				public void run()
				{
					control[0] = createDialogArea(parent); // call to the
					// WorkspaceResourceBrowser's
					// method
				}
			});
			return control[0];
		}

		@Override
		public TreeViewer viewer()
		{
			return super.viewer();
		}
	}
	
	private class WorkspaceResourceBrowserTestHelper {

		private Shell shell = new Shell(Display.getCurrent());
		private Composite comp = new Composite(shell, SWT.NONE);

		/**
		 * 
		 * @return the shell
		 */
		public Shell shell() {
			return shell;
		}

		/**
		 * 
		 * @return the composite for the browser
		 */
		public Composite comp() {
			return comp;
		}

		public void dispose() {
			shell.dispose();
		}

	}
	
}
