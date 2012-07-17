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
package org.eclipse.servicesregistry.core.internal.config;

import java.util.Set;
import org.eclipse.core.runtime.IStatus;

/**
 * Interface for validating an object to be created among already existing objects. This interface can be used when we have a set of already existing objects and we want to check whether adding a new object to that set would cause issues
 * 
 * @author Danail Branekov
 * 
 * @param <T>
 */
public interface ICreatedObjectValidator<T>
{
	/**
	 * Validates the object to be created
	 * 
	 * @param objectToCreate
	 *            the object to be validated
	 * @param alreadyExistingObjects
	 *            a set of already existing objects
	 * @return status of the validation
	 */
	public IStatus validateCreatedObject(final T objectToCreate, final Set<T> alreadyExistingObjects);
}
