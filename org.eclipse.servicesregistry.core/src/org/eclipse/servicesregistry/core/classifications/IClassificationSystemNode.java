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
package org.eclipse.servicesregistry.core.classifications;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.core.internal.classifications.ClassificationSystemType;

/**
 * Base type for a classification system used by SR.
 * 
 * @author Joerg Dehmel
 */
public interface IClassificationSystemNode extends ITreeNode
{
	/**
	 * Provides the classification system name
	 * 
	 * @return the classification system name
	 */
	String getName();
	
	QName getQname();

	/**
	 * Provides the type of the classification system (flat, group, hierarchical)
	 * 
	 * @return the classification system type
	 * @see IClassificationSystemInfo IClassificationSystemInfo contains possible type values
	 */
	ClassificationSystemType getType();
}
