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
@javax.xml.ws.WebFault(name = "SRException", targetNamespace = "http://sap.com/esi/uddi/sr/api/ws/", faultBean = "org.eclipse.servicesregistry.proxy.types.SRException")
public class ServicesRegistrySifindClassificationSystemsPerPhysicalSystemsFault12 extends java.lang.Exception {

  private org.eclipse.servicesregistry.proxy.types.SRException _ServicesRegistrySifindClassificationSystemsPerPhysicalSystemsFault12;

  public ServicesRegistrySifindClassificationSystemsPerPhysicalSystemsFault12(String message, org.eclipse.servicesregistry.proxy.types.SRException faultInfo){
    super(message);
    this._ServicesRegistrySifindClassificationSystemsPerPhysicalSystemsFault12 = faultInfo;
  }

  public ServicesRegistrySifindClassificationSystemsPerPhysicalSystemsFault12(String message, org.eclipse.servicesregistry.proxy.types.SRException faultInfo, Throwable cause){
    super(message, cause);
    this._ServicesRegistrySifindClassificationSystemsPerPhysicalSystemsFault12 = faultInfo;
  }

  public org.eclipse.servicesregistry.proxy.types.SRException getFaultInfo(){
    return this._ServicesRegistrySifindClassificationSystemsPerPhysicalSystemsFault12;
  }

}
