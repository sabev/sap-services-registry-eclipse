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
package org.eclipse.servicesregistry.core.internal.classifications;

import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemsGreaterThanVersionFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault;

public interface IClassificationsTreeBuilder
{
	/**
	 * Creates a {@link ITreeNodeList} structure which corresponds to the classification systems and their values available in a services registry system
	 */
	public ITreeNodeList buildTree() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault, GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault;
}