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
package org.eclipse.servicesregistry.search.ui.test.properties;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.ui.internal.properties.ServiceDefGeneralSection;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.junit.Test;
import org.mockito.Mockito;

public class ServiceDefGeneralSectionTest extends AbstractPropertiesTest<ServiceDefGeneralSection> {
	private static final String ID="id";
	private static final String NAME="name";
	private static final String DOCUMENTATION_URL = "http://documentation/test";
	private static final String WSDL_URL = "http://wsdl/test";
	private static final String PHYSICAL_SYSTEM = "system";
	private static final QName PORT_TYPE = new QName("port_type");
	

	@Test
	public void testId(){
		assertEquals(ID, bot.styledTextWithLabel(SearchUIMessages.ServiceDefinitionId + ":").getText());
	}
	
	@Test
	public void testName(){
		assertEquals(NAME, bot.styledTextWithLabel(SearchUIMessages.ServiceDefinitionName + ":").getText());
	}
	
	@Test
	public void testDocumentationUrl(){
		Widget widget = bot.widget(withText(DOCUMENTATION_URL));
		assertTrue(widget instanceof Hyperlink);
	}
	
	@Test
	public void testWsdlUrl(){
		Widget widget = bot.widget(withText(WSDL_URL));
		assertTrue(widget instanceof Hyperlink);
	}

	@Test
	public void testPortType(){
		assertEquals(PORT_TYPE.toString(), bot.styledTextWithLabel(SearchUIMessages.ServicePorttype+":").getText());
	}
	
	@Test
	public void testPhysicalSystem(){
		assertEquals(PHYSICAL_SYSTEM.toString(), bot.styledTextWithLabel(SearchUIMessages.ServiceDefinitionPhysicalSystem+":").getText());
	}
	
	@Override
	protected Object createInput() {
		IServiceDefinition service = Mockito.mock(IServiceDefinition.class);
		
		Mockito.when(service.getId()).thenReturn(ID);
		Mockito.when(service.getName()).thenReturn(NAME);
		Mockito.when(service.getPorttypeQName()).thenReturn(PORT_TYPE);
		Mockito.when(service.getDocumentationUrl()).thenReturn(DOCUMENTATION_URL);
		Mockito.when(service.getWsdlUrl()).thenReturn(WSDL_URL);
		Mockito.when(service.getPhysicalSystem()).thenReturn(PHYSICAL_SYSTEM);
		
		
		return service;
	}

	@Override
	protected ServiceDefGeneralSection createPropertiesSection() {
		return new ServiceDefGeneralSection();
	}
	
}
