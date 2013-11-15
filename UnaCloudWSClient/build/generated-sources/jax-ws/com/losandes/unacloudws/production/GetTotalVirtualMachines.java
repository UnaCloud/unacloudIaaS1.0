
package com.losandes.unacloudws.production;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para getTotalVirtualMachines complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getTotalVirtualMachines">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="machineDisk" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="machineCores" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="machineRam" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="templateCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTotalVirtualMachines", propOrder = {
    "machineDisk",
    "machineCores",
    "machineRam",
    "templateCode",
    "username",
    "pass"
})
public class GetTotalVirtualMachines {

    protected int machineDisk;
    protected int machineCores;
    protected int machineRam;
    protected int templateCode;
    protected String username;
    protected String pass;

    /**
     * Obtiene el valor de la propiedad machineDisk.
     * 
     */
    public int getMachineDisk() {
        return machineDisk;
    }

    /**
     * Define el valor de la propiedad machineDisk.
     * 
     */
    public void setMachineDisk(int value) {
        this.machineDisk = value;
    }

    /**
     * Obtiene el valor de la propiedad machineCores.
     * 
     */
    public int getMachineCores() {
        return machineCores;
    }

    /**
     * Define el valor de la propiedad machineCores.
     * 
     */
    public void setMachineCores(int value) {
        this.machineCores = value;
    }

    /**
     * Obtiene el valor de la propiedad machineRam.
     * 
     */
    public int getMachineRam() {
        return machineRam;
    }

    /**
     * Define el valor de la propiedad machineRam.
     * 
     */
    public void setMachineRam(int value) {
        this.machineRam = value;
    }

    /**
     * Obtiene el valor de la propiedad templateCode.
     * 
     */
    public int getTemplateCode() {
        return templateCode;
    }

    /**
     * Define el valor de la propiedad templateCode.
     * 
     */
    public void setTemplateCode(int value) {
        this.templateCode = value;
    }

    /**
     * Obtiene el valor de la propiedad username.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Define el valor de la propiedad username.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Obtiene el valor de la propiedad pass.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPass() {
        return pass;
    }

    /**
     * Define el valor de la propiedad pass.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPass(String value) {
        this.pass = value;
    }

}
