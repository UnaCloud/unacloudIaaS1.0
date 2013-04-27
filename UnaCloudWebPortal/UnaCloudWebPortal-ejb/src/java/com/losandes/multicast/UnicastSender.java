/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.multicast;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Class for point to point file transfers
 * @author ga.sotelo69
 */
public class UnicastSender {

    /**
     * Sends a file to the specified host on with the given port
     * @param host
     * @param archivo
     * @param fileTransferSocketPort
     * @throws Exception
     */
    public static void sendFile(String host,File archivo,int fileTransferSocketPort)throws Exception{
        Socket c = new Socket(host,fileTransferSocketPort+1);
        FileInputStream fr = new FileInputStream(archivo);
        OutputStream os = c.getOutputStream();
        byte[] buffer = new byte[1024*1024];
        for(int n;(n=fr.read(buffer))!=-1;){
            os.write(buffer,0,n);
        }
        os.flush();
        c.close();
        fr.close();
    }

}
