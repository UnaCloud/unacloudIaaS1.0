package com.losandes.security;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the user security
 */
public class SecurityBean implements Serializable{

    private String styleCloud;
    private String styleGrid;
    private String styleAdministrator;
    

    @PostConstruct
    public void init() {
        //new ClouderServerAttention();
    }


    /**
     * @return the enabledCloud
     */
    public boolean isDisabledCloud() {
        return !FacesContext.getCurrentInstance().getExternalContext().isUserInRole("IaaS");
    }

    public boolean isEnabledCloud() {
        return !isDisabledCloud();
    }

    /**
     * @return the enabledGrid
     */
    public boolean isDisabledGrid() {
        return !FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Grid");
    }
    public boolean isEnabledGrid() {
        return !isDisabledGrid();
    }

    /**
     * @return the enabledAdministrator
     */
    public boolean isDisabledAdministrator() {
        return !FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Administrator");
    }

    public boolean isLogedIn(){
        String h=FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        return h!=null;
    }

    public boolean isLoggedOff(){
        String h=FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        return h==null;
    }

    /**
     * @return the styleCloud
     */
    public String getStyleCloud() {
        return styleCloud;
    }

    /**
     * @param styleCloud the styleCloud to set
     */
    public void setStyleCloud(String styleCloud) {
        this.styleCloud = styleCloud;
    }

    /**
     * @return the styleGrid
     */
    public String getStyleGrid() {
        return styleGrid;
    }

    /**
     * @param styleGrid the styleGrid to set
     */
    public void setStyleGrid(String styleGrid) {
        this.styleGrid = styleGrid;
    }

    /**
     * @return the styleAdministrator
     */
    public String getStyleAdministrator() {
        return styleAdministrator;
    }

    /**
     * @param styleAdministrator the styleAdministrator to set
     */
    public void setStyleAdministrator(String styleAdministrator) {
        this.styleAdministrator = styleAdministrator;
    }

    public String getSystemUserName() {
        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }

}
