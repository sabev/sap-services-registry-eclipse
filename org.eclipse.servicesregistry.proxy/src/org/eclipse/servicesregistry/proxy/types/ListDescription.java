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
 * <p>Java class for listDescription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listDescription">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actualCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="includedCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="listHead" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listDescription", propOrder = {
    "actualCount",
    "includedCount",
    "listHead"
})
public class ListDescription {

    protected int actualCount;
    protected int includedCount;
    protected int listHead;

    /**
     * Gets the value of the actualCount property.
     * 
     */
    public int getActualCount() {
        return actualCount;
    }

    /**
     * Sets the value of the actualCount property.
     * 
     */
    public void setActualCount(int value) {
        this.actualCount = value;
    }

    /**
     * Gets the value of the includedCount property.
     * 
     */
    public int getIncludedCount() {
        return includedCount;
    }

    /**
     * Sets the value of the includedCount property.
     * 
     */
    public void setIncludedCount(int value) {
        this.includedCount = value;
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

}
