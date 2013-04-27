package com.losandes.cloud;

import com.losandes.beans.IOperatingSystemServicesLocal;
import com.losandes.persistence.entity.OperatingSystem;
import com.losandes.persistence.entity.OperatingSystemType;
import com.losandes.persistence.entity.Template;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import com.losandes.template.ITemplateServices;
import com.losandes.user.IUserServices;
import com.losandes.utils.ArrayListOperations;
import com.losandes.virtualmachine.IVirtualMachineServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the ClouderConfiguration JSF
 */
public class ClouderBean implements Serializable {

    //private HtmlTabPanel tabPanel2;
    @EJB
    private ITemplateServices templateServices;
    @EJB
    private IVirtualMachineServices virtualMachineServices;
    @EJB
    private IPhysicalMachineServices physicalMachineServices;
    @EJB
    private IUserServices userServices;
    @EJB
    private IOperatingSystemServicesLocal operatingSystemServices2;
    private Integer operatingSystemTypeSelected = -1;
    private Integer operatingSystemSelected = -1;
    private int templateSelected = -1;
    private Template templateObjectSelected;
    private String hardDiskSizeSelected = "0";
    private String cpuCoresSelected = "0";
    private String ramSelected = "0";
    private int executionInstancesNumber = 0;
    private String executionInstancesSelected = "0";
    private int executionTimeSelected = 1;
    private List<OperatingSystem> operatingSystems;
    private List<Template> templates;
    private List hardDiskSizes;
    private List processorCores;
    private List memoryRam;
    private List<VirtualMachine> executionInstances;
    private List<VirtualMachine> virtualMachineExecuted;
    private String OperatingSystemTypeSelectedName = "N/A";
    private String OperatingSystemSelectedName = "N/A";
    private String AvailableVirtualMachineSelectedName = "N/A";
    private String datosAux;

    /** Creates a new instance of ClouderBean */
    public ClouderBean() {
    }

    @PostConstruct
    public void init() {
        operatingSystems = operatingSystemServices2.getAvailableOperatingSystems(1);
        templates = templateServices.getAvailableTemplates(1);
        hardDiskSizes = new ArrayList();
        hardDiskSizes.add(1);
        processorCores = new ArrayList();
        processorCores.add(1);
        memoryRam = new ArrayList();
        memoryRam.add(1);
        executionInstances = new ArrayList<VirtualMachine>();
        VirtualMachine virMac = new VirtualMachine();
        virMac.setVirtualMachineName("N/A");
        executionInstances.add(virMac);
    }

    public String clearComponents() {
        operatingSystemTypeSelected = -1;
        operatingSystemSelected = -1;
        templateSelected = -1;
        hardDiskSizeSelected = "0";
        cpuCoresSelected = "0";
        ramSelected = "0";
        executionInstancesNumber = 0;
        executionInstancesSelected = "0";
        executionTimeSelected = 1;
        OperatingSystemTypeSelectedName = "N/A";
        OperatingSystemSelectedName = "N/A";
        AvailableVirtualMachineSelectedName = "N/A";
        return "ClouderAdmin";
    }

