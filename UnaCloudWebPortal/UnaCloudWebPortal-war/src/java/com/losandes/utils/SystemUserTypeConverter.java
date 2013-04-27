/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.utils;

import com.losandes.persistence.entity.SystemUserType;
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
@FacesConverter("systemUserTypeConverter")
public class SystemUserTypeConverter  implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for(SystemUserType sut:getUserBean(context).getAllUserTypes()){
            if(sut.getSystemUserTypeName().equals(value))return sut;
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value instanceof SystemUserType)return ""+((SystemUserType)value).getSystemUserTypeName();
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
