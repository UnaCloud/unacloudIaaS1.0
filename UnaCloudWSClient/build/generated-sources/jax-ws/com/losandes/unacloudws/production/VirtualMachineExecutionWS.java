
package com.losandes.unacloudws.production;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para virtualMachineExecutionWS complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="virtualMachineExecutionWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="systemUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="template" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="virtualMachine" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="virtualMachineExecutionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virtualMachineExecutionCores" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="virtualMachineExecutionHardDisk" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="virtualMachineExecutionIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virtualMachineExecutionRAMMemory" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="virtualMachineExecutionStart" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="virtualMachineExecutionStatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="virtualMachineExecutionStatusMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virtualMachineExecutionStop" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="virtualMachineName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "virtualMachineExecutionWS", propOrder = {
    "systemUser",
    "template",
    "virtualMachine",
    "virtualMachineExecutionCode",
    "virtualMachineExecutionCores",
    "virtualMachineExecutionHardDisk",
    "virtualMachineExecutionIP",
    "virtualMachineExecutionRAMMemory",
    "virtualMachineExecutionStart",
    "virtualMachineExecutionStatus",
    "virtualMachineExecutionStatusMessage",
    "virtualMachineExecutionStop",
    "virtualMachineName"
})
public class VirtualMachineExecutionWS {

    protected String systemUser;
    protected Integer template;
    protected Integer virtualMachine;
    protected String virtualMachineExecutionCode;
    protected int virtualMachineExecutionCores;
    protected long virtualMachineExecutionHardDisk;
    protected String virtualMachineExecutionIP;
    protected int virtualMachineExecutionRAMMemory;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar virtualMachineExecutionStart;
    protected int virtualMachineExecutionStatus;
    protected String virtualMachineExecutionStatusMessage;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar virtualMachineExecutionStop;
    protected String virtualMachineName;

    /**
     * Obtiene el valor de la propiedad systemUser.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemUser() {
        return systemUser;
    }

    /**
     * Define el valor de la propiedad systemUser.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemUser(String value) {
        this.systemUser = value;
    }

    /**
     * Obtiene el valor de la propiedad template.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTemplate() {
        return template;
    }

    /**
     * Define el valor de la propiedad template.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTemplate(Integer value) {
        this.template = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachine.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVirtualMachine() {
        return virtualMachine;
    }

    /**
     * Define el valor de la propiedad virtualMachine.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVirtualMachine(Integer value) {
        this.virtualMachine = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineExecutionCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualMachineExecutionCode() {
        return virtualMachineExecutionCode;
    }

    /**
     * Define el valor de la propiedad virtualMachineExecutionCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualMachineExecutionCode(String value) {
        this.virtualMachineExecutionCode = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineExecutionCores.
     * 
     */
    public int getVirtualMachineExecutionCores() {
        return virtualMachineExecutionCores;
    }

    /**
     * Define el valor de la propiedad virtualMachineExecutionCores.
     * 
     */
    public void setVirtualMachineExecutionCores(int value) {
        this.virtualMachineExecutionCores = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineExecutionHardDisk.
     * 
     */
    public long getVirtualMachineExecutionHardDisk() {
        return virtualMachineExecutionHardDisk;
    }

    /**
     * Define el valor de la propiedad virtualMachineExecutionHardDisk.
     * 
     */
    public void setVirtualMachineExecutionHardDisk(long value) {
        this.virtualMachineExecutionHardDisk = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineExecutionIP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualMachineExecutionIP() {
        return virtualMachineExecutionIP;
    }

    /**
     * Define el valor de la propiedad virtualMachineExecutionIP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualMachineExecutionIP(String value) {
        this.virtualMachineExecutionIP = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineExecutionRAMMemory.
     * 
     */
    public int getVirtualMachineExecutionRAMMemory() {
        return virtualMachineExecutionRAMMemory;
    }

    /**
     * Define el valor de la propiedad virtualMachineExecutionRAMMemory.
     * 
     */
    public void setVirtualMachineExecutionRAMMemory(int value) {
        this.virtualMachineExecutionRAMMemory = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineExecutionStart.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVirtualMachineExecutionStart() {
        return virtualMachineExecutionStart;
    }

    /**
     * Define el valor de la propiedad virtualMachineExecutionStart.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVirtualMachineExecutionStart(XMLGregorianCalendar value) {
        this.virtualMachineExecutionStart = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineExecutionStatus.
     * 
     */
    public int getVirtualMachineExecutionStatus() {
        return virtualMachineExecutionStatus;
    }

    /**
     * Define el valor de la propiedad virtualMachineExecutionStatus.
     * 
     */
    public void setVirtualMachineExecutionStatus(int value) {
        this.virtualMachineExecutionStatus = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineExecutionStatusMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualMachineExecutionStatusMessage() {
        return virtualMachineExecutionStatusMessage;
    }

    /**
     * Define el valor de la propiedad virtualMachineExecutionStatusMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualMachineExecutionStatusMessage(String value) {
        this.virtualMachineExecutionStatusMessage = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineExecutionStop.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getVirtualMachineExecutionStop() {
        return virtualMachineExecutionStop;
    }

    /**
     * Define el valor de la propiedad virtualMachineExecutionStop.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setVirtualMachineExecutionStop(XMLGregorianCalendar value) {
        this.virtualMachineExecutionStop = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualMachineName() {
        return virtualMachineName;
    }

    /**
     * Define el valor de la propiedad virtualMachineName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualMachineName(String value) {
        this.virtualMachineName = value;
    }

}
