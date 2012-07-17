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
package org.eclipse.servicesregistry.core.internal.plugin;

import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.IPasswordStore;
import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.PasswordStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class SrCorePlugin extends AbstractUIPlugin implements BundleActivator
{
	private static SrCorePlugin instance;
	
	@Override
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		instance = this;
	}
	
	
	@Override
	public void stop(BundleContext context) throws Exception
	{
		super.stop(context);
		instance = null;
	}
	
	public static SrCorePlugin getDefault()
	{
		return instance;
	}
	
	public IPasswordStore getPasswordStore() {
		return PasswordStore.getInstance();
	}	
}
