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
package org.eclipse.servicesregistry.wsdl.test;

import org.eclipse.servicesregistry.wsdl.test.util.FileUtilsTest;
import org.eclipse.servicesregistry.wsdl.test.util.NtfsFileTrimmerTest;
import org.eclipse.servicesregistry.wsdl.test.util.URLUtilsTest;
import org.eclipse.servicesregistry.wsdl.test.wsdl.InaccessibleImportsTest;
import org.eclipse.servicesregistry.wsdl.test.wsdl.UnusedImportsTest;
import org.eclipse.servicesregistry.wsdl.test.wsdl.WsdlDownloadManagerTest;
import org.eclipse.servicesregistry.wsdl.test.wsdl.WsdlImportToolTest;
import org.eclipse.servicesregistry.wsdl.test.wsdl.WsdlImportToolTest2;
import org.eclipse.servicesregistry.wsdl.test.wsdl.WsdlWtpImportToolImplTest;
import org.eclipse.servicesregistry.wsdl.test.wsdl.WsdlWtpStrategyDefaultTest;
import org.eclipse.servicesregistry.wsdl.test.wsdl.endpoint.EndpointWsdlRefactorerTest;
import org.eclipse.servicesregistry.wsdl.test.wsdl.endpoint.sr.EndpointServicesRegistryWsdlRefactorerTest;
import org.eclipse.servicesregistry.wsdl.test.wsdl.headless.HeadlessWsdlImporterFactoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	FileUtilsTest.class,
	URLUtilsTest.class,
	NtfsFileTrimmerTest.class,
	InaccessibleImportsTest.class,
	UnusedImportsTest.class,
	WsdlDownloadManagerTest.class,
	WsdlImportToolTest.class,
	WsdlImportToolTest2.class,
	EndpointWsdlRefactorerTest.class,
	EndpointServicesRegistryWsdlRefactorerTest.class,
	WsdlWtpImportToolImplTest.class,
	WsdlWtpStrategyDefaultTest.class,
	HeadlessWsdlImporterFactoryTest.class
})
@RunWith(Suite.class)
public class AllTestsSuite
{

}
