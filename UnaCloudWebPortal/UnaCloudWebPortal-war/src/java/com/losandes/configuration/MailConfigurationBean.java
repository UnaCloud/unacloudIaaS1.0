/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.configuration;

import com.losandes.mail.MailSenderLocal;
import com.losandes.persistence.IPersistenceServices;
import com.losandes.user.IUserServices;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Clouder
 */
@ManagedBean
@SessionScoped
public class MailConfigurationBean {

    @EJB
    private MailSenderLocal mailSender;
    @EJB
    private IPersistenceServices persistence;
    @EJB
    private IUserServices userServices;
    private boolean mailEnable;
    private String smtpServer,user,password;
    private int serverPort;
    private boolean tls,authentication;


    /** Creates a new instance of MailConfigurationBean */
    public MailConfigurationBean() {

    }

    @PostConstruct
    public void init(){
        mailEnable=Boolean.parseBoolean(persistence.getProperty("mail.Enable"));
        smtpServer=persistence.getProperty("mail.smtpServer");
        String h=persistence.getProperty("mail.smtpPort");
        serverPort=(h==null?0:Integer.parseInt(h));
        authentication=Boolean.parseBoolean(persistence.getProperty("mail.auth"));
        user=persistence.getProperty("mail.user");
        password=persistence.getProperty("mail.passwd");
        tls=Boolean.parseBoolean(persistence.getProperty("mail.tls"));
    }

    public boolean isMailEnable() {
        return mailEnable;
    }

    public void setMailEnable(boolean mailEnable) {
        this.mailEnable = mailEnable;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public boolean isTls() {
        return tls;
    }

    public void setTls(boolean tls) {
        this.tls = tls;
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String test(){
        save();
        FacesContext f=FacesContext.getCurrentInstance();
        String email = userServices.getUserByID(f.getExternalContext().getRemoteUser()).getSystemUserEmail();
        String h = mailSender.sendMail("Test mail","This mail is a test.", email);
        f.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,h,h));
        return null;
    }

    public String save(){
        mailSender.setProperties(mailEnable, smtpServer, serverPort, authentication, user, password, tls);
        return null;
    }

}
