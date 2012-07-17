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
package org.eclipse.servicesregistry.search.ui.test.classifications.pageobjects;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;
import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.servicesregistry.search.core.destinations.IServicesRegistryDestination;
import org.eclipse.servicesregistry.search.ui.internal.classifications.ClassificationTreeContribution;
import org.eclipse.servicesregistry.testutils.Assertions;
import org.eclipse.servicesregistry.testutils.ConditionCheckException;
import org.eclipse.servicesregistry.testutils.IWaitCondition;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.mockito.Mockito;

public class ClassificationsTreePageObject
{
	private SWTBotShell shell;
	private FormToolkit toolkit;

	public ClassificationsTreePageObject(final ClassificationTreeContribution contribution, final IDiscoveryEnvironment environment)
	{
		toolkit = createFormToolkit();
		
		createUi(contribution, environment);
		expandAllItems();
	}

	public void dispose()
	{
		shell.close();
		toolkit.dispose();
	}
	
	public boolean isClassificationValueVisible(String classificationSystemName, String valueNodeName)
	{
		return findClassificationValue(classificationSystemName, valueNodeName) != null;
	}

	public void toggleValue(final String classificationSystemName, final String valueNodeName)
	{
		final SWTBotTreeItem valueItem = findClassificationValue(classificationSystemName, valueNodeName);
		valueItem.select();
		valueItem.pressShortcut(0, 0, ' '); // press space
		syncExec(new VoidResult()
		{
			@Override
			public void run()
			{
			}
		});
	}
	
	public boolean isClassificationSystemVisible(final String classificationSystemName)
	{
		return findClassificationSystemItem(classificationSystemName) != null;
	}
	
	private SWTBotTreeItem findClassificationSystemItem(final String systemName)
	{
		for(SWTBotTreeItem ti : classificationsTree().getAllItems())
		{
			if(ti.getText().equals(systemName))
			{
				return ti;
			}
		}
		
		return null;
	}

	private SWTBotTreeItem findClassificationValue(final String classificationSystemName, final String valueNodeName)
	{
		final SWTBotTreeItem systemItem = findClassificationSystemItem(classificationSystemName);
		for(SWTBotTreeItem ti : systemItem.getItems())
		{
			if(ti.getText().equals(valueNodeName))
			{
				return ti;
			}
		}
		return null;
	}
	
	private SWTBotTree classificationsTree()
	{
		return shell.bot().tree();
	}
	
	private void expandAllItems()
	{
		for(SWTBotTreeItem ti : classificationsTree().getAllItems())
		{
			ti.expand();
		}
	}

	private void createUi(final ClassificationTreeContribution contribution, final IDiscoveryEnvironment environment)
	{
		asyncExec(new VoidResult()
		{
			@Override
			public void run()
			{
				final Dialog dlg = new Dialog(PlatformUI.getWorkbench().getDisplay().getActiveShell())
				{
					@Override
					protected Control createContents(Composite parent)
					{
						parent.setLayout(new FillLayout(SWT.VERTICAL));
						final Composite hostComposite = new Composite(parent, SWT.NONE);
						hostComposite.setLayout(new FormLayout());
						contribution.createUi(hostComposite, Mockito.mock(IServicesRegistryDestination.class), toolkit, environment, null);
						contribution.handleVisibilityChange(true);
						return parent;
					}
				};

				dlg.setBlockOnOpen(false);
				dlg.open();
				shell = new SWTBotShell(dlg.getShell());
			}
		});

		Assertions.waitAssert(new IWaitCondition()
		{

			@Override
			public boolean checkCondition() throws ConditionCheckException
			{
				return shell != null;
			}
		}, "Test shell could not be created");
	}
	
	private FormToolkit createFormToolkit()
	{
		return syncExec(new Result<FormToolkit>()
		{
			@Override
			public FormToolkit run()
			{
				return new FormToolkit(PlatformUI.getWorkbench().getDisplay());
			}
		});
	}
}
