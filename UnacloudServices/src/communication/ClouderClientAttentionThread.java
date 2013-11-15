package communication;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.messages.UnaCloudMessage;
import com.losandes.communication.security.utils.AbstractCommunicator;
import com.losandes.persistence.PersistenceServices;
import com.losandes.utils.VirtualMachineCPUStates;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * Responsible for attending or discarding a Clouder Server operation request in a thread
 */
public class ClouderClientAttentionThread extends Thread {

    private AbstractCommunicator communication;

    private static MachineStateManager machineManager;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    /**
     * Constructor method
     * @param clientSocket
     */
    public ClouderClientAttentionThread(AbstractCommunicator ac,MachineStateManager machineManager) {
        communication = ac;
        this.machineManager=machineManager;
    }

    /**
     * Responsible for sorting or discarding the Clouder Server operation request
     */
    public void run() {
        try {
            //receiving a operation request from the Clouder Server
            UnaCloudMessage clouderServerRequest = communication.readUTFList();
            if(clouderServerRequest==null)return;
            //clouderClientOperationResult is the result variable for responding to Clouder Server
            String clouderClientOperationResult = "";
            // operationDomain = {VIRTUAL_MACHINE_OPERATION, PHYSICAL_MACHINE_OPERATION}
            int operationDomain = clouderServerRequest.getInteger(0);

            if (operationDomain != 0 && operationDomain < 3) {
                if (operationDomain == UnaCloudAbstractMessage.DATABASE_OPERATION) {
                    int OperationType = clouderServerRequest.getInteger(1);
                    String physicalMachineName = clouderServerRequest.getString(2);
                    PersistenceServices persistence = new PersistenceServices();
                    switch (OperationType) {
                        case TURN_ON_DB:
                            persistence.updatePhysicalMachineState(ON_STATE,physicalMachineName);
                            //REPORT_DELAY,REPORT_FAIL_LIMIT
                            communication.writeUTF(MachineStateManager.period+MESSAGE_SEPARATOR_TOKEN+MachineStateManager.limitFail);
                            break;
                        case TURN_OFF_DB:
                            persistence.updatePhysicalMachineState(OFF_STATE,physicalMachineName);
                            break;
                        case LOGIN_DB:
                            String physicalMachineUser=clouderServerRequest.getString(3);
                            persistence.logginPhysicalMachineUser(physicalMachineName, physicalMachineUser);
                            machineManager.reportMachine(physicalMachineName);
                            break;
                        case LOGOUT_DB:
                            persistence.logginPhysicalMachineUser(physicalMachineName, NOTHING_AVAILABLE);
                            break;
                        case VIRTUAL_MACHINE_STATE_DB:
                            persistence.updateVirtualMachineState(clouderServerRequest.getString(3),clouderServerRequest.getInteger(4),clouderServerRequest.getString(5));
                            machineManager.reportMachine(physicalMachineName);
                            break;
                        case VIRTUAL_MACHINE_CPU_STATE:
                            persistence.updateVirtualMachineCPUState(clouderServerRequest.getString(3),VirtualMachineCPUStates.valueOf(clouderServerRequest.getString(4)));
                            break;
                        default:
                            clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Client operation request is invalid: " + OperationType;
                            System.err.println(clouderClientOperationResult);
                            System.out.println(clouderServerRequest);
                    }
                } else if (operationDomain == UnaCloudAbstractMessage.PHYSICAL_MACHINE_OPERATION) {
                    System.out.println("The Clouder Server operation request is physical machine type");
                    // TODO por definirse
                    int physicalOperationType = clouderServerRequest.getInteger(1);
                    switch (physicalOperationType) {
                        case PM_TURN_OFF:
                            break;
                        case PM_RESTART:
                            break;
                        case PM_LOGOUT:
                            break;
                        case PM_MONITOR:
                            break;
                        default:
                            clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server physical machine operation request is invalid: " + physicalOperationType;
                            System.err.println(clouderClientOperationResult);
                            System.out.println(clouderServerRequest);
                    }
                } else {
                    clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server request type is invalid: " + operationDomain;
                    System.err.println(clouderClientOperationResult);
                    System.out.println(clouderServerRequest);
                }
            } else {
                clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server request is null or an invalid number: " + operationDomain;
                System.err.println(clouderClientOperationResult);
            }
            communication.close();

        } catch (Exception ex) {
            System.err.println("The communication process with Clouder Server failed in ClouderServerAttentionThread: " + ex.getMessage());
        }
    }
}//end of ClouderServerAttentionThread

