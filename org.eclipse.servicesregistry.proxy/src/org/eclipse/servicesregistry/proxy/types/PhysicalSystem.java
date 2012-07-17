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
 * <p>Java class for physicalSystem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="physicalSystem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="classifications" type="{http://sap.com/esi/uddi/sr/api/ws/}classifications" minOccurs="0"/>
 *         &lt;element name="host" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="physicalSystemKey" type="{http://sap.com/esi/uddi/sr/api/ws/}physicalSystemKey" minOccurs="0"/>
 *         &lt;element name="systemName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "physicalSystem", propOrder = {
    "classifications",
    "host",
    "physicalSystemKey",
    "systemName",
    "type"
})
public class PhysicalSystem {

    protected Classifications classifications;
    protected String host;
    protected PhysicalSystemKey physicalSystemKey;
    protected String systemName;
    protected byte type;

    /**
     * Gets the value of the classifications property.
     * 
     * @return
     *     possible object is
     *     {@link Classifications }
     *     
     */
    public Classifications getClassifications() {
        return classifications;
    }

    /**
     * Sets the value of the classifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link Classifications }
     *     
     */
    public void setClassifications(Classifications value) {
        this.classifications = value;
    }

    /**
     * Gets the value of the host property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the value of the host property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Gets the value of the physicalSystemKey property.
     * 
     * @return
     *     possible object is
     *     {@link PhysicalSystemKey }
     *     
     */
    public PhysicalSystemKey getPhysicalSystemKey() {
        return physicalSystemKey;
    }

    /**
     * Sets the value of the physicalSystemKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhysicalSystemKey }
     *     
     */
    public void setPhysicalSystemKey(PhysicalSystemKey value) {
        this.physicalSystemKey = value;
    }

    /**
     * Gets the value of the systemName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * Sets the value of the systemName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemName(String value) {
        this.systemName = value;
    }

    /**
     * Gets the value of the type property.
     * 
     */
    public byte getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     */
    public void setType(byte value) {
        this.type = value;
    }

}
