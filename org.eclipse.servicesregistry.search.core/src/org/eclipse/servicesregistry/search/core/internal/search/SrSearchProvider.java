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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.platform.discovery.runtime.api.GroupingHierarchy;
import org.eclipse.platform.discovery.runtime.api.ISearchDestination;
import org.eclipse.platform.discovery.runtime.api.ISearchParameters;
import org.eclipse.platform.discovery.runtime.api.ISearchProvider;
import org.eclipse.platform.discovery.runtime.api.ISearchQuery;
import org.eclipse.platform.discovery.runtime.api.ISearchSubdestination;
import org.eclipse.servicesregistry.core.ISrApi;
import org.eclipse.servicesregistry.core.SrApiFactory;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.logging.Logger;
import org.eclipse.servicesregistry.search.core.destinations.IServicesRegistryDestination;

public class SrSearchProvider implements ISearchProvider
{
	public static final String SR_SEARCH_PROVIDER_ID = "org.eclipse.servicesregistry.search.core.sr.searchprovider";
	private static final String CLASSIFICATION_VALUES_PROPERTY_NAME = "CLASSIFICATION_VALUES_SELECTION"; //$NON-NLS-1$
	
	
	@Override
	public ISearchQuery createQuery(final ISearchParameters searchParameters)
	{
		final IServicesRegistrySystem srSystem = ((IServicesRegistryDestination) searchParameters.getSearchDestination()).servicesRegistrySystem();
		final String keyword = searchParameters.getKeywordString();
		return new FindServiceDefinitionsQuery(keyword, classifications(searchParameters), createSrApi(srSystem), Logger.instance());
	}

	@SuppressWarnings("unchecked")
	private Collection<IClassificationValueNode> classifications(final ISearchParameters searchParameters)
	{
		final Object classifications = searchParameters.getCustomParameters().get(CLASSIFICATION_VALUES_PROPERTY_NAME);
		return (Collection<IClassificationValueNode>) (classifications == null ? Collections.emptyList() : classifications);
	}

	@Override
	public Set<GroupingHierarchy> getGroupingHierarchies(final ISearchDestination searchDestination, final Set<ISearchSubdestination> subdestinations)
	{
		return new HashSet<GroupingHierarchy>();
	}
	
	protected ISrApi createSrApi(final IServicesRegistrySystem srSystem)
	{
		return new SrApiFactory().create(srSystem);
	}
}
