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
package org.eclipse.servicesregistry.search.ui.test.properties;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;

public abstract class AbstractPropertiesTest<T extends AbstractPropertySection> {
	protected static SWTBot bot;
	private static SWTBotShell swtBotShell;
	
	protected abstract T createPropertiesSection();
	protected abstract Object createInput();
	
	
	@Before
	public void setUp() {
		
		UIThreadRunnable.syncExec(new VoidResult() {
			
			@Override
			public void run() {
				
				final Shell shell = new Shell();
				shell.setVisible(true);
				
				TabbedPropertySheetPage tabbedPage = new TabbedPropertySheetPage(createContributor());
			
				tabbedPage.init(createPageSite());
				tabbedPage.createControl(shell);
				
				final T propertiesSection = createPropertiesSection();
				propertiesSection.createControls(shell, tabbedPage);
				propertiesSection.setInput(null, createSelection());
				propertiesSection.refresh();
				shell.update();
				
				swtBotShell = new SWTBotShell(shell);
				bot = swtBotShell.bot();
				
			}
		});
	
	}


	@After
	public void tearDown() {
		swtBotShell.close();
	}

	private static ITabbedPropertySheetPageContributor createContributor() {
		return new ITabbedPropertySheetPageContributor() {
			@Override
			public String getContributorId() {
				return "test";
			}
		};
	}

	private static IPageSite createPageSite() {
		IPageSite site = Mockito.mock(IPageSite.class);
		Mockito.stub(site.getWorkbenchWindow()).toReturn(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		
		return site;
	}
	
	
	private IStructuredSelection createSelection() {
		final IStructuredSelection selection = Mockito.mock(IStructuredSelection.class);
		Object service = createInput();
		Mockito.when(selection.getFirstElement()).thenReturn(service);
		
		return selection;
	}

}