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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IReferenceDirective;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.ISchemaDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact.ARTIFACT_TYPE;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IWsdlArtifactFileNameCalculator;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlWtpStrategy;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlWtpImportTool;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.impl.XSDImportImpl;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.w3c.dom.Element;

/**
 * Interface, which provides all required functionality for downloading WSDLs on the File system, using the WTP functionality for download.
 * 
 * The result of the download process is an instance of WsdlWrappersContainer which represents the WSDL document "model"
 * 
 * @author Plamen Pavlov, Danail Branekov
 */
public class WsdlWtpImportToolImpl implements IWsdlWtpImportTool
{
	private static final String WSDL_EXTENSION = "wsdl"; //$NON-NLS-1$
	private static final String SCHEMA_EXTENSION = "xsd"; //$NON-NLS-1$

	private static final String CURRENT_FOLDER = "./"; //$NON-NLS-1$
	private static final String IMPORTS_FOLDER_SEPARATOR = "/"; //$NON-NLS-1$
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private static final String DIAGNOSTIC_MESSAGE = "{0}, Location : {1}, Line : {2}, Column : {3} \n"; //$NON-NLS-1$

	/**
	 * URL, which points to the root WSDL.
	 */
	private URL wsdlLocation;
	
	private IWsdlArtifactFileNameCalculator wsdlFileNameCalc;

	/**
	 * Location where the WSDLs will be downloaded.
	 */
	private File targetFolder;

	/**
	 * Map which contains URL - to processed WSDL artifacts
	 */
	private final Map<URL, IWsdlDefinition> processedWsdlsUrlToArtifactMap = new HashMap<URL, IWsdlDefinition>();

	/**
	 * Map which contains URL - to processed schema artifacts
	 */
	private final Map<URL, ISchemaDefinition> processedSchemasUrlToArtifactMap = new HashMap<URL, ISchemaDefinition>();

	/**
	 * Container for the WSDL and Schema Definitions, which are loaded. Represents the WSDL document model
	 */
	private WsdlArtifactsContainer wsdlDescriptorsContainer = null;

	/**
	 * Strategy behaviour for the WSDL downloader.
	 */
	private WsdlWtpStrategy strategy = null;

	/**
	 * Constructor for WsdlWtpImportToolImpl.
	 * 
	 * @param wsdlLocation
	 *            - URL, which points to the root WSDL.
	 * @param targetFolder2
	 *            - Location where the WSDLs will be downloaded.
	 */
	public WsdlWtpImportToolImpl(final URL wsdlLocation)
	{
		this(wsdlLocation, null);
	}

	/**
	 * Constructor for WsdlWtpImportToolImpl.
	 * 
	 * @param wsdlLocation
	 *            - URL, which points to the root WSDL.
	 * @param targetFolder2
	 *            - Location where the WSDLs will be downloaded.
	 * @param strategy
	 *            - Strategy, which will be used during the WSDL download process.
	 */
	public WsdlWtpImportToolImpl(final URL wsdlLocation, final WsdlWtpStrategy strategy)
	{
		this.wsdlLocation = wsdlLocation;
		this.strategy = (strategy == null) ? new WsdlWtpStrategyDefault() : strategy;
	}

	public IWsdlWtpDescriptorContainer loadWsdls() throws WsdlStrategyException, IOException
	{
		prepareForLoading();
		loadRootWsdl(this.wsdlLocation);
		strategy.postLoad(this.wsdlDescriptorsContainer);
		return this.wsdlDescriptorsContainer;
	}

	private void prepareForLoading() throws WsdlStrategyException
	{
		strategy.preLoad();
		this.processedSchemasUrlToArtifactMap.clear();
		this.processedWsdlsUrlToArtifactMap.clear();
		this.wsdlFileNameCalc = this.strategy.createWsdlFileNameCalculator();
		wsdlDescriptorsContainer = null;
	}

