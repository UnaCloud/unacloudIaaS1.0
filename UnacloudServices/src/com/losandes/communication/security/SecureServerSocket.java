/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.communication.security;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Clouder
 */
public class SecureServerSocket {

    private ServerSocket ss;

    public SecureServerSocket(int port) throws IOException{
        ss = new ServerSocket(port);
    }

    public AbstractCommunicator accept() throws ConectionException, IOException{
        SecureServerStream ssss = new SecureServerStream();
        ssss.connect(ss.accept());
        return ssss;
    }

    public void close(){
        try {
            ss.close();
        } catch (IOException ex) {
        }
    }
}
