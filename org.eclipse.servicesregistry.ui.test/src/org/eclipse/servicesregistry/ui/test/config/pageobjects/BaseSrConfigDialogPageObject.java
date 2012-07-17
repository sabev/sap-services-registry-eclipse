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
package org.eclipse.servicesregistry.ui.test.config.pageobjects;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystemValidator;
import org.eclipse.servicesregistry.testutils.Assertions;
import org.eclipse.servicesregistry.testutils.ConditionCheckException;
import org.eclipse.servicesregistry.testutils.IWaitCondition;
import org.eclipse.servicesregistry.ui.internal.config.AbstractSrConfigDialog;
import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public abstract class BaseSrConfigDialogPageObject <T extends AbstractSrConfigDialog, Q> {
	@Mock
	private IWorkbenchHelpSystem wbHelpSystemMock;

	private SWTBot bot;

	private T dialog;

	public BaseSrConfigDialogPageObject(final ServicesRegistrySystemValidator servicesRegistrySystemValidator, final Set<IServicesRegistrySystem> existingConfigs, final Q additionalData) {
		MockitoAnnotations.initMocks(this);

		syncExec(new VoidResult() {
			@Override
			public void run() {
				dialog = createDialog(servicesRegistrySystemValidator, existingConfigs, wbHelpSystemMock, additionalData);
			}
		});
	}

	public void open() {
		syncExec(new VoidResult() {

			@Override
			public void run() {

				dialog.create();
				dialog.setBlockOnOpen(false);
				dialog.open();

				Assertions.waitAssert(new IWaitCondition() {
					@Override
					public boolean checkCondition() throws ConditionCheckException {
						return dialog.getShell()!=null;
					}
				}, "Dialog shell not initialized");
				bot = new SWTBot(dialog.getShell());
			}
		});
	}

	public void cancel() {
		syncExec(new VoidResult() {
			@Override
			public void run() {
				dialog.close();
			}
		});
	}
	
	public boolean canOk() {
		return bot.button(IDialogConstants.OK_LABEL).isEnabled();
	}

	public void ok() {
		bot.button(IDialogConstants.OK_LABEL).click();
		Assertions.waitAssert(new IWaitCondition() {
			@Override
			public boolean checkCondition() throws ConditionCheckException {
				return dialog.getReturnCode() == Window.OK;
			}
		}, "Window did not close with OK code");
	}

	public boolean areCredentialsStored() {
		return storeCredentialsCheckbox().isChecked();
	}

	public void toggleStoreCredentials() {
		storeCredentialsCheckbox().click();
		waitForEvents();
	}

	public boolean isUseHttps() {
		return httpsCheckbox().isChecked();
	}

	public void toggleUseHttps() {
		httpsCheckbox().click();
		waitForEvents();
	}

	public String getUserName() {
		return userNameText().getText();
	}

	public void setUserName(String userName) {
		userNameText().setText(userName);
		waitForEvents();
	}

	public boolean isUserNameEditable() {
		return userNameText().isEnabled();
	}

	public String getPassword() {
		return passwordText().getText();
	}

	public void setPassword(String password) {
		passwordText().setText(password);
		waitForEvents();
	}

	public boolean isPasswordEditable() {
		return passwordText().isEnabled();
	}


	public String getConnectionName() {
		return syncExec(new Result<String>() {
			@Override
			public String run() {
				return bot.textWithLabel(SrUiMessages.CFG_DLG_CONNECTION_NAME).widget.getText();
			}
		});
	}

	public void setConnectionName(String connectionName) {
		connectionNameText().setText(connectionName);
		waitForEvents();
	}

	public String getConnectionNameHint() {

		return syncExec(new Result<String>() {
			@Override
			public String run() {
				return bot.textWithLabel(SrUiMessages.CFG_DLG_CONNECTION_NAME).widget.getMessage();
			}
		});
	}

	public String getHost() {
		return hostText().getText();
	}

	public void setHost(String host) {
		hostText().setText(host);
		waitForEvents();
	}

	public String getPort() {
		return portText().getText();
	}
	public void setPort(String port) {
		portText().setText(port);
		waitForEvents();
	}

	public IStatus getStatus() {
		return dialog.getStatus();
	}

	public IServicesRegistrySystem getConfig() {
		return dialog.getConfig();
	}

	public void verifyHelpIsSet() {
		Mockito.verify(wbHelpSystemMock).setHelp(Mockito.isA(Shell.class), Mockito.eq(AbstractSrConfigDialog.CONFIG_HELP_CONTEXT_ID));
	}
	
	protected abstract T createDialog(final ServicesRegistrySystemValidator servicesRegistrySystemValidator, final Set<IServicesRegistrySystem> existingConfigs, IWorkbenchHelpSystem helpSystem, Q additionalData);

	private SWTBotCheckBox storeCredentialsCheckbox() {
		return bot.checkBox(SrUiMessages.CFG_DLG_STORE_CREDENTIALS);
	}

	private SWTBotCheckBox httpsCheckbox() {
		return bot.checkBox(SrUiMessages.CFG_DLG_USE_HTTPS_BUTTON);
	}

	private SWTBotText userNameText() {
		return bot.textWithLabel(SrUiMessages.CFG_DLG_USER_NAME);
	}

	private SWTBotText passwordText() {
		return bot.textWithLabel(SrUiMessages.CFG_DLG_PASSWORD);
	}

	private SWTBotText hostText() {
		return bot.textWithLabel(SrUiMessages.CFG_DLG_HOST);
	}

	private SWTBotText portText() {
		return bot.textWithLabel(SrUiMessages.CFG_DLG_PORT);
	}

	private SWTBotText connectionNameText() {
		return bot.textWithLabel(SrUiMessages.CFG_DLG_CONNECTION_NAME);
	}
	
	private void waitForEvents() {
		syncExec(new VoidResult() {
			@Override
			public void run() {
			}
		});
	}
}
