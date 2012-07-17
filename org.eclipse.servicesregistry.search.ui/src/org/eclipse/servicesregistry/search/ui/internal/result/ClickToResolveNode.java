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
package org.eclipse.servicesregistry.search.ui.internal.result;

import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.IResolvable;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.OperationCancelledException;
import org.eclipse.swt.widgets.Display;

/**
 * This class represents an artificial node in the SR search results view which is a child of a node which has not been resolved. The class implements the {@link IResolvable} interface. Thus whenever the user clicks on this artificial node, its {@link #resolve()} method will be invoked. This
 * resolves the node's parent and delegates to the {@link IPostResolveCallback} specified in the constructor
 * 
 * @author Danail Branekov
 * 
 */
public class ClickToResolveNode implements IResolvable
{
	public interface IPostResolveCallback {
		/**
		 * Callback to perform follow up actions after the parent has been resolved
		 * @param resolvableParent the parent already resolved
		 */
		public void postResolve(IResolvable resolvableParent);
	}
	
	private final IResolvable resolvableParent;
	private final IPostResolveCallback postResolveCallback;

	/**
	 * Constructor
	 * @param resolvableParent the unresolved parent 
	 * @param postResolveCallback a callback to be executed after the parent resolvable gets resolved
	 */
	public ClickToResolveNode(final IResolvable resolvableParent, final IPostResolveCallback postResolveCallback)
	{
		this.resolvableParent = resolvableParent;
		this.postResolveCallback = postResolveCallback;
	}

	/**
	 * Always returns <code>false</code>
	 * 
	 * @see super{@link #isResolved()}
	 */
	@Override
	public boolean isResolved()
	{
		return false;
	}

	/**
	 * Resolves the parent and delegates to the post resolve callback. Resolving is usually performed out of the UI thread and therefore viewer update is performed in a {@link Display#asyncExec(Runnable)} block
	 * 
	 * @see super{@link #resolve()}
	 */
	@Override
	public void resolve() throws OperationCancelledException
	{
		this.resolvableParent.resolve();
		this.postResolveCallback.postResolve(resolvableParent);
	}
}
