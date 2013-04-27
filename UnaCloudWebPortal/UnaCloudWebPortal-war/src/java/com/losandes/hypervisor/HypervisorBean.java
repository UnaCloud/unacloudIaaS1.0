package com.losandes.hypervisor;

import com.losandes.persistence.entity.Hypervisor;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the Hypervisor JSF
 */
public class HypervisorBean {

    @EJB
    private IHypervisorServices hypervisorServices;
    private Hypervisor hypervisor;
    private int id;

    public HypervisorBean() {
        hypervisor = new Hypervisor();
    }

    public SelectItem[] Hypervisors() {
        List<Hypervisor> opts = hypervisorServices.getHypervisors();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getHypervisorCode(), opts.get(i).getHypervisorName());
        }
        return items;
    }

    public String clearComponents(){
        hypervisor=new Hypervisor();
        return null;
    }

    public String createOrUpdate() {
        if (getHypervisor().getHypervisorCode() != 0) {
            if (!hypervisorServices.updateHypervisor(hypervisor)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"The hypervisor was not updated",""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"The hypervisor was modified",""));
            }
        } else {
            if (!hypervisorServices.createHypervisor(hypervisor)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"The hypervisor was not created",""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"The hypervisor was created",""));
            }
        }
        return null;
    }

    public String findByID() {
        hypervisor = hypervisorServices.getHypervisorByID(getId());
        return null;
    }


    public String delete() {
        if (!hypervisorServices.deleteHypervisor(id)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"The hypervisor was not deleted",""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"The hypervisor was deleted",""));
        }
        return null;
    }

    public List getAllHypervisors() {
        return hypervisorServices.getHypervisors();
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
}
