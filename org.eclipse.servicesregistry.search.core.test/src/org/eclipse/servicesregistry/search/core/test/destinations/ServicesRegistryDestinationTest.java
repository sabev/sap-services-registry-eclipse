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
package org.eclipse.servicesregistry.search.core.test.destinations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.search.core.internal.destinations.ServicesRegistryDestination;
import org.eclipse.servicesregistry.testutils.EqualsTestCase;
import org.junit.Before;

public class ServicesRegistryDestinationTest extends EqualsTestCase<ServicesRegistryDestination>
{
	public ServicesRegistryDestinationTest()
	{
		super("ServicesRegistryDestinationTest", ServicesRegistryDestination.class);
	}

	protected IServicesRegistrySystem serverConfig1;
	protected IServicesRegistrySystem serverConfig2;
	
	@Before
	public void setUp() throws Exception
	{
		serverConfig1 = createConfig("myhost", 1111, "myuser", "mypass", "myconnection", true, false);
		serverConfig2 = createConfig("myhost", 1111, "myuser", null, "myconnection", false, true);
	}
	
	@Override
	public void modifyObjectInstance(ServicesRegistryDestination instance)
	{
	}

	@Override
	public ServicesRegistryDestination newAncestorEqualInstance()
	{
		return new ServicesRegistryDestination(serverConfig1){
			
		};
	}

	@Override
	public ServicesRegistryDestination newEqualInstance()
	{
		return new ServicesRegistryDestination(serverConfig1);
	}

	@Override
	public ServicesRegistryDestination newNonEqualInstance()
	{
		return new ServicesRegistryDestination(serverConfig2);
	}

	@Override
	public Iterator<ServicesRegistryDestination> newObjectIterator(int iterations)
	{
		final List<ServicesRegistryDestination> list = new ArrayList<ServicesRegistryDestination>(iterations);
		for(int i = 0; i < iterations; i++)
		{
			list.add(new ServicesRegistryDestination(serverConfig1));
		}
		
		return list.iterator();
	}
	
	private IServicesRegistrySystem createConfig(final String host, final int port, final String user, final String pass, final String connName, final boolean keepCredentials, final boolean useHttps)
	{
		return new IServicesRegistrySystem(){
			@Override
			public String displayName()
			{
				if(connName != null)
				{
					return connName;
				}
				return host + ":" + Integer.toString(port);
			}

			@Override
			public String host()
			{
				return host;
			}

			@Override
			public int port()
			{
				return port;
			}

			@Override
			public String userName()
			{
				return user;
			}

			@Override
			public String password()
			{
				return pass;
			}

			@Override
			public boolean areCredentialsStored()
			{
				return keepCredentials;
			}

			public boolean useHttps() {
				return useHttps;
			}};
	}
}
