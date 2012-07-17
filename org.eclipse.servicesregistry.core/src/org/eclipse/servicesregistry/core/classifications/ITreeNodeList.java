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
 * Wraps a list of <ITreeNode> objects and adds some additional information about them, e.g. which nodes are selected
 * 
 * @author Joerg Dehmel
 */
public interface ITreeNodeList
{
	/**
	 * Provides the list of root nodes.
	 * 
	 * @return list of root nodes
	 */
	List<ITreeNode> getRootNodes();

	/**
	 * Indicates if a certain node acts as root node.
	 * 
	 * @param node
	 *            node to be check
	 * @return true if passed node is a root node, otherwise false
	 * @throws IllegalArgumentException if the specified node doesn't belong to the wrapped node structure
	 */
	boolean isRootNode(ITreeNode node);

	/**
	 * Gets the currently selected classification values; empty list if none
	 */
	List<IClassificationValueNode> getSelectedNodes();
}
