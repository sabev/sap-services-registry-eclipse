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
package org.eclipse.servicesregistry.core.internal.text;

import org.eclipse.osgi.util.NLS;

public class SrCoreMessages extends NLS {
	private static final String BUNDLE_NAME = SrCoreMessages.class.getCanonicalName(); //$NON-NLS-1$
	
	public static String CFG_INVALID_HOST_ERROR;
	public static String CFG_NO_HOST_ERROR;
	public static String CFG_NO_USER_ERROR;
	public static String CFG_STORE_CREDENTIALS_INFO;
	public static String CFG_INVALID_PORT_VALUE;
	public static String CFG_ALREADY_EXISTS;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SrCoreMessages.class);
	}

	private SrCoreMessages() {
	}
}
