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
package org.eclipse.servicesregistry.proxy.types;

/**
 * Exception class for service fault.
 */
@javax.xml.ws.WebFault(name = "RemoteException", targetNamespace = "http://sap.com/esi/uddi/sr/api/ws/", faultBean = "org.eclipse.servicesregistry.proxy.types.RemoteException")
public class FindServiceDefinitionsFault extends java.lang.Exception {

  private org.eclipse.servicesregistry.proxy.types.RemoteException _FindServiceDefinitionsFault;

  public FindServiceDefinitionsFault(String message, org.eclipse.servicesregistry.proxy.types.RemoteException faultInfo){
    super(message);
    this._FindServiceDefinitionsFault = faultInfo;
  }

  public FindServiceDefinitionsFault(String message, org.eclipse.servicesregistry.proxy.types.RemoteException faultInfo, Throwable cause){
    super(message, cause);
    this._FindServiceDefinitionsFault = faultInfo;
  }

  public org.eclipse.servicesregistry.proxy.types.RemoteException getFaultInfo(){
    return this._FindServiceDefinitionsFault;
  }

}
