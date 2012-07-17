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
package org.eclipse.servicesregistry.search.ui.internal.result.actions;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.platform.discovery.util.internal.property.Property;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.IEndpointActionConfiguration;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.config.AbstractActionConfig;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.servicesregistry.wsdl.endpoint.IEndpointWsdlData;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Action which gets a WSDL. The action will trigger a dialog via which the user can select whether to open the WSDL in editor and whether to save it
 * 
 * @author Danail Branekov
 * 
 */
public class GetEndpointWsdlAction extends Action
{
	private final IDiscoveryEnvironment environment;
	private final IContributedAction getWsdlAction;
	private final IServiceEndpoint serviceEndpoint;

	private final Property<Boolean> openWsdlInEditor;
	private final Property<File> saveWsdlDirectory;

	public GetEndpointWsdlAction(final IServiceEndpoint serviceEndpoint, final IContributedAction getWsdlAction, final IDiscoveryEnvironment environment)
	{
		this.environment = environment;
		this.getWsdlAction = getWsdlAction;
		this.serviceEndpoint = serviceEndpoint;

		openWsdlInEditor = new Property<Boolean>();
		saveWsdlDirectory = new Property<File>();
	}
	
	@Override
	public String getId()
	{
		return "org.eclipse.servicesregistry.search.ui.internal.result.actions.create.endpoint.wsdl";//$NON-NLS-1$
	}

	@Override
	public String getText()
	{
		return SearchUIMessages.SrSearchViewCustomization_CreateEndpointWsdlAction;
	}
	

	@Override
	public void run()
	{
		final GetWsdlWizardDialog getWsdlDialog = new GetWsdlWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		if (getWsdlDialog.open() == Dialog.OK)
		{
			final Set<Object> sel = new HashSet<Object>();
			sel.add(createActionConfig());
			getWsdlAction.perform(environment.operationRunner(), sel);
		}
	}

	protected IExistingFilesProcessor getExistingFilesProcessor()
	{
		if (saveWsdlDirectory.get() == null)
		{
			// The WSDL will not be saved in the workspace, i.e. will be downloaded to a temporary location. Therefore no edit validation is required 
			return new StubProcessor();
		}
		return new InteractiveExistingFilesProcessor();
	}

	private IEndpointActionConfiguration createActionConfig()
	{
		return new EndpointWsdlActionConfig(this.environment);
	}
	
	private class GetWsdlWizardDialog extends Dialog
	{
		private static final int WIDTH_HINT = 500;
		private static final int HEIGHT_HINT = 300;
		private Button openWsdlInEditorButton;
		private Button saveWsdlButton;
		private TreeViewer workspaceViewer;

		protected GetWsdlWizardDialog(Shell parentShell)
		{
			super(parentShell);
			openWsdlInEditor.set(true);
			saveWsdlDirectory.set(null);
		}

		@Override
		protected void configureShell(Shell newShell)
		{
			super.configureShell(newShell);
			newShell.setText(GetEndpointWsdlAction.this.getText());
		}

		@Override
		protected Control createDialogArea(Composite parent)
		{
			parent.setLayout(new GridLayout(1, false));
			openWsdlInEditorButton = new Button(parent, SWT.CHECK);
			openWsdlInEditorButton.setText(SearchUIMessages.GetWsdlAction_OpenWsdlInEditorCheckBox);
			openWsdlInEditorButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));
			openWsdlInEditorButton.setSelection(openWsdlInEditor.get());
			openWsdlInEditorButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					openWsdlInEditor.set(openWsdlInEditorButton.getSelection());
					setOkButtonEnabledState();
				}
			});

			saveWsdlButton = new Button(parent, SWT.CHECK);
			saveWsdlButton.setText(SearchUIMessages.GetWsdlAction_SaveWsdlCheckBox);
			saveWsdlButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));

			workspaceViewer = (new WorkspaceResourceBrowser(this.getShell())).new WorkspaceBrowserTreeViewerFactory().createWorkspaceTreeViewer(parent);
			final GridData workspaceViewerGD = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			workspaceViewerGD.widthHint = WIDTH_HINT;
			workspaceViewerGD.heightHint = HEIGHT_HINT;
			workspaceViewer.getTree().setLayoutData(workspaceViewerGD);
			workspaceViewer.getTree().setEnabled(saveWsdlDirectory.get() != null);
			workspaceViewer.getTree().addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					final IContainer selectedDir = (IContainer) ((IStructuredSelection) workspaceViewer.getSelection()).getFirstElement();
					saveWsdlDirectory.set(new File(selectedDir.getLocation().toOSString()));
					setOkButtonEnabledState();
				}
			});

			saveWsdlButton.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					workspaceViewer.getTree().setEnabled(saveWsdlButton.getSelection());
					saveWsdlDirectory.set(null);
					workspaceViewer.setSelection(new StructuredSelection());

					setOkButtonEnabledState();
				}
			});

			workspaceViewer.setInput(ResourcesPlugin.getWorkspace().getRoot());

			return parent;
		}

		private void setOkButtonEnabledState()
		{
			boolean enabled = openWsdlInEditorButton.getSelection() || saveWsdlButton.getSelection();
			enabled = enabled && (!saveWsdlButton.getSelection() || saveWsdlDirectory.get() != null);
			getButton(IDialogConstants.OK_ID).setEnabled(enabled);
		}
	}
	
	private class EndpointWsdlActionConfig extends AbstractActionConfig implements IEndpointActionConfiguration
	{

		public EndpointWsdlActionConfig(final IDiscoveryEnvironment env)
		{
			super(env);
		}

		@Override
		public IExistingFilesProcessor existingFilesProcessor()
		{
			return GetEndpointWsdlAction.this.getExistingFilesProcessor();
		}

		@Override
		public File saveDestination()
		{
			return saveWsdlDirectory.get() == null ? fileUtils().systemTempDir() : saveWsdlDirectory.get();
		}

		@Override
		public void wsdlDownloaded(final IWsdlWtpDescriptorContainer wsdlContainer, final ILongOperationRunner operationRunner)
		{
			if (saveWsdlDirectory.get() != null)
			{
				scheduleWsdlDirectoryRefresh(wsdlContainer);
			}
			else
			{
				this.setWsdlFilesDeleteOnExit(wsdlContainer);
			}

			if (openWsdlInEditor.get())
			{
				openWsdlInEditor(wsdlContainer);
			}
		}

		@Override
		public String wsdlUrl()
		{
			return serviceEndpoint.getBindingWsdlUrl();
		}

		@Override
		public String rootWsdlFileName()
		{
			return serviceEndpoint.getEndpointName();
		}

		@Override
		public IEndpointWsdlData endpointData()
		{
			return new IEndpointWsdlData()
			{
				
				@Override
				public QName serviceQName()
				{
					return serviceEndpoint.getServiceQName();
				}
				
				@Override
				public QName porttypeQName()
				{
					return serviceEndpoint.getServiceDefinition().getPorttypeQName();
				}
				
				@Override
				public String endpointName()
				{
					return serviceEndpoint.getEndpointName();
				}
				
				@Override
				public String endpointAddress()
				{
					return serviceEndpoint.getEndpointAddress();
				}
				
				@Override
				public URL bindingWsdlUrl()
				{
					try
					{
						return new URL(serviceEndpoint.getBindingWsdlUrl());
					}
					catch (MalformedURLException e)
					{
						errorHandler().handleException(e);
						return null;
					}
				}
				
				@Override
				public QName bindingQName()
				{
					return serviceEndpoint.getBindingQName();
				}
			};
		}
	}
}
