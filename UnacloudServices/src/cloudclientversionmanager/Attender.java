/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudclientversionmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Clouder
 */
public class Attender extends Thread{

    private Socket s;
    static final int CLIENT_PORT = 81;
    static File raiz = new File("version");
    private String lastVersion="0.0";
    private ArrayList<Archivo> archivos = new ArrayList<Archivo>();

    public Attender(Socket s) {
        this.s = s;
        start();
    }

    public void run() {
        System.out.println("Atendiendo");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            String h = br.readLine();
            String[] t = h.split(" +");
            if (t[0].equals("update")) {
                String version = t[1],serverVersion=getVersion();
                int port = CLIENT_PORT;
                if(t.length>2)try{
                    port=Integer.parseInt(t[2]);
                }catch(Throwable a){}
                System.out.println(version+"\t"+serverVersion);
                if (version.equals(serverVersion)) {
                    pw.println("version ok");
                    pw.flush();
                } else {
                    pw.println("no last version");
                    pw.println(serverVersion);
                    ArrayList<Archivo> nuevos = getArchivos(serverVersion);
                    pw.println(""+nuevos.size());
                    for(Archivo f : nuevos){
                        pw.println(f.path + "\t" + f.f.length());
                    }
                    pw.flush();
                    Socket canalArchivos = new Socket(s.getInetAddress(), port);
                    OutputStream os = canalArchivos.getOutputStream();
                    for (Archivo f : nuevos) {
                        enviarArchivo(os, f.f);
                    }
                    canalArchivos.close();
                    pw.println("EXIT");
                    pw.flush();
                }
                s.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    private void enviarArchivo(OutputStream os, File c) {
        try {
            FileInputStream fis = new FileInputStream(c);
            byte[] buffer = new byte[1024];
            int a;
            while ((a = fis.read(buffer)) != -1) {
                os.write(buffer, 0, a);
            }
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getVersion() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("version.txt"));
            String h = br.readLine();
            br.close();
            return h;
        } catch (Exception e) {
            try{
                PrintWriter pw = new PrintWriter("version.txt");
                pw.println("1.0");
                pw.close();
            }catch(Exception ex){
            }
        }
        return "1.0";
    }

    private ArrayList<Archivo> getArchivos(String version){
        if(version.equals(lastVersion))return archivos;
        ArrayList<Archivo> temp = new ArrayList<Archivo>();
        recursiveFill(temp,new Archivo(raiz,""));
        archivos=temp;
        lastVersion=version;
        return archivos;
    }

    private void recursiveFill(ArrayList<Archivo> archivos,Archivo file){
        if(file.f.isFile()){
            archivos.add(file);
        }
        else if(file.f.isDirectory())for(File c:file.f.listFiles()){
            recursiveFill(archivos,new Archivo(c,file.path+"/"+c.getName()));

        }
    }

    class Archivo{
        File f;
        String path;

        public Archivo(File f, String path) {
            this.f = f;
            this.path = path;
        }

    }

}
