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
package org.eclipse.servicesregistry.wsdl.internal.util;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import static java.text.MessageFormat.format;

/**
 * Class which is responsible to trim file names to a length below 255 characters to overcome the well-known NTFS limitation.
 * An instance of this class keeps state of already trimmed files, so if trimming a file would result in a name which has already been returned, the suffix is increased,
 * bearing results in the following fashion:
 * <ol>
 * <li>verylongfilename...._0.extension</li>
 * <li>verylongfilename...._1.extension</li>
 * <li>so forth</li>
 * </ol> 
 */
public class NtfsFileTrimmer {

	public static final int DEFAULT_MAX_FILE_NAME_LENGTH = 245;//little less than NTFS limit to be on the safe side
	
	private final int maxFilenameLength;
	
	private int suffixCounter;
	
	private StringTrimmer stringTrimmer;

	private final FileUtils fileUtils;
	private final Set<File> trimmedFiles;
	
	
	public NtfsFileTrimmer() {
		this(DEFAULT_MAX_FILE_NAME_LENGTH);
	}
	
	public NtfsFileTrimmer(int maxFileNameLength) {
		checkLength(maxFileNameLength);
		this.maxFilenameLength = maxFileNameLength;
		suffixCounter = 0;
		stringTrimmer = new StringTrimmer("_"+suffixCounter); //$NON-NLS-1$
		fileUtils = new FileUtils();
		trimmedFiles = new HashSet<File>();
	}

	private void checkLength(int length) {
		if(length<1||length>255) {
			throw new IllegalArgumentException(format("Illegal length : {0} . NTFS file names must be between 1 and 255 characters ", length)); //$NON-NLS-1$
		}
	}

	public File trim(File file) {
		boolean needsTrim = needsTrim(file);
		
		if(!needsTrim) {
			return file;
		}
		
		String originalFileNameWithoutExtension  = fileUtils.getFileNameWithoutExtension(file);
		String extension = fileUtils.getFileExtension(file);
		int trimLength = maxFilenameLength - extension.length() - 1;
		
		String trimmedFileNameWithoutExtension  = stringTrimmer.trim(originalFileNameWithoutExtension, trimLength);
		File result = createTrimmedFile(file, extension, trimmedFileNameWithoutExtension);
		
		while(trimmedFiles.contains(result)) {
			suffixCounter++;
			stringTrimmer = new StringTrimmer(getCurrentSuffix());
			trimmedFileNameWithoutExtension  = stringTrimmer.trim(originalFileNameWithoutExtension, trimLength);
			result = createTrimmedFile(file, extension, trimmedFileNameWithoutExtension);
		}

		trimmedFiles.add(result);
		return result;
	}

	public boolean needsTrim(File file) {
		return file.getName().length() > maxFilenameLength;
	}

	private File createTrimmedFile(File originalFile, String extension, String trimmedFileNameWithoutExtension) {
		return new File( originalFile.getParentFile() , trimmedFileNameWithoutExtension + "." + extension ); //$NON-NLS-1$
	}

	protected String getCurrentSuffix() {
		return "_" + suffixCounter; //$NON-NLS-1$
	}

}
