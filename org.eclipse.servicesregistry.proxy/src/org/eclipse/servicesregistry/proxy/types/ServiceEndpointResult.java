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
import javax.xml.namespace.QName;


/**
 * <p>Java class for serviceEndpointResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceEndpointResult">
 *   &lt;complexContent>
 *     &lt;extension base="{http://sap.com/esi/uddi/sr/api/ws/}serviceEndpoint">
 *       &lt;sequence>
 *         &lt;element name="serviceKey" type="{http://sap.com/esi/uddi/sr/api/ws/}serviceKey" minOccurs="0"/>
 *         &lt;element name="serviceQName" type="{http://www.w3.org/2001/XMLSchema}QName" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceEndpointResult", propOrder = {
    "serviceKey",
    "serviceQName"
})
public class ServiceEndpointResult
    extends ServiceEndpoint
{

    protected ServiceKey serviceKey;
    protected QName serviceQName;

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

    /**
     * Gets the value of the serviceQName property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getServiceQName() {
        return serviceQName;
    }

    /**
     * Sets the value of the serviceQName property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setServiceQName(QName value) {
        this.serviceQName = value;
    }

}
