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
package org.eclipse.servicesregistry.ui.test;

import org.eclipse.servicesregistry.ui.test.config.AbstractSrConfigDialogIllegalStatesTest;
import org.eclipse.servicesregistry.ui.test.config.AbstractSrConfigDialogTest;
import org.eclipse.servicesregistry.ui.test.config.EditSrConfigDialogTest;
import org.eclipse.servicesregistry.ui.test.config.NewSrConfigDialogTest;
import org.eclipse.servicesregistry.ui.test.prefpage.SrConfigContentProviderTest;
import org.eclipse.servicesregistry.ui.test.prefpage.SrConfigLabelProviderTest;
import org.eclipse.servicesregistry.ui.test.prefpage.SrPreferencePageUiTest;
import org.eclipse.servicesregistry.ui.test.prefpage.SrPreferencesControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({AbstractSrConfigDialogTest.class, AbstractSrConfigDialogIllegalStatesTest.class, EditSrConfigDialogTest.class, NewSrConfigDialogTest.class,
	SrConfigContentProviderTest.class, SrConfigLabelProviderTest.class, SrPreferencesControllerTest.class, SrPreferencePageUiTest.class})
@RunWith(Suite.class)
public class AllSwtBotTestsSuite
{

}
