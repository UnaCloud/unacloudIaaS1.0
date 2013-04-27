package domain;

import com.losandes.dataChannel.DataServerSocket;
import communication.ClouderStateServerAttention;
import communication.MachineStateManager;

/**
 * @author Eduardo Rosales
 * Responsible for starting the Clouder State Server
 *
 */
public class Main {

    public static void main(String[] args) {
        DataServerSocket.init();
        new ClouderStateServerAttention();
    }
}//end of Main

