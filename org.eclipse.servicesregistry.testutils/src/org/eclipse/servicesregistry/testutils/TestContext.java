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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.swt.widgets.Display;



/**
 * Utility to execute runnable instances in a separate thread. Implementation is pretty similar to {@link ModalContext} but it uses {@link TestContextThread} to perform forking
 * 
 * @author Danail Branekov
 * 
 */
public class TestContext
{
	/**
	 * Executes the operation specified in the caller or in a {@link TestContextThread} thread depending on the <code>fork</code> value. In case the caller of this method is a {@link TestContextThread} instance, the execution of the <code>operation</code> is performed in the current thread no matter
	 * of the <code>fork</code> value
	 * 
	 * @param operation
	 *            the operation; must not be null
	 * @param fork
	 *            true to perform the <code>operation</code> in a {@link TestContextThread}; false to perform the <code>operation</code> in the current thread
	 * @param monitor
	 *            progress monitor
	 * @param display TODO
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public static void run(final IRunnableWithProgress operation, final boolean fork, final IProgressMonitor monitor, final Display display) throws InvocationTargetException, InterruptedException
	{
		ContractChecker.nullCheckParam(operation, "operation");

		final boolean willFork = !isInTestContextThread() && fork;
		if (willFork)
		{
			runInTestContextThread(operation, monitor, display);
		} else
		{
			runInCurrentThread(operation, monitor);
		}
	}

	/**
	 * Runs the operation in the current thread
	 * 
	 * @param operation
	 *            the operation
	 * @throws InvocationTargetException
	 *             if a {@link Throwable} has been thrown during operation execution. The exception wraps that {@link Throwable}
	 */
	private static void runInCurrentThread(final IRunnableWithProgress operation, final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
	{
		operation.run(monitor);
	}

	/**
	 * Runs the operation in a {@link TestContextThread}.
	 * 
	 * @param runnable
	 *            operation
	 * @throws InvocationTargetException
	 *             if the {@link TestContextThread} finished with error
	 * @see TestContextThread#getError()
	 */
	private static void runInTestContextThread(final IRunnableWithProgress runnable, final IProgressMonitor monitor, final Display display) throws InvocationTargetException
	{
		final TestContextThread contextThread = new TestContextThread(runnable, monitor, display);
		contextThread.start();
		contextThread.block();

		if (contextThread.getError() != null)
		{
			if(contextThread.getError() instanceof InvocationTargetException) {
				throw (InvocationTargetException)contextThread.getError();
			}
			else {
				throw new InvocationTargetException(contextThread.getError());
			}
		}
	}

	/**
	 * Checks whether the current thread is instance of {@link TestContextThread}
	 */
	private static boolean isInTestContextThread()
	{
		final Thread t = Thread.currentThread();
		return t instanceof TestContextThread;
	}
}
