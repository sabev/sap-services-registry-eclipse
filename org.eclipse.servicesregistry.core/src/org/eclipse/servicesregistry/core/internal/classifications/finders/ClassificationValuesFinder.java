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
import java.util.Collections;
import java.util.LinkedList;

import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemKey;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValue;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValueList;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.ListDescription;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemValuesFault;

public class ClassificationValuesFinder implements IClassificationValuesFinder
{
	private final ServicesRegistrySi srProxy;
	
	public ClassificationValuesFinder(final ServicesRegistrySi srProxy)
	{
		this.srProxy = srProxy;
	}

	@Override
	public Collection<ClassificationSystemValue> findValues(final ClassificationSystem classifSystem) throws GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault
	{
		if(!classifSystem.isHasValues())
		{
			return Collections.emptyList();
		}
		
		Collection<ClassificationSystemValue> result = new LinkedList<ClassificationSystemValue>();
		int actualCount = 2;
		int listHead = 1;
		while (listHead <= actualCount)
		{
			final ClassificationSystemKey systemKey = new ClassificationSystemKey();
			systemKey.setQname(classifSystem.getQname());

			final ClassificationSystemValueList valueList = srProxy.getClassificationSystemValues(systemKey, 0, listHead, Integer.MAX_VALUE);
			final ListDescription listDescription = valueList.getListDescription();

			result.addAll(valueList.getClassificationSystemValue());

			actualCount = listDescription.getActualCount();
			listHead += listDescription.getIncludedCount();
		}

		return result;
	}
	
}
