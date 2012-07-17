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


/**
 * Enumeration of possible classification system types.
 * 
 * @author Joerg Dehmel
 */
public enum ClassificationSystemType
{
	/**
	 * classification system that contains flat list of key value pairs
	 */
	FLAT("FLAT"),
	/** values of this classification system are structured as tree */
	HIERARCHY("HIERARCHY"),
	/**
	 * classification system contains a list of flat and / or hierarchical classification systems
	 */
	GROUP("GROUP");

	private final String systemType;

	private ClassificationSystemType(final String systemType)
	{
		this.systemType = systemType;
	}

	/**
	 * Provides the systemType attribute of the enum constant.
	 * 
	 * @return the unique id
	 */
	public final String getSystemType()
	{
		return this.systemType;
	}

	/**
	 * Returns the enum constant by the specified id attribute.
	 * 
	 * @param pId
	 *            id attribute
	 * @return the enum constant, never null
	 */
	public static ClassificationSystemType findByType(final String pId)
	{
		for (final ClassificationSystemType type : values())
		{
			if (type.getSystemType().equals(pId))
			{
				return type;
			}
		}
		throw new IllegalArgumentException("ID " + pId + " is not a valid classification type ID");//$NON-NLS-1$ //$NON-NLS-2$
	}
}
