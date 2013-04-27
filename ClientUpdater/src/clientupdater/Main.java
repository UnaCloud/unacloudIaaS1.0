/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientupdater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of UnaCloud Client Updater. This component is responsible for downloading the latests version of unacloud client from unacloud server instance.
 * To do this, this class stores a file containing the latests downloaded version of unacloud client. When starting the physical machine, if this component finds that
 * the current version differs with the version at unacloud server then it downloads the latests version and replace the current version. This component has a little failure probability.
 * @author Clouder
 */
public class Main {
    static int SERVER_PORT;
    final static File root = new File("./");
    final static File versions = new File("versions.txt");
    static int CLIENT_PORT = 81;
    static String SERVER_IP = "";
    public static void main(String... args){
        print("InicioActualizador "+Arrays.toString(args)+" "+new Date(System.currentTimeMillis())+" "+new File("./").getAbsolutePath());
        VariableManager.init("./vars");
        SERVER_PORT=VariableManager.getIntValue("VERSION_MANAGER_PORT");
        CLIENT_PORT = VariableManager.getIntValue("CLOUDER_CLIENT_PORT");
        SERVER_IP = VariableManager.getStringValue("CLOUDER_SERVER_IP");
        print("InicioActualizador "+Arrays.toString(args));
        if(args.length>=1){
            int e = Integer.parseInt(args[0]);
            startClient(e);
        }
    }

    /**
     * It starts unacloud client with the given operation. The operations are: start, stop, login and logoff. The version is checked and updated only on start operations.
     * @param opcion
     */
    public static void startClient(int opcion) {
        print(new File("./").getAbsolutePath());
        if(opcion==6){
            opcion=1;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(opcion==1)doUpdate();
        print("Actualizado");
        try {
            Process p = Runtime.getRuntime().exec("java -jar ClouderClient.jar "+opcion+"\"");
        } catch (Throwable t) {
            print("EXE: "+t.getMessage());
        }
    }

    /**
     * Print a string to a log file
     * @param h
     */
    public static void print(String h) {
        System.out.println(h);
        try {
            PrintWriter pwerr = new PrintWriter(new FileOutputStream("./Errores.txt", true), true);
            pwerr.println(h);
            pwerr.close();
        } catch (FileNotFoundException ex) {
        }
    }

    /**
     * Method used to recieve a file over the given Input Stream
     * @param ar
     * @param tam
     * @param isr
     */
    private static void recibirArchivo(File ar, long tam, InputStream isr) {
        int t;
        byte[] buffer = new byte[1024];
        try {
            long cent = 0;
            ar.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(ar);
            System.out.println(ar);
            for (t = 0; cent + buffer.length < tam;) {
                t = isr.read(buffer);
                fos.write(buffer,0,t);
                cent += t;
            }
            while(tam - cent != 0) {
                int a = isr.read(buffer, 0, (int) (tam - cent));
                fos.write(buffer, 0, (int) (tam - cent));
                cent+=a;
            }
            print(ar+" "+tam+" "+cent);
            fos.close();
        } catch (IOException ex) {
            print(ex.getMessage());
        }
    }

    /**
     * This method checks the current version and compares it with the version stored on unacloud server. If the version doesn't match then the newest version is downloaded and installed.
     */
    private static void doUpdate() {
        ServerSocket ss = null;
        try {
            //imprimir("paso 1");
            ss = new ServerSocket(CLIENT_PORT);
            //imprimir("paso 2");
            Socket s = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //imprimir("paso 3");
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            ArrayList<String> versionsFile = gerVersionFile();
            pw.println("update " + versionsFile.get(0));
            //imprimir("paso 4");
            pw.flush();
            String h = br.readLine();
            if (h.equals("version ok")) {
            } else if (h.equals("no last version")) {
                for (int e = 1; e < versionsFile.size(); e++) {
                    File c = new File(root, versionsFile.get(e));
                    if (c.exists()) {
                        c.delete();
                    }
                }
                try {
                    Socket canalArchivos = ss.accept();
                    PrintWriter versionFile = new PrintWriter(versions);
                    versionFile.println(br.readLine());
                    //imprimir("paso 5");
                    int n = Integer.parseInt(br.readLine());
                    //imprimir("paso 6 sarchivos");
                    ss.close();
                    InputStream isr = canalArchivos.getInputStream();
                    String[] f;
                    for (int e = 0; e < n; e++) {
                        //imprimir("paso 7:" + e);
                        f = br.readLine().split("\t");
                        File ar = new File(root, f[0]);
                        versionFile.println(f[0]);
                        long tam = Long.parseLong(f[1]);
                        recibirArchivo(ar, tam, isr);
                    }
                    //imprimir("paso 8");
                    versionFile.flush();
                    versionFile.close();
                    canalArchivos.close();
                    //imprimir("paso 9");
                } catch (Throwable exx) {
                    PrintWriter versionFile = new PrintWriter(versions);
                    versionFile.println("1.0");
                    versionFile.close();
                    print(exx.getMessage());
                }
                System.out.println(br.readLine());
                s.close();
            }

        } catch (Exception ex) {
            print(ex.getLocalizedMessage());
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException ex1) {
                    print(ex1.getMessage());
                }
            }
        }

    }

    /**
     * Rrturns the information contained on stored version file.
     * @return
     */
    private static ArrayList<String> gerVersionFile() {
        ArrayList<String> ret = new ArrayList<String>();
        try {
            BufferedReader ver = new BufferedReader(new FileReader(versions));
            for (String h; (h = ver.readLine()) != null;) {
                ret.add(h);
            }
            ver.close();
            if (ret.size() == 0) {
                ret.add("NOVERSION");
            }
        } catch (IOException ex) {
            ret = new ArrayList<String>();
            ret.add("NOVERSION");
            print(ex.getMessage());
        }
        return ret;
    }
    
}
