
package com.losandes.virtualmachinesecurity;

import com.losandes.persistence.entity.VirtualMachineSecurity;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the VirtualMachineSecurity JSF
 */
public class VirtualMachineSecurityBean {

    @EJB
    private IVirtualMachineSecurityServices virtualMachineSecurityServices;
    private VirtualMachineSecurity virtualMachineSecurity;
    private String virtualMachineSecurityError;
    private int id;

    public VirtualMachineSecurityBean() {
        virtualMachineSecurity = new VirtualMachineSecurity();
    }



    public String clearComponents(){
        virtualMachineSecurity = new VirtualMachineSecurity();
        return null;
    }

     public String createOrUpdate() {

        if (virtualMachineSecurity.getVirtualMachineSecurityCode() != 0) {
            if (!virtualMachineSecurityServices.updateVirtualMachineSecurity(virtualMachineSecurity)) {
                virtualMachineSecurityError = ERROR_MESSAGE +"the virtual machine security schema were not updated";
            } else {
                virtualMachineSecurityError = "the virtual machine security schema was modified";
            }
        } else {
            if (!virtualMachineSecurityServices.createVirtualMachineSecurity(virtualMachineSecurity)) {
                virtualMachineSecurityError = ERROR_MESSAGE +"the virtual machine security schema was not created";
            } else {
                virtualMachineSecurityError = "The virtual machine security schema was created";
            }
        }
        return null;
    }

    public SelectItem[] getVirtualMachineSecurities() {
        List<VirtualMachineSecurity> opts = getVirtualMachineSecurityServices().getVirtualMachineSecurities();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getVirtualMachineSecurityCode(), opts.get(i).getVirtualMachineSecurityUser());
        }
        return items;
    }

    public String findByID() {
        virtualMachineSecurity = getVirtualMachineSecurityServices().getVirtualMachineSecurityByID(getId());
        return null;
    }

    public String deleteVirtualMachineSecurity() {
        if (!virtualMachineSecurityServices.deleteVirtualMachineSecurity(id)) {
            virtualMachineSecurityError = ERROR_MESSAGE +"the virtual machine security schema was not deleted";
        } else {
            virtualMachineSecurityError = "The virtual machine security schema was deleted";
        }
        return null;
    }

    public List getAllVirtualMachineSecurities() {   
        return virtualMachineSecurityServices.getVirtualMachineSecurities();
    }


    /**
     * @return the virtualMachineSecurity
     */
    public VirtualMachineSecurity getVirtualMachineSecurity() {
        return virtualMachineSecurity;
    }

    /**
     * @param virtualMachineSecurity the virtualMachineSecurity to set
     */
    public void setVirtualMachineSecurity(VirtualMachineSecurity virtualMachineSecurity) {
        this.virtualMachineSecurity = virtualMachineSecurity;
    }

    /**
     * @return the virtualMachineSecurityErrors
     */
    public String getVirtualMachineSecurityError() {
        return virtualMachineSecurityError;
    }

    /**
     * @param virtualMachineSecurityError the virtualMachineSecurityErrors to set
     */
    public void setVirtualMachineSecurityError(String virtualMachineSecurityError) {
        this.virtualMachineSecurityError = virtualMachineSecurityError;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the virtualMachineSecurityServices
     */
    public IVirtualMachineSecurityServices getVirtualMachineSecurityServices() {
        return virtualMachineSecurityServices;
    }

    /**
     * @param virtualMachineSecurityServices the virtualMachineSecurityServices to set
     */
    public void setVirtualMachineSecurityServices(IVirtualMachineSecurityServices virtualMachineSecurityServices) {
        this.virtualMachineSecurityServices = virtualMachineSecurityServices;
    }

}
