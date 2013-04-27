/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unacloudws;

import development.TemplateWS;
import java.util.List;
import development.VirtualMachineExecutionWS;


/**
 *
 * @author Clouder
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        List<TemplateWS> ts=UnaCloudOperationsTest.getTemplateLists("ga.sotelo69", "asdasdasd");
        for(TemplateWS t:ts)System.out.println(t.getTemplateName()+" "+t.getTemplateCode());
        //UnaCloudOperations.turnOnVirtualCluster("ga.sotelo69", "asdasdasd",63,20,0,0,0,24*3);
        for(VirtualMachineExecutionWS vme:UnaCloudOperationsTest.getVirtualMachineExecutions("ga.sotelo69", "asdasdasd",63)){
            System.out.println(vme.getVirtualMachineExecutionCode());
            UnaCloudOperationsTest.writeFileOnVirtualMachine("ga.sotelo69", "asdasdasd",vme.getVirtualMachineExecutionCode(),"/unacloudhello222","Hoa");
        }
        /*System.out.println(UnaCloudOperations.getTotalUnaCloudResources(20,4,1024));
        System.out.println(UnaCloudOperations.getAvailableUnaCloudResources(20,4,1024));*/

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
