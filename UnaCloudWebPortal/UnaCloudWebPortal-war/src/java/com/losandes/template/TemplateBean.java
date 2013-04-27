package com.losandes.template;

import com.losandes.application.IApplicationServices;
import com.losandes.beans.IOperatingSystemServicesLocal;
import com.losandes.persistence.entity.Application;
import com.losandes.persistence.entity.OperatingSystem;
import com.losandes.persistence.entity.SystemUser;
import com.losandes.persistence.entity.Template;
import com.losandes.persistence.entity.TemplateType;
import com.losandes.user.IUserServices;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the Template JSF
 */
public class TemplateBean {

    @EJB
    private ITemplateServices templateServices;
    @EJB
    private IApplicationServices applicationServices;
    @EJB
    private IUserServices userServices;
    @EJB
    private IOperatingSystemServicesLocal operatingSystemServices;
    private Template template;
    private TemplateType templateType;
    private OperatingSystem operatingSystem;
    private int id,elacticRuleType;
    private List<Application> allApplications;


    /** Creates a new instance of TemplateBean */
    public TemplateBean() {
        template = new Template();
        templateType = new TemplateType();
        operatingSystem = new OperatingSystem();
    }

    @PostConstruct
    public void init(){
        allApplications=applicationServices.getApplications();
    }

    public String clearComponents() {
        template = new Template();
        templateType = new TemplateType();
        operatingSystem = new OperatingSystem();
        return null;
    }

    public SelectItem[] getTemplates() {
        List<Template> opeSysType = templateServices.getTemplates();
        SelectItem[] items = new SelectItem[opeSysType.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opeSysType.get(i).getTemplateCode(), opeSysType.get(i).getTemplateName());
        }
        return items;
    }

    public List getAllTemplates() {
        System.err.println("El usuario es "+FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        return getTemplateservices().getTemplates();
    }

    public SelectItem[] getAllTemplatesType() {
        List<TemplateType> opts = templateServices.getTemplatesTypes();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getTemplateTypeCode(), opts.get(i).getTemplateTypeName());
        }
        return items;
    }

    public SelectItem[] getAllSystemUsers() {
        List<SystemUser> opts = userServices.getUsers();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getSystemUserName(), opts.get(i).getSystemUserName());
        }
        return items;
    }

    public SelectItem[] getAllOperetingSystems() {
        List<OperatingSystem> opts = operatingSystemServices.getOperatingSystems();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getOperatingSystemCode(), opts.get(i).getOperatingSystemName());
        }
        return items;
    }

    public String createOrUpdate() {
        System.out.println("createOrUpdate");
        template.setOperatingSystem(operatingSystem);
        TemplateType tt =new TemplateType();
        tt.setTemplateTypeCode(templateType.getTemplateTypeCode());
        template.setTemplateType(tt);
        if (template.getTemplateCode() != 0) {
            if (!templateServices.updateTemplate(template)) {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"The template was not updated",""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"The template was modified",""));
            }
        } else {
            if (!templateServices.createTemplate(template)) {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"The template was not created",""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"The template was created",""));
            }
        }
        return null;
    }

    public String delete(){
        return null;
    }

    public String findByID() {
        template = templateServices.getTemplateByID(id);
        operatingSystem = template.getOperatingSystem();
        templateType = template.getTemplateType();
        for(Application app:allApplications){
            for(int e=0;e<template.getApplications().size();e++)if(template.getApplications().get(e).getApplicationCode().equals(app.getApplicationCode())){
                template.getApplications().set(e, app);
            }
        }
        return null;
    }

    /**
     * @return the templateServices
     */
    public ITemplateServices getTemplateservices() {
        return templateServices;
    }

    /**
     * @param templateservices the templateServices to set
     */
    public void setTemplateservices(ITemplateServices templateservices) {
        this.templateServices = templateservices;
    }

    /**
     * @return the Template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * @param template the Template to set
     */
    public void setTemplate(Template template) {
        this.template = template;
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

    public int getElacticRuleType() {
        return elacticRuleType;
    }

    public void setElacticRuleType(int elacticRuleType) {
        this.elacticRuleType = elacticRuleType;
    }

    public List<Application> getAllApplications() {
        return allApplications;
    }

    public void setAllApplications(List<Application> allApplications) {
        this.allApplications = allApplications;
    }


}
