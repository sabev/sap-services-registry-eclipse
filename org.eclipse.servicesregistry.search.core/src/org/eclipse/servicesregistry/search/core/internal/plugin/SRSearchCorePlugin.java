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
package org.eclipse.servicesregistry.search.core.internal.plugin;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;

public class SRSearchCorePlugin extends Plugin implements IStartup
{
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.servicesregistry.search.core"; //$NON-NLS-1$

	// The shared instance
	private static SRSearchCorePlugin plugin;
	
	/**
	 * The constructor
	 */
	public SRSearchCorePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static SRSearchCorePlugin getDefault() {
		return plugin;
	}

	@Override
	public void earlyStartup()
	{
		// Nothing particular to do. However, this bundle activator provides common logging API which need to be available as early as possible 
	}
}
