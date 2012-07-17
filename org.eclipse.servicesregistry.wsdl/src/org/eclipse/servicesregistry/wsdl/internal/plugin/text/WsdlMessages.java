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
package org.eclipse.servicesregistry.wsdl.internal.plugin.text;

import org.eclipse.osgi.util.NLS;

public class WsdlMessages
{
	private static final String BUNDLE_NAME = "org.eclipse.servicesregistry.wsdl.internal.plugin.text.WsdlMessages"; //$NON-NLS-1$
	public static String EndpointWsdlRefactorer_BindingNotFound;
	public static String EndpointWsdlRefactorer_BindingNotSupported;
	public static String WsdlWtpStrategyDefault_FileNotFoundMsg;
	public static String WsdlDownloadManager_WsdlFileCannotBeProcessedMsg;
	public static String WsdlWtpStrategyDefault_WsdlorShemaCannotBeStoredMsg;

	static
	{
		NLS.initializeMessages(BUNDLE_NAME, WsdlMessages.class);
	}

}
