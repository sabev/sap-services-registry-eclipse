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
package org.eclipse.servicesregistry.ui.test.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystemValidator;
import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.servicesregistry.ui.test.config.pageobjects.NewSrConfigDialogPageObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class NewSrConfigDialogTest 
{
	private NewSrConfigDialogPageObject configDialog;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		configDialog = new NewSrConfigDialogPageObject(new ServicesRegistrySystemValidator(), new HashSet<IServicesRegistrySystem>());
		configDialog.open();
	}
	
	@After
	public void tearDown()
	{
		configDialog.cancel();
	}
	
	@Test
	public void testOkButtonIsDisabledByDefault()
	{
		assertFalse("OK button should be disabled by default", configDialog.canOk());
	}
	
	@Test
	public void testInitialStatus()
	{
		final IStatus status = configDialog.getStatus();
		assertTrue("INFO status expected by default", status.getSeverity() == IStatus.INFO);
		assertEquals("Unexpected info status message", SrUiMessages.NEW_CFG_DLG_INFO, status.getMessage());
	}
	
}
