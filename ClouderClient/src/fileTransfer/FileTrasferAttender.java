/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fileTransfer;

import com.losandes.communication.messages.UnaCloudMessage;
import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.losandes.utils.Log;

import static com.losandes.utils.Constants.*;
import java.util.HashSet;
/**
 * Class responsible for attend file operations requests over the physical machine file system
 * @author Clouder
 */
public class FileTrasferAttender {

    /**
     * Attends a file operation requests. Suported operation are multicast file distributions and file deletions,
     * @param message The request to be attended
     * @param abc The abstract communicator used to attend the request
     */
    public static void attendFileOperation(UnaCloudMessage message,AbstractCommunicator abc){
        Log.print(message.toString());
        //message[0] = PHYSICAL MACHINE OPERATION
        //message[1] = PM_WRITE_FILE
        //message[2] = TIPO TRANSFERENCIA
        //message[3] = ID_TRANSFERENCIA
        //message[4] = RUTA
        //message[5] = TAMAÑO
        //message[6] = NUMERO PARTICIONES
        //message[7] = limpiar directorio
        //message[8...] = IP Destinos
        int tipoTransferencia = message.getInteger(2);
        if(tipoTransferencia==PM_WRITE_FILE_TREE_DISB){
            long idTransferencia = message.getLong(3);
            String ruta = message.getString(4);
            long tamano = message.getLong(5);
            int nParticiones = message.getInteger(6);
            try{
                boolean limpiarDirectorio=Boolean.parseBoolean(message.getString(7));
                if(limpiarDirectorio){
                    String[] formats=new String[]{".vmx",".vmdk",".vmxf",".vmsn",".vmsd",".nvram",".vbox",".vbox-prev",".vdi"};
                    for(File f:new File(ruta).getParentFile().listFiles()){
                        for(String format:formats)if(f.getName().endsWith(format)){
                            f.delete();
                            break;
                        }
                    }
                    File snapshots=new File(new File(ruta).getParentFile(),"Snapshots");
                    if(snapshots.exists()&&snapshots.isDirectory())for(File vdi:snapshots.listFiles()){
                        for(String format:formats)if(vdi.getName().endsWith(format)){
                            vdi.delete();
                            break;
                        }
                    }
                }
            }catch(Exception ex){
            }

            String[] destinos = message.getStrings(8,message.length);
            try {
                TransferenciaArchivo ta = new TransferenciaArchivo(destinos, idTransferencia, nParticiones, new File(ruta),tamano);
                System.out.println("Put transfer "+idTransferencia+" "+ta);
                TreeDistributionChannelManager.transfers.put(idTransferencia, ta);
                abc.writeUTF(OK_MESSAGE);
            } catch( ConnectionException | IOException ex){
            	try {
                    abc.writeUTF(ERROR_MESSAGE);
                } catch (ConnectionException ex1) {
                    Logger.getLogger(FileTrasferAttender.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        else if(tipoTransferencia==PM_DELETE_FILE){
            String ruta = message.getString(3);
            File c = new File(ruta);
            if(ruta.endsWith("lck")){
                deleteFolder(c.getParentFile());
            }else{
                if(c.isFile()&&c.delete())Log.print("borrado "+ruta);
            }
        }
    }

    /**
     * Safely deletes a given folder, just lck files are erased
     * @param c The folder to be
     */
    private static void deleteFolder(File c){
        for(File f:c.listFiles()){
            if(f.isFile()&&f.getName().toLowerCase().endsWith("lck"))f.delete();
            else if(f.isDirectory()&&f.getName().toLowerCase().endsWith("lck")){
                deleteFolder(f);
                f.delete();
            }
        }
    }


}
