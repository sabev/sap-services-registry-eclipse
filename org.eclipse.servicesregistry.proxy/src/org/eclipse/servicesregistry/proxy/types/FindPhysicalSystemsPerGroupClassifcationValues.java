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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for findPhysicalSystemsPerGroupClassifcationValues complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="findPhysicalSystemsPerGroupClassifcationValues">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="values" type="{http://sap.com/esi/uddi/sr/api/ws/}classificationGroupNode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listHead" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="maxRows" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findPhysicalSystemsPerGroupClassifcationValues", propOrder = {
    "values",
    "listHead",
    "maxRows"
})
public class FindPhysicalSystemsPerGroupClassifcationValues {

    protected List<ClassificationGroupNode> values;
    protected int listHead;
    protected int maxRows;

    /**
     * Gets the value of the values property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the values property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassificationGroupNode }
     * 
     * 
     */
    public List<ClassificationGroupNode> getValues() {
        if (values == null) {
            values = new ArrayList<ClassificationGroupNode>();
        }
        return this.values;
    }

    /**
     * Gets the value of the listHead property.
     * 
     */
    public int getListHead() {
        return listHead;
    }

    /**
     * Sets the value of the listHead property.
     * 
     */
    public void setListHead(int value) {
        this.listHead = value;
    }

    /**
     * Gets the value of the maxRows property.
     * 
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * Sets the value of the maxRows property.
     * 
     */
    public void setMaxRows(int value) {
        this.maxRows = value;
    }

}
