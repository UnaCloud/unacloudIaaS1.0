package com.losandes.virtualmachineexecution;

import com.losandes.persistence.entity.Hypervisor;
import com.losandes.persistence.entity.Laboratory;
import com.losandes.persistence.entity.OperatingSystem;
import com.losandes.persistence.entity.OperatingSystemType;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.SystemUser;
import com.losandes.persistence.entity.Template;
import com.losandes.persistence.entity.TemplateType;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.persistence.entity.VirtualMachineExecution;
import com.losandes.persistence.entity.VirtualMachineSecurity;
import com.losandes.user.IUserServices;
import com.losandes.user.UserServices;
import java.util.List;
import javax.ejb.EJB;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the VirtualMachineExecution JSF
 */
public class VirtualMachineExecutionBean {

    @EJB
    private IVirtualMachineExecutionServices virtualMachineExecutionServices;
    @EJB
    private IUserServices userServices;
    private SystemUser systemUser;

    private VirtualMachineExecution virtualMachineExecution;
    private VirtualMachine virtualMachine;
    private PhysicalMachine physicalMachine;
    private Laboratory laboratory;
    private Hypervisor hypervisor;
    private VirtualMachineSecurity virtualMachineSecurity;
    private Template template;
    private TemplateType templateType;
    private OperatingSystem operatingSystem;
    private OperatingSystemType operatingSystemType;

    private String virtualMachineExecutionError;
    private String id;

    /** Creates a new instance of VirtualMachineExecutionBean */
    public VirtualMachineExecutionBean() {
        virtualMachineExecution = new VirtualMachineExecution();
    }

    public String createOrUpdate() {
        if (getVirtualMachineExecution().getVirtualMachineExecutionCode() != null) {
            if (!virtualMachineExecutionServices.updateVirtualMachineExecution(virtualMachineExecution)) {
                setVirtualMachineExecutionError(ERROR_MESSAGE + "the virtual machine execution were not updated");
            } else {
                setVirtualMachineExecutionError("the virtual machine execution was modified");
            }
        } else {
            if (!virtualMachineExecutionServices.createVirtualMachineExecution(virtualMachineExecution)) {
                setVirtualMachineExecutionError(ERROR_MESSAGE + "the virtual machine execution was not created");
            } else {
                setVirtualMachineExecutionError("The virtual machine execution was created");
            }
        }
        return null;
    }

    public String findByID() {
        setVirtualMachineExecution(virtualMachineExecutionServices.getVirtualMachineExecutionByID(getId()));
        setSystemUser(userServices.getUserByID(getVirtualMachineExecution().getSystemUser().getSystemUserName()));
        setVirtualMachine(getVirtualMachineExecution().getVirtualMachine());
        setPhysicalMachine(getVirtualMachine().getPhysicalMachine());
        setLaboratory(getPhysicalMachine().getLaboratory());
        setHypervisor(getVirtualMachine().getHypervisor());
        setVirtualMachineSecurity(getVirtualMachine().getVirtualMachineSecurity());
        setTemplate(getVirtualMachineExecution().getTemplate());
        setTemplateType(getTemplate().getTemplateType());
        setOperatingSystem(getTemplate().getOperatingSystem());
        setOperatingSystemType(operatingSystem.getOperatingSystemType());
        return null;
    }

    public String delete() {
        if (!virtualMachineExecutionServices.deleteVirtualMachineExecution(id)) {
            setVirtualMachineExecutionError(ERROR_MESSAGE + "the virtual machine execution was not deleted");
        } else {
            setVirtualMachineExecutionError("The virtual machine execution was deleted");
        }
        return null;
    }

    public String returnIaaSTraceability(){
        return "MonitorCloudIaaS";
    }

    public List getAllVirtualMachineExecutions() {
        return getVirtualMachineExecutionServices().getVirtualMachineExecutions();
    }

    /**
     * @return the virtualMachineExecutionServices
     */
    public IVirtualMachineExecutionServices getVirtualMachineExecutionServices() {
        return virtualMachineExecutionServices;
    }

    /**
     * @param virtualMachineExecutionServices the virtualMachineExecutionServices to set
     */
    public void setVirtualMachineExecutionServices(IVirtualMachineExecutionServices virtualMachineExecutionServices) {
        this.virtualMachineExecutionServices = virtualMachineExecutionServices;
    }

    /**
     * @return the virtualMachineExecution
     */
    public VirtualMachineExecution getVirtualMachineExecution() {
        return virtualMachineExecution;
    }

    /**
     * @param virtualMachineExecution the virtualMachineExecution to set
     */
    public void setVirtualMachineExecution(VirtualMachineExecution virtualMachineExecution) {
        this.virtualMachineExecution = virtualMachineExecution;
    }

    /**
     * @return the virtualMachineExecutionError
     */
    public String getVirtualMachineExecutionError() {
        return virtualMachineExecutionError;
    }

    /**
     * @param virtualMachineExecutionError the virtualMachineExecutionError to set
     */
    public void setVirtualMachineExecutionError(String virtualMachineExecutionError) {
        this.virtualMachineExecutionError = virtualMachineExecutionError;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    

     /**
     * @return the systemUser
     */
    public SystemUser getSystemUser() {
        return systemUser;
    }

    /**
     * @param systemUser the systemUser to set
     */
    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
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
     * @return the hypervisor
     */
    public Hypervisor getHypervisor() {
        return hypervisor;
    }

    /**
     * @param hypervisor the hypervisor to set
     */
    public void setHypervisor(Hypervisor hypervisor) {
        this.hypervisor = hypervisor;
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
     * @return the template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(Template template) {
        this.template = template;
    }

    /**
     * @return the templateType
     */
    public TemplateType getTemplateType() {
        return templateType;
    }

    /**
     * @param templateType the templateType to set
     */
    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
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
     * @return the operatingSystemType
     */
    public OperatingSystemType getOperatingSystemType() {
        return operatingSystemType;
    }

    /**
     * @param operatingSystemType the operatingSystemType to set
     */
    public void setOperatingSystemType(OperatingSystemType operatingSystemType) {
        this.operatingSystemType = operatingSystemType;
    }
}
