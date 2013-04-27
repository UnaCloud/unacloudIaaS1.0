package com.losandes.laboratory;

import com.losandes.persistence.entity.Laboratory;
import com.losandes.persistence.entity.LaboratoryType;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import com.losandes.vo.PhysicalMachineMonitor;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the Laboratory JSF
 */
public class LaboratoryBean {

    private Laboratory laboratory;
    private PhysicalMachine physicalMachine;
    private VirtualMachine virtualMachine;
    @EJB
    private ILaboratoryServices laboratoryServices;
    @EJB
    private IPhysicalMachineServices physicalMachineServices;
    private LaboratoryType laboratoryType;
    private PhysicalMachineMonitor physicalMachineMonitor;
    private String physicalMachineError;
    private String executionMachineError;
    private int id;
    private String namePhysicalMachine;


    /** Creates a new instance of LaboratoryBean */
    public LaboratoryBean() {
        if (laboratory == null) {
            laboratory = new Laboratory();
            physicalMachineError = "";
            executionMachineError = "";
        }
        laboratoryType = new LaboratoryType();
        physicalMachine = new PhysicalMachine();
    }

     public String clearComponents(){
        laboratory = new Laboratory();
        physicalMachineError = "";
        executionMachineError = "";
        return null;
    }

    public SelectItem[] getLaboratoryTypes() {
        physicalMachineError = "";
        executionMachineError = "";
        List<LaboratoryType> opts = laboratoryServices.getLaboratoryTypes();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getLaboratoryTypeCode(), opts.get(i).getLaboratoryTypeName());
        }
        return items;
    }

    public SelectItem[] getLaboratories() {
        physicalMachineError = "";
        executionMachineError = "";
        List<Laboratory> opts = laboratoryServices.getLaboratories();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getLaboratoryCode(), opts.get(i).getLaboratoryName());
        }
        return items;
    }

    public String createOrUpdate() {
        laboratory.setLaboratoryType(getLaboratoryType());
        if (laboratory.getLaboratoryCode() != 0) {
            if (!laboratoryServices.updateLaboratory(laboratory)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"The laboratory was not updated",""));
            } else {
                clearComponents();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"The laboratory was modified",""));
            }
        } else {
            if (!laboratoryServices.createLaboratory(laboratory)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"The laboratory was not created",""));
            } else {
                clearComponents();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"The laboratory was created",""));
            }
            laboratory = null;
        }
        return null;
    }

    public String findByID() {
        physicalMachineError = "";
        executionMachineError = "";
        laboratory = laboratoryServices.getLaboratoryByID(getId());
        setLaboratoryType(laboratory.getLaboratoryType());
        for(PhysicalMachine p:laboratory.getPhysicalMachine()){
            p.setExecutionsSlots(physicalMachineServices.getExecutionSlots(p.getPhysicalMachineName()));
        }
        return null;
    }

    public String findByIDPhysicalMachine() {
        physicalMachineError = "";
        executionMachineError = "";
        physicalMachine = physicalMachineServices.getPhysicalMachineByName(namePhysicalMachine);
        return "ViewPhysicalMachine";
    }

    public String findByIDPhysicalMachineMonitor() {
        physicalMachine = physicalMachineServices.getPhysicalMachineByName(namePhysicalMachine);
        monitorPhysicalMachine();
        return "MonitorPhysicalMachine";
    }

    public String monitorPhysicalMachine() {
        //physicalMachineMonitor = physicalMachineServices.monitorPhysicalMachine(physicalMachine);
        return null;
    }

    public String turnOffPhysicalMachine() {
        ArrayList<PhysicalMachine> executionPhysicalMachines = new ArrayList<PhysicalMachine>();
        for (int i = 0; i < laboratory.getPhysicalMachine().size(); i++) {
            if (laboratory.getPhysicalMachine().get(i).isPhysicalMachineEnable()) {
                executionPhysicalMachines.add(laboratory.getPhysicalMachine().get(i));
            }
        }
        setExecutionMachineError(physicalMachineServices.turnOffPhysicalMachine(executionPhysicalMachines));
        return null;
    }

    public String restartPhysicalMachine() {
        ArrayList<PhysicalMachine> executionPhysicalMachines = new ArrayList<PhysicalMachine>();
        for (int i = 0; i < laboratory.getPhysicalMachine().size(); i++) {
            if (laboratory.getPhysicalMachine().get(i).isPhysicalMachineEnable()) {
                executionPhysicalMachines.add(laboratory.getPhysicalMachine().get(i));
            }
        }
        setExecutionMachineError(physicalMachineServices.restartPhysicalMachine(executionPhysicalMachines));
        return null;
    }

    public String logOutPhysicalMachine() {
        ArrayList<PhysicalMachine> executionPhysicalMachines = new ArrayList<PhysicalMachine>();
        for (int i = 0; i < laboratory.getPhysicalMachine().size(); i++) {
            if (laboratory.getPhysicalMachine().get(i).isPhysicalMachineEnable()) {
                executionPhysicalMachines.add(laboratory.getPhysicalMachine().get(i));
            }
        }
      setExecutionMachineError(physicalMachineServices.logoutPhysicalMachine(executionPhysicalMachines));
        return null;
    }

    public String lockPhysicalMachine() {
        ArrayList<PhysicalMachine> executionPhysicalMachines = new ArrayList<PhysicalMachine>();
        for (int i = 0; i < laboratory.getPhysicalMachine().size(); i++) {
            if (laboratory.getPhysicalMachine().get(i).isPhysicalMachineEnable()) {
                executionPhysicalMachines.add(laboratory.getPhysicalMachine().get(i));
            }
        }
        setExecutionMachineError(physicalMachineServices.lockPhysicalMachine(executionPhysicalMachines));
        return null;
    }

    public String unlockPhysicalMachine() {
        ArrayList<PhysicalMachine> executionPhysicalMachines = new ArrayList<PhysicalMachine>();
        for (int i = 0; i < laboratory.getPhysicalMachine().size(); i++) {
            if (laboratory.getPhysicalMachine().get(i).isPhysicalMachineEnable()) {
                executionPhysicalMachines.add(laboratory.getPhysicalMachine().get(i));
            }
        }
        setExecutionMachineError(physicalMachineServices.unlockPhysicalMachine(executionPhysicalMachines));
        return null;
    }

    public String updatePhysicalMachineAgent() {
        ArrayList<PhysicalMachine> executionPhysicalMachines = new ArrayList<PhysicalMachine>();
        for (int i = 0; i < laboratory.getPhysicalMachine().size(); i++) {
            if (laboratory.getPhysicalMachine().get(i).isPhysicalMachineEnable()) {
                executionPhysicalMachines.add(laboratory.getPhysicalMachine().get(i));
            }
        }
        setExecutionMachineError(physicalMachineServices.updatePhysicalMachineAgent(executionPhysicalMachines));
        return null;
    }
    
    public String monitorPhysicalMachines() {
        ArrayList<PhysicalMachine> executionPhysicalMachines = new ArrayList<PhysicalMachine>();
        for (int i = 0; i < laboratory.getPhysicalMachine().size(); i++) {
            if (laboratory.getPhysicalMachine().get(i).isPhysicalMachineEnable()) {
                executionPhysicalMachines.add(laboratory.getPhysicalMachine().get(i));
            }
        }
        setExecutionMachineError(physicalMachineServices.startMonitorPhysicalMachines(executionPhysicalMachines));
        return null;
    }

    public String stopMonitorPhysicalMachines() {
        ArrayList<PhysicalMachine> executionPhysicalMachines = new ArrayList<PhysicalMachine>();
        for (int i = 0; i < laboratory.getPhysicalMachine().size(); i++) {
            if (laboratory.getPhysicalMachine().get(i).isPhysicalMachineEnable()) {
                executionPhysicalMachines.add(laboratory.getPhysicalMachine().get(i));
            }
        }
        setExecutionMachineError(physicalMachineServices.stopMonitorPhysicalMachines(executionPhysicalMachines));
        return null;
    }



    public String delete() {
        if (!laboratoryServices.deleteLaboratory(id)) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Laboratory was not deleted",""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"The laboratory was deleted",""));
        }
        return null;
    }

    public String laboratoryContext() {
        String bLaboratoryCode = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("referencia");
        String navegacion = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("navegar");
        laboratory = laboratoryServices.getLaboratoryByID(Integer.parseInt(bLaboratoryCode));
        for(PhysicalMachine p:laboratory.getPhysicalMachine()){
            p.setExecutionsSlots(physicalMachineServices.getExecutionSlots(p.getPhysicalMachineName()));
        }
        return navegacion;
    }

    public String returnLaboratorySelection() {
        return "OperateInfrastructure";
    }

    public String returnPhysicalLaboratory() {
        physicalMachine = null;
        physicalMachineError = null;
        return "returnPhysicalLaboratory";
    }

    public String returnVirtualLaboratorySelection() {
        return "DeployManualCloudIaaS";
    }

   public String returnVirtualFastLaboratorySelection() {
        return "DeployFastManualCloudIaaS";
    }


    public String returnMonitorLaboratorySelection() {
        return "MonitorInfrastructure";
    }

    public String returnMonitorPhysicalLaboratory() {
        return "returnMonitorPhysicalLaboratory";
    }


    public List getAllLaboratories() {
        return laboratoryServices.getLaboratories();
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
     * @return the laboratoryType
     */
    public LaboratoryType getLaboratoryType() {
        return laboratoryType;
    }

    /**
     * @param laboratoryType the laboratoryType to set
     */
    public void setLaboratoryType(LaboratoryType laboratoryType) {
        this.laboratoryType = laboratoryType;
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

    public String getNamePhysicalMachine() {
        return namePhysicalMachine;
    }

    public void setNamePhysicalMachine(String namePhysicalMachine) {
        this.namePhysicalMachine = namePhysicalMachine;
    }

    

    /**
     * @return the physicalMachineError
     */
    public String getPhysicalMachineError() {
        return physicalMachineError;
    }

    /**
     * @param physicalMachineError the physicalMachineError to set
     */
    public void setPhysicalMachineError(String physicalMachineError) {
        this.physicalMachineError = physicalMachineError;
    }

    /**
     * @return the executionMachineError
     */
    public String getExecutionMachineError() {
        return executionMachineError;
    }

    /**
     * @param executionMachineError the executionMachineError to set
     */
    public void setExecutionMachineError(String executionMachineError) {
        this.executionMachineError = executionMachineError;
    }

    /**
     * @return the physicalMachineMonitor
     */
    public PhysicalMachineMonitor getPhysicalMachineMonitor() {
        return physicalMachineMonitor;
    }

    /**
     * @param physicalMachineMonitor the physicalMachineMonitor to set
     */
    public void setPhysicalMachineMonitor(PhysicalMachineMonitor physicalMachineMonitor) {
        this.physicalMachineMonitor = physicalMachineMonitor;
    }




    /**
     * @return the virtualMachine
     */
    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    /**
     * @param virtualMachine the virtualMachine to set
     */
    public void setVirtualMachine(VirtualMachine virtualMachine) {
        this.virtualMachine = virtualMachine;
    }
}
