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
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.servicesregistry.wsdl.internal.plugin.text.WsdlMessages;
import org.eclipse.servicesregistry.wsdl.internal.util.FileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.IFileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.NtfsFileTrimmer;
import org.eclipse.servicesregistry.wsdl.walker.CancelWalking;
import org.eclipse.servicesregistry.wsdl.walker.WsdlVisitor;
import org.eclipse.servicesregistry.wsdl.walker.WsdlWalker;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.ISchemaDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact.ARTIFACT_TYPE;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IWsdlArtifactFileNameCalculator;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlWtpStrategy;

/**
 * Default strategy implementation for wtp import tool. Instances must not be reused across different downloads. 
 */
public class WsdlWtpStrategyDefault extends WsdlWtpStrategy
{
	private static final String SCHEMAS_SUBFOLDER = "Schemas"; //$NON-NLS-1$
	private final static String EMPTY_STRING = ""; //$NON-NLS-1$
	private boolean saveAllowed = true;
	
	
	public WsdlWtpStrategyDefault()
	{
		existingFilesProcessor = new DefaultExistingFilesProcessor();
	}
	
	@Override
	public String getSchemaSubFolder() 
	{
		return SCHEMAS_SUBFOLDER; 
	}

	@Override
	public void postSave(final IWsdlWtpDescriptorContainer wsdlDescriptorsContainer) throws WsdlStrategyException
	{				
	}

	@Override
	public void preSave(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer, File targetFolder) throws WsdlStrategyException
	{
		trimFilesIfNecessary(wsdlDescriptorsContainer);
		
		Set<File> existingFiles = existingFiles(wsdlDescriptorsContainer, targetFolder);
		
		if(existingFiles.isEmpty()) {
			saveAllowed = true;
			return;
		}
		
		final IStatus checkoutStatus = existingFilesProcessor.process(existingFiles);
		if (checkoutStatus.getSeverity() == IStatus.ERROR) {
			throw new WsdlStrategyException("Failed to make files writable", checkoutStatus.getMessage(), checkoutStatus.getException()); //$NON-NLS-1$
		}
		saveAllowed = checkoutStatus.isOK();
		
	}
	
	private void trimFilesIfNecessary(IWsdlWtpDescriptorContainer container) 
	{
		NtfsFileTrimmer trimmer = new NtfsFileTrimmer(calculateMaxLength());
		LargeFilesCollector largeFilesCollector = new LargeFilesCollector(trimmer);
		
		WsdlWalker<RuntimeException> walker = new WsdlWalker<RuntimeException>(largeFilesCollector);
		walker.walk(container.getRootWsdlDefinition());
		
		for(Entry<File, IWsdlArtifact<?>> largeFileEntry : largeFilesCollector.largeFiles.entrySet()) {
			
			File trimmedFile = trimmer.trim(largeFileEntry.getKey());
			File originalFile = largeFileEntry.getKey();
			IWsdlArtifact<?> currentArtifact = largeFileEntry.getValue();
			
			switch (currentArtifact.getType()) {

			case ROOT_WSDL:
			case REFERENCED_WSDL: {
				renameWsdlDefinitionUnchecked(container, originalFile, trimmedFile.getName());
				break;
			}
			case REFERENCED_SCHEMA: {
				renameSchemaDefinitionUnchecked(container, currentArtifact.getFile(), trimmedFile.getName());
				break;
			}
			default: {
				throw new IllegalStateException("Unknown artifact type:"+currentArtifact.getType()); //$NON-NLS-1$
			}
			}
			
		}
	}
	
	private int calculateMaxLength() {
	
		final int tolerance = 10;
		/*
		 * Service Composer suffixes the wsdl name with "_simplified" after the import tool has completed, which can result in a filename longer than 255 chars.
		 * This works around the described case. Real solution is to fix this in Service Composer.
		 */
		final int serviceComposerSuffixLength = "_simplified".length(); //$NON-NLS-1$
		return 255 - tolerance - serviceComposerSuffixLength;
	}

