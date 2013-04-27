/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.utils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import static com.losandes.utils.Constants.*;

/**
 *
 * @author Clouder
 */
public class Log {

    public static void print(String msg){
        try{
            /*Socket s = new Socket("unacloud.uniandes.edu.co",VariableManager.getIntValue("LOG_SOCKET"));
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println(executeCommandOutput("hostname", true).trim()+" : "+msg);
            pw.flush();
            pw.close();
            s.close();*/
        }catch(Exception e){

        }
    }
}
