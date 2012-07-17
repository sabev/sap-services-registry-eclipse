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
package org.eclipse.servicesregistry.search.core.internal.search.result.resolve;

/**
 * Runtime exception which indicates that the operation has been cancelled by the user<br>
 * The exception is not localized which means that the handler of the exception should show custom localized message to the user
 * 
 * @author Danail Branekov
 */
public class OperationCancelledException extends RuntimeException
{
	private static final long serialVersionUID = -8639861064177430123L;

	public OperationCancelledException()
	{
		super();
	}

	public OperationCancelledException(Throwable cause)
	{
		super(cause);
	}
}
