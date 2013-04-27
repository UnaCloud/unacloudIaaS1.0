/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.utils;


import com.losandes.persistence.entity.SystemUserType;
import com.losandes.persistence.entity.Template;
import com.losandes.user.UserBean;
import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Clouder
 */
@FacesConverter("templateConverter")
public class TemplateConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for(Template sut:getUserBean(context).getAllTemplates()){
            if(sut.getTemplateName().equals(value))return sut;
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value instanceof Template)return ""+((Template)value).getTemplateName();
        return null;
    }

    private UserBean userBean;
    private UserBean getUserBean(FacesContext context){
        if (userBean == null) {
            ELContext elContext = context.getELContext();
            userBean = (UserBean) elContext.getELResolver().getValue(elContext, null, "UserBean");
        }
        return userBean;
    }

}
