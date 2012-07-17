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
package org.eclipse.servicesregistry.testutils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.platform.discovery.util.internal.ContractChecker;

public class TestFileUtils
{
	private static int dir_index = 0;
	private static final int BUFFER_LENGTH = 1024;
	
	public File copyClassResourceToFileSystem(Class<?> classContext, String relativePathOfResource, File targetDir) throws IOException{
		
		ContractChecker.nullCheckParam(classContext, "classContext"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(relativePathOfResource, "relativePath"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(targetDir, "targetDir"); //$NON-NLS-1$

		InputStream source = null;
		OutputStream target = null;
		
		try {
			source = classContext.getResourceAsStream(relativePathOfResource);
			final String[] segments = relativePathOfResource.split("/"); //$NON-NLS-1$
			final String filename = segments[segments.length-1];
			
			final File result = new File(targetDir, filename);
			target = new FileOutputStream(result);
			final byte[] buffer = new byte[BUFFER_LENGTH];
			int read = -1;
			while( (read = source.read(buffer)) !=-1) {
				target.write(buffer, 0, read);
			}

			return result;
			
		} finally {// when finally java 7 comes we won't need to write this
					// annoying boilerplate
			for (Closeable closeable : new Closeable[] { source, target }) {
				if (closeable != null) {
					closeable.close();
				}
			}
		}

	}
	
	/**
	 * Unpacks the ZipInputStream given into the root directory given.
	 * <p/>
	 * @param zip the zip to unpack.
	 * @param rootDir the directory to write to.
	 * @throws IOException if something goes wrong.
	 */
	public void unpackToDir(ZipInputStream zip, File rootDir) throws IOException {
		ZipEntry nextEntry = null;
		while ((nextEntry = zip.getNextEntry()) != null) {
			if (nextEntry.isDirectory()) {
				(new File(rootDir, nextEntry.getName())).mkdirs();
			}
			else {
				File unpackFile = new File(rootDir, nextEntry.getName());
				if (!unpackFile.getParentFile().exists()) {
					unpackFile.getParentFile().mkdirs();
				}
				OutputStream unpackOutputStream = new FileOutputStream(unpackFile);
				try {
					copyContent(zip, unpackOutputStream);
				}
				finally {
					unpackOutputStream.close();
				}
				unpackFile.setLastModified(nextEntry.getTime());
			}
		}
	}
	
	/**
	 * Copies the entire content of the input stream given to the output stream given.
	 * <p/>
	 * @param source the InputStream to read from.
	 * @param sink the OutputStream to write to.
	 * @throws IOException if something fails.
	 */
	public void copyContent(InputStream source, OutputStream sink) throws IOException {
		byte[] content = new byte[BUFFER_LENGTH];
		int bytes = -1;
		while ((bytes = source.read(content, 0, BUFFER_LENGTH)) > 0) {
			sink.write(content, 0, bytes);
		}
	}
	
	public File createTempDirectory() {
		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		dir_index++;
		File temp1 = new File(tempDir, Integer.toString(this.hashCode()) + "_" //$NON-NLS-1$
				+ dir_index);
		temp1.mkdir();
		return temp1;
	}
	
	/**
	 * Copy the content of <code>filePath</code> and copies it to temporary file with the specified name in system temp directory.
	 */
	public File copyToTempLocation(Class<?> classContext, String filePath, final String fileName) throws IOException
	{
		final InputStream is = classContext.getResourceAsStream(filePath);
		final File dir = new File(System.getProperty("java.io.tmpdir"));
		final File tempFile = new File(dir, fileName);
		tempFile.deleteOnExit();

		final FileOutputStream fos = new FileOutputStream(tempFile);
		try
		{
			byte[] buff = new byte[1024];
			for (int cnt = 0; (cnt = is.read(buff)) > -1;)
			{
				fos.write(buff, 0, cnt);
			}
		} finally
		{
			is.close();
			fos.close();
		}

		return tempFile;
	}
	
	public void deleteDirectory(final File directory) {
		if (directory == null) {
			return;
		}
		if (!directory.exists()) {
			return;
		}
		for (File f : directory.listFiles()) {
			if(f.isDirectory())
			{
				deleteDirectory(f);
			}
			else
			{
				f.delete();
			}
		}
		directory.delete();
	}
	
	/**
	 * Reads the contents of a file and returns it as String.
	 * 
	 * @param file -
	 *            the file to read from
	 * @return the contents of the file
	 * @throws IOException
	 */
	public String readFileContents(File file) throws IOException
	{
		String readInf = null;
		final InputStream in = new FileInputStream(file);

		try
		{
			int inRead = in.read();

			readInf = "";
			while (inRead != -1)
			{
				readInf += String.valueOf((char) inRead);
				inRead = in.read();
			}
		} catch (IOException e)
		{
			throw e;
		} finally
		{
			in.close();
		}

		return readInf;
	}
	
}
