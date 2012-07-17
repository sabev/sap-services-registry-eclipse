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
package org.eclipse.servicesregistry.ui.internal.prefpage;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.ConfigCreationCanceledException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigLoadException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigStoreException;
import org.eclipse.servicesregistry.core.config.persistency.IConfigStorage;
import org.eclipse.servicesregistry.core.config.persistency.IPreferencesController;
import org.eclipse.servicesregistry.core.config.persistency.IUserCredentialsHandler;
import org.eclipse.servicesregistry.core.config.persistency.SrConfigStorageFactory;
import org.eclipse.servicesregistry.core.internal.logging.ILogger;
import org.eclipse.servicesregistry.core.internal.logging.Logger;
import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * Preference page for configuring connections to the services registry
 * 
 * @author Danail Branekov
 */
public class SrPreferencePage extends PreferencePage implements IWorkbenchPreferencePage, IShellAware
{
	public final static int YES_BUTTON = 0;
	private static final int CONNECTION_DISPLAY_NAME_COLUMN_INDEX = 0;
	private ILogger logger = Logger.instance();
	private final IConfigStorage configStorage;

	private IPreferencesController controller;

	private Button addConfigButton;
	private Button editConfigButton;
	private Button deleteConfigButton;
	private TableViewer configsTableViewer;

	public SrPreferencePage()
	{
		super();
		noDefaultAndApplyButton();
		configStorage = SrConfigStorageFactory.getDefault();		
	}
	
	@Override
	protected Control createContents(Composite parent)
	{
		final Composite preferencesComposite = new Composite(parent, SWT.NONE);
		preferencesComposite.setLayout(new GridLayout(5, true));

		createPreferencesTable(preferencesComposite);
		createPreferencesButtons(preferencesComposite);

		return preferencesComposite;
	}

	private void createPreferencesButtons(final Composite parent)
	{
		addConfigButton = new Button(parent, SWT.PUSH);
		addConfigButton.setText(SrUiMessages.SR_PREFERENCE_ADD_CONFIG_BUTTON);
		addConfigButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		addConfigButton.addSelectionListener(new AddConfigSelectionListener());

		editConfigButton = new Button(parent, SWT.PUSH);
		editConfigButton.setText(SrUiMessages.SR_PREFERENCE_EDIT_CONFIG_BUTTON);
		editConfigButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		editConfigButton.addSelectionListener(new EditConfigSelectionListener());
		editConfigButton.setEnabled(false);

		deleteConfigButton = new Button(parent, SWT.PUSH);
		deleteConfigButton.setText(SrUiMessages.SR_PREFERENCE_DELETE_CONFIG_BUTTON);
		deleteConfigButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		deleteConfigButton.addSelectionListener(new DeleteConfigSelectionListener());
		deleteConfigButton.setEnabled(false);
	}

