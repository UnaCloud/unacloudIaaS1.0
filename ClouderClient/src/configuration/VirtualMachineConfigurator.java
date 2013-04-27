package configuration;

import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.communication.security.utils.ConnectionException;
import com.losandes.utils.VariableManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import virtualmachine.Hypervisor;
import virtualmachine.HypervisorFactory;
import static com.losandes.utils.Constants.*;
import virtualmachine.HypervisorOperationException;

/**
 * Class responsible for attending virtual machine configuration requests
 * @author Clouder
 */
public class VirtualMachineConfigurator {

    /**
     * Temporal file to be used for writeFileOnVirtualMachine operations
     */
    private static final File temporalFile = new File("temp/a.txt");

    /**
     * Attends a requets to write a file on a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     */
    private void writeFileOnVirtualMachine(String[] solicitud, AbstractCommunicator con) {
        try {
            String hypervisor = solicitud[2], destinationMachine = solicitud[3], login = solicitud[4], pass = solicitud[5], destinationFileRute = solicitud[6], rutaVMRUN = solicitud[7];
            temporalFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(temporalFile);
            recieveFileOverChannel(fos, con);
            System.out.println("archivo recibido");
            try {
                HypervisorFactory.getHypervisor(Integer.parseInt(hypervisor), rutaVMRUN, destinationMachine).copyFileOnVirtualMachine(login, pass, destinationFileRute, temporalFile);
                con.writeUTF("Archivo copiado exitosamente");
            } catch (HypervisorOperationException ex) {
                con.writeUTF("Error copiando el archivo " + ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Attends a request to change a virtual machine MAC address
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     */
    private void changeVirtualMachineMACAddress(String[] solicitud, AbstractCommunicator con) {
        System.out.println("cambiarMacMaquina");
        try {
            String destinationMachine = solicitud[2], prefijo = solicitud[3], rutavmx = solicitud[4];
            if (!(rutavmx.endsWith("/") || rutavmx.endsWith("\\"))) {
                rutavmx = rutavmx + "/";
            }
            File maquina = new File(destinationMachine);

            Process p = Runtime.getRuntime().exec(DOUBLE_QUOTE + rutavmx + "mac.exe" + DOUBLE_QUOTE + " \"" + maquina.getAbsolutePath() + "\"");
            if (p.getErrorStream().read(new byte[256]) != -1) {
                con.writeUTF("Error cambiando la mac");
                return;
            }
            BufferedReader brMaquinaVMX = new BufferedReader(new FileReader(maquina));
            boolean encontrado = false;
            for (String h; (h = brMaquinaVMX.readLine()) != null && !encontrado;) {
                if (h.startsWith(prefijo)) {
                    con.writeUTF(h);
                    encontrado = true;
                }
            }
            brMaquinaVMX.close();
            if (!encontrado) {
                con.writeUTF("Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Terminar cambiarMacMaquina");
    }

    /**
     * Attends a request to obtain a virtual machine MAC address
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     */
    private void obtainVirtualMachineMACAddress(String[] solicitud, AbstractCommunicator con) {
        System.out.println("sacarMacMaquina");
        //rutaMaquina,prefijo
        String destinationMachine = solicitud[2];
        File maquina = new File(destinationMachine);
        String prefijo = solicitud[3];
        boolean encontrado = false;
        try {
            BufferedReader brMaquinaVMX = new BufferedReader(new FileReader(maquina));
            for (String h; (h = brMaquinaVMX.readLine()) != null && !encontrado;) {
                if (h.startsWith(prefijo)) {
                    con.writeUTF(h);
                    encontrado = true;
                }
            }
            brMaquinaVMX.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!encontrado) {
            try {
                con.writeUTF("Error");
            } catch (ConnectionException ex) {
                Logger.getLogger(VirtualMachineConfigurator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Termino sacarMacMaquina");
    }

    /**
     * Recieves a File over the file transfer socket and writes it on the given File output stream
     * @param fos The file output stream in which the file will be write
     * @param con The channel to interact with UnaCloud server
     * @throws Exception
     */
    private void recieveFileOverChannel(FileOutputStream fos, AbstractCommunicator con) throws Exception {
        ServerSocket ss = new ServerSocket(VariableManager.getIntValue("FILE_TRANSFER_SOCKET") + 1);
        con.writeUTF(OK_MESSAGE);
        Socket c = ss.accept();
        ss.close();
        InputStream isr = (c.getInputStream());
        byte[] buffer = new byte[1024];
        for (int e = 0; (e = isr.read(buffer)) != -1;) {
            fos.write(buffer, 0, e);
            System.out.println(e);
        }
        c.close();
        fos.close();
    }

    /**
     * Attends a request to write a file on this physical machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     */
    private void recieveUnicastFile(String[] solicitud, AbstractCommunicator con) {
        System.out.println("recibirArchivoUnicast");
        try {
            String rutaArchivo = solicitud[2];
            new File(rutaArchivo).getParentFile().mkdirs();
            File c = new File(rutaArchivo);
            con.writeUTF("OK");
            FileOutputStream fos = new FileOutputStream(c);
            recieveFileOverChannel(fos, con);
            con.writeUTF("OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Termino recibirArchivoUnicast");
    }


    /**
     * Attends a request to start a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @throws ConnectionException
     */
    private void startVirtualMachine(String[] solicitud, AbstractCommunicator con) throws ConnectionException {
        String hypervisor = solicitud[2], destinationMachine = solicitud[3], rutaVMRUN = solicitud[4];
        try {
            HypervisorFactory.getHypervisor(Integer.parseInt(hypervisor), rutaVMRUN, destinationMachine).turnOnVirtualMachine();
            con.writeUTF("Maquina encendida exitosamente");
        } catch (HypervisorOperationException ex) {
            con.writeUTF(ex.getMessage());
        }

    }

    /**
     * Attends a request to execute commands on a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @throws ConnectionException
     */
    private void executeCommandsOnVirtualMachine(String[] solicitud, AbstractCommunicator con) throws ConnectionException {
        String hypervisor = solicitud[2], destinationMachine = solicitud[3], user = solicitud[4], pass = solicitud[5], rutaVMRUN = solicitud[6];
        int numeroComandos = Integer.parseInt(solicitud[7]);
        String respuesta[] = new String[numeroComandos];
        Hypervisor v = HypervisorFactory.getHypervisor(Integer.parseInt(hypervisor), rutaVMRUN, destinationMachine);
        for (int e = 0; e < numeroComandos; e++) {
            String comando = solicitud[8 + e];
            try {
                v.executeCommandOnMachine(user, pass, comando);
                respuesta[e] = "Successful execution";
            } catch (HypervisorOperationException ex) {
                respuesta[e] = ex.getMessage();
            }
        }
        con.writeUTF(respuesta);
    }

    /**
     * Attends a request to stop a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @throws ConnectionException
     */
    private void stopVirtualMachine(String[] solicitud, AbstractCommunicator con) throws ConnectionException {
        String hypervisor = solicitud[2], destinationMachine = solicitud[3], rutaVMRUN = solicitud[4];
        try {
            HypervisorFactory.getHypervisor(Integer.parseInt(hypervisor), rutaVMRUN, destinationMachine).turnOffVirtualMachine();
            con.writeUTF("Maquina apagada exitosamente");
        } catch (HypervisorOperationException ex) {
            con.writeUTF(ex.getMessage());
        }
    }

    /**
     * Attends a request to make a configuration operation over a virtual machine
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     * @return
     */
    public boolean attendConfigurationRequest(String[] solicitud, AbstractCommunicator con) {
        System.out.println(Arrays.toString(solicitud));
        try {
            if (solicitud[1].equals("RecibirArchivoUnicast")) {
                recieveUnicastFile(solicitud, con);
            } else if (solicitud[1].equals("EscribirArchivoMaquina")) {
                writeFileOnVirtualMachine(solicitud, con);
            } else if (solicitud[1].equals("StartMachine")) {
                startVirtualMachine(solicitud, con);
            } else if (solicitud[1].equals("StopMachine")) {
                stopVirtualMachine(solicitud, con);
            } else if (solicitud[1].equals("comandMachine")) {
                executeCommandsOnVirtualMachine(solicitud, con);
            } else if (solicitud[1].equals("CambiarMac")) {
                changeVirtualMachineMACAddress(solicitud, con);
            } else if (solicitud[1].equals("SacarMac")) {
                obtainVirtualMachineMACAddress(solicitud, con);
            } else if (solicitud[1].equals("SacarIP")) {
                obtainLinuxIP(solicitud, con);
            } else {
                return false;
            }
            System.out.println("Sali");
            return true;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param solicitud The UnaCloud server request
     * @param con The channel to interact with UnaCloud server
     */
    private void obtainLinuxIP(String[] solicitud, AbstractCommunicator con) {
        try {
            String destinationMachine = solicitud[2];
            String login = solicitud[3];
            String pass = solicitud[4];
            String rutaVMRUN = solicitud[5];

            File maquina = new File(destinationMachine);
            temporalFile.getParentFile().mkdirs();
            PrintWriter pw = new PrintWriter(temporalFile);
            pw.print("#!/bin/bash\n");
            pw.print("ifconfig eth0 > /ifconfig.txt\n");
            pw.close();
            Process p = Runtime.getRuntime().exec(DOUBLE_QUOTE + rutaVMRUN + VMW_RUN_FILE + DOUBLE_QUOTE + " -gu " + login + " -gp " + pass + " copyFileFromHostToGuest \"" + maquina.getAbsolutePath() + "\" \"" + temporalFile.getAbsolutePath() + "\"  /ifconfig.sh");
            if (p.getErrorStream().read(new byte[256]) != -1) {
                con.writeUTF("Error copiando el archivo");
                return;
            }
            p = Runtime.getRuntime().exec(DOUBLE_QUOTE + rutaVMRUN + VMW_RUN_FILE + DOUBLE_QUOTE + " -T -ws -gu " + login + " -gp " + pass + " runProgramInGuest \"" + maquina.getAbsolutePath() + "\" /bin/bash /ifconfig.sh");
            if (p.getErrorStream().read(new byte[256]) != -1) {
                con.writeUTF("Error copiando el archivo");
                return;
            }
            p = Runtime.getRuntime().exec(DOUBLE_QUOTE + rutaVMRUN + VMW_RUN_FILE + DOUBLE_QUOTE + " -gu " + login + " -gp " + pass + " copyFileFromGuestToHost \"" + maquina.getAbsolutePath() + "\" /ifconfig.txt \"" + temporalFile.getAbsolutePath() + "\"");
            if (p.getErrorStream().read(new byte[256]) != -1) {
                con.writeUTF("Error copiando el archivo");
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(temporalFile));
            for (String h; (h = br.readLine()) != null;) {
                System.out.println(h);
                if (h.contains("inet addr:")) {
                    String[] t = h.trim().split(" +");
                    br.close();
                    if (t[1].endsWith(":")) {
                        con.writeUTF(t[2]);
                    } else {
                        con.writeUTF(t[1].split(":")[1]);
                    }
                    return;
                }
            }
            br.close();
            con.writeUTF("Not up yet");
            System.out.println("orden enviada");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
