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
package org.eclipse.servicesregistry.ui.internal.text;

import org.eclipse.osgi.util.NLS;

public class SrUiMessages  extends NLS {
	private static final String BUNDLE_NAME = SrUiMessages.class.getCanonicalName(); //$NON-NLS-1$


	/** message constant */
	public static String CFG_DLG_CONNECTION_NAME;
	/** message constant */
	public static String CFG_DLG_HOST;
	/** message constant */
	public static String CFG_DLG_PORT;
	public static String CFG_DLG_USE_DEFAULT_HINT_CONN;

	public static String CFG_DLG_USE_HTTPS_BUTTON;

	/** message constant */
	public static String CFG_DLG_USER_NAME;

	/** message constant */
	public static String CFG_DLG_PASSWORD;
	/** message constant */
	public static String CFG_DLG_STORE_CREDENTIALS;
	
	/** message constant */
	public static String CFG_DLG_SETTINGS_GROUP_TEXT;
	/** message constant */
	public static String CFG_DLG_UDE_DEFAULT_HINT;

	/** message constant */
	public static String CFG_DLG_USER_CREDENTIALS_GROUP_TEXT;
	/** message constant */
	public static String NEW_CFG_DLG_TITLE;
	/** message constant */
	public static String EDIT_CFG_DLG_TITLE;
	/** message constant */
	public static String NEW_CFG_DLG_INFO;


	public static String SR_PREFERENCE_ADD_CONFIG_BUTTON;
	public static String SR_PREFERENCE_EDIT_CONFIG_BUTTON;
	public static String SR_PREFERENCE_DELETE_CONFIG_BUTTON;
	public static String SR_PREFERENCE_TABLE_DISPLAY_NAME_COLUMN_TITLE;
	public static String SR_PREFERENCE_TABLE_HOST_COLUMN_TITLE;
	public static String SR_PREFERENCE_TABLE_PORT_COLUMN_TITLE;
	public static String SrPreferencePage_IdenticalCredentialsDialogTitle;
	public static String SrPreferencePage_IdenticalCredentialsDialogMessage;
	public static String SrPreferencePage_ReuseButton;
	public static String SrPreferencePage_OverrideButton;
	public static String SrPreferencesController_DeleteConfigWithAuthDetails;
	public static String SrPreferencesController_DeleteConfigWithoutAuthDetails;
	public static String SrPreferencesController_DeleteConfigConfirmTitle;
	public static String SrPreferencesController_YesButton;
	public static String SrPreferencesController_NoButton;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SrUiMessages.class);
	}

	private SrUiMessages() {
	}
}
