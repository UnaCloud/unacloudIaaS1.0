
package com.losandes.unacloudws.production;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para templateWS complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="templateWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="applications" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="customizable" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="highAvailability" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="operatingSystem" type="{http://wss.losandes.com/}operatingSystemWS" minOccurs="0"/>
 *         &lt;element name="templateCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="templateName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="templateType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "templateWS", propOrder = {
    "applications",
    "customizable",
    "highAvailability",
    "operatingSystem",
    "templateCode",
    "templateName",
    "templateType"
})
public class TemplateWS {

    @XmlElement(nillable = true)
    protected List<String> applications;
    protected boolean customizable;
    protected boolean highAvailability;
    protected OperatingSystemWS operatingSystem;
    protected Integer templateCode;
    protected String templateName;
    protected String templateType;

    /**
     * Gets the value of the applications property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applications property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplications().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getApplications() {
        if (applications == null) {
            applications = new ArrayList<String>();
        }
        return this.applications;
    }

    /**
     * Obtiene el valor de la propiedad customizable.
     * 
     */
    public boolean isCustomizable() {
        return customizable;
    }

    /**
     * Define el valor de la propiedad customizable.
     * 
     */
    public void setCustomizable(boolean value) {
        this.customizable = value;
    }

    /**
     * Obtiene el valor de la propiedad highAvailability.
     * 
     */
    public boolean isHighAvailability() {
        return highAvailability;
    }

    /**
     * Define el valor de la propiedad highAvailability.
     * 
     */
    public void setHighAvailability(boolean value) {
        this.highAvailability = value;
    }

    /**
     * Obtiene el valor de la propiedad operatingSystem.
     * 
     * @return
     *     possible object is
     *     {@link OperatingSystemWS }
     *     
     */
    public OperatingSystemWS getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * Define el valor de la propiedad operatingSystem.
     * 
     * @param value
     *     allowed object is
     *     {@link OperatingSystemWS }
     *     
     */
    public void setOperatingSystem(OperatingSystemWS value) {
        this.operatingSystem = value;
    }

    /**
     * Obtiene el valor de la propiedad templateCode.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTemplateCode() {
        return templateCode;
    }

    /**
     * Define el valor de la propiedad templateCode.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTemplateCode(Integer value) {
        this.templateCode = value;
    }

    /**
     * Obtiene el valor de la propiedad templateName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Define el valor de la propiedad templateName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplateName(String value) {
        this.templateName = value;
    }

    /**
     * Obtiene el valor de la propiedad templateType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemplateType() {
        return templateType;
    }

    /**
     * Define el valor de la propiedad templateType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemplateType(String value) {
        this.templateType = value;
    }

}