    public SelectItem[] getOperatingSystemTypes() {
        List<OperatingSystemType> opeSysType = operatingSystemServices2.getAvailableOperatingSystemTypes();
        SelectItem[] items = new SelectItem[opeSysType.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opeSysType.get(i).getOperatingSystemTypeCode(), opeSysType.get(i).getOperatingSystemTypeName());
        }
        return items;
    }

    public SelectItem[] getOperatingSystems() {
        operatingSystems = operatingSystemServices2.getAvailableOperatingSystems(operatingSystemTypeSelected);
        SelectItem[] items = new SelectItem[operatingSystems.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(operatingSystems.get(i).getOperatingSystemCode(), operatingSystems.get(i).getOperatingSystemName());
        }

        return items;
    }

    public SelectItem[] getHardDisks() {
        if (templateSelected!=-1) {
            hardDiskSizes = new ArrayList();
            List<List> virtualMachineHardDisk = virtualMachineServices.getAvailableVirtualHardDisk(templateSelected);
            hardDiskSizes = ArrayListOperations.getSortedIntValues(virtualMachineHardDisk);
        }
        SelectItem[] items = new SelectItem[hardDiskSizes.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(hardDiskSizes.get(i));
        }

        return items;
    }

    public SelectItem[] getCores() {
        if (!hardDiskSizeSelected.equals("0")) {
            processorCores = new ArrayList();
            List<List> physicalMachineCores = physicalMachineServices.getAvailablePhysicalMachineCores(templateSelected);
            processorCores = ArrayListOperations.getSortedIntValues(physicalMachineCores);
            processorCores = ArrayListOperations.getSortedUniqueCores((ArrayList<Integer>) processorCores);
        }
        SelectItem[] items = new SelectItem[processorCores.size()];
        for (int i = 0; i
                < items.length; i++) {
            items[i] = new SelectItem(processorCores.get(i));
        }

        return items;
    }

    public SelectItem[] getMemoryRam() {
        if (!cpuCoresSelected.equals("0")) {
            memoryRam = new ArrayList();
            List<List> physicalMachineRAM = physicalMachineServices.getAvailablePhysicalMachineRAM(templateSelected);
            memoryRam = ArrayListOperations.getSortedIntValues(physicalMachineRAM);
            memoryRam = ArrayListOperations.getSortedUniqueRAM((ArrayList<Integer>) memoryRam);
        }
        SelectItem[] items = new SelectItem[memoryRam.size()];
        for (int i = 0; i
                < items.length; i++) {
            items[i] = new SelectItem(memoryRam.get(i));
        }

        return items;
    }

    public String nextAccessInformation() {
        System.out.println("nextAccessInformation "+getRamSelected()+" "+getMemorySelectedName());
        System.out.println(operatingSystemTypeSelected+" "+OperatingSystemTypeSelectedName);
        if (!executionInstancesSelected.equals("0")) {
            System.out.println(templateSelected+","+ executionTimeSelected+","+ Integer.parseInt(executionInstancesSelected)+","+ Integer.parseInt(cpuCoresSelected)+","+ Integer.parseInt(hardDiskSizeSelected)+","+Integer.parseInt(ramSelected)+","+ FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
            virtualMachineServices.turnOnVirtualClusterBySize(templateSelected, executionTimeSelected, Integer.parseInt(executionInstancesSelected), Integer.parseInt(cpuCoresSelected), Integer.parseInt(hardDiskSizeSelected),Integer.parseInt(ramSelected), FacesContext.getCurrentInstance().getExternalContext().getRemoteUser(),true);
        }
        return "CloudManagement";
    }

    public String returnClouderManagement() {
        return "CloudManagement";
    }

    

    public List getAllTemplates() {
        templates = templateServices.getAvailableTemplates(operatingSystemSelected);
        return templates;
    }

    /**
     * @return the operatingSystemTypeSelected
     */
    public Integer getOperatingSystemTypeSelected() {
        return operatingSystemTypeSelected;
    }

    /**
     * @param operatingSystemTypeSelected the operatingSystemTypeSelected to set
     */
    public void setOperatingSystemTypeSelected(Integer operatingSystemTypeSelected) {
        this.operatingSystemTypeSelected = operatingSystemTypeSelected;

    }

    /**
     * @return the operatingSystemSelected
     */
    public Integer getOperatingSystemSelected() {
        return operatingSystemSelected;
    }

    /**
     * @param operatingSystemSelected the operatingSystemSelected to set
     */
    public void setOperatingSystemSelected(Integer operatingSystemSelected) {
        
        this.operatingSystemSelected = operatingSystemSelected;

    }

    /**
     * @return the templateSelected
     */
    public int getTemplateSelected() {
        return templateSelected;
    }

    /**
     * @param templateSelected the templateSelected to set
     */
    public void setTemplateSelected(int templateSelected) {
        this.templateSelected = templateSelected;
        templateObjectSelected=templateServices.getTemplateByID(templateSelected);
    }

    /**
     * @return the hardDiskSizeSelected
     */
    public String getHardDiskSizeSelected() {
        return hardDiskSizeSelected;
    }

    /**
     * @param hardDiskSizeSelected the hardDiskSizeSelected to set
     */
    public void setHardDiskSizeSelected(String hardDiskSizeSelected) {
        if (hardDiskSizeSelected == null) {
            this.hardDiskSizeSelected = "0";
        } else {
            this.hardDiskSizeSelected = hardDiskSizeSelected;
        }

    }

    /**
     * @return the processorCores
     */
    public List getProcessorCores() {
        return processorCores;
    }

    /**
     * @param processorCores the processorCores to set
     */
    public void setProcessorCores(List processorCores) {
        this.processorCores = processorCores;
    }

    /**
     * @return the cpuCoresSelected
     */
    public String getCpuCoresSelected() {
        return cpuCoresSelected;
    }

    /**
     * @param cpuCoresSelected the cpuCoresSelected to set
     */
    public void setCpuCoresSelected(String cpuCoresSelected) {
        if (cpuCoresSelected == null) {
            this.cpuCoresSelected = "0";
        } else {
            this.cpuCoresSelected = cpuCoresSelected;
        }

    }

    /**
     * @return the ramSelected
     */
    public String getRamSelected() {
        return ramSelected;
    }

    /**
     * @param ramSelected the ramSelected to set
     */
    public void setRamSelected(String ramSelected) {
        if (ramSelected == null) {
            this.ramSelected = "0";
        } else {
            this.ramSelected = ramSelected;
        }
        
    }

    /**
     * @return the executionInstancesNumber
     */
    public int getExecutionInstancesNumber() {
        executionInstances = new ArrayList<VirtualMachine>();
        executionInstancesNumber = virtualMachineServices.getAvailableVirtualMachines(templateSelected, Integer.parseInt(hardDiskSizeSelected), Integer.parseInt(cpuCoresSelected), Integer.parseInt(ramSelected)).size();
        return executionInstancesNumber;
    }

    /**
     * @param executionInstancesNumber the executionInstancesNumber to set
     */
    public void setExecutionInstancesNumber(int executionInstancesNumber) {
        this.executionInstancesNumber = executionInstancesNumber;
    }

    /**
     * @return the executionInstancesSelected
     */
    public String getExecutionInstancesSelected() {
        return executionInstancesSelected;
    }

    /**
     * @param executionInstancesSelected the executionInstancesSelected to set
     */
    public void setExecutionInstancesSelected(String executionInstancesSelected) {
        if (executionInstancesSelected == null) {
            this.ramSelected = "0";
        } else {
            this.executionInstancesSelected = executionInstancesSelected;
        }

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
     * @return the userServices
     */
    public IUserServices getUserServices() {
        return userServices;
    }

    /**
     * @param userServices the userServices to set
     */
    public void setUserServices(IUserServices userServices) {
        this.userServices = userServices;
    }

    

    /**
     * @return the virtualMachineExecuted
     */
    public List<VirtualMachine> getVirtualMachineExecuted() {
        return virtualMachineExecuted;
    }

    /**
     * @param virtualMachineExecuted the virtualMachineExecuted to set
     */
    public void setVirtualMachineExecuted(List<VirtualMachine> virtualMachineExecuted) {
        this.virtualMachineExecuted = virtualMachineExecuted;
    }

    /**
     * @return the OperatingSystemTypeSelectedName
     */
    public String getOperatingSystemTypeSelectedName() {
        OperatingSystemTypeSelectedName=(operatingSystemTypeSelected == -1 ? "N/A" : operatingSystemServices2.getOperatingSystemTypeByID(operatingSystemTypeSelected).getOperatingSystemTypeName());
        return OperatingSystemTypeSelectedName;
    }

    /**
     * @param OperatingSystemTypeSelectedName the OperatingSystemTypeSelectedName to set
     */
    public void setOperatingSystemTypeSelectedName(String OperatingSystemTypeSelectedName) {
        this.OperatingSystemTypeSelectedName = OperatingSystemTypeSelectedName;
    }

    /**
     * @return the OperatingSystemSelectedName
     */
    public String getOperatingSystemSelectedName() {
        OperatingSystemSelectedName= operatingSystemSelected == -1 ? "N/A" : operatingSystemServices2.getOperatingSystemByID(operatingSystemSelected).getOperatingSystemName();
        return OperatingSystemSelectedName;
    }

    /**
     * @return the datosAux
     */
    public String getDatosAux() {
        return datosAux;
    }

    /**
     * @param datosAux the datosAux to set
     */
    public void setDatosAux(String datosAux) {
        this.datosAux = datosAux;
    }

    /**
     * @return the TemplateSelectedName
     */
    public String getTemplateSelectedName() {
        return templateObjectSelected==null?"N/A":templateObjectSelected.getTemplateName();
    }

    /**
     * @return the HardDiskSelectedName
     */
    public String getHardDiskSelectedName() {
        if(hardDiskSizeSelected==null||hardDiskSizeSelected.equals("0"))return "N/A";
        return hardDiskSizeSelected;
    }

    /**
     * @return the ProcessorSelectedName
     */
    public String getProcessorSelectedName() {
        if(cpuCoresSelected==null||cpuCoresSelected.equals("0"))return "N/A";
        return cpuCoresSelected;
    }

    /**
     * @return the AvailableVirtualMachineSelectedName
     */
    public String getAvailableVirtualMachineSelectedName() {
        return AvailableVirtualMachineSelectedName;
    }

    /**
     * @param AvailableVirtualMachineSelectedName the AvailableVirtualMachineSelectedName to set
     */
    public void setAvailableVirtualMachineSelectedName(String AvailableVirtualMachineSelectedName) {
        this.AvailableVirtualMachineSelectedName = AvailableVirtualMachineSelectedName;
    }

    /**
     * @return the MemorySelectedName
     */
    public String getMemorySelectedName() {
        if(ramSelected==null||ramSelected.equals("0"))return "N/A";
        return ramSelected;
    }

    public Template getTemplateObjectSelected() {
        System.out.println("getTemplateObjectSelected "+templateSelected);
        if(templateObjectSelected==null||templateObjectSelected.getTemplateCode().equals(templateSelected)){
            templateObjectSelected=templateServices.getTemplateByID(templateSelected);
        }
        return templateObjectSelected;
    }

    public void setTemplateObjectSelected(Template templateObjectSelected) {
        this.templateObjectSelected = templateObjectSelected;
    }

    public boolean getRenderTemplateSelected(){
        return templateObjectSelected!=null;
    }

    public String gotoOperatingSystemType(){
        return "/Pages/Cloud/OperatingSystemType.xhtml";
    }
    public String gotoOperatingSystem(){
        return "/Pages/Cloud/OperatingSystem.xhtml";
    }

    public String gotoTemplate(){
        return "/Pages/Cloud/Template.xhtml";
    }

    public String gotoHardDisk(){
        if(templateObjectSelected==null){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"You must select a template",""));
            return null;
        }
        return "/Pages/Cloud/HardDisk.xhtml";
    }

    public String gotoProcessor(){
        return "/Pages/Cloud/Processor.xhtml";
    }
    public String gotoRam(){
        return "/Pages/Cloud/RamMemory.xhtml";
    }
    public String gotoTimeSize(){
        return "/Pages/Cloud/TimeSize.xhtml";
    }
}
