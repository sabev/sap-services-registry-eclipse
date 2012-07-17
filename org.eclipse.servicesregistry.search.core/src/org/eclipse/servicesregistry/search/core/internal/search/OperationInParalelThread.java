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
package org.eclipse.servicesregistry.search.core.internal.search;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.platform.discovery.util.api.longop.ILongOperation;
import org.eclipse.platform.discovery.util.api.longop.LongOpCanceledException;

/**
 * Decorator of a {@link ILongOperation} which is implemented in a way that cancellation is not possible. This implementation would run the delegate
 * {@link ILongOperation} instance in a paralel thread. In case the monitor is cancelled, then the implementation would throw
 * {@link OperationCanceledException} and would leave the paralel thread finish its execution (meaning that the result of the delegate operation is
 * neglected)<br>
 * 
 * @author Danail Branekov
 */
public abstract class OperationInParalelThread<T> implements ILongOperation<T>
{
	private static final int THREAD_SLEEP_TIME = 50;

	private ILongOperation<T> delegateOperation;

	public OperationInParalelThread(final ILongOperation<T> delegateOperation)
	{
		this.delegateOperation = delegateOperation;
	}

	@Override
	public T run(final IProgressMonitor monitor) throws LongOpCanceledException, Exception
	{
		final Holder<T> resultHolder = new Holder<T>();
		final Holder<Exception> excHolder = new Holder<Exception>();
		final ParalelThread paralelThread = new ParalelThread(resultHolder, excHolder);

		beginTask(monitor);
		try
		{
			paralelThread.start();
	
			while (paralelThread.isAlive())
			{
				if (monitor.isCanceled())
				{
					throw new LongOpCanceledException();
				}
				Thread.sleep(THREAD_SLEEP_TIME);
			}
	
			final Exception paralelExc = excHolder.holdObject;
			if(paralelExc == null)
			{
				return resultHolder.holdObject;
			}
			
			throw new InvocationTargetException(paralelExc);
		}
		finally
		{
			done(monitor);
		}
	}

	public abstract void beginTask(IProgressMonitor monitor);
	public abstract void done(IProgressMonitor monitor);

	private class ParalelThread extends Thread
	{
		private final Holder<Exception> excHolder;
		private final Holder<T> resultHolder;

		ParalelThread(final Holder<T> resultHolder, final Holder<Exception> excHolder)
		{
			this.excHolder = excHolder;
			this.resultHolder = resultHolder;
		}

		@Override
		public void run()
		{
			try
			{
				resultHolder.hold(delegateOperation.run(new NullProgressMonitor()));
			}
			catch (Exception e)
			{
				excHolder.hold(e);
			}
		}
	}

	private class Holder<H>
	{
		H holdObject;

		void hold(H object)
		{
			holdObject = object;
		}
	}
}
