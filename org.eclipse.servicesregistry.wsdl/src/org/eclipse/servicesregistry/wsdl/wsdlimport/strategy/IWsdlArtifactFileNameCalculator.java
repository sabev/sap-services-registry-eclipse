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
package org.eclipse.servicesregistry.wsdl.wsdlimport.strategy;

import java.net.URL;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;

public interface IWsdlArtifactFileNameCalculator
{
	/**
	 * Proposes file name for the root WSDL artifact specified by the URL. The result is just the file name, without the extension
	 * 
	 * @param artifactUrl
	 *            the URL (must be a valid URI)
	 * @param wsdlDefinition
	 *            the WSDL definition
	 * @return proposal for file name
	 */
	public String proposeRootWsdlFileName(final URL artifactUrl, final Definition wsdlDefinition);
	
	/**
	 * Proposes file name for the referenced WSDL artifact specified by the URL. The result is just the file name, without the extension
	 * 
	 * @param artifactUrl
	 *            the URL  of the referenced wsdl artifact (must be a valid URI)
	 * @param rootWsdlFileName
	 *            the file name (without extension) of the root wsdl
	 * @param wsdlDefinition the loaded definition of the referenced wsdl
	 * @return proposal for file name
	 */
	public String proposeReferencedWsdlFileName(final String rootWsdlFileName, final URL artifactUrl, final Definition wsdlDefinition);
	
	/**
	 * Proposes file name for the referenced schema artifact. The result is just the file name, without the extension
	 * 
	 * @param rootWsdlFileName
	 *            the file name (without extension) of the root wsdl
	 *            
	 * @param artifactUrl the url of the referenced schema artifact
	 * @param schema the loaded xsd schema
	 * @return proposal for file name
	 */
	public String proposeReferencedSchemaFileName(final String rootWsdlFileName, final URL artifactUrl, final XSDSchema schema);
}
