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
 * <p>Java class for findServiceDefinitions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="findServiceDefinitions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceDefinitionSearchAttributes" type="{http://sap.com/esi/uddi/sr/api/ws/}serviceDefinitionSearchAttributes" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findServiceDefinitions", propOrder = {
    "serviceDefinitionSearchAttributes"
})
public class FindServiceDefinitions {

    protected ServiceDefinitionSearchAttributes serviceDefinitionSearchAttributes;

    /**
     * Gets the value of the serviceDefinitionSearchAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDefinitionSearchAttributes }
     *     
     */
    public ServiceDefinitionSearchAttributes getServiceDefinitionSearchAttributes() {
        return serviceDefinitionSearchAttributes;
    }

    /**
     * Sets the value of the serviceDefinitionSearchAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDefinitionSearchAttributes }
     *     
     */
    public void setServiceDefinitionSearchAttributes(ServiceDefinitionSearchAttributes value) {
        this.serviceDefinitionSearchAttributes = value;
    }

}
