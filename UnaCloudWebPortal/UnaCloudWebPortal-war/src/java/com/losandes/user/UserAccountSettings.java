/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.user;

import com.losandes.persistence.entity.SystemUser;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


/**
 *
 * @author Clouder
 */
public class UserAccountSettings {

    @EJB
    private IUserServices userServices;

    private SystemUser currentSystemUser;

    private boolean edit;

    private String passwordOld;
    private String passwordNew;
    private String passwordRepeat;

    /** Creates a new instance of UserAccountSettings */
    public UserAccountSettings() {
    }

    public SystemUser getCurrentSystemUser() {
        if(currentSystemUser==null)currentSystemUser=userServices.getUserByID(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        return currentSystemUser;
    }

    public void setCurrentSystemUser(SystemUser currentSystemUser) {
        this.currentSystemUser = currentSystemUser;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public String enterEditionMode(){
        edit=true;
        return null;
    }

    public String cancel(){
        edit=false;
        currentSystemUser=userServices.getUserByID(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        return null;
    }

    public String save(){
        boolean save=true;
        if(!passwordOld.equals(currentSystemUser.getSystemUserPassword())){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,"Wrong old password",""));
            save=false;
        }
        if(!passwordNew.equals(passwordRepeat)){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,"New password doesn't match",""));
            save=false;
        }else if(passwordNew.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,"Password can´t be empty",""));
            save=false;
        }else if(passwordNew.length()<8){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,"Password must have at least 8 characters",""));
            save=false;
        }

        if(currentSystemUser.getSystemUserEmail().isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,"Email can´t be empty",""));
            save=false;
        }else if(!currentSystemUser.getSystemUserEmail().matches("[^@]+@.*\\..*")){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_FATAL,"Invalid email",""));
            save=false;
        }
        if(save){
            edit=false;
            currentSystemUser.setSystemUserPassword(passwordNew);
            userServices.updateUser(currentSystemUser);
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Settings saved",""));
        }
        return null;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }



    
}
