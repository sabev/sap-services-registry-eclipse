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
import javax.xml.namespace.QName;


/**
 * <p>Java class for classificationSystemKey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="classificationSystemKey">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="qname" type="{http://www.w3.org/2001/XMLSchema}QName" minOccurs="0"/>
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
@XmlType(name = "classificationSystemKey", propOrder = {
    "qname",
    "uddiKey"
})
public class ClassificationSystemKey {

    protected QName qname;
    @XmlSchemaType(name = "anyURI")
    protected String uddiKey;

    /**
     * Gets the value of the qname property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getQname() {
        return qname;
    }

    /**
     * Sets the value of the qname property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setQname(QName value) {
        this.qname = value;
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
