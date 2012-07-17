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
 * <p>Java class for groupClassificationSystem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="groupClassificationSystem">
 *   &lt;complexContent>
 *     &lt;extension base="{http://sap.com/esi/uddi/sr/api/ws/}classificationSystem">
 *       &lt;sequence>
 *         &lt;element name="constituentClassificationSystems" type="{http://sap.com/esi/uddi/sr/api/ws/}classificationSystem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "groupClassificationSystem", propOrder = {
    "constituentClassificationSystems"
})
public class GroupClassificationSystem
    extends ClassificationSystem
{

    @XmlElement(nillable = true)
    protected List<ClassificationSystem> constituentClassificationSystems;

    /**
     * Gets the value of the constituentClassificationSystems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the constituentClassificationSystems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConstituentClassificationSystems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassificationSystem }
     * 
     * 
     */
    public List<ClassificationSystem> getConstituentClassificationSystems() {
        if (constituentClassificationSystems == null) {
            constituentClassificationSystems = new ArrayList<ClassificationSystem>();
        }
        return this.constituentClassificationSystems;
    }

}