	/**
	 * Loads the root WSDL and the referenced WSDL artifacts
	 */
	private void loadRootWsdl(final URL urlLocation) throws IOException
	{
		final URI rootWsdlUri = toURI(urlLocation);
		final ResourceSet resourceSet = loadWsdlResource(rootWsdlUri);

		// Load the root WSDL
		final WSDLResourceImpl rootWsdlResource = (WSDLResourceImpl) resourceSet.getResource(rootWsdlUri, false);
		final Definition definition = rootWsdlResource.getDefinition();
		final String rootWsdlName = wsdlFileNameCalc.proposeRootWsdlFileName(urlLocation, definition);

		final WsdlDefinition rootWrapper = createRootWsdlArtifact(urlLocation, rootWsdlName, definition);
		this.processedWsdlsUrlToArtifactMap.put(rootWrapper.getOriginalLocation(), rootWrapper);

		loadWsdlDefinitionEImportsSchemaWrappers(urlLocation, definition, rootWrapper, rootWsdlName);
		loadWsdlDefinitionETypeSchemaWrappers(urlLocation, definition, rootWrapper, rootWsdlName);
	}

	/**
	 * Loads a referenced WSDL
	 * 
	 * @param referencedWsdl
	 *            the referenced WSDL definition
	 * @param importingLocation
	 *            the location which imports the WSDL definition
	 * @param refDirective
	 *            the directive via which the WSDL definition is imported
	 * @param parentWsdl
	 *            the parent (importing) WSDL artifact
	 * @param rootWsdlName
	 *            the name of the root WSDL
	 */
	private void loadReferencedWsdl(final Definition referencedWsdl, final URL importingLocation, final IReferenceDirective refDirective, final IWsdlDefinition parentWsdl,
									final String rootWsdlName) throws IOException
	{
		final URL importedWsdlUrl = defineImportUrl(importingLocation, referencedWsdl.getLocation());
		IWsdlDefinition referencedWrapper = this.processedWsdlsUrlToArtifactMap.get(importedWsdlUrl);
		if (referencedWrapper == null)
		{
			// The referenced WSDL has not been processed up to now. Create a new WSDL artifact and traverse through its import statements
			referencedWrapper = createReferencedWsdlArtifact(importingLocation, rootWsdlName, referencedWsdl, parentWsdl);
			this.processedWsdlsUrlToArtifactMap.put(referencedWrapper.getOriginalLocation(), referencedWrapper);
			loadWsdlDefinitionEImportsSchemaWrappers(importingLocation, referencedWsdl, referencedWrapper, rootWsdlName);
			loadWsdlDefinitionETypeSchemaWrappers(importingLocation, referencedWsdl, referencedWrapper, rootWsdlName);
		}

		// The referencing directive location has to be updated with the location of the referenced WSDL file
		refDirective.setLocation(determineWsdlReferenceDirectiveLocation(referencedWrapper.getFile().getName(), parentWsdl.getType()));

		this.wsdlDescriptorsContainer.addWsdlDefinitionWrapper(referencedWrapper, parentWsdl, refDirective);
	}
	
