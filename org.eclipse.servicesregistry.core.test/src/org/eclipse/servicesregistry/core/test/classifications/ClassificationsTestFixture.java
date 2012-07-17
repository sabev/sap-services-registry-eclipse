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
package org.eclipse.servicesregistry.core.test.classifications;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemKey;
import org.eclipse.servicesregistry.proxy.types.ListDescription;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

class ClassificationsTestFixture
{
	protected long longZero()
	{
		return (long)0;
	}
	
	protected ClassificationSystem mockSystem(final QName qname, final String systemType)
	{
		return mockSystem(qname, systemType, true);
	}
	
	protected ClassificationSystem mockSystem(final QName qname, final String systemType, boolean hasValues)
	{
		final ClassificationSystem system = new ClassificationSystem();
		system.setType(systemType);
		system.setQname(qname);
		system.setHasValues(hasValues);
		return system;
	}
	
	
	protected Matcher<ClassificationSystemKey> classifSystemKeyMatcher(final ClassificationSystem classifSystem)
	{
		final QName expectedQName = classifSystem.getQname();
		return new BaseMatcher<ClassificationSystemKey>()
		{
			@Override
			public boolean matches(Object item)
			{
				if(item == null)
				{
					return false;
				}
				return expectedQName.equals(((ClassificationSystemKey)item).getQname());
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}
	
	protected ListDescription listDescription(int actualCount, int includedCount)
	{
		final ListDescription descr = new ListDescription();
		descr.setActualCount(actualCount);
		descr.setIncludedCount(includedCount);
		return descr;
	}
}
