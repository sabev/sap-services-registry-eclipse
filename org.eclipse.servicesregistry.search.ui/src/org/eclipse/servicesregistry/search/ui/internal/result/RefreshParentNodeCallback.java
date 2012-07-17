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

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.IResolvable;
import org.eclipse.servicesregistry.search.ui.internal.result.ClickToResolveNode.IPostResolveCallback;

class RefreshParentNodeCallback implements IPostResolveCallback
{
	private final StructuredViewer viewer;

	RefreshParentNodeCallback(final StructuredViewer viewer)
	{
		this.viewer = viewer;
	}

	@Override
	public void postResolve(final IResolvable resolvableParent)
	{
		final Runnable refreshRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				viewer.refresh(resolvableParent, true);
			}
		};
		executeRunnableInUi(refreshRunnable);
	}

	protected void executeRunnableInUi(final Runnable runnable)
	{
		viewer.getControl().getDisplay().asyncExec(runnable);
	}
}
