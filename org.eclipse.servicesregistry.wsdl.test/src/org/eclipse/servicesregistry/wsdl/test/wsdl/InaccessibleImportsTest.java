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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlWtpImportToolFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.junit.Test;

/** 
 * Imported, included and redefined schemas in a wsdl, having an inaccessible "schemaLocation" attribute, should fail the download.
 * @author Dimitar Georgiev
 *
 */
public class InaccessibleImportsTest extends ImportsTestFixture{
	
	@Test
	public void testWithImport() throws WsdlStrategyException {
		testDownloadOfWsdlWithInaccessibleSchema(getWsdlWithUnusedAndInaccessibleImport());
	}
	
	@Test
	public void testWithInclude() throws WsdlStrategyException {
		testDownloadOfWsdlWithInaccessibleSchema(getWsdlWithUnusedAndInaccessibleInclude());
	}

	@Test
	public void testWithRedefine() throws WsdlStrategyException {
		testDownloadOfWsdlWithInaccessibleSchema(getWsdlWithUnusedAndInaccessibleRedefine());
	}
	
	private void testDownloadOfWsdlWithInaccessibleSchema(URL wsdlUrl) throws WsdlStrategyException {
		try{
			importTool = WsdlWtpImportToolFactory.createWsdlWtpImportTool(wsdlUrl);
			importTool.downloadWsdls(getDownloadFolder());
			fail("IOException expected, referenced schema is inexisting");
		}catch (IOException e) {
			//expected
		}
	}
}
