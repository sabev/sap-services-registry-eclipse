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
package org.eclipse.servicesregistry.search.core.internal.slavectrl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.platform.discovery.core.api.ISearchConsoleMasterController;
import org.eclipse.platform.discovery.core.api.ISearchConsoleSlaveController;


public class SRSlaveController implements ISearchConsoleSlaveController
{
	@Override
	public Set<IContributedAction> createActions()
	{
		final Set<IContributedAction> actions = new HashSet<IContributedAction>();
		actions.add(new DownloadWsdlContributedAction());
		actions.add(new GetEndpointWsdlContributedAction());
		actions.add(new OpenWsdlContributedAction());
		
		return actions;
	}

	@Override
	public void setMasterController(final ISearchConsoleMasterController masterController)
	{
	}

}
