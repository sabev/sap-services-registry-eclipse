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
package org.eclipse.servicesregistry.search.core.internal.search.result.resolve;

/**
 * Interface for instances which can be resolved
 * 
 * @author Danail Branekov
 */
public interface IResolvable
{
	/**
	 * Resolves the instance
	 * 
	 * @throws OperationCancelledException
	 *             in case the resolve operation has been cancelled
	 */
	public void resolve() throws OperationCancelledException;

	/**
	 * Checks whether the object is resolved
	 * 
	 * @return true in case the object is resolved, false otherwise
	 */
	public boolean isResolved();
}
