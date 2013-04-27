
package com.losandes.wss;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "UnaCloudWSService", targetNamespace = "http://wss.losandes.com/", wsdlLocation = "http://localhost/UnaCloudWSService/UnaCloudWS?wsdl")
public class UnaCloudWSService
    extends Service
{

    private final static URL UNACLOUDWSSERVICE_WSDL_LOCATION;
    private final static WebServiceException UNACLOUDWSSERVICE_EXCEPTION;
    private final static QName UNACLOUDWSSERVICE_QNAME = new QName("http://wss.losandes.com/", "UnaCloudWSService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost/UnaCloudWSService/UnaCloudWS?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        UNACLOUDWSSERVICE_WSDL_LOCATION = url;
        UNACLOUDWSSERVICE_EXCEPTION = e;
    }

    public UnaCloudWSService() {
        super(__getWsdlLocation(), UNACLOUDWSSERVICE_QNAME);
    }

    public UnaCloudWSService(WebServiceFeature... features) {
        super(__getWsdlLocation(), UNACLOUDWSSERVICE_QNAME, features);
    }

    public UnaCloudWSService(URL wsdlLocation) {
        super(wsdlLocation, UNACLOUDWSSERVICE_QNAME);
    }

    public UnaCloudWSService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, UNACLOUDWSSERVICE_QNAME, features);
    }

    public UnaCloudWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UnaCloudWSService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns UnaCloudWS
     */
    @WebEndpoint(name = "UnaCloudWSPort")
    public UnaCloudWS getUnaCloudWSPort() {
        return super.getPort(new QName("http://wss.losandes.com/", "UnaCloudWSPort"), UnaCloudWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns UnaCloudWS
     */
    @WebEndpoint(name = "UnaCloudWSPort")
    public UnaCloudWS getUnaCloudWSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://wss.losandes.com/", "UnaCloudWSPort"), UnaCloudWS.class, features);
    }

    private static URL __getWsdlLocation() {
        if (UNACLOUDWSSERVICE_EXCEPTION!= null) {
            throw UNACLOUDWSSERVICE_EXCEPTION;
        }
        return UNACLOUDWSSERVICE_WSDL_LOCATION;
    }

}
