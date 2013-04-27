/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.grid;

import com.losandes.application.IApplicationServices;
import com.losandes.beans.IOperatingSystemServicesLocal;
import com.losandes.entity.physicalMachine;
import com.losandes.laboratory.ILaboratoryServices;
import com.losandes.persistence.entity.*;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import com.losandes.template.ITemplateServices;
import com.losandes.user.IUserServices;
import com.losandes.utils.ArrayListOperations;
import com.losandes.virtualmachine.IVirtualMachineServices;
import java.util.*;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Clouder
 */
public class GridDeploymentBean {

    @EJB
    private IUserServices userServices;
    @EJB
    private IOperatingSystemServicesLocal operatingSystemServices;
    @EJB
    private ITemplateServices templateServices;
    @EJB
    private IApplicationServices applicationServices;
    @EJB
    private IVirtualMachineServices virtualMachineServices;
    @EJB
    private IPhysicalMachineServices physicalMachineServices;
    @EJB
    private ILaboratoryServices laboratoryServices;
    private List<OperatingSystemType> operatingSystemsTypes;
    private List<OperatingSystem> operatingSystems;
    private List<Template> templates;
    private List<Application> applications;
    private List hardDiskSizes;
    private List processorCores;
    private List memoryRam;
    private List<VirtualMachine> virtualMachineAvailable;
    private int operatingSystemTypeSelected = -1;
    private int operatingSystemSelected = -1;
    private int templateSelected = -1;
    private String hardDiskSizeSelected = "0";
    private String processorCoresSelected = "0";
    private String memoryRAMSelected = "0";
    private int executionTimeSelected = -1;
    private boolean laboratorySelection;
    private List<Laboratory> allLaboratories;
    private String selectedLaboratoryId;
    private Laboratory selectedLaboratory;
    private Set<PhysicalMachine> aviablepm;

    public GridDeploymentBean() {
    }

    @PostConstruct
    public void init() {
        operatingSystems = operatingSystemServices.getGridAvailableOperatingSystems(2, FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        operatingSystemsTypes = operatingSystemServices.getGridAvailableOperatingSystemTypes(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        allLaboratories = laboratoryServices.getLaboratories();
        applications = new ArrayList<Application>();
        hardDiskSizes = new ArrayList();
        processorCores = new ArrayList();
        memoryRam = new ArrayList();
        laboratorySelection = true;
        templates = new ArrayList<Template>();
    }

    public SelectItem[] getGridOperatingSystemTypes() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(-1,""));
        for (int i = 0; i < operatingSystemsTypes.size(); i++) {
            items.add(new SelectItem(operatingSystemsTypes.get(i).getOperatingSystemTypeCode(), operatingSystemsTypes.get(i).getOperatingSystemTypeName()));
        }
        return items.toArray(new SelectItem[0]);
    }

