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
@javax.xml.ws.WebFault(name = "SRPublicationException", targetNamespace = "http://sap.com/esi/uddi/sr/api/ws/", faultBean = "org.eclipse.servicesregistry.proxy.types.SRPublicationException")
public class ServicesRegistrySipublishServicesFault12 extends java.lang.Exception {

  private org.eclipse.servicesregistry.proxy.types.SRPublicationException _ServicesRegistrySipublishServicesFault12;

  public ServicesRegistrySipublishServicesFault12(String message, org.eclipse.servicesregistry.proxy.types.SRPublicationException faultInfo){
    super(message);
    this._ServicesRegistrySipublishServicesFault12 = faultInfo;
  }

  public ServicesRegistrySipublishServicesFault12(String message, org.eclipse.servicesregistry.proxy.types.SRPublicationException faultInfo, Throwable cause){
    super(message, cause);
    this._ServicesRegistrySipublishServicesFault12 = faultInfo;
  }

  public org.eclipse.servicesregistry.proxy.types.SRPublicationException getFaultInfo(){
    return this._ServicesRegistrySipublishServicesFault12;
  }

}