	/**
	 * Loads a referenced schema
	 * 
	 * @param schema
	 *            the schema to be loaded
	 * @param importingLocation
	 *            the location which imports the schema
	 * @param refDirective
	 *            the import/include directive via which the schema is referenced
	 * @param parentArtifact
	 *            the artifact which references this schema
	 * @param rootWsdlName
	 *            the name of the root WSDL
	 */
	private void loadReferencedSchema(final XSDSchema schema, final URL importingLocation, final IReferenceDirective refDirective, final IWsdlArtifact<?> parentArtifact, final String rootWsdlName)
									throws MalformedURLException, IOException
	{
		final URL schemaWrapperLocation = defineImportUrl(importingLocation, refDirective.getLocation());
		ISchemaDefinition importedSchema = this.processedSchemasUrlToArtifactMap.get(schemaWrapperLocation);
		if (importedSchema == null)
		{
			// The schema URL has not been processed so far. Create a new schema artifact and traverse through the schema import/include statements
			importedSchema = createSchemaArtifact(importingLocation, schema, refDirective, parentArtifact, rootWsdlName);
			this.processedSchemasUrlToArtifactMap.put(importedSchema.getOriginalLocation(), importedSchema);
			final List<EObject> eObjects = new ArrayList<EObject>(schema.getContents());
			traverseSchemaContentsIterator(eObjects.iterator(), schemaWrapperLocation, importedSchema, rootWsdlName);
		}
		// Update the reference directive location with the location of the corresponding schema file
		refDirective.setLocation(determineSchemaReferenceDirectiveLocation(importedSchema.getFile().getName(), parentArtifact.getType()));

		this.wsdlDescriptorsContainer.addSchemaDefinitionWrapper(importedSchema, parentArtifact, refDirective);
	}

	private URI toURI(final URL url)
	{
		return URI.createURI(url.toExternalForm());
	}

	private ResourceSet createResourceSet()
	{
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getAdapterFactories().add(new WsdlModelLocatorAdapterFactory());
		resourceSet.getAdapterFactories().add(new XsdSchemaLocationResolverAdapterFactory());

		return resourceSet;
	}

	private ResourceSet loadWsdlResource(final URI uri) throws IOException
	{
		final ResourceSet resourceSet = createResourceSet();
		WSDLResourceImpl wsdlMainResource = (WSDLResourceImpl) resourceSet.createResource(URI.createURI("*.wsdl")); //$NON-NLS-1$
		wsdlMainResource.setURI(uri);
		java.util.Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(WSDLResourceImpl.CONTINUE_ON_LOAD_ERROR, Boolean.valueOf(false));
		map.put(WSDLResourceImpl.USE_EXTENSION_FACTORIES, Boolean.valueOf(true));
		map.put(WSDLResourceImpl.TRACK_LOCATION, Boolean.valueOf(true));

		// When importing wsdls with wrong syntax the WSDLResourceImpl is throwing NPE
		// Therefore it is wrapped in a IOException to provide an error message to the
		// user.
		try
		{
			wsdlMainResource.load(map);
		} catch (NullPointerException npe)
		{
			throw new IOException();
		}

		verifyResource(wsdlMainResource);

		return resourceSet;
	}

	private ResourceSet loadSchemaResouce(final URI uri) throws IOException
	{
		final ResourceSet resourceSet = createResourceSet();
		XSDResourceImpl xsdMainResource = (XSDResourceImpl) resourceSet.createResource(URI.createURI("*.xsd")); //$NON-NLS-1$
		xsdMainResource.setURI(uri);
		xsdMainResource.load(new HashMap<String, Boolean>());

		// must also check diagnostics
		return resourceSet;
	}

	private void verifyResource(Resource resource) throws IOException
	{
		final EList<Diagnostic> wsdlErrors = resource.getErrors();
		if (wsdlErrors.size() > 0)
		{
			throw new IOException(createMessage(resource, wsdlErrors));
		}
	}

	private String createMessage(Resource wsdlResource, EList<Diagnostic> diagnosticList)
	{
		final StringBuilder messageBuilder = new StringBuilder();
		String diagnosticMessage = null;

		for (Diagnostic diagnostic : diagnosticList)
		{
			diagnosticMessage = MessageFormat.format(DIAGNOSTIC_MESSAGE, new Object[] { createMessageField(diagnostic.getMessage()),
							createMessageField(diagnostic.getLocation(), wsdlResource.getURI().toString()), createMessageField(String.valueOf(diagnostic.getLine())),
							createMessageField(String.valueOf(diagnostic.getColumn())) });

			messageBuilder.append(diagnosticMessage);
		}

		return messageBuilder.toString();
	}

