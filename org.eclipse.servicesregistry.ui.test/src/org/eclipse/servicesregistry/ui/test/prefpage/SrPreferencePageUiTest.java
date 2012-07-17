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
package org.eclipse.servicesregistry.ui.test.prefpage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.ConfigCreationCanceledException;
import org.eclipse.servicesregistry.core.config.persistency.IPreferencesController;
import org.eclipse.servicesregistry.core.config.persistency.IUserCredentialsHandler;
import org.eclipse.servicesregistry.ui.internal.prefpage.SrPreferencePage;
import org.eclipse.servicesregistry.ui.test.prefpage.pageobj.DeleteConfigConfirmDlgPO;
import org.eclipse.servicesregistry.ui.test.prefpage.pageobj.IdenticalCredentialsConfirmDlgPO;
import org.eclipse.servicesregistry.ui.test.prefpage.pageobj.SrPrefPagePO;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class SrPreferencePageUiTest
{
	private SrPrefPagePO prefPagePO;

	private static final String CONNECTION_NAME_1 = "First connection";
	private static final String CONNECTION_NAME_2 = "Second connection";
	private static final String CONNECTION_NAME_3 = "Third connection";
	
	private static final String CONNECTION_HOST_1 = "First host";
	private static final String CONNECTION_HOST_2 = "Second host";
	private static final String CONNECTION_HOST_3 = "Third host";
	
	private static final int CONNECTION_PORT_1 = 81;
	private static final int CONNECTION_PORT_2 = 82;
	private static final int CONNECTION_PORT_3 = 83;
	
	private static final String MODIFIED_CONNECTION_NAME = "Modified connection";

	private IServicesRegistrySystem config1;
	private IServicesRegistrySystem config2;
	private IServicesRegistrySystem config3;

	private IPreferencesController controller;
	
	private MyPreferencePage page;
	
	private Set<IServicesRegistrySystem> availableConfigurations;

	@Before
	public void setUp() 
	{
		setupSrConfigs();
		setupPreferenfesController();
		setupPage();
		startPreferencesPage();
	}
	
	@After
	public void tearDown()
	{
		prefPagePO.dispose();
	}

	private void setupSrConfigs()
	{
		config1 = stubConfiguration(CONNECTION_NAME_1, CONNECTION_HOST_1, CONNECTION_PORT_1);
		config2 = stubConfiguration(CONNECTION_NAME_2, CONNECTION_HOST_2, CONNECTION_PORT_2);
		config3 = stubConfiguration(CONNECTION_NAME_3, CONNECTION_HOST_3, CONNECTION_PORT_3);
	}

	private IServicesRegistrySystem stubConfiguration(final String displayName, final String host, final int port)
	{
		final IServicesRegistrySystem config = Mockito.mock(IServicesRegistrySystem.class);
		Mockito.stub(config.displayName()).toReturn(displayName);
		Mockito.stub(config.host()).toReturn(host);
		Mockito.stub(config.port()).toReturn(port);
		return config;
	}

	private void setupPreferenfesController()
	{
		availableConfigurations = new HashSet<IServicesRegistrySystem>();
		availableConfigurations.add(config1);
		availableConfigurations.add(config2);
		
		controller = Mockito.mock(IPreferencesController.class);
		Mockito.stub(controller.getConfigurations()).toReturn(availableConfigurations);
	}

	private void setupPage()
	{
		page = new MyPreferencePage()
		{
			@Override
			protected IPreferencesController createPreferencesController()
			{
				return controller;
			}
		};
		page.init(null);
	}
	
	@Test
	public void testAllConfigsAreShown()
	{
		assertEquals("Two configs expected", 2, prefPagePO.getDisplayedConfigNames().size());
		
		assertConnection1IsDisplayed();
		assertConnection2IsDisplayed();
	}

	@Test
	public void testEditAndDeleteEnablesOnSelection()
	{
		//test edit and delete button are disabled
		assertFalse("Edit button is enabled without any selection", prefPagePO.canEdit());
		assertFalse("Delete button is enabled without any selection",prefPagePO.canDelete());
		
		prefPagePO.selectConfiguration(CONNECTION_NAME_1);
		
		//test edit and delete button are enabled
		assertTrue("Edit button is enabled without any selection", prefPagePO.canEdit());
		assertTrue("Delete button is enabled without any selection",prefPagePO.canDelete());
	}

	@Test
	public void testAddConfig() throws ConfigCreationCanceledException
	{
		availableConfigurations.add(config3);
		Mockito.stub(controller.createNewConfiguration()).toReturn(config3);

		prefPagePO.addNewConfiguration();

		assertEquals("Three configs expected", 3, prefPagePO.getDisplayedConfigNames().size());
		assertConnection1IsDisplayed();
		assertConnection2IsDisplayed();
		assertConnection3IsDisplayed();
	}

	@Test
	public void testAddConfigCancelled() throws ConfigCreationCanceledException
	{
		Mockito.stub(controller.createNewConfiguration()).toThrow(new ConfigCreationCanceledException());
		prefPagePO.addNewConfiguration();

		assertEquals("Two configs expected", 2, prefPagePO.getDisplayedConfigNames().size());
		assertConnection1IsDisplayed();
		assertConnection1IsDisplayed();
	}
	
	@Test
	public void testEditConfig()
	{
		prefPagePO.selectConfiguration(CONNECTION_NAME_1);
		
		// Remock config1
		Mockito.stub(config1.displayName()).toReturn(MODIFIED_CONNECTION_NAME);

		prefPagePO.editSelectedConfiguration();
		
		assertEquals("Two configs expected", 2, prefPagePO.getDisplayedConfigNames().size());
		assertConnection2IsDisplayed();
		assertTrue(MODIFIED_CONNECTION_NAME + " not contained", prefPagePO.getDisplayedConfigNames().contains(MODIFIED_CONNECTION_NAME));
		Mockito.verify(controller, Mockito.times(1)).editConfiguration(Mockito.eq(config1));
	}
	
	@Test
	public void testDeleteConfig()
	{
		prefPagePO.selectConfiguration(CONNECTION_NAME_1);
		availableConfigurations.clear();
		availableConfigurations.add(config2);
		
		final DeleteConfigConfirmDlgPO deleteConfirmDialog = prefPagePO.deleteSelectedConfig();
		deleteConfirmDialog.confirm();
		
		assertEquals("One config expected", 1, prefPagePO.getDisplayedConfigNames().size());
		assertConnection2IsDisplayed();
		
		Mockito.verify(controller, Mockito.times(1)).deleteConfiguration(Mockito.eq(config1));
	}
	
	@Test
	public void testCancelDeleteConfigConfirmDialog()
	{
		prefPagePO.selectConfiguration(CONNECTION_NAME_1);
		//setupForConfirmDialogTest();

		DeleteConfigConfirmDlgPO deleteConfigDlgPO = prefPagePO.deleteSelectedConfig();
		availableConfigurations.clear();
		availableConfigurations.add(config2);
		deleteConfigDlgPO.cancel();
		
		assertEquals("Two configs expected", 2, prefPagePO.getDisplayedConfigNames().size());
		assertConnection1IsDisplayed();
		assertConnection2IsDisplayed();
		Mockito.verify(controller, Mockito.never()).deleteConfiguration(Mockito.any(IServicesRegistrySystem.class));
	}	
	
	@Test
	public void testExistingUserCredentialsDialog()
	{
		final IUserCredentialsHandler handler = page.createExistingUserCredentialsHandler();
		assertNotNull(handler);
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable(){
	  			@Override
				public void run() {
					handler.handleExistingCredentials();
	 			}
		});
		
		final IdenticalCredentialsConfirmDlgPO credentialsConfirmDlgPo = IdenticalCredentialsConfirmDlgPO.find();
		credentialsConfirmDlgPo.reuse();
	}

	private void assertConnection1IsDisplayed()
	{
		assertTrue(CONNECTION_NAME_1 + " not contained", prefPagePO.getDisplayedConfigNames().contains(CONNECTION_NAME_1));
		assertTrue(CONNECTION_HOST_1 + " not contained", prefPagePO.getDisplayedConfigHosts().contains(CONNECTION_HOST_1));
		assertTrue(CONNECTION_PORT_1 + " not contained", prefPagePO.getDisplayedConfigPorts().contains(""+CONNECTION_PORT_1));
	}
	
	private void assertConnection2IsDisplayed()
	{
		assertTrue(CONNECTION_NAME_2 + " not contained", prefPagePO.getDisplayedConfigNames().contains(CONNECTION_NAME_2));
		assertTrue(CONNECTION_HOST_2 + " not contained", prefPagePO.getDisplayedConfigHosts().contains(CONNECTION_HOST_2));
		assertTrue(CONNECTION_PORT_2 + " not contained", prefPagePO.getDisplayedConfigPorts().contains(""+CONNECTION_PORT_2));
	}
	
	private void assertConnection3IsDisplayed()
	{
		assertTrue(CONNECTION_NAME_3 + " not contained", prefPagePO.getDisplayedConfigNames().contains(CONNECTION_NAME_3));
		assertTrue(CONNECTION_HOST_3 + " not contained", prefPagePO.getDisplayedConfigHosts().contains(CONNECTION_HOST_3));
		assertTrue(CONNECTION_PORT_3 + " not contained", prefPagePO.getDisplayedConfigPorts().contains(""+CONNECTION_PORT_3));
	}
	
	private void startPreferencesPage()
	{
		prefPagePO = SrPrefPagePO.create(page);
		prefPagePO.open();
	}

	private class MyPreferencePage extends SrPreferencePage
	{
		@Override
		protected IUserCredentialsHandler createExistingUserCredentialsHandler() {
			return super.createExistingUserCredentialsHandler();
	 	}
	}
}
