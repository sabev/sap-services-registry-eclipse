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
package org.eclipse.servicesregistry.core;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.SrApiImpl;
import org.eclipse.servicesregistry.core.internal.classifications.ClassificationsTreeBuilder;
import org.eclipse.servicesregistry.core.internal.classifications.finders.ClassificationSystemsFinder;
import org.eclipse.servicesregistry.core.internal.classifications.finders.ClassificationValuesFinder;
import org.eclipse.servicesregistry.core.internal.proxy.factory.ServicesRegistryProxyFactory;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;

public class SrApiFactory
{
	public ISrApi create(final IServicesRegistrySystem srSystem)
	{
		final ServicesRegistrySi srProxy = new ServicesRegistryProxyFactory(srSystem).createProxy();
		return new SrApiImpl(srProxy, new ClassificationsTreeBuilder(new ClassificationSystemsFinder(srProxy), new ClassificationValuesFinder(srProxy)));
	}
}
