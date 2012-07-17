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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.servicesregistry.wsdl.walker.CancelWalking;
import org.eclipse.servicesregistry.wsdl.walker.WsdlVisitor;
import org.eclipse.servicesregistry.wsdl.walker.WsdlWalker;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IReferenceDirective;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.ISchemaDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifactReference;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact.ARTIFACT_TYPE;

/**
 * WSDL artifacts container which represents the participants in a WSDL (WSDLs and schemas) and their relations
 * @author Plamen Pavlov, Danail Branekov
 */
class WsdlArtifactsContainer implements IWsdlWtpDescriptorContainer
{
	private final WsdlDefinition rootWsdlDefinitionWrapper;

	private final String schemaSubFolder;

	/**
	 * Constructor
	 * @param rootDefinitionWrapper the root WSDL
	 * @param schemaSubFolder the name of the subfolder into which schemas would be stored
	 */
	public WsdlArtifactsContainer(final WsdlDefinition rootWsdl, final String schemaSubFolder)
	{
		this.rootWsdlDefinitionWrapper = rootWsdl;
		this.schemaSubFolder = schemaSubFolder;
	}

	public WsdlDefinition getRootWsdlDefinition()
	{
		return this.rootWsdlDefinitionWrapper;
	}

	/**
	 * Registers a referenced WSDL artifact in the container
	 * 
	 * @param addedWsdl
	 *            the WSDL to register
	 * @param parentWsdl
	 *            the WSDL which references the registered WSDL
	 * @param refDirective
	 *            the wsdl:import directive via which the WSDL is imported
	 */
	void addWsdlDefinitionWrapper(final IWsdlDefinition addedWsdl, final IWsdlDefinition parentWsdl, final IReferenceDirective refDirective)
	{
		ContractChecker.nullCheckParam(addedWsdl, "wsdlDefinitionWrapper"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(parentWsdl, "parentWsdl"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(refDirective, "refDirective"); //$NON-NLS-1$

		final WsdlImportReference rel = new WsdlImportReference(addedWsdl, refDirective);
		parentWsdl.references().add(rel);
	}

	/**
	 * Registers a referenced schema artifact in the container
	 * 
	 * @param addedSchema
	 *            the schema to be registered
	 * @param parentArtifact
	 *            the artifact which references the schema
	 * @param refDirective
	 *            the directive (xsd:import or xsd:include) via which the schema is referenced
	 */
	void addSchemaDefinitionWrapper(final ISchemaDefinition addedSchema, final IWsdlArtifact<?> parentArtifact, final IReferenceDirective refDirective)
	{
		ContractChecker.nullCheckParam(addedSchema, "addedSchema"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(parentArtifact, "parentArtifact"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(refDirective, "refDirective"); //$NON-NLS-1$

		final XsdReference rel = new XsdReference(addedSchema, refDirective);
		parentArtifact.references().add(rel);
	}

	void save(File targetFolder) throws IOException
	{
		setTargetFolder(targetFolder);
		WsdlWalker<IOException> walker = new WsdlWalker<IOException>(new SaveVisitor());
		walker.walk(rootWsdlDefinitionWrapper);
	}



	private void setTargetFolder(File targetFolder) {
		if(targetFolder.equals(rootWsdlDefinitionWrapper.getFile())) {
			return;
		}
		TargetFolderSetter setter = new TargetFolderSetter(targetFolder);
		WsdlWalker<RuntimeException> walker = new WsdlWalker<RuntimeException>(setter);
		walker.walk(rootWsdlDefinitionWrapper);
	}

	public SchemaDefinition getSchemaDefinition(File schemaFile)
	{
		ArtifactFinder finder = new ArtifactFinder(schemaFile, ARTIFACT_TYPE.REFERENCED_SCHEMA);
		WsdlWalker<RuntimeException> walker = new WsdlWalker<RuntimeException>(finder);
		walker.walk(rootWsdlDefinitionWrapper);
		
		return (SchemaDefinition)finder.getFoundArtifact();
	}

	public WsdlDefinition getWsdlDefinition(File wsdlFile)
	{
		ArtifactFinder finder = new ArtifactFinder(wsdlFile, ARTIFACT_TYPE.ROOT_WSDL, ARTIFACT_TYPE.REFERENCED_WSDL);
		WsdlWalker<RuntimeException> walker = new WsdlWalker<RuntimeException>(finder);
		walker.walk(rootWsdlDefinitionWrapper);
		
		return (WsdlDefinition)finder.getFoundArtifact();
	}

	public void renameSchemaDefinition(File schemaFile, String newFileName) throws FileNotFoundException
	{
		ContractChecker.nullCheckParam(schemaFile, "schemaFile"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(newFileName, "newFileName"); //$NON-NLS-1$
		if (newFileName.equals(""))//$NON-NLS-1$
		{
			throw new IllegalArgumentException("newFileName should not be \"\"."); //$NON-NLS-1$
		}

		final SchemaDefinition schema = getSchemaDefinition(schemaFile);
		if (schema == null)
		{
			throw new FileNotFoundException(MessageFormat.format("Schema for file {0} not found", schemaFile.getPath())); //$NON-NLS-1$
		}
		schema.setFile(new File(schemaFile.getParent(), newFileName));

		ReferencesFinder refFinder = new ReferencesFinder(schema);
		WsdlWalker<RuntimeException> walker = new WsdlWalker<RuntimeException>(refFinder);
		walker.walk(rootWsdlDefinitionWrapper);
		
		for (IWsdlArtifactReference rel : refFinder.foundReferences)
		{
			rel.setLocation(rel.getLocation().replaceAll(schemaFile.getName(), URI.encodeQuery(newFileName, false)));
		}
	}

	public void renameWsdlDefinition(final File wsdlFile, String newFileName) throws FileNotFoundException
	{
		ContractChecker.nullCheckParam(wsdlFile, "wsdlFile"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(newFileName, "newFileName"); //$NON-NLS-1$
		if (newFileName.equals("")) //$NON-NLS-1$
		{
			throw new IllegalArgumentException("newFileName should not be \"\"."); //$NON-NLS-1$
		}

		WsdlDefinition wsdl = getWsdlDefinition(wsdlFile);
		if (wsdl == null)
		{
			throw new FileNotFoundException(MessageFormat.format("Wsdl for file {0} not found", wsdlFile.getPath()));//$NON-NLS-1$
		}
		wsdl.setFile(new File(wsdlFile.getParent(), newFileName));

		ReferencesFinder refFinder = new ReferencesFinder(wsdl);
		WsdlWalker<RuntimeException> walker = new WsdlWalker<RuntimeException>(refFinder);
		walker.walk(rootWsdlDefinitionWrapper);
		for (IWsdlArtifactReference rel : refFinder.foundReferences)
		{
			rel.setLocation(rel.getLocation().replaceAll(wsdlFile.getName(), URI.encodeQuery(newFileName, false)));
		}
	}

	public Set<File> getAllAbsoluteLocations(File targetFolder)
	{
		FilesCollector collector = new FilesCollector(targetFolder);
		WsdlWalker<RuntimeException> walker = new WsdlWalker<RuntimeException>(collector);
		walker.walk(this.rootWsdlDefinitionWrapper);
		
		return collector.getCollectedFiles();
	}
	
	public String getSchemaSubFolder()
	{
		return this.schemaSubFolder;
	}

	private class ArtifactFinder implements WsdlVisitor<RuntimeException>
	{
		private File file;
		private ARTIFACT_TYPE[] typesToSearchFor;

		private IWsdlArtifact<?> foundArtifact;
		public ArtifactFinder(File fileToSearchFor, ARTIFACT_TYPE... typesToSearchFor) {
			ContractChecker.nullCheckParam(fileToSearchFor,"fileToSearchFor"); //$NON-NLS-1$
			ContractChecker.nullCheckParam(typesToSearchFor,"typeToSearchFor"); //$NON-NLS-1$
			this.file = fileToSearchFor;
			this.typesToSearchFor = typesToSearchFor;
		}
		@Override
		public void visit(IWsdlArtifact<?> currentArtifact) throws CancelWalking {
			if(currentArtifact.getFile().equals(file)) {
				for(ARTIFACT_TYPE typeToSearchFor: typesToSearchFor) {
					if(currentArtifact.getType().equals(typeToSearchFor)){
						foundArtifact = currentArtifact;
						break;
					}
				}
				//found the file, do not traverse the tree further
				throw new CancelWalking();
			}
		}
		
		public IWsdlArtifact<?> getFoundArtifact() {
			return foundArtifact;
		}
	}

	private class ReferencesFinder implements WsdlVisitor<RuntimeException> 
	{

		private IWsdlArtifact<?> targetArtifact;
		Set<IWsdlArtifactReference> foundReferences;
		
		public ReferencesFinder(IWsdlArtifact<?> targetArtifact) {
			ContractChecker.nullCheckParam(targetArtifact,"targetArtifact"); //$NON-NLS-1$
			this.targetArtifact = targetArtifact;
			this.foundReferences = new HashSet<IWsdlArtifactReference>();
		}
		@Override
		public void visit(IWsdlArtifact<?> currentArtifact) {
			for(IWsdlArtifactReference ref: currentArtifact.references()) {
				if(ref.referencedArtifact().equals(targetArtifact)) {
					foundReferences.add(ref);
				}
			}
		}
	}
	
	private class SaveVisitor implements WsdlVisitor<IOException> {

		@Override
		public void visit(IWsdlArtifact<?> currentArtifact) throws IOException{
			final Resource resource = currentArtifact.getEObject().eResource();
			resource.setURI(URI.createFileURI(currentArtifact.getFile().getAbsolutePath()));
			resource.save(null);
			
		}
	}
	
	private class FilesCollector implements WsdlVisitor<RuntimeException> {
		File targetFolder;
		Set<File> allFiles;

		public FilesCollector(File targetFolder) {
			ContractChecker.nullCheckParam(targetFolder, "targetFolder"); //$NON-NLS-1$
			this.targetFolder = targetFolder;
			allFiles = new HashSet<File>();
		}
		
		@Override
		public void visit(IWsdlArtifact<?> currentArtifact)
				throws RuntimeException {
			if(targetFolder==null) {
				allFiles.add(currentArtifact.getFile());
			}else{
				allFiles.add(new File(targetFolder, getRelativePathFromRoot(currentArtifact)));
			}
		}
		
		public Set<File> getCollectedFiles() {
			return allFiles;
		}
	}
	
	private class TargetFolderSetter implements WsdlVisitor<RuntimeException> {

		private File targetFolder;

		public TargetFolderSetter(File targetfolder) {
			this.targetFolder = targetfolder;
		}

		@Override
		public void visit(IWsdlArtifact<?> currentArtifact)
				throws RuntimeException, CancelWalking {
			String relativePathFromRoot = getRelativePathFromRoot(currentArtifact);
			assert(currentArtifact instanceof WsdlArtifact<?>);
			((WsdlArtifact<?>)currentArtifact).setFile(new File(targetFolder, relativePathFromRoot));
		}
	}
	
	private String getRelativePathFromRoot(IWsdlArtifact<?> artifact) {
		String relativePathFromRoot;
		if(artifact.getType().equals(ARTIFACT_TYPE.REFERENCED_SCHEMA)) {
			relativePathFromRoot = new File(schemaSubFolder, artifact.getFile().getName()).getPath();
		}else{
			relativePathFromRoot =  artifact.getFile().getName();
		}
		return relativePathFromRoot;
	}

}
