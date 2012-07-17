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
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifactReference;

/**
 * Abstract implementation of the {@link IWsdlArtifact} interface
 */
abstract class WsdlArtifact<T extends EObject> implements IWsdlArtifact<T>
{
	private File file;
	private URL artifactUrl;
	private String defaultFileName;
	private final ARTIFACT_TYPE type;
	private final Collection<IWsdlArtifactReference> references;

	/**
	 * Constructor
	 * @param artifactFile the artifact file
	 * @param artifactUrl the artifact URL
	 * @param defaultFileName the default name of the artifact file
	 * @param type the artifact type
	 */
	WsdlArtifact(final File artifactFile, final URL artifactUrl, final String defaultFileName, final ARTIFACT_TYPE type)
	{
		this.file = artifactFile;
		this.artifactUrl = artifactUrl;
		this.defaultFileName = defaultFileName;
		this.type = type;
		this.references = new LinkedList<IWsdlArtifactReference>();
	}
	
	public URL getOriginalLocation()
	{
		return this.artifactUrl;
	}

	public File getFile()
	{
		return this.file;
	}

	void setFile(File schemaFile)
	{
		this.file = schemaFile;
	}

	@Override
	public String getDefaultFileName()
	{
		return this.defaultFileName;
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null || !this.getClass().equals(obj.getClass()))
		{
			return false;
		}
		@SuppressWarnings("rawtypes")
		final WsdlArtifact wrapper = (WsdlArtifact) obj;
		return this.file.equals(wrapper.getFile()) && this.getEObject().equals(wrapper.getEObject());
	}
	
	@Override
	public int hashCode()
	{
		return this.file.hashCode()*31 + this.getEObject().hashCode();
	}

	@Override
	public ARTIFACT_TYPE getType()
	{
		return this.type;
	}
	
	@Override
	public Collection<IWsdlArtifactReference> references()
	{
		return this.references;
	}
}
