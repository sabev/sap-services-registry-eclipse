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
package org.eclipse.servicesregistry.core.internal.classifications.finders;

import java.util.Collection;

import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValue;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemValuesFault;

public interface IClassificationValuesFinder
{
	/**
	 * Finds all values which are available in a classification system
	 */
	public Collection<ClassificationSystemValue> findValues(final ClassificationSystem classifSystem) throws GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault;
}
