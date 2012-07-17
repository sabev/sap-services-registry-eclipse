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
package org.eclipse.servicesregistry.core.internal.classifications.finders;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.servicesregistry.core.internal.classifications.ClassificationSystemType;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemList;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemsGreaterThanVersionFault;
import org.eclipse.servicesregistry.proxy.types.ListDescription;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault;

public class ClassificationSystemsFinder implements IClassificationSystemsFinder
{
	private final ServicesRegistrySi srProxy;
	
	public ClassificationSystemsFinder(final ServicesRegistrySi srProxy)
	{
		this.srProxy = srProxy;
	}
	
	@Override
	public Collection<ClassificationSystem> findClassificationSystems() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault
	{
		return findAllAcceptableSystems();
	}
	
	private Collection<ClassificationSystem> findAllAcceptableSystems() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault
	{
		final Collection<ClassificationSystem> result = new LinkedList<ClassificationSystem>();
		for(ClassificationSystem system : findAllClassificationSystems())
		{
			if(acceptClassificationSystem(system))
			{
				result.add(system);
			}
		}
		return result;
	}
	
	private Collection<ClassificationSystem> findAllClassificationSystems() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault
	{
		final Collection<ClassificationSystem> result = new LinkedList<ClassificationSystem>();
		int actualCount = 2;
		int listHead = 1;
		while (listHead <= actualCount)
		{
			final ClassificationSystemList systemsList = srProxy.getClassificationSystemsGreaterThanVersion(0, listHead, Integer.MAX_VALUE);
			final ListDescription listDescription = systemsList.getListDesc();

			result.addAll(systemsList.getClassificationSystems());

			actualCount = listDescription.getActualCount();
			listHead += listDescription.getIncludedCount();
		}

		return result;
	}
	

	private boolean acceptClassificationSystem(final ClassificationSystem system)
	{
		return system.isHasValues() && (ClassificationSystemType.findByType(system.getType()) != ClassificationSystemType.GROUP);
	}
	
}
