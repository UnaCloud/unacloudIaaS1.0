package com.losandes.virtualmachine;

import com.losandes.beans.IOperatingSystemServicesLocal;
import com.losandes.deploy.IPGenerationPolicy;
import com.losandes.deploy.MACGenerationPolicy;
import com.losandes.hypervisor.IHypervisorServices;
import com.losandes.laboratory.ILaboratoryServices;
import com.losandes.persistence.entity.Hypervisor;
import com.losandes.persistence.entity.Laboratory;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.Template;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.persistence.entity.VirtualMachineExecution;
import com.losandes.persistence.entity.VirtualMachineSecurity;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import com.losandes.template.ITemplateServices;
import com.losandes.virtualmachinesecurity.IVirtualMachineSecurityServices;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the VirtualMachine JSF
 */
public class VirtualMachineBean {

    long idid=System.currentTimeMillis();
    private Laboratory laboratory;
    private PhysicalMachine physicalMachine;
    private VirtualMachine virtualMachine;
    @EJB
    private ILaboratoryServices laboratoryServices;
    @EJB
    private IPhysicalMachineServices physicalMachineServices;
    @EJB
    private IVirtualMachineServices virtualMachineServices;
    @EJB
    private IVirtualMachineSecurityServices virtualMachineSecurityServices;
    @EJB
    private IOperatingSystemServicesLocal operatingSystemServices;
    @EJB
    private ITemplateServices templateServices;
    @EJB
    private IHypervisorServices hypervisorServices;
    private VirtualMachineExecution virtualMachineExecution;
    private Template template;
    private Hypervisor hypervisor;
    private VirtualMachineSecurity virtualMachineSecurity;
    private String physicalMachineError;
    private String namePhysicalMachine;
    private int id;
    private String exId;
    private int idvirtualMachine;
    private int executionTimeSelected;
    private int executionTimeChanged;
    private String templateSelected = "0";
    private int virtualMachineRAMSelected;
    private int virtualMachineCoresSelected;
    public ArrayList<PhysicalMachine> executionGridPhysicalMachines;
    public ArrayList<PhysicalMachine> executionFastGridPhysicalMachines;

    private String filterTemplateName;


    /** Creates a new instance of VirtualMachineBean */
    public VirtualMachineBean() {
        virtualMachine = new VirtualMachine();
        physicalMachineError = "";
        physicalMachine = new PhysicalMachine();
        hypervisor = new Hypervisor();
        template = new Template();
        virtualMachineSecurity = new VirtualMachineSecurity();
    }

    public String clearComponents(){
        physicalMachineError = "";
        virtualMachine = new VirtualMachine();
        physicalMachine = new PhysicalMachine();
        return null;
    }

