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
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemsGreaterThanVersionFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault;

public interface IClassificationSystemsFinder
{
	/**
	 * Find all classification systems available in a services registry system
	 */
	public Collection<ClassificationSystem> findClassificationSystems() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault;
}
