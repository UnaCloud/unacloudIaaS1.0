/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.configuration;

import com.losandes.communication.security.KeyGenerator;
import com.losandes.persistence.IPersistenceServices;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Clouder
 */
public class ServerConfigurationBean {

    @EJB
    private IPersistenceServices persistence;
    @EJB
    private IPhysicalMachineServices pmServices;
    private Map<String,String> properties=new TreeMap<String, String>();
    private int keySize;
    String RSA_MODULUS_PUBLIC,RSA_EXPONENT_PUBLIC,RSA_MODULUS_PRIVATE,RSA_EXPONENT_PRIVATE;

    /** Creates a new instance of ServerConfigurationBean */
    public ServerConfigurationBean() {
    }

    @PostConstruct
    public void init(){
        properties.put("String.CLOUDER_SERVER_IP",persistence.getProperty("String.CLOUDER_SERVER_IP"));
        properties.put("Integer.CLOUDER_SERVER_PORT",persistence.getProperty("Integer.CLOUDER_SERVER_PORT"));
        properties.put("Integer.CLOUDER_CLIENT_PORT",persistence.getProperty("Integer.CLOUDER_CLIENT_PORT"));
        properties.put("Integer.FILE_TRANSFER_SOCKET",persistence.getProperty("Integer.FILE_TRANSFER_SOCKET"));
        properties.put("Integer.DATA_SOCKET",persistence.getProperty("Integer.DATA_SOCKET"));
        properties.put("Integer.LOG_SOCKET",persistence.getProperty("Integer.LOG_SOCKET"));
        properties.put("Integer.VERSION_MANAGER_PORT",persistence.getProperty("Integer.VERSION_MANAGER_PORT"));
        properties.put("String.MONITORING_ENABLE",persistence.getProperty("String.MONITORING_ENABLE"));
        properties.put("String.MONITORING_SERVER_IP",persistence.getProperty("String.MONITORING_SERVER_IP"));
        properties.put("String.MONITORING_DATABASE_NAME",persistence.getProperty("String.MONITORING_DATABASE_NAME"));
        properties.put("String.MONITORING_DATABASE_USER",persistence.getProperty("String.MONITORING_DATABASE_USER"));
        properties.put("String.MONITORING_DATABASE_PASSWORD",persistence.getProperty("String.MONITORING_DATABASE_PASSWORD"));
        properties.put("Integer.MONITOR_FREQUENCY",persistence.getProperty("Integer.MONITOR_FREQUENCY"));
        properties.put("Integer.MONITOR_REGISTER_FREQUENCY",persistence.getProperty("Integer.MONITOR_REGISTER_FREQUENCY"));

        RSA_MODULUS_PUBLIC=persistence.getProperty("String.RSA_MODULUS_PUBLIC");
        RSA_MODULUS_PRIVATE=persistence.getProperty("Secret.String.RSA_MODULUS_PRIVATE");
        RSA_EXPONENT_PUBLIC=persistence.getProperty("String.RSA_EXPONENT_PUBLIC");
        RSA_EXPONENT_PRIVATE=persistence.getProperty("Secret.String.RSA_EXPONENT_PRIVATE");
    }



    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }


    public String save(){
        pmServices.sendMachineParameters(properties);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Configuration saved", ""));
        return null;
    }

    public String saveSecurity(){
        Map<String,String> securityProperties=new TreeMap<String, String>();
        securityProperties.put("String.RSA_MODULUS_PUBLIC", RSA_MODULUS_PUBLIC);
        securityProperties.put("Secret.String.RSA_MODULUS_PRIVATE", RSA_MODULUS_PRIVATE);
        securityProperties.put("String.RSA_EXPONENT_PUBLIC", RSA_EXPONENT_PUBLIC);
        securityProperties.put("Secret.String.RSA_EXPONENT_PRIVATE", RSA_EXPONENT_PRIVATE);
        persistence.setProperties(securityProperties);
        pmServices.setKeyPair(keyPair);
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Configuration saved", ""));
        return null;
    }

    public String cancel(){
        init();
        return null;
    }
    private KeyPair keyPair;

    public String regenerateKeys(){
        if(keySize!=0){
            keyPair =new KeyGenerator().generateKeys(keySize);
            if(keyPair==null)FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Can't create keys", null));
            else{
                RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
                RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
                RSA_MODULUS_PUBLIC=publicKey.getModulus().toString();
                RSA_EXPONENT_PUBLIC=publicKey.getPublicExponent().toString();
                RSA_MODULUS_PRIVATE=privateKey.getModulus().toString();
                RSA_EXPONENT_PRIVATE=privateKey.getPrivateExponent().toString();
            }
        }
        return "securityConfiguration";
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public String getSystemTime(){
        return ""+System.currentTimeMillis();
    }

    public String getRSA_EXPONENT_PRIVATE() {
        return RSA_EXPONENT_PRIVATE;
    }

    public String getRSA_EXPONENT_PUBLIC() {
        return RSA_EXPONENT_PUBLIC;
    }

    public String getRSA_MODULUS_PRIVATE() {
        return RSA_MODULUS_PRIVATE;
    }

    public String getRSA_MODULUS_PUBLIC() {
        return RSA_MODULUS_PUBLIC;
    }



}

