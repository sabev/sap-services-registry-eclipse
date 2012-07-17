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
package org.eclipse.servicesregistry.core.internal.config;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;

public class ServicesRegistrySystem implements IServicesRegistrySystem {

	private final String displayName;
	private final String host;
	private final int port;
	private final boolean useHttps;
	private final boolean areCredentialsStored;
	private final String userName;
	private final String password;

	public ServicesRegistrySystem(String displayName, String host, int port,
			boolean useHttps, String userName, boolean areCredentialsStored,
			String password) {
		super();
		this.displayName = displayName;
		this.host = host;
		this.port = port;
		this.useHttps = useHttps;
		this.userName = userName;
		this.areCredentialsStored = areCredentialsStored;
		this.password = password;
	}

	@Override
	public String displayName() {
		return displayName;
	}

	@Override
	public String host() {
		return host;
	}

	@Override
	public int port() {
		return port;
	}

	@Override
	public boolean useHttps() {
		return useHttps;
	}

	@Override
	public String userName() {
		return userName;
	}

	@Override
	public String password() {
		return password;
	}

	@Override
	public boolean areCredentialsStored() {
		return areCredentialsStored;
	}

}