    public SelectItem[] getGridOperatingSystems() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(-1,""));
        for (int i = 0; i < operatingSystems.size(); i++) {
            items.add(new SelectItem(operatingSystems.get(i).getOperatingSystemCode(), operatingSystems.get(i).getOperatingSystemName()));
        }
        return items.toArray(new SelectItem[0]);
    }

    public SelectItem[] getFastGridTemplates() {
        templates = templateServices.getFastGridAvailableTemplates(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (int i = 0; i < templates.size(); i++) {
            items.add(new SelectItem(templates.get(i).getTemplateCode(), templates.get(i).getTemplateName()));
        }
        return items.toArray(new SelectItem[0]);
    }

    public SelectItem[] getGridTemplates() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        for (int i = 0; i < templates.size(); i++) {
            items.add(new SelectItem(templates.get(i).getTemplateCode(), templates.get(i).getTemplateName()));
        }
        return items.toArray(new SelectItem[0]);
    }

    public SelectItem[] getGridApplications() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        for (int i = 0; i < applications.size(); i++) {
            items.add(new SelectItem(applications.get(i).getApplicationCode(), applications.get(i).getApplicationName()));
        }
        return items.toArray(new SelectItem[0]);
    }

    public SelectItem[] getGridVirtualHardDisk() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        for (int i = 0; i < hardDiskSizes.size(); i++) {
            items.add(new SelectItem(hardDiskSizes.get(i)));
        }
        return items.toArray(new SelectItem[0]);
    }

    public SelectItem[] getGridProcessorCores() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        for (int i = 0; i < processorCores.size(); i++) {
            items.add(new SelectItem(processorCores.get(i)));
        }
        return items.toArray(new SelectItem[0]);
    }

    public SelectItem[] getGridMemoryRAM() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        for (int i = 0; i < memoryRam.size(); i++) {
            items.add(new SelectItem(memoryRam.get(i)));
        }
        return items.toArray(new SelectItem[0]);
    }

    public SelectItem[] getGridVirtualMachines() {
        try {
            virtualMachineAvailable = virtualMachineServices.getGridAvailableVirtualMachines(templateSelected, Integer.parseInt(hardDiskSizeSelected), Integer.parseInt(processorCoresSelected), Integer.parseInt(memoryRAMSelected), FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        } catch (Exception e) {
            return new SelectItem[0];
        }

        SelectItem[] items = null;
        items = new SelectItem[virtualMachineAvailable.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(virtualMachineAvailable.get(i).getPhysicalMachine().getPhysicalMachineName());
        }
        return items;
    }

    /**
     * @return the operatingSystemTypeSelected
     */
    public int getOperatingSystemTypeSelected() {
        return operatingSystemTypeSelected;
    }

    /**
     * @param operatingSystemTypeSelected the operatingSystemTypeSelected to set
     */
    public void setOperatingSystemTypeSelected(int operatingSystemTypeSelected) {
        if (operatingSystemTypeSelected == -1) {
            setOperatingSystemSelected(-1);
            setTemplateSelected(-1);
        }
        this.operatingSystemTypeSelected = operatingSystemTypeSelected;
    }

    /**
     * @return the operatingSystemSelected
     */
    public int getOperatingSystemSelected() {
        return operatingSystemSelected;
    }

    /**
     * @param operatingSystemSelected the operatingSystemSelected to set
     */
    public void setOperatingSystemSelected(int operatingSystemSelected) {
        if (operatingSystemSelected == -1) {
            setTemplateSelected(-1);
            templates = new ArrayList<Template>();
        } else if (operatingSystemSelected != this.operatingSystemSelected) {
            templates = templateServices.getGridAvailableTemplates(operatingSystemSelected, FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        }
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
        if (templateSelected == -1) {
            applications = new ArrayList<Application>();
            hardDiskSizes = new ArrayList();
            processorCores = new ArrayList();
            memoryRam = new ArrayList();
            virtualMachineAvailable = new ArrayList<VirtualMachine>();
            aviablepm = new TreeSet<PhysicalMachine>();
            for(Laboratory b:allLaboratories)for(PhysicalMachine pm:b.getPhysicalMachine()){
                pm.setUnaviable(false);
            }
        } else if (templateSelected != this.templateSelected) {
            applications = applicationServices.getAvailableApplications(templateSelected);
            hardDiskSizes = ArrayListOperations.getSortedIntValues(virtualMachineServices.getGridAvailableVirtualHardDisk(templateSelected, FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()));
            processorCores = new ArrayList();
            List<List> virtualProcessorCores = physicalMachineServices.getGridAvailablePhysicalMachineCores(templateSelected, FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
            processorCores = ArrayListOperations.getSortedIntValues(virtualProcessorCores);
            processorCores = ArrayListOperations.getSortedUniqueCores((ArrayList<Integer>) processorCores);
            memoryRam = new ArrayList();
            List<List> virtualMemoryRAM = physicalMachineServices.getGridAvailablePhysicalMachineRAM(templateSelected, FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
            memoryRam = ArrayListOperations.getSortedIntValues(virtualMemoryRAM);
            memoryRam = ArrayListOperations.getSortedUniqueRAM((ArrayList<Integer>) memoryRam);
            virtualMachineAvailable = virtualMachineServices.getGridAvailableVirtualMachines(templateSelected, Integer.parseInt(hardDiskSizeSelected), Integer.parseInt(processorCoresSelected), Integer.parseInt(memoryRAMSelected), FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
            aviablepm = new TreeSet<PhysicalMachine>(new Comparator<PhysicalMachine>() {
                @Override
                public int compare(PhysicalMachine o1, PhysicalMachine o2) {
                    return o1.getPhysicalMachineName().compareTo(o2.getPhysicalMachineName());
                }
            });
            for (VirtualMachine vm : virtualMachineAvailable)aviablepm.add(vm.getPhysicalMachine());
            for(Laboratory b:allLaboratories)for(PhysicalMachine pm:b.getPhysicalMachine()){
                boolean t=aviablepm.contains(pm);
                pm.setUnaviable(!t);
                if(!t)pm.setPhysicalMachineEnable(false);
            }
        }
        this.templateSelected = templateSelected;
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
        this.hardDiskSizeSelected = hardDiskSizeSelected;
    }

    /**
     * @return the processorCoresSelected
     */
    public String getProcessorCoresSelected() {
        return processorCoresSelected;
    }

    /**
     * @param processorCoresSelected the processorCoresSelected to set
     */
    public void setProcessorCoresSelected(String processorCoresSelected) {
        this.processorCoresSelected = processorCoresSelected;
    }

    /**
     * @return the memoryRAMSelected
     */
    public String getMemoryRAMSelected() {
        return memoryRAMSelected;
    }

    /**
     * @param memoryRAMSelected the memoryRAMSelected to set
     */
    public void setMemoryRAMSelected(String memoryRAMSelected) {
        this.memoryRAMSelected = memoryRAMSelected;
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

    public List<VirtualMachine> getVirtualMachineAvailable() {
        return virtualMachineAvailable;
    }

    public boolean isLaboratorySelection() {
        return laboratorySelection;
    }

    public void setLaboratorySelection(boolean laboratorySelection) {
        this.laboratorySelection = laboratorySelection;
    }

    public List getAllLaboratories() {
        return allLaboratories;
    }

    public String findLaboratory() {
        int s = Integer.parseInt(selectedLaboratoryId);
        for (Laboratory b : allLaboratories) {
            if (b.getLaboratoryCode() == s) {
                selectedLaboratory = b;
                if (b.isSelected()) {
                    for (PhysicalMachine pm : b.getPhysicalMachine()) {
                        if (!pm.getUnaviable()) {
                            pm.setPhysicalMachineEnable(true);
                        }
                    }
                } else {
                    boolean complete = true;
                    for (PhysicalMachine pm : b.getPhysicalMachine()) {
                        if (!pm.getUnaviable() && !pm.isPhysicalMachineEnable()) {
                            complete = false;
                            break;
                        }
                    }
                    if (complete) {
                        for (PhysicalMachine pm : b.getPhysicalMachine()) {
                            if (!pm.getUnaviable()) {
                                pm.setPhysicalMachineEnable(false);
                            }
                        }
                    }

                }
                break;
            }
        }
        laboratorySelection = false;
        return null;
    }

    public String backFromLaboratory() {
        laboratorySelection = true;
        return null;
    }

    public Laboratory getSelectedLaboratory() {
        return selectedLaboratory;
    }

    public void setSelectedLaboratory(Laboratory selectedLaboratory) {
        this.selectedLaboratory = selectedLaboratory;
    }

    public String getSelectedLaboratoryId() {
        return selectedLaboratoryId;
    }

    public void setSelectedLaboratoryId(String selectedLaboratoryId) {
        this.selectedLaboratoryId = selectedLaboratoryId;
    }

    public boolean isLaboratorySelected() {
        return !laboratorySelection;
    }

    public void setLaboratorySelected(boolean laboratorySelected) {
        this.laboratorySelection = !laboratorySelected;
    }

    public String getFastVirtualMachinesAviableOverTotal() {
        if (templateSelected != -1) {
            int total = 0, selected = 0;
            for (Laboratory b : allLaboratories) {
                if (b.isSelected()) {
                    for (PhysicalMachine pm : b.getPhysicalMachine()) {
                        if (!pm.getUnaviable() && aviablepm.contains(pm)) {
                            selected++;
                            total++;
                        }
                    }
                } else {
                    for (PhysicalMachine pm : b.getPhysicalMachine()) {
                        if (aviablepm.contains(pm)) {
                            if (!pm.getUnaviable()) {
                                total++;
                                if (pm.isPhysicalMachineEnable()) {
                                    selected++;
                                }
                            }
                        }
                    }
                }
            }
            return selected + "/" + total;
        }
        return "0/0";
    }

    public String turnOnVirtualMachineGrid() {
        if (templateSelected == -1) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid CVC",""));
            return null;
        }if(getProcessorCoresSelected().equals("0")||getHardDiskSizeSelected().equals("0")||getMemoryRAMSelected().equals("0")){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid RAM,Hard disk or CPU cores", ""));
            return null;
        }
        ArrayList<PhysicalMachine> executionGridPhysicalMachines = new ArrayList<PhysicalMachine>();
        if (allLaboratories != null) {
            for (Laboratory b : allLaboratories) {
                for (PhysicalMachine pm : b.getPhysicalMachine()) {
                    if ((pm.isPhysicalMachineEnable()||b.isSelected())&&!pm.getUnaviable()) {
                        executionGridPhysicalMachines.add(pm);
                    }
                }
            }
        } else {
            System.err.println("Laboratory is null");
        }
        virtualMachineServices.turnOnVirtualCluster(executionGridPhysicalMachines, getExecutionTimeSelected(), templateSelected, Integer.parseInt(getProcessorCoresSelected()), Integer.parseInt(getHardDiskSizeSelected()), Integer.parseInt(getMemoryRAMSelected()), FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        return "CloudManagement";
    }

    public String turnOnFastVirtualMachineGrid() {
        if (templateSelected == -1) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid CVC", ""));
            return null;
        }
        ArrayList<PhysicalMachine> executionGridPhysicalMachines = new ArrayList<PhysicalMachine>();
        if (allLaboratories != null) {
            for (Laboratory b : allLaboratories) {
                for (PhysicalMachine pm : b.getPhysicalMachine()) {
                    if((pm.isPhysicalMachineEnable()||b.isSelected())&&!pm.getUnaviable()) {
                        executionGridPhysicalMachines.add(pm);
                    }
                }
            }
        } else {
            System.err.println("Laboratory is null");
        }
        virtualMachineServices.turnOnVirtualCluster(executionGridPhysicalMachines, getExecutionTimeSelected(), templateSelected, 0, 0, 0, FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        return "CloudManagement";
    }
}
