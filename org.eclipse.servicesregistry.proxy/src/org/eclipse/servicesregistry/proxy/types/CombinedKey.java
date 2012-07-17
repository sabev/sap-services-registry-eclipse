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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for combinedKey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="combinedKey">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="logicalKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uddiKey" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "combinedKey", propOrder = {
    "logicalKey",
    "uddiKey"
})
public abstract class CombinedKey {

    protected String logicalKey;
    @XmlSchemaType(name = "anyURI")
    protected String uddiKey;

    /**
     * Gets the value of the logicalKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogicalKey() {
        return logicalKey;
    }

    /**
     * Sets the value of the logicalKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogicalKey(String value) {
        this.logicalKey = value;
    }

    /**
     * Gets the value of the uddiKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUddiKey() {
        return uddiKey;
    }

    /**
     * Sets the value of the uddiKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUddiKey(String value) {
        this.uddiKey = value;
    }

}
