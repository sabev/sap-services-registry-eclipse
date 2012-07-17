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
package org.eclipse.servicesregistry.core.test.prefs.securestore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.Credentials;
import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.PasswordStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class PasswordStoreTest
{
	@Mock
	private ISecurePreferences securePreferencesMock;
	
	private final String role = "role";
	private final String url  = "sr.user/srview";
	private final String path = "/role/sr.user(srview";
	private final String username = "user";
	private final String password = "pass";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testObtainCredentials() throws StorageException
	{
		final ISecurePreferences nodeMock = getNodeMockForLoad();

		Mockito.stub(securePreferencesMock.nodeExists(Mockito.eq(path))).toReturn(true);
		Mockito.stub(securePreferencesMock.node(Mockito.eq(path))).toReturn(nodeMock);
		
		final MyPasswordStore mps = new MyPasswordStore("");
		Credentials actual = mps.obtainCredential(url, role);
		
		Mockito.verify(securePreferencesMock, Mockito.times(1)).nodeExists(Mockito.eq(path));
		Mockito.verify(securePreferencesMock, Mockito.times(2)).node(Mockito.eq(path));
		
		assertNotNull(actual);
		assertCredentials(actual);
	}
	
	@Test
	public void testPutCredential() throws StorageException
	{
		final ISecurePreferences nodeMock = getNodeMockForSave();
		Mockito.stub(securePreferencesMock.node(Mockito.eq(path))).toReturn(nodeMock);
		
		Credentials credentials = new Credentials(username, password);
		
		final MyPasswordStore mps = new MyPasswordStore("");
		mps.putCredential(url, role, credentials);
		
		verifyCredentialsStoredOnce(nodeMock, username, password);
		Mockito.verify(securePreferencesMock, Mockito.times(1)).node(Mockito.eq(path));
	}
	
	@Test
	public void testResetCredentials() throws IOException, StorageException
	{
		final ISecurePreferences nodeMock = getNodeMockForLoad();
		
		Mockito.stub(securePreferencesMock.nodeExists(Mockito.eq(path))).toReturn(true);
		Mockito.stub(securePreferencesMock.node(Mockito.eq(path))).toReturn(nodeMock);
		
		final MyPasswordStore mps = new MyPasswordStore("");
		Credentials actual = mps.resetCredential(url, role);
		assertCredentials(actual);
		
		Mockito.verify(nodeMock, Mockito.times(1)).removeNode();
		Mockito.verify(securePreferencesMock, Mockito.times(2)).nodeExists(Mockito.eq(path));
		Mockito.verify(securePreferencesMock, Mockito.times(3)).node(Mockito.eq(path));
		Mockito.verify(securePreferencesMock, Mockito.times(1)).flush();
	}
	
	private ISecurePreferences getNodeMockForLoad() throws StorageException
	{
		final ISecurePreferences nodeMock = Mockito.mock(ISecurePreferences.class);
		Mockito.stub(nodeMock.get(Mockito.eq(PasswordStore.USER), (String)Mockito.isNull())).toReturn(username);
		Mockito.stub(nodeMock.get(Mockito.eq(PasswordStore.PASSWORD), (String)Mockito.isNull())).toReturn(password);
		
		return nodeMock;
	}
	
	private ISecurePreferences getNodeMockForSave()
	{
		return Mockito.mock(ISecurePreferences.class);
	}
	
	private void verifyCredentialsStoredOnce(final ISecurePreferences prefsMock, final String userName, final String password) throws StorageException
	{
		Mockito.verify(prefsMock, Mockito.times(1)).put(Mockito.eq(PasswordStore.USER), Mockito.eq(userName), Mockito.eq(false));
		Mockito.verify(prefsMock, Mockito.times(1)).put(Mockito.eq(PasswordStore.PASSWORD), Mockito.eq(password), Mockito.eq(true));
	}
	
	private void assertCredentials(Credentials actual)
	{
		assertEquals(username, actual.getUser());
		assertEquals(password, actual.getPassword());
	}
	

	private class MyPasswordStore extends PasswordStore
	{
		public MyPasswordStore(String a)
		{}
		
		@Override
		protected ISecurePreferences getEclipseSecureStore() 
		{
			return securePreferencesMock;
		}
		
	}
}
