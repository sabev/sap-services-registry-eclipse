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

import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.properties.EndpointGeneralSection;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.junit.Test;
import org.mockito.Mockito;

public class EndpointGeneralSectionTest extends AbstractPropertiesTest<EndpointGeneralSection> {
	static final String END_POINT_NAME="endpoint_name";
	static final String BINDING_URL="http://endpoint/binding";
	static final String ADDRESS = "http://endpoint/address";
	static final QName BINDING_QNAME = new QName("binding");
	static final QName SERVICE_QNAME = new QName("service");
	

	@Test
	public void testEndpointName(){
		assertEquals(END_POINT_NAME, bot.styledTextWithLabel(SearchUIMessages.EndpointName+":").getText());
	}
	
	@Test
	public void testBindingUrl(){
		Widget widget = bot.widget(withText(BINDING_URL));
		assertTrue(widget instanceof Hyperlink);
	}
	
	@Test
	public void testAddress(){
		Widget widget = bot.widget(withText(ADDRESS));
		assertTrue(widget instanceof Hyperlink);
	}

	@Test
	public void testBindingQName(){
		assertEquals(BINDING_QNAME.toString(), bot.styledTextWithLabel(SearchUIMessages.EndpointBinding+":").getText());
	}
	
	@Test
	public void testServiceQName(){
		assertEquals(SERVICE_QNAME.toString(), bot.styledTextWithLabel(SearchUIMessages.EndpointService+":").getText());
	}
	
	
	@Override
	protected Object createInput() {
		IServiceEndpoint serviceEndpoint = Mockito.mock(IServiceEndpoint.class);
		
		Mockito.when(serviceEndpoint.getEndpointName()).thenReturn(END_POINT_NAME);
		Mockito.when(serviceEndpoint.getBindingWsdlUrl()).thenReturn(BINDING_URL);
		Mockito.when(serviceEndpoint.getEndpointAddress()).thenReturn(ADDRESS);
		Mockito.when(serviceEndpoint.getBindingQName()).thenReturn(BINDING_QNAME);
		Mockito.when(serviceEndpoint.getServiceQName()).thenReturn(SERVICE_QNAME);
		
		return serviceEndpoint;
	}

	@Override
	protected EndpointGeneralSection createPropertiesSection() {
		return new EndpointGeneralSection();
	}
	
}
