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

import org.eclipse.platform.discovery.core.api.IContributedAction;

/**
 * {@link IContributedAction} implementation which opens a WSDL in the WSDL editor
 * 
 * @author Danail Branekov
 * 
 */
public class OpenWsdlContributedAction extends GetWsdlContributedAction<IActionConfiguration> implements IContributedAction
{
	public static final String OPEN_WSDL_EXPLORER_ACTION = OpenWsdlContributedAction.class.getName();

	@Override
	public Object getActionId()
	{
		return OPEN_WSDL_EXPLORER_ACTION;
	}
}
