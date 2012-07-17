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
 * <p>Java class for classificationReferenceGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="classificationReferenceGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="classificationReferences" type="{http://sap.com/esi/uddi/sr/api/ws/}classificationReference" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "classificationReferenceGroup", propOrder = {
    "classificationReferences",
    "classificationSystemKey"
})
public class ClassificationReferenceGroup {

    @XmlElement(nillable = true)
    protected List<ClassificationReference> classificationReferences;
    protected ClassificationSystemKey classificationSystemKey;

    /**
     * Gets the value of the classificationReferences property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classificationReferences property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassificationReferences().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassificationReference }
     * 
     * 
     */
    public List<ClassificationReference> getClassificationReferences() {
        if (classificationReferences == null) {
            classificationReferences = new ArrayList<ClassificationReference>();
        }
        return this.classificationReferences;
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
