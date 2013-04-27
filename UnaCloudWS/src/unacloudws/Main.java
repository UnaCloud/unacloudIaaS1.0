/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unacloudws;

import com.losandes.wss.TemplateWS;
import com.losandes.wss.VirtualMachineExecutionWS;
import java.util.List;


/**
 *
 * @author Clouder
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(UnaCloudOperations.getTotalUnaCloudResources(20,4,1024));
        System.out.println(UnaCloudOperations.getAvailableUnaCloudResources(20,4,1024));
        /*List<TemplateWS> ts=UnaCloudOperations.getTemplateLists("fh.castillo27", "tyffilygiogyi");
        for(TemplateWS t:ts)System.out.println(t.getTemplateName()+" "+t.getTemplateCode());
        Integer a=UnaCloudOperations.getAvailableVirtualMachines(48,0,0,0,"fh.castillo27","tyffilygiogyi");
        if(a!=null){
            UnaCloudOperations.turnOnVirtualCluster("fh.castillo27", "tyffilygiogyi",48,a/2,0,0,0,24*3);
        }*/

        /*List<VirtualMachineExecutionWS> l=UnaCloudOperations.getVirtualMachineExecutions("fh.castillo27", "tyffilygiogyi",48);
        for(VirtualMachineExecutionWS vme:l){
            //UnaCloudOperations.turnOffVirtualMachine("fh.castillo27", "tyffilygiogyi",vme.getVirtualMachineExecutionCode());
            System.out.println(vme.getVirtualMachineName()+" "+vme.getVirtualMachineExecutionIP()+" "+vme.getVirtualMachineExecutionStatusMessage()+" "+vme.getVirtualMachineExecutionStatus());
        }*/
    }

    
}
