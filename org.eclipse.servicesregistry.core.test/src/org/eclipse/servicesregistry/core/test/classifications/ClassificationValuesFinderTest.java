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
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.core.internal.classifications.finders.ClassificationValuesFinder;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValue;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValueList;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemValuesFault;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ClassificationValuesFinderTest extends ClassificationsTestFixture
{
	@Mock
	private ServicesRegistrySi srProxy;
	
	private ClassificationSystem classificationSystem;
	private ClassificationValuesFinder finder;
	private ClassificationSystemValue firstValue;
	private ClassificationSystemValue secondValue;
	
	@Before
	public void setUp() throws GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault
	{
		MockitoAnnotations.initMocks(this);
		classificationSystem = mockSystem(new QName("MySystem"), "0");
		finder = new ClassificationValuesFinder(srProxy);
		
		firstValue = new ClassificationSystemValue();
		secondValue = new ClassificationSystemValue();
		
		final ClassificationSystemValueList firstChunk = new ClassificationSystemValueList();
		firstChunk.setListDescription(listDescription(2, 1));
		firstChunk.getClassificationSystemValue().add(firstValue);
		
		final ClassificationSystemValueList secondChunk = new ClassificationSystemValueList();
		secondChunk.setListDescription(listDescription(2, 1));
		secondChunk.getClassificationSystemValue().add(secondValue);
		
		Mockito.stub(srProxy.getClassificationSystemValues(Mockito.argThat(classifSystemKeyMatcher(classificationSystem)), Mockito.eq(longZero()), Mockito.eq(1), Mockito.eq(Integer.MAX_VALUE))).toReturn(firstChunk);
		Mockito.stub(srProxy.getClassificationSystemValues(Mockito.argThat(classifSystemKeyMatcher(classificationSystem)), Mockito.eq(longZero()), Mockito.eq(2), Mockito.eq(Integer.MAX_VALUE))).toReturn(secondChunk);
	}
	
	@Test
	public void testFindValuesForSystemWithoutValues() throws GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault
	{
		classificationSystem.setHasValues(false);
		assertTrue("No values expected for classification without values", finder.findValues(classificationSystem).isEmpty());
		Mockito.verifyNoMoreInteractions(srProxy);
	}
	
	@Test
	public void testFindClassificationValues() throws GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault
	{
		classificationSystem.setHasValues(true);
		final Collection<ClassificationSystemValue> foundValues = finder.findValues(classificationSystem);
		
		assertEquals("2 values expected", 2, foundValues.size());
		assertTrue("First value not found", foundValues.contains(firstValue));
		assertTrue("Second value not found", foundValues.contains(secondValue));
		
		Mockito.verify(srProxy, Mockito.times(1)).getClassificationSystemValues(Mockito.argThat(classifSystemKeyMatcher(classificationSystem)), Mockito.eq(longZero()), Mockito.eq(1), Mockito.eq(Integer.MAX_VALUE));
		Mockito.verify(srProxy, Mockito.times(1)).getClassificationSystemValues(Mockito.argThat(classifSystemKeyMatcher(classificationSystem)), Mockito.eq(longZero()), Mockito.eq(2), Mockito.eq(Integer.MAX_VALUE));
		Mockito.verifyNoMoreInteractions(srProxy);
	}
}
