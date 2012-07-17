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
package org.eclipse.servicesregistry.wsdl.internal.wsdlimport;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;

class XsdSchemaLocationResolverAdapterFactory extends AdapterFactoryImpl
{
	protected XsdSchemaLocationResolverImpl schemaLocator = new XsdSchemaLocationResolverImpl();

    public boolean isFactoryForType(Object type)
    {
      return type == XSDSchemaLocationResolver.class;
    }

    public Adapter adaptNew(Notifier target, Object type)
    {
      return schemaLocator;
    }
}
