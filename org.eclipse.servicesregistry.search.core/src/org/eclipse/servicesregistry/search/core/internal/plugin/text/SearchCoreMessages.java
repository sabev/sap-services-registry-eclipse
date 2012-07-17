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
package org.eclipse.servicesregistry.search.core.internal.plugin.text;

import org.eclipse.osgi.util.NLS;

public class SearchCoreMessages
{
	private static final String BUNDLE_NAME = "org.eclipse.servicesregistry.search.core.internal.plugin.text.SearchCoreMessages"; //$NON-NLS-1$

	public static String SEARCH_IN_PROGRESS;
	public static String RESOLVING;

	public static String CopyWsdlInWorkspaceAction_Downloading;
	public static String SaveWsdlAction_ErrorTitle;
	public static String SaveWsdlAction_WsdlProcessingFailedMessage;

	public static String EditResourcesManager_FILE_CONTENTS_CHANGE_FAILED_MSG;

	public static String PhysicalSystemNotFound;

	static
	{
		NLS.initializeMessages(BUNDLE_NAME, SearchCoreMessages.class);
	}
}
