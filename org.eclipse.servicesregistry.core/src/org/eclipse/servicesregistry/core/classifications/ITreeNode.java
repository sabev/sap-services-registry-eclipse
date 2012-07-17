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
 * A common interface to organize classification system and values together in a tree.
 * 
 * @author Joerg Dehmel
 */
public interface ITreeNode
{
	/**
	 * Provides the parent of this node.
	 * 
	 * @return the parent, null in the case this is a root node
	 */
	ITreeNode getParentNode();

	/**
	 * Provides a list of childs of this node.
	 * 
	 * @return the childs, an empty list if no childs exist, never null
	 */
	List<ITreeNode> getChildNodes();

	/**
	 * Provides a display name of the node.
	 * 
	 * @return the display name
	 */
	String getName();
}
