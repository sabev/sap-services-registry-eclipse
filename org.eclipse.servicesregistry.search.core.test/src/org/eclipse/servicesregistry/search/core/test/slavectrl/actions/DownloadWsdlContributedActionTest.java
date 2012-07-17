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
package org.eclipse.servicesregistry.search.core.test.slavectrl.actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.platform.discovery.util.api.env.IErrorHandler;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.platform.discovery.util.internal.StatusUtils;
import org.eclipse.platform.discovery.util.internal.longop.CurrentThreadOperationRunner;
import org.eclipse.servicesregistry.core.internal.logging.ILogger;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.DownloadWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.IActionConfiguration;
import org.eclipse.servicesregistry.testutils.TestFileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.FileUtils;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DownloadWsdlContributedActionTest
{
	@Mock
	private IErrorHandler errHandler;
	@Mock
	private IExistingFilesProcessor existingFilesProcessor;
	@Mock
	private ILogger logger;
	@Mock
	private IActionConfiguration actionConfig;
	
	private File targetDir;
	private ILongOperationRunner opRunner;
	private File wsdlFile;
	private DownloadWsdlContributedAction action;
	private String calculatedWsdlFileName;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		wsdlFile = new TestFileUtils().copyToTempLocation(this.getClass(), "morse.wsdl", "morse" + System.currentTimeMillis() + ".wsdl");

		targetDir = new File((new FileUtils()).systemTempDir(), "target" + System.currentTimeMillis());
		assertTrue("Target dir was not created", targetDir.mkdirs());
		
		opRunner = new CurrentThreadOperationRunner(new NullProgressMonitor());
		calculatedWsdlFileName = "TMorseSoap";

		Mockito.stub(actionConfig.wsdlUrl()).toReturn(wsdlFile.toURI().toURL().toExternalForm());
		Mockito.stub(actionConfig.errorHandler()).toReturn(errHandler);
		Mockito.stub(actionConfig.existingFilesProcessor()).toReturn(existingFilesProcessor);
		Mockito.stub(actionConfig.saveDestination()).toReturn(targetDir);
		Mockito.stub(actionConfig.rootWsdlFileName()).toReturn("TMorseSoap");
		
		action = new DownloadWsdlContributedAction()
		{
			@Override
			protected ILogger logger()
			{
				return logger;
			}
		};
	}

	@After
	public void tearDown() throws Exception
	{
		new TestFileUtils().deleteDirectory(targetDir);
	}

	@Test
	public void testPerformAction()
	{
		final File expectedWsdlFile = new File(targetDir, calculatedWsdlFileName + ".wsdl");
		assertFalse("Expected file already exists", expectedWsdlFile.exists());
		
		action.perform(opRunner, actionConfigSet());
		Mockito.verify(actionConfig, Mockito.times(1)).wsdlDownloaded(Mockito.argThat(wsdlContainerExpectation(expectedWsdlFile)), Mockito.eq(opRunner));
		assertTrue("Wsdl file was not downloaded to the file system", expectedWsdlFile.exists());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPerformAction_EditCancelled() throws IOException
	{
		final File expectedWsdlFile = new File(targetDir, calculatedWsdlFileName + ".wsdl");
		//create file in order for existing files processor to be called
		assertTrue(expectedWsdlFile.createNewFile());

		Mockito.stub(existingFilesProcessor.process(Mockito.anySet())).toReturn(StatusUtils.statusCancel("TEST Cancellation"));
		action.perform(opRunner, actionConfigSet());
		
		Mockito.verify(existingFilesProcessor, Mockito.times(1)).process(Mockito.argThat(setWithElements(expectedWsdlFile)));
		assertNotDownloaded(expectedWsdlFile);
	}

	private void assertNotDownloaded(final File expectedWsdlFile) throws IOException {
		assertTrue("Wsdl file was downloaded to the file system even though writing was cancelled", new TestFileUtils().readFileContents(expectedWsdlFile).isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPerformAction_EditFailed() throws IOException
	{
		final File expectedWsdlFile = new File(targetDir, calculatedWsdlFileName + ".wsdl");
		//create file in order for existing files processor to be called
		assertTrue(expectedWsdlFile.createNewFile());
		
		Mockito.stub(existingFilesProcessor.process(Mockito.anySet())).toReturn(StatusUtils.statusError("TEST Error"));
		action.perform(opRunner, actionConfigSet());
		assertNotDownloaded(expectedWsdlFile);
		Mockito.verify(existingFilesProcessor, Mockito.times(1)).process(Mockito.argThat(setWithElements(expectedWsdlFile)));
		Mockito.verify(logger, Mockito.times(1)).logError(Mockito.anyString(), Mockito.any(Throwable.class));
		Mockito.verify(errHandler, Mockito.times(1)).showError(Mockito.anyString(), Mockito.anyString());
	}
	
	private Matcher<IWsdlWtpDescriptorContainer> wsdlContainerExpectation(final File expectedWsdlFile)
	{
		return new BaseMatcher<IWsdlWtpDescriptorContainer>()
		{

			@Override
			public boolean matches(Object item)
			{
				final IWsdlWtpDescriptorContainer container = (IWsdlWtpDescriptorContainer) item;
				return container.getRootWsdlDefinition().getFile().equals(expectedWsdlFile);
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}
	
	private Set<Object> actionConfigSet()
	{
		final Set<Object> result = new HashSet<Object>();
		result.add(actionConfig);
		return result;
	}
	
	private <T> Matcher<Set<T>> setWithElements(final T... expectedElements)
	{
		return new BaseMatcher<Set<T>>()
		{

			@Override
			public boolean matches(Object item)
			{
				@SuppressWarnings("unchecked")
				final Set<T> set = (Set<T>)item;
				return set.size() == expectedElements.length && set.containsAll(Arrays.asList(expectedElements));
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}
}
