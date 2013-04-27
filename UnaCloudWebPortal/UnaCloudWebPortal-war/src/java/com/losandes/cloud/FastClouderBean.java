package com.losandes.cloud;

import com.losandes.persistence.entity.Template;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.template.ITemplateServices;
import com.losandes.utils.ArrayListOperations;
import com.losandes.virtualmachine.IVirtualMachineServices;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the ClouderFastConfiguration JSF
 */
public class FastClouderBean {

    @EJB
    private ITemplateServices templateServices;
    @EJB
    private IVirtualMachineServices virtualMachineServices;
    private List<Template> templates;
    private int templateSelected = -1;
    private int executionInstancesNumberSelected = 0;
    private int executionTimeSelected = 0;

    /** Creates a new instance of FastClouderBean */
    public FastClouderBean() {
    }

    @PostConstruct
    public void init() {
    }

    public SelectItem[] getFastTemplates() {
        templates = templateServices.getFastAvailableTemplates(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (int i = 0; i < templates.size(); i++) {
            items.add(new SelectItem(templates.get(i).getTemplateCode(), templates.get(i).getTemplateName()));
        }
        if(!templates.isEmpty())templateSelected=templates.get(0).getTemplateCode();
        return items.toArray(new SelectItem[0]);
    }

    public String nextAccessInformation() {
        if (executionInstancesNumberSelected != 0 && templateSelected!=-1) {
            virtualMachineServices.turnOnVirtualClusterBySize(templateSelected, executionTimeSelected, executionInstancesNumberSelected,0,0,0, FacesContext.getCurrentInstance().getExternalContext().getRemoteUser(),true);
            return "CloudManagement";
        } else {
            return "FastClouderAdmin";
        }

    }

    /**
     * @return the TemplateSelected
     */
    public int getTemplateSelected() {
        return templateSelected;
    }

    /**
     * @param TemplateSelected the TemplateSelected to set
     */
    public void setTemplateSelected(int TemplateSelected) {
        this.templateSelected = TemplateSelected;
    }

    /**
     * @return the executionInstancesNumber
     */
    public int getExecutionInstancesNumber() {
        if (templateSelected == -1) {
            return 0;
        }
        return virtualMachineServices.getAvailableVirtualMachines(templateSelected,0,0,0).size();
    }

    /**
     * @return the executionInstancesNumberSelected
     */
    public int getExecutionInstancesNumberSelected() {
        return executionInstancesNumberSelected;
    }

    /**
     * @param executionInstancesNumberSelected the executionInstancesNumberSelected to set
     */
    public void setExecutionInstancesNumberSelected(int executionInstancesNumberSelected) {
        this.executionInstancesNumberSelected = executionInstancesNumberSelected;
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
}
