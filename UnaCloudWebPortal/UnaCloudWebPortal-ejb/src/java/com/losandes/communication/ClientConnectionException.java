/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.communication;

/**
 * Exception used to report errors when connecting to clients
 * @author Clouder
 */
public class ClientConnectionException extends Exception{

    /**
     * Constructs a new client connection exception with the given message
     * @param message
     */
    public ClientConnectionException(String message){
        super(message);
    }


}
