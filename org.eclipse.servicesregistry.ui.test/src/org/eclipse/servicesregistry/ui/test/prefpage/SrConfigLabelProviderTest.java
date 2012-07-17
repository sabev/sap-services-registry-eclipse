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
package org.eclipse.servicesregistry.ui.test.prefpage;

import static org.junit.Assert.assertEquals;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.ui.internal.prefpage.SrConfigLabelProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SrConfigLabelProviderTest
{
	@Mock
	private IServicesRegistrySystem config;
	private static final String CONNECTION_NAME = "MyConnection";
	private static final String CONNECTION_HOST = "MyHost";
	private static final int CONNECTION_PORT = 80;
	private SrConfigLabelProvider provider;
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		Mockito.stub(config.displayName()).toReturn(CONNECTION_NAME);
		Mockito.stub(config.host()).toReturn(CONNECTION_HOST);
		Mockito.stub(config.port()).toReturn(CONNECTION_PORT);
		
		provider = new SrConfigLabelProvider();
	}

	@Test
	public void testGetLabel()
	{
		assertEquals("Unexpected text", CONNECTION_NAME, provider.getColumnText(config, 0));
		assertEquals("Unexpected text", CONNECTION_HOST, provider.getColumnText(config, 1));
		assertEquals("Unexpected text", ""+CONNECTION_PORT, provider.getColumnText(config, 2));
	}

}
