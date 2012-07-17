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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.core.internal.classifications.finders.ClassificationSystemsFinder;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemList;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemsGreaterThanVersionFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ClassificationSystemsFinderTest extends ClassificationsTestFixture
{
	@Mock
	private ServicesRegistrySi srProxy;

	private ClassificationSystemsFinder finder;

	private ClassificationSystem flatSystem;
	private ClassificationSystem hierarchicalSystem;
	private ClassificationSystem groupSystem;
	private ClassificationSystem systemWithoutValues;

	@Before
	public void setUp() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault
	{
		MockitoAnnotations.initMocks(this);
		finder = new ClassificationSystemsFinder(srProxy);

		flatSystem = mockSystem(new QName("FLAT"), "FLAT");
		hierarchicalSystem = mockSystem(new QName("HIERARCHICAL"), "HIERARCHY"); // HIERARCHY
		groupSystem = mockSystem(new QName("GROUP"), "GROUP"); // GROUP
		systemWithoutValues = mockSystem(new QName("NOVALUES"), "FLAT", false);

		final ClassificationSystemList firstChunk = new ClassificationSystemList();
		firstChunk.getClassificationSystems().add(flatSystem);
		firstChunk.getClassificationSystems().add(systemWithoutValues);
		firstChunk.setListDesc(listDescription(4, 2));

		final ClassificationSystemList secondChunk = new ClassificationSystemList();
		secondChunk.getClassificationSystems().add(hierarchicalSystem);
		secondChunk.getClassificationSystems().add(groupSystem);
		secondChunk.setListDesc(listDescription(4, 2));

		Mockito.stub(srProxy.getClassificationSystemsGreaterThanVersion(Mockito.eq(longZero()), Mockito.eq(1), Mockito.eq(Integer.MAX_VALUE))).toReturn(firstChunk);
		Mockito.stub(srProxy.getClassificationSystemsGreaterThanVersion(Mockito.eq(longZero()), Mockito.eq(3), Mockito.eq(Integer.MAX_VALUE))).toReturn(secondChunk);
	}

	@Test
	public void testFindClassificationSystems() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault
	{
		final Collection<ClassificationSystem> foundSystems = finder.findClassificationSystems();
		assertEquals("2 classification systems expected", 2, foundSystems.size());
		assertTrue("Flat system not found", foundSystems.contains(flatSystem));
		assertTrue("Hierarchical system not found", foundSystems.contains(hierarchicalSystem));
		assertFalse("Group system unexpectedly returned", foundSystems.contains(groupSystem));

		Mockito.verify(srProxy, Mockito.times(1)).getClassificationSystemsGreaterThanVersion(Mockito.eq(longZero()), Mockito.eq(1), Mockito.eq(Integer.MAX_VALUE));
		Mockito.verify(srProxy, Mockito.times(1)).getClassificationSystemsGreaterThanVersion(Mockito.eq(longZero()), Mockito.eq(3), Mockito.eq(Integer.MAX_VALUE));
		Mockito.verifyNoMoreInteractions(srProxy);
	}
}
