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
package org.eclipse.servicesregistry.search.core.test.slavectrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.DownloadWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.GetEndpointWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.OpenWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.SRSlaveController;
import org.junit.Test;

public class SRSlaveControllerTest
{
	@Test
	public void testCreateActions()
	{
		final SRSlaveController slaveController = new SRSlaveController();
		final Set<IContributedAction> actions = slaveController.createActions();
		assertEquals("3 actions expected", 3, actions.size());
		for(IContributedAction action : actions)
		{
			assertTrue("Unexpected action: " + action.getClass().getName(), expectedExplorerActionClasses().contains(action.getClass()));
		}
		
	}
	
	private Set<Class<?>> expectedExplorerActionClasses()
	{
		final Set<Class<?>> result = new HashSet<Class<?>>();
		result.add(DownloadWsdlContributedAction.class);
		result.add(GetEndpointWsdlContributedAction.class);
		result.add(OpenWsdlContributedAction.class);
		
		return result;
	}
}
