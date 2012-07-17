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
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.IResolvable;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;

public class ServiceDefFilter implements IFilter
{
	@Override
	public boolean select(Object toTest)
	{
		if(!(toTest instanceof IServiceDefinition))
		{
			return false;
		}
		
		if(toTest instanceof IResolvable)
		{
			return ((IResolvable)toTest).isResolved();
		}
		
		return true;
	}
}
