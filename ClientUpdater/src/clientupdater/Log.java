/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientupdater;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Clouder
 */
public class Log {

    public static void print(String msg){
        try{
            PrintWriter pw = new PrintWriter(new FileWriter("Log.txt",true));
            pw.println(msg);
            pw.close();
        }catch(Throwable a){}
    }
    public static void print2(String msg){
        try{
            PrintWriter pw = new PrintWriter(new FileWriter("Log2.txt",true));
            pw.println(msg);
            pw.close();
        }catch(Throwable a){}
    }
}
