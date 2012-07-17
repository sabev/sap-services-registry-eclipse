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

import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.jface.action.Action;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.IActionConfiguration;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.OpenWsdlContributedAction;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;

public class OpenWsdlInEditorAction extends Action
{
	private IDiscoveryEnvironment env;
	private OpenWsdlContributedAction openWsdlAction;
	private final IActionConfiguration actionConfig;

	public OpenWsdlInEditorAction(final IActionConfiguration actionConfig, final OpenWsdlContributedAction openWsdlAction, final IDiscoveryEnvironment env)
	{
		this.env = env;
		this.openWsdlAction = openWsdlAction;
		this.actionConfig = actionConfig;
	}

	@Override
	public String getId()
	{
		return "org.eclipse.servicesregistry.search.ui.internal.result.actions.open.wsdl.in.editor";//$NON-NLS-1$
	}

	@Override
	public String getText()
	{
		return SearchUIMessages.SrSearchViewCustomization_OpenInWsdlEditorMenuItem;
	}

	@Override
	public void run()
	{
		this.openWsdlAction.perform(this.env.operationRunner(), new HashSet<Object>(Arrays.asList(new Object[] { actionConfig })));
	}
}
