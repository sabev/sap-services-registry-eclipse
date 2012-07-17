/*******************************************************************************
 * Copyright (c) 2010, 2012 SAP AG, Walldorf.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
/**
 * 
 */
package org.eclipse.servicesregistry.core.internal.logging;

import org.eclipse.core.runtime.ILog;
import org.eclipse.platform.discovery.util.internal.StatusUtils;
import org.eclipse.servicesregistry.core.internal.plugin.SrCorePlugin;


/**
 * Convenient logger that wraps the plugin logger to fit the needs of this module.
 * 
 * @author Joerg Dehmel, Danail Branekov
 */
public class Logger implements ILogger
{
	private static final Logger INSTANCE = new Logger();

	public static ILogger instance()
	{
		return INSTANCE;
	}

	@Override
	public void logError(String message)
	{
		log().log(StatusUtils.statusError(message));
	}

	@Override
	public void logError(String message, Throwable cause)
	{
		log().log(StatusUtils.statusError(message, cause));
	}

	@Override
	public void logWarn(String message)
	{
		log().log(StatusUtils.statusWarning(message));
	}

	@Override
	public void logDebug(String message)
	{
		log().log(StatusUtils.statusInfo(message));
	}

	private ILog log()
	{
		return SrCorePlugin.getDefault().getLog();
	}

	@Override
	public void logDebug(String message, Throwable cause)
	{
		log().log(StatusUtils.statusInfo(message, cause));
	}
}
