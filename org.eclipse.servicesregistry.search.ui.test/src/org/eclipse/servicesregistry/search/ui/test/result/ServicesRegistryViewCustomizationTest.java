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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.platform.discovery.ui.api.IMasterDiscoveryView;
import org.eclipse.platform.discovery.ui.api.IResultsViewAccessor;
import org.eclipse.servicesregistry.search.core.internal.search.SrSearchProvider;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.IResolvable;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.DownloadWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.GetEndpointWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.OpenWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.result.ServicesContentProvider;
import org.eclipse.servicesregistry.search.ui.internal.result.ServicesLabelProvider;
import org.eclipse.servicesregistry.search.ui.internal.result.ServicesRegistryViewCustomization;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.GetEndpointWsdlAction;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.OpenWsdlInEditorAction;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.SaveWsdlInWorkspaceAction;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ServicesRegistryViewCustomizationTest
{
	@Mock
	private IMasterDiscoveryView masterView;
	@Mock
	private IResultsViewAccessor viewAccessor;
	@Mock
	private IServiceDefinition serviceDefinition;
	@Mock
	private IServiceEndpoint serviceEndpoint;
	
	private ServicesRegistryViewCustomization customization;
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		
		customization = new ServicesRegistryViewCustomization();
		customization.setMasterView(masterView);
		
		final TreeViewer resultsTreeViewer = mockTreeViewer();
		Mockito.stub(viewAccessor.getTreeViewer()).toReturn(resultsTreeViewer);
		
		final MenuManager menuManager = new MenuManager();
		Mockito.stub(viewAccessor.getMenuManager()).toReturn(menuManager);
	}

	private TreeViewer mockTreeViewer()
	{
		return new TreeViewer(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()) {
			private ISelection selection;
			@Override
			public ISelection getSelection()
			{
				return selection;
			}
			
			@Override
			public void setSelection(ISelection selection)
			{
				this.selection = selection;
			}
		};
	}
	
	@After
	public void tearDown()
	{
		viewAccessor.getTreeViewer().getControl().dispose();
	}
	
	@Test
	public void testGetContentProvider()
	{
		assertEquals("Unexpected content provider class", ServicesContentProvider.class, customization.getContentProvider().getClass());
	}
	
	@Test
	public void testGetLabelProvider()
	{
		assertEquals("Unexpected label provider", ServicesLabelProvider.class, customization.getLabelProvider().getClass());
	}
	
	@Test
	public void testAcceptSearchProvider()
	{
		assertFalse("Foreign search provider accepted", customization.acceptSearchProvider("some.other.search.provider"));
		assertTrue("ES search provider not accepted", customization.acceptSearchProvider(SrSearchProvider.SR_SEARCH_PROVIDER_ID));
	}
	
	@Test
	public void testNoActionsInstalledOnSelectionWithMultipleElements()
	{
		setSelection(serviceEndpoint, serviceDefinition);
		customization.installAction(mockDummyAction(), viewAccessor);
		assertEquals("No actions expected to be installed for selection with many elements", 0, viewAccessor.getMenuManager().getItems().length);
	}
	
	@Test 
	public void testActionsOnAnyObject()
	{
		setSelection(new Object());
		customization.installAction(mockDummyAction(), viewAccessor);
		assertEquals("No actions expected to be installed for selection with unknown object", 0, viewAccessor.getMenuManager().getItems().length);
	}

	@Test 
	public void testActionsForEmptySelection()
	{
		setSelection();
		customization.installAction(mockDummyAction(), viewAccessor);
		assertEquals("No actions expected to be installed for empty selection", 0, viewAccessor.getMenuManager().getItems().length);
	}
	
	@Test 
	public void testInstallUnknownActionOnServiceDefinition()
	{
		setSelection(serviceDefinition);
		customization.installAction(mockDummyAction(), viewAccessor);
		assertEquals("No actions expected to be installed for unknown action on service definition", 0, viewAccessor.getMenuManager().getItems().length);
	}

	@Test 
	public void testInstallUnknownActionOnServiceEndpoint()
	{
		setSelection(serviceEndpoint);
		customization.installAction(mockDummyAction(), viewAccessor);
		assertEquals("No actions expected to be installed for unknown action on service endpoint", 0, viewAccessor.getMenuManager().getItems().length);
	}
	
	@Test
	public void testInstallDownloadWsdlActionOnServiceDefinition()
	{
		installActionTest(serviceDefinition, new DownloadWsdlContributedAction(), SaveWsdlInWorkspaceAction.class);
	}
	
	@Test
	public void testInstallOpenWsdlActionOnServiceDefinition()
	{
		installActionTest(serviceDefinition, new OpenWsdlContributedAction(), OpenWsdlInEditorAction.class);
	}
	
	@Test 
	public void testInstallEndpointWsdlActionOnEndpoint()
	{
		installActionTest(serviceEndpoint, new GetEndpointWsdlContributedAction(), GetEndpointWsdlAction.class);
	}
	
	@Test
	public void testSelectionChangedToUnresolvedObjectResolvesTheFirstOne()
	{
		final IResolvable firstUnresolved = mockResolvable(false);
		final IResolvable secondUnresolved = mockResolvable(false);
		customization.selectionChanged(selectionWithResolvables(firstUnresolved, secondUnresolved));
		Mockito.verify(firstUnresolved, Mockito.times(1)).resolve();
		Mockito.verify(secondUnresolved, Mockito.never()).resolve();
	}

	@Test
	public void testSelectionChangedToResolvedObject()
	{
		final IResolvable unresolved = mockResolvable(true);
		customization.selectionChanged(selectionWithResolvables(unresolved));
		Mockito.verify(unresolved, Mockito.never()).resolve();
	}
	
	private ISelection selectionWithResolvables(final IResolvable... resolvables)
	{
		return new StructuredSelection(resolvables);
	}

	private IResolvable mockResolvable(boolean resolved)
	{
		final IResolvable resolvable = Mockito.mock(IResolvable.class);
		Mockito.stub(resolvable.isResolved()).toReturn(resolved);
		return resolvable;
	}

	private void installActionTest(final Object selectedObject, final IContributedAction contributedAction, Class<? extends IAction> expectedMenuAction)
	{
		setSelection(selectedObject);
		customization.installAction(contributedAction, viewAccessor);
		assertEquals("Unexpected menu action installed", expectedMenuAction, getInstalledAction().getClass());
	}
	
	private IAction getInstalledAction()
	{
		for(IContributionItem item : viewAccessor.getMenuManager().getItems())
		{
			if(item instanceof ActionContributionItem)
			{
				return ((ActionContributionItem)item).getAction();
			}
		}
		fail("No actions installed");
		throw new IllegalStateException();
	}

	private void setSelection(final Object... selectedObjects)
	{
		viewAccessor.getTreeViewer().setSelection(new StructuredSelection(selectedObjects));
	}
	
	private IContributedAction mockDummyAction()
	{
		final IContributedAction action = Mockito.mock(IContributedAction.class);
		Mockito.stub(action.getActionId()).toReturn("something unknown");
		return action;
	}
}