    public SelectItem[] getPhysicalMachines() {
        List<PhysicalMachine> opts = physicalMachineServices.getPhysicalMachines();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getPhysicalMachineName(), opts.get(i).getPhysicalMachineName());
        }
        return items;
    }

    public SelectItem[] getHypervisors() {
        List<Hypervisor> opts = hypervisorServices.getHypervisors();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getHypervisorCode(), opts.get(i).getHypervisorName());
        }
        return items;
    }

    public SelectItem[] getVirtualMachineSecurities() {
        List<VirtualMachineSecurity> opts = virtualMachineSecurityServices.getVirtualMachineSecurities();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getVirtualMachineSecurityCode(), opts.get(i).getVirtualMachineSecurityName());
        }
        return items;
    }

    public SelectItem[] getTemplates() {
        List<Template> opts = templateServices.getTemplates();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getTemplateCode(), opts.get(i).getTemplateName());
        }
        return items;
    }

    public String createOrUpdate() {
        virtualMachine.setVirtualMachineRAMMemory(virtualMachine.getVirtualMachineRAMMemory()-virtualMachine.getVirtualMachineRAMMemory()%4);
        virtualMachine.setPhysicalMachine(getPhysicalMachine());
        virtualMachine.setHypervisor(hypervisor);
        virtualMachine.setTemplate(template);
        virtualMachine.setVirtualMachineSecurity(virtualMachineSecurity);
        if (getVirtualMachine().getVirtualMachineCode() != 0) {
            if (!virtualMachineServices.updateVirtualMachine(virtualMachine)) {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"the virtual machine was not updated",""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"the virtual machine was updated",""));
            }
        } else {
            if (!virtualMachineServices.createVirtualMachine(virtualMachine)) {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"virtual machine was not created",""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"virtual machine was created",""));
            }
        }
        return null;
    }

    public String delete() {
        if (!virtualMachineServices.deleteVirtualMachine(id)) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"the virtual machine was not deleted",""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"the virtual machine was deleted",""));
        }
        virtualMachine = new VirtualMachine();
        hypervisor = new Hypervisor();
        template = new Template();
        virtualMachineSecurity = new VirtualMachineSecurity();
        return null;
    }

    public List getAllVirtualMachines() {
        return virtualMachineServices.getVirtualMachines();
    }

    public String virtualLaboratoryContext() {
        String bLaboratoryCode = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("referencia");
        String navegacion = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("navegar");
        laboratory = laboratoryServices.getLaboratoryByID(Integer.parseInt(bLaboratoryCode));
        return navegacion;
    }

    public String findByID() {
        virtualMachine = virtualMachineServices.getVirtualMachineByID(id);
        physicalMachine = virtualMachine.getPhysicalMachine();
        hypervisor = virtualMachine.getHypervisor();
        template = virtualMachine.getTemplate();
        virtualMachineSecurity = virtualMachine.getVirtualMachineSecurity();
        return null;
    }

    public String findByIDVirtualMachine() {
        /*setPhysicalMachine(physicalMachineServices.getPhysicalMachineByName(getNamePhysicalMachine()));
        if (getPhysicalMachine().getVirtualMachines() != null) {
            for (VirtualMachine virMac : getPhysicalMachine().getVirtualMachines()) {
                if (virMac.getTemplate().getTemplateCode() == GridBean.templateSelectedGrid) {
                    virtualMachine = virMac;
                    return "ViewVirtualMachine";
                }
            }
        }*/
        return "VirtualLaboratory";
    }

    public String returnVirtualLaboratory() {
        physicalMachineError = "";
        return "returnVirtualLaboratory";
    }

    public String returnCloudManagement() {
        return "returnCloudManagement";
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
     * @return the virtualMachineServices
     */
    public IVirtualMachineServices getVirtualMachineServices() {
        return virtualMachineServices;
    }

    /**
     * @param virtualMachineServices the virtualMachineServices to set
     */
    public void setVirtualMachineServices(IVirtualMachineServices virtualMachineServices) {
        this.virtualMachineServices = virtualMachineServices;
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
     * @return the idvirtualMachine
     */
    public int getIdvirtualMachine() {
        return idvirtualMachine;
    }

    /**
     * @param idvirtualMachine the idvirtualMachine to set
     */
    public void setIdvirtualMachine(int idvirtualMachine) {
        this.idvirtualMachine = idvirtualMachine;
    }

    /**
     * @return the executionTimeSelected
     */
    public int getExecutionTimeSelected() {
        return executionTimeSelected;
    }

    /**
     * @param executionTimeSelected the executionTimeSelected to set
     */
    public void setExecutionTimeSelected(int executionTimeSelected) {
        this.executionTimeSelected = executionTimeSelected;
    }

    /**
     * @return the virtualmachineRAMSelected
     */
    public int getVirtualMachineRAMSelected() {
        return virtualMachineRAMSelected;
    }

    /**
     * @param virtualmachineRAMSelected the virtualmachineRAMSelected to set
     */
    public void setVirtualMachineRAMSelected(int virtualmachineRAMSelected) {
        this.virtualMachineRAMSelected = virtualmachineRAMSelected;
    }

    /**
     * @return the virtualMachineCoresSelected
     */
    public int getVirtualMachineCoresSelected() {
        return virtualMachineCoresSelected;
    }

    /**
     * @param virtualMachineCoresSelected the virtualMachineCoresSelected to set
     */
    public void setVirtualMachineCoresSelected(int virtualMachineCoresSelected) {
        this.virtualMachineCoresSelected = virtualMachineCoresSelected;
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
     * @return the templateServices
     */
    public ITemplateServices getTemplateServices() {
        return templateServices;
    }

    /**
     * @param templateServices the templateServices to set
     */
    public void setTemplateServices(ITemplateServices templateServices) {
        this.templateServices = templateServices;
    }

    /**
     * @return the templateSelected
     */
    public String getTemplateSelected() {
        return templateSelected;
    }

    /**
     * @param templateSelected the templateSelected to set
     */
    public void setTemplateSelected(String templateSelected) {
        if (templateSelected == null) {
            this.templateSelected = "1";
        } else {
            this.templateSelected = templateSelected;
        }
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

    public String getRemainingExecutionTime() {
        Date remainingExecutionTime = new Date();
        if(remainingExecutionTime!=null)
        {
        remainingExecutionTime.setHours(virtualMachineExecution.getVirtualMachineExecutionTime().getHours() - remainingExecutionTime.getHours());
        remainingExecutionTime.setMinutes(virtualMachineExecution.getVirtualMachineExecutionTime().getMinutes() - remainingExecutionTime.getMinutes());
        remainingExecutionTime.setSeconds(virtualMachineExecution.getVirtualMachineExecutionTime().getSeconds() - remainingExecutionTime.getSeconds());
        }
        else
            return "N/A";

        Format formatter = new SimpleDateFormat("HH-mm-ss");
        return formatter.format(remainingExecutionTime);
    }

    /**
     * @return the executionTimeChanged
     */
    public int getExecutionTimeChanged() {
        return executionTimeChanged;
    }

    /**
     * @param executionTimeChanged the executionTimeChanged to set
     */
    public void setExecutionTimeChanged(int executionTimeChanged) {
        this.executionTimeChanged = executionTimeChanged;
    }

    /**
     * @return the hypervisorServices
     */
    public IHypervisorServices getHypervisorServices() {
        return hypervisorServices;
    }

    /**
     * @param hypervisorServices the hypervisorServices to set
     */
    public void setHypervisorServices(IHypervisorServices hypervisorServices) {
        this.hypervisorServices = hypervisorServices;
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

    public String getNamePhysicalMachine() {
        return namePhysicalMachine;
    }

    public void setNamePhysicalMachine(String namePhysicalMachine) {
        this.namePhysicalMachine = namePhysicalMachine;
    }

    public SelectItem[] getAllGenerationIPPolicies() {
        IPGenerationPolicy[] policies = IPGenerationPolicy.values();
        SelectItem[] items = new SelectItem[policies.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(policies[i], "" + policies[i].nameString);
        }
        return items;
    }

    public SelectItem[] getAllGenerationMACPolicies() {
        MACGenerationPolicy[] policies = MACGenerationPolicy.values();
        SelectItem[] items = new SelectItem[policies.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(policies[i], "" + policies[i].nameString);
        }
        return items;
    }

    public String getExId() {
        return exId;
    }

    public void setExId(String exId) {
        this.exId = exId;
    }

    public String getFilterTemplateName() {
        return filterTemplateName;
    }

    public void setFilterTemplateName(String filterTemplateName) {
        this.filterTemplateName = filterTemplateName;
    }

    

    
}
