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
package org.eclipse.servicesregistry.ui.internal.config;



import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.platform.discovery.util.internal.property.Access;
import org.eclipse.platform.discovery.util.internal.property.IPropertyAttributeListener;
import org.eclipse.platform.discovery.util.internal.property.Property;
import org.eclipse.platform.discovery.util.internal.property.PropertyAttributeChangedEvent;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.ICreatedObjectValidator;
import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystem;
import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

/**
 * Dialog for visualizing a Services Registry configuration
 * 
 * @author Danail Branekov
 * 
 */
public abstract class AbstractSrConfigDialog extends StatusDialog  implements IConfigDialog
{
	private Button useHttpCheckbox;
	private Text connectionNameText;
	private Text hostText;
	private Text portText;

	private Button storeCredentialsCheckbox;
	private Label userNameLabel;
	private Text  userNameText;
	private Label passwordLabel;
	private Text  passwordText;
	
	public static final String CONFIG_HELP_CONTEXT_ID = "org.eclipse.servicesregistry.config"; //$NON-NLS-1$

	/**
	 * Property for connection name
	 */
	protected final Property<String> connectionName;

	/**
	 * Property for server host name
	 */
	protected final Property<String> serverHost;

	/**
	 * Property for server port
	 */
	protected final Property<String> serverPort;
	
	/**
	 * Property for whether use https
	 */
	protected final Property<Boolean> useHttps;
	
	/**
	 * Property for whether user credentials are stored in the preferences store
	 */
	protected final Property<Boolean> storeCredentials;

	/**
	 * Property for user name
	 */
	protected final Property<String> userName;

	/**
	 * Property for user password
	 */
	protected final Property<String> password;
	
	private final ICreatedObjectValidator<IServicesRegistrySystem> createConfigValidator;
	private final Set<IServicesRegistrySystem> currentlyExistingConfigs;

	/**
	 * Constructor
	 * @param parent
	 * @param createConfigValidator validator which checks whether the configuration managed by this dialog is OK
	 * @param currentlyExistingConfigs a set of configurations which already exist
	 */
	public AbstractSrConfigDialog(final Shell parent, final ICreatedObjectValidator<IServicesRegistrySystem> createConfigValidator, final Set<IServicesRegistrySystem> currentlyExistingConfigs)
	{
		super(parent);
		this.createConfigValidator = createConfigValidator;
		this.currentlyExistingConfigs = Collections.unmodifiableSet(currentlyExistingConfigs);
		connectionName = new Property<String>();
		connectionName.set(""); //$NON-NLS-1$
		serverHost = new Property<String>();
		serverHost.set(""); //$NON-NLS-1$
		serverPort = new Property<String>();
		serverPort.set(""); //$NON-NLS-1$
		useHttps = new Property<Boolean>();
		useHttps.set(false);
		
		storeCredentials = new Property<Boolean>();
		storeCredentials.set(false);
		userName = new Property<String>();
		userName.set(""); //$NON-NLS-1$
		password = new Property<String>();
		password.set(""); //$NON-NLS-1$

		setReturnCode(Window.CANCEL);
	}

	/**
	 * Extenders should implement this method in order to initialize the initial content of the UI controls
	 */
	protected abstract void initializeProperties();

