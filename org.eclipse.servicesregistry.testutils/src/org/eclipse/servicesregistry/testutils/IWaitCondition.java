/*******************************************************************************
 * Copyright (c) 2010, 2012 SAP AG, Walldorf.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.servicesregistry.testutils;



/**
 * Interface which defines a condition upon which the execution can continue 
 * @author Danail Branekov
 */
public interface IWaitCondition
{
	/**
	 * Method which checks whether a given condition is met
	 * @return <code>true</code> in case the condition is met, <code>false</code> otherwise
	 * @throws ConditionCheckException when implementors decide that they cannot deal with a given exceptional situation
	 */
	public boolean checkCondition() throws ConditionCheckException;
}
