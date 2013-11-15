/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unacloudws;

import com.losandes.unacloudws.development.*;


/**
 *
 * @author Clouder
 */
public class UnaCloudOperationsTest {

    String username;
    String password;
    public UnaCloudOperationsTest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    
    public java.util.List<VirtualMachineExecutionWS> getVirtualMachineExecutions(int templateID) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getVirtualMachineExecutions(username, password, templateID);
    }

    public Integer getAvailableVirtualMachines(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getAvailableVirtualMachines(templateSelected, virtualMachineDisk, virtualMachineCores, virtualMachineRAM, username, password);
    }

    public java.util.List<VirtualMachineExecutionWS> turnOnVirtualCluster(int templateID, int size, int ram, int cores, int hdSize, int time) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.turnOnVirtualCluster(username, password, templateID, size, ram, cores, hdSize, time);
    }

    public String turnOffVirtualMachine(java.lang.String virtualMachineExID) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.turnOffVirtualMachine(username, password, virtualMachineExID);
    }
    public  java.util.List<TemplateWS> getTemplateLists() {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTemplateLists(username, password);
    }

    public static Integer getTotalUnaCloudResources(int machineDisk, int machineCores, int machineRam) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTotalUnaCloudResources(machineDisk, machineCores, machineRam);
    }

    public static Integer getAvailableUnaCloudResources(int machineDisk, int machineCores, int machineRam) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getAvailableUnaCloudResources(machineDisk, machineCores, machineRam);
    }

    public Integer getTotalVirtualMachines(int machineDisk, int machineCores, int machineRam, int templateCode) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTotalVirtualMachines(machineDisk, machineCores, machineRam, templateCode, username, password);
    }

    public static Integer getBusyUnaCloudResources(int machineDisk, int machineCores, int machineRam) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getBusyUnaCloudResources(machineDisk, machineCores, machineRam);
    }

    
    
    

}
