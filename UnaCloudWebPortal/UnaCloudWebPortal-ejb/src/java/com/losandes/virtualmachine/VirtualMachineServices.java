package com.losandes.virtualmachine;

import com.losandes.communication.messages.UnaCloudAbstractMessage;
import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.SecureSocket;
import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.persistence.entity.VirtualMachineExecution;
import com.losandes.user.IUserServices;
import com.losandes.utils.Queries;
import com.losandes.virtualmachineexecution.IVirtualMachineExecutionServices;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import static com.losandes.utils.Constants.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for exposing the Virtual Machine persistence services
 */
@Stateless
public class VirtualMachineServices implements IVirtualMachineServices {

    @EJB
    private IPersistenceServices persistenceServices;
    @EJB(beanName = "UserServices")
    private IUserServices userServices;
    @EJB
    private IVirtualMachineServices virtualMachineServices;
    @EJB
    private VirtualMachineOperationsLocal virtualMachineOperations;
    @EJB
    private IVirtualMachineExecutionServices ivmes;
    /**
     * Responsible for exposing the Virtual Machine create persistence services
     */
    @Override
    public boolean createVirtualMachine(VirtualMachine virtualMachine) {
        //return true;
        return persistenceServices.create(virtualMachine);
    }

    /**
     * Responsible for exposing the Virtual Machine Execution update persistence services
     */
    @Override
    public boolean updateVirtualMachineExecution(VirtualMachineExecution virtualMachineExecution) {
        return persistenceServices.update(virtualMachineExecution)!=null;
    }

    /**
     * Responsible for exposing the Virtual Machine update persistence services
     */
    @Override
    public boolean updateVirtualMachine(VirtualMachine virtualMachine) {
        System.out.println("updateVirtualMachine");
        return persistenceServices.update(virtualMachine)!=null;
    }

    /**
     * Responsible for exposing the Virtual Machine delete persistence services
     */
    @Override
    public boolean deleteVirtualMachine(int code) {
        //return true;
        VirtualMachine virtualMachine = getVirtualMachineByID(code);
        return persistenceServices.delete(virtualMachine);
    }

    /**
     * Responsible for exposing the Virtual Machine query by id
     */
    @Override
    public VirtualMachine getVirtualMachineByID(int VirtualMachineCode) {
        //return null;
        return (VirtualMachine) persistenceServices.findById(VirtualMachine.class, VirtualMachineCode);
    }

    /**
     * Responsible for exposing all the Virtual Machines available
     */
    @Override
    public List getVirtualMachines() {
        return persistenceServices.findAll(VirtualMachine.class);
    }

    @Override
    public String turnOffVirtualMachineCluster(List<String> virtualMachineExecution) {
        String result = "";
        for (String vmeId : virtualMachineExecution){
            VirtualMachineExecution vme=(VirtualMachineExecution)persistenceServices.findById(VirtualMachineExecution.class,vmeId);
            if(vme.getVirtualMachineExecutionStatus()==ON_STATE||vme.getVirtualMachineExecutionStatus()==ERROR_STATE){
                result = turnOffVirtualMachine(vme);
                vme.setVirtualMachineExecutionStatus(OFF_STATE);
                vme.setVirtualMachineExecutionStop(new Date());
                vme.getVirtualMachine().setVirtualMachineState(OFF_STATE);
                persistenceServices.update(vme);
                persistenceServices.update(vme.getVirtualMachine());
            }
        }
        return result;
    }

    @Override
    public String restartVirtualMachineCluster(List<VirtualMachineExecution> virtualMachineExecution) {
        String result = "";
        for (VirtualMachineExecution vme : virtualMachineExecution)if(vme.getVirtualMachineExecutionStatus()==ON_STATE){
            result = restartVirtualMachine(vme.getVirtualMachine());
        }
        return result;
    }

