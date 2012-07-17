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
@javax.xml.ws.WebFault(name = "SRAuthenticationException", targetNamespace = "http://sap.com/esi/uddi/sr/api/ws/", faultBean = "org.eclipse.servicesregistry.proxy.types.SRAuthenticationException")
public class ServicesRegistrySinotifyPhysicalSystemFault1 extends java.lang.Exception {

  private org.eclipse.servicesregistry.proxy.types.SRAuthenticationException _ServicesRegistrySinotifyPhysicalSystemFault1;

  public ServicesRegistrySinotifyPhysicalSystemFault1(String message, org.eclipse.servicesregistry.proxy.types.SRAuthenticationException faultInfo){
    super(message);
    this._ServicesRegistrySinotifyPhysicalSystemFault1 = faultInfo;
  }

  public ServicesRegistrySinotifyPhysicalSystemFault1(String message, org.eclipse.servicesregistry.proxy.types.SRAuthenticationException faultInfo, Throwable cause){
    super(message, cause);
    this._ServicesRegistrySinotifyPhysicalSystemFault1 = faultInfo;
  }

  public org.eclipse.servicesregistry.proxy.types.SRAuthenticationException getFaultInfo(){
    return this._ServicesRegistrySinotifyPhysicalSystemFault1;
  }

}
