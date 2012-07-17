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
 * Exception which can be thrown by {@link IWaitCondition#checkCondition()} method. This is a wrapper exception which encloses the exception cause
 * 
 * @author Danail Branekov
 */
public class ConditionCheckException extends Exception
{
	private static final long serialVersionUID = -4929080285506788984L;

	public ConditionCheckException(final Throwable cause)
	{
		super(cause);
	}
	
	public ConditionCheckException(final String message)
	{
		super(message);
	}
}
