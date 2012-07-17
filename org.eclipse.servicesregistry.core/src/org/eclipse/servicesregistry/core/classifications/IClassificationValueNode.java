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


/**
 * Represents a value used by flat classification systems.
 * 
 * @author Joerg Dehmel
 */
public interface IClassificationValueNode extends ITreeNode
{
	/**
	 * Provides the display value for the ui.
	 * 
	 * @return the display value
	 */
	String getName();

	/**
	 * Provides the value used for looking up service definitions.
	 * 
	 * @return the value used for looking up in ER
	 */
	String getCode();

	/**
	 * Provides the classification system this value belongs to.
	 * 
	 * @return the classification system
	 */
	IClassificationSystemNode getClassificationSystem();
	
	/**
	 * Selects/deselects the node
	 * @param selected true is selected; false otherwise
	 */
	void setSelected(final boolean selected);
	
	/**
	 * Returns true if the node is selected; false otherwise
	 */
	boolean isSelected();
}