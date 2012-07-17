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

import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;

public class WsdlCollector extends ArtifactsCollector<IWsdlDefinition> {

	@Override
	protected boolean isOurType(IWsdlArtifact<?> currentArtifact) {
		return currentArtifact instanceof IWsdlDefinition; 
	}

}
