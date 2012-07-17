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
package org.eclipse.servicesregistry.ui.test.prefpage.pageobj;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;
import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.servicesregistry.testutils.Assertions;
import org.eclipse.servicesregistry.testutils.ConditionCheckException;
import org.eclipse.servicesregistry.testutils.IWaitCondition;
import org.eclipse.servicesregistry.ui.internal.prefpage.SrPreferencePage;
import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.ui.PlatformUI;

public class SrPrefPagePO
{
	public static SrPrefPagePO create(final SrPreferencePage prefPage)
	{
		final TesterDialog testDlg = new TesterDialog(activeShell(), prefPage);
		return new SrPrefPagePO(testDlg);
	}

	private static Shell activeShell()
	{
		final Display display = PlatformUI.getWorkbench().getDisplay();
		final Shell[] result = new Shell[1];
		display.syncExec(new Runnable()
		{
			@Override
			public void run()
			{
				result[0] = display.getActiveShell();
			}
		});
		return result[0];
	}

	private final TesterDialog testDlg;
	private SWTBotShell testShell;

	public SrPrefPagePO(final TesterDialog testDlg)
	{
		this.testDlg = testDlg;
	}

	public void open()
	{
		syncExec(new VoidResult()
		{
			@Override
			public void run()
			{
				testDlg.setBlockOnOpen(false);
				testDlg.open();
				Assertions.waitAssert(new IWaitCondition() {
					@Override
					public boolean checkCondition() throws ConditionCheckException {
						return testDlg.getShell() != null;
					}
				}, "Dialog did not open");
				testShell = new SWTBotShell(testDlg.getShell());
			}
		});
	}

//	private void syncExecRunnable()
//	{
//		syncExec(new VoidResult()
//		{
//			@Override
//			public void run()
//			{
//			}
//		});
//	}

	public void dispose()
	{
		if (testShell.isOpen())
		{
			testShell.close();
		}
	}

	public List<String> getDisplayedConfigNames()
	{
		return getDisplayedObjectsInColumn(0);
	}

	public List<String> getDisplayedConfigHosts()
	{
		return getDisplayedObjectsInColumn(1);
	}

	public List<String> getDisplayedConfigPorts()
	{
		return getDisplayedObjectsInColumn(2);
	}

	private List<String> getDisplayedObjectsInColumn(int index)
	{
		final List<String> configNames = new ArrayList<String>();
		final SWTBotTable cfgTable = getConfigsTable();
		for (int i = 0; i < cfgTable.rowCount(); i++)
		{
			configNames.add(cfgTable.cell(i, index));
		}

		return configNames;
	}

	private SWTBotTable getConfigsTable()
	{
		return testShell.bot().table();
	}

	private SWTBotButton getAddConfigButton()
	{
		return testShell.bot().button(SrUiMessages.SR_PREFERENCE_ADD_CONFIG_BUTTON);
	}

	private SWTBotButton getEditConfigButton()
	{
		return testShell.bot().button(SrUiMessages.SR_PREFERENCE_EDIT_CONFIG_BUTTON);
	}

	private SWTBotButton getDeleteConfigButton()
	{
		return testShell.bot().button(SrUiMessages.SR_PREFERENCE_DELETE_CONFIG_BUTTON);
	}

	public boolean canEdit()
	{
		return getEditConfigButton().isEnabled();
	}

	public boolean canDelete()
	{
		return getDeleteConfigButton().isEnabled();
	}

	public void selectConfiguration(final String configName)
	{
		getTableItemByConfigName(configName).select();
	}

	private SWTBotTableItem getTableItemByConfigName(final String configName)
	{
		final SWTBotTable cfgTable = getConfigsTable();
		for (int i = 0; i < cfgTable.rowCount(); i++)
		{
			if (cfgTable.cell(i, 0).equals(configName))
			{
				return cfgTable.getTableItem(i);
			}
		}
		return null;
	}

	public void addNewConfiguration()
	{
		getAddConfigButton().click();
	}

	public void editSelectedConfiguration()
	{
		getEditConfigButton().click();
	}

	public DeleteConfigConfirmDlgPO deleteSelectedConfig()
	{
		getDeleteConfigButton().click();
		return DeleteConfigConfirmDlgPO.find();
	}
}
