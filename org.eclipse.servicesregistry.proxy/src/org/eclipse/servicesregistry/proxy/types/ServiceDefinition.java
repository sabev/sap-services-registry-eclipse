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
import javax.xml.namespace.QName;


/**
 * <p>Java class for serviceDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceDefinition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="classifications" type="{http://sap.com/esi/uddi/sr/api/ws/}classifications" minOccurs="0"/>
 *         &lt;element name="configState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descriptions" type="{http://sap.com/esi/uddi/sr/api/ws/}description" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="physicalSystemKey" type="{http://sap.com/esi/uddi/sr/api/ws/}physicalSystemKey" minOccurs="0"/>
 *         &lt;element name="qname" type="{http://www.w3.org/2001/XMLSchema}QName" minOccurs="0"/>
 *         &lt;element name="serviceDefinitionKey" type="{http://sap.com/esi/uddi/sr/api/ws/}serviceDefinitionKey" minOccurs="0"/>
 *         &lt;element name="technicalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="wsdlURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceDefinition", propOrder = {
    "classifications",
    "configState",
    "descriptions",
    "documentation",
    "physicalSystemKey",
    "qname",
    "serviceDefinitionKey",
    "technicalName",
    "wsdlURL"
})
public class ServiceDefinition {

    protected Classifications classifications;
    protected String configState;
    @XmlElement(nillable = true)
    protected List<Description> descriptions;
    protected String documentation;
    protected PhysicalSystemKey physicalSystemKey;
    protected QName qname;
    protected ServiceDefinitionKey serviceDefinitionKey;
    protected String technicalName;
    protected String wsdlURL;

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
     * Gets the value of the configState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigState() {
        return configState;
    }

    /**
     * Sets the value of the configState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigState(String value) {
        this.configState = value;
    }

    /**
     * Gets the value of the descriptions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the descriptions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescriptions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Description }
     * 
     * 
     */
    public List<Description> getDescriptions() {
        if (descriptions == null) {
            descriptions = new ArrayList<Description>();
        }
        return this.descriptions;
    }

    /**
     * Gets the value of the documentation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentation() {
        return documentation;
    }

    /**
     * Sets the value of the documentation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentation(String value) {
        this.documentation = value;
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
     * Gets the value of the serviceDefinitionKey property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDefinitionKey }
     *     
     */
    public ServiceDefinitionKey getServiceDefinitionKey() {
        return serviceDefinitionKey;
    }

    /**
     * Sets the value of the serviceDefinitionKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDefinitionKey }
     *     
     */
    public void setServiceDefinitionKey(ServiceDefinitionKey value) {
        this.serviceDefinitionKey = value;
    }

    /**
     * Gets the value of the technicalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTechnicalName() {
        return technicalName;
    }

    /**
     * Sets the value of the technicalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTechnicalName(String value) {
        this.technicalName = value;
    }

    /**
     * Gets the value of the wsdlURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsdlURL() {
        return wsdlURL;
    }

    /**
     * Sets the value of the wsdlURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsdlURL(String value) {
        this.wsdlURL = value;
    }

}
