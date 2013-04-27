/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.communication.security;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import static com.losandes.utils.Constants.*;

/**
 *
 * @author Clouder
 */
public abstract class AbstractCommunicator {

    protected Socket socket;
    protected DataOutputStream dataOutput;
    protected DataInputStream dataInput;
    protected Cipher cipher;
    protected Key key;
    protected boolean cifrado=true;

    public AbstractCommunicator(){
    }
    /**
     * Responsible for connecting with Clouder Server using the Clouder Client client socket
     * if method returns true, the process was successful
     * if method return false, the process failed
     */
    public void connect(Socket s)throws ConectionException{
        socket = s;
        /*try {
            socket.setSoTimeout(1000);
        } catch (SocketException ex) {
        }*/
        try {
            dataOutput = new DataOutputStream(socket.getOutputStream());
            dataInput = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            close();
            throw new ConectionException("Unable to create data streams.");
        }
    }

    /**
     * Responsible for disconnecting of Clouder Server using the Clouder Client client socket
     * if method returns true, the process was successful
     * if method return false, the process failed
     */
    public void close(){
        try {
            if(dataOutput!=null)dataOutput.close();
        } catch (Exception ex) {
        }try{
            if(dataInput!=null)dataInput.close();
        } catch (Exception ex) {
        }try{
            if(socket!=null)socket.close();
        }catch(Exception ex){
            
        }
        dataOutput=null;
        dataInput=null;
        socket=null;
    }


    public void writeUTF(String ... msg)throws ConectionException{
         writeString(makeMessage(msg));
    }

    public String readUTF()throws ConectionException{
         return readString();
    }

    public String[] readUTFList()throws ConectionException{
         return readString().split(MESSAGE_SEPARATOR_TOKEN);
    }

    private String readString()throws ConectionException{
        try {
            if(cifrado){
                cipher.init(Cipher.DECRYPT_MODE, key);
                int n = dataInput.readInt();
                String ret="";
                for(int e=0;e<n;e++){
                    int j = dataInput.readInt();
                    if(j>10000){
                        throw new ConectionException("Bad message "+socket.getRemoteSocketAddress());
                    }
                    byte[] cipherText = new byte[j];
                    dataInput.readFully(cipherText, 0, cipherText.length);
                    byte[] plainText = cipher.doFinal(cipherText);
                    ret+=new String(plainText);
                }

                return ret;
            }else{
                int n = dataInput.readInt();
                byte[] cipherText = new byte[n];
                dataInput.readFully(cipherText, 0, cipherText.length);
                return new String(cipherText);
            }
        } catch (Exception ex){
            close();
            System.err.println("Unable to read msg. "+ex.getMessage());
            throw new ConectionException("Unable to read msg. "+ex.getLocalizedMessage());
        }

    }

    private void writeString(String line)throws ConectionException{
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException ex) {
            throw new ConectionException("Unable to init cipher.");
        }
        byte[] h=line.getBytes();
        int n = (h.length-1)/cipher.getBlockSize()+1,t=cipher.getBlockSize();
        try {
            if(cifrado){
                
                long o = System.currentTimeMillis();
                dataOutput.writeInt(n);
                for(int e=0;e<n;e++){
                    byte[] cipherText = cipher.doFinal(h,e*t,(h.length-(e+1)*t)<0?h.length-e*t:t);
                    dataOutput.writeInt(cipherText.length);
                    dataOutput.write(cipherText);
                }
            }else{
                dataOutput.writeInt(h.length);
                dataOutput.write(h);
            }
            dataOutput.flush();
        } catch (Exception ex) {
            close();
            throw new ConectionException("Unable to send message." +ex.getLocalizedMessage());
        }

    }

     private String makeMessage(String ... args){
        String resp = "";
        int e=0;
        while(args[e]==null&&e<args.length)e++;
        if(args.length!=e)resp=args[e];
        for(e++;e<args.length;e++)if(args[e]!=null)resp+=MESSAGE_SEPARATOR_TOKEN+args[e];
        return resp;
    }
}
