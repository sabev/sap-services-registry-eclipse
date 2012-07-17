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
import java.io.FileNotFoundException;
import java.util.Set;



public interface IWsdlWtpDescriptorContainer
{
	/**
	* Returns the root WSDL, based on which the load process was started.
	* 
	* @return WsdlDefinitionWrapper, which represents the root WSDL.
	*/
	public IWsdlDefinition getRootWsdlDefinition();

	/**
	 * Returns {@link IWsdlDefinition}, which corresponds to a specified file.
	 * 
	 * @param wsdlFile – file, which is stored on the File System and which corresponding {@link IWsdlDefinition} will be returned.
	 * 
	 * @return IWsdlDefinitionWrapper, which has the specified IFile..
	 */
	public IWsdlDefinition getWsdlDefinition(File wsdlFile);

	/**
	* Returns {@link ISchemaDefinition}, which corresponds to a specified IFile.
	* 
	* @param schemaFile – IFile, which is stored on the File System and which corresponding ISchemaDefinitionWrapper will be returned.
	* 
	* @return {@link ISchemaDefinition}, which has the specified IFile.
	*/
	public ISchemaDefinition getSchemaDefinition(File schemaFile);
	
	/**
	 * Renames the file, in which WSDL definition will be saves. Folder in which this file is located will not be changed, only the name.
	 * 
	 * @param wsdlFile – IFile, which represents the IWSDLDefinitionWrapper.
	 * @param newFileName– New file name of the WSDL.
	 * 
	 * @throws NullPointerException – wsdlFile or newFileName is <code>null</code>.
	 * @throws IllegalArgumentException – One of the parameters has incorrect value.
	 * @throws FileNotFoundException
	 */
	public void renameWsdlDefinition(File wsdlFile, String newFileName) throws FileNotFoundException;


	/**
	* Renames the file, in which Schema definition will be saves. Folder in which this file is located will not be changed, only the name.
	* 
	* @param schemaFile – IFile, which represents the ISchemaDefinitionWrapper.
	* @param newFileName– New file name of the Schema.
	* 
	* @throws NullPointerException – schemaFile or newFileName is <code>null</code>.
	* @throws IllegalArgumentException – One of the parameters has incorrect value.
	 * @throws FileNotFoundException
	*/
	public void renameSchemaDefinition(File schemaFile, String newFileName) throws FileNotFoundException;
	
	/**
	 * Returns String representations of all absolute locations, where loaded definitions will be saved.
	 * 
	 * @param target folder - the desired root folder of the download, based on which the other paths will be calculated.
	 * @return Set with File values for all absolute locations.
	 */
	public Set<File> getAllAbsoluteLocations(File targetFolder);
	
	/**
	 * @return the relative to the WSDL location where schemas are located. 
	 */
	public String getSchemaSubFolder();
}