	@Override
	protected Control createDialogArea(Composite parent)
	{
		final Composite composite = new Composite((Composite) super.createDialogArea(parent), SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);

		gd.widthHint = getLongestLabelSize("Passwords are stored in your computer which is difficult, but not impossible, for an intruder to read."); //$NON-NLS-1$
		composite.setLayoutData(gd);
		
		final Group connectionGroup = new Group(composite, SWT.NONE);
		connectionGroup.setText(SrUiMessages.CFG_DLG_SETTINGS_GROUP_TEXT);
		connectionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		int connectionGroupSpan = 3;
		connectionGroup.setLayout(new GridLayout(connectionGroupSpan, true));
		useHttpCheckbox = new Button(connectionGroup, SWT.CHECK);
		useHttpCheckbox.setText(SrUiMessages.CFG_DLG_USE_HTTPS_BUTTON);
		useHttpCheckbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false, connectionGroupSpan, 1));
		connectionNameText = createTextWithLabel(connectionGroup, SrUiMessages.CFG_DLG_CONNECTION_NAME, SWT.BORDER | SWT.SEARCH);
		connectionNameText.setMessage(MessageFormat.format(SrUiMessages.CFG_DLG_USE_DEFAULT_HINT_CONN, IServicesRegistrySystem.DISPLAY_NAME_HOST_PORT_SEPARATOR));
		hostText = createTextWithLabel(connectionGroup, SrUiMessages.CFG_DLG_HOST, SWT.BORDER);
		hostText.setFocus();
		portText = createTextWithLabel(connectionGroup, SrUiMessages.CFG_DLG_PORT, SWT.BORDER);
		
		
		final Group credentialsGroup = new Group(composite, SWT.NONE);
		credentialsGroup.setText(SrUiMessages.CFG_DLG_USER_CREDENTIALS_GROUP_TEXT);
		credentialsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		credentialsGroup.setLayout(new GridLayout(connectionGroupSpan, true));
		
		storeCredentialsCheckbox = new Button(credentialsGroup, SWT.CHECK);
		storeCredentialsCheckbox.setText(SrUiMessages.CFG_DLG_STORE_CREDENTIALS);
		storeCredentialsCheckbox.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true, connectionGroupSpan, 1));
		
		
		userNameLabel = new Label(credentialsGroup,SWT.NONE);
		userNameLabel.setText(SrUiMessages.CFG_DLG_USER_NAME);
		userNameText = new Text(credentialsGroup, SWT.BORDER);
		setLayoutDataToTextAndLabel(userNameText, userNameLabel);
		
		passwordLabel = new Label(credentialsGroup,SWT.NONE);
		passwordLabel.setText(SrUiMessages.CFG_DLG_PASSWORD);
		passwordText = new Text(credentialsGroup, SWT.BORDER | SWT.PASSWORD);
		setLayoutDataToTextAndLabel(passwordText, passwordLabel);
		
	
		installListeners();
		setDefaultValues();
		initializeProperties();
		installHelpContext();

		return composite;
	}
	
	private Text createTextWithLabel(final Composite parent, final String labelText, final int textStyle)
	{
		final Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		final Text text = new Text(parent, textStyle);
		setLayoutDataToTextAndLabel(text, label);

		return text;
	}
	
	
	private void installHelpContext()
	{
		this.wbHelpSystem().setHelp(this.getShell(), getHelpContextId());
	}
	
	private int getLongestLabelSize(String label)
	{
		int result = 0;
		
		final GC gc = new GC(PlatformUI.getWorkbench().getDisplay());
		try
		{
			final int extent = gc.textExtent(label).x;
			result = extent > result ? extent : result;
		}
		finally
		{
			gc.dispose();
		}
		
		return result;
	}

	private String getDefaultConnectionName()
	{
		return serverHost.get()+IServicesRegistrySystem.DISPLAY_NAME_HOST_PORT_SEPARATOR +serverPort.get();
	}
	
	private void installListeners()
	{
		connectionNameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				connectionName.set(connectionNameText.getText());
			}
		});
		hostText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				if(connectionName.get().equals(getDefaultConnectionName()))
					connectionName.set(hostText.getText()+':'+serverPort.get());
				serverHost.set(hostText.getText());
			}
		});
		portText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				if(connectionName.get().equals(getDefaultConnectionName()))
					connectionName.set(serverHost.get()+':'+portText.getText());
				serverPort.set(portText.getText());
			}
		});
				
		connectionName.registerValueListener(new IPropertyAttributeListener<String>()
		{
			public void attributeChanged(PropertyAttributeChangedEvent<String> event)
			{
				setText(event.getNewAttribute(), connectionNameText);
			}
		}, false);

		serverHost.registerValueListener(new IPropertyAttributeListener<String>()
		{

			public void attributeChanged(PropertyAttributeChangedEvent<String> event)
			{
				setText(event.getNewAttribute(), hostText);
			}
		}, false);

		serverPort.registerValueListener(new IPropertyAttributeListener<String>()
		{

			public void attributeChanged(PropertyAttributeChangedEvent<String> event)
			{
				setText(event.getNewAttribute(), portText);
			}
		}, false);
		
	
		installUseHttpsListenrers();
		installStoreCredentialsListeners();
		installUserNameListeners();
		installUserPasswordListeners();

		installStatusListeners();
	}

	private void installUseHttpsListenrers() 
	{
		useHttps.registerValueListener(new IPropertyAttributeListener<Boolean>(){
			@Override
			public void attributeChanged(
					PropertyAttributeChangedEvent<Boolean> event) {
				useHttpCheckbox.setSelection(event.getNewAttribute());
			}
			
		}, false);
		
		useHttpCheckbox.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				useHttps.set(useHttpCheckbox.getSelection());
			}
		});
	}

	private void installStoreCredentialsListeners()
	{
		storeCredentials.registerValueListener(new IPropertyAttributeListener<Boolean>()
		{
			public void attributeChanged(PropertyAttributeChangedEvent<Boolean> event)
			{
				if (event.getNewAttribute())
				{
					userName.setAccess(Access.READ_WRITE);
					password.setAccess(Access.READ_WRITE);
				} else
				{
					userName.set(""); //$NON-NLS-1$
					userName.setAccess(Access.READ_ONLY);
					password.set(""); //$NON-NLS-1$
					password.setAccess(Access.READ_ONLY);
				}
				storeCredentialsCheckbox.setSelection(event.getNewAttribute());
			}
		}, false);
		storeCredentialsCheckbox.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				storeCredentials.set(storeCredentialsCheckbox.getSelection());
			}
		});
	}

	private void installUserNameListeners()
	{
		userName.registerAccessListener(new IPropertyAttributeListener<Access>()
		{
			public void attributeChanged(PropertyAttributeChangedEvent<Access> event)
			{
				final boolean enabled = event.getNewAttribute() == Access.READ_WRITE;
     			userNameText.setEditable(enabled);
				userNameLabel.setEnabled(enabled);
				userNameText.setEnabled(enabled);
			}
		}, false);

		userName.registerValueListener(new IPropertyAttributeListener<String>()
		{
			public void attributeChanged(PropertyAttributeChangedEvent<String> event)
			{
				setText(event.getNewAttribute(), userNameText);
			}
		}, false);

		userNameText.addModifyListener(new ModifyListener()
		{

			public void modifyText(ModifyEvent e)
			{
				userName.set(userNameText.getText());
			}
		});
	}

	private void installUserPasswordListeners()
	{
		password.registerAccessListener(new IPropertyAttributeListener<Access>()
		{
			public void attributeChanged(PropertyAttributeChangedEvent<Access> event)
			{
				final boolean enabled = event.getNewAttribute() == Access.READ_WRITE;
				passwordText.setEditable(enabled);
				passwordLabel.setEnabled(enabled);
				passwordText.setEnabled(enabled);
			}
		}, false);

		password.registerValueListener(new IPropertyAttributeListener<String>()
		{

			public void attributeChanged(PropertyAttributeChangedEvent<String> event)
			{
				setText(event.getNewAttribute(), passwordText);
			}
		}, false);

		passwordText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				password.set(passwordText.getText());
			}
		});
	}

	private void installStatusListeners()
	{
		final IPropertyAttributeListener<String> listener = new IPropertyAttributeListener<String>()
		{
			public void attributeChanged(PropertyAttributeChangedEvent<String> event)
			{
				updateStatus(getDialogStatus());
			}
		};
		
		final IPropertyAttributeListener<Boolean> booleanListener = new IPropertyAttributeListener<Boolean>()
		{
			public void attributeChanged(PropertyAttributeChangedEvent<Boolean> event)
			{
				updateStatus(getDialogStatus());
			}
		};
		
		connectionName.registerValueListener(listener, true);
		serverHost.registerValueListener(listener, true);
		serverPort.registerValueListener(listener, true);
		
		useHttps.registerValueListener(booleanListener, true);
		
		
		userName.registerValueListener(listener, true);
		password.registerValueListener(listener, true);
		
		storeCredentials.registerValueListener(booleanListener, true);
	}

	
	
	private void setLayoutDataToTextAndLabel(Text text, Label label)
	{
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false, 1, 1));
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
	}

	private void setDefaultValues()
	{
		storeCredentialsCheckbox.setSelection(false);
		userName.setAccess(Access.READ_ONLY);
		password.setAccess(Access.READ_ONLY);
		//storeCredentialsCheckbox.setEnabled(false);
	}

	protected IStatus getDialogStatus()
	{
		return createConfigValidator.validateCreatedObject(createConfigFromDialogData(portForValidation()), existingConfigs());
	}
	
	private int portForValidation()
	{
		// return illegal port number in case the port is not specified or contains illegal symbols
		if (serverPort.get().length() == 0)
		{
			return -1;
		}
		try
		{
			return Integer.parseInt(serverPort.get());
		} catch (NumberFormatException e)
		{
			return -1;
		}
	}
	
	protected Set<IServicesRegistrySystem> existingConfigs()
	{
		return this.currentlyExistingConfigs;
	}

	/**
	 * Gets a {@link IServicesRegistrySystem} instance which contains the data entered by the user
	 * 
	 * @return
	 */
	public IServicesRegistrySystem getConfig()
	{
		if (this.getReturnCode() != Window.OK)
		{
			throw new IllegalStateException("Configuration could not be created since the dialog has been cancelled or not opened"); //$NON-NLS-1$
		}

		return createConfigFromDialogData(Integer.parseInt(serverPort.get()));
	}

	private IServicesRegistrySystem createConfigFromDialogData(final int port)
	{
		final String connName = connectionName.get().isEmpty() ? getDefaultConnectionName() : connectionName.get();
		return new ServicesRegistrySystem(connName, serverHost.get(), port, useHttps.get(), userName.get(), storeCredentials.get(), password.get());
	}

	private void setText(final String textToSet, final Text text)
	{
		if (textToSet.equals(text.getText()))
		{
			return;
		}

		text.setText(textToSet);
	}
	
	/**
	 * Retrieves the help system associated with this dialog
	 * @return
	 */
	protected IWorkbenchHelpSystem wbHelpSystem()
	{
		return PlatformUI.getWorkbench().getHelpSystem();
	}
	
	/**
	 * Retrieves the help context ID for this dialog
	 * @return
	 */
	protected String getHelpContextId()
	{
		return CONFIG_HELP_CONTEXT_ID;
	}
	
	
}
