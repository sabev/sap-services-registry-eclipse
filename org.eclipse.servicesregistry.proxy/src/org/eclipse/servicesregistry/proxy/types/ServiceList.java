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


/**
 * <p>Java class for serviceList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listDesrciption" type="{http://sap.com/esi/uddi/sr/api/ws/}listDescription" minOccurs="0"/>
 *         &lt;element name="service" type="{http://sap.com/esi/uddi/sr/api/ws/}service" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceList", propOrder = {
    "listDesrciption",
    "service"
})
public class ServiceList {

    protected ListDescription listDesrciption;
    @XmlElement(nillable = true)
    protected List<Service> service;

    /**
     * Gets the value of the listDesrciption property.
     * 
     * @return
     *     possible object is
     *     {@link ListDescription }
     *     
     */
    public ListDescription getListDesrciption() {
        return listDesrciption;
    }

    /**
     * Sets the value of the listDesrciption property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListDescription }
     *     
     */
    public void setListDesrciption(ListDescription value) {
        this.listDesrciption = value;
    }

    /**
     * Gets the value of the service property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the service property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Service }
     * 
     * 
     */
    public List<Service> getService() {
        if (service == null) {
            service = new ArrayList<Service>();
        }
        return this.service;
    }

}
