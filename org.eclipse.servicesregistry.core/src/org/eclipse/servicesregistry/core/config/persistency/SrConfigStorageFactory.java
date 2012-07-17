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
package org.eclipse.servicesregistry.core.config.persistency;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.servicesregistry.core.internal.config.persistency.SrConfigStorage;
import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.IPasswordStore;
import org.eclipse.servicesregistry.core.internal.plugin.SrCorePlugin;

/**
 * Factory for creating new {@link IConfigStorage} instances
 * @author Danail Branekov
 *
 */
public class SrConfigStorageFactory
{
	private static IConfigStorage storage;
	
	public static IConfigStorage getDefault()
	{
		if(storage == null)
		{
			storage = createSrConfigStorage();
		}
		
		return storage;
	}
	
	private static IConfigStorage createSrConfigStorage()
	{
		final IPersistentPreferenceStore prefStore = (IPersistentPreferenceStore)SrCorePlugin.getDefault().getPreferenceStore();
		final IPasswordStore passwordStore = SrCorePlugin.getDefault().getPasswordStore();
		return new SrConfigStorage(prefStore, passwordStore);
	}
}
