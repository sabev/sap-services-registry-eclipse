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
package org.eclipse.servicesregistry.core.test.prefs.securestore;

import static org.junit.Assert.assertEquals;

import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.Credentials;
import org.junit.Test;

public class CredentialsTest
{
	@Test
	public void testCredentials()
	{
		final String user = "testUser";
		final String password = "testPass";
		
		final Credentials testCredentials = new Credentials(user , password );
		assertEquals(user, testCredentials.getUser());
		assertEquals(password, testCredentials.getPassword());
		
		final Credentials testCredentials2 = new Credentials(null, null);
		assertEquals("", testCredentials2.getUser());
		assertEquals("", testCredentials2.getPassword());
	}

}
