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
package org.eclipse.servicesregistry.core.test.prefs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.IConfigChangeHandler;
import org.eclipse.servicesregistry.core.config.persistency.IUserCredentialsHandler;
import org.eclipse.servicesregistry.core.config.persistency.IUserCredentialsHandler.Buttons;
import org.eclipse.servicesregistry.core.config.persistency.ConfigLoadException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigStoreException;
import org.eclipse.servicesregistry.core.internal.config.persistency.SrConfigStorage;
import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.Credentials;
import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.IPasswordStore;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
public class SrConfigStorageTest
{
	private static final String ROLE = "org.eclipse.servicesregistry.user";
	
	@Mock
	private IPersistentPreferenceStore preferenceStore;
	@Mock
	private IPasswordStore passwordStore;
	@Mock
	private IServicesRegistrySystem config1;
	@Mock
	private IServicesRegistrySystem config2;
	@Mock
	private IServicesRegistrySystem defaultConfig;
	@Mock
	private IServicesRegistrySystem defaultConfigWithCredentials;
	@Mock
	private IConfigChangeHandler configChangeHandler;
	@Mock
	private IUserCredentialsHandler credentialsHandler;

	private SrConfigStorage storage;
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		setupSrConfig();
	}
	
	private void setupSrConfig()
	{
		stubSrSystem(config1, "Connection1", "Host1", 1111, false, null, null, false);
		stubSrSystem(config2, "Connection2", "Host2", 2222, true, "User2", "Pass2", true);
		stubSrSystem(defaultConfig, "SAP ES Workplace", "sr.esworkplace.sap.com", 80, false, null, null, false);
		stubSrSystem(defaultConfigWithCredentials, "SAP ES Workplace", "sr.esworkplace.sap.com", 80, true, "User_Default", "Password_Default", false);
	}
	
	@Test
	public void testResetConfigurations() throws ConfigStoreException, ConfigLoadException, IOException 
	{
		storage = new SrConfigStorage(preferenceStore, passwordStore){
			@Override
			public Set<IServicesRegistrySystem> readConfigurations()
			{
				return new LinkedHashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[]{config1, config2}));
			}
		};
		storage.registerServerConfigChangeHandler(configChangeHandler);
		
		storage.resetConfigurations();
		verifyMocksOnResetConfigurations(config1, config2);
	}
	
	@Test
	public void testReadConfigurations() throws ConfigLoadException
	{
		storage = new SrConfigStorage(preferenceStore, passwordStore);
		storage.registerServerConfigChangeHandler(configChangeHandler);
		
		setupExpectationsForLoad();
		Mockito.stub(preferenceStore.getBoolean(Mockito.eq(SrConfigStorage.SR_CONFIG_DEFAULT_CONFIG_CREATED))).toReturn(true);

		final Set<IServicesRegistrySystem> configurations = storage.readConfigurations();
		assertEquals("Two configurations expected", 2, configurations.size());
		for(IServicesRegistrySystem cfg : configurations)
		{
			if(cfg.displayName().equals(config1.displayName()))
			{
				assertConfigurationsEqual(cfg, config1);
			}
			else
			{
				assertConfigurationsEqual(cfg, config2);
			}
		}
	}
	
	@Test
	public void testDefaultConfigurationIsCreated() throws ConfigLoadException
	{
		storage = new SrConfigStorage(preferenceStore, passwordStore);
		storage.registerServerConfigChangeHandler(configChangeHandler);
		
		setupExpectationsForLoad();
		Mockito.stub(preferenceStore.getBoolean(Mockito.eq(SrConfigStorage.SR_CONFIG_DEFAULT_CONFIG_CREATED))).toReturn(false);
		
		final Set<IServicesRegistrySystem> configurations = storage.readConfigurations();
		assertEquals("Three configurations expected", 3, configurations.size());
		for(IServicesRegistrySystem cfg : configurations)
		{
			if(cfg.displayName().equals(config1.displayName()))
			{
				assertConfigurationsEqual(cfg, config1);
			}
			if(cfg.displayName().equals(config2.displayName()))
			{
				assertConfigurationsEqual(cfg, config2);
			}
			if(cfg.displayName().equals(defaultConfig.displayName()))
			{
				assertConfigurationsEqual(cfg, defaultConfig);
			}
		}
	}
	
	@Test
	public void testDefaultConfigurationIsNotCreatedWhenSameConfigExists() throws ConfigLoadException
	{
		storage = new SrConfigStorage(preferenceStore, passwordStore);
		storage.registerServerConfigChangeHandler(configChangeHandler);
		setupExpectationsForLoadWithDefaltConfiguration();
		
		final Set<IServicesRegistrySystem> configurations = storage.readConfigurations();
		assertEquals("Three configurations expected", 3, configurations.size());
		for(IServicesRegistrySystem cfg : configurations)
		{
			if(cfg.displayName().equals(config1.displayName()))
			{
				assertConfigurationsEqual(cfg, config1);
			}
			if(cfg.displayName().equals(config2.displayName()))
			{
				assertConfigurationsEqual(cfg, config2);
			}
			if(cfg.displayName().equals(defaultConfigWithCredentials.displayName()))
			{
				assertConfigurationsEqual(cfg, defaultConfigWithCredentials);
			}
		}
	}

	@Test
	public void testStoreConfigurations() throws ConfigStoreException, ConfigLoadException, IOException
	{
		final IServicesRegistrySystem config3 = setupConfigWithStoredCredAndNoUser();
		final Set<IServicesRegistrySystem> configs = new LinkedHashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[]{config1, config2, config3}));
		storage = new SrConfigStorage(preferenceStore, passwordStore){
			@Override
			public void resetConfigurations() throws ConfigStoreException, ConfigLoadException
			{
			}

			@Override
			public Set<IServicesRegistrySystem> readConfigurations() throws ConfigLoadException
			{
				return configs;
			}
		};
		
		Mockito.stub(credentialsHandler.handleExistingCredentials()).toReturn(Buttons.OVERRIDE);
		
		storage.registerServerConfigChangeHandler(configChangeHandler);
		storage.storeConfigurations(configs, credentialsHandler);
		
		verifyPreferenceStoreWhenStoring(config1, config2, config3);
	}
	
	@Test
	public void testUnregisterChangeHandler() throws ConfigStoreException, ConfigLoadException
	{
		final Set<IServicesRegistrySystem> configs = new LinkedHashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[]{config1, config2}));
		storage = new SrConfigStorage(preferenceStore, passwordStore){
			@Override
			public void resetConfigurations() throws ConfigStoreException, ConfigLoadException
			{
			}

			@Override
			public Set<IServicesRegistrySystem> readConfigurations() throws ConfigLoadException
			{
				return configs;
			}
		};

		storage.registerServerConfigChangeHandler(configChangeHandler);
		storage.unregisterServerConfigChangeHandler(configChangeHandler);
		storage.storeConfigurations(configs, credentialsHandler);
		Mockito.verify(configChangeHandler, Mockito.never()).handleConfigChange();
	}

	private void assertConfigurationsEqual(IServicesRegistrySystem toVerify, IServicesRegistrySystem expected)
	{
		assertEquals("Unexpected display name name", expected.displayName(), toVerify.displayName());
		assertEquals("Unexpected host", expected.host(), toVerify.host());
		assertEquals("Unexpected port", expected.port(), toVerify.port());
		assertEquals("Unexpected 'store credentials' value", expected.areCredentialsStored(), toVerify.areCredentialsStored());
		if(expected.areCredentialsStored())
		{
			assertEquals("Unexpected user name", expected.userName(), toVerify.userName());
			assertEquals("Unexpected password", expected.password(), toVerify.password());
		}
	}
	
	private String getUrl(IServicesRegistrySystem config)
	{
		return config.host()+":"+config.port(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private void verifyMocksOnResetConfigurations(final IServicesRegistrySystem... initiallyAvailableSystems) throws IOException
	{
		for(int i = 0; i < initiallyAvailableSystems.length; i++)
		{
			final String idxStr = Integer.toString(i);
			Mockito.verify(preferenceStore, Mockito.times(1)).setToDefault(Mockito.eq(SrConfigStorage.SR_CONFIGS_COUNT_PROPERTY_NAME));
			Mockito.verify(preferenceStore, Mockito.times(1)).setToDefault(Mockito.eq(SrConfigStorage.SR_CONFIG_DISPLAY_NAME_PROPERTY_PREFIX + idxStr));
			Mockito.verify(preferenceStore, Mockito.times(1)).setToDefault(Mockito.eq(SrConfigStorage.SR_CONFIG_HOST_PROPERTY_PREFIX + idxStr));
			Mockito.verify(preferenceStore, Mockito.times(1)).setToDefault(Mockito.eq(SrConfigStorage.SR_CONFIG_PORT_PROPERTY_PREFIX + idxStr));
			Mockito.verify(preferenceStore, Mockito.times(1)).setToDefault(Mockito.eq(SrConfigStorage.SR_CONFIG_STORECRED_PROPERTY_PREFIX + idxStr));
			Mockito.verify(preferenceStore, Mockito.times(1)).setToDefault(Mockito.eq(SrConfigStorage.SR_CONFIG_USEHTTPS_PROPERTY_PREFIX + idxStr));
			
			Mockito.verify(passwordStore, Mockito.times(1)).resetCredential(Mockito.eq(getUrl(initiallyAvailableSystems[i])), Mockito.eq(ROLE));
		}
		
		
		Mockito.verify(preferenceStore, Mockito.times(1)).save();
		Mockito.verify(configChangeHandler, Mockito.times(1)).handleConfigChange();
	}
	
	private void setupExpectationsForLoad()
	{
		stubAvailableConfigurations(config1, config2);
		
		final Credentials credentials = new Credentials(config2.userName(), config2.password());
		Mockito.stub(passwordStore.obtainCredential(Mockito.eq(getUrl(config2)), Mockito.eq(ROLE))).toReturn(credentials);
	}
	
	private void stubAvailableConfigurations(final IServicesRegistrySystem... configurations)
	{
		Mockito.stub(preferenceStore.getInt(Mockito.eq(SrConfigStorage.SR_CONFIGS_COUNT_PROPERTY_NAME))).toReturn(configurations.length);
		for(int i = 0; i < configurations.length; i++)
		{
			final String idxString = Integer.toString(i);
			final IServicesRegistrySystem srSystem = configurations[i];
			
			final String displayName = srSystem.displayName();
			Mockito.stub(preferenceStore.getString(Mockito.eq(SrConfigStorage.SR_CONFIG_DISPLAY_NAME_PROPERTY_PREFIX + idxString))).toReturn(displayName);
			final String host = srSystem.host();
			Mockito.stub(preferenceStore.getString(Mockito.eq(SrConfigStorage.SR_CONFIG_HOST_PROPERTY_PREFIX + idxString))).toReturn(host);
			final int port = srSystem.port();
			Mockito.stub(preferenceStore.getInt(Mockito.eq(SrConfigStorage.SR_CONFIG_PORT_PROPERTY_PREFIX + idxString))).toReturn(port);
			final boolean areCredentialsStored = srSystem.areCredentialsStored();
			Mockito.stub(preferenceStore.getBoolean(Mockito.eq(SrConfigStorage.SR_CONFIG_STORECRED_PROPERTY_PREFIX + idxString))).toReturn(areCredentialsStored);
			final boolean useHttps = srSystem.useHttps();
			Mockito.stub(preferenceStore.getBoolean(Mockito.eq(SrConfigStorage.SR_CONFIG_USEHTTPS_PROPERTY_PREFIX + idxString))).toReturn(useHttps);
			
			if(srSystem.areCredentialsStored())
			{
				final Credentials credentials = new Credentials(srSystem.userName(), srSystem.password());
				Mockito.stub(passwordStore.obtainCredential(Mockito.eq(getUrl(srSystem)), Mockito.eq(ROLE))).toReturn(credentials);
			}
		}
	}
	
	private void stubSrSystem(final IServicesRegistrySystem srMock, String displayName, String host, int port, boolean storeCredentials, String username, String password, boolean useHttps)
	{
		Mockito.stub(srMock.host()).toReturn(host);
		Mockito.stub(srMock.port()).toReturn(port);
		Mockito.stub(srMock.displayName()).toReturn(displayName);
		Mockito.stub(srMock.areCredentialsStored()).toReturn(storeCredentials);
		Mockito.stub(srMock.useHttps()).toReturn(useHttps);
		
		if(storeCredentials)
		{
			Mockito.stub(srMock.userName()).toReturn(username);
			Mockito.stub(srMock.password()).toReturn(password);
			
			final Credentials credentials = new Credentials(srMock.userName(), srMock.password());
			Mockito.stub(passwordStore.obtainCredential(Mockito.eq(getUrl(srMock)), Mockito.eq(ROLE))).toReturn(credentials);
		}
		else
		{
			Mockito.stub(srMock.userName()).toThrow(new IllegalStateException("unexpected invocation of userName() in mock"));
			Mockito.stub(srMock.password()).toThrow(new IllegalStateException("unexpected invocation of password() in mock"));
		}
	}
	
	private void verifyPreferenceStoreWhenStoring(final IServicesRegistrySystem... storedConfigurations) throws IOException
	{
		Mockito.verify(preferenceStore, Mockito.times(1)).setValue(Mockito.eq(SrConfigStorage.SR_CONFIGS_COUNT_PROPERTY_NAME), Mockito.eq(storedConfigurations.length));
		for(int i = 0; i < storedConfigurations.length; i++)
		{
			final String idxStr = Integer.toString(i);
			final IServicesRegistrySystem conf = storedConfigurations[i];
			final String displayName = conf.displayName();
			Mockito.verify(preferenceStore, Mockito.times(1)).setValue(Mockito.eq(SrConfigStorage.SR_CONFIG_DISPLAY_NAME_PROPERTY_PREFIX + idxStr), Mockito.eq(displayName));
			final String host = conf.host();
			Mockito.verify(preferenceStore, Mockito.times(1)).setValue(Mockito.eq(SrConfigStorage.SR_CONFIG_HOST_PROPERTY_PREFIX + idxStr), Mockito.eq(host));
			final int port = conf.port();
			Mockito.verify(preferenceStore, Mockito.times(1)).setValue(Mockito.eq(SrConfigStorage.SR_CONFIG_PORT_PROPERTY_PREFIX + idxStr), Mockito.eq(port));
			final boolean areCredentialsStored = conf.areCredentialsStored();
			Mockito.verify(preferenceStore, Mockito.times(1)).setValue(Mockito.eq(SrConfigStorage.SR_CONFIG_STORECRED_PROPERTY_PREFIX + idxStr), Mockito.eq(areCredentialsStored));
			final boolean useHttps = conf.useHttps();
			Mockito.verify(preferenceStore, Mockito.times(1)).setValue(Mockito.eq(SrConfigStorage.SR_CONFIG_USEHTTPS_PROPERTY_PREFIX + idxStr), Mockito.eq(useHttps));
			
			if(conf.areCredentialsStored())
			{
				Mockito.verify(passwordStore, Mockito.times(1)).putCredential(Mockito.eq(getUrl(conf)), Mockito.eq(ROLE), Mockito.argThat(credentialsMatcher(conf)));
			}
		}

		Mockito.verify(preferenceStore, Mockito.times(1)).setValue(Mockito.eq(SrConfigStorage.SR_CONFIG_DEFAULT_CONFIG_CREATED), Mockito.eq(true));
		Mockito.verify(configChangeHandler, Mockito.times(1)).handleConfigChange();
		Mockito.verify(preferenceStore, Mockito.times(1)).save();
	}
	
	private Matcher<Credentials> credentialsMatcher(final IServicesRegistrySystem conf)
	{
		return new BaseMatcher<Credentials>()
		{
			@Override
			public boolean matches(Object item)
			{
				final Credentials cred = (Credentials)item;
				return cred.getUser().equals(conf.userName()) && cred.getPassword().equals(conf.password());
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}

	private void setupExpectationsForLoadWithDefaltConfiguration() 
	{
		stubAvailableConfigurations(config1, config2, defaultConfigWithCredentials);
		Mockito.stub(preferenceStore.getBoolean(Mockito.eq(SrConfigStorage.SR_CONFIG_DEFAULT_CONFIG_CREATED))).toReturn(true);
	}
	
	
	private IServicesRegistrySystem setupConfigWithStoredCredAndNoUser()
	{
		final IServicesRegistrySystem srConfig = Mockito.mock(IServicesRegistrySystem.class);
		stubSrSystem(srConfig, "Connection3", "Host3", 3333, true, null, null, true);
		
		Mockito.stub(passwordStore.obtainCredential(Mockito.eq(getUrl(srConfig)), Mockito.eq(ROLE))).toReturn(null);
		
		return srConfig;
	}
}
