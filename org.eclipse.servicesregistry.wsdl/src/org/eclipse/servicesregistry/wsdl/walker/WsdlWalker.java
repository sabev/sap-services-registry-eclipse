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
package org.eclipse.servicesregistry.wsdl.walker;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifactReference;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;

/**
 * A class which is capable of traversing a whole wsdl reference tree or a subset of it, without ending up in an infinite recursion in case of cyclic references.
 * @author Dimitar Georgiev
 * @param <E> - the exceptions the walk methods throw, which are the propagated exceptions of the visitor.
 *
 */
public class WsdlWalker<E extends Exception> {
	
	private WsdlVisitor<E> visitor;

	public WsdlWalker(WsdlVisitor<E> visitor) {
		ContractChecker.nullCheckParam(visitor, "processor"); //$NON-NLS-1$
		this.visitor = visitor;
	}
	
	/**
	 * Walks the whole wsdl tree.
	 */
	public void walk(IWsdlDefinition definition) throws E{
		try{
			walkInternal(definition, new HashSet<IWsdlArtifact<?>>());
		}catch (CancelWalking cw) {
			return;
		}
		
	}
	
	/**
	 * Walks the subtree starting from the passed artifact.
	 * @param artifact
	 */
	public void walk(IWsdlArtifact<?> artifact) throws E{
		try{
			walkInternal(artifact, new HashSet<IWsdlArtifact<?>>());
		}catch (CancelWalking cw) {
			return;
		}
		
	}
	
	private void walkInternal(IWsdlArtifact<?> artifact, Set<IWsdlArtifact<?>> visited) throws E, CancelWalking{
		
		visited.add(artifact);
		visitor.visit(artifact);

		for (IWsdlArtifactReference ref : artifact.references()) {
			IWsdlArtifact<?> current = ref.referencedArtifact();
			if (visited.contains(current)) {
				// keep walking :)
				continue;
			} else {
				walkInternal(current, visited);
			}
		}

	}
	
	
}


