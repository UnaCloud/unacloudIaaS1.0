/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package domain;

import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Clouder
 */
public class StartStateManager {

    public static void main(String[] args)throws Exception{
        Socket s = new Socket("127.0.0.1",25689);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        pw.println("START");
        pw.flush();
        s.close();
    }
}