	private String createMessageField(String fieldValue)
	{
		return (fieldValue != null) ? fieldValue : ""; //$NON-NLS-1$	
	}

	private String createMessageField(String fieldValue1, String fieldValue2)
	{
		return (fieldValue1 != null) ? fieldValue1 : createMessageField(fieldValue2);
	}

	/**
	 * Traverses through the <code>eImport</code> members of the <code>definition</code>
	 * 
	 * @param importingLocation
	 *            the location which imports the WSDL definition
	 * @param definition
	 *            the WSDL definition which eImports will be processed
	 * @param parentArtifact
	 *            the "parent" of the processed WSDL definition
	 * @param rootWsdlName
	 *            the name of the root WSDL
	 * @see Definition#getEImports()
	 */
	private void loadWsdlDefinitionEImportsSchemaWrappers(final URL importingLocation, final Definition definition, final IWsdlArtifact<?> parentArtifact, final String rootWsdlName)
									throws IOException
	{
		@SuppressWarnings("unchecked")
		final EList<org.eclipse.wst.wsdl.Import> importWsdls = definition.getEImports();
		for (org.eclipse.wst.wsdl.Import imported : importWsdls)
		{
			final URL importedResourceURL = defineImportUrl(importingLocation, imported.getLocationURI());
			final URI importedResourceURI = toURI(importedResourceURL);
			if (definition.getElement().getLocalName().equalsIgnoreCase("schema") || imported.getESchema() != null) //$NON-NLS-1$
			{
				// The import directive refers to a schema
				final ResourceSet resourceSet = loadSchemaResouce(importedResourceURI);
				final XSDResourceImpl xsdResource = (XSDResourceImpl) resourceSet.getResource(importedResourceURI, true);
				loadReferencedSchema(xsdResource.getSchema(), importingLocation, new WsdlImportDirective(imported), parentArtifact, rootWsdlName);
			} else
			{
				// The import directive refers to another WSDL document
				final ResourceSet resourceSet = loadWsdlResource(importedResourceURI);
				final WSDLResourceImpl wsdlResource = (WSDLResourceImpl) resourceSet.getResource(importedResourceURI, true);
				loadReferencedWsdl(wsdlResource.getDefinition(), importedResourceURL, (IReferenceDirective) (new WsdlImportDirective(imported)), (IWsdlDefinition) parentArtifact, rootWsdlName);
			}
		}
	}

	/**
	 * Traverses through the <code>eType</code> members of the <code>definition</code>
	 * 
	 * @param importingLocation
	 *            the location which imports the WSDL definition
	 * @param definition
	 *            the WSDL definition which eImports will be processed
	 * @param parentArtifact
	 *            the "parent" of the processed WSDL definition
	 * @param rootWsdlName
	 *            the name of the root WSDL
	 * @see Definition#getETypes()
	 */
	private void loadWsdlDefinitionETypeSchemaWrappers(final URL importingLocation, final Definition definition, final IWsdlArtifact<?> parentArtifact, final String rootWsdlName) throws IOException
	{
		if (definition.getETypes() == null)
		{
			return;
		}

		final URL definitionLocation = defineImportUrl(importingLocation, definition.getLocation());
		final TreeIterator<EObject> contentsIterator = definition.getETypes().eAllContents();
		traverseSchemaContentsIterator(contentsIterator, definitionLocation, parentArtifact, rootWsdlName);
	}

