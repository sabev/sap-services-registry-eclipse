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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystemValidator;
import org.eclipse.servicesregistry.ui.test.config.pageobjects.AbstractSrConfigDialogPageObject;
import org.junit.Before;
import org.junit.Test;

public class AbstractSrConfigDialogIllegalStatesTest {
	
	private Set<IServicesRegistrySystem> existingConfigs;
	private ServicesRegistrySystemValidator validator;
	private AbstractSrConfigDialogPageObject configDialog;
	
	@Before
	public void setUp() throws Exception
	{
		existingConfigs = new HashSet<IServicesRegistrySystem>();
		validator = new ServicesRegistrySystemValidator();
		configDialog = new AbstractSrConfigDialogPageObject(validator, existingConfigs);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetConfigWhenDialogNotOpened() {
		configDialog = new AbstractSrConfigDialogPageObject(validator, existingConfigs);
		configDialog.getConfig();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetConfigWhenDialogCancelled() {
		configDialog.open();
		configDialog.cancel();
		configDialog.getConfig();
	}
}
	
