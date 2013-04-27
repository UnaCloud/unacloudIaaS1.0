package com.losandes.physicalmachine;

import com.losandes.beans.IOperatingSystemServices;
import com.losandes.beans.IOperatingSystemServicesLocal;
import com.losandes.laboratory.ILaboratoryServices;
import com.losandes.persistence.entity.Laboratory;
import com.losandes.persistence.entity.OperatingSystem;
import com.losandes.persistence.entity.PhysicalMachine;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the PhysicalMachine JSF
 */
public class PhysicalMachineBean {

    private PhysicalMachine physicalMachine;
    @EJB
    private IPhysicalMachineServices physicalMachineServices;
    @EJB
    private IOperatingSystemServicesLocal operatingSystemServices;
    @EJB
    private ILaboratoryServices laboratoryServices;
    private OperatingSystem operatingSystem;
    private Laboratory laboratory;
    private String id;
    private int idPhysicalMachine;

    /** Creates a new instance of LaboratoryBean */
    public PhysicalMachineBean() {
        physicalMachine = new PhysicalMachine();
        laboratory = new Laboratory();
        operatingSystem = new OperatingSystem();
    }

    public String clearComponents(){
        physicalMachine = new PhysicalMachine();
        laboratory = new Laboratory();
        operatingSystem = new OperatingSystem();
        return null;
    }
      
    public SelectItem[] getOperatingSystems() {
        List<OperatingSystem> opts = operatingSystemServices.getOperatingSystems();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getOperatingSystemCode(), opts.get(i).getOperatingSystemName());
        }
        return items;
    }

    public SelectItem[] getLaboratories() {
        List<Laboratory> opts = laboratoryServices.getLaboratories();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getLaboratoryCode(), opts.get(i).getLaboratoryName());
        }
        return items;
    }

    public String createOrUpdate() {
        physicalMachine.setOperatingSystem(operatingSystem);
        physicalMachine.setPhysicalMachineUser(NOTHING_AVAILABLE);
        physicalMachine.setLaboratory(laboratory);
        String m=physicalMachineServices.createOrUpdatePhysicalMachine(physicalMachine);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,m,""));
        return null;
    }

    public String findByID() {
        physicalMachine = physicalMachineServices.getPhysicalMachineByName(getId());
        laboratory = physicalMachine.getLaboratory();
        operatingSystem = physicalMachine.getOperatingSystem();
        return null;
    }

    public String delete() {
        if (!physicalMachineServices.deletePhysicalMachine(getId())) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"This physical machine can't be deleted",""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"The physical machine was deleted",""));
        }
        physicalMachine = null;
        operatingSystem = null;
        return null;
    }

    public List getAllphysicalMachines() {
        return physicalMachineServices.getPhysicalMachines();
    }

    /**
     * @return the physicalMachine
     */
    public PhysicalMachine getPhysicalMachine() {
        return physicalMachine;
    }

    /**
     * @param physicalMachine the physicalMachine to set
     */
    public void setPhysicalMachine(PhysicalMachine physicalMachine) {
        this.physicalMachine = physicalMachine;
    }

    /**
     * @return the laboratory
     */
    public Laboratory getLaboratory() {
        return laboratory;
    }

    /**
     * @param laboratory the laboratory to set
     */
    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    /**
     * @return the idPhysicalMachine
     */
    public int getIdPhysicalMachine() {
        return idPhysicalMachine;
    }

    /**
     * @param idPhysicalMachine the idPhysicalMachine to set
     */
    public void setIdPhysicalMachine(int idPhysicalMachine) {
        this.idPhysicalMachine = idPhysicalMachine;
    }

    /**
     * @return the operatingSystemServices
     */
    public IOperatingSystemServicesLocal getOperatingSystemServices() {
        return operatingSystemServices;
    }

    /**
     * @param operatingSystemServices the operatingSystemServices to set
     */
    public void setOperatingSystemServices(IOperatingSystemServicesLocal operatingSystemServices) {
        this.operatingSystemServices = operatingSystemServices;
    }

    /**
     * @return the operatingSystem
     */
    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * @param operatingSystem the operatingSystem to set
     */
    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    /**
     * @return the laboratoryServices
     */
    public ILaboratoryServices getLaboratoryServices() {
        return laboratoryServices;
    }

    /**
     * @param laboratoryServices the laboratoryServices to set
     */
    public void setLaboratoryServices(ILaboratoryServices laboratoryServices) {
        this.laboratoryServices = laboratoryServices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    

    

}
