package com.losandes.user;

import com.losandes.persistence.entity.SystemUser;
import com.losandes.persistence.entity.SystemUserType;
import com.losandes.persistence.entity.Template;
import com.losandes.template.ITemplateServices;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import static com.losandes.utils.Constants.*;

/**
 *
 * @author Eduardo Rosales
 * Responsible for managing the user_management JSF
 */
public class UserBean {

    @EJB
    private IUserServices userServices;
    @EJB
    private ITemplateServices templateServices;
    private SystemUser user;
    private String id;
    private List<SystemUserType> allUserTypes;
    private List<Template> allTemplates;
    private String newPassword;

    /**
     * Constructor method
     */
    public UserBean() {
        user = new SystemUser();
        allUserTypes=new ArrayList<SystemUserType>();
        allTemplates=new ArrayList<Template>();
    }
    
    @PostConstruct
    public void init(){
        allUserTypes.addAll(userServices.getUsersTypes());
        allTemplates=templateServices.getTemplates();
    }



   public String clearComponents(){
        user = new SystemUser();
        return null;
    }

    public String createOrUpdate() {
        if(newPassword!=null&&!newPassword.isEmpty())user.setSystemUserPassword(newPassword);
        if (user.getSystemUserName() != null) {
            if (!userServices.updateUser(user)) {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "The user was not updated", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "The user was modified", ""));
            }
        } else {
            if (!userServices.createUser(user)) {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "The user was not created", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "The user was created", ""));
            }
        }
        newPassword="";
        user = null;
        return null;
    }

    public String findByID() {
        user = userServices.getUserByID(getId());
        for(SystemUserType sut:allUserTypes){
            for(int e=0;e<user.getUserGroups().size();e++)if(user.getUserGroups().get(e).getSystemUserTypeName().equals(sut.getSystemUserTypeName())){
                user.getUserGroups().set(e, sut);
                break;
            }
        }
        for(Template temp:allTemplates){
            for(int e=0;e<user.getTemplates().size();e++)if(user.getTemplates().get(e).getTemplateName().equals(temp.getTemplateName())){
                user.getTemplates().set(e, temp);
                break;
            }
        }
        return null;
    }

    public String delete() {
        if (!userServices.deleteUser(id)) {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "The user was not deleted", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "The user was deleted", ""));
        }
        user = null;
        return null;
    }

    public List getAllSystemUsers() {
        return userServices.getUsers();
    }

    /**
     * @return the user
     */
    public SystemUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(SystemUser user) {
        this.user = user;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    public List<SystemUserType> getAllUserTypes() {
        return allUserTypes;
    }

    public void setAllUserTypes(List<SystemUserType> allUserTypes) {
        this.allUserTypes = allUserTypes;
    }

    public List<Template> getAllTemplates() {
        return allTemplates;
    }

    public void setAllTemplates(List<Template> allTemplates) {
        this.allTemplates = allTemplates;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
