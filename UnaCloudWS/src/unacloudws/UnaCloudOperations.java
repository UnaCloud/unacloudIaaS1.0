/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package unacloudws;

/**
 *
 * @author Clouder
 */
public class UnaCloudOperations {

    public static java.util.List<com.losandes.wss.VirtualMachineExecutionWS> getVirtualMachineExecutions(java.lang.String username, java.lang.String pass, int templateID) {
        com.losandes.wss.UnaCloudWSService service = new com.losandes.wss.UnaCloudWSService();
        com.losandes.wss.UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getVirtualMachineExecutions(username, pass, templateID);
    }

    public static Integer getAvailableVirtualMachines(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM, java.lang.String user, java.lang.String password) {
        com.losandes.wss.UnaCloudWSService service = new com.losandes.wss.UnaCloudWSService();
        com.losandes.wss.UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getAvailableVirtualMachines(templateSelected, virtualMachineDisk, virtualMachineCores, virtualMachineRAM, user, password);
    }

    public static java.util.List<com.losandes.wss.VirtualMachineExecutionWS> turnOnVirtualCluster(java.lang.String username, java.lang.String pass, int templateID, int size, int ram, int cores, int hdSize, int time) {
        com.losandes.wss.UnaCloudWSService service = new com.losandes.wss.UnaCloudWSService();
        com.losandes.wss.UnaCloudWS port = service.getUnaCloudWSPort();
        return port.turnOnVirtualCluster(username, pass, templateID, size, ram, cores, hdSize, time);
    }

    public static String turnOffVirtualMachine(java.lang.String username, java.lang.String pass, java.lang.String virtualMachineExID) {
        com.losandes.wss.UnaCloudWSService service = new com.losandes.wss.UnaCloudWSService();
        com.losandes.wss.UnaCloudWS port = service.getUnaCloudWSPort();
        return port.turnOffVirtualMachine(username, pass, virtualMachineExID);
    }

    public static java.util.List<com.losandes.wss.TemplateWS> getTemplateLists(java.lang.String username, java.lang.String pass) {
        com.losandes.wss.UnaCloudWSService service = new com.losandes.wss.UnaCloudWSService();
        com.losandes.wss.UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTemplateLists(username, pass);
    }

    public static Integer getTotalUnaCloudResources(int machineDisk, int machineCores, int machineRam) {
        com.losandes.wss.UnaCloudWSService service = new com.losandes.wss.UnaCloudWSService();
        com.losandes.wss.UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTotalUnaCloudResources(machineDisk, machineCores, machineRam);
    }

    public static Integer getAvailableUnaCloudResources(int machineDisk, int machineCores, int machineRam) {
        com.losandes.wss.UnaCloudWSService service = new com.losandes.wss.UnaCloudWSService();
        com.losandes.wss.UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getAvailableUnaCloudResources(machineDisk, machineCores, machineRam);
    }

    public static Integer getTotalVirtualMachines(int machineDisk, int machineCores, int machineRam, int templateCode, java.lang.String username, java.lang.String pass) {
        com.losandes.wss.UnaCloudWSService service = new com.losandes.wss.UnaCloudWSService();
        com.losandes.wss.UnaCloudWS port = service.getUnaCloudWSPort();
        return port.getTotalVirtualMachines(machineDisk, machineCores, machineRam, templateCode, username, pass);
    }


    
    

}
