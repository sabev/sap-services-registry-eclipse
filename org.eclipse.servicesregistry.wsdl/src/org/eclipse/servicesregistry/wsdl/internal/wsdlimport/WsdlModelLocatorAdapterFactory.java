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
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.wst.wsdl.internal.util.WSDLModelLocator;

@SuppressWarnings("restriction")
class WsdlModelLocatorAdapterFactory extends AdapterFactoryImpl
{
	protected CustomWSDLModelLocator customWSDLModelLocator = new CustomWSDLModelLocator();
	  
	  class CustomWSDLModelLocator extends AdapterImpl implements WSDLModelLocator
	  {
	    public String resolveURI(String baseLocation, String namespace, String location)
	    {
	      return URIResolverPlugin.createResolver().resolve(baseLocation, namespace, location);
	    }

	    public boolean isAdatperForType(Object type)
	    {
	      return type == WSDLModelLocator.class;
	    }
	  }

	  public boolean isFactoryForType(Object type)
	  {
	    return type == WSDLModelLocator.class;
	  }

	  public Adapter adaptNew(Notifier target, Object type)
	  {
	    return customWSDLModelLocator;
	  }
}
