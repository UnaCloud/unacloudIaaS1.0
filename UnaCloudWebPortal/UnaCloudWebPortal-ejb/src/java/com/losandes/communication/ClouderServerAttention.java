package com.losandes.communication;

import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.SecureServerSocket;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.ejb.EJB;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * Responsible for listening to the Clouder Clients
 */
public class ClouderServerAttention {

    private SecureServerSocket clouderClientServerSocket;
    private static int localPort;
    private Executor poolExe;
    @EJB
    private IPhysicalMachineServices physicalMachineServices;

    /**
     * Responsible for obtaining decrypted data connection and listening to Clouder Clients
     */
    public ClouderServerAttention() {
            poolExe = Executors.newFixedThreadPool(POOL_THREAD_SIZE);
            localPort = 81;
            System.out.println("Waiting for a Clouder Client operation request...");
            ServerSocketThread server = new ServerSocketThread();
            PhysicalMachine physicalMachine = physicalMachineServices.getPhysicalMachineByName("ISC201");
            System.out.println("Nombre: "+physicalMachine.getPhysicalMachineName() );
            server.start();                  
    }

    /**
     * Responsible for connecting with Clouder Clients and start a communication thread
     */
    public void connect() {
        try {
            clouderClientServerSocket = new SecureServerSocket(localPort);
            while (true) {
                AbstractCommunicator sSocket = clouderClientServerSocket.accept();
                System.out.println("Clouder Server accepted");
                poolExe.execute(new ClouderClientAttentionThread(sSocket));
            }
        } catch (Exception ex) {
            System.err.println(ERROR_MESSAGE + "Clouder Client server socket connection failed: " + ex.getMessage());
        }
    }

    class ServerSocketThread extends Thread {

        public void run() {
            connect();
        }
    }
}//end of ClouderServerAttention