    /**
     * Responsible for extending a Virtual Machines execution time
     */
    @Override
    public String extendVirtualMachineExecutionTime(int executionTime, VirtualMachineExecution... virtualMachineExecutions) {
        System.out.println("extendVirtualMachineExecutionTime "+executionTime );
        String result = "";
        if (virtualMachineExecutions != null) {
            for (VirtualMachineExecution vme : virtualMachineExecutions) {
                System.out.println(vme.getVirtualMachine().getVirtualMachineName());
                try {
                    SecureSocket ss = new SecureSocket(vme.getVirtualMachine().getPhysicalMachine().getPhysicalMachineIP(), persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
                    AbstractCommunicator communication = ss.connect();
                    communication.writeUTF("" + UnaCloudAbstractMessage.VIRTUAL_MACHINE_OPERATION, VM_TIME + "", "" + vme.getVirtualMachine().getVirtualMachineCode(), "" + executionTime);
                    communication.close();
                    vme.setVirtualMachineExecutionTime(new Date(vme.getVirtualMachineExecutionTime().getTime() + (((long) executionTime) * 60l * 60000l)));
                    persistenceServices.update(vme);
                } catch (Exception e) {
                    e.printStackTrace();
                }   
            }
        } else {
            result = ERROR_MESSAGE;
        }
        if (!result.contains(ERROR_MESSAGE)) {
            result = SUCCESSFUL_OPERATION;
        } else {
            result = UNSUCCESSFUL_OPERATION;
        }

        return result;
    }

    /**
     * Responsible for exposing all the Virtual Machine Hard Disks available for execution
     */
    @Override
    public List getAvailableVirtualHardDisk(int templateSelected) {
        return persistenceServices.executeNativeSQLList(Queries.getVirtualHardDiskAvailable(templateSelected), null);
    }

    /**
     * Responsible for exposing all the Grid Virtual Machine Hard Disks available for execution
     */
    @Override
    public List getGridAvailableVirtualHardDisk(int templateSelected, String systemUserName) {
        return persistenceServices.executeNativeSQLList(Queries.getGridVirtualHardDiskAvailable(templateSelected, systemUserName), null);
    }

    /**
     * Responsible for exposing all the Virtual Machines available for execution
     */
    @Override
    public List getAvailableVirtualMachines(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM) {
        if (virtualMachineCores == 0 || virtualMachineDisk == 0 || virtualMachineRAM == 0) {
            return persistenceServices.executeNativeSQLList(Queries.getFastVirtualMachineAvailable(templateSelected), VirtualMachine.class);
        }
        return persistenceServices.executeNativeSQLList(Queries.getVirtualMachineAvailable(templateSelected, virtualMachineDisk, virtualMachineCores, virtualMachineRAM), VirtualMachine.class);
    }

    /**
     * Responsible for exposing all the Grid Virtual Machines available for execution
     */
    @Override
    public List getGridAvailableVirtualMachines(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM, String systemUserName) {
        if (virtualMachineCores == 0 || virtualMachineDisk == 0 || virtualMachineRAM == 0) {
            return persistenceServices.executeNativeSQLList(Queries.getFastGridVirtualMachineAvailable(templateSelected, systemUserName), VirtualMachine.class);
        }
        return persistenceServices.executeNativeSQLList(Queries.getGridVirtualMachineAvailable(templateSelected, virtualMachineDisk, virtualMachineCores, virtualMachineRAM, systemUserName), VirtualMachine.class);
    }

    /**
     * Responsible for exposing all the Virtual Machines executions for a System User
     */
    @Override
    public List getVirtualMachineExecutions(String systemUserName) {
        return persistenceServices.executeNativeSQLList(Queries.getConsistentVirtualMachines(systemUserName), VirtualMachineExecution.class);
    }

    /**
     * Responsible for exposing all the Virtual Machines executions for a System User an a Virtual Machine Code
     */
    @Override
    public VirtualMachineExecution getVirtualMachineExecution(String systemUserName, int virtualMachineCode) {
        List<VirtualMachineExecution> virtualMachineExecutions = getVirtualMachineExecutions(systemUserName);
        VirtualMachineExecution virtualMachineExecution = null;
        for (int i = 0; i < virtualMachineExecutions.size(); i++) {
            if (virtualMachineExecutions.get(i).getVirtualMachine().getVirtualMachineCode() == virtualMachineCode) {
                virtualMachineExecution = virtualMachineExecutions.get(i);
                i = virtualMachineExecutions.size();
            }
        }
        return virtualMachineExecution;
    }

    /**
     * Responsible for turning off a Virtual Machine Execution
     */
    public void setTurnOffVirtualMachineExecution(String systemUserName, int virtualMachineCode) {
        List<VirtualMachineExecution> virtualMachineExecutions = getVirtualMachineExecutions(systemUserName);
        for (int i = 0; i < virtualMachineExecutions.size(); i++) {
            if (virtualMachineExecutions.get(i).getVirtualMachine().getVirtualMachineCode() == virtualMachineCode) {
                virtualMachineExecutions.get(i).setVirtualMachineExecutionStatus(OFF_STATE);
                virtualMachineExecutions.get(i).setVirtualMachineExecutionStop(new Date());
                virtualMachineExecutions.get(i).getVirtualMachine().setVirtualMachineState(OFF_STATE);
                virtualMachineExecutions.get(i).getVirtualMachine().getPhysicalMachine().setPhysicalMachineState(ON_STATE);
                persistenceServices.update(virtualMachineExecutions.get(i));
            }
        }
    }

    @Override
    public void setVirtualMachineExecutionState(int executionId, int state, String message) {
        VirtualMachineExecution vme = (VirtualMachineExecution) persistenceServices.findById(VirtualMachineExecution.class, executionId);
        vme.setVirtualMachineExecutionStatus(state);
        vme.setVirtualMachineExecutionStatusMessage(message);
        persistenceServices.update(vme);
    }

    private String restartVirtualMachine(VirtualMachine virMac) {
        PhysicalMachine phyMac = virMac.getPhysicalMachine();
        try {
            SecureSocket socket = new SecureSocket(phyMac.getPhysicalMachineIP(), persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF(UnaCloudAbstractMessage.VIRTUAL_MACHINE_OPERATION + MESSAGE_SEPARATOR_TOKEN + VM_RESTART + MESSAGE_SEPARATOR_TOKEN + virMac.getVirtualMachineCode() + MESSAGE_SEPARATOR_TOKEN + virMac.getHypervisor().getHypervisorCode() + MESSAGE_SEPARATOR_TOKEN + virMac.getVirtualMachinePath() + MESSAGE_SEPARATOR_TOKEN + phyMac.getPhysicalMachineHypervisorPath());
            communication.close();
            return "";
        } catch (ConnectionException ex) {
            return ERROR_MESSAGE;
        }
    }

    private String turnOffVirtualMachine(VirtualMachineExecution vme) {
        VirtualMachine virMac=vme.getVirtualMachine();
        PhysicalMachine phyMac = virMac.getPhysicalMachine();
        System.out.println("TURN OFF " + phyMac.getPhysicalMachineName());
        try {
            SecureSocket socket = new SecureSocket(phyMac.getPhysicalMachineIP(), persistenceServices.getIntValue("CLOUDER_CLIENT_PORT"));
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF("" + UnaCloudAbstractMessage.VIRTUAL_MACHINE_OPERATION, "" + VM_TURN_OFF, "" + vme.getVirtualMachineExecutionCode(), "" + virMac.getHypervisor().getHypervisorCode(), virMac.getVirtualMachinePath(), phyMac.getPhysicalMachineHypervisorPath());
            communication.close();
            return "";
        } catch (ConnectionException ex) {
        }
        return ERROR_MESSAGE;
    }

    @Override
    public String turnOnVirtualCluster(List<PhysicalMachine> physicalMachines, int executionTime, int templateSelected, int vmCores, int HDsize, int vmRAM, String userName) {
        System.out.println("turnOnVirtualCluster");
        for (PhysicalMachine pm : physicalMachines) {
            System.out.println("PM " + pm.getPhysicalMachineName());
        }
        List<VirtualMachine> vms = virtualMachineServices.getGridAvailableVirtualMachines(templateSelected, HDsize, vmCores, vmRAM, userName);
        List<VirtualMachine> toTurnOn = new ArrayList<VirtualMachine>();
        for (VirtualMachine vm : vms) {
            for (PhysicalMachine pm : physicalMachines) {
                if (pm.getPhysicalMachineName().equals(vm.getPhysicalMachine().getPhysicalMachineName())) {
                    toTurnOn.add(vm);
                }
            }
        }
        System.out.println("vms " + vms.size());
        for (VirtualMachine vm : vms) {
            System.out.println("VM " + vm.getVirtualMachineName() + " " + vm.getPhysicalMachine().getPhysicalMachineName());
        }
        System.out.println("toTurnOn " + toTurnOn.size());
        virtualMachineOperations.turnOnCluster(vmCores, vmRAM, executionTime, userServices.getUserByID(userName), toTurnOn.toArray(new VirtualMachine[0]));
        return "";
    }

    @Override
    public void writeFileOnVirtualMachine(String virtualMachineExecutionId,String path,String content){
        virtualMachineOperations.writeFileOnVirtualMachine(virtualMachineExecutionId, path, content);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public VirtualMachineExecution[] turnOnVirtualClusterBySize(int template, int executionTime, int numberInstances, int vmCores, int HDsize, int vmRAM, String userName,boolean retry) {
        List<VirtualMachine> vms = getAvailableVirtualMachines(template, HDsize, vmCores, vmRAM);
        VirtualMachine[] avms = new VirtualMachine[Math.min(numberInstances, vms.size())];
        for (int e = 0; e < avms.length; e++)avms[e] = vms.get(e);
        if(retry&&numberInstances-avms.length>0)virtualMachineOperations.turnOnPhysicalMachines(template, executionTime, numberInstances-avms.length, vmCores, HDsize, vmRAM, userName);
        return virtualMachineOperations.turnOnCluster(vmCores, vmRAM, executionTime, userServices.getUserByID(userName), avms);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public VirtualMachineExecution[] turnOnVirtualClusterBySizeAndUsers(int template, int executionTime, int numberInstances, int vmCores, int HDsize, int vmRAM, String userName,int usedPms,int notUsedPms){
        
        List<VirtualMachine> vms = getAvailableVirtualMachines(template, HDsize, vmCores, vmRAM);
        {
            Collections.sort(vms,new Comparator<VirtualMachine>(){

                @Override
                public int compare(VirtualMachine o1, VirtualMachine o2) {
                    return o1.getVirtualMachineName().compareTo(o2.getVirtualMachineName());
                }
            });
            String h="";
            for(VirtualMachine vm:vms)h+=(vm.getVirtualMachineName()+", ");
            System.out.println(h);
            List<VirtualMachine> withUser=new ArrayList<VirtualMachine>();
            List<VirtualMachine> withNoUser=new ArrayList<VirtualMachine>();
            for(VirtualMachine vm:vms)(vm.getPhysicalMachine().getPhysicalMachineUser()==null?withNoUser:withUser).add(vm);
            int total=vms.size();
            vms.clear();
            if(withUser.isEmpty()||usedPms<=0){
                vms.addAll(withNoUser);
                vms.addAll(withUser);
            }else if(withNoUser.isEmpty()||notUsedPms<=0){
                vms.addAll(withUser);
                vms.addAll(withNoUser);
            }else{
                for(int e=0,u=0,nu=0;e<total;e++){
                    for(int i=0;i<usedPms&&u<withUser.size();i++)vms.add(withUser.get(u++));
                    for(int i=0;i<notUsedPms&&nu<withNoUser.size();i++)vms.add(withNoUser.get(nu++));
                }
            }
            
        }
        //String h="";
        //    for(VirtualMachine vm:vms)h+=(vm.getVirtualMachineName()+", ");
        //    System.out.println(h);
        VirtualMachine[] avms = new VirtualMachine[Math.min(numberInstances, vms.size())];
        for (int e = 0; e < avms.length; e++)avms[e] = vms.get(e);
        //h="";
         //   for(VirtualMachine vm:avms)h+=(vm.getVirtualMachineName()+", ");
          //  System.out.println(h);
        if(numberInstances-avms.length>0)virtualMachineOperations.turnOnPhysicalMachines(template, executionTime, numberInstances-avms.length, vmCores, HDsize, vmRAM, userName);
        return virtualMachineOperations.turnOnCluster(vmCores, vmRAM, executionTime, userServices.getUserByID(userName), avms);
    }

    @Override
    public List<VirtualMachine> getAllOnVirtualMachines() {
        return persistenceServices.executeNativeSQLList(Queries.getAllOnVirtualMacnines(), VirtualMachine.class);
    }

    @Override
    @RolesAllowed("Administrator")
    public List getAllVirtualMachineExecutions() {
        System.out.println("getAllVirtualMachineExecutions");
        return persistenceServices.executeNativeSQLList(Queries.getAllVirtualMachinesExecutions(), VirtualMachineExecution.class);
    }

    
}// end of VirtualMachineServices

