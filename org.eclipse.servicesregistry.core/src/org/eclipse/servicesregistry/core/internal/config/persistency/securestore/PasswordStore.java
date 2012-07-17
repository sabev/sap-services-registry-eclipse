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
package org.eclipse.servicesregistry.core.internal.config.persistency.securestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.equinox.security.storage.provider.IProviderHints;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;

public class PasswordStore implements IPasswordStore
{

	public static final String USER = "user"; //$NON-NLS-1$
	public static final String PASSWORD = "password"; //$NON-NLS-1$
	public static final String CLIENT = "client"; //$NON-NLS-1$
	public static final String SSL = "ssl"; //$NON-NLS-1$
	private static PasswordStore INSTANCE = null;

	public static synchronized IPasswordStore getInstance() 
	{
		if (INSTANCE == null) 
		{
			INSTANCE = new PasswordStore();
	    }
		return INSTANCE;
	}

	protected PasswordStore()
	{
	}
	
	@Override
	public Credentials obtainCredential(String url, String role) 
	{
		ISecurePreferences storage = getEclipseSecureStore();
		final String path = getPath(url, role);
	
		if (storage.nodeExists(path) && storage.node(path)!= null) 
		{
			try 
			{
                return getCredentials(storage.node(path));
            } 
			catch (StorageException e) 
			{
                // If we fail to get the credentials from the secureStore log the error
                Logger.getLogger("SecureStore").throwing("", "", e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		return null;
	}

	@Override
	public void putCredential(String url, String role, Credentials credentials) 
	{
		//credentials.getUser() cannot be null
        if (credentials != null && credentials.getUser().length() > 0) 
        {
        	writeToPersistentStore(role, credentials, url);
        }  
	}

	@Override
	public Credentials resetCredential(String url, String role) 
	{
        ISecurePreferences storage = getEclipseSecureStore();
        String path = getPath(url, role);
        Credentials credentials = obtainCredential(url, role);
        
        if (storage.nodeExists(path)) 
        {
        	storage.node(path).removeNode();
        }
        
		try 
		{
			storage.flush();
		} 
		catch (IOException e) 
		{
			Logger.getLogger("SecureStore").throwing("", "", e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
        return credentials;
	}

    protected ISecurePreferences getEclipseSecureStore() {
		Map<String, Object> map = new HashMap<String, Object>();
        map.put(IProviderHints.PROMPT_USER, false);
		ISecurePreferences storage;
		try 
		{
		    storage = SecurePreferencesFactory.open(null, map);
		} 
		catch (IOException e) 
		{
		    Logger.getLogger("SecureStore").throwing("", "", e);  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		    storage = SecurePreferencesFactory.getDefault();
		}
		return storage;
	}
    
    protected static String getPath(String url, String role) 
    {
        return "/" + mangle(role) + "/" + mangle(url); //$NON-NLS-1$ //$NON-NLS-2$
    }


    private static String mangle(String name) {
        // This method is used for backwards compatibility with 720 SAP secure store
    	// original name is saved
        char[] a = name.toCharArray();
        for (int i = 0; i < a.length; i++) {
            //this symbol is replaced because it makes subnode
        	if (a[i] == '/') {
                a[i] = '(';
            }
            if (a[i] < 32 || a[i] > 126) {
                a[i] = '$';
            }
        }
        return String.valueOf(a);
    }
    
    private void writeToPersistentStore(String role, final Credentials credentials, String url)
    {
		String path = PasswordStore.getPath(url,role);
		final ISecurePreferences node = getEclipseSecureStore().node(path);
		try 
		{
			writeNode(node, credentials);
		} 
		catch (StorageException e) 
		{
			// If we fail to store the credentials, log the error,
			// and return the values anyway.
			Logger.getLogger("SecureStore").throwing("", "", e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} 
		catch (SWTException e) 
		{
			//Method will be called in the same Thread that it is invoked in. 
			//Any Thread dependant methods (such as those who update an SWT widget) 
			//will need to update in the correct Thread. 
			Logger.getLogger("SecureStore").throwing("", "", e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if ("Invalid thread access".equals(e.getMessage())) { //$NON-NLS-1$
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						try 
						{
                          writeNode(node, credentials);
						} 
						catch (Throwable e) 
						{
							Logger.getLogger("SecureStore").throwing("", "", e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						}

					}
				});
			}
		}
	}
    
    private void writeNode(final ISecurePreferences node,  final Credentials credentials) throws StorageException
    {
    	node.put(PasswordStore.USER, credentials.getUser(), false);
		node.put(PasswordStore.PASSWORD, credentials.getPassword(), true);
    }
    
    private Credentials getCredentials(ISecurePreferences node) throws StorageException
    {
    	return new Credentials(node.get(USER, null), 
							   node.get(PASSWORD, null));
    }
}
