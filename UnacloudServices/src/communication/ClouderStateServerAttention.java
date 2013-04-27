package communication;

import com.losandes.communication.security.AbstractCommunicator;
import com.losandes.communication.security.ConectionException;
import com.losandes.communication.security.SecureServerSocket;
import com.losandes.utils.VariableManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * 
 */
public class ClouderStateServerAttention {

    private SecureServerSocket clouderClientServerSocket;
    private static int localPort;
    private Executor poolExe;

 
    public ClouderStateServerAttention() {
        /*XMLServerSocketParameters ClouderStateServerSocketParameters = new XMLServerSocketParameters();
        ClouderStateServerSocketParameters = XMLServerSocketParameters.getClouderClientParametersDecrypted();*/
        poolExe = Executors.newFixedThreadPool(POOL_THREAD_SIZE);
        localPort = VariableManager.getIntValue("CLOUDER_SERVER_PORT");
        System.out.println("Waiting for a UnaCloud Client operation request...");
        connect();
    }

    public void connect() {
        MachineStateManager msm = new MachineStateManager();
        try {
            clouderClientServerSocket = new SecureServerSocket(localPort);
            while (true) {
                AbstractCommunicator sSocket = clouderClientServerSocket.accept();
                poolExe.execute(new ClouderClientAttentionThread(sSocket,msm));
            }
        } catch (IOException ex) {
            System.err.println(ERROR_MESSAGE + "UnaCloud Client server socket connection failed: " + ex.getMessage());
        } catch (ConectionException ex) {
            System.err.println(ERROR_MESSAGE + "UnaCloud Client server socket connection failed: " + ex.getMessage());
        }
    }
}

