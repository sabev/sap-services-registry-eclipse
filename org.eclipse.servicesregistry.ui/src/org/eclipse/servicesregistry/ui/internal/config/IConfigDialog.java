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
package org.eclipse.servicesregistry.ui.internal.config;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;

public interface IConfigDialog
{
	public int open();
	public IServicesRegistrySystem getConfig();
}