	private void createPreferencesTable(final Composite parent)
	{
		configsTableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.RESIZE | SWT.SINGLE);
		configsTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 3));

		final Table prefTable = configsTableViewer.getTable();
		prefTable.setLinesVisible(true);
		prefTable.setHeaderVisible(true);
		final TableLayout tableLayout = new TableLayout();
		prefTable.setLayout(tableLayout);

		createDisplayNameColumn(configsTableViewer, tableLayout);

		configsTableViewer.setContentProvider(new SrConfigContentProvider(this.controller));
		configsTableViewer.setLabelProvider(new SrConfigLabelProvider());
		configsTableViewer.setInput(this.controller);
		configsTableViewer.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if(!event.getSelection().isEmpty())
				{
					deleteConfigButton.setEnabled(true);
					editConfigButton.setEnabled(true);
				}
				else
				{
					deleteConfigButton.setEnabled(false);
					editConfigButton.setEnabled(false);
				}
				
			}});;
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if (visible)
		{
			configsTableViewer.refresh();
		}
	}

	private void createDisplayNameColumn(final TableViewer parentViewer, final TableLayout parentTableLayout)
	{
		final TableViewerColumn displayNameTableColumn = new TableViewerColumn(parentViewer, SWT.LEFT, CONNECTION_DISPLAY_NAME_COLUMN_INDEX);
		displayNameTableColumn.getColumn().setText(SrUiMessages.SR_PREFERENCE_TABLE_DISPLAY_NAME_COLUMN_TITLE);
		final TableViewerColumn displayHostTableColumn = new TableViewerColumn(parentViewer, SWT.LEFT);
		displayHostTableColumn.getColumn().setText(SrUiMessages.SR_PREFERENCE_TABLE_HOST_COLUMN_TITLE);
		final TableViewerColumn displayPortTableColumn = new TableViewerColumn(parentViewer, SWT.LEFT);
		displayPortTableColumn.getColumn().setText(SrUiMessages.SR_PREFERENCE_TABLE_PORT_COLUMN_TITLE);
		
		final ColumnWeightData displayNameColumnData = new ColumnWeightData(30);
		final ColumnWeightData displayHostColumnData = new ColumnWeightData(30);
		final ColumnWeightData displayPortColumnData = new ColumnWeightData(30);
		
		parentTableLayout.addColumnData(displayNameColumnData);
		parentTableLayout.addColumnData(displayHostColumnData);
		parentTableLayout.addColumnData(displayPortColumnData);
	}

	public void init(IWorkbench workbench)
	{
		this.controller = createPreferencesController();
	}

	protected IPreferencesController createPreferencesController()
	{
		final IUserCredentialsHandler existingCredentialsHandler = createExistingUserCredentialsHandler();
		try
		{
			return new SrPreferencesController(this, configStorage, existingCredentialsHandler);
		}
		catch (ConfigLoadException e)
		{
			logger.logError(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}


	protected IUserCredentialsHandler createExistingUserCredentialsHandler()
  	{
  		return new IUserCredentialsHandler(){
  
  			@Override
  			public Buttons handleExistingCredentials() {
  				switch (showUserCredentialsExistDialog())
  				{ 
  					case 0: return Buttons.REUSE;
  					case 1: return Buttons.OVERRIDE;
 					default:
  						return null;
  				}
  			}

  		};
  	}
	
  	protected int showUserCredentialsExistDialog()
  	{
  		return new MessageDialog(getShell(),
				SrUiMessages.SrPreferencePage_IdenticalCredentialsDialogTitle,
  				null, SrUiMessages.SrPreferencePage_IdenticalCredentialsDialogMessage,
  				MessageDialog.QUESTION,
  				new String[]{SrUiMessages.SrPreferencePage_ReuseButton,SrUiMessages.SrPreferencePage_OverrideButton},
  				SWT.ICON_QUESTION).open();
  	}
	  	
	
	@Override
	public boolean performOk()
	{
		super.performOk();
		try
		{
			this.controller.storeConfigurations();
			return true;
		}
		catch (ConfigStoreException e)
		{
			logger.logError(e.getMessage(), e);
		}
		catch (ConfigLoadException e)
		{
			logger.logError(e.getMessage(), e);
		}

		return false;
	}

	private IServicesRegistrySystem getSelectedConfig()
	{
		if (configsTableViewer.getSelection().isEmpty())
		{
			return null;
		}

		return (IServicesRegistrySystem) ((IStructuredSelection) configsTableViewer.getSelection()).getFirstElement();
	}

	private class AddConfigSelectionListener extends SelectionAdapter implements SelectionListener
	{
		public void widgetSelected(SelectionEvent e)
		{
			try
			{
				controller.createNewConfiguration();
			} catch (ConfigCreationCanceledException e1)
			{
				return;
			}
			configsTableViewer.refresh();
		}
	}

	private class EditConfigSelectionListener extends SelectionAdapter implements SelectionListener
	{
		public void widgetSelected(SelectionEvent e)
		{
			final IServicesRegistrySystem config = getSelectedConfig();
			if (config == null)
			{
				return;
			}

			controller.editConfiguration(config);
			configsTableViewer.refresh();
		}
	}

	private class DeleteConfigSelectionListener extends SelectionAdapter implements SelectionListener
	{
		public void widgetSelected(SelectionEvent e)
		{
			final IServicesRegistrySystem config = getSelectedConfig();
			if (config == null || showConfirmDialog(config) != YES_BUTTON)
			{
				return;
			}
						
			controller.deleteConfiguration(config);
			configsTableViewer.refresh();
		}
	}
	
	protected int showConfirmDialog(final IServicesRegistrySystem config)
	{
		String description;
		if (config.areCredentialsStored())
			description = SrUiMessages.SrPreferencesController_DeleteConfigWithAuthDetails;
		else
			description = SrUiMessages.SrPreferencesController_DeleteConfigWithoutAuthDetails;
		return new MessageDialog(getShell(),
								SrUiMessages.SrPreferencesController_DeleteConfigConfirmTitle,
								null, description,
								MessageDialog.QUESTION,
								new String[]{SrUiMessages.SrPreferencesController_YesButton,
								SrUiMessages.SrPreferencesController_NoButton},
								SWT.ICON_QUESTION).open();
				
	}
}
