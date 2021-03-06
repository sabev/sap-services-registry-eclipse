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

public class ResolveException extends RuntimeException
{
	private static final long serialVersionUID = 6261367542499266629L;

	public ResolveException(final Throwable cause)
	{
		super(cause);
	}

}
