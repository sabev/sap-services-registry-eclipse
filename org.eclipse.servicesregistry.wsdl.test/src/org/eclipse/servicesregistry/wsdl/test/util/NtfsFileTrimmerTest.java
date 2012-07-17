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
package org.eclipse.servicesregistry.wsdl.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;

import org.eclipse.servicesregistry.wsdl.internal.util.FileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.IFileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.NtfsFileTrimmer;
import org.junit.Before;
import org.junit.Test;

public class NtfsFileTrimmerTest{

	private IFileUtils fileUtils = FileUtils.getInstance();
	private MyNtfsFileTrimmer fileTrimmer;

	private final File originalFile = new File(URI.create("file:///C:/work/nwds/runtime-New_configuration/asd/src/rootwsdl_sapws_demo.sap.com_quitea~long~ear~namethis~is_demo.sap.com~quite~a~long~dc~" +
			"name~thisis_EJB_SupplierNaGoshoAndPeshoByNameandGoshoAndPeshoqueryResponse_Sync_SupplierNaGoshoAndPeshoByNameandGoshoAndPeshoqueryResponse_Sync_SAP" +
	"_DEFAULT_PROFILE_Service_MsgAuthOverHTTP_1.wsdl"));

	@Before
	public void setUp()
	{
		fileUtils = FileUtils.getInstance();
		fileTrimmer = new MyNtfsFileTrimmer();
	}
	
	@Test
	public void testTrim() {

		File trimmedFile = fileTrimmer.trim(originalFile);

		assertTrue("Filename length is too long:" + trimmedFile.getName().length() , trimmedFile.getName().length() <= NtfsFileTrimmer.DEFAULT_MAX_FILE_NAME_LENGTH );

		assertOriginalNameAndTrimmedNameHaveSamePrefix(originalFile, trimmedFile);

		assertExtensionIsSame(originalFile, trimmedFile);
	}

	@Test
	public void testTwoTrims() {

		File trimmedFile = fileTrimmer.trim(originalFile);
		String suffix1 = fileTrimmer.getCurrentSuffix();

		File secondTrimmedFile = fileTrimmer.trim(originalFile);
		String suffix2 = fileTrimmer.getCurrentSuffix();

		assertOriginalNameAndTrimmedNameHaveSamePrefix(trimmedFile, secondTrimmedFile);

		assertTrue("unexpected suffix", fileUtils.getFileNameWithoutExtension(trimmedFile).endsWith(suffix1));
		assertTrue("unexpected suffix", fileUtils.getFileNameWithoutExtension(secondTrimmedFile).endsWith(suffix2));

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithIllegalParameter() {
		new NtfsFileTrimmer(256);
	}

	private void assertOriginalNameAndTrimmedNameHaveSamePrefix(
			File originalFile, File trimmedFile) {
		String trimmedFileNameWithoutExtension = fileUtils.getFileNameWithoutExtension(trimmedFile);
		String trimmedFileNameWithoutSuffix = trimmedFileNameWithoutExtension.substring(0, trimmedFile.getName().lastIndexOf(fileTrimmer.getCurrentSuffix()));
		assertTrue(originalFile.getName().startsWith(trimmedFileNameWithoutSuffix));
	}

	private void assertExtensionIsSame(File originalFile, File trimmedFile) {
		assertEquals("Filename extensions differ", fileUtils.getFileExtension(originalFile), fileUtils.getFileExtension(trimmedFile));
	}
	
	private class MyNtfsFileTrimmer extends NtfsFileTrimmer	{
		@Override
		protected String getCurrentSuffix() {
			return super.getCurrentSuffix();
		}
	}
}
