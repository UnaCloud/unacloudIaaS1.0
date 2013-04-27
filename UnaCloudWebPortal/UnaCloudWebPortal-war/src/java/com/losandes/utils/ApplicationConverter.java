/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.utils;

import com.losandes.persistence.entity.Application;
import com.losandes.persistence.entity.SystemUserType;
import com.losandes.template.TemplateBean;
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
@FacesConverter("applicationConverter")
public class ApplicationConverter  implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for(Application sut:getTemplateBean(context).getAllApplications()){
            if(sut.getApplicationName().equals(value))return sut;
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value instanceof Application)return ""+((Application)value).getApplicationName();
        return null;
    }

    private TemplateBean templateBean;
    private TemplateBean getTemplateBean(FacesContext context){
        if (templateBean == null) {
            ELContext elContext = context.getELContext();
            templateBean = (TemplateBean) elContext.getELResolver().getValue(elContext, null, "TemplateBean");
        }
        return templateBean;
    }
}
