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
package org.eclipse.servicesregistry.testutils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.platform.discovery.util.internal.logging.Logger;
import org.eclipse.swt.widgets.Display;


/**
 * {@link Thread} extender which is capable of providing event loop processing while the runnable is executed<br>
 * A typical usage of this class should be:
 * 
 * <pre>
 * TestContextThread t = new TestContextThread(someRunnable());
 * t.start();
 * t.block();
 * </pre>
 * 
 * The implementation of this thread resembles to the ModalContextThread with the difference that it does not call {@link Display#sleep()} and is simplified in order to meet test requirements. The reason not to call {@link Display#sleep()} is that under certain conditions putting the display to
 * sleep causes junit tests to wait forever
 * 
 * @author Danail Branekov
 */
class TestContextThread extends Thread
{
	private boolean isRunning;
	private Throwable errorThrowable;
	private final IProgressMonitor monitor;
	private final IRunnableWithProgress runnable;
	private final Display display;

	TestContextThread(final IRunnableWithProgress runnable, final IProgressMonitor monitor, final Display display)
	{
		super();
		ContractChecker.nullCheckParam(runnable, "runnable");
		ContractChecker.nullCheckParam(monitor, "monitor");
		
		isRunning = true;
		this.monitor = monitor;
		this.runnable = runnable;
		this.setName("TestContext");
		this.setDaemon(true);
		this.display = display;
	}

	/**
	 * Retrieves the error throwable which happened during execution
	 * 
	 * @return the error throwable or null if none
	 */
	public Throwable getError()
	{
		return errorThrowable;
	}

	@Override
	public void run()
	{
		isRunning = true;
		try
		{
			runnable.run(monitor);
		} catch (Throwable t)
		{
			errorThrowable = t;
		} finally
		{
			// Make sure that asynchronous events are processed by the event loop via sync execution of an "empty" runnable
			display().syncExec(new Runnable()
			{
				public void run()
				{
				}
			});
			isRunning = false;

			// Force the display to wake up
			display().asyncExec(null);
		}
	}

	/**
	 * Blocks the caller while the {@link Runnable} specified in the constructor is being executed.<br>
	 * If the caller is the UI thread, {@link Display#readAndDispatch()} is invoked in order to ensure that the event loop is processed<br>
	 * If the caller is not a UI thread, this method simply joins. <br>
	 * 
	 * @throws IllegalStateException
	 *             in case the method is invoked within its own thread
	 */
	public void block()
	{
		if (Thread.currentThread() == this)
		{
			throw new IllegalStateException("The method should not be called from inside the same thread");
		}

		if (Display.getCurrent() == null)
		{
			try
			{
				join();
			} catch (Throwable e)
			{
				errorThrowable = e;
			}
		} else
		{
			while (isRunning)
			{
				if(!display().readAndDispatch())
				{
					try
					{
						Thread.sleep(50);
					} catch (InterruptedException e)
					{
						// ignore
						Logger.instance().logDebug(e.getMessage(), e);
					}
				}
			}
		}
	}
	
	private Display display()
	{
		return display;
	}
}
