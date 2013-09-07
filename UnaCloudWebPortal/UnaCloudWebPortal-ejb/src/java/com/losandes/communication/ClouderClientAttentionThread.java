package com.losandes.communication;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.security.utils.*;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import java.net.Socket;
import javax.ejb.EJB;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * Responsible for attending or discarding a Clouder Client operation request in a thread
 */
public class ClouderClientAttentionThread extends Thread {

    @EJB
    private IPhysicalMachineServices physicalMachineServices;
    
    private AbstractCommunicator communication;

    /**
     * Constructor method
     * @param clientSocket
     */
    public ClouderClientAttentionThread(AbstractCommunicator clientSocket) {
        communication = clientSocket;
    }

    /**
     * Responsible for sorting or discarding the Clouder Client operation request
     */
    public void run() {
        try {
            System.out.println("Entro 1");
            //receiving a operation request from the Clouder Client
            String clouderServerRequest = communication.readUTF();
            System.out.println("Entro 2");
            //spliting the message in a processable vector
            String[] clouderServerRequestSplitted = clouderServerRequest.split(MESSAGE_SEPARATOR_TOKEN);
            System.out.println("Entro 3");
            //clouderServerOperationResult is the result variable for responding to Clouder Client
            String clouderServerOperationResult = "";
            // operationDomain = {DATABASE_OPERATION, REGISTRATION_OPERATION}
            int operationDomain = 0;
            if (clouderServerRequestSplitted[0] != null && !clouderServerRequestSplitted[0].equals("")) {
                operationDomain = Integer.parseInt(clouderServerRequestSplitted[0]);
                System.out.println(" ESTO ES OD: "+operationDomain);
            }
            System.out.println("Entro 4");

            if (operationDomain != 0 && operationDomain < 3) {
                if (operationDomain == UnaCloudAbstractMessage.DATABASE_OPERATION) {
                    System.out.println("The Clouder Server operation request is database operation");
                    int databaseOperationType = 0;
                    String physicalMachineName ="";
                    if (clouderServerRequestSplitted[1] != null && !clouderServerRequestSplitted[1].equals("")) {
                        databaseOperationType = Integer.parseInt(clouderServerRequestSplitted[1]);
                        System.out.println(" ESTO ES OT: "+databaseOperationType);
                    }
                    System.out.println("Entro 5");
                    if (clouderServerRequestSplitted[2] != null && !clouderServerRequestSplitted[2].equals("")) {
                        physicalMachineName = clouderServerRequestSplitted[2];
                        System.out.println(" ESTO ES PMN: "+physicalMachineName);
                    }
                    System.out.println("Entro 6");

                    switch (databaseOperationType) {
                        case TURN_ON_DB:
                            System.out.println("Entro 7");
                            PhysicalMachine physicalMachine = physicalMachineServices.getPhysicalMachineByName("ISC201");
                            System.out.println("Nombre: "+physicalMachine.getPhysicalMachineName() );
                            /*
                            clouderServerOperationResult = "TURN_ON_DB";
                            System.out.println(clouderServerOperationResult);
                            
                                PhysicalMachine physicalMachine=null;
                                System.out.println("Entro 7");
                                if(physicalMachineServices.getPhysicalMachineByName("ISC201")!=null)
                                {
                                physicalMachine = physicalMachineServices.getPhysicalMachineByName(physicalMachineName);
                                System.out.println("Nombre: " + physicalMachine.getPhysicalMachineName());
                                System.out.println("Codigo: " + physicalMachine.getPhysicalMachineCode());
                                physicalMachine.setPhysicalMachineState(ON_STATE);
                                physicalMachineServices.updatePhysicalMachine(physicalMachine);
                                }
                                else
                                {
                                    System.out.println("CONSULTA NULA");
                                }
                                */
                            break;

                        case TURN_OFF_DB:
                            clouderServerOperationResult = "TURN_OFF_DB";
                            if (clouderServerRequestSplitted.length > 4) {
                                System.out.println(clouderServerOperationResult);
                            } else {
                                clouderServerOperationResult += ERROR_MESSAGE + "invalid number of parameters: " + clouderServerRequestSplitted.length;
                            }
                            break;

                        case LOGIN_DB:
                            clouderServerOperationResult = "LOGIN_DB";
                            if (clouderServerRequestSplitted.length > 4) {
                                System.out.println(clouderServerOperationResult);
                            } else {
                                clouderServerOperationResult += ERROR_MESSAGE + "invalid number of parameters: " + clouderServerRequestSplitted.length;
                            }
                            break;

                        case LOGOUT_DB:
                            clouderServerOperationResult = "LOGOUT_DB";
                            if (clouderServerRequestSplitted.length > 4) {
                                System.out.println(clouderServerOperationResult);
                            } else {
                                clouderServerOperationResult += ERROR_MESSAGE + "invalid number of parameters: " + clouderServerRequestSplitted.length;
                            }
                            break;

                        default:
                            clouderServerOperationResult += ERROR_MESSAGE + "The Clouder Client database operation is invalid: " + databaseOperationType;
                            System.err.println(clouderServerOperationResult);
                    }
                } else if (operationDomain == UnaCloudAbstractMessage.REGISTRATION_OPERATION) {
                    System.out.println("The Clouder Server operation request is registration type");
                    int registrationOperationType = 0;
                    if (clouderServerRequestSplitted.length > 1) {
                        if (clouderServerRequestSplitted[1] != null && !clouderServerRequestSplitted[1].equals("")) {
                            registrationOperationType = Integer.parseInt(clouderServerRequestSplitted[1]);
                        }
                    }
                    switch (registrationOperationType) {
                        case NEW_REG:
                            clouderServerOperationResult = "NEW_REG";
                            System.out.println(clouderServerOperationResult);
                            break;

                        case UPDATE_REG:
                            clouderServerOperationResult = "UPDATE_REG";
                            System.out.println(clouderServerOperationResult);
                            break;

                        case ERASE_REG:
                            clouderServerOperationResult = "ERASE_REG";
                            System.out.println(clouderServerOperationResult);
                            break;

                        default:
                            clouderServerOperationResult += ERROR_MESSAGE + "The Clouder Client registration operation is invalid: " + registrationOperationType;
                            System.err.println(clouderServerOperationResult);
                    }
                } else {
                    clouderServerOperationResult += ERROR_MESSAGE + "The Clouder Client request type is invalid: " + operationDomain;
                    System.err.println(clouderServerOperationResult);
                }
            } else {
                clouderServerOperationResult += ERROR_MESSAGE + "The Clouder Client request is null or an invalid number: " + operationDomain;
                System.err.println(clouderServerOperationResult);
            }
            communication.writeUTF(clouderServerOperationResult);
            communication.close();
        } catch (Exception ex) {
            System.err.println("The communication process with Clouder Client failed in ClouderClientAttentionThread: " + ex.getMessage());
        }
    }

    /**
     * @return the physicalMachineServices
     */
    public IPhysicalMachineServices getPhysicalMachineServices() {
        return physicalMachineServices;
    }

    /**
     * @param physicalMachineServices the physicalMachineServices to set
     */
    public void setPhysicalMachineServices(IPhysicalMachineServices physicalMachineServices) {
        this.physicalMachineServices = physicalMachineServices;
    }
}//end of ClouderClientAttentionThread

