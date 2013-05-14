/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientupdater;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    public static void main(String... args) throws FileNotFoundException{
        if(args.length>0&&args[0]!=null&&args[0].length()<2)System.setOut(new PrintStream("logActualizador"+args[0]));
        else System.setOut(new PrintStream("logActualizador"));
        System.out.println("InicioActualizador "+Arrays.toString(args)+" "+new Date(System.currentTimeMillis())+" "+new File("./").getAbsolutePath());
        VariableManager.init("./vars");
        SERVER_PORT=VariableManager.getIntValue("VERSION_MANAGER_PORT");
        CLIENT_PORT = VariableManager.getIntValue("CLOUDER_CLIENT_PORT");
        SERVER_IP = VariableManager.getStringValue("CLOUDER_SERVER_IP");
        System.out.println("InicioActualizador "+Arrays.toString(args));
        if(args.length>=1){
            int e = Integer.parseInt(args[0]);
            startClient(e);
        }
        System.out.close();
    }

    /**
     * It starts unacloud client with the given operation. The operations are: start, stop, login and logoff. The version is checked and updated only on start operations.
     * @param opcion
     */
    public static void startClient(int opcion) {
        System.out.println(new File("./").getAbsolutePath());
        if(opcion==6){
            opcion=1;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(opcion==1)doUpdate();
        System.out.println("Actualizado");
        try {
            Process p = Runtime.getRuntime().exec("java -jar ClouderClient.jar "+opcion+"\"");
        } catch (Throwable t) {
            System.out.println("EXE: "+t.getMessage());
        }
    }

    /**
     * Method used to recieve a file over the given Input Stream
     * @param ar
     * @param tam
     * @param isr
     */
    private static void recibirArchivo(File ar, InputStream isr) {
        byte[] buffer = new byte[1024];
        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            for (int n;(n=isr.read(buffer))!=-1;)baos.write(buffer,0,n);
            baos.close();
            ar.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(ar);
            fos.write(baos.toByteArray());
            fos.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * This method checks the current version and compares it with the version stored on unacloud server. If the version doesn't match then the newest version is downloaded and installed.
     */
    private static void doUpdate() {
        try {
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
                    File c = new File(versionsFile.get(e));
                    if (c.exists())c.delete();
                }
                try {
                    Socket canalArchivos = new Socket(SERVER_IP,SERVER_PORT);
                    PrintWriter pwCanalArchivos=new PrintWriter(canalArchivos.getOutputStream(),true);
                    pwCanalArchivos.println("files");
                    PrintWriter versionFile = new PrintWriter(new FileOutputStream(versions),false);
                    versionFile.println(br.readLine());
                    ZipInputStream zis=new ZipInputStream(canalArchivos.getInputStream());
                    for(ZipEntry ze;(ze=zis.getNextEntry())!=null;){
                        versionFile.println(ze.getName());
                        recibirArchivo(new File(ze.getName()),zis);
                    }
                    versionFile.flush();
                    versionFile.close();
                    pwCanalArchivos.close();
                    canalArchivos.close();
                } catch (Throwable exx) {
                    System.out.println("error Conetando a "+SERVER_IP+" en 80");
                    PrintWriter versionFile = new PrintWriter(versions);
                    versionFile.println("1.0");
                    versionFile.close();
                    System.out.println(exx.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
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
            System.out.println(ex.getMessage());
        }
        return ret;
    }
}