	/**
	 * Recursively traverse through the schema contents and loads referenced schema artifacts
	 * 
	 * @param contentsIterator
	 *            iterator for a schema contents
	 * @param importingLocation
	 *            the location which imports the schema
	 * @param parentArtifact
	 *            the parent artifact
	 * @param rootWsdlName
	 *            the root WSDL name
	 * 
	 */
	private void traverseSchemaContentsIterator(final Iterator<EObject> contentsIterator, final URL importingLocation, final IWsdlArtifact<?> parentArtifact, final String rootWsdlName)
									throws MalformedURLException, IOException
	{
		while (contentsIterator.hasNext())
		{
			final EObject content = contentsIterator.next();
			if (!(content instanceof XSDSchemaDirective))
			{
				continue;
			}

			final XSDSchemaDirective schemaDirective = (XSDSchemaDirective) content;
			final String schemaLocation = schemaDirective.getSchemaLocation();
			if (schemaLocation == null || schemaLocation.equals(EMPTY_STRING))
			{
				continue;
			}

			final XSDSchema resolvedSchema = getResolvedSchema(schemaDirective);
			if(resolvedSchema == null)
			{
				throw new IOException(MessageFormat.format("Referenced schema with location {0} cannot be resolved", schemaDirective.getSchemaLocation())); //$NON-NLS-1$
			}
			loadReferencedSchema(resolvedSchema, importingLocation, new XsdDirective(schemaDirective), parentArtifact, rootWsdlName);
		}
	}
	
	private XSDSchema getResolvedSchema(final XSDSchemaDirective schemaDirective) throws IOException
	{
		
		final XSDSchema resolvedSchema = schemaDirective.getResolvedSchema();
		
		/*
		 * If the schema directive is "import", but the imported schema types are not used in the WSDL, it will not be resolved by the wtp api.
		 * So, schemaDirective.getResolvedSchema() will return null. In this case we still want to download the schema, so we need to 'force resolve' it.
		 * A way to do this is XSDImportImpl.importSchema(), even though it breaks the encapsulation of the WTP model.
		 * 
		 * If the schema directive is "include" or "redefine", it is always resolved, even if the types are not used throughout the WSDL.
		 * Therefore, this workaround only needs to be executed if (schemaDirective instanceof XSDImport)
		 * 
		 * The other posibility for schemaDirective.getResolvedSchema() to return null is that the "schemaLocation" attribute of the directive points to
		 * an unexisting or otherwise inaccessible location. In this case the importSchema() will have no effect, and this method will still return null.
		 * This will, of course, result in a failure to download the document tree, displaying an error to the user and logging of the problem in the eclipse log.
		 */
		if(resolvedSchema == null && (schemaDirective instanceof XSDImport))
		{
			// Force schema resolution
			final XSDSchema candidateSchema = ((XSDImportImpl)schemaDirective).importSchema();
			if(candidateSchema==null)
			{
				//resolution failed, no resource at this location
				return null;
			}
			else
			{
				final Element rootElement = candidateSchema.getElement();
				if(rootElement==null)
				{
					/*
					 * ((XSDImportImpl)schemaDirective).importSchema() will return successfuly in case the 'schema' at the specified location exists but is not
					 * a valid xml document at all. In such a case getElement() should return null. We check this as a last resort to determine whether we should
					 * download this wsdl document, or fail
					 */
					throw new IOException(MessageFormat.format("Referenced schema with location {0} is not a valid XML document", schemaDirective.getSchemaLocation())); //$NON-NLS-1$

				}
			}
			
		}
		
		return schemaDirective.getResolvedSchema();
	}

	protected URL defineImportUrl(final URL root, final String imported) throws IOException
	{
		try
		{
			return new URL(imported);
		} catch (MalformedURLException e)
		{ // $JL-EXC$
		}

		final URLConnection connection = root.openConnection();
		if (connection instanceof HttpURLConnection)
		{
			return new URL(getRedirectedUrl((HttpURLConnection) connection), imported);
		}

		return new URL(root, imported);
	}

	protected URL getRedirectedUrl(final HttpURLConnection httpConnection) throws IOException
	{
		try
		{
			httpConnection.setInstanceFollowRedirects(true);
			httpConnection.getInputStream();
			return httpConnection.getURL();
		} finally
		{
			httpConnection.disconnect();
		}
	}

