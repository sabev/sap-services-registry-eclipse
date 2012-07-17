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
package org.eclipse.servicesregistry.wsdl.wsdlimport.model;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;

/**
 * Interface for WSDL artifacts
 * 
 * <T> the class of the EMF resource associated with this artifact
 * @author Danail Branekov
 */
public interface IWsdlArtifact<T extends EObject>
{
	/**
	 * Enumeration for possible WSDL artifact types
	 */
	public enum ARTIFACT_TYPE
	{
		ROOT_WSDL, REFERENCED_WSDL, REFERENCED_SCHEMA;
	}

	/**
	 * Returns the location, from where this artifact is loaded.
	 * 
	 * @return URL for the original Schema location..
	 */
	public URL getOriginalLocation();

	/**
	 * Returns the location, where the artifact will be saved on the File system.
	 * 
	 * @return File, which represents where the artifact will be saved.
	 */
	public File getFile();

	/**
	 * Gets the default file name
	 */
	public String getDefaultFileName();

	/**
	 * Retrieves the type of this artifact in the complete WSDL document
	 */
	public ARTIFACT_TYPE getType();

	/**
	 * Retrieves a collection of references to other artifacts<br>
	 * For example, in case the artifact represents a XSD schema, then the collection represents all its import/include statements
	 * 
	 * @return a modifiable collection of references
	 */
	public Collection<IWsdlArtifactReference> references();
	
	/**
	 * Retrieves the EMF object associated with this artifact
	 */
	public T getEObject();
}