	private void renameWsdlDefinitionUnchecked(IWsdlWtpDescriptorContainer container, final File wsdlFile, String newFileName) {
		try{
			container.renameWsdlDefinition(wsdlFile, newFileName);
		}catch(FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void renameSchemaDefinitionUnchecked(IWsdlWtpDescriptorContainer container, final File wsdlFile, String newFileName) {
		try{
			container.renameSchemaDefinition(wsdlFile, newFileName);
		}catch(FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public void postLoad(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer) throws WsdlStrategyException
	{
		final IWsdlDefinition rootWsdlDef = wsdlDescriptorsContainer.getRootWsdlDefinition();
		if(!fileUtils().isFile(rootWsdlDef.getOriginalLocation()))
		{
			updateArtifactFileName(wsdlDescriptorsContainer.getRootWsdlDefinition(), wsdlDescriptorsContainer);
		}
	}
	
	private void updateArtifactFileName(final IWsdlArtifact<?> artifact, final IWsdlWtpDescriptorContainer container) throws WsdlStrategyException
	{
		WsdlWalker<WsdlStrategyException> walker = new WsdlWalker<WsdlStrategyException>(new FileNameUpdater(container, computeWsdlFileSuffix(container)));
		walker.walk(artifact);
	}
	
	/**
	 * Computes the suffix which should be appended to the WSDL file name. By default this suffix is the names of the services available in the WSDL separated with underscores. If services are not available, the suffix is derived by the root WSDL URL Extenders may override this method to define
	 * their own suffixes
	 */
	protected String computeWsdlFileSuffix(final IWsdlWtpDescriptorContainer container)
	{
		final String servicesSuffix = computeServicesSuffix(container);
		return servicesSuffix.equals(EMPTY_STRING) ? computePathSuffix(container) : servicesSuffix;
	}

	private String computeServicesSuffix(final IWsdlWtpDescriptorContainer container)
	{
		final StringBuilder suffixBuilder = new StringBuilder();
		final WsdlVisitor<RuntimeException> visitor = new WsdlVisitor<RuntimeException>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void visit(IWsdlArtifact<?> currentArtifact) throws RuntimeException, CancelWalking
			{
				if (currentArtifact.getType() == ARTIFACT_TYPE.ROOT_WSDL || currentArtifact.getType() == ARTIFACT_TYPE.REFERENCED_WSDL)
				{
					final IWsdlDefinition wsdlDef = (IWsdlDefinition) currentArtifact;
					Set<QName> keys = wsdlDef.getEObject().getServices().keySet();
					for (QName key : keys)
					{
						suffixBuilder.append("_" + key.getLocalPart()); //$NON-NLS-1$
					}
				}
			}
		};
		final WsdlWalker<RuntimeException> walker = new WsdlWalker<RuntimeException>(visitor);
		walker.walk(container.getRootWsdlDefinition());

		return suffixBuilder.toString();
	}

	private String computePathSuffix(final IWsdlWtpDescriptorContainer container)
	{
		final URL rootWsdlLocation = container.getRootWsdlDefinition().getOriginalLocation();
		if (rootWsdlLocation.getProtocol().equalsIgnoreCase("http") || rootWsdlLocation.getProtocol().equalsIgnoreCase("https")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			if (rootWsdlLocation.getPath().startsWith("/")) //$NON-NLS-1$
			{
				return "_" + extractUnescapedPath(rootWsdlLocation).substring(1).replace('/', '_').replace('\\', '_'); //$NON-NLS-1$
			} else
			{
				return "_" + extractUnescapedPath(rootWsdlLocation); //$NON-NLS-1$
			}
		}

		return ""; //$NON-NLS-1$
	}

	private String extractUnescapedPath(final URL url)
	{
		try
		{
			return url.toURI().getPath();
		} catch (URISyntaxException e)
		{ // $JL-EXC$
			return url.getPath();
		}
	}	

	@Override
	public void preLoad() throws WsdlStrategyException
	{
		// Nothing to do
	}
	
	@Override
	public boolean isSaveAllowed()
	{
		return saveAllowed;
	}
	
	@Override
	public void setOverwriteFiles(boolean overwriteFiles) 
	{
	}
	
	private Set<File> existingFiles(final IWsdlWtpDescriptorContainer wsdlDescriptorsContainer, File targetFolder)
	{
		final Set<File> result = new HashSet<File>();
		for (File f : wsdlDescriptorsContainer.getAllAbsoluteLocations(targetFolder))
		{
			if (f.exists())
			{
				result.add(f);
			}
		}

		return result;
	}

	@Override
	public IWsdlArtifactFileNameCalculator createWsdlFileNameCalculator()
	{
		return new WsdlArtifactFileNameCalculator(fileUtils());
	}
	
	protected IFileUtils fileUtils()
	{
		return new FileUtils();
	}
	
	private class FileNameUpdater implements WsdlVisitor<WsdlStrategyException>  {
		
		private IWsdlWtpDescriptorContainer container;
		private String sufix;
		
		public FileNameUpdater(IWsdlWtpDescriptorContainer container, final String suffix) {
			ContractChecker.nullCheckParam(container, "container"); //$NON-NLS-1$
			this.container = container;
			this.sufix = suffix;
		}
		
		@Override
		public void visit(IWsdlArtifact<?> currentArtifact)
				throws WsdlStrategyException {
			try{
				if(currentArtifact instanceof IWsdlDefinition) {
					container.renameWsdlDefinition(currentArtifact.getFile(), getFileName(currentArtifact.getFile(), currentArtifact.getDefaultFileName(), sufix)) ;
				}else if(currentArtifact instanceof ISchemaDefinition) {
					container.renameSchemaDefinition(currentArtifact.getFile(), getFileName(currentArtifact.getFile(), currentArtifact.getDefaultFileName(), sufix)) ;
				}
			}catch(FileNotFoundException ex) {
				throw new WsdlStrategyException(ex.getMessage(), MessageFormat.format(WsdlMessages.WsdlWtpStrategyDefault_FileNotFoundMsg, 
						currentArtifact.getFile().getAbsolutePath()), ex);
			}
			
		}
		
		private String getFileName(File file, String wrapperName, String suffix)
		{
			if(!file.getName().equals(wrapperName))
			{
				return wrapperName;
			}
			
			return fileUtils().getFileNameWithoutExtension(file) + suffix + "." + fileUtils().getFileExtension(file); //$NON-NLS-1$
		}
	}
	
	private class LargeFilesCollector implements WsdlVisitor<RuntimeException> {

		private NtfsFileTrimmer fileTrimmer;
		final Map<File, IWsdlArtifact<?>> largeFiles = new HashMap<File, IWsdlArtifact<?>>();

		public LargeFilesCollector(NtfsFileTrimmer fileTrimmer) {
			this.fileTrimmer = fileTrimmer;
		}

		@Override
		public void visit(IWsdlArtifact<?> currentArtifact) throws RuntimeException, CancelWalking {
			if(fileTrimmer.needsTrim(currentArtifact.getFile())) {
				largeFiles.put(currentArtifact.getFile(), currentArtifact);
			}
		}
		
	}
	
}
