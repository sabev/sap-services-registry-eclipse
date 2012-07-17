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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteServiceIdentifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteServiceIdentifier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceDefinitionKey" type="{http://sap.com/esi/uddi/sr/api/ws/}serviceDefinitionKey" minOccurs="0"/>
 *         &lt;element name="serviceKey" type="{http://sap.com/esi/uddi/sr/api/ws/}serviceKey" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteServiceIdentifier", propOrder = {
    "serviceDefinitionKey",
    "serviceKey"
})
public class DeleteServiceIdentifier {

    protected ServiceDefinitionKey serviceDefinitionKey;
    protected ServiceKey serviceKey;

    /**
     * Gets the value of the serviceDefinitionKey property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDefinitionKey }
     *     
     */
    public ServiceDefinitionKey getServiceDefinitionKey() {
        return serviceDefinitionKey;
    }

    /**
     * Sets the value of the serviceDefinitionKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDefinitionKey }
     *     
     */
    public void setServiceDefinitionKey(ServiceDefinitionKey value) {
        this.serviceDefinitionKey = value;
    }

    /**
     * Gets the value of the serviceKey property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceKey }
     *     
     */
    public ServiceKey getServiceKey() {
        return serviceKey;
    }

    /**
     * Sets the value of the serviceKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceKey }
     *     
     */
    public void setServiceKey(ServiceKey value) {
        this.serviceKey = value;
    }

}
