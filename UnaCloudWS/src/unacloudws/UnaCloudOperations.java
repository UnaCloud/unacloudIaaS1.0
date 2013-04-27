/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unacloudws;

import production.*;

/**
 *
 * @author Clouder
 */
public class UnaCloudOperations {

    public static java.util.List<VirtualMachineExecutionWS> getVirtualMachineExecutions(java.lang.String username, java.lang.String pass, int templateID) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getVirtualMachineExecutions(username, pass, templateID);
    }

    public static Integer getAvailableVirtualMachines(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM, java.lang.String user, java.lang.String password) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getAvailableVirtualMachines(templateSelected, virtualMachineDisk, virtualMachineCores, virtualMachineRAM, user, password);
    }

    public static java.util.List<VirtualMachineExecutionWS> turnOnVirtualCluster(java.lang.String username, java.lang.String pass, int templateID, int size, int ram, int cores, int hdSize, int time) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.turnOnVirtualCluster(username, pass, templateID, size, ram, cores, hdSize, time);
    }

    public static String turnOffVirtualMachine(java.lang.String username, java.lang.String pass, java.lang.String virtualMachineExID) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.turnOffVirtualMachine(username, pass, virtualMachineExID);
    }

    public static java.util.List<TemplateWS> getTemplateLists(java.lang.String username, java.lang.String pass) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTemplateLists(username, pass);
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

    public static Integer getTotalVirtualMachines(int machineDisk, int machineCores, int machineRam, int templateCode, java.lang.String username, java.lang.String pass) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTotalVirtualMachines(machineDisk, machineCores, machineRam, templateCode, username, pass);
    }

    public static Integer getBusyUnaCloudResources(int machineDisk, int machineCores, int machineRam) {
        UnaCloudWSService service = new UnaCloudWSService();
        UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getBusyUnaCloudResources(machineDisk, machineCores, machineRam);
    }

    
    
    

}
