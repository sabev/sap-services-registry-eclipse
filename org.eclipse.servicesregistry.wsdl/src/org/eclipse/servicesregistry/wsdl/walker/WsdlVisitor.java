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


/**
 * 
 * @param <T> - the exception which the visitor is allowed to throw
 */
public interface WsdlVisitor<T extends Exception> {
	/**
	 * @throws T
	 * @throws CancelWalking if no further processing of the wsdl tree is desired by this visitor
	 */
	public void visit(IWsdlArtifact<?> currentArtifact) throws T, CancelWalking;
}