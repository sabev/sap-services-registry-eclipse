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
 * <p>Java class for classificationSystemValueListFlat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="classificationSystemValueListFlat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="classificationSystemValue" type="{http://sap.com/esi/uddi/sr/api/ws/}classificationSystemValueFlat" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listDescrption" type="{http://sap.com/esi/uddi/sr/api/ws/}listDescription" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "classificationSystemValueListFlat", propOrder = {
    "classificationSystemValue",
    "listDescrption",
    "version"
})
public class ClassificationSystemValueListFlat {

    @XmlElement(nillable = true)
    protected List<ClassificationSystemValueFlat> classificationSystemValue;
    protected ListDescription listDescrption;
    protected long version;

    /**
     * Gets the value of the classificationSystemValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classificationSystemValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassificationSystemValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassificationSystemValueFlat }
     * 
     * 
     */
    public List<ClassificationSystemValueFlat> getClassificationSystemValue() {
        if (classificationSystemValue == null) {
            classificationSystemValue = new ArrayList<ClassificationSystemValueFlat>();
        }
        return this.classificationSystemValue;
    }

    /**
     * Gets the value of the listDescrption property.
     * 
     * @return
     *     possible object is
     *     {@link ListDescription }
     *     
     */
    public ListDescription getListDescrption() {
        return listDescrption;
    }

    /**
     * Sets the value of the listDescrption property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListDescription }
     *     
     */
    public void setListDescrption(ListDescription value) {
        this.listDescrption = value;
    }

    /**
     * Gets the value of the version property.
     * 
     */
    public long getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     */
    public void setVersion(long value) {
        this.version = value;
    }

}
