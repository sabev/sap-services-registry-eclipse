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
package org.eclipse.servicesregistry.wsdl.endpoint;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.wsdl.internal.plugin.text.WsdlMessages;
import org.eclipse.servicesregistry.wsdl.walker.CancelWalking;
import org.eclipse.servicesregistry.wsdl.walker.WsdlVisitor;
import org.eclipse.servicesregistry.wsdl.walker.WsdlWalker;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact.ARTIFACT_TYPE;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.binding.http.HTTPAddress;
import org.eclipse.wst.wsdl.binding.http.HTTPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;

/**
 * Utility for "refactoring" a binding WSDL to an endpoint one. Binding WSDLs could have several portttypes, bindings, services.
 * The implementation would remove all services and all obsolete porttypes/bindings and would create a dedicated service with port
 * for the binding/porttype specified by utility user. The utility implementation supports SOAP and HTTP bindings
 * 
 * @author Danail Branekov
 */
public class EndpointWsdlRefactorer
{
	private static final String SOAP_BINDING_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/soap/"; //$NON-NLS-1$
	private static final String HTTP_BINDING_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/http/"; //$NON-NLS-1$
	private static final String BINDING_ELEMENT_NAME = "binding"; //$NON-NLS-1$

	/**
	 * Refactors the WSDL specified by wsdlContainer to become endpoint one
	 * 
	 * @param wsdlContainer
	 *            the WSDL document model
	 * @param endpointData
	 *            the data of the selected endpoint
	 * @throws BindingNotFoundException
	 *             when endpointData specifies a binding which is not available in the WSDL document
	 * @throws UnsupportedBindingException
	 *             when the binding specified by endpointData is not SOAP or HTTP
	 */
	public void refactorToEndpointWsdl(final IWsdlWtpDescriptorContainer wsdlContainer, final IEndpointWsdlData endpointData) throws BindingNotFoundException, UnsupportedBindingException
	{
		final WsdlWalker<RuntimeException> deletingWalker = new WsdlWalker<RuntimeException>(removeObsoleteWsdlArtifactsVisitor(endpointData));
		deletingWalker.walk(wsdlContainer.getRootWsdlDefinition());

		final List<Binding> foundBindings = new ArrayList<Binding>();
		final WsdlWalker<RuntimeException> findBindingWalker = new WsdlWalker<RuntimeException>(findBindingVisitor(endpointData.bindingQName(), foundBindings));
		findBindingWalker.walk(wsdlContainer.getRootWsdlDefinition());

		if (foundBindings.size() > 0)
		{
			createServiceWithEndpoint(wsdlContainer.getRootWsdlDefinition().getEObject(), foundBindings.iterator().next(), endpointData);
		} else
		{
			throw new BindingNotFoundException("Binding " + endpointData.bindingQName() + " not available in WSDL " + endpointData.bindingWsdlUrl(), MessageFormat.format( //$NON-NLS-1$ //$NON-NLS-2$
											WsdlMessages.EndpointWsdlRefactorer_BindingNotFound, endpointData.bindingQName(), endpointData.bindingWsdlUrl()));
		}
	}

	@SuppressWarnings("unchecked")
	private void createServiceWithEndpoint(final Definition wsdlDef, final Binding binding, final IEndpointWsdlData endpointData) throws UnsupportedBindingException
	{
		/*
		 * If the namespace of the binding is not declared as a prefix in the wsdl document,
		 * when we set the binding to the newly created port, the wtp wsdl api will not prefix the referred binding name.
		 * An unprefixed binding attribute of a port renders the WSDL invalid according to spec.
		 * We workaround this limitation by checking whether such a prefix exists in the document, and if not, we create it
		 * in the underlying DOM.
		 */
		declareBindingNamespacePrefixIfInexistent(wsdlDef, binding);
		
		final Port port = WSDLFactory.eINSTANCE.createPort();
		port.setEBinding(binding);
		port.setName(endpointData.endpointName());
		port.addExtensibilityElement(createPortEndpointAddressExtElement(binding, endpointData.endpointAddress()));

		final Service service = WSDLFactory.eINSTANCE.createService();
		service.setQName(endpointData.serviceQName());
		service.getEPorts().add(port);
		wsdlDef.getEServices().add(service);
		
	}

	private void declareBindingNamespacePrefixIfInexistent(final Definition wsdlDef, final Binding binding) 
	{
		
		final Set<String> allKeys = new HashSet<String>(); 
		@SuppressWarnings("unchecked")
		final Map<String, String> declaredNamespaces = (Map<String, String>)wsdlDef.getNamespaces();
		boolean bindingNamespacePrefixIsDeclared = false;
		
		for(Entry<String, String> currentNamespaceEntry: declaredNamespaces.entrySet()) 
		{
			allKeys.add(currentNamespaceEntry.getKey());
			if(currentNamespaceEntry.getValue().equals(binding.getQName().getNamespaceURI())) 
			{
				bindingNamespacePrefixIsDeclared = true;
			}
		}
		
		if(!bindingNamespacePrefixIsDeclared) 
		{
			//we need to ensure we declare the namespace under a prefix name inexistent so far
			final String prefix = calculatePrefixName(allKeys);
			wsdlDef.getElement().setAttribute("xmlns:"+prefix, binding.getQName().getNamespaceURI()); //$NON-NLS-1$
			
		}
	}

