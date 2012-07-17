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

import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;


public abstract class ArtifactsCollector<T extends IWsdlArtifact<?>> implements WsdlVisitor<RuntimeException> {

	private Set<T> collectedArtifacts = new HashSet<T>();
	@Override
	
	@SuppressWarnings("unchecked")
	public void visit(IWsdlArtifact<?> currentArtifact) throws CancelWalking {
		if(isOurType(currentArtifact)) {
			collectedArtifacts.add((T)currentArtifact);
		}
	}
	
	protected abstract boolean isOurType(IWsdlArtifact<?> currentArtifact);

	public Set<T> collectedArtifacts() {
		return collectedArtifacts;
	}

}
