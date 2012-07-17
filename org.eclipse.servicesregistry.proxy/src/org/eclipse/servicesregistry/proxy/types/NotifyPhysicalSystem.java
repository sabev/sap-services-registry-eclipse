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
import javax.xml.namespace.QName;


/**
 * <p>Java class for notifyPhysicalSystem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="notifyPhysicalSystem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="details" type="{http://sap.com/esi/uddi/sr/api/ws/}notificationDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="notificationQname" type="{http://www.w3.org/2001/XMLSchema}QName" minOccurs="0"/>
 *         &lt;element name="physicalSystemKey" type="{http://sap.com/esi/uddi/sr/api/ws/}physicalSystemKey" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "notifyPhysicalSystem", propOrder = {
    "details",
    "notificationQname",
    "physicalSystemKey"
})
public class NotifyPhysicalSystem {

    protected List<NotificationDetails> details;
    protected QName notificationQname;
    protected PhysicalSystemKey physicalSystemKey;

    /**
     * Gets the value of the details property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the details property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NotificationDetails }
     * 
     * 
     */
    public List<NotificationDetails> getDetails() {
        if (details == null) {
            details = new ArrayList<NotificationDetails>();
        }
        return this.details;
    }

    /**
     * Gets the value of the notificationQname property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getNotificationQname() {
        return notificationQname;
    }

    /**
     * Sets the value of the notificationQname property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setNotificationQname(QName value) {
        this.notificationQname = value;
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

}
