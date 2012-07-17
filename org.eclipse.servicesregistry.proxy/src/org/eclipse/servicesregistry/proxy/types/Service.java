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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for service complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="service">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descriptions" type="{http://sap.com/esi/uddi/sr/api/ws/}description" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="endpoints" type="{http://sap.com/esi/uddi/sr/api/ws/}serviceEndpoint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="serviceKey" type="{http://sap.com/esi/uddi/sr/api/ws/}serviceKey" minOccurs="0"/>
 *         &lt;element name="serviceQName" type="{http://www.w3.org/2001/XMLSchema}QName" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "service", propOrder = {
    "descriptions",
    "endpoints",
    "serviceKey",
    "serviceQName"
})
public class Service {

    @XmlElement(nillable = true)
    protected List<Description> descriptions;
    @XmlElement(nillable = true)
    protected List<ServiceEndpoint> endpoints;
    protected ServiceKey serviceKey;
    protected QName serviceQName;

    /**
     * Gets the value of the descriptions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the descriptions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescriptions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Description }
     * 
     * 
     */
    public List<Description> getDescriptions() {
        if (descriptions == null) {
            descriptions = new ArrayList<Description>();
        }
        return this.descriptions;
    }

    /**
     * Gets the value of the endpoints property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the endpoints property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEndpoints().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceEndpoint }
     * 
     * 
     */
    public List<ServiceEndpoint> getEndpoints() {
        if (endpoints == null) {
            endpoints = new ArrayList<ServiceEndpoint>();
        }
        return this.endpoints;
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
