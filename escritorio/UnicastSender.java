/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast;

import communication.ServerSocketCommunication;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static utils.Constants.*;

/**
 *
 * @author Clouder
 */
public class UnicastSender {

    public void atenderPedirArchivos(String[] solicitud, ServerSocketCommunication conexion) {
        System.out.println("Entró");
        String ruta = solicitud[2];
        File archivosAEnviar[];
        {
            ArrayList<File> archivos = new ArrayList<File>();
            File maquina = new File(ruta).getParentFile();
            for (File c : maquina.listFiles()) {
                if (c.isFile() && c.canRead()) {
                    archivos.add(c);
                }
            }
            archivos.toArray(archivosAEnviar = new File[archivos.size()]);
        }
        String[] respuesta = new String[2+archivosAEnviar.length*2];
        respuesta[0] = "" + archivosAEnviar.length;
        ServerSocket ss;
        try {
            ss = new ServerSocket();
            ss.bind(null);
            respuesta[1] = "" + ss.getLocalPort();
            System.out.println("Binding realizado");
        } catch (IOException ex) {
            respuesta[1] = "" + -1;
            respuesta[0] = "" + 0;
            conexion.writeUTF(setupMessage(respuesta));
            System.out.println("Error");
            return;
            //Enviar cero archivos
        }
        for (int e = 0; e < archivosAEnviar.length; e++) {
            respuesta[e * 2 + 2] = archivosAEnviar[e].getName();
            respuesta[e * 2 + 3] = "" + archivosAEnviar[e].length();
        }
        System.out.println("Respuesta enviada");
        conexion.writeUTF(setupMessage(respuesta));
        byte[] buffer = new byte[1024];
        int l;
        Socket s;
        OutputStream os;
        try {
            s = ss.accept();
            os = s.getOutputStream();
        } catch (IOException ex){
            ex.printStackTrace();
            return;
        }
        System.out.println("DOS abierto");
        for (int e = 0; e < archivosAEnviar.length; e++) {
            System.out.println("Enviando:"+archivosAEnviar[e].getName());
            FileInputStream fis=null;
            try {
                fis = new FileInputStream(archivosAEnviar[e]);
                while ((l = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, l);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(fis!=null)try {
                fis.close();
            } catch (IOException ex) {
            }
        }
        System.out.println("DOS cerrado");
        try {
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(UnicastSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        conexion.disconnet();
    }
}