	private WsdlDefinition createRootWsdlArtifact(final URL urlLocation, final String wsdlName, final Definition definition)
	{
		assert this.wsdlDescriptorsContainer == null;

		final String fileName = wsdlName + "." + WSDL_EXTENSION; //$NON-NLS-1$
		final File wsdlFile = new File(targetFolder, fileName);
		final WsdlDefinition wdw = new WsdlDefinition(definition, wsdlFile, ARTIFACT_TYPE.ROOT_WSDL, urlLocation, fileName);
		this.wsdlDescriptorsContainer = new WsdlArtifactsContainer(wdw, strategy.getSchemaSubFolder());

		return wdw;
	}

	private WsdlDefinition createReferencedWsdlArtifact(final URL importingLocation, final String rootWsdlName, final Definition definition, final IWsdlDefinition parentWsdl)
									throws IOException
	{
		final URL urlLocation = defineImportUrl(importingLocation, definition.getLocation());
		final String fileName = wsdlFileNameCalc.proposeReferencedWsdlFileName(rootWsdlName, urlLocation, definition) + "." + WSDL_EXTENSION; //$NON-NLS-1$ 

		final File wsdlFile = new File(targetFolder, fileName);
		return new WsdlDefinition(definition, wsdlFile, ARTIFACT_TYPE.REFERENCED_WSDL, urlLocation, fileName);
	}

	private SchemaDefinition createSchemaArtifact(final URL importingLocation, final XSDSchema schema, final IReferenceDirective refDirective, final IWsdlArtifact<?> parentArtifact,
									final String rootWsdlName) throws IOException
	{
		final URL urlLocation = defineImportUrl(importingLocation, refDirective.getLocation());

		final String fileName = wsdlFileNameCalc.proposeReferencedSchemaFileName(rootWsdlName, urlLocation, schema) + "." + SCHEMA_EXTENSION; //$NON-NLS-1$ 

		final File schemaFile;
		if (strategy.getSchemaSubFolder() != null && !strategy.getSchemaSubFolder().equals("")) //$NON-NLS-1$
		{
			schemaFile = new File(new File(targetFolder, strategy.getSchemaSubFolder()), fileName);
		} else
		{
			schemaFile = new File(targetFolder, fileName);
		}

		return new SchemaDefinition(schema, schemaFile, urlLocation, fileName);
	}

	private String determineSchemaReferenceDirectiveLocation(final String artifactFileName, final ARTIFACT_TYPE parentArtifactType)
	{
		if (parentArtifactType == ARTIFACT_TYPE.REFERENCED_SCHEMA)
		{
			return CURRENT_FOLDER + artifactFileName;
		} else
		{
			return CURRENT_FOLDER + strategy.getSchemaSubFolder() + IMPORTS_FOLDER_SEPARATOR + artifactFileName;
		}
	}

	private String determineWsdlReferenceDirectiveLocation(final String artifactFileName, final ARTIFACT_TYPE parentArtifactType)
	{
		return CURRENT_FOLDER + artifactFileName;
	}

	public void saveWsdls(File targetFolder) throws WsdlStrategyException, IOException
	{
		ContractChecker.nullCheckParam(targetFolder, "targetFolder"); //$NON-NLS-1$
		if(!targetFolder.isAbsolute()) {
			throw new IllegalArgumentException("targetFolder must be absolute"); //$NON-NLS-1$
		}
		strategy.preSave(wsdlDescriptorsContainer, targetFolder);

		if (!strategy.isSaveAllowed())
		{
			return;
		}

		wsdlDescriptorsContainer.save(targetFolder);

		strategy.postSave(wsdlDescriptorsContainer);
	}

	public IWsdlWtpDescriptorContainer downloadWsdls(File targetFolder) throws WsdlStrategyException, IOException
	{
		IWsdlWtpDescriptorContainer result = loadWsdls();
		saveWsdls(targetFolder);
		return result;
	}
}
