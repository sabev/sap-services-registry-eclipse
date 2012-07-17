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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlWtpImportToolImpl;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlWtpImportToolFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.ISchemaDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifactReference;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.junit.Test;

/**
 * Imported, included and redefined schemas in a wsdl should be downloaded even when
 * they are not used inside the wsdl document.
 * See {@link WsdlWtpImportToolImpl}.getResolvedSchema()
 * @author Dimitar Georgiev
 */
public class UnusedImportsTest extends ImportsTestFixture{
	
	@Test
	public void testUnusedImportIsDownloaded() throws IOException, WsdlStrategyException {
		downloadWsdlAndAssertExpectedStructure(getWsdlWithUnusedImport());
	}
	
	private void downloadWsdlAndAssertExpectedStructure(URL testWsdlUrl) throws IOException, WsdlStrategyException {
		importTool = WsdlWtpImportToolFactory.createWsdlWtpImportTool(testWsdlUrl);
		IWsdlWtpDescriptorContainer container = importTool.downloadWsdls(getDownloadFolder());
		assertTrue("Root wsdl does not exist", container.getRootWsdlDefinition().getFile().exists());
		assertEquals("Only one artifact reference expected", 1, container.getRootWsdlDefinition().references().size());
		
		IWsdlArtifactReference reference = container.getRootWsdlDefinition().references().iterator().next();
		IWsdlArtifact<?> referencedArtifact = reference.referencedArtifact();
		assertTrue("Referenced artifact is not a schema, runtime class returned is " + referencedArtifact.getClass(),
				referencedArtifact instanceof ISchemaDefinition);
		assertTrue("Referenced schema does not exist", referencedArtifact.getFile().exists());
	}
	
	@Test
	public void testUnusedIncludeIsDownloaded() throws IOException, WsdlStrategyException {
		downloadWsdlAndAssertExpectedStructure(getWsdlWithUnusedInclude());
	}
	
	@Test
	public void testUnusedRedefineIsDownloaded() throws IOException, WsdlStrategyException {
		downloadWsdlAndAssertExpectedStructure(getWsdlWithUnusedRedefine());
	}
}
