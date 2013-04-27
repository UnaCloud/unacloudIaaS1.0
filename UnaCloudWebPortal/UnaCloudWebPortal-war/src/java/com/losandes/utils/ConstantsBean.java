/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.utils;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Clouder
 */
@ManagedBean
@ApplicationScoped
public class ConstantsBean {

    private SelectItem[] executionHoursAdministrator;
    private SelectItem[] executionHoursUser;
    /** Creates a new instance of Constants */
    public ConstantsBean() {
        {
            int i=0;
            List<SelectItem> items = new ArrayList<SelectItem>();
            items.add(new SelectItem(1,"1 hour"));
            for(int e=2;e<=12;e+=2)items.add(new SelectItem(e,e+" hours"));
            for(int e=1;e<=3;e++)items.add(new SelectItem(e*24,e+" days"));
            executionHoursUser=items.toArray(new SelectItem[0]);
        }{
            List<SelectItem> items = new ArrayList<SelectItem>();
            items.add(new SelectItem(1,"1 hour"));
            for(int e=2;e<=12;e+=2)items.add(new SelectItem(e,e+" hours"));
            for(int e=1;e<=15;e++)items.add(new SelectItem(e*24,e+" days"));
            for(int e=1;e<=6;e++)items.add(new SelectItem(e*24*30,e+" months"));
            executionHoursAdministrator=items.toArray(new SelectItem[0]);
        }

    }

    public SelectItem[] getExecutionTimes(){
        if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Administrator")){
            return executionHoursAdministrator;
        }else{
            return executionHoursUser;
        }
    }

}
