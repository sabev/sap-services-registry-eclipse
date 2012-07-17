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
package org.eclipse.servicesregistry.core.config;

import java.util.Comparator;
import java.util.Locale;

public class ServicesRegistrySystemComparator implements Comparator<IServicesRegistrySystem> {

	@Override
	public int compare(IServicesRegistrySystem sys1, IServicesRegistrySystem sys2) {
		boolean areEqual = sys1.host().toLowerCase(Locale.ENGLISH).equals(sys2.host().toLowerCase(Locale.ENGLISH));
		areEqual = areEqual && (sys1.port() == sys2.port());
		
		return areEqual ? 0 : 1;
	}

}
