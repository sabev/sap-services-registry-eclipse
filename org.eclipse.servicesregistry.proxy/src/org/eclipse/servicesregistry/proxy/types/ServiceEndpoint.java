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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for serviceEndpoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceEndpoint">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bindingKey" type="{http://sap.com/esi/uddi/sr/api/ws/}bindingKey" minOccurs="0"/>
 *         &lt;element name="bindingQName" type="{http://www.w3.org/2001/XMLSchema}QName" minOccurs="0"/>
 *         &lt;element name="bindingTemplateKey" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="bindingWSDL" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="classifications" type="{http://sap.com/esi/uddi/sr/api/ws/}classifications" minOccurs="0"/>
 *         &lt;element name="description" type="{http://sap.com/esi/uddi/sr/api/ws/}description" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="endpointName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="endpointTargetAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="protocol" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="serviceDefinitionKey" type="{http://sap.com/esi/uddi/sr/api/ws/}serviceDefinitionKey" minOccurs="0"/>
 *         &lt;element name="transport" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceEndpoint", propOrder = {
    "bindingKey",
    "bindingQName",
    "bindingTemplateKey",
    "bindingWSDL",
    "classifications",
    "description",
    "endpointName",
    "endpointTargetAddress",
    "protocol",
    "serviceDefinitionKey",
    "transport"
})
public class ServiceEndpoint {

    protected BindingKey bindingKey;
    protected QName bindingQName;
    @XmlSchemaType(name = "anyURI")
    protected String bindingTemplateKey;
    @XmlSchemaType(name = "anyURI")
    protected String bindingWSDL;
    protected Classifications classifications;
    @XmlElement(nillable = true)
    protected List<Description> description;
    protected String endpointName;
    protected String endpointTargetAddress;
    @XmlSchemaType(name = "anyURI")
    protected String protocol;
    protected ServiceDefinitionKey serviceDefinitionKey;
    @XmlSchemaType(name = "anyURI")
    protected String transport;

    /**
     * Gets the value of the bindingKey property.
     * 
     * @return
     *     possible object is
     *     {@link BindingKey }
     *     
     */
    public BindingKey getBindingKey() {
        return bindingKey;
    }

    /**
     * Sets the value of the bindingKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link BindingKey }
     *     
     */
    public void setBindingKey(BindingKey value) {
        this.bindingKey = value;
    }

    /**
     * Gets the value of the bindingQName property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getBindingQName() {
        return bindingQName;
    }

    /**
     * Sets the value of the bindingQName property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setBindingQName(QName value) {
        this.bindingQName = value;
    }

    /**
     * Gets the value of the bindingTemplateKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBindingTemplateKey() {
        return bindingTemplateKey;
    }

    /**
     * Sets the value of the bindingTemplateKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBindingTemplateKey(String value) {
        this.bindingTemplateKey = value;
    }

    /**
     * Gets the value of the bindingWSDL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBindingWSDL() {
        return bindingWSDL;
    }

    /**
     * Sets the value of the bindingWSDL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBindingWSDL(String value) {
        this.bindingWSDL = value;
    }

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
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Description }
     * 
     * 
     */
    public List<Description> getDescription() {
        if (description == null) {
            description = new ArrayList<Description>();
        }
        return this.description;
    }

    /**
     * Gets the value of the endpointName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndpointName() {
        return endpointName;
    }

    /**
     * Sets the value of the endpointName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndpointName(String value) {
        this.endpointName = value;
    }

    /**
     * Gets the value of the endpointTargetAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndpointTargetAddress() {
        return endpointTargetAddress;
    }

    /**
     * Sets the value of the endpointTargetAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndpointTargetAddress(String value) {
        this.endpointTargetAddress = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocol(String value) {
        this.protocol = value;
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
     * Gets the value of the transport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransport() {
        return transport;
    }

    /**
     * Sets the value of the transport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransport(String value) {
        this.transport = value;
    }

}
