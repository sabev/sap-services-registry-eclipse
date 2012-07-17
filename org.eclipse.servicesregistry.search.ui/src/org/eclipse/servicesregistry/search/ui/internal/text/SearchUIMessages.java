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
package org.eclipse.servicesregistry.search.ui.internal.text;

import org.eclipse.osgi.util.NLS;

public class SearchUIMessages
{
	private static final String BUNDLE_NAME = "org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages"; //$NON-NLS-1$
	public static String ServiceDefinitionId;
	public static String ServiceDefinitionName;
	public static String ServiceDocumentation;
	public static String ServiceDefinitionWsdlURL;
	public static String ServicePorttype;
	public static String EndpointName;
	public static String EndpointWsdlUrl;
	public static String EndpointAddress;
	public static String EndpointService;
	public static String EndpointBinding;
	public static String ServiceDefinitionPhysicalSystem;
	public static String RefreshResourcesJobName;
	public static String GetWsdlAction_SaveWsdlCheckBox;
	public static String WorkspaceResourceBrowser_BROWSER_SHELL_TITLE;
	public static String WorkspaceResourceBrowser_RESUORCE_TREE_TOOLTIP;
	public static String GetWsdlAction_OpenWsdlInEditorCheckBox;
	public static String FilesToOverwriteWithDialog_DialogTitle;
	public static String FilesToOverwriteWithDialog_DialogMessage;
	public static String SrSearchViewCustomization_OpenInWsdlEditorMenuItem;
	public static String SrSearchViewCustomization_CreateEndpointWsdlAction;
	public static String CopyWsdlInWorkspaceAction_MenuItemText;
	public static String ClickToResolveNodeText;
	public static String ClassificationValues_Loading;

	static
	{
		NLS.initializeMessages(BUNDLE_NAME, SearchUIMessages.class);
	}
}
