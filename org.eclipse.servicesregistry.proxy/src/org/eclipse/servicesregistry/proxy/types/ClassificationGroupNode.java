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
 * <p>Java class for classificationGroupNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="classificationGroupNode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="classificationGoupNodeValues" type="{http://sap.com/esi/uddi/sr/api/ws/}classificationGroupNodeValue" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="classificationSystemKey" type="{http://sap.com/esi/uddi/sr/api/ws/}classificationSystemKey" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "classificationGroupNode", propOrder = {
    "classificationGoupNodeValues",
    "classificationSystemKey"
})
public class ClassificationGroupNode {

    @XmlElement(nillable = true)
    protected List<ClassificationGroupNodeValue> classificationGoupNodeValues;
    protected ClassificationSystemKey classificationSystemKey;

    /**
     * Gets the value of the classificationGoupNodeValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classificationGoupNodeValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassificationGoupNodeValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassificationGroupNodeValue }
     * 
     * 
     */
    public List<ClassificationGroupNodeValue> getClassificationGoupNodeValues() {
        if (classificationGoupNodeValues == null) {
            classificationGoupNodeValues = new ArrayList<ClassificationGroupNodeValue>();
        }
        return this.classificationGoupNodeValues;
    }

    /**
     * Gets the value of the classificationSystemKey property.
     * 
     * @return
     *     possible object is
     *     {@link ClassificationSystemKey }
     *     
     */
    public ClassificationSystemKey getClassificationSystemKey() {
        return classificationSystemKey;
    }

    /**
     * Sets the value of the classificationSystemKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassificationSystemKey }
     *     
     */
    public void setClassificationSystemKey(ClassificationSystemKey value) {
        this.classificationSystemKey = value;
    }

}
