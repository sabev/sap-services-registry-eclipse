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
package org.eclipse.servicesregistry.wsdl.internal.wsdlimport;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.internal.util.IFileUtils;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IWsdlArtifactFileNameCalculator;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;

public class WsdlArtifactFileNameCalculator implements IWsdlArtifactFileNameCalculator
{
	private static final String WSDL_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$
	private static final String ROOT_WSDL = "rootwsdl"; //$NON-NLS-1$
	private static final String IMPORTED_SCHEMA = "importedschema"; //$NON-NLS-1$
	private static final String IMPORTED_WSDL = "importedwsdl"; //$NON-NLS-1$

	private int importedWsdlsCounter = 1;
	private int importedSchemasCounter = 1;
	private final IFileUtils fileUtils;

	public WsdlArtifactFileNameCalculator(final IFileUtils fileUtils)
	{
		this.fileUtils = fileUtils;
	}
	
	@Override
	public String proposeReferencedSchemaFileName(String rootWsdlFileName, URL artifactUrl, XSDSchema schema)
	{
		if (fileUtils.isFile(artifactUrl))
		{
			return getFilenameWithoutExtension(artifactUrl);
		}
		return rootWsdlFileName + "_" + IMPORTED_SCHEMA + "_" + Integer.toString(nextSchemaCounter()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String proposeReferencedWsdlFileName(final String rootWsdlFileName, URL artifactUrl, Definition wsdlDef)
	{
		if (fileUtils.isFile(artifactUrl))
		{
			return getFilenameWithoutExtension(artifactUrl);
		}
		return rootWsdlFileName + "_" + IMPORTED_WSDL + "_" + Integer.toString(nextWsdlCounter()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Proposes file name for the root wsdl.<br>
	 * <ul>
	 * <li>If the URL points to a local file, then the file name is returned</li>
	 * <li>If the wsdl:definitions element has a nonempty "name" attribute, the attribute's value is returned</li>
	 * <li>
	 * </ul>
	 * @param artifactUrl - expected to be URI-compliant
	 * @throws IllegalArgumentException if artifactUrl is not URI-compliant
	 */
	@Override
	public String proposeRootWsdlFileName(final URL artifactUrl, final Definition wsdlDefinition)
	{
		if (fileUtils.isFile(artifactUrl))
		{
			return getFilenameWithoutExtension(artifactUrl);
		}

		final String wsdlName = wsdlDefinition.getDocument().getDocumentElement().getAttribute(WSDL_NAME_ATTRIBUTE);
		if (wsdlName != null && !wsdlName.isEmpty())
		{
			return wsdlName;
		}
		return ROOT_WSDL;
	}

	private String getFilenameWithoutExtension(final URL fileUrl) {
		try {
			return fileUtils.getFileNameWithoutExtension(new File( fileUrl.toURI().getPath()) );
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private int nextSchemaCounter()
	{
		return this.importedSchemasCounter++;
	}

	private int nextWsdlCounter()
	{
		return this.importedWsdlsCounter++;
	}
}
