
package com.losandes.unacloudws.production;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para getAvailableVirtualMachines complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getAvailableVirtualMachines">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="templateSelected" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="virtualMachineDisk" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="virtualMachineCores" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="virtualMachineRAM" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAvailableVirtualMachines", propOrder = {
    "templateSelected",
    "virtualMachineDisk",
    "virtualMachineCores",
    "virtualMachineRAM",
    "user",
    "password"
})
public class GetAvailableVirtualMachines {

    protected int templateSelected;
    protected int virtualMachineDisk;
    protected int virtualMachineCores;
    protected int virtualMachineRAM;
    protected String user;
    protected String password;

    /**
     * Obtiene el valor de la propiedad templateSelected.
     * 
     */
    public int getTemplateSelected() {
        return templateSelected;
    }

    /**
     * Define el valor de la propiedad templateSelected.
     * 
     */
    public void setTemplateSelected(int value) {
        this.templateSelected = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineDisk.
     * 
     */
    public int getVirtualMachineDisk() {
        return virtualMachineDisk;
    }

    /**
     * Define el valor de la propiedad virtualMachineDisk.
     * 
     */
    public void setVirtualMachineDisk(int value) {
        this.virtualMachineDisk = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineCores.
     * 
     */
    public int getVirtualMachineCores() {
        return virtualMachineCores;
    }

    /**
     * Define el valor de la propiedad virtualMachineCores.
     * 
     */
    public void setVirtualMachineCores(int value) {
        this.virtualMachineCores = value;
    }

    /**
     * Obtiene el valor de la propiedad virtualMachineRAM.
     * 
     */
    public int getVirtualMachineRAM() {
        return virtualMachineRAM;
    }

    /**
     * Define el valor de la propiedad virtualMachineRAM.
     * 
     */
    public void setVirtualMachineRAM(int value) {
        this.virtualMachineRAM = value;
    }

    /**
     * Obtiene el valor de la propiedad user.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Define el valor de la propiedad user.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Obtiene el valor de la propiedad password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define el valor de la propiedad password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

}
