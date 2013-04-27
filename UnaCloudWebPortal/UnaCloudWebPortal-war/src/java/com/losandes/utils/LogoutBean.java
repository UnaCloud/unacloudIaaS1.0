/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Clouder
 */
public class LogoutBean {

    /** Creates a new instance of LogoutBean */
    public LogoutBean() {
    }

    public String doLogout(){
        Object c = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        HttpSession https = (HttpSession)c;
        https.invalidate();
        return "logout";
    }

    public String getLogout(){
        Object c = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        HttpSession https = (HttpSession)c;
        https.invalidate();
        return "Session timeout";
    }

}
