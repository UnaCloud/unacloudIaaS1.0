/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package administration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;


/**
 *
 * @author Clouder
 */
public class ClientInstallationBean {

    /** Creates a new instance of ClientInstallationBean */
    public ClientInstallationBean() {

    }

    public StreamedContent getFile() {
        FileInputStream fis = null;
        try {
            ZIPFileOutputStream zos = new ZIPFileOutputStream(new FileOutputStream("./a.zip"));
            zos.addFile(new File("./ClientUpdater.jar"));
            if(new File("./vars").exists())zos.addFile(new File("./vars"));
            zos.addFile(new File("./start.bat"));
            zos.addFile(new File("./stop.bat"));
            zos.addFile(new File("./logon.bat"));
            zos.addFile(new File("./logoff.bat"));
            zos.close();
            FileInputStream out = new FileInputStream(new File("./a.zip"));
            StreamedContent f = new DefaultStreamedContent(out, "application/zip", "UnacloudClientAgent.zip");
            return f;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    class ZIPFileOutputStream extends ZipOutputStream{

        public ZIPFileOutputStream(OutputStream out){
            super(out);
        }

        public void addFile(File f){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                ZipEntry ze = new ZipEntry(f.getName());
                putNextEntry(ze);
                byte[] b = new byte[256];
                for (int e; (e = fis.read(b)) != -1;) {
                    write(b,0,e);
                }
                fis.close();
                closeEntry();
            } catch (IOException ex) {
                Logger.getLogger(ClientInstallationBean.class.getName()).log(Level.SEVERE, null, ex);
            }finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientInstallationBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
