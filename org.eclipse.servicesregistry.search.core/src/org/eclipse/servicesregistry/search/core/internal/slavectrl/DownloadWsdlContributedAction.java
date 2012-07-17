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
 * {@link IContributedAction} implementation which downloads a WSDL to the target directory specified
 * 
 * @author Danail Branekov
 */
public class DownloadWsdlContributedAction extends GetWsdlContributedAction<IActionConfiguration> implements IContributedAction
{
	public static final String DOWNLOAD_WSDL_CONTRIBUTED_ACTION = DownloadWsdlContributedAction.class.getName();

	@Override
	public Object getActionId()
	{
		return DOWNLOAD_WSDL_CONTRIBUTED_ACTION;
	}
}