	private String calculatePrefixName(final Set<String> existingPrefixes) 
	{
		
		Integer prefixIndex = 0;
		
		while(existingPrefixes.contains("n"+prefixIndex))  //$NON-NLS-1$
		{
			prefixIndex++;
		}
		
		final String prefix = "n" + prefixIndex; //$NON-NLS-1$
		return prefix;
	}

	private ExtensibilityElement createPortEndpointAddressExtElement(final Binding binding, final String endpointAddress) throws UnsupportedBindingException
	{
		final String bindingNamespaceUri = getBindingNamespaceUri(binding);
		if (bindingNamespaceUri.equals(SOAP_BINDING_NAMESPACE))
		{
			final SOAPAddress soapAddress = SOAPFactory.eINSTANCE.createSOAPAddress();
			soapAddress.setLocationURI(endpointAddress);
			return soapAddress;
		}

		if (bindingNamespaceUri.equals(HTTP_BINDING_NAMESPACE))
		{
			final HTTPAddress httpAddr = HTTPFactory.eINSTANCE.createHTTPAddress();
			httpAddr.setLocationURI(endpointAddress);
			return httpAddr;
		}

		throw new UnsupportedBindingException("Unsupported binding namespace: " + bindingNamespaceUri, MessageFormat.format(WsdlMessages.EndpointWsdlRefactorer_BindingNotSupported, //$NON-NLS-1$
										bindingNamespaceUri));
	}

	private String getBindingNamespaceUri(final Binding binding)
	{
		for (Object e : binding.getEExtensibilityElements())
		{
			final ExtensibilityElement extElement = (ExtensibilityElement) e;
			if (extElement.getElementType().getLocalPart().equals(BINDING_ELEMENT_NAME))
			{
				return extElement.getElement().getNamespaceURI();
			}
		}

		throw new IllegalStateException("No extensibility element for binding " + binding.getQName()); //$NON-NLS-1$
	}

	private WsdlVisitor<RuntimeException> removeObsoleteWsdlArtifactsVisitor(final IEndpointWsdlData epData)
	{
		// Visitor which removes all services, all obsolete portyypes and all obsolete bindings from the current wsdl artifacts
		return new WsdlVisitor<RuntimeException>()
		{
			@Override
			public void visit(final IWsdlArtifact<?> currentArtifact) throws RuntimeException, CancelWalking
			{
				if (!isWsdl(currentArtifact))
				{
					return;
				}

				final Definition def = ((IWsdlDefinition) currentArtifact).getEObject();
				removeServices(def);
				removeBindings(def, epData.bindingQName());
				removePorttypes(def, epData.porttypeQName());
			}

			private void removeServices(final Definition def)
			{
				def.getEServices().clear();
			}

			@SuppressWarnings("unchecked")
			private void removePorttypes(final Definition def, final QName porttypeQname)
			{
				final List<PortType> porttypesToRemove = new ArrayList<PortType>();
				for (Object pt : def.getEPortTypes())
				{
					final PortType porttype = (PortType) pt;
					if (!porttype.getQName().equals(porttypeQname))
					{
						porttypesToRemove.add(porttype);
					}
				}

				def.getEPortTypes().removeAll(porttypesToRemove);
			}

			@SuppressWarnings("unchecked")
			private void removeBindings(final Definition def, final QName bindingQname)
			{
				final List<Binding> bindingsToRemove = new ArrayList<Binding>();
				for (Object b : def.getEBindings())
				{
					final Binding binding = (Binding) b;
					if (!binding.getQName().equals(bindingQname))
					{
						bindingsToRemove.add(binding);
					}
				}

				def.getEBindings().removeAll(bindingsToRemove);
			}
		};
	}

	private WsdlVisitor<RuntimeException> findBindingVisitor(final QName bindingQname, final List<Binding> foundBindingsHolder)
	{
		return new WsdlVisitor<RuntimeException>()
		{
			@Override
			public void visit(IWsdlArtifact<?> currentArtifact) throws RuntimeException, CancelWalking
			{
				if (!isWsdl(currentArtifact))
				{
					return;
				}

				final IWsdlDefinition def = (IWsdlDefinition) currentArtifact;
				for (Object b : def.getEObject().getEBindings())
				{
					final Binding binding = (Binding) b;
					if (binding.getQName().equals(bindingQname))
					{
						foundBindingsHolder.add(binding);
					}
				}
			}
		};

	}

	private boolean isWsdl(final IWsdlArtifact<?> artifact)
	{
		return artifact.getType() == ARTIFACT_TYPE.REFERENCED_WSDL || artifact.getType() == ARTIFACT_TYPE.ROOT_WSDL;
	}
}
