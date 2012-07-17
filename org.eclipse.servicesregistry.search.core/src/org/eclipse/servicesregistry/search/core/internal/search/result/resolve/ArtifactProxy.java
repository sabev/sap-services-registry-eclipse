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
package org.eclipse.servicesregistry.search.core.internal.search.result.resolve;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.platform.discovery.util.api.longop.ILongOperation;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.platform.discovery.util.api.longop.LongOpCanceledException;
import org.eclipse.servicesregistry.search.core.internal.plugin.text.SearchCoreMessages;

public abstract class ArtifactProxy<T> implements IResolvable
{
	/**
	 * Enumeration for possible resolve states
	 * 
	 * @author Danail Branekov
	 */
	private enum RESOLVE_STATE
	{
		RESOLVED, UNRESOLVED, RESOLVING;
	}
	
	public interface OnDemandArtifactQuery<T>
	{
		public T resolve(final T artifactToResolve, final IProgressMonitor monitor) throws LongOpCanceledException;
	}

	private RESOLVE_STATE resolveState;
	private T resolvedDelegate;
	private final ILongOperationRunner operationRunner;
	private OnDemandArtifactQuery<T> resolver;

	public ArtifactProxy(final OnDemandArtifactQuery<T> resolver, final ILongOperationRunner operationRunner)
	{
		this.resolveState = RESOLVE_STATE.UNRESOLVED;
		this.operationRunner = operationRunner;
		this.resolver = resolver;
	}

	public T create(final Class<T> proxyInterface, final ClassLoader cl)
	{
		final InvocationHandler refIH = new ProxyInvocationHandler();
		@SuppressWarnings("unchecked")
		final T ret = (T) Proxy.newProxyInstance(cl, new Class[] { proxyInterface, IResolvable.class }, refIH);
		return ret;
	}

	protected ILongOperationRunner operationRunner()
	{
		return this.operationRunner;
	}

	public void resolve() throws OperationCancelledException
	{
		if (isResolving())
		{
			waitForResolved();
		}

		if (isResolved())
		{
			return;
		}

		final ILongOperation<Void> resolveOperation = new ILongOperation<Void>()
		{
			@Override
			public Void run(IProgressMonitor monitor) throws LongOpCanceledException, Exception
			{
				monitor.beginTask(SearchCoreMessages.RESOLVING, IProgressMonitor.UNKNOWN);
				try
				{
					resolvedDelegate = resolver.resolve(artifact(), monitor);
					synchronized (ArtifactProxy.this)
					{
						resolveState = RESOLVE_STATE.RESOLVED;
					}
					resolveCompleted();
					return null;
				} finally
				{
					monitor.done();
				}
			}
		};
		try
		{
			synchronized (this)
			{
				resolveState = RESOLVE_STATE.RESOLVING;
			}
			operationRunner().run(resolveOperation);

		} catch (LongOpCanceledException e)
		{
			synchronized (this)
			{
				resolveState = RESOLVE_STATE.UNRESOLVED;
			}
			throw new OperationCancelledException(e);
		} catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Resolve has been performed. Clean up resources
	 */
	protected void resolveCompleted()
	{
		resolver = null;
	}

	private void waitForResolved()
	{
		final ILongOperation<Void> waitOperation = new ILongOperation<Void>()
		{
			@Override
			public Void run(IProgressMonitor monitor) throws LongOpCanceledException, Exception
			{
				return null;
			}
		};
		try
		{
			ArtifactProxy.this.operationRunner().run(waitOperation);
		} catch (LongOpCanceledException e)
		{
			throw new RuntimeException(e);
		} catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized boolean isResolved()
	{
		return resolveState == RESOLVE_STATE.RESOLVED;
	}

	private synchronized boolean isResolving()
	{
		return resolveState == RESOLVE_STATE.RESOLVING;
	}

	/**
	 * Retrieves the artifact to be resolved
	 * 
	 * @return
	 */
	protected abstract T artifact();

	private class ProxyInvocationHandler implements InvocationHandler
	{

		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
		{
			if (method.getName().equals("isResolved")) //$NON-NLS-1$
			{
				return isResolved();
			}

			if (!isResolved())
			{
				try
				{
					return unresolvedInvocation(proxy, method, args);
				} catch (UnresolvedInvocationImpossibleException e)
				{
					// $JL-EXC$
					// The method invocation cannot be performed before the artifact is resolved. Continue with resolution
				}
			}

			resolve();

			if (method.getName().equals("resolve")) //$NON-NLS-1$
			{
				return null;
			} else
			{
				return method.invoke(resolvedDelegate, args);
			}
		}
	}

	/**
	 * Extenders should implement this method to dispatch invocations to unresolved artifact. If the invocation is not possible without resolution, {@link UnresolvedInvocationImpossibleException} must be thrown
	 */
	protected abstract Object unresolvedInvocation(final Object proxy, final Method method, final Object[] args) throws UnresolvedInvocationImpossibleException;
}
