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

import org.eclipse.servicesregistry.wsdl.wsdlimport.model.ISchemaDefinition;
import org.eclipse.xsd.XSDSchema;

class SchemaDefinition extends WsdlArtifact<XSDSchema> implements ISchemaDefinition
{
	private XSDSchema schema;
	
	SchemaDefinition(final XSDSchema schema, final File schemaFile, final URL schemaUrl, final String defaultFileName)
	{
		super(schemaFile, schemaUrl, defaultFileName, ARTIFACT_TYPE.REFERENCED_SCHEMA);
		this.schema = schema;
	}

	@Override
	public XSDSchema getEObject()
	{
		return this.schema;
	}
}
