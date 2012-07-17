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
package org.eclipse.servicesregistry.wsdl.test.wsdl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.wsdl.Service;
import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.wsdl.internal.util.FileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.IFileUtils;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlWtpStrategyDefault;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.ISchemaDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifactReference;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.wst.wsdl.Definition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class WsdlWtpStrategyDefaultTest
{
	private IWsdlWtpDescriptorContainer wsdlContainer;
	private WsdlWtpStrategyDefault strategy;
	private IFileUtils fileUtils;
	private IWsdlDefinition rootWsdl;
	private Definition rootWsdlDefinition;
	private static final String ROOT_WSDL_SERVICE_NAME = "RootService";
	
	private IWsdlDefinition referencedWsdl;
	private Definition referencedWsdlDefinition;
	private static final String REFERENCED_WSDL_SERVICE_NAME = "ReferencedService";
	
	private ISchemaDefinition referencedSchema;
	private URL rootWsdlUrl;
	private URL referencedWsdlUrl;
	private URL referencedSchemaUrl;

	@Before
	public void setUp() throws Exception
	{
		setupUrls();
		setupWsdlArtifacts();
		setupWsdlContainer();
		setupFileUtils();
		strategy = new WsdlWtpStrategyDefault()
		{
			protected IFileUtils fileUtils()
			{
				return fileUtils;
			}
		};
	}
	
	private void setupFileUtils()
	{
		fileUtils = Mockito.mock(IFileUtils.class);
		Mockito.stub(fileUtils.getFileNameWithoutExtension(Mockito.any(File.class))).toAnswer(new Answer<String>()
		{
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable
			{
				return (new FileUtils()).getFileNameWithoutExtension((File)invocation.getArguments()[0]);
			}
		});
		Mockito.stub(fileUtils.getFileExtension(Mockito.isA(File.class))).toAnswer(new Answer<String>()
		{
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable
			{
				return (new FileUtils()).getFileExtension((File)invocation.getArguments()[0]);
			}
		});
	}
	
	private void setupUrls() throws MalformedURLException
	{
		rootWsdlUrl = new URL("http://localhost/rootwsdl");
		referencedWsdlUrl = new URL("http://localhost/referencedWsdl");
		referencedSchemaUrl = new URL("http://localhost/referencedSchema");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setupWsdlArtifacts()
	{
		rootWsdl = Mockito.mock(IWsdlDefinition.class, "rootWsdl");
		Mockito.stub(rootWsdl.getOriginalLocation()).toReturn(rootWsdlUrl);
		Mockito.stub(rootWsdl.getFile()).toReturn(new File("rootwsdl.wsdl"));
		Mockito.stub(rootWsdl.getDefaultFileName()).toReturn("rootwsdl.wsdl");
		
		rootWsdlDefinition = Mockito.mock(Definition.class);
		setupDefinitionServices(rootWsdlDefinition, ROOT_WSDL_SERVICE_NAME);
		Mockito.stub(rootWsdl.getEObject()).toReturn(rootWsdlDefinition);
		Mockito.stub(rootWsdl.getType()).toReturn(IWsdlArtifact.ARTIFACT_TYPE.ROOT_WSDL);
		
		referencedWsdl = Mockito.mock(IWsdlDefinition.class, "referencedWsdl");
		Mockito.stub(referencedWsdl.getOriginalLocation()).toReturn(referencedWsdlUrl);
		Mockito.stub(referencedWsdl.getFile()).toReturn(new File("referencedWsdl.wsdl"));
		Mockito.stub(referencedWsdl.getDefaultFileName()).toReturn("referencedWsdl.wsdl");
		
		referencedWsdlDefinition = Mockito.mock(Definition.class);
		setupDefinitionServices(referencedWsdlDefinition, REFERENCED_WSDL_SERVICE_NAME);
		Mockito.stub(referencedWsdl.getEObject()).toReturn(referencedWsdlDefinition);
		Mockito.stub(referencedWsdl.getType()).toReturn(IWsdlArtifact.ARTIFACT_TYPE.REFERENCED_WSDL);
		
		referencedSchema = Mockito.mock(ISchemaDefinition.class, "referencedSchema");
		Mockito.stub(referencedSchema.getOriginalLocation()).toReturn(referencedSchemaUrl);
		Mockito.stub(referencedSchema.getFile()).toReturn(new File("referencedSchema.xsd"));
		Mockito.stub(referencedSchema.getDefaultFileName()).toReturn("referencedSchema.xsd");
		Mockito.stub(referencedSchema.getType()).toReturn(IWsdlArtifact.ARTIFACT_TYPE.REFERENCED_SCHEMA);
		
		
		final IWsdlArtifactReference rootToWsdlRef = Mockito.mock(IWsdlArtifactReference.class);
		Mockito.stub(rootToWsdlRef.referencedArtifact()).toReturn((IWsdlArtifact) referencedWsdl);

		Mockito.stub(rootWsdl.references()).toReturn(Arrays.asList(new IWsdlArtifactReference[]{rootToWsdlRef}));
		
		final IWsdlArtifactReference wsdlToSchemaRef = Mockito.mock(IWsdlArtifactReference.class);
		Mockito.stub(wsdlToSchemaRef.referencedArtifact()).toReturn((IWsdlArtifact) referencedSchema);

		Mockito.stub(referencedWsdl.references()).toReturn(Arrays.asList(new IWsdlArtifactReference[]{wsdlToSchemaRef}));
		
		Mockito.stub(referencedSchema.references()).toReturn(new ArrayList<IWsdlArtifactReference>());
	}
	
	private void setupDefinitionServices(final Definition def, final String... serviceNames)
	{
		final Map<QName, Service> servicesMap = new HashMap<QName, Service>();
		for(String sName : serviceNames)
		{
			servicesMap.put(new QName(sName), Mockito.mock(Service.class));
		}
		
		Mockito.stub(def.getServices()).toReturn(servicesMap);
	}

	private void setupWsdlContainer()
	{
		wsdlContainer = Mockito.mock(IWsdlWtpDescriptorContainer.class);
		Mockito.stub(wsdlContainer.getRootWsdlDefinition()).toReturn(rootWsdl);
	}
	
	@Test
	public void testPostLoadLocalFileUrl() throws WsdlStrategyException
	{
		stubFileUtilsForLocalFileUrl();
		strategy.postLoad(wsdlContainer);
		Mockito.verify(fileUtils, Mockito.atLeastOnce()).isFile(Mockito.eq(rootWsdlUrl));
	}
	
	@Test
	public void testPostLoadRemoteUrl() throws WsdlStrategyException, FileNotFoundException
	{
		stubFileUtilsForRemoteUrl();
		final String suffix = "_" + ROOT_WSDL_SERVICE_NAME + "_" + REFERENCED_WSDL_SERVICE_NAME;
		strategy.postLoad(wsdlContainer);
		
		Mockito.verify(wsdlContainer, Mockito.times(1)).renameWsdlDefinition(Mockito.eq(rootWsdl.getFile()), Mockito.eq("rootwsdl" + suffix + ".wsdl"));
		Mockito.verify(wsdlContainer, Mockito.times(1)).renameWsdlDefinition(Mockito.eq(referencedWsdl.getFile()), Mockito.eq("referencedWsdl" + suffix + ".wsdl"));
		Mockito.verify(wsdlContainer, Mockito.times(1)).renameSchemaDefinition(Mockito.eq(referencedSchema.getFile()), Mockito.eq("referencedSchema" + suffix + ".xsd"));
	}
	
	@Test
	public void testPostLoadWithEscapedCharacters() throws MalformedURLException, WsdlStrategyException, FileNotFoundException
	{
		stubFileUtilsForRemoteUrl();
		setupDefinitionServices(rootWsdlDefinition, new String[0]);
		setupDefinitionServices(referencedWsdlDefinition, new String[0]);
		final URL escapedCharsUrl = new URL("http://host/my%7etest%20%7burl%7d");
		Mockito.stub(rootWsdl.getOriginalLocation()).toReturn(escapedCharsUrl);
		
		final String suffix = "_my~test {url}";
		strategy.postLoad(wsdlContainer);
		
		Mockito.verify(wsdlContainer, Mockito.times(1)).renameWsdlDefinition(Mockito.eq(rootWsdl.getFile()), Mockito.eq("rootwsdl" + suffix + ".wsdl"));
		Mockito.verify(wsdlContainer, Mockito.times(1)).renameWsdlDefinition(Mockito.eq(referencedWsdl.getFile()), Mockito.eq("referencedWsdl" + suffix + ".wsdl"));
		Mockito.verify(wsdlContainer, Mockito.times(1)).renameSchemaDefinition(Mockito.eq(referencedSchema.getFile()), Mockito.eq("referencedSchema" + suffix + ".xsd"));
	}
	
	private void stubFileUtilsForLocalFileUrl()
	{
		Mockito.stub(fileUtils.isFile(Mockito.isA(URL.class))).toReturn(true);
	}
	
	private void stubFileUtilsForRemoteUrl()
	{
		Mockito.stub(fileUtils.isFile(Mockito.isA(URL.class))).toReturn(false);
	}
	
}
