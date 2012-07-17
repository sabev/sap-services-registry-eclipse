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

import static java.text.MessageFormat.format;

import java.text.MessageFormat;

import org.eclipse.platform.discovery.util.internal.ContractChecker;


public class StringTrimmer {
	
	private final String suffix;

	public StringTrimmer(String suffix) {
		emptyStringCheckParam(suffix,"suffix"); //$NON-NLS-1$
		this.suffix = suffix;
	}


	public String trim(String input, int desiredLength) {
		
		checkLength(desiredLength);
		
		if(input.length()<=suffix.length()) {
			throw new IllegalArgumentException("Input must be longer than suffix"); //$NON-NLS-1$
		}
		
		if(desiredLength==0) {
			return ""; //$NON-NLS-1$
		}
		
		if(desiredLength>=input.length()) {
			return input;
		}

		String actualSuffix = (desiredLength < suffix.length()-1) ? suffix.substring(0, desiredLength+1) : suffix;

		return input.substring(0, desiredLength-actualSuffix.length()) + actualSuffix;
	}

	private void checkLength(int length) {
		if(length<0) {
			throw new IllegalArgumentException(format("Length cannot be negative:{0}", length)); //$NON-NLS-1$
		}
	}
	
	public static void emptyStringCheckParam(final String param, final String varName) 	{
		ContractChecker.nullCheckParam(param, varName);
		
		if (param.trim().length() == 0) {
			throw new IllegalArgumentException(MessageFormat.format("Parameter {0} is empty string or contains only white spaces", varName)); //$NON-NLS-1$
		}
	}
}
