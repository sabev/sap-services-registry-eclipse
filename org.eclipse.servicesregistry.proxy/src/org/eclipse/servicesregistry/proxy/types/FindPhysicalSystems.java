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
 * <p>Java class for findPhysicalSystems complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="findPhysicalSystems">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="physicalSystemSearchAttributes" type="{http://sap.com/esi/uddi/sr/api/ws/}physicalSystemSearchAttributes" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findPhysicalSystems", propOrder = {
    "physicalSystemSearchAttributes"
})
public class FindPhysicalSystems {

    protected PhysicalSystemSearchAttributes physicalSystemSearchAttributes;

    /**
     * Gets the value of the physicalSystemSearchAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link PhysicalSystemSearchAttributes }
     *     
     */
    public PhysicalSystemSearchAttributes getPhysicalSystemSearchAttributes() {
        return physicalSystemSearchAttributes;
    }

    /**
     * Sets the value of the physicalSystemSearchAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhysicalSystemSearchAttributes }
     *     
     */
    public void setPhysicalSystemSearchAttributes(PhysicalSystemSearchAttributes value) {
        this.physicalSystemSearchAttributes = value;
    }

}
