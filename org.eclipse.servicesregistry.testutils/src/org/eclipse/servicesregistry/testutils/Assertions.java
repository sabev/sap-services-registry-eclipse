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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import junit.framework.Assert;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;


/**
 * Utility class which provides methods which assert that a given condition is met. If necessary, methods will block the execution of the caller until
 * the condition is met for a reasonable time. If the conditions is not met, <code>Assert.fail()</code> is invoked
 * 
 * @author Danail Branekov
 * 
 */
public class Assertions
{
	private final static int MAX_CONDITION_CHECKS = 5000;

	private final static int SLEEP_TIMEOUT = 42;

	/**
	 * Asserts that waits some time until the condition specified is met. If the condition is not met after some reasonable time, the method calls Assert.fail()  
	 * @param condition the condition to check
	 * @param failMessage the message which will be passed to <code>Assert.fail()</code> thus indicating a test failure
	 */
	public static void waitAssert(final IWaitCondition condition, final String failMessage)
	{
		try
		{
			if (!waitForConditionLoop(condition))
			{
				Assert.fail(failMessage);
			}
		} catch (ConditionCheckException e)
		{
			failUponThrowable(e);
		}
	}

	/**
	 * Blocks the execution of the caller until the <code>condition</code> is met or MAX_CONDITION_CHECKS attempts have been reached.<br>
	 * The method behaviour slightly depends on whether the method is invoked from the UI thread or not
	 * <li>If the caller is the UI thread, a new modal context thread is started which sleeps for {@link #SLEEP_TIMEOUT}
	 * <li>If the caller is not the UI thread, then the current thread sleeps for {@link #SLEEP_TIMEOUT} <br>
	 * 
	 * @param condition
	 *            condition on which the method will return if met
	 * @throws ConditionCheckException
	 */
	private static boolean waitForConditionLoop(final IWaitCondition condition) throws ConditionCheckException
	{
		int attempts = 0;
		while (attempts < MAX_CONDITION_CHECKS)
		{
			if (condition.checkCondition())
			{
				return true;
			}

			attempts++;
			final IRunnableWithProgress sleepRunnable = getSleepRunnable();

			try
			{
				TestContext.run(sleepRunnable, Display.getCurrent() != null, new NullProgressMonitor(), PlatformUI.getWorkbench().getDisplay());
			} catch (InvocationTargetException e)
			{
				throw new ConditionCheckException(e);
			} catch (InterruptedException e)
			{
				throw new ConditionCheckException(e);
			}
		}

		return false;
	}

	private static IRunnableWithProgress getSleepRunnable()
	{
		return new IRunnableWithProgress()
		{
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
			{
				Thread.sleep(SLEEP_TIMEOUT);
			}
		};
	}

	private static void failUponThrowable(final Throwable t)
	{
		final StringBuilder msgBuilder = new StringBuilder(t.getMessage());
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		t.printStackTrace(printWriter);
		msgBuilder.append(stringWriter.toString());
		msgBuilder.append("------------------------------------\n\n");

		Assert.fail(msgBuilder.toString());
	}

}
