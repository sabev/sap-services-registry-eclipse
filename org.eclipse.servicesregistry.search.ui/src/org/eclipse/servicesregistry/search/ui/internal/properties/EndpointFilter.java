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
package org.eclipse.servicesregistry.search.ui.internal.properties;

import org.eclipse.jface.viewers.IFilter;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;

public class EndpointFilter implements IFilter
{
	@Override
	public boolean select(Object toTest)
	{
		return toTest instanceof IServiceEndpoint;
	}

}
