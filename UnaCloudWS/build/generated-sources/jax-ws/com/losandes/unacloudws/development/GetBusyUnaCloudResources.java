
package com.losandes.unacloudws.development;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para getBusyUnaCloudResources complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getBusyUnaCloudResources">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="machineDisk" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="machineCores" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="machineRam" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getBusyUnaCloudResources", propOrder = {
    "machineDisk",
    "machineCores",
    "machineRam"
})
public class GetBusyUnaCloudResources {

    protected int machineDisk;
    protected int machineCores;
    protected int machineRam;

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

}
