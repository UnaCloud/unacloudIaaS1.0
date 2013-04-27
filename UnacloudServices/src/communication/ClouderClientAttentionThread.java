package communication;

import com.losandes.communication.security.AbstractCommunicator;
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
            String clouderServerRequest = communication.readUTF();
            if(clouderServerRequest==null)return;
            //spliting the message in a processable vector
            String[] clouderClientRequestSplitted = clouderServerRequest.split(MESSAGE_SEPARATOR_TOKEN);
            //clouderClientOperationResult is the result variable for responding to Clouder Server
            String clouderClientOperationResult = "";
            // operationDomain = {VIRTUAL_MACHINE_OPERATION, PHYSICAL_MACHINE_OPERATION}
            int operationDomain = 0;
            if (clouderClientRequestSplitted[0] != null && !clouderClientRequestSplitted[0].equals("")) {
                operationDomain = Integer.parseInt(clouderClientRequestSplitted[0]);
            }
            try{
                if(clouderClientRequestSplitted.length>1&&Integer.parseInt(clouderClientRequestSplitted[1])!=4)System.out.println(Arrays.toString(clouderClientRequestSplitted)+" "+sdf.format(new Date(System.currentTimeMillis())));
            }catch(Throwable a){}

            if (operationDomain != 0 && operationDomain < 3) {
                if (operationDomain == DATABASE_OPERATION) {
                    int OperationType = 0;
                    if (clouderClientRequestSplitted[1] != null && !clouderClientRequestSplitted[1].equals("")) {
                        OperationType = Integer.parseInt(clouderClientRequestSplitted[1]);
                    }
                    String physicalMachineName = "";
                    if (clouderClientRequestSplitted[2] != null && !clouderClientRequestSplitted[2].equals("")) {
                        physicalMachineName = clouderClientRequestSplitted[2];
                    }
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
                            if (clouderClientRequestSplitted[3] != null && !clouderClientRequestSplitted[3].equals("")) {
                                String physicalMachineUser = clouderClientRequestSplitted[3];
                                persistence.logginPhysicalMachineUser(physicalMachineName, physicalMachineUser);
                            }
                            break;
                        case LOGOUT_DB:
                            persistence.logginPhysicalMachineUser(physicalMachineName, NOTHING_AVAILABLE);
                            break;
                        case REPORT_DB:
                            machineManager.reportMachine(physicalMachineName);
                            break;
                        case VIRTUAL_MACHINE_STATE_DB:
                            persistence.updateVirtualMachineState(clouderClientRequestSplitted[3],Integer.parseInt(clouderClientRequestSplitted[4]),clouderClientRequestSplitted[5]);
                            machineManager.reportMachine(physicalMachineName);
                            break;
                        case VIRTUAL_MACHINE_CPU_STATE:
                            persistence.updateVirtualMachineCPUState(clouderClientRequestSplitted[3],VirtualMachineCPUStates.valueOf(clouderClientRequestSplitted[4]));
                            break;
                        default:
                            clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Client operation request is invalid: " + OperationType;
                            System.err.println(clouderClientOperationResult);
                    }
                } else if (operationDomain == PHYSICAL_MACHINE_OPERATION) {
                    System.out.println("The Clouder Server operation request is physical machine type");
                    // TODO por definirse
                    int physicalOperationType = 0;
                    if (clouderClientRequestSplitted.length > 1) {
                        if (clouderClientRequestSplitted[1] != null && !clouderClientRequestSplitted[1].equals("")) {
                            physicalOperationType = Integer.parseInt(clouderClientRequestSplitted[1]);
                        }
                    }
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
                    }
                } else {
                    clouderClientOperationResult += ERROR_MESSAGE + "The Clouder Server request type is invalid: " + operationDomain;
                    System.err.println(clouderClientOperationResult);
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

