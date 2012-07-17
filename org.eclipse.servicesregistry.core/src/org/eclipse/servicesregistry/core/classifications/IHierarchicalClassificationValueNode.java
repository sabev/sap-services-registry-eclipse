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

import java.util.List;


/**
 * Represents a classification value that is not only a key-value-pair but also part of a hierarchical structure.
 * 
 * @author Joerg Dehmel
 */
public interface IHierarchicalClassificationValueNode extends IClassificationValueNode
{

	/**
	 * Provides the children of this value.
	 * 
	 * @return children, empty list if no children exist
	 */
	List<IHierarchicalClassificationValueNode> getChildValues();

	/**
	 * Provides the parent of this instance.
	 * 
	 * @return the parent, null if this instance acts as root node
	 */
	IHierarchicalClassificationValueNode getParentValue();

